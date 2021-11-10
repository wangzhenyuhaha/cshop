package com.lingmiao.shop.business.goods

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.util.Log
import com.james.common.base.BaseActivity
import com.james.common.utils.exts.*
import com.lingmiao.shop.R
import com.lingmiao.shop.business.common.pop.MediaMenuPop
import com.lingmiao.shop.business.goods.api.bean.*
import com.lingmiao.shop.business.goods.api.request.DeliveryRequest
import com.lingmiao.shop.business.goods.presenter.GoodsPublishNewPre
import com.lingmiao.shop.business.goods.presenter.impl.GoodsPublishPreNewImpl
import com.lingmiao.shop.business.photo.PhotoHelper
import com.lingmiao.shop.util.GlideUtils
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

/**
 * Author : Elson
 * Date   : 2020/7/25
 * Desc   : 添加商品页面
 */
class GoodsPublishNewActivity : BaseActivity<GoodsPublishNewPre>(), GoodsPublishNewPre.PublishView {

    companion object {
        const val KEY_GOODS_ID = "KEY_GOODS_ID"
        private const val KEY_GOODS_PUB_TYPE = "KEY_GOODS_PUB_TYPE"
        private const val KEY_SCAN = "KEY_SCAN"

        const val REQUEST_CODE_VIDEO = 1000
        const val REQUEST_CODE_DELIVERY = 1001
        const val REQUEST_CODE_SKU = 1002
        const val REQUEST_CODE_DESC = 1003
        const val REQUEST_CODE_INFO = 1004

        //编辑已有商品
        //传入good_id
        fun openActivity(context: Context, goodsId: String?, scan: Boolean = false) {
            val intent = Intent(context, GoodsPublishNewActivity::class.java)
            intent.putExtra(KEY_GOODS_ID, goodsId)
            intent.putExtra(KEY_SCAN, scan)
            context.startActivity(intent)
        }

        //处理新增商品
        //type的值为0
        fun newPublish(context: Context, type: Int?, scan: Boolean = false) {
            val intent = Intent(context, GoodsPublishNewActivity::class.java)
            intent.putExtra(KEY_GOODS_PUB_TYPE, type)
            intent.putExtra(KEY_SCAN, scan)
            context.startActivity(intent)
        }
    }

    //是否是虚拟商品
    private var isVirtualGoods = false

    //是否从扫码处跳转 true表示是
    private var scan: Boolean = false

    // 底部按钮是否展开
    private var isExpand = false

    // 编辑商品时，所携带的商品ID
    private var goodsId: String? = null

    // 添加/编辑商品 的数据实体
    private var goodsVO: GoodsVOWrapper = GoodsVOWrapper()

    override fun useLightMode() = false

    override fun getLayoutId() = R.layout.goods_activity_publish_new

    override fun initBundles() {
        //编辑已有商品时获得此数据
        goodsId = intent.getStringExtra(KEY_GOODS_ID)
        scan = intent.getBooleanExtra(KEY_SCAN, false)
    }

    override fun createPresenter(): GoodsPublishNewPre {
        return GoodsPublishPreNewImpl(this, this)
    }

    override fun initView() {

        mToolBarDelegate.setMidTitle(if (goodsId.isNotBlank()) "编辑商品" else "发布商品")

        //点击操作，实体商品和虚拟商品
        initSectionView()

        //添加新的分类
        cateAddIv.singleClick {
            GoodsCategoryActivity.openActivity(this, 1)
        }

        //useless
        initSection3View()

        //实体商品多规格
        initSection4View()

        //商品详情
        initSection5678View()

        initBottomView()
        if (scan) {
            goodsId?.let { mPresenter.loadGoodsInfoFromCenter(it) }
        } else {
            mPresenter.loadGoodsInfo(goodsId)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (data == null) {
            return
        }
        when (requestCode) {
            // 视频
            REQUEST_CODE_VIDEO -> {
                data.getStringExtra(GoodsVideoActivity.KEY_VIDEO_URL)?.apply {
                    goodsVO.videoUrl = this
                }
                goodsVideoTv.text = if (goodsVO.videoUrl.isNotBlank()) "已添加" else "请选择"
            }
            // 发货方式
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
                if (isVirtualGoods) {
                    goodsVirtualSpecTv.text = if (skuList.isNullOrEmpty()) "请设置" else "已设置"
                } else {
                    goodsSpecTv.text = if (skuList.isNullOrEmpty()) "请设置规格" else "已设置"
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

    //实体商品和虚拟商品切换
    override fun onSetGoodsType(isVirtual: Boolean) {
        isVirtualGoods = isVirtual
        setVirtualGoodsView()
    }

    override fun onSetUseTimeStr(useTime: String) {
        goodsVirtualUseTimeTv.text = useTime
    }

    override fun onLoadGoodsSuccess(goodsVO: GoodsVOWrapper) {
        this.goodsVO = goodsVO
        goodsVO.apply {
            if (isAuth == 3) {
                authFailLayout.visiable()
                goodsAuthMsgTv.setText(authMessage)
            } else {
                authFailLayout.gone()
            }
            GlideUtils.setImageUrl(imageView, goodsVO.thumbnail, R.mipmap.goods_selected_img)
            // 商品分类
//            goodsCategoryTv.text = Html.fromHtml(categoryName)
            goodsCategoryTv.text = categoryName?.replace("&gt;", "/")
            // 商品名称
            goodsNameEdt.setText(goodsName)
            galleryRv.addDataList(goodsGalleryList)
            // 发货方式
            if (isSelectDeliveryWay()) {
                goodsDeliveryTv.text = "已选择"
            }
            isVirtualGoods = isVirtualGoods()
            if (isVirtualGoods) {
                // 虚拟商品
                setVirtualGoodsView()
                goodsVirtualExpireTv.text = expiryDayText
                goodsVirtualUseTimeTv.text = availableDate
                goodsVirtualSpecTv.text = if (this.hasMultiSpec()) "已设置" else "请设置"
            } else {
                // 规格
                if (this.hasMultiSpec()) {
                    goodsSpecTv.text = "已设置"
                    switchBtn.isChecked = true
                    showSection45WithStatus(switchBtn.isChecked, true)
                } else {
                    eventPriceEdt.setText(eventPrice)
                    eventQuantityEdt.setText(eventQuantity)
                    goodsPriceEdt.setText(price)
                    marketPriceEdt.setText(mktprice)
                    goodsQuantityEdt.setText(quantity)
                    goodsCostEdt.setText(cost)
                    goodsNoEdt.setText(sn)
                    goodsWeightEdt.setText(weight)
                    goodsSKUEdt.setText(upSkuId)
                    goodsIDEdt.setText(sn)
                    switchBtn.isChecked = false
//                    if(!goodsId.isNullOrBlank()) {
//                        goodsQuantityEdt.isEnabled = false;
//                    }
                }
            }
            // 商品分组 //categoryName
            goodsGroupTv.text = if (shopCatName == null)
                categoryName
            else
                shopCatName?.replace("&gt;", "/")
            // 商品视频
            if (videoUrl.isNotBlank()) {
                goodsVideoTv.text = "已选择"
            }
            // 商品详情
//            if (intro.isNotBlank()) {
//                goodsDetailTv.text = "已添加"
//            }
            if (goodsParamsList?.size ?: 0 > 0) {
                goodsDetailTv.text = "已添加"
            }
            //metaDescription = des
            //                    this.intro
            if (!metaDescription.isNullOrEmpty()) {
                goodsDetailTv.text = "已添加"
            }
            if (!intro.isNullOrEmpty()) {
                goodsDetailTv.text = "已添加"
            }
            // 商品卖点
            goodsSellingDescEdt.setText(selling)
            // 打包费
            goodsPackageFeeEdt.setText(packagePrice.toString())
        }
    }

    override fun onUpdateCategory(categoryId: String?, categoryName: String?) {
        goodsCategoryTv.text = categoryName
        goodsVO.apply {
            this.categoryId = categoryId
            this.categoryName = categoryName
            //菜单未设置
            if (this.shopCatId.isNullOrEmpty()) {
                //UI上显示分类，但是实际上不传菜单数据
                onUpdateGroup(null, categoryName)
            }
        }
    }

    override fun onUpdateGroup(groupId: String?, groupName: String?) {
        goodsGroupTv.text = groupName
        if (groupId != null) {
            goodsVO.apply {
                this.shopCatId = groupId
                this.shopCatName = groupName
            }
        }

    }

    private fun initSectionView() {
        // 切换商品类型
        goodsTypeTv.singleClick {
            mPresenter.showGoodsType(isVirtualGoods)
        }
        // 商品分类
        goodsCategoryTv.singleClick {
            mPresenter.showCategoryPop()
        }
        // 有效期至  无用
        goodsVirtualExpireTv.singleClick {
            mPresenter?.showExpirePop(goodsVO.expiryDay ?: "")
        }
        // 使用时间   无用
        goodsVirtualUseTimeTv.singleClick {
            mPresenter?.showUseTimePop(goodsVO.availableDate ?: "")
        }
        //虚拟商品，商品套餐，设置多规格
        goodsVirtualSpecTv.singleClick {
            if (goodsVO.categoryId.isNotBlank()) {
                SpecSettingActivity.openActivity(
                    this,
                    REQUEST_CODE_SKU,
                    isVirtualGoods,
                    goodsVO,
                    scan = scan
                )
            } else {
                showToast("请先选择商品分类")
            }
        }
        // 时效   无用
        goodsDeliveryTimeTv.singleClick {
            mPresenter?.showDeliveryTypePop(goodsVO.goodsDeliveryType)
        }

        //设置主图
        galleryRv.setCountLimit(1, 20)
        deleteIv.gone()
        imageView.singleClick {
            openGallery()
        }
    }

    override fun onUpdateSpeed(id: String?, name: String?) {
        goodsVO.goodsDeliveryType = id
        goodsDeliveryTimeTv.text = name
    }

    override fun onUpdateExpire(item: GoodsUseExpireVo?) {
        goodsVO.expiryDay = item?.value
        goodsVirtualExpireTv.text = item?.label
    }

    override fun onUpdateUseTime(items: List<MultiPickerItemBean>?) {
        val values = items?.map { it.value }?.joinToString(separator = ",")
        val names = items?.map { it.name }?.joinToString(separator = ",")
        goodsVO.availableDate = values
        goodsVirtualUseTimeTv.text = if (names.isNotBlank()) names else "请设置"
    }

    private fun setVirtualGoodsView() {
        goodsTypeTv.text = GoodsTypeVO.getName(isVirtualGoods)
        //配送方式，无用
        section3RowDeliveryLL.show(!isVirtualGoods)

        //设置实体虚拟商品UI显示
        if (isVirtualGoods) {
            //虚拟商品
            //隐藏UI
            showSection45WithStatus(false, isExpand)
        } else {
            //非虚拟商品,显示多规格
            section4Row6Ll.show(true)
            //显示设置套餐
            section4RowVirtualSpecLl.show(true)
        }
    }


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

    private fun convert2GalleryVO(list: List<LocalMedia>): List<GoodsGalleryVO> {
        val galleryList = mutableListOf<GoodsGalleryVO>()
        list.forEach {
            galleryList.add(GoodsGalleryVO.convert(it))
        }
        return galleryList
    }

    private fun initSection3View() {
        goodsDeliveryTv.singleClick {
//            DeliverySettingActivity.openActivity(this, REQUEST_CODE_DELIVERY, DeliveryRequest.convert(goodsVO))
            mPresenter?.showDeliveryModelPop(goodsVO.goodsDeliveryModel)
        }
    }

    override fun onUpdateModel(id: String?, name: String?) {
        goodsVO.goodsDeliveryModel = id
        goodsDeliveryTv.text = name
    }

    private fun initSection4View() {

        //实体商品设置多规格
        goodsSpecTv.singleClick {
            SpecSettingActivity.openActivity(
                this,
                REQUEST_CODE_SKU,
                isVirtualGoods,
                goodsVO,
                scan = scan
            )
        }
        switchBtn.setOnCheckedChangeListener { _, isChecked ->
            showSection45WithStatus(isChecked, isExpand)
        }
    }

    private fun initSection5678View() {
        goodsGroupTv.singleClick {
            mPresenter.showGroupPop()
        }
        goodsVideoTv.singleClick {
            GoodsVideoActivity.openActivity(this, REQUEST_CODE_VIDEO, goodsVO.videoUrl)
        }
        goodsDetailTv.singleClick {
            if (goodsVO.categoryId.isNotBlank()) {
                GoodsDetailActivity.openActivity(this, REQUEST_CODE_INFO, goodsVO)
            } else {
                showToast("请先选择商品分类")
            }
        }
    }

    private fun initBottomView() {
        moreTv.singleClick {
            isExpand = !isExpand
            showSection45WithStatus(switchBtn.isChecked, isExpand)
        }
        confirmTv.singleClick {
            clickConfirmView()
        }
    }

    private fun clickConfirmView() {
        goodsVO.apply {
            goodsName = goodsNameEdt.getViewText()
            selling = goodsSellingDescEdt.getViewText()
            goodsGalleryList = galleryRv.getSelectPhotos()
            goodsType = GoodsTypeVO.getValue(isVirtualGoods)
            if (isVirtualGoods) {
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
            } else {
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
            //
            packagePrice = try {
                goodsPackageFeeEdt.getViewText().toDouble()
            } catch (e: Exception) {
                0.00
            }

            // 默认
//            if (this.shopCatId == null) {
//                this.shopCatId = categoryId
//                this.shopCatName = categoryName
//            }
        }

        mPresenter.publish(goodsVO, isVirtualGoods, switchBtn.isChecked, scan)
    }

    /**
     * 以下几个控件的显示隐藏和 "多规格的开关"、"底部展开/收起按钮"关联
     */
    private fun showSection45WithStatus(isChecked: Boolean, isExpand: Boolean) {
        if (isVirtualGoods) {
            //隐藏设置单规格
            section4Row1Ll.show(false)
            section4Row6Ll.show(false)
            section4RowVirtualSpecLl.show(true)
        } else {
            section4Row6Ll.show(true)
            section4Row1Ll.show(!isChecked)
            section4RowVirtualSpecLl.show(isChecked)
        }
        //useless
        moreTv.text = if (isExpand) "收起更多信息" else "展开更多信息"
        val drawable =
            if (isExpand) R.mipmap.goods_blue_arrow_up else R.mipmap.goods_blue_arrow_down
        moreTv.setCompoundDrawablesWithIntrinsicBounds(0, 0, drawable, 0)
    }

    override fun onUpdateGoodsWeight(id: String?, name: String?) {
        goodsVO.goodsWeight = id
//        goodsWeightTv.text = name;
    }

    override fun onUpdateGoodsUnit(id: String?, name: String?) {
        goodsVO.goodsUnit = id
//        goodsUnitTv.text = name;
    }

}