package com.lingmiao.shop.business.me

import android.content.ContentResolver
import android.content.ContentValues
import android.graphics.Bitmap
import android.media.MediaScannerConnection
import android.os.Build
import android.os.Environment
import android.provider.MediaStore
import com.blankj.utilcode.util.ImageUtils
import com.blankj.utilcode.util.SnackbarUtils
import com.blankj.utilcode.util.ThreadUtils
import com.james.common.base.BaseActivity
import com.james.common.utils.exts.singleClick
import com.lingmiao.shop.MyApp
import com.lingmiao.shop.R
import com.lingmiao.shop.base.IConstant.CACHE_PATH
import com.lingmiao.shop.business.me.presenter.IShopQRCodePre
import com.lingmiao.shop.business.me.presenter.impl.ShopQRCodePreImpl
import com.lingmiao.shop.util.GlideUtils
import com.luck.picture.lib.config.PictureMimeType
import kotlinx.android.synthetic.main.me_activity_qr_code.*
import java.io.*

/**
Create Date : 2021/6/43:07 PM
Auther      : Fox
Desc        :
 **/
class ShopQRCodeActivity : BaseActivity<IShopQRCodePre>(), IShopQRCodePre.View {

    val path = CACHE_PATH + "share.jpg";

    override fun createPresenter(): IShopQRCodePre {
        return ShopQRCodePreImpl(this, this);
    }

    override fun getLayoutId(): Int {
        return R.layout.me_activity_qr_code;
    }

    override fun initView() {
        mToolBarDelegate.setMidTitle("点击二维码可保存")

        mPresenter?.getQRCode();

        ivQRCode.singleClick {
            ThreadUtils.executeBySingle(object : ThreadUtils.SimpleTask<File?>() {
                override fun doInBackground(): File? {
                    return ImageUtils.save2Album(ImageUtils.view2Bitmap(it), Bitmap.CompressFormat.JPEG);
                }

                override fun onSuccess(result: File?) {
                    SnackbarUtils.with(it)
                        .setDuration(SnackbarUtils.LENGTH_LONG)
                        .apply {
                            if(result?.exists() == true) {
                                updateMedia(result!!);
                                setMessage("图片保存在"+result?.absolutePath).showSuccess(true)
                            } else {
                                setMessage("保存失败.").showError(true)
                            }
                        }
                }
            })
        }
    }

    fun updateMedia(file : File) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            val values = ContentValues()
            values.put(MediaStore.MediaColumns.DISPLAY_NAME, file.getName())
            values.put(
                MediaStore.MediaColumns.MIME_TYPE,
                PictureMimeType.getImageMimeType(file.absolutePath)
            )
            values.put(MediaStore.MediaColumns.RELATIVE_PATH, Environment.DIRECTORY_DCIM)
            val contentResolver: ContentResolver = context.getContentResolver()
            contentResolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);

        } else {
            MediaScannerConnection.scanFile(
                MyApp.getInstance().getApplicationContext(),
                arrayOf<String>(file.getAbsolutePath()),
                arrayOf("image/jpeg"),
                { path, uri -> })
        }
    }

    override fun onSetQRCode(url: String) {
        GlideUtils.setImageUrl12(ivQRCode, url)
    }

    override fun onGetQRCodeFail() {

    }
}