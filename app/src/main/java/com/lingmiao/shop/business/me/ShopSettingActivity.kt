package com.lingmiao.shop.business.me

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import com.amap.api.maps.model.LatLng
import com.blankj.utilcode.util.LogUtils
import com.james.common.base.BaseVBActivity
import com.james.common.netcore.coroutine.CoroutineSupport
import com.james.common.utils.DialogUtils
import com.james.common.utils.exts.singleClick
import com.lingmiao.shop.R
import com.lingmiao.shop.base.CommonRepository
import com.lingmiao.shop.base.UserManager
import com.lingmiao.shop.business.common.pop.MediaMenuPop
import com.lingmiao.shop.business.common.ui.PhotoListActivity
import com.lingmiao.shop.business.goods.api.bean.GoodsGalleryVO
import com.lingmiao.shop.business.main.ShopAddressActivity
import com.lingmiao.shop.business.main.bean.ApplyShopInfo
import com.lingmiao.shop.business.main.bean.ApplyShopPoiEvent
import com.lingmiao.shop.business.me.bean.BannerBean
import com.lingmiao.shop.business.me.bean.ShopManageImageEvent
import com.lingmiao.shop.business.me.fragment.BannerActivity
import com.lingmiao.shop.business.me.fragment.BannerItem
import com.lingmiao.shop.business.me.presenter.ShopSettingPresenter
import com.lingmiao.shop.business.me.presenter.impl.ShopSettingPresenterImpl
import com.lingmiao.shop.business.photo.GlideEngine
import com.lingmiao.shop.databinding.ActivityShopSettingBinding
import com.lingmiao.shop.util.GlideUtils
import com.lingmiao.shop.util.OtherUtils
import com.luck.picture.lib.PictureSelector
import com.luck.picture.lib.config.PictureMimeType
import com.luck.picture.lib.entity.LocalMedia
import com.luck.picture.lib.listener.OnResultCallbackListener
import kotlinx.android.synthetic.main.me_fragment_shop_base_setting.*
import kotlinx.android.synthetic.main.me_fragment_shop_operate_setting.*
import kotlinx.coroutines.launch
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode

class ShopSettingActivity :
    BaseVBActivity<ActivityShopSettingBinding, ShopSettingPresenter>(),
    ShopSettingPresenter.View {

    private val bannerArray = listOf(
        BannerItem(
            "https://c-shop-prod.oss-cn-hangzhou.aliyuncs.com/apk/banner/20211126150741.jpg",
            0
        ),
        BannerItem(
            "https://c-shop-prod.oss-cn-hangzhou.aliyuncs.com/apk/banner/20211021142234.png",
            1
        ),
        BannerItem(
            "https://c-shop-prod.oss-cn-hangzhou.aliyuncs.com/apk/banner/20211021142243.jpg",
            2
        ),
        BannerItem(
            "https://c-shop-prod.oss-cn-hangzhou.aliyuncs.com/apk/banner/20211021142257.jpg",
            3
        ),
        BannerItem(
            "https://c-shop-prod.oss-cn-hangzhou.aliyuncs.com/apk/banner/20211021142306.jpg",
            4
        ),
        BannerItem(
            "https://c-shop-prod.oss-cn-hangzhou.aliyuncs.com/apk/banner/20211021142314.jpg",
            5
        ),
        BannerItem(
            "https://c-shop-prod.oss-cn-hangzhou.aliyuncs.com/apk/banner/20211021142321.jpg",
            6
        ),
        BannerItem(
            "https://c-shop-prod.oss-cn-hangzhou.aliyuncs.com/apk/banner/20211021142405.jpg",
            7
        ),
        BannerItem(
            "https://c-shop-prod.oss-cn-hangzhou.aliyuncs.com/apk/banner/20211021142634.jpg",
            8
        ),
        BannerItem(
            "https://c-shop-prod.oss-cn-hangzhou.aliyuncs.com/apk/banner/20211021142643.jpg",
            9
        ),
        BannerItem(
            "https://c-shop-prod.oss-cn-hangzhou.aliyuncs.com/apk/banner/20211021142650.jpg",
            10
        ),
        BannerItem(
            "https://c-shop-prod.oss-cn-hangzhou.aliyuncs.com/apk/banner/20211021142657.jpg",
            11
        ),
        BannerItem(
            "https://c-shop-prod.oss-cn-hangzhou.aliyuncs.com/apk/banner/20211021142705.jpg",
            12
        ),
        BannerItem(
            "https://c-shop-prod.oss-cn-hangzhou.aliyuncs.com/apk/banner/20211021142712.png",
            13
        ),
        BannerItem(
            "https://c-shop-prod.oss-cn-hangzhou.aliyuncs.com/apk/banner/20211116101006.jpg",
            14
        ),
        BannerItem(
            "https://c-shop-prod.oss-cn-hangzhou.aliyuncs.com/apk/banner/20211116100955.jpg",
            15
        ),
        BannerItem(
            "https://c-shop-prod.oss-cn-hangzhou.aliyuncs.com/apk/banner/20211116100947.jpg",
            16
        ),
        BannerItem(
            "https://c-shop-prod.oss-cn-hangzhou.aliyuncs.com/apk/banner/20211116100940.jpg",
            17
        ),
        BannerItem(
            "https://c-shop-prod.oss-cn-hangzhou.aliyuncs.com/apk/banner/20211116100933.jpg",
            18
        ),
        BannerItem(
            "https://c-shop-prod.oss-cn-hangzhou.aliyuncs.com/apk/banner/20211116100925.jpg",
            19
        ),
        BannerItem(
            "https://c-shop-prod.oss-cn-hangzhou.aliyuncs.com/apk/banner/20211116100915.jpg",
            20
        ),
        BannerItem(
            "https://c-shop-prod.oss-cn-hangzhou.aliyuncs.com/apk/banner/20211116100905.jpg",
            21
        ),
        BannerItem(
            "https://c-shop-prod.oss-cn-hangzhou.aliyuncs.com/apk/banner/20211116100853.jpg",
            22
        ),
        BannerItem(
            "https://c-shop-prod.oss-cn-hangzhou.aliyuncs.com/apk/banner/20220110114334.jpg",
            23
        ),
        BannerItem(
            "https://c-shop-prod.oss-cn-hangzhou.aliyuncs.com/apk/banner/20220110114323.jpg",
            24
        )
    )

    override fun useLightMode() = false

    override fun useEventBus() = true

    override fun createPresenter() = ShopSettingPresenterImpl(this, this)

    override fun getViewBinding() = ActivityShopSettingBinding.inflate(layoutInflater)

    private var shopManage: ApplyShopInfo? = null

    private var licenceImg: String? = null

    private val mCoroutine: CoroutineSupport by lazy { CoroutineSupport() }

    override fun initBundles() {
        super.initBundles()
        shopManage = intent?.getSerializableExtra("item") as ApplyShopInfo
    }

    override fun initView() {

        mToolBarDelegate?.setMidTitle("????????????")
        //????????????
        if (shopManage != null) {
            onShopManageSuccess(shopManage!!)
        } else {
            mPresenter?.requestShopManageData()
        }

        //????????????
        mBinding.ivShopManageLogo.setOnClickListener {
            //??????logo
            val pop =
                MediaMenuPop(
                    context!!,
                    MediaMenuPop.TYPE_SELECT_PHOTO or MediaMenuPop.TYPE_PLAY_PHOTO
                )
            pop.setOnClickListener { flags ->
                run {
                    when (flags) {
                        MediaMenuPop.TYPE_SELECT_PHOTO -> {
                            PictureSelector.create(this)
                                .openGallery(PictureMimeType.ofImage())
                                .maxSelectNum(1)
                                .loadImageEngine(GlideEngine.createGlideEngine())
                                .forResult(object : OnResultCallbackListener<LocalMedia?> {
                                    override fun onResult(result: List<LocalMedia?>) {
                                        // ????????????
                                        val localMedia = result[0]
                                        showAndUploadImage(localMedia)
                                    }

                                    override fun onCancel() {
                                        // ??????
                                    }
                                })
                        }

                        MediaMenuPop.TYPE_PLAY_PHOTO -> {
                            PictureSelector.create(this)
                                .openCamera(PictureMimeType.ofImage())
                                .maxSelectNum(1)
                                .loadImageEngine(GlideEngine.createGlideEngine())
                                .forResult(object : OnResultCallbackListener<LocalMedia?> {
                                    override fun onResult(result: List<LocalMedia?>) {
                                        // ????????????
                                        val localMedia = result[0]
                                        showAndUploadImage(localMedia)
                                    }

                                    override fun onCancel() {
                                        // ??????
                                    }
                                })
                        }
                    }
                }
            }
            pop.showPopupWindow()
        }

        //????????????,????????????
//        mBinding.rlShopManageName.setOnClickListener {
//            DialogUtils.showInputDialog(
//                this,
//                "????????????",
//                "",
//                "?????????",
//                shopManage?.shopName,
//                "??????",
//                "??????",
//                null
//            ) {
//                mBinding.tvShopManageName.text = it
//                shopManage?.shopName = it
//                val request = ApplyShopInfo()
//                val loginInfo = UserManager.getLoginInfo()
//                loginInfo?.let { info -> request.shopId = info.shopId }
//                request.shopName = it
//                mPresenter?.updateShopManage(request)
//            }
//        }

        //????????????
        mBinding.tvShopManageSlogan.setOnClickListener {
            DialogUtils.showInputDialog(
                this,
                "????????????",
                "",
                "?????????",
                shopManage?.shopSlogan,
                "??????",
                "??????",
                null
            ) {
                mBinding.tvShopManageSlogan.text = it
                val request = ApplyShopInfo()
                val loginInfo = UserManager.getLoginInfo()
                loginInfo?.let { info -> request.shopId = info.shopId }
                request.shopSlogan = it
                mPresenter?.updateShopSlogan(request)
            }
        }

        //???????????????
        mBinding.tvShopManageSloganHint.singleClick {
            DialogUtils.showDialog(this, R.mipmap.ic_shop_slogan)
        }

        mBinding.tvShopManageRemark.setOnClickListener {
            //????????????
            DialogUtils.showInputDialog(
                this,
                "????????????",
                "",
                "?????????",
                shopManage?.shopNotice,
                "??????",
                "??????",
                null
            ) {
                mBinding.tvShopManageRemark.text = it
                val request = ApplyShopInfo()
                val loginInfo = UserManager.getLoginInfo()
                loginInfo?.let { info -> request.shopId = info.shopId }
                request.shopNotice = it
                mPresenter?.updateShopNotice(request)
            }
        }

        mBinding.tvShopManageRemarkHint.singleClick {
            DialogUtils.showDialog(this, R.mipmap.ic_shop_remark)
        }

        mBinding.rlShopManageDesc.setOnClickListener {
            //????????????
            DialogUtils.showMultInputDialog(
                this,
                "????????????",
                "",
                "???????????????????????????~",
                "??????",
                "??????",
                null
            ) {
                mBinding.tvShopManageDesc.text = it
                val request = ApplyShopInfo()
                val loginInfo = UserManager.getLoginInfo()
                loginInfo?.let { info -> request.shopId = info.shopId }
                request.shopDesc = it
                mPresenter?.updateShopManage(request)
            }
        }

        //????????????
        mBinding.rlShopManageAddress.singleClick {
            ShopAddressActivity.openActivity(context!!, shopManage)
        }
        //????????????
        mBinding.rlShopManageQualification.setOnClickListener {
            //????????????
            if (licenceImg != null) {
                val bundle = Bundle().apply {
                    putStringArrayList("list", arrayListOf<String>(licenceImg!!))
                    putInt("position", 0)
                }
                val intent = Intent(context, PhotoListActivity::class.java)
                intent.putExtra("Photo", bundle)
                context?.startActivity(intent)
            }
        }

        //???????????????
        mBinding.rlShopManageContactName.setOnClickListener {
            //???????????????
            DialogUtils.showInputDialog(
                this,
                "???????????????",
                "",
                "?????????",
                shopManage?.linkName,
                "??????",
                "??????",
                null
            ) {
                mBinding.tvShopManageContactName.text = it
                val request = ApplyShopInfo()
                val loginInfo = UserManager.getLoginInfo()
                loginInfo?.let { info -> request.shopId = info.shopId }
                request.linkName = it
                mPresenter?.updateShopManage(request)
            }
        }
        //??????
        mBinding.rlShopManageServicePhone.setOnClickListener {
            //????????????
            DialogUtils.showInputDialog(
                this,
                "?????????????????????",
                "",
                "?????????",
                shopManage?.linkPhone,
                "??????",
                "??????",
                null
            ) {
                mBinding.tvShopManageServicePhone.text = it
                val request = ApplyShopInfo()
                val loginInfo = UserManager.getLoginInfo()
                loginInfo?.let { info -> request.shopId = info.shopId }
                request.linkPhone = it
                mPresenter?.updateShopManage(request)
            }
        }

        //?????????
        photoSelect.singleClick {
            val intent = Intent(this, BannerActivity::class.java)
            intent.putExtra("number", galleryRv.getSelectPhotos().size)
            startActivityForResult(intent, 22)
        }

        tvShopSettingSubmit.singleClick {
            if (shopManage?.shopLogo.isNullOrEmpty()) {
                showToast("???????????????LOGO")
                return@singleClick
            }
            mPresenter?.setBanner(mBinding.galleryRv.getSelectPhotos())
        }

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == 22 && resultCode == Activity.RESULT_OK) {

            //?????????Banner
            val tempList = mutableListOf<GoodsGalleryVO>()
            //?????????List
            val list = data?.getStringExtra("list")?.split(",")

            try {
                list?.also {
                    if (it.isNotEmpty()) {
                        for (i in list) {
                            if (i.isNotEmpty()) {
                                tempList.add(GoodsGalleryVO(null, bannerArray[i.toInt()].url, null))
                            }
                        }
                    }
                }

                mBinding.galleryRv.addDataList(tempList)
            } catch (e: Exception) {
                showToast("????????????????????????????????????")
            }
        }
    }

    override fun onUpdateSloganSuccess(string: String?) {
        hidePageLoading()
        showToast("????????????")
        tvShopManageSlogan?.text = string
        shopManage?.shopSlogan = string
    }

    override fun onUpdateNoticeSuccess(string: String?) {
        hidePageLoading()
        showToast("????????????")
        mBinding.tvShopManageRemark.text = string
        shopManage?.shopNotice = string
    }

    //???UI???????????????
    override fun onShopManageSuccess(bean: ApplyShopInfo) {
        hidePageLoading()
        shopManage = bean
        //????????????
        if (!TextUtils.isEmpty(bean.shopLogo)) GlideUtils.setImageUrl(
            mBinding.ivShopManageLogo,
            bean.shopLogo
        )
        mBinding.tvShopManageName.text = bean.shopName
        mBinding.tvShopManageDesc.text = bean.shopDesc
        mBinding.tvShopManageCategory.text = bean.categoryNames?.replace(" ", "/")
        mBinding.tvShopManageContactName.text = bean.linkName
        mBinding.tvShopManageServicePhone.text = bean.linkPhone
        mBinding.tvShopManageAddress.text = bean.getFullAddress()
        mBinding.tvShopManageNumber.text = String.format("%s", bean.shopId)
        mBinding.tvShopManageType.text = bean.getShopTypeStr()


        mBinding.tvShopManageSlogan.text = bean.shopSlogan
        mBinding.tvShopManageRemark.text = bean.shopNotice

        licenceImg = bean.licenceImg


        //??????Banner???
        initBanner()
        mPresenter?.getBanner()
    }

    //??????Banner?????????
    private fun initBanner() {
        galleryRv.setCountLimit(1, 5)
        galleryRv.setAspectRatio(750, 176)
    }

    override fun onSetBanner(bannerList: List<BannerBean>?) {
        val list = mutableListOf<GoodsGalleryVO>()
        bannerList?.forEachIndexed { _, bannerBean ->
            list.add(GoodsGalleryVO(original = bannerBean.banner_url, sort = null))
        }
        mBinding.galleryRv.addDataList(list)
    }


    override fun onShopManageError(code: Int) {
        hidePageLoading()
    }

    private fun showAndUploadImage(localMedia: LocalMedia?) {
        GlideUtils.setImageUrl(ivShopManageLogo, OtherUtils.getImageFile(localMedia))
        mCoroutine.launch {
            val uploadFile =
                CommonRepository.uploadFile(OtherUtils.getImageFile(localMedia), true)
            if (uploadFile.isSuccess) {
                LogUtils.d(uploadFile.data.url)
                val request = ApplyShopInfo()
                val loginInfo = UserManager.getLoginInfo()
                loginInfo?.let {
                    request.shopId = it.shopId
                }
                request.shopLogo = uploadFile.data.url
                mPresenter?.updateShopManage(request)
            }
        }
    }

    override fun onUpdateShopSuccess(bean: ApplyShopInfo) {
        hideDialogLoading()
        bean.shopLogo?.apply {
            shopManage?.shopLogo = this
        }
        bean.shopName?.apply {
            shopManage?.shopName = this
        }
        bean.shopDesc?.apply {
            shopManage?.shopDesc = this
        }
        bean.linkName?.apply {
            shopManage?.linkName = this
        }
        bean.linkPhone?.apply {
            shopManage?.linkPhone = this
        }
        bean.linkPhone?.apply {
            shopManage?.linkPhone = this
        }
    }

    override fun onUpdateShopError(code: Int) {
        hideDialogLoading()
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun getUploadImageUrl(event: ShopManageImageEvent) {
        val request = ApplyShopInfo()
        val loginInfo = UserManager.getLoginInfo()
        loginInfo?.let { info -> request.shopId = info.shopId }
        request.licenceImg = event.remoteUrl
        licenceImg = event.remoteUrl
        mPresenter?.updateShopManage(request)
    }

    var addressLatLng: LatLng? = null

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun setAddress(event: ApplyShopPoiEvent) {
        addressLatLng = event.adInfo?.latLng
        shopManage?.shopLng = addressLatLng?.longitude
        shopManage?.shopLat = addressLatLng?.latitude
        shopManage?.shopAdd = event.adInfo?.address
        shopManage?.shopProvince = event.adInfo?.province
        shopManage?.shopCity = event.adInfo?.city
        shopManage?.shopCounty = event.adInfo?.district
        mBinding.tvShopManageAddress.text = shopManage?.getFullAddress()

        showDialogLoading()
        val request = ApplyShopInfo()
        val loginInfo = UserManager.getLoginInfo()
        loginInfo?.let { info -> request.shopId = info.shopId }
        request.shopLat = addressLatLng?.latitude
        request.shopLng = addressLatLng?.longitude
        request.shopAdd = event.adInfo?.address
        request.shopProvince = shopManage?.shopProvince
        request.shopCity = shopManage?.shopCity
        request.shopCounty = shopManage?.shopCounty
        mPresenter?.updateShopManage(request)
    }

    //?????????????????????Activity
    override fun onBackPressed() {
        intent.putExtra("item", shopManage)
        setResult(Activity.RESULT_OK, intent)
        super.onBackPressed()
    }

}