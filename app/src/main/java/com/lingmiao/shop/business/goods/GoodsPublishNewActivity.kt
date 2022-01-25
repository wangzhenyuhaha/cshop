package com.lingmiao.shop.business.goods

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.text.Editable
import android.text.TextWatcher
import androidx.lifecycle.lifecycleScope
import com.blankj.utilcode.util.ActivityUtils
import com.james.common.base.BaseActivity
import com.james.common.netcore.networking.http.core.HiResponse
import com.james.common.utils.exts.*
import com.lingmiao.shop.R
import com.lingmiao.shop.base.CommonRepository
import com.lingmiao.shop.business.common.bean.FileResponse
import com.lingmiao.shop.business.common.pop.MediaMenuPop
import com.lingmiao.shop.business.goods.adapter.SimpleAdapter
import com.lingmiao.shop.business.goods.api.bean.*
import com.lingmiao.shop.business.goods.api.request.DeliveryRequest
import com.lingmiao.shop.business.goods.config.GoodsConfig
import com.lingmiao.shop.business.goods.presenter.GoodsPublishNewPre
import com.lingmiao.shop.business.goods.presenter.impl.GoodsPublishPreNewImpl
import com.lingmiao.shop.business.photo.PhotoHelper
import com.lingmiao.shop.util.GlideUtils
import com.lingmiao.shop.util.initAdapter
import com.luck.picture.lib.entity.LocalMedia
import kotlinx.android.synthetic.main.goods_activity_publish_new.*
import kotlinx.android.synthetic.main.goods_adapter_goods_gallery.*
import kotlinx.android.synthetic.main.goods_include_publish_section1.*
import kotlinx.android.synthetic.main.goods_include_publish_section2.*
import kotlinx.android.synthetic.main.goods_include_publish_section3_new.*
import kotlinx.android.synthetic.main.goods_include_publish_section4_new.*
import kotlinx.android.synthetic.main.goods_include_publish_section5.*
import kotlinx.android.synthetic.main.goods_include_publish_section6.*
import kotlinx.android.synthetic.main.goods_include_publish_section8.*
import kotlinx.android.synthetic.main.goods_include_publish_section_package.*
import kotlinx.android.synthetic.main.goods_include_publish_section_v_time.*
import kotlinx.coroutines.*
import java.io.File

/**
 * Author : Elson
 * Date   : 2020/7/25
 * Desc   : 添加商品页面
 */

@SuppressLint("NotifyDataSetChanged")
class GoodsPublishNewActivity : BaseActivity<GoodsPublishNewPre>(), GoodsPublishNewPre.PublishView {

