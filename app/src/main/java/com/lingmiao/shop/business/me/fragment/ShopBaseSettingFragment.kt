package com.lingmiao.shop.business.me.fragment

import android.content.Intent
import android.text.TextUtils
import android.view.View
import com.amap.api.mapcore.util.it
import com.amap.api.maps.model.LatLng
import com.blankj.utilcode.util.ActivityUtils
import com.blankj.utilcode.util.LogUtils
import com.james.common.base.BaseFragment
import com.james.common.netcore.coroutine.CoroutineSupport
import com.james.common.utils.DialogUtils
import com.james.common.utils.exts.singleClick
import com.lingmiao.shop.R
import com.lingmiao.shop.base.CommonRepository
import com.lingmiao.shop.base.UserManager
import com.lingmiao.shop.business.common.pop.MediaMenuPop
import com.lingmiao.shop.business.main.AddressActivity
import com.lingmiao.shop.business.main.bean.ApplyShopPoiEvent
import com.lingmiao.shop.business.me.ShopQualificationActivity
import com.lingmiao.shop.business.me.bean.ShopManage
import com.lingmiao.shop.business.me.bean.ShopManageImageEvent
import com.lingmiao.shop.business.me.bean.ShopManageRequest
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
class ShopBaseSettingFragment : BaseFragment<ShopBaseSettingPresenter>(), ShopBaseSettingPresenter.View {

    companion object {
        fun newInstance(): ShopBaseSettingFragment {
            return ShopBaseSettingFragment()
        }
    }

    override fun useEventBus(): Boolean {
        return true
    }

    private val mCoroutine: CoroutineSupport by lazy { CoroutineSupport() }

    private var shopManage: ShopManage?=null
    private var licenceImg:String?=null

    override fun getLayoutId(): Int? {
        return R.layout.me_fragment_shop_base_setting;
    }

    override fun createPresenter(): ShopBaseSettingPresenter? {
        return ShopBaseSettingPresenterImpl(context!!, this);
    }

    override fun initViewsAndData(rootView: View) {
        ivShopManageLogo.setOnClickListener{
            //店铺logo
            val pop =
                MediaMenuPop(context!!, MediaMenuPop.TYPE_SELECT_PHOTO or MediaMenuPop.TYPE_PLAY_PHOTO)
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
//            PictureSelector.create(this)
//                .openGallery(PictureMimeType.ofImage())
//                .maxSelectNum(1)
//                .loadImageEngine(GlideEngine.createGlideEngine())
//                .forResult(object : OnResultCallbackListener<LocalMedia?> {
//                    override fun onResult(result: List<LocalMedia?>) {
//                        // 结果回调
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
//                        // 取消
//                    }
//                })
        }

        rlShopManageName.setOnClickListener{
            //店铺名称
            DialogUtils.showInputDialog(activity!!, "店铺名称", "", "请输入","取消", "保存",null) {
                tvShopManageName.text = it
                showDialogLoading()
                val request = ShopManageRequest()
                val loginInfo = UserManager.getLoginInfo()
                loginInfo?.let { info-> request.shopId = info.shopId }
                request.shopName = it
                mPresenter?.updateShopManage(request)
            }
        }

        tvShopManageSlogan.setOnClickListener{
            //店铺口号
            DialogUtils.showInputDialog(activity!!, "店铺口号", "", "请输入","取消", "保存",null) {
                tvShopManageSlogan.text = it;
            }
        }

        tvShopManageSloganHint.singleClick {
            DialogUtils.showDialog(activity!!, R.mipmap.ic_shop_slogan);
        }

        tvShopManageRemark.setOnClickListener {
            //店铺公告
            DialogUtils.showInputDialog(activity!!, "店铺公告", "", "请输入","取消", "保存",null) {
                tvShopManageRemark.text = it;
            }
        }

        tvShopManageRemarkHint.singleClick {
            DialogUtils.showDialog(activity!!, R.mipmap.ic_shop_remark);
        }

        rlShopManageDesc.setOnClickListener{
            //店铺简介
            DialogUtils.showMultInputDialog(activity!!, "店铺简介", "", "请简单介绍你的店铺~","取消", "保存",null) {
                tvShopManageDesc.text = it
                showDialogLoading()
                val request = ShopManageRequest()
                val loginInfo = UserManager.getLoginInfo()
                loginInfo?.let { info-> request.shopId = info.shopId }
                request.shopDesc = it
                mPresenter?.updateShopManage(request)
            }
        }
        rlShopManageAddress.singleClick {
            AddressActivity.openActivity(context!!, addressLatLng);
        }
        rlShopManageQualification.setOnClickListener{
            //店铺资质
            val qualificationIntent = Intent(context, ShopQualificationActivity::class.java)
            qualificationIntent.putExtra("imageUrl",licenceImg)
            startActivity(qualificationIntent)
        }
        rlShopManageContactName.setOnClickListener{
            //紧急联系人
            DialogUtils.showInputDialog(activity!!, "紧急联系人", "", "请输入","取消", "保存",null) {
                tvShopManageContactName.text = it
                showDialogLoading()
                val request = ShopManageRequest()
                val loginInfo = UserManager.getLoginInfo()
                loginInfo?.let { info-> request.shopId = info.shopId }
                request.linkName = it
                mPresenter?.updateShopManage(request)
            }
        }
        rlShopManageServicePhone.setOnClickListener{
            //客服电话
            DialogUtils.showInputDialog(activity!!, "客服电话", "", "请输入","取消", "保存",null) {
                tvShopManageServicePhone.text = it
                showDialogLoading()
                val request = ShopManageRequest()
                val loginInfo = UserManager.getLoginInfo()
                loginInfo?.let { info-> request.shopId = info.shopId }
                request.linkPhone = it
                mPresenter?.updateShopManage(request)
            }
        }
        // 保存
        tvShopSettingSubmit.singleClick {
            // mPresenter?.updateShopManage(request);
        }
        showPageLoading()
        mPresenter?.requestShopManageData()

    }

    private fun showAndUploadImage(localMedia: LocalMedia?) {
        GlideUtils.setImageUrl(ivShopManageLogo, OtherUtils.getImageFile(localMedia))
        mCoroutine.launch {
            showDialogLoading()
            val uploadFile =
                CommonRepository.uploadFile(OtherUtils.getImageFile(localMedia), true)
            if (uploadFile.isSuccess) {
                LogUtils.d(uploadFile.data.url)
                val request = ShopManageRequest()
                val loginInfo = UserManager.getLoginInfo()
                loginInfo?.let {
                    request.shopId = it.shopId
                }
                request.shopLogo = uploadFile.data.url
                mPresenter?.updateShopManage(request)
            }else{
                hideDialogLoading()
            }
        }
    }

    override fun onShopManageSuccess(bean: ShopManage) {
        hidePageLoading()
        shopManage = bean
        if(!TextUtils.isEmpty(bean.shopLogo)) GlideUtils.setImageUrl(ivShopManageLogo,bean.shopLogo)
        tvShopManageName.text = bean.shopName
        tvShopManageDesc.text = bean.shopDesc
        tvShopManageCategory.text = bean.categoryNames?.replace(" ","/")
        tvShopManageContactName.text = bean.linkName
        tvShopManageServicePhone.text = bean.linkPhone
        tvShopManageAddress.text = bean.shopAdd
        licenceImg = bean.licenceImg
    }

    override fun onShopManageError(code: Int) {
        hidePageLoading()
    }

    override fun onUpdateShopSuccess() {
        hideDialogLoading()
        showToast("修改成功")
    }

    override fun onUpdateShopError(code: Int) {
        hideDialogLoading()
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun getUploadImageUrl(event: ShopManageImageEvent) {
        val request = ShopManageRequest()
        val loginInfo = UserManager.getLoginInfo()
        loginInfo?.let { info-> request.shopId = info.shopId }
        request.licenceImg = event.remoteUrl
        licenceImg = event.remoteUrl
        mPresenter?.updateShopManage(request)
    }

    var addressLatLng : LatLng?= null;

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun setAddress(event : ApplyShopPoiEvent) {
        tvShopManageAddress.text = event?.adInfo?.address;
        addressLatLng = event?.adInfo?.latLng;


        showDialogLoading()
        val request = ShopManageRequest()
        val loginInfo = UserManager.getLoginInfo()
        loginInfo?.let { info-> request.shopId = info.shopId }
        request.shopLat = addressLatLng?.latitude
        request.shopLng = addressLatLng?.longitude
        mPresenter?.updateShopManage(request)
    }

}