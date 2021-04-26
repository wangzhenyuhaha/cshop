package com.lingmiao.shop.business.me

import android.content.Intent
import android.text.TextUtils
import android.view.View
import com.blankj.utilcode.util.ActivityUtils
import com.blankj.utilcode.util.LogUtils
import com.lingmiao.shop.R
import com.lingmiao.shop.base.CommonRepository
import com.lingmiao.shop.base.UserManager
import com.lingmiao.shop.business.main.bean.ApplyShopImageEvent
import com.lingmiao.shop.business.me.bean.ShopManage
import com.lingmiao.shop.business.me.bean.ShopManageImageEvent
import com.lingmiao.shop.business.me.bean.ShopManageRequest
import com.lingmiao.shop.business.me.presenter.ShopManagePresenter
import com.lingmiao.shop.business.me.presenter.impl.ShopManagePresenterImpl
import com.lingmiao.shop.business.photo.GlideEngine
import com.lingmiao.shop.util.GlideUtils
import com.lingmiao.shop.util.OtherUtils
import com.james.common.base.BaseActivity
import com.james.common.netcore.coroutine.CoroutineSupport
import com.james.common.utils.DialogUtils
import com.luck.picture.lib.PictureSelector
import com.luck.picture.lib.config.PictureMimeType
import com.luck.picture.lib.entity.LocalMedia
import com.luck.picture.lib.listener.OnResultCallbackListener
import kotlinx.android.synthetic.main.me_activity_shop_manage.*
import kotlinx.coroutines.launch
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import java.io.File

/**
*   店铺管理
*/
class ShopManageActivity : BaseActivity<ShopManagePresenter>(),ShopManagePresenter.View,
    View.OnClickListener {


    private val mCoroutine: CoroutineSupport by lazy { CoroutineSupport() }
    private var shopManage:ShopManage?=null
    private var licenceImg:String?=null

    override fun getLayoutId(): Int {
        return R.layout.me_activity_shop_manage
    }

    override fun useLightMode(): Boolean {
        return false
    }

    override fun initView() {
        mToolBarDelegate.setMidTitle("店铺管理")
        // mToolBarDelegate.setToolbarBackgroundOfOtherTheme(context!!, R.color.color_secondary, R.color.white);
        ivShopManageLogo.setOnClickListener(this)
        rlShopManageName.setOnClickListener(this)
        rlShopManageDesc.setOnClickListener(this)
        rlShopManageQualification.setOnClickListener(this)
        rlShopManageContactName.setOnClickListener(this)
        rlShopManageServicePhone.setOnClickListener(this)
        showPageLoading()
        mPresenter.requestShopManageData()
    }

    override fun useEventBus(): Boolean {
        return true
    }


    override fun createPresenter(): ShopManagePresenter {
        return ShopManagePresenterImpl(this, this)
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

    override fun onClick(v: View?) {
        when(v?.id){
            R.id.ivShopManageLogo ->{//店铺logo
                PictureSelector.create(this)
                    .openGallery(PictureMimeType.ofImage())
                    .maxSelectNum(1)
                    .loadImageEngine(GlideEngine.createGlideEngine())
                    .forResult(object : OnResultCallbackListener<LocalMedia?> {
                        override fun onResult(result: List<LocalMedia?>) {
                            // 结果回调
                            val localMedia = result[0]
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
                                    mPresenter.updateShopManage(request)
                                }else{
                                    hideDialogLoading()
                                }
                            }
                        }

                        override fun onCancel() {
                            // 取消
                        }
                    })
            }
            R.id.rlShopManageName ->{//店铺名称
                DialogUtils.showInputDialog(this, "店铺名称", "", "请输入","取消", "保存",null) {
                    tvShopManageName.text = it
                    showDialogLoading()
                    val request = ShopManageRequest()
                    val loginInfo = UserManager.getLoginInfo()
                    loginInfo?.let { info-> request.shopId = info.shopId }
                    request.shopName = it
                    mPresenter.updateShopManage(request)
                }
            }
            R.id.rlShopManageDesc ->{//店铺简介
                DialogUtils.showMultInputDialog(this, "店铺简介", "", "请简单介绍你的店铺~","取消", "保存",null) {
                    tvShopManageDesc.text = it
                    showDialogLoading()
                    val request = ShopManageRequest()
                    val loginInfo = UserManager.getLoginInfo()
                    loginInfo?.let { info-> request.shopId = info.shopId }
                    request.shopDesc = it
                    mPresenter.updateShopManage(request)
                }
            }
            R.id.rlShopManageQualification ->{//店铺资质
                val qualificationIntent = Intent(this,ShopQualificationActivity::class.java)
                qualificationIntent.putExtra("imageUrl",licenceImg)
                startActivity(qualificationIntent)
            }
            R.id.rlShopManageContactName ->{//紧急联系人
                DialogUtils.showInputDialog(this, "紧急联系人", "", "请输入","取消", "保存",null) {
                    tvShopManageContactName.text = it
                    showDialogLoading()
                    val request = ShopManageRequest()
                    val loginInfo = UserManager.getLoginInfo()
                    loginInfo?.let { info-> request.shopId = info.shopId }
                    request.linkName = it
                    mPresenter.updateShopManage(request)
                }
            }
            R.id.rlShopManageServicePhone ->{//客服电话
                DialogUtils.showInputDialog(this, "客服电话", "", "请输入","取消", "保存",null) {
                    tvShopManageServicePhone.text = it
                    showDialogLoading()
                    val request = ShopManageRequest()
                    val loginInfo = UserManager.getLoginInfo()
                    loginInfo?.let { info-> request.shopId = info.shopId }
                    request.linkPhone = it
                    mPresenter.updateShopManage(request)
                }
            }


        }
    }


    @Subscribe(threadMode = ThreadMode.MAIN)
    fun getUploadImageUrl(event: ShopManageImageEvent) {
        val request = ShopManageRequest()
        val loginInfo = UserManager.getLoginInfo()
        loginInfo?.let { info-> request.shopId = info.shopId }
        request.licenceImg = event.remoteUrl
        licenceImg = event.remoteUrl
        mPresenter.updateShopManage(request)
    }

}