package com.lingmiao.shop.business.main

import android.app.Activity
import android.content.Intent
import android.graphics.Color
import android.os.Build
import android.os.Environment
import android.text.Spannable
import android.text.SpannableStringBuilder
import android.text.style.ForegroundColorSpan
import android.view.View
import androidx.annotation.RequiresApi
import androidx.lifecycle.lifecycleScope
import com.blankj.utilcode.util.*
import com.james.common.base.BaseActivity
import com.james.common.utils.DialogUtils
import com.lingmiao.shop.R
import com.lingmiao.shop.base.CommonRepository
import com.lingmiao.shop.business.main.bean.ApplyShopHint
import com.lingmiao.shop.business.main.presenter.ApplyShopHintPresenter
import com.lingmiao.shop.business.main.presenter.impl.ApplyShopHintPresenterImpl
import kotlinx.android.synthetic.main.main_activity_apply_shop_hint.*
import kotlinx.android.synthetic.main.me_fragment_qr_image.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.*
import java.util.*

/**
 *   申请开店
 */
class ApplyShopHintActivity : BaseActivity<ApplyShopHintPresenter>(), ApplyShopHintPresenter.View {


    override fun getLayoutId() = R.layout.main_activity_apply_shop_hint

    companion object {
        private const val REQUEST_CODE_SERVICE = 8
    }

    override fun useLightMode() = false

    fun test() {

    }

    @RequiresApi(Build.VERSION_CODES.Q)
    override fun initView() {
        mToolBarDelegate.setMidTitle("申请开店")

        //设置文字下载承诺函
        val content = "若企业结算账户为对私，需下载《签约承诺函》并盖章，然后上传图片。点击可下载"
        val builder = SpannableStringBuilder(content)
        val blueSpan = ForegroundColorSpan(Color.parseColor("#3870EA"))
        builder.setSpan(blueSpan, 32, 37, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
        applyShopHintTV.text = builder

        //点击下载DOC文件
        applyShopHintTV.setOnClickListener {


            DialogUtils.showDialog(context, "承诺函下载", "是否确认下载承诺函？", "取消", "下载", null,
                View.OnClickListener {

                    lifecycleScope.launch(Dispatchers.IO)
                    {

                        withContext(Dispatchers.Main) {
                            showDialogLoading()
                        }

                        //获取InputStream
                        val call =
                            CommonRepository.download("https://c-shop-prod.oss-cn-hangzhou.aliyuncs.com/%E7%AD%BE%E7%BA%A6%E6%89%BF%E8%AF%BA%E5%87%BD.doc")

                        //目标文件
                        var externalFileRootDir:File? = getExternalFilesDir(null)
                        do {
                            externalFileRootDir =
                                Objects.requireNonNull(externalFileRootDir)?.parentFile
                        } while (Objects.requireNonNull(externalFileRootDir)?.absolutePath?.contains(
                                "/Android"
                            ) == true
                        )
                        val saveDir: String?=
                            Objects.requireNonNull(externalFileRootDir)?.absolutePath
                        val savePath = saveDir + "/" + Environment.DIRECTORY_DOWNLOADS

                        val destinationFile = File(savePath, "签约承诺函.doc")

                        var inputStream: InputStream? = null
                        var outputStream: OutputStream? = null

                        val data = ByteArray(2048)
                        var count: Int?


                        count = inputStream?.read(data)

                        try {
                            inputStream = call?.body()?.byteStream()
                            outputStream = FileOutputStream(destinationFile)

                            while (count != -1) {
                                if (count != null) {
                                    outputStream.write(data, 0, count)
                                }
                                count = inputStream?.read(data)
                            }

                            outputStream.flush()


                        } catch (e: Exception) {

                            withContext(Dispatchers.Main)
                            {
                                hideDialogLoading()
                                ToastUtils.showShort("下载失败")
                            }
                        } finally {
                            inputStream?.close()
                            outputStream?.close()
                            withContext(Dispatchers.Main)
                            {
                                hideDialogLoading()
                                ToastUtils.showShort("下载成功：${destinationFile.absolutePath}")
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

    override fun createPresenter() = ApplyShopHintPresenterImpl(this, this)


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