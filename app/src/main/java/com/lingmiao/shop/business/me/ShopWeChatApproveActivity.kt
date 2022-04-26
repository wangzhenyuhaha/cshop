package com.lingmiao.shop.business.me

import android.content.ContentResolver
import android.content.ContentValues
import android.graphics.Bitmap
import android.media.MediaScannerConnection
import android.os.Build
import android.os.Environment
import android.provider.MediaStore
import android.view.View
import com.blankj.utilcode.util.ImageUtils
import com.blankj.utilcode.util.SnackbarUtils
import com.blankj.utilcode.util.ThreadUtils
import com.james.common.base.BaseVBActivity
import com.james.common.utils.DialogUtils
import com.james.common.utils.exts.gone
import com.james.common.utils.exts.visiable
import com.lingmiao.shop.MyApp
import com.lingmiao.shop.base.ShopStatusConstants
import com.lingmiao.shop.base.UserManager
import com.lingmiao.shop.business.me.presenter.WeChatApprovePresenter
import com.lingmiao.shop.business.me.presenter.impl.WeChatApprovePresenterImpl
import com.lingmiao.shop.databinding.ActivityShopWeChatApproveBinding
import com.luck.picture.lib.config.PictureMimeType
import kotlinx.android.synthetic.main.activity_shop_we_chat_approve.*
import kotlinx.android.synthetic.main.activity_shop_we_chat_approve.ivQRCode
import kotlinx.android.synthetic.main.me_fragment_qr_image.*
import java.io.File

class ShopWeChatApproveActivity :
    BaseVBActivity<ActivityShopWeChatApproveBinding, WeChatApprovePresenter>(),
    WeChatApprovePresenter.View {


    override fun createPresenter() = WeChatApprovePresenterImpl(this)

    override fun getViewBinding() = ActivityShopWeChatApproveBinding.inflate(layoutInflater)

    override fun useBaseLayout() = true

    private var type: Int = 0


    override fun onBackPressed() {
        if (type != 1) {
            if (ShopStatusConstants.isFinalOpen(UserManager.getLoginInfo()?.shopStatus)) {
                super.onBackPressed()
            } else {
                DialogUtils.showDialog(context!!, "商户认证", "认证成功后才能正常结算，确认微信商户认证成功？",
                    "取消", "确认已认证", {
                        super.onBackPressed()
                    }, {
                        mPresenter?.approve()
                        super.onBackPressed()
                    })
            }
        }
    }

    override fun initBundles() {
        super.initBundles()
        type = intent.getIntExtra("type", 0)
    }

    override fun initView() {
        mToolBarDelegate?.setMidTitle("微信认证")
        if (ShopStatusConstants.isFinalOpen(UserManager.getLoginInfo()?.shopStatus)) {
            warningLayout.gone()
        } else {
            warningLayout.gone()
        }
        //长按图片下载
        mBinding.ivQRCode.setOnLongClickListener {
            downloadImage { result ->
                SnackbarUtils.with(ivQRCode)
                    .setDuration(SnackbarUtils.LENGTH_LONG)
                    .apply {
                        if (result?.exists() == true) {
                            updateMedia(result)
                            setMessage("图片保存在" + result.absolutePath).showSuccess(true)

                        } else {
                            setMessage("保存失败.").showError(true)

                        }
                    }
            }


            false
        }
    }

    override fun approved() {
        super.onBackPressed()
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
            values.put(MediaStore.MediaColumns.DISPLAY_NAME, file.name)
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

}