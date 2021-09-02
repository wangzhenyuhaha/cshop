package com.lingmiao.shop.business.main

import android.app.Activity
import android.content.ContentResolver
import android.content.ContentValues
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Color
import android.media.MediaScannerConnection
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.provider.MediaStore
import android.text.Spannable
import android.text.SpannableStringBuilder
import android.text.style.ForegroundColorSpan
import android.util.Log
import android.view.View
import androidx.lifecycle.lifecycleScope
import com.blankj.utilcode.util.*
import com.james.common.base.BaseActivity
import com.james.common.utils.DialogUtils
import com.james.common.utils.exts.singleClick
import com.lingmiao.shop.MyApp
import com.lingmiao.shop.R
import com.lingmiao.shop.base.CommonRepository
import com.lingmiao.shop.business.main.bean.ApplyShopHint
import com.lingmiao.shop.business.main.presenter.ApplyShopHintPresenter
import com.lingmiao.shop.business.main.presenter.impl.ApplyShopHintPresenterImpl
import com.luck.picture.lib.config.PictureMimeType
import kotlinx.android.synthetic.main.main_activity_apply_shop_hint.*
import kotlinx.android.synthetic.main.me_fragment_qr_image.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.io.File

/**
 *   申请开店
 */
class ApplyShopHintActivity : BaseActivity<ApplyShopHintPresenter>(), ApplyShopHintPresenter.View {


    override fun getLayoutId(): Int {
        return R.layout.main_activity_apply_shop_hint
    }

    companion object {
        private const val REQUEST_CODE_SERVICE = 8
    }

    override fun useLightMode(): Boolean {
        return false
    }

    private fun downloadImage(callback: (result: File?) -> Unit) {
        ThreadUtils.executeBySingle(object : ThreadUtils.SimpleTask<File?>() {
            override fun doInBackground(): File? {
                // return FileUtils.createFileByDeleteOldFile(UriUtils.uri2File(Uri.parse("https://c-shop-prod.oss-cn-hangzhou.aliyuncs.com/%E7%AD%BE%E7%BA%A6%E6%89%BF%E8%AF%BA%E5%87%BD.doc")))
                //uri 转 file
                //return ImageUtils.
                //https://th.bing.com/th/id/OIP.gkTsyWDm1QKOUPb0Is1OmAHaNK?pid=ImgDet&rs=1
                // return UriUtils.uri2File(Uri.parse("https://th.bing.com/th/id/OIP.gkTsyWDm1QKOUPb0Is1OmAHaNK?pid=ImgDet&rs=1"))
                // return UriUtils.uri2File(Uri.parse("https://c-shop-prod.oss-cn-hangzhou.aliyuncs.com/%E7%AD%BE%E7%BA%A6%E6%89%BF%E8%AF%BA%E5%87%BD.doc"))


                //此处需要获取File
                return UriUtils.uri2File(Uri.parse("https://c-shop-prod.oss-cn-hangzhou.aliyuncs.com/%E7%AD%BE%E7%BA%A6%E6%89%BF%E8%AF%BA%E5%87%BD"))

            }

            override fun onSuccess(result: File?) {
                callback.invoke(result)
            }
        })
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
            val contentResolver: ContentResolver = context.contentResolver
            contentResolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values)

        } else {
            MediaScannerConnection.scanFile(
                MyApp.getInstance().applicationContext,
                arrayOf<String>(file.absolutePath),
                arrayOf("doc"),
                { path, uri -> })
        }
    }

    override fun initView() {
        mToolBarDelegate.setMidTitle("申请开店")

        val content: String = "若企业结算账户为对私，需下载《签约承诺函》并盖章，然后上传图片。点击可下载"
        val builder: SpannableStringBuilder = SpannableStringBuilder(content)
        val blueSpan = ForegroundColorSpan(Color.parseColor("#3870EA"))
        builder.setSpan(blueSpan, 32, 37, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
        applyShopHintTV.text = builder


        applyShopHintTV.setOnClickListener {

            DialogUtils.showDialog(context, "承诺函下载", "是否确认下载承诺函？", "取消", "下载", null,
                View.OnClickListener {

                    lifecycleScope.launch(Dispatchers.IO)
                    {
                        val temp =
                            CommonRepository.download("https://c-shop-prod.oss-cn-hangzhou.aliyuncs.com/%E7%AD%BE%E7%BA%A6%E6%89%BF%E8%AF%BA%E5%87%BD.doc")
                        Log.d("WZYAAA", temp.toString())

                    }
                    downloadImage { result ->
                        SnackbarUtils.with(applyShopHintTV)
                            .setDuration(SnackbarUtils.LENGTH_LONG)
                            .apply {
                                if (result?.exists() == true) {
                                    updateMedia(result!!)
                                    setMessage("图片保存在" + result?.absolutePath).showSuccess(true)

                                } else {
                                    setMessage("保存失败.").showError(true)
                                }
                            }
                    }

                }
            )


        }

        //showPageLoading()
        //mPresenter.requestApplyShopHintData()
        ivApplyShopHintNext.isSelected = true
        ivApplyShopHintNext.setOnClickListener {
            ivApplyShopHintNext.isSelected = !ivApplyShopHintNext.isSelected
        }
        tvApplyShopHintService.setOnClickListener {
            val intent = Intent(this, UserServiceH5Activity::class.java)
            startActivityForResult(intent, REQUEST_CODE_SERVICE)
        }
        tvApplyShopHintNext.setOnClickListener {
            if (!ivApplyShopHintNext.isSelected) {
                showToast("请先同意商城入驻协议")
                return@setOnClickListener
            }
            ActivityUtils.startActivity(ApplyShopInfoActivity::class.java)
            finish()
        }

    }

    override fun createPresenter(): ApplyShopHintPresenter {
        return ApplyShopHintPresenterImpl(this, this)
    }

    override fun onApplyShopHintSuccess(bean: ApplyShopHint) {
        hidePageLoading()
    }

    override fun onApplyShopHintError(code: Int) {
        hidePageLoading()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_CODE_SERVICE && resultCode == Activity.RESULT_OK) {
            ivApplyShopHintNext.isSelected = true
        }
    }

}