    companion object {
        //商品good_id
        const val KEY_GOODS_ID = "KEY_GOODS_ID"

        //是否从扫码处跳转
        private const val KEY_SCAN = "KEY_SCAN"

        //扫码后获得的条形码
        private const val KEY_SCAN_CODE = "KEY_SCAN_CODE"

        //保存的商品图片地址
        private const val KEY_PICTURE_ADDRESS = "KEY_PICTURE_ADDRESS"

        //商品缩略图
        private const val KEY_GOODS_THUMBNAIL = "KEY_GOODS_THUMBNAIL"

        //商品价格
        private const val KEY_GOODS_PRICE = "KEY_GOODS_PRICE"

        //商品库存 quantity
        private const val KEY_GOODS_QUANTITY = "KEY_GOODS_QUANTITY"

        //商品分类ID
        private const val KEY_GOODS_CATEGORYID = "KEY_GOODS_CATEGORYID"

        //商品分类name
        private const val KEY_GOODS_CATEGORYNAME = "KEY_GOODS_CATEGORYNAME"

        //商品菜单
        private const val KEY_GOODS_SHOPCATID = "KEY_GOODS_SHOPCATID"

        //商品菜单name
        private const val KEY_GOODS_SHOPCATNAME = "KEY_GOODS_SHOPCATNAME"

        //是否菜单分类是从中心库中所来
        private const val IS_FROM_CENTER = "IS_FROM_CENTER"

        const val REQUEST_CODE_VIDEO = 1000
        const val REQUEST_CODE_DELIVERY = 1001
        const val REQUEST_CODE_SKU = 1002
        const val REQUEST_CODE_DESC = 1003
        const val REQUEST_CODE_INFO = 1004

        //编辑已有商品或者中心库中已有商品
        fun openActivity(
            context: Context,
            goodsId: String?,
            scan: Boolean = false,
            thumbnail: String? = null,
            price: String? = null,
            quantity: String? = null,
            categoryId: String? = null,
            categoryName: String? = null,
            shopCatId: String? = null,
            shopCatName: String? = null,
            isFromCenter: Int? = null
        ) {
            val intent = Intent(context, GoodsPublishNewActivity::class.java)
            intent.putExtra(KEY_GOODS_ID, goodsId)
            intent.putExtra(KEY_SCAN, scan)
            intent.putExtra(KEY_GOODS_THUMBNAIL, thumbnail)
            intent.putExtra(KEY_GOODS_PRICE, price)
            intent.putExtra(KEY_GOODS_QUANTITY, quantity)
            intent.putExtra(KEY_GOODS_CATEGORYID, categoryId)
            intent.putExtra(KEY_GOODS_CATEGORYNAME, categoryName)
            intent.putExtra(KEY_GOODS_SHOPCATID, shopCatId)
            intent.putExtra(KEY_GOODS_SHOPCATNAME, shopCatName)
            intent.putExtra(IS_FROM_CENTER, isFromCenter)
            context.startActivity(intent)
        }

        //处理新增商品
        fun newPublish(
            context: Context,
            scan: Boolean = false,
            scanCode: String = "",
            pictureAddress: String = ""
        ) {
            val intent = Intent(context, GoodsPublishNewActivity::class.java)
            intent.putExtra(KEY_SCAN, scan)
            intent.putExtra(KEY_SCAN_CODE, scanCode)
            intent.putExtra(KEY_PICTURE_ADDRESS, pictureAddress)
            context.startActivity(intent)
        }
    }

    //是否从中心库复制而来的商品分类，菜单(该页面默认不是来自中心库)
    private var isFromCenter = 0

    // 编辑商品的商品ID
    private var goodsId: String? = null

    //是否从扫码处跳转 true表示是，是否禁用多规格,true表示禁用
    private var scan: Boolean = false

    //是否启用根据商品名的模糊查询，scan为true。goods_id为null启用,此时表示扫码条形码或者搜索条形码结果没有
    //查询到商品，跳转到该页面，这时还需要将商品的图片和条形码以及返回的商品ID上传到后台
    private var searchGoods: Boolean = false

    //图片的保存地址
    private var pictureAddress: String = ""

    //商品的条形码
    private var scanCode: String = ""

    //是否是虚拟商品  1  虚拟商品   0   实体商品    2券包
    private var isVirtualGoods = 0

    //底部按钮是否展开   useless
    private var isExpand = false

    //添加/编辑商品 的数据实体
    private var goodsVO: GoodsVOWrapper = GoodsVOWrapper()

    //当前的商品名
    private var goodsName: String = ""

    //显示模糊查询商品的RecyclerView
    private var adapter: SimpleAdapter? = null

    //已经提前修改的商品信息
    private var thumbnail: String? = null
    private var price: String? = null
    private var quantity: String? = null
    private var categoryId: String? = null
    private var categoryName: String? = null
    private var shopCatId: String? = null
    private var shopCatName: String? = null


    override fun useLightMode() = false

    override fun getLayoutId() = R.layout.goods_activity_publish_new

    //获取从其他页面传过来的信息
    override fun initBundles() {
        //四种情况
        //1 scan false  goods_id null  添加商品
        //2 scan false  goods_id Not Null  编辑商品
        //3 scan true  goods_id null  中途goods_id可能会变为 Not Null,最后还要变为null   添加商品
        //4 scan true  goods_id not null 编辑完了需要赋值为Null    添加商品
        goodsId = intent.getStringExtra(KEY_GOODS_ID)
        scan = intent.getBooleanExtra(KEY_SCAN, false)
        if (scan && goodsId == null) {
            //这个是扫码时未查询到任何商品的情况，这时要求输入商品名时可以根据商品名查询商品
            searchGoods = true
        }
        //商品的条形码
        scanCode = intent.getStringExtra(KEY_SCAN_CODE) ?: ""
        //如何扫码未查询到商品，会在这里保存商品图片
        pictureAddress = intent.getStringExtra(KEY_PICTURE_ADDRESS) ?: ""

        //如果在扫码之后查询到了商品，可以选择编辑进入编辑页面修改商品信息，并且会把之前页面中的商品信息带过来
        thumbnail = intent.getStringExtra(KEY_GOODS_THUMBNAIL)
        price = intent.getStringExtra(KEY_GOODS_PRICE)
        quantity = intent.getStringExtra(KEY_GOODS_QUANTITY)
        categoryId = intent.getStringExtra(KEY_GOODS_CATEGORYID)
        categoryName = intent.getStringExtra(KEY_GOODS_CATEGORYNAME)
        shopCatId = intent.getStringExtra(KEY_GOODS_SHOPCATID)
        shopCatName = intent.getStringExtra(KEY_GOODS_SHOPCATNAME)
        isFromCenter = intent.getIntExtra(IS_FROM_CENTER, 0)
    }

    override fun createPresenter() = GoodsPublishPreNewImpl(this, this)

    override fun initView() {
        mToolBarDelegate.setMidTitle(if (goodsId.isNotBlank() && !scan) "编辑商品" else "发布商品")

        if (scan) {
            //扫码添加商品时不能
            section4Row6Ll.gone()
        }

        //扫码时未扫描到商品
        if (scan && goodsId == null) {
            goodsScanLayout.visiable()
            inputEdt.text = scanCode
            scanTitle.visiable()
            scanResume.singleClick {
                ActivityUtils.startActivity(GoodsScanActivity::class.java)
                finish()
            }
            scanGoodsName.visiable()
        }

        // 切换商品类型
        goodsTypeTv.singleClick {
            mPresenter.showGoodsType(isVirtualGoods)
        }

        // 商品分类
        goodsCategoryTv.singleClick {
            mPresenter.showCategoryPop()
        }

        //添加新的分类，只是跳转
        cateAddIv.singleClick {
            GoodsCategoryActivity.openActivity(this, 1)
        }

        //菜单管理
        goodsGroupTv.singleClick {
            mPresenter.showGroupPop()
        }

        //商品名称
        //监听商品名称的变化
        if (searchGoods) {

            goodsNameEdt.addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(
                    s: CharSequence?,
                    start: Int,
                    count: Int,
                    after: Int
                ) {
                }

                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                }

                override fun afterTextChanged(s: Editable?) {
                    if (s.toString() == goodsName) {
                        return
                    } else {
                        if (s?.length ?: 0 >= 2) {
                            goodsName = s.toString()
                            mPresenter?.searchGoods(s.toString())
                        } else {
                            searchGoodsLayout.gone()
                        }
                    }

                }

            })
        }
        adapter = SimpleAdapter()
        adapter?.also {
            goodsSearch.initAdapter(it)
        }
        deleteSearchGoods.singleClick {
            searchGoodsLayout.gone()
        }
        adapter?.setOnItemClickListener { adapter, _, position ->
            val item = adapter.data[position] as Data
            goodsName = item.goodsName.toString()
            mPresenter.loadGoodsInfoFromCenter(item.goodsId.toString())
            isFromCenter = 1
            searchGoodsLayout.gone()
        }

        //商品缩略图
        deleteIv.gone()
        imageView.singleClick {
            openGallery()
        }

        //添加主图
        galleryRv.setCountLimit(1, 20)

        //设置多规格
        goodsVirtualSpecTv.singleClick {
            if (goodsVO.categoryId.isNotBlank()) {
                SpecSettingActivity.openActivity(
                    this,
                    REQUEST_CODE_SKU,
                    isVirtualGoods != 0,
                    goodsVO,
                    scan = scan
                )
            } else {
                showToast("请先选择商品分类")
            }
        }

        //实体商品在单规格和多规格之间切换
        switchBtn.setOnCheckedChangeListener { _, isChecked ->
            showSection45WithStatus(isChecked, isExpand)
        }

        //商品详情
        goodsDetailTv.singleClick {
            if (goodsVO.categoryId.isNotBlank()) {
                GoodsDetailActivity.openActivity(this, REQUEST_CODE_INFO, goodsVO)
            } else {
                showToast("请先选择商品分类")
            }
        }

        //保存  0
        saveTv.singleClick {
            clickConfirmView(0)
        }
        //保存并上架  1
        confirmTv.singleClick {
            clickConfirmView(1)
        }

        //加载数据
        if (scan) {
            goodsId?.let { mPresenter.loadGoodsInfoFromCenter(it) }
        } else {
            mPresenter.loadGoodsInfo(goodsId)
        }

        //useless list
        //有效期  useless
        goodsVirtualExpireTv.singleClick {
            mPresenter?.showExpirePop(goodsVO.expiryDay ?: "")
        }
        //使用时间   useless
        goodsVirtualUseTimeTv.singleClick {
            mPresenter?.showUseTimePop(goodsVO.availableDate ?: "")
        }
        //useless
        goodsVideoTv.singleClick {
            GoodsVideoActivity.openActivity(this, REQUEST_CODE_VIDEO, goodsVO.videoUrl)
        }
        //展开更多 useless
        moreTv.singleClick {
            isExpand = !isExpand
            showSection45WithStatus(switchBtn.isChecked, isExpand)
        }

    }

    //切换商品类型
    override fun onSetGoodsType(isVirtual: Boolean) {
        // isVirtualGoods = isVirtual
        //  setVirtualGoodsView()
    }

    override fun onSetGoodsTypeNew(type: String?) {
        when (type) {
            GoodsConfig.GOODS_TYPE_VIRTUAL -> {
                isVirtualGoods = 1
                setVirtualGoodsView()
            }
            GoodsConfig.GOODS_TYPE_NORMAL -> {
                isVirtualGoods = 0
                setVirtualGoodsView()
            }
            GoodsConfig.GOODS_TYPE_TICKET -> {
                isVirtualGoods = 2
                setVirtualGoodsView()
            }
            else -> {

            }
        }
    }

    //选择分类
    override fun onUpdateCategory(categoryId: String?, categoryName: String?) {
        //显示分类名字
        goodsCategoryTv.text = categoryName
        goodsVO.apply {
            //赋值分类ID和Name
            this.categoryId = categoryId
            this.categoryName = categoryName
        }

        if (isFromCenter == 1) {
            goodsVO.shopCatId = null
            goodsVO.shopCatName = null
            goodsGroupTv.text = "请选择"
            isFromCenter = 0
        }

        //菜单未设置
        if (goodsVO.shopCatId.isNullOrEmpty()) {
            //UI上菜单显示分类，但是实际上不传菜单数据
            onUpdateGroup(null, categoryName)
        }
    }

    //选择菜单
    override fun onUpdateGroup(groupId: String?, groupName: String?) {
        goodsGroupTv.text = groupName
        if (groupId != null) {
            goodsVO.apply {
                this.shopCatId = groupId
                this.shopCatName = groupName
            }
        }

        if (isFromCenter == 1) {
            goodsVO.categoryId = null
            goodsVO.categoryName = null
            goodsCategoryTv.text = "请选择商品分类"
            isFromCenter = 0
        }
    }

    //获得商品名列表
    override fun searchGoodsSuccess(list: List<Data>) {
        if (list.isEmpty()) {
            return
        }
        searchGoodsLayout.visiable()
        adapter?.replaceData(list)
        adapter?.notifyDataSetChanged()
    }

    //获得商品数据
    override fun onLoadGoodsSuccess(goodsVO: GoodsVOWrapper, center: Boolean) {
        this.goodsVO = goodsVO
        goodsVO.apply {
            //审核未通过原因
            if (isAuth == 3) {
                //审核未通过
                authFailLayout.visiable()
                goodsAuthMsgTv.text = authMessage
            } else {
                //通过
                authFailLayout.gone()
            }

            //券包相关信息
            if (num != null) {
                ticket_number.setText(num.toString())
            }
            if (couponID != null) {
                ticker_goods_name.text = "已设置"
            }

            //商品类型
            isVirtualGoods = isVirtualGoods()
            when (isVirtualGoods) {
                0 -> {
                    //实体商品
                    goodsVirtualSpecTv.text = if (this.hasMultiSpec() && !scan) "已设置" else "请设置"
                    if (this.hasMultiSpec()) {
                        //goodsSpecTv.text = "已设置"
                        switchBtn.isChecked = true
                        showSection45WithStatus(switchBtn.isChecked, true)
                    } else {
                        eventPriceEdt.setText(eventPrice)
                        eventQuantityEdt.setText(eventQuantity)
                        goodsPriceEdt.setText(price)
                        marketPriceEdt.setText(mktprice)
                        goodsQuantityEdt.setText(quantity)
                        goodsCostEdt.setText(cost)
                        switchBtn.isChecked = false
                    }
                }
                1 -> {
                    //虚拟商品
                    setVirtualGoodsView()
                    goodsVirtualSpecTv.text = if (this.hasMultiSpec() && !scan) "已设置" else "请设置"
                }
                2 -> {
                    //券包
                    eventPriceEdt.setText(eventPrice)
                    eventQuantityEdt.setText(eventQuantity)
                    goodsPriceEdt.setText(price)
                    marketPriceEdt.setText(mktprice)
                    goodsQuantityEdt.setText(quantity)
                    goodsCostEdt.setText(cost)
                    switchBtn.isChecked = false
                }
                else -> {

                }

            }

            //商品分类
            goodsCategoryTv.text = categoryName?.replace("&gt;", "/")

            //商品菜单
            goodsGroupTv.text = if (shopCatName == null)
                categoryName
            else
                shopCatName?.replace("&gt;", "/")

            // 商品名称
            goodsNameEdt.setText(goodsName)

            //商品缩略图
            if (thumbnail.isNotBlank()) {
                GlideUtils.setImageUrl(imageView, goodsVO.thumbnail, R.mipmap.goods_selected_img)
            } else {
                GlideUtils.setImageUrl(
                    imageView,
                    goodsGalleryList?.get(0)?.original,
                    R.mipmap.goods_selected_img
                )
                thumbnail = goodsGalleryList?.get(0)?.original
            }

            //商品主图
            galleryRv.addDataList(goodsGalleryList)

            //商品详情
            if (goodsParamsList?.size ?: 0 > 0) {
                goodsDetailTv.text = "已添加"
            }
            if (!metaDescription.isNullOrEmpty()) {
                goodsDetailTv.text = "已添加"
            }
            if (!intro.isNullOrEmpty()) {
                goodsDetailTv.text = "已添加"
            }

            // 打包费
            goodsPackageFeeEdt.setText(packagePrice.toString())
        }
        if (thumbnail != null) {
            this@GoodsPublishNewActivity.goodsVO.apply {
                thumbnail = this@GoodsPublishNewActivity.thumbnail
                price = this@GoodsPublishNewActivity.price
                quantity = this@GoodsPublishNewActivity.quantity
                categoryId = this@GoodsPublishNewActivity.categoryId
                categoryName = this@GoodsPublishNewActivity.categoryName
                shopCatId = this@GoodsPublishNewActivity.shopCatId
                shopCatName = this@GoodsPublishNewActivity.shopCatName
            }
            //更新UI显示
            GlideUtils.setImageUrl(imageView, this.goodsVO.thumbnail, R.mipmap.goods_selected_img)
            goodsPriceEdt.setText(this.goodsVO.price)
            goodsQuantityEdt.setText(this.goodsVO.quantity)
            //将分类菜单对应的值赋值给页面，不能调用方法，以免重置isFromCenter

            //显示分类名字
            goodsCategoryTv.text = this@GoodsPublishNewActivity.goodsVO.categoryName
            //显示菜单名字
            if (this@GoodsPublishNewActivity.goodsVO.shopCatId.isNullOrEmpty()) {
                //显示分类名字
                goodsGroupTv.text = this@GoodsPublishNewActivity.goodsVO.categoryName
            } else {
                //显示菜单名字
                goodsGroupTv.text = this@GoodsPublishNewActivity.goodsVO.shopCatName
            }

        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (data == null) {
            return
        }
        when (requestCode) {
            // 视频 useless
            REQUEST_CODE_VIDEO -> {
                data.getStringExtra(GoodsVideoActivity.KEY_VIDEO_URL)?.apply {
                    goodsVO.videoUrl = this
                }
                goodsVideoTv.text = if (goodsVO.videoUrl.isNotBlank()) "已添加" else "请选择"
            }
            // 发货方式 useless
            REQUEST_CODE_DELIVERY -> {
                val deliveryVO =
                    data.getSerializableExtra(DeliverySettingActivity.KEY_Delivery) as? DeliveryRequest
                goodsVO.convert(deliveryVO)
                goodsDeliveryTv.text = if (goodsVO.isSelectDeliveryWay()) "已选择" else "请选择"
            }
            // 规格设置
            REQUEST_CODE_SKU -> {
                val specKeyList =
                    data.getSerializableExtra(SpecSettingActivity.KEY_SPEC_KEY_LIST) as? List<SpecKeyVO>
                val skuList =
                    data.getSerializableExtra(SpecSettingActivity.KEY_SKU_LIST) as? List<GoodsSkuVOWrapper>
                goodsVO.apply {
                    this.isAddSpec = true
                    this.skuList = skuList
                    this.specKeyList = specKeyList
                }
                if (isVirtualGoods == 1) {
                    goodsVirtualSpecTv.text = if (skuList.isNullOrEmpty()) "请设置" else "已设置"
                } else if (isVirtualGoods == 0) {
                    goodsSpecTv.text = if (skuList.isNullOrEmpty()) "请设置规格" else "已设置"
                    goodsVirtualSpecTv.text = if (skuList.isNullOrEmpty()) "请设置" else "已设置"
                }
            }
            REQUEST_CODE_INFO -> {
                //
                val infoList =
                    data.getSerializableExtra(GoodsDetailActivity.KEY_ITEM) as? List<GoodsParamVo>
                val des = data.getStringExtra(GoodsDetailActivity.KEY_DESC)
                val images = data.getStringExtra(GoodsDetailActivity.KEY_IMAGES)
                goodsVO.apply {
                    this.goodsParamsList = infoList
                    this.metaDescription = des
                    this.intro = images
                }
                goodsDetailTv.text = if (infoList?.size ?: 0 > 0) "已添加" else "未添加"
                if (!des.isNullOrEmpty()) goodsDetailTv.text = "已添加"
                if (!images.isNullOrEmpty()) goodsDetailTv.text = "已添加"
            }
            // 商品详情
            REQUEST_CODE_DESC -> {
                data.getStringExtra(GoodsDescH5Activity.KEY_DESC)?.apply {
                    goodsVO.intro = this
                    goodsDetailTv.text = if (goodsVO.intro.isNotBlank()) "已添加" else "未添加"
                    if (goodsVO.goodsParamsList?.size ?: 0 > 0) goodsDetailTv.text = "已添加"
                    if (!goodsVO.metaDescription.isNullOrEmpty()) goodsDetailTv.text = "已添加"
                }
            }
        }
    }

    //图片选择
    private fun openGallery() {
        val menus = MediaMenuPop.TYPE_SELECT_PHOTO or MediaMenuPop.TYPE_PLAY_PHOTO
        MediaMenuPop(context, menus).apply {
            setOnClickListener { type ->
                when (type) {
                    MediaMenuPop.TYPE_SELECT_PHOTO -> {
                        PhotoHelper.openCropAlbum(context as Activity, 1, null, true, 32) {
                            val item = convert2GalleryVO(it)[0]
                            GlideUtils.setImageUrl1(imageView, item.original)
                            goodsVO.thumbnail = item.original
                        }
                    }
                    MediaMenuPop.TYPE_PLAY_PHOTO -> {
                        PhotoHelper.openCropCamera(context as Activity, null, true, 32) {
                            val item = convert2GalleryVO(it)[0]
                            GlideUtils.setImageUrl1(imageView, item.original)
                            goodsVO.thumbnail = item.original
                        }
                    }
                }
            }
        }.showPopupWindow()
    }

    override fun searchGoodsFailed() {
        searchGoodsLayout.gone()
    }

    override fun loadGoodsInfo(goods_id: String?) {
        if (searchGoods && pictureAddress.isNotBlank()) {
            //上传图片，成功后调用接口
            val urlList =
                ArrayList(listOf(pictureAddress))
            val id = try {
                (goods_id ?: "0").toInt()
            } catch (e: Exception) {
                0
            }

            uploadImages(urlList, {
                //失败了，nothing to do
            }, {
                mPresenter?.addGoodsSkuBarCodeLog(
                    id,
                    scanCode,
                    urlList[0]
                )
            })
        } else if (searchGoods && pictureAddress.isBlank()) {
            val id = try {
                (goods_id ?: "0").toInt()
            } catch (e: Exception) {
                0
            }
            mPresenter?.addGoodsSkuBarCodeLog(
                id,
                scanCode,
                ""
            )
        }
    }

    //上传图片
    private fun uploadImages(list: ArrayList<String>, fail: () -> Unit, success: () -> Unit) {
        lifecycleScope.launch(Dispatchers.IO) {
            val requestList = ArrayList<Deferred<HiResponse<FileResponse>>>()
            // 多张图片并行上传
            list.forEach {
                val request = async {
                    if (it.isNetUrl()) {
                        HiResponse(0, "", FileResponse("", "", it))
                    } else {
                        CommonRepository.uploadFile(
                            File(it),
                            true,
                            CommonRepository.SCENE_GOODS
                        )
                    }
                }
                requestList.add(request)
            }

            // 多个接口相互等待
            val respList = requestList.awaitAll()
            var isAllSuccess = true
            respList.forEachIndexed { index, it ->
                if (it.isSuccess) {
                    list[index] = it.data?.url ?: ""
                } else {
                    isAllSuccess = false
                }
            }
            if (isAllSuccess) {
                success.invoke()
            } else {
                fail.invoke()
            }
        }
    }

    override fun onUpdateSpeed(id: String?, name: String?) {
        goodsVO.goodsDeliveryType = id
        goodsDeliveryTimeTv.text = name
    }

    private fun convert2GalleryVO(list: List<LocalMedia>): List<GoodsGalleryVO> {
        val galleryList = mutableListOf<GoodsGalleryVO>()
        list.forEach {
            galleryList.add(GoodsGalleryVO.convert(it))
        }
        return galleryList
    }

    override fun onUpdateModel(id: String?, name: String?) {
        goodsVO.goodsDeliveryModel = id
        goodsDeliveryTv.text = name
    }

    private fun clickConfirmView(type: Int) {

        if (isVirtualGoods == 2) {
            //券包
            goodsVO.apply {
                num = try {
                    ticket_number.getViewText().toInt()
                } catch (e: Exception) {
                    null
                }
                bar_code = if (scanCode.isNotBlank()) scanCode else null
                goodsName = goodsNameEdt.getViewText()
                selling = goodsSellingDescEdt.getViewText()
                goodsGalleryList = galleryRv.getSelectPhotos()
                goodsType = GoodsTypeVO.getValue(isVirtualGoods)


                expiryDay = ""
                availableDate = ""

                eventPrice = eventPriceEdt.getViewText()
                eventQuantity = eventQuantityEdt.getViewText()
                price = goodsPriceEdt.getViewText()
                mktprice = marketPriceEdt.getViewText()
                quantity = goodsQuantityEdt.getViewText()
                cost = goodsCostEdt.getViewText()
                sn = goodsNoEdt.getViewText()
                weight = goodsWeightEdt.getViewText()
                upSkuId = goodsSKUEdt.getViewText()
                upGoodsId = goodsIDEdt.getViewText()
                skuList = null

                packagePrice = try {
                    goodsPackageFeeEdt.getViewText().toDouble()
                } catch (e: Exception) {
                    0.00
                }

            }

            //type  0 保存  ，1  保存并上架
            mPresenter.publishTicket(
                goodsVO,
                scan = scan,
                type = type,
                isFromCenter = isFromCenter
            )

        } else {
            goodsVO.apply {
                num = null
                couponID = null
                bar_code = if (scanCode.isNotBlank()) scanCode else null
                goodsName = goodsNameEdt.getViewText()
                selling = goodsSellingDescEdt.getViewText()
                goodsGalleryList = galleryRv.getSelectPhotos()
                goodsType = GoodsTypeVO.getValue(isVirtualGoods)
                if (isVirtualGoods == 1) {
                    // 虚拟
                    price = null
                    mktprice = null
                    eventPrice = null
                    eventQuantity = null
                    quantity = null
                    cost = null
                    sn = null
                    weight = null
                    upSkuId = null
                    upGoodsId = null
                } else if (isVirtualGoods == 0) {
                    // 实物
                    expiryDay = ""
                    availableDate = ""

                    if (switchBtn.isChecked) {
                        eventPrice = null
                        eventQuantity = null
                        price = null
                        mktprice = null
                        quantity = null
                        cost = null
                        sn = null
                        weight = null
                        upSkuId = null
                        upGoodsId = null
                    } else {
                        eventPrice = eventPriceEdt.getViewText()
                        eventQuantity = eventQuantityEdt.getViewText()
                        price = goodsPriceEdt.getViewText()
                        mktprice = marketPriceEdt.getViewText()
                        quantity = goodsQuantityEdt.getViewText()
                        cost = goodsCostEdt.getViewText()
                        sn = goodsNoEdt.getViewText()
                        weight = goodsWeightEdt.getViewText()
                        upSkuId = goodsSKUEdt.getViewText()
                        upGoodsId = goodsIDEdt.getViewText()
                        skuList = null
                    }
                }
                // 打包费
                packagePrice = try {
                    goodsPackageFeeEdt.getViewText().toDouble()
                } catch (e: Exception) {
                    0.00
                }
            }

            //type  0 保存  ，1  保存并上架
            mPresenter.publish(
                goodsVO,
                isVirtualGoods == 1,
                switchBtn.isChecked,
                scan,
                type,
                isFromCenter
            )
        }

    }

    override fun onUpdateGoodsWeight(id: String?, name: String?) {
        goodsVO.goodsWeight = id
//        goodsWeightTv.text = name;
    }

    override fun onUpdateGoodsUnit(id: String?, name: String?) {
        goodsVO.goodsUnit = id
//        goodsUnitTv.text = name;
    }


    //设置实体商品还是虚拟商品
    private fun setVirtualGoodsView() {

        //显示是虚拟商品还是实体商品
        goodsTypeTv.text = GoodsTypeVO.getName(isVirtualGoods)

        //设置实体虚拟商品UI显示(主要是规格)
        when (isVirtualGoods) {
            0 -> {
                //实体商品,显示多规格切换按钮
                section4Row6Ll.show(true)
                if (scan) {
                    section4Row6Ll.gone()
                }
                //根据多规格按钮选中情况显示单规格还是多规格
                section4Row1Ll.show(!switchBtn.isChecked)
                section4RowVirtualSpecLl.show(switchBtn.isChecked)
                ticket.gone()
            }
            1 -> {
                //虚拟商品
                //隐藏UI
                showSection45WithStatus(false, isExpand)
                ticket.gone()
            }
            2 -> {
                //券包
                ticket.visiable()
                //隐藏实体商品设置多规格按钮
                section4Row6Ll.show(false)
                section4Row1Ll.show(true)
                //显示设置多规格
                section4RowVirtualSpecLl.show(false)
            }
            else -> {

            }
        }
    }

    //设置规格的显示与useless
    private fun showSection45WithStatus(isChecked: Boolean, isExpand: Boolean) {
        if (isVirtualGoods == 1) {
            //虚拟商品
            //隐藏实体商品设置单规格
            section4Row1Ll.show(false)
            //隐藏实体商品设置多规格按钮
            section4Row6Ll.show(false)
            if (scan) {
                section4Row6Ll.gone()
            }
            //显示设置多规格
            section4RowVirtualSpecLl.show(true)
        } else if (isVirtualGoods == 0) {
            //实体商品
            //显示实体商品多规格按钮
            section4Row6Ll.show(true)
            if (scan) {
                section4Row6Ll.gone()
            }
            section4Row1Ll.show(!isChecked)
            section4RowVirtualSpecLl.show(isChecked)
        }

        //useless
        moreTv.text = if (isExpand) "收起更多信息" else "展开更多信息"
        val drawable =
            if (isExpand) R.mipmap.goods_blue_arrow_up else R.mipmap.goods_blue_arrow_down
        moreTv.setCompoundDrawablesWithIntrinsicBounds(0, 0, drawable, 0)
    }


    //useless
    override fun onUpdateExpire(item: GoodsUseExpireVo?) {
        goodsVO.expiryDay = item?.value
        goodsVirtualExpireTv.text = item?.label
    }

    //useless
    override fun onUpdateUseTime(list: List<MultiPickerItemBean>?) {
        val values = list?.map { it.value }?.joinToString(separator = ",")
        val names = list?.map { it.name }?.joinToString(separator = ",")
        goodsVO.availableDate = values
        goodsVirtualUseTimeTv.text = if (names.isNotBlank()) names else "请设置"
    }

    //useless
    override fun onSetUseTimeStr(useTime: String) {
        // goodsVirtualUseTimeTv.text = useTime
    }
}