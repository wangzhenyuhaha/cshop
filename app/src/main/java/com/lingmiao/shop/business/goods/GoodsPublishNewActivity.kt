package com.lingmiao.shop.business.goods

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
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
import com.lingmiao.shop.business.sales.EVouchersDetailActivity
import com.lingmiao.shop.business.sales.SelectGoodsActivity
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
 * Desc   : ??????????????????
 */

@SuppressLint("NotifyDataSetChanged")
class GoodsPublishNewActivity : BaseActivity<GoodsPublishNewPre>(), GoodsPublishNewPre.PublishView {

    companion object {
        //??????good_id
        const val KEY_GOODS_ID = "KEY_GOODS_ID"

        //????????????????????????
        private const val KEY_SCAN = "KEY_SCAN"

        //???????????????????????????
        private const val KEY_SCAN_CODE = "KEY_SCAN_CODE"

        //???????????????????????????
        private const val KEY_PICTURE_ADDRESS = "KEY_PICTURE_ADDRESS"

        //???????????????
        private const val KEY_GOODS_THUMBNAIL = "KEY_GOODS_THUMBNAIL"

        //????????????
        private const val KEY_GOODS_PRICE = "KEY_GOODS_PRICE"

        //???????????? quantity
        private const val KEY_GOODS_QUANTITY = "KEY_GOODS_QUANTITY"

        //????????????ID
        private const val KEY_GOODS_CATEGORYID = "KEY_GOODS_CATEGORYID"

        //????????????name
        private const val KEY_GOODS_CATEGORYNAME = "KEY_GOODS_CATEGORYNAME"

        //????????????
        private const val KEY_GOODS_SHOPCATID = "KEY_GOODS_SHOPCATID"

        //????????????name
        private const val KEY_GOODS_SHOPCATNAME = "KEY_GOODS_SHOPCATNAME"

        //??????????????????????????????????????????
        private const val IS_FROM_CENTER = "IS_FROM_CENTER"

        const val REQUEST_CODE_VIDEO = 1000
        const val REQUEST_CODE_DELIVERY = 1001
        const val REQUEST_CODE_SKU = 1002
        const val REQUEST_CODE_DESC = 1003
        const val REQUEST_CODE_INFO = 1004
        const val REQUEST_CODE_TICKET = 22

        //????????????????????????????????????????????????
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

        //??????????????????
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

    //??????????????????????????????????????????????????????(????????????????????????????????????)
    private var isFromCenter = 0

    // ?????????????????????ID
    private var goodsId: String? = null

    //???????????????????????? true?????????????????????????????????,true????????????
    private var scan: Boolean = false

    //?????????????????????????????????????????????scan???true???goods_id???null??????,????????????????????????????????????????????????????????????
    //?????????????????????????????????????????????????????????????????????????????????????????????????????????ID???????????????
    private var searchGoods: Boolean = false

    //?????????????????????
    private var pictureAddress: String = ""

    //??????????????????
    private var scanCode: String = ""

    //?????????????????????  1  ????????????   0   ????????????    2??????
    private var isVirtualGoods = 0

    //????????????????????????   useless
    private var isExpand = false

    //??????/???????????? ???????????????
    private var goodsVO: GoodsVOWrapper = GoodsVOWrapper()

    //??????????????????
    private var goodsName: String = ""

    //???????????????????????????RecyclerView
    private var adapter: SimpleAdapter? = null

    //?????????????????????????????????
    private var thumbnail: String? = null
    private var price: String? = null
    private var quantity: String? = null
    private var categoryId: String? = null
    private var categoryName: String? = null
    private var shopCatId: String? = null
    private var shopCatName: String? = null


    override fun useLightMode() = false

    override fun getLayoutId() = R.layout.goods_activity_publish_new

    //???????????????????????????????????????
    override fun initBundles() {
        //????????????
        //1 scan false  goods_id null  ????????????
        //2 scan false  goods_id Not Null  ????????????
        //3 scan true  goods_id null  ??????goods_id??????????????? Not Null,??????????????????null   ????????????
        //4 scan true  goods_id not null ???????????????????????????Null    ????????????
        goodsId = intent.getStringExtra(KEY_GOODS_ID)
        scan = intent.getBooleanExtra(KEY_SCAN, false)
        if (scan && goodsId == null) {
            //?????????????????????????????????????????????????????????????????????????????????????????????????????????????????????
            searchGoods = true
        }
        //??????????????????
        scanCode = intent.getStringExtra(KEY_SCAN_CODE) ?: ""
        //???????????????????????????????????????????????????????????????
        pictureAddress = intent.getStringExtra(KEY_PICTURE_ADDRESS) ?: ""

        //??????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????
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
        mToolBarDelegate.setMidTitle(if (goodsId.isNotBlank() && !scan) "????????????" else "????????????")

        if (scan) {
            //???????????????????????????
            section4Row6Ll.gone()
        }

        //???????????????????????????
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

        // ??????????????????
        goodsTypeTv.singleClick {
            mPresenter.showGoodsType(isVirtualGoods)
        }

        // ????????????
        goodsCategoryTv.singleClick {
            mPresenter.showCategoryPop()
        }

        //?????????????????????????????????
        cateAddIv.singleClick {
            GoodsCategoryActivity.openActivity(this, 1)
        }

        //????????????
        goodsGroupTv.singleClick {
            mPresenter.showGroupPop()
        }

        //????????????
        //???????????????????????????
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


        //???????????????????????????????????????

        ticket_number.addTextChangedListener(object : TextWatcher {
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
                val goodsName: String = goodsNameEdt.getViewText()
                if (goodsName.endsWith("???????????????")) {
                    //???????????????
                    val index = goodsName.indexOfLast { it == '???' }
                    val temp = goodsName.subSequence(0, index).toString() + "?????????${s.toString()}???????????????"
                    goodsNameEdt.setText(temp)
                } else {
                    //???????????????
                    val temp = goodsName + "?????????${s.toString()}???????????????"
                    goodsNameEdt.setText(temp)
                }
            }
        })


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

        //???????????????
        deleteIv.gone()
        imageView.singleClick {
            openGallery()
        }

        //????????????
        galleryRv.setCountLimit(1, 20)

        //???????????????
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
                showToast("????????????????????????")
            }
        }

        //????????????????????????????????????????????????
        switchBtn.setOnCheckedChangeListener { _, isChecked ->
            showSection45WithStatus(isChecked, isExpand)
        }

        //????????????????????????
        ticker_goods_name.singleClick {
            val intent = Intent(this, SelectTicketActivity::class.java)
            intent.putExtra("coupon_id", goodsVO.couponID)
            startActivityForResult(intent, 22)
        }

        addEV.singleClick {
            EVouchersDetailActivity.openActivity(this, 1)
        }

        //????????????
        goodsDetailTv.singleClick {
            if (goodsVO.categoryId.isNotBlank()) {
                GoodsDetailActivity.openActivity(this, REQUEST_CODE_INFO, goodsVO)
            } else {
                showToast("????????????????????????")
            }
        }

        //??????  0
        saveTv.singleClick {
            clickConfirmView(0)
        }
        //???????????????  1
        confirmTv.singleClick {
            clickConfirmView(1)
        }

        //????????????
        if (scan) {
            goodsId?.let { mPresenter.loadGoodsInfoFromCenter(it) }
        } else {
            //??????????????????????????????
            mPresenter.loadGoodsInfo(goodsId)
        }

        //useless list
        //?????????  useless
        goodsVirtualExpireTv.singleClick {
            mPresenter?.showExpirePop(goodsVO.expiryDay ?: "")
        }
        //????????????   useless
        goodsVirtualUseTimeTv.singleClick {
            mPresenter?.showUseTimePop(goodsVO.availableDate ?: "")
        }
        //useless
        goodsVideoTv.singleClick {
            GoodsVideoActivity.openActivity(this, REQUEST_CODE_VIDEO, goodsVO.videoUrl)
        }
        //???????????? useless
        moreTv.singleClick {
            isExpand = !isExpand
            showSection45WithStatus(switchBtn.isChecked, isExpand)
        }

    }

    //??????????????????
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

    //????????????
    override fun onUpdateCategory(categoryId: String?, categoryName: String?) {
        //??????????????????
        goodsCategoryTv.text = categoryName
        goodsVO.apply {
            //????????????ID???Name
            this.categoryId = categoryId
            this.categoryName = categoryName
        }

        if (isFromCenter == 1) {
            goodsVO.shopCatId = null
            goodsVO.shopCatName = null
            goodsGroupTv.text = "?????????"
            isFromCenter = 0
        }

        //???????????????
        if (goodsVO.shopCatId.isNullOrEmpty()) {
            //UI?????????????????????????????????????????????????????????
            onUpdateGroup(null, categoryName)
        }
    }

    //????????????
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
            goodsCategoryTv.text = "?????????????????????"
            isFromCenter = 0
        }
    }

    //?????????????????????
    override fun searchGoodsSuccess(list: List<Data>) {
        if (list.isEmpty()) {
            return
        }
        searchGoodsLayout.visiable()
        adapter?.replaceData(list)
        adapter?.notifyDataSetChanged()
    }

    //??????????????????
    override fun onLoadGoodsSuccess(goodsVO: GoodsVOWrapper, center: Boolean) {
        this.goodsVO = goodsVO
        goodsVO.apply {
            //?????????????????????
            if (isAuth == 3) {
                //???????????????
                authFailLayout.visiable()
                goodsAuthMsgTv.text = authMessage
            } else {
                //??????
                authFailLayout.gone()
            }

            //??????????????????
            if (goodsCoupon != null) {
                ticket_number.setText(goodsCoupon?.num.toString())
                if (num == null) {
                    num = goodsCoupon?.num
                }
            }
            if (goodsCoupon?.couponID != null) {
                ticker_goods_name.text = "?????????"
                if (couponID == null) {
                    couponID = goodsCoupon?.couponID
                }
            }

            //????????????
            isVirtualGoods = isVirtualGoods()
            when (isVirtualGoods) {
                0 -> {
                    //????????????
                    goodsVirtualSpecTv.text = if (this.hasMultiSpec() && !scan) "?????????" else "?????????"
                    if (this.hasMultiSpec()) {
                        //goodsSpecTv.text = "?????????"
                        switchBtn.isChecked = true
                        showSection45WithStatus(switchBtn.isChecked, true)
                    } else {
                        eventPriceEdt.setText(eventPrice)
                        eventQuantityEdt.setText(eventQuantity)
                        goodsPriceEdt.setText(price)
                        marketPriceEdt.setText(mktprice)
                        goodsQuantityEdt.setText(enableQuantity)
                        goodsCostEdt.setText(cost)
                        switchBtn.isChecked = false
                    }
                }
                1 -> {
                    //????????????
                    setVirtualGoodsView()
                    goodsVirtualSpecTv.text = if (this.hasMultiSpec() && !scan) "?????????" else "?????????"
                }
                2 -> {
                    //??????
                    setVirtualGoodsView()
                    eventPriceEdt.setText(eventPrice)
                    eventQuantityEdt.setText(eventQuantity)
                    goodsPriceEdt.setText(price)
                    marketPriceEdt.setText(mktprice)
                    goodsQuantityEdt.setText(enableQuantity)
                    goodsCostEdt.setText(cost)
                    switchBtn.isChecked = false
                }
                else -> {

                }

            }

            //????????????
            goodsCategoryTv.text = categoryName?.replace("&gt;", "/")

            //????????????
            goodsGroupTv.text = if (shopCatName == null)
                categoryName
            else
                shopCatName?.replace("&gt;", "/")

            // ????????????
            goodsNameEdt.setText(goodsName)

            //???????????????
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

            //????????????
            galleryRv.addDataList(goodsGalleryList)

            //????????????
            if (goodsParamsList?.size ?: 0 > 0) {
                goodsDetailTv.text = "?????????"
            }
            if (!metaDescription.isNullOrEmpty()) {
                goodsDetailTv.text = "?????????"
            }
            if (!intro.isNullOrEmpty()) {
                goodsDetailTv.text = "?????????"
            }

            // ?????????
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
            //??????UI??????
            GlideUtils.setImageUrl(imageView, this.goodsVO.thumbnail, R.mipmap.goods_selected_img)
            goodsPriceEdt.setText(this.goodsVO.price)
            goodsQuantityEdt.setText(this.goodsVO.enableQuantity)
            //??????????????????????????????????????????????????????????????????????????????isFromCenter

            //??????????????????
            goodsCategoryTv.text = this@GoodsPublishNewActivity.goodsVO.categoryName
            //??????????????????
            if (this@GoodsPublishNewActivity.goodsVO.shopCatId.isNullOrEmpty()) {
                //??????????????????
                goodsGroupTv.text = this@GoodsPublishNewActivity.goodsVO.categoryName
            } else {
                //??????????????????
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
            // ?????? useless
            REQUEST_CODE_VIDEO -> {
                data.getStringExtra(GoodsVideoActivity.KEY_VIDEO_URL)?.apply {
                    goodsVO.videoUrl = this
                }
                goodsVideoTv.text = if (goodsVO.videoUrl.isNotBlank()) "?????????" else "?????????"
            }
            // ???????????? useless
            REQUEST_CODE_DELIVERY -> {
                val deliveryVO =
                    data.getSerializableExtra(DeliverySettingActivity.KEY_Delivery) as? DeliveryRequest
                goodsVO.convert(deliveryVO)
                goodsDeliveryTv.text = if (goodsVO.isSelectDeliveryWay()) "?????????" else "?????????"
            }
            // ????????????
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
                    goodsVirtualSpecTv.text = if (skuList.isNullOrEmpty()) "?????????" else "?????????"
                } else if (isVirtualGoods == 0) {
                    goodsSpecTv.text = if (skuList.isNullOrEmpty()) "???????????????" else "?????????"
                    goodsVirtualSpecTv.text = if (skuList.isNullOrEmpty()) "?????????" else "?????????"
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
                goodsDetailTv.text = if (infoList?.size ?: 0 > 0) "?????????" else "?????????"
                if (!des.isNullOrEmpty()) goodsDetailTv.text = "?????????"
                if (!images.isNullOrEmpty()) goodsDetailTv.text = "?????????"
            }
            // ????????????
            REQUEST_CODE_DESC -> {
                data.getStringExtra(GoodsDescH5Activity.KEY_DESC)?.apply {
                    goodsVO.intro = this
                    goodsDetailTv.text = if (goodsVO.intro.isNotBlank()) "?????????" else "?????????"
                    if (goodsVO.goodsParamsList?.size ?: 0 > 0) goodsDetailTv.text = "?????????"
                    if (!goodsVO.metaDescription.isNullOrEmpty()) goodsDetailTv.text = "?????????"
                }
            }
            REQUEST_CODE_TICKET -> {

                val goodsId: Int = data.getIntExtra("goods_id", 0)
                val goodsName = data.getStringExtra("goods_name")
                if (goodsId != 0) {
                    goodsVO.couponID = goodsId
                    ticker_goods_name.text = goodsName
                }
            }
        }
    }

    //????????????
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
            //????????????????????????????????????
            val urlList =
                ArrayList(listOf(pictureAddress))
            val id = try {
                (goods_id ?: "0").toInt()
            } catch (e: Exception) {
                0
            }

            uploadImages(urlList, {
                //????????????nothing to do
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

    //????????????
    private fun uploadImages(list: ArrayList<String>, fail: () -> Unit, success: () -> Unit) {
        lifecycleScope.launch(Dispatchers.IO) {
            val requestList = ArrayList<Deferred<HiResponse<FileResponse>>>()
            // ????????????????????????
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

            // ????????????????????????
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
            //??????
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

            //type  0 ??????  ???1  ???????????????
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
                    // ??????
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
                    // ??????
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
                // ?????????
                packagePrice = try {
                    goodsPackageFeeEdt.getViewText().toDouble()
                } catch (e: Exception) {
                    0.00
                }
            }

            //type  0 ??????  ???1  ???????????????
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


    //????????????????????????????????????
    private fun setVirtualGoodsView() {

        //???????????????????????????????????????
        goodsTypeTv.text = GoodsTypeVO.getName(isVirtualGoods)

        //????????????????????????UI??????(???????????????)
        when (isVirtualGoods) {
            0 -> {
                //????????????,???????????????????????????
                section4Row6Ll.show(true)
                if (scan) {
                    section4Row6Ll.gone()
                }
                //???????????????????????????????????????????????????????????????
                section4Row1Ll.show(!switchBtn.isChecked)
                section4RowVirtualSpecLl.show(switchBtn.isChecked)
                ticket.gone()
                sectionVirtualExpireTime.gone()
            }
            1 -> {
                //????????????
                //??????UI
                showSection45WithStatus(false, isExpand)
                ticket.gone()
                sectionVirtualExpireTime.visiable()
            }
            2 -> {
                //??????
                sectionVirtualExpireTime.gone()
                ticket.visiable()
                //???????????????????????????????????????
                section4Row6Ll.show(false)
                section4Row1Ll.show(true)
                //?????????????????????
                section4RowVirtualSpecLl.show(false)
            }
            else -> {

            }
        }
    }

    //????????????????????????useless
    private fun showSection45WithStatus(isChecked: Boolean, isExpand: Boolean) {
        if (isVirtualGoods == 1) {
            //????????????
            //?????????????????????????????????
            section4Row1Ll.show(false)
            //???????????????????????????????????????
            section4Row6Ll.show(false)
            if (scan) {
                section4Row6Ll.gone()
            }
            //?????????????????????
            section4RowVirtualSpecLl.show(true)
            sectionVirtualExpireTime.visiable()
        } else if (isVirtualGoods == 0) {
            //????????????
            //?????????????????????????????????
            section4Row6Ll.show(true)
            if (scan) {
                section4Row6Ll.gone()
            }
            sectionVirtualExpireTime.gone()
            section4Row1Ll.show(!isChecked)
            section4RowVirtualSpecLl.show(isChecked)
        }

        //useless
        moreTv.text = if (isExpand) "??????????????????" else "??????????????????"
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
        goodsVirtualUseTimeTv.text = if (names.isNotBlank()) names else "?????????"
    }

    //useless
    override fun onSetUseTimeStr(useTime: String) {
        // goodsVirtualUseTimeTv.text = useTime
    }
}