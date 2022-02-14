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
import com.lingmiao.shop.business.main.ShopAddressActivity
import com.lingmiao.shop.business.main.bean.ApplyShopInfo
import com.lingmiao.shop.business.main.bean.ApplyShopPoiEvent
import com.lingmiao.shop.business.me.bean.ShopManageImageEvent
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
import kotlinx.coroutines.launch
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode

class ShopSettingActivity :
    BaseVBActivity<ActivityShopSettingBinding, ShopSettingPresenter>(),
    ShopSettingPresenter.View {

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

        //加载数据
        if (shopManage != null) {
            onShopManageSuccess(shopManage!!)
        } else {
            mPresenter?.requestShopManageData()
        }

        //店铺头像
        mBinding.ivShopManageLogo.setOnClickListener {
            //店铺logo
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
                                        // 结果回调
                                        val localMedia = result[0]
                                        showAndUploadImage(localMedia)
                                    }

                                    override fun onCancel() {
                                        // 取消
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
                                        // 结果回调
                                        val localMedia = result[0]
                                        showAndUploadImage(localMedia)
                                    }

                                    override fun onCancel() {
                                        // 取消
                                    }
                                })
                        }
                    }
                }
            }
            pop.showPopupWindow()
        }

        //店铺名字
        mBinding.rlShopManageName.setOnClickListener {
            DialogUtils.showInputDialog(
                this,
                "店铺名称",
                "",
                "请输入",
                shopManage?.shopName,
                "取消",
                "保存",
                null
            ) {
                mBinding.tvShopManageName.text = it
                shopManage?.shopName = it
                val request = ApplyShopInfo()
                val loginInfo = UserManager.getLoginInfo()
                loginInfo?.let { info -> request.shopId = info.shopId }
                request.shopName = it
                mPresenter?.updateShopManage(request)
            }
        }

        //店铺口号
        mBinding.tvShopManageSlogan.setOnClickListener {
            DialogUtils.showInputDialog(
                this,
                "店铺口号",
                "",
                "请输入",
                shopManage?.shopSlogan,
                "取消",
                "保存",
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

        //什么是口号
        mBinding.tvShopManageSloganHint.singleClick {
            DialogUtils.showDialog(this, R.mipmap.ic_shop_slogan)
        }

        mBinding.tvShopManageRemark.setOnClickListener {
            //店铺公告
            DialogUtils.showInputDialog(
                this,
                "店铺公告",
                "",
                "请输入",
                shopManage?.shopNotice,
                "取消",
                "保存",
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
            //店铺简介
            DialogUtils.showMultInputDialog(
                this,
                "店铺简介",
                "",
                "请简单介绍你的店铺~",
                "取消",
                "保存",
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

        //店铺地址
        mBinding.rlShopManageAddress.singleClick {
            ShopAddressActivity.openActivity(context!!, shopManage)
        }
        //店铺资质
        mBinding.rlShopManageQualification.setOnClickListener {
            //店铺资质
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

        //紧急联系人
        mBinding.rlShopManageContactName.setOnClickListener {
            //紧急联系人
            DialogUtils.showInputDialog(
                this,
                "紧急联系人",
                "",
                "请输入",
                shopManage?.linkName,
                "取消",
                "保存",
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
        //电话
        mBinding.rlShopManageServicePhone.setOnClickListener {
            //客服电话
            DialogUtils.showInputDialog(
                this,
                "紧急联系人电话",
                "",
                "请输入",
                shopManage?.linkPhone,
                "取消",
                "保存",
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

    }

    override fun onUpdateSloganSuccess(string: String?) {
        hidePageLoading()
        showToast("修改成功")
        tvShopManageSlogan?.text = string
        shopManage?.shopSlogan = string
    }

    override fun onUpdateNoticeSuccess(string: String?) {
        hidePageLoading()
        showToast("修改成功")
        mBinding.tvShopManageRemark.text = string
        shopManage?.shopNotice = string
    }

    //在UI上添加数据
    override fun onShopManageSuccess(bean: ApplyShopInfo) {
        hidePageLoading()
        shopManage = bean
        //店铺头像
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

    var addressLatLng: LatLng? = null;

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

    //返回数据至上一Activity
    override fun onBackPressed() {
        intent.putExtra("item", shopManage)
        setResult(Activity.RESULT_OK, intent)
        super.onBackPressed()
    }


}