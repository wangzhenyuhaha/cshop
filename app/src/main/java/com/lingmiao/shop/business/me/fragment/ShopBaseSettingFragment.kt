package com.lingmiao.shop.business.me.fragment

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import com.amap.api.maps.model.LatLng
import com.blankj.utilcode.util.LogUtils
import com.james.common.base.BaseFragment
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
import com.lingmiao.shop.business.me.ShopQualificationActivity
import com.lingmiao.shop.business.me.bean.ShopManageImageEvent
import com.lingmiao.shop.business.me.presenter.ShopBaseSettingPresenter
import com.lingmiao.shop.business.me.presenter.impl.ShopBaseSettingPresenterImpl
import com.lingmiao.shop.business.photo.GlideEngine
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

/**
Create Date : 2021/3/24:10 PM
Auther      : Fox
Desc        :
 **/

@SuppressLint("UseRequireInsteadOfGet")
class ShopBaseSettingFragment : BaseFragment<ShopBaseSettingPresenter>(),
    ShopBaseSettingPresenter.View {

    private var shopManage: ApplyShopInfo? = null

    private var licenceImg: String? = null

    private val mCoroutine: CoroutineSupport by lazy { CoroutineSupport() }

    companion object {
        fun newInstance(item: ApplyShopInfo?): ShopBaseSettingFragment {
            return ShopBaseSettingFragment().apply {
                arguments = Bundle().apply {
                    putSerializable("item", item)
                }
            }
        }
    }

    override fun initBundles() {
        shopManage = arguments?.getSerializable("item") as ApplyShopInfo
    }

    override fun useEventBus(): Boolean {
        return true
    }

    override fun getLayoutId(): Int {
        return R.layout.me_fragment_shop_base_setting
    }

    override fun createPresenter(): ShopBaseSettingPresenter {
        return ShopBaseSettingPresenterImpl(context!!, this)
    }

    override fun initViewsAndData(rootView: View) {
        ivShopManageLogo.setOnClickListener {
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
//            PictureSelector.create(this)
//                .openGallery(PictureMimeType.ofImage())
//                .maxSelectNum(1)
//                .loadImageEngine(GlideEngine.createGlideEngine())
//                .forResult(object : OnResultCallbackListener<LocalMedia?> {
//                    override fun onResult(result: List<LocalMedia?>) {
//                        // ????????????
//                        val localMedia = result[0]
//                        GlideUtils.setImageUrl(ivShopManageLogo, OtherUtils.getImageFile(localMedia))
//                        mCoroutine.launch {
//                            showDialogLoading()
//                            val uploadFile =
//                                CommonRepository.uploadFile(OtherUtils.getImageFile(localMedia), true)
//                            if (uploadFile.isSuccess) {
//                                LogUtils.d(uploadFile.data.url)
//                                val request = ShopManageRequest()
//                                val loginInfo = UserManager.getLoginInfo()
//                                loginInfo?.let {
//                                    request.shopId = it.shopId
//                                }
//                                request.shopLogo = uploadFile.data.url
//                                mPresenter?.updateShopManage(request)
//                            }else{
//                                hideDialogLoading()
//                            }
//                        }
//                    }
//
//                    override fun onCancel() {
//                        // ??????
//                    }
//                })
        }

        rlShopManageName.setOnClickListener {
            //????????????
            DialogUtils.showInputDialog(
                activity!!,
                "????????????",
                "",
                "?????????",
                shopManage?.shopName,
                "??????",
                "??????",
                null
            ) {
                tvShopManageName.text = it
                shopManage?.shopName = it
//                showDialogLoading()
                val request = ApplyShopInfo()
                val loginInfo = UserManager.getLoginInfo()
                loginInfo?.let { info -> request.shopId = info.shopId }
                request.shopName = it
                mPresenter?.updateShopManage(request)
            }
        }

        tvShopManageSlogan.setOnClickListener {
            //????????????
            DialogUtils.showInputDialog(
                activity!!,
                "????????????",
                "",
                "?????????",
                shopManage?.shopSlogan,
                "??????",
                "??????",
                null
            ) {
                tvShopManageSlogan.text = it
                val request = ApplyShopInfo()
                val loginInfo = UserManager.getLoginInfo()
                loginInfo?.let { info -> request.shopId = info.shopId }
                request.shopSlogan = it
                mPresenter?.updateShopSlogan(request)
            }
        }

        tvShopManageSloganHint.singleClick {
            DialogUtils.showDialog(activity!!, R.mipmap.ic_shop_slogan)
        }
        tvShopManageRemark.setOnClickListener {
            //????????????
            DialogUtils.showInputDialog(
                activity!!,
                "????????????",
                "",
                "?????????",
                shopManage?.shopNotice,
                "??????",
                "??????",
                null
            ) {
                tvShopManageRemark.text = it
                val request = ApplyShopInfo()
                val loginInfo = UserManager.getLoginInfo()
                loginInfo?.let { info -> request.shopId = info.shopId }
                request.shopNotice = it
                mPresenter?.updateShopNotice(request)
            }
        }

        tvShopManageRemarkHint.singleClick {
            DialogUtils.showDialog(activity!!, R.mipmap.ic_shop_remark)
        }

        rlShopManageDesc.setOnClickListener {
            //????????????
            DialogUtils.showMultInputDialog(
                activity!!,
                "????????????",
                "",
                "???????????????????????????~",
                "??????",
                "??????",
                null
            ) {
                tvShopManageDesc.text = it
                val request = ApplyShopInfo()
                val loginInfo = UserManager.getLoginInfo()
                loginInfo?.let { info -> request.shopId = info.shopId }
                request.shopDesc = it
                mPresenter?.updateShopManage(request)
            }
        }
        rlShopManageAddress.singleClick {
//            AddressActivity.openActivity(context!!, shopManage)
            ShopAddressActivity.openActivity(context!!, shopManage)
        }
        rlShopManageQualification.setOnClickListener {
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
//            val qualificationIntent = Intent(context, ShopQualificationActivity::class.java)
//            qualificationIntent.putExtra("imageUrl",licenceImg)
//            startActivity(qualificationIntent)
        }
        rlShopManageContactName.setOnClickListener {
            //???????????????
            DialogUtils.showInputDialog(
                activity!!,
                "???????????????",
                "",
                "?????????",
                shopManage?.linkName,
                "??????",
                "??????",
                null
            ) {
                tvShopManageContactName.text = it
                val request = ApplyShopInfo()
                val loginInfo = UserManager.getLoginInfo()
                loginInfo?.let { info -> request.shopId = info.shopId }
                request.linkName = it
                mPresenter?.updateShopManage(request)
            }
        }
        rlShopManageServicePhone.setOnClickListener {
            //????????????
            DialogUtils.showInputDialog(
                activity!!,
                "?????????????????????",
                "",
                "?????????",
                shopManage?.linkPhone,
                "??????",
                "??????",
                null
            ) {
                tvShopManageServicePhone.text = it
                val request = ApplyShopInfo()
                val loginInfo = UserManager.getLoginInfo()
                loginInfo?.let { info -> request.shopId = info.shopId }
                request.linkPhone = it
                mPresenter?.updateShopManage(request)
            }
        }
        // ??????
        tvShopSettingSubmit.singleClick {
            // mPresenter?.updateShopManage(request);
        }
        if (shopManage != null) {
            onShopManageSuccess(shopManage!!)
        } else {
            mPresenter?.requestShopManageData()
        }
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

    override fun onUpdateSloganSuccess(string: String?) {
        hidePageLoading()
        showToast("????????????")
        tvShopManageSlogan?.setText(string)
        shopManage?.shopSlogan = string
    }

    override fun onUpdateNoticeSuccess(string: String?) {
        hidePageLoading()
        showToast("????????????")
        tvShopManageRemark?.setText(string)
        shopManage?.shopNotice = string
    }

    override fun onShopManageSuccess(bean: ApplyShopInfo) {
        hidePageLoading()
        shopManage = bean
        if (!TextUtils.isEmpty(bean.shopLogo)) GlideUtils.setImageUrl(
            ivShopManageLogo,
            bean.shopLogo
        )
        tvShopManageName.text = bean.shopName
        tvShopManageDesc.text = bean.shopDesc
        tvShopManageCategory.text = bean.categoryNames?.replace(" ", "/")
        tvShopManageContactName.text = bean.linkName
        tvShopManageServicePhone.text = bean.linkPhone
        tvShopManageAddress.text = bean.getFullAddress()
        tvShopManageNumber.text = String.format("%s", bean.shopId)
        tvShopManageType.text = bean.getShopTypeStr()


        tvShopManageSlogan.text = bean.shopSlogan
        tvShopManageRemark.text = bean.shopNotice

        licenceImg = bean.licenceImg
    }

    override fun onShopManageError(code: Int) {
        hidePageLoading()
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
        addressLatLng = event?.adInfo?.latLng
        shopManage?.shopLng = addressLatLng?.longitude
        shopManage?.shopLat = addressLatLng?.latitude
        shopManage?.shopAdd = event?.adInfo?.address
        shopManage?.shopProvince = event?.adInfo?.province
        shopManage?.shopCity = event?.adInfo?.city
        shopManage?.shopCounty = event?.adInfo?.district
        tvShopManageAddress.text = shopManage?.getFullAddress()

        showDialogLoading()
        val request = ApplyShopInfo()
        val loginInfo = UserManager.getLoginInfo()
        loginInfo?.let { info -> request.shopId = info.shopId }
        request.shopLat = addressLatLng?.latitude
        request.shopLng = addressLatLng?.longitude
        request.shopAdd = event?.adInfo?.address
        request.shopProvince = shopManage?.shopProvince
        request.shopCity = shopManage?.shopCity
        request.shopCounty = shopManage?.shopCounty
        mPresenter?.updateShopManage(request)
    }

}