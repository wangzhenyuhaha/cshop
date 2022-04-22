package com.lingmiao.shop.business.sales

import android.content.ContentResolver
import android.content.ContentValues
import android.content.Intent
import android.graphics.Bitmap
import android.media.MediaScannerConnection
import android.os.Build
import android.os.Environment
import android.provider.MediaStore
import com.blankj.utilcode.util.ActivityUtils
import com.blankj.utilcode.util.ImageUtils
import com.blankj.utilcode.util.SnackbarUtils
import com.blankj.utilcode.util.ThreadUtils
import com.fox7.wx.WxShare
import com.james.common.base.BaseVBActivity
import com.james.common.utils.exts.singleClick
import com.lingmiao.shop.MyApp
import com.lingmiao.shop.base.IConstant
import com.lingmiao.shop.base.IWXConstant
import com.lingmiao.shop.business.me.pop.ShopQRCodePop
import com.lingmiao.shop.business.sales.presenter.WechatPublicPre
import com.lingmiao.shop.business.sales.presenter.impl.WechatPublicPreImpl
import com.lingmiao.shop.databinding.ActivityWechatPublicBinding
import com.luck.picture.lib.config.PictureMimeType
import com.tencent.mm.opensdk.modelbiz.WXLaunchMiniProgram
import com.tencent.mm.opensdk.openapi.WXAPIFactory
import kotlinx.android.synthetic.main.me_fragment_qr_image.*
import java.io.File

class WechatPublicActivity : BaseVBActivity<ActivityWechatPublicBinding, WechatPublicPre>(),
    WechatPublicPre.View {

    override fun createPresenter() = WechatPublicPreImpl(this, this)

    override fun getViewBinding() = ActivityWechatPublicBinding.inflate(layoutInflater)

    override fun initView() {

        val pop = ShopQRCodePop(this)

        //设置对PopWindow的点击
        //保存图片
        pop.setSaveListener {
            downloadImage { result ->
                SnackbarUtils.with(ivQRCode)
                    .setDuration(SnackbarUtils.LENGTH_LONG)
                    .apply {
                        if (result?.exists() == true) {
                            updateMedia(result)
                            setMessage("图片保存在" + result.absolutePath).showSuccess(true)
                            pop.dismiss()
                        } else {
                            setMessage("保存失败.").showError(true)
                            pop.dismiss()
                        }
                    }
            }
        }

        //分享二维码
        pop.setShareListener {
//            downloadImage { result ->
//                SnackbarUtils.with(ivQRCode)
//                    .setDuration(SnackbarUtils.LENGTH_LONG)
//                    .apply {
//                        if (result?.exists() == true) {
//                            val api = WXAPIFactory.createWXAPI(
//                                this@WechatPublicActivity,
//                                IWXConstant.APP_ID
//                            )
//                            val share = WxShare(this@WechatPublicActivity, api)
//                            share.shareToFriend()
//                            share.shareFile(result.path)
//                            pop.dismiss()
//                        } else {
//                            setMessage("分享失败.").showError(true)
//                            pop.dismiss()
//                        }
//                    }
//            }
            val api = WXAPIFactory.createWXAPI(context, IWXConstant.APP_ID)
            val req = WXLaunchMiniProgram.Req()
            req.userName = IWXConstant.APP_ORIGINAL_ID
            req.path = "pages/cshop-follow/cshop-follow"
            req.miniprogramType =
                if (IConstant.official) WXLaunchMiniProgram.Req.MINIPTOGRAM_TYPE_RELEASE else WXLaunchMiniProgram.Req.MINIPROGRAM_TYPE_PREVIEW
            api.sendReq(req)
        }

        //打开相册
        pop.setWatchListener {
            val intent = Intent(Intent.ACTION_VIEW).also {
                //图片列表
                it.type = "vnd.android.cursor.dir/image"
            }
            val context = ActivityUtils.getTopActivity()
            context.startActivity(intent)
            pop.dismiss()
        }

        ivQRCode.singleClick {
            pop.showPopupWindow()
        }


    }

    private fun downloadImage(callback: (result: File?) -> Unit) {
        ThreadUtils.executeBySingle(object : ThreadUtils.SimpleTask<File?>() {
            override fun doInBackground(): File? {
                return ImageUtils.save2Album(
                    ImageUtils.view2Bitmap(ivQRCode),
                    Bitmap.CompressFormat.JPEG
                )
            }

            override fun onSuccess(result: File?) {
                callback.invoke(result)
            }
        })
    }

    private fun updateMedia(file: File) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            val values = ContentValues()
            values.put(MediaStore.MediaColumns.DISPLAY_NAME, file.getName())
            values.put(
                MediaStore.MediaColumns.MIME_TYPE,
                PictureMimeType.getImageMimeType(file.absolutePath)
            )
            values.put(MediaStore.MediaColumns.RELATIVE_PATH, Environment.DIRECTORY_DCIM)
            val contentResolver: ContentResolver = contentResolver
            contentResolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values)

        } else {
            MediaScannerConnection.scanFile(
                MyApp.getInstance().applicationContext,
                arrayOf<String>(file.absolutePath),
                arrayOf("image/jpeg"),
                { path, uri -> })
        }
    }


    override fun useLightMode() = false

}