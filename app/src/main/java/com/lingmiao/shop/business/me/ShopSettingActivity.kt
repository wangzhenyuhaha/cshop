package com.lingmiao.shop.business.me

import android.app.Activity
import android.util.Log
import com.blankj.utilcode.util.LogUtils
import com.james.common.base.BaseVBActivity
import com.james.common.netcore.coroutine.CoroutineSupport
import com.lingmiao.shop.base.CommonRepository
import com.lingmiao.shop.base.UserManager
import com.lingmiao.shop.business.common.pop.MediaMenuPop
import com.lingmiao.shop.business.main.bean.ApplyShopInfo
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

class ShopSettingActivity :
    BaseVBActivity<ActivityShopSettingBinding, ShopSettingPresenter>(),
    ShopSettingPresenter.View {

    override fun useLightMode() = false

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
                //mPresenter?.updateShopManage(request)
            }
        }
    }

    override fun onBackPressed() {
        intent.putExtra("item", shopManage)
        setResult(Activity.RESULT_OK, intent)
        super.onBackPressed()
    }

}