package com.lingmiao.shop.business.me

import com.blankj.utilcode.util.LogUtils
import com.lingmiao.shop.R
import com.lingmiao.shop.base.CommonRepository
import com.lingmiao.shop.business.common.pop.MediaMenuPop
import com.lingmiao.shop.business.main.bean.ApplyShopImageEvent
import com.lingmiao.shop.business.me.bean.ShopManageImageEvent
import com.lingmiao.shop.business.me.bean.ShopQualification
import com.lingmiao.shop.business.me.presenter.ShopQualificationPresenter
import com.lingmiao.shop.business.me.presenter.impl.ShopQualificationPresenterImpl
import com.lingmiao.shop.business.photo.GlideEngine
import com.lingmiao.shop.util.GlideUtils
import com.lingmiao.shop.util.OtherUtils
import com.james.common.base.BaseActivity
import com.james.common.netcore.coroutine.CoroutineSupport
import com.luck.picture.lib.PictureSelector
import com.luck.picture.lib.config.PictureMimeType
import com.luck.picture.lib.entity.LocalMedia
import com.luck.picture.lib.listener.OnResultCallbackListener
import kotlinx.android.synthetic.main.me_activity_shop_qualification.*
import kotlinx.coroutines.launch
import org.greenrobot.eventbus.EventBus
import java.io.File

/**
*   店铺资质
*/
class ShopQualificationActivity : BaseActivity<ShopQualificationPresenter>(),ShopQualificationPresenter.View {


    private val mCoroutine: CoroutineSupport by lazy { CoroutineSupport() }

    override fun getLayoutId(): Int {
        return R.layout.me_activity_shop_qualification
    }

    override fun useLightMode(): Boolean {
        return false
    }

    override fun initView() {
        mToolBarDelegate.setMidTitle("店铺资质")

        val imageUrl :String?= intent.getStringExtra("imageUrl")
        imageUrl?.let {
            GlideUtils.setImageUrl(ivShopUpload, imageUrl,5f)
        }
        rlShopQualificationUpload.setOnClickListener {
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



    override fun createPresenter(): ShopQualificationPresenter {
        return ShopQualificationPresenterImpl(this, this)
    }

    override fun onShopQualificationSuccess(bean: ShopQualification) {
        hidePageLoading()
    }

    override fun onShopQualificationError(code: Int) {
        hidePageLoading()
    }


    private fun showAndUploadImage(localMedia: LocalMedia?) {
            GlideUtils.setImageUrl(ivShopUpload, OtherUtils.getImageFile(localMedia),5f)
            mCoroutine.launch {
            val uploadFile = CommonRepository.uploadFile(OtherUtils.getImageFile(localMedia), true)
            if (uploadFile.isSuccess) {
                LogUtils.d(uploadFile.data.url)
//                showToast("上传成功")
                EventBus.getDefault().post(ShopManageImageEvent("",uploadFile.data.url))
            }
        }
    }
}