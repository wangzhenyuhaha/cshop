package com.lingmiao.shop.business.me

import android.content.ContentResolver
import android.content.ContentValues
import android.content.Intent
import android.graphics.Bitmap
import android.media.MediaScannerConnection
import android.os.Build
import android.os.Environment
import android.provider.MediaStore
import android.widget.ImageView
import com.blankj.utilcode.util.ActivityUtils
import com.blankj.utilcode.util.ImageUtils
import com.blankj.utilcode.util.SnackbarUtils
import com.blankj.utilcode.util.ThreadUtils
import com.james.common.base.BaseActivity
import com.lingmiao.shop.MyApp
import com.lingmiao.shop.R
import com.lingmiao.shop.base.IConstant.CACHE_PATH
import com.lingmiao.shop.business.me.pop.ShopQRCodePop
import com.lingmiao.shop.business.me.presenter.IShopQRCodePre
import com.lingmiao.shop.business.me.presenter.impl.ShopQRCodePreImpl
import com.lingmiao.shop.util.GlideUtils
import com.luck.picture.lib.config.PictureMimeType
import kotlinx.android.synthetic.main.me_activity_qr_code.*
import java.io.File


/**
Create Date : 2021/6/43:07 PM
Author      : Fox
Desc        :
 **/
class ShopQRCodeActivity : BaseActivity<IShopQRCodePre>(), IShopQRCodePre.View {

    val path = CACHE_PATH + "share.jpg"
    private var shopId: Int? = null

    //ImageView
    private lateinit var imageView: ImageView


    override fun createPresenter(): IShopQRCodePre {
        return ShopQRCodePreImpl(this, this);
    }

    override fun initBundles() {
        shopId = intent.extras?.getInt("SHOP_ID")
    }

    override fun getLayoutId(): Int {
        return R.layout.me_activity_qr_code;
    }

    override fun initView() {
        mToolBarDelegate.setMidTitle("我的二维码")
        imageView = findViewById(R.id.ivQRCode)

        mPresenter?.getQRCode()

        val pop = ShopQRCodePop(this)

        //设置对PopWindow的点击
        //保存图片
        pop.setSaveListener {
            ThreadUtils.executeBySingle(object : ThreadUtils.SimpleTask<File?>() {
                override fun doInBackground(): File? {
                    return ImageUtils.save2Album(
                        ImageUtils.view2Bitmap(imageView),
                        Bitmap.CompressFormat.JPEG
                    )
                }

                override fun onSuccess(result: File?) {
                    SnackbarUtils.with(imageView)
                        .setDuration(SnackbarUtils.LENGTH_LONG)
                        .apply {
                            if (result?.exists() == true) {
                                updateMedia(result!!)
                                setMessage("图片保存在" + result?.absolutePath).showSuccess(true)
                                pop.dismiss()
                            } else {
                                setMessage("保存失败.").showError(true)
                                pop.dismiss()
                            }
                        }
                }
            })

        }

        //分享二维码
        pop.setShareListener {
            mPresenter?.getShareInfo(shopId!!)
            pop.dismiss()
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


        //打开PopWindow
        ivQRCode.setOnClickListener {
            pop.showPopupWindow()
        }

    }

    fun updateMedia(file: File) {
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
                MyApp.getInstance().applicationContext,
                arrayOf<String>(file.absolutePath),
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