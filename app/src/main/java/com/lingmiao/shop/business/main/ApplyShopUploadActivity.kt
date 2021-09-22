package com.lingmiao.shop.business.main

import com.blankj.utilcode.util.LogUtils
import com.james.common.base.BaseActivity
import com.james.common.netcore.coroutine.CoroutineSupport
import com.lingmiao.shop.R
import com.lingmiao.shop.base.CommonRepository
import com.lingmiao.shop.business.common.pop.MediaMenuPop
import com.lingmiao.shop.business.main.bean.ApplyShopImageEvent
import com.lingmiao.shop.business.main.bean.ApplyShopUpload
import com.lingmiao.shop.business.main.presenter.ApplyShopUploadPresenter
import com.lingmiao.shop.business.main.presenter.impl.ApplyShopUploadPresenterImpl
import com.lingmiao.shop.business.photo.GlideEngine
import com.lingmiao.shop.util.GlideUtils
import com.lingmiao.shop.util.OtherUtils
import com.luck.picture.lib.PictureSelector
import com.luck.picture.lib.config.PictureMimeType
import com.luck.picture.lib.entity.LocalMedia
import com.luck.picture.lib.listener.OnResultCallbackListener
import kotlinx.android.synthetic.main.main_activity_apply_shop_upload.*
import kotlinx.coroutines.launch
import org.greenrobot.eventbus.EventBus

/**
 *   首页-提交资料-上传图片
 */
class ApplyShopUploadActivity : BaseActivity<ApplyShopUploadPresenter>(),
    ApplyShopUploadPresenter.View {

    private var type: Int = 1

    private val mCoroutine: CoroutineSupport by lazy { CoroutineSupport() }


    override fun getLayoutId(): Int {
        return R.layout.main_activity_apply_shop_upload
    }

    override fun useLightMode(): Boolean {
        return false
    }

    override fun initView() {

        type = intent.getIntExtra("type", 1)
        mToolBarDelegate.setMidTitle(intent.getStringExtra("title"))

        imageUrl = intent.getStringExtra("imageUrl")
        imageUrl?.let {
            GlideUtils.setImageUrl(ivShopUpload, imageUrl,12f)
        }

        tvUploadSubmit.setOnClickListener {
            if(imageUrl?.isNotEmpty() == true) {
                EventBus.getDefault().post(ApplyShopImageEvent("",imageUrl, type))
                finish()
            }
        }

        rlShopUpload.setOnClickListener {
            val pop =
                MediaMenuPop(this, MediaMenuPop.TYPE_SELECT_PHOTO or MediaMenuPop.TYPE_PLAY_PHOTO)
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

    private var imageUrl : String ?= ""
    private fun showAndUploadImage(localMedia: LocalMedia?) {
        GlideUtils.setImageUrl(ivShopUpload, OtherUtils.getImageFile(localMedia),12f)
        mCoroutine.launch {
            val uploadFile = CommonRepository.uploadFile(OtherUtils.getImageFile(localMedia), true)
            if (uploadFile.isSuccess) {
                imageUrl = uploadFile.data.url
                LogUtils.d(uploadFile.data.url)
//                showToast("上传成功")
                // EventBus.getDefault().post(ApplyShopImageEvent("",uploadFile.data.url, type))
            }
        }
    }


    override fun createPresenter(): ApplyShopUploadPresenter {
        return ApplyShopUploadPresenterImpl(this, this)
    }

    override fun onApplyShopUploadSuccess(bean: ApplyShopUpload) {
        hidePageLoading()
    }

    override fun onApplyShopUploadError(code: Int) {
        hidePageLoading()
    }



}