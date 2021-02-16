package com.lingmiao.shop.business.photo

import android.app.Activity
import android.content.pm.ActivityInfo
import android.os.Build
import com.lingmiao.shop.R
import com.lingmiao.shop.business.goods.api.bean.GoodsGalleryVO
import com.luck.picture.lib.PictureSelector
import com.luck.picture.lib.config.PictureMimeType
import com.luck.picture.lib.config.PictureSelectionConfig
import com.luck.picture.lib.entity.LocalMedia
import com.luck.picture.lib.listener.OnResultCallbackListener


/**
 * Author : Elson
 * Date   : 2020/7/19
 * Desc   : 相册选择图片
 */
class PhotoHelper {

    companion object {

        /**
         * 获取图片，图片大小超过500K，则开启压缩
         */
        @JvmStatic
        fun openAlbum(activity: Activity, maxCount: Int, cancel: (() -> Unit)?, success: ((List<LocalMedia>) -> Unit)?) {
            PictureSelector.create(activity)
                .openGallery(PictureMimeType.ofImage())
                .maxSelectNum(maxCount)
                .isCompress(true)
                .minimumCompressSize(500)
                .imageEngine(GlideEngine.createGlideEngine())
                .forResult(object : OnResultCallbackListener<LocalMedia> {
                    override fun onResult(result: List<LocalMedia>) { // 结果回调
                        success?.invoke(result)
                    }

                    override fun onCancel() { // 取消
                        cancel?.invoke()
                    }
                })
        }

        /**
         * 拍照：图片大小超过500K，则开启压缩
         */
        @JvmStatic
        fun openCamera(activity: Activity, cancel: (() -> Unit)?, success: ((List<LocalMedia>) -> Unit)?) {
            PictureSelector.create(activity)
                .openCamera(PictureMimeType.ofImage())
                .isCompress(true)
                .minimumCompressSize(500)
                .imageEngine(GlideEngine.createGlideEngine())
                .forResult(object : OnResultCallbackListener<LocalMedia> {
                    override fun onResult(result: List<LocalMedia>) { // 结果回调
                        success?.invoke(result)
                    }

                    override fun onCancel() { // 取消
                        cancel?.invoke()
                    }
                })
        }

        /**
         * 选择视频
         */
        @JvmStatic
        fun openVideo(activity: Activity, maxCount: Int = 1, cancel: (() -> Unit)?, success: ((List<LocalMedia>) -> Unit)?) {
            PictureSelector.create(activity)
                .openGallery(PictureMimeType.ofVideo())
                .maxSelectNum(maxCount)
                .imageEngine(GlideEngine.createGlideEngine())
                .forResult(object : OnResultCallbackListener<LocalMedia> {
                    override fun onResult(result: List<LocalMedia>) { // 结果回调
                        success?.invoke(result)
                    }

                    override fun onCancel() { // 取消
                        cancel?.invoke()
                    }
                })
        }

        fun previewAlbum(activity: Activity, position: Int, list: List<GoodsGalleryVO>?) {
            val newList = mutableListOf<LocalMedia>()
            list?.forEach {
                newList.add(it.convert2LocalMedia())
            }
            if (newList.isNullOrEmpty()) {
                return
            }
            PictureSelectionConfig.imageEngine = GlideEngine.createGlideEngine()
            PictureSelectionConfig.getInstance().requestedOrientation =
                ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
            PictureSelector.create(activity)
                .externalPicturePreview(position, newList, 0)
        }

        fun previewImage(activity: Activity, imageUrl : String?) {
            val newList = mutableListOf<LocalMedia>()
            var image = GoodsGalleryVO(imageUrl, imageUrl, "0");
            newList.add(image.convert2LocalMedia())
            if (newList.isNullOrEmpty()) {
                return
            }
            PictureSelector.create(activity)
                .themeStyle(R.style.picture_default_style)
                .isNotPreviewDownload(true)
                .imageEngine(GlideEngine.createGlideEngine())
                .openExternalPreview(0, newList);

        }

    }
}

/**
 * 兼容 AndroidQ 获取图片的问题
 */
fun LocalMedia.getImagePath(): String {
    return if (Build.VERSION.SDK_INT >= 29) androidQToPath else path
}