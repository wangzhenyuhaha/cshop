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
import android.widget.Toast
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
import okhttp3.Callback
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Response
import java.io.*

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

    //https://www.jianshu.com/p/d7ea4f0493e1
    //https://www.cnblogs.com/rustfisher/p/11704563.html
    //https://blog.csdn.net/cunchi4221/article/details/107475770
    //https://mp.weixin.qq.com/s/i088pd4y61gPRnnofjpJAA
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

    private fun updateMedia(file: File) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            val values = ContentValues()
            values.put(MediaStore.MediaColumns.DISPLAY_NAME, file.name)
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

    //将位图添加到图库中
    /**
     * 下载到本地
     *
     * @param body 内容
     * @return 成功或者失败
     */
    private fun writeResponseBodyToDisk(body: ResponseBody): Boolean {
        val rootFile = Environment.getExternalStorageDirectory()
        return try {
            //判断文件夹是否存在
            val files: File =  File(rootFile, "mytest.doc") //跟目录一个文件夹
            if (!files.exists()) {
                //不存在就创建出来
                files.mkdirs()
            }
            //初始化输入流
            var inputStream: InputStream? = null
            //初始化输出流
            var outputStream: OutputStream? = null
            try {
                //设置每次读写的字节
                val fileReader = ByteArray(4096)
                val fileSize = body.contentLength()
                var fileSizeDownloaded: Long = 0
                //请求返回的字节流
                inputStream = body.byteStream()
                //创建输出流
                outputStream = FileOutputStream(files)
                //进行读取操作
                while (true) {
                    val read: Int = inputStream.read(fileReader)
                    if (read == -1) {
                        break
                    }
                    //进行写入操作
                    outputStream.write(fileReader, 0, read)
                    fileSizeDownloaded += read.toLong()
                }

                //刷新
                outputStream.flush()
                true
            } catch (e: IOException) {
                false
            } finally {
                if (inputStream != null) {
                    //关闭输入流
                    inputStream.close()
                }
                if (outputStream != null) {
                    //关闭输出流
                    outputStream.close()
                }
            }
        } catch (e: IOException) {
            false
        }
    }

    override fun initView() {
        mToolBarDelegate.setMidTitle("申请开店")

        //设置文字
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
                        val call =
                            CommonRepository.download ("https://c-shop-prod.oss-cn-hangzhou.aliyuncs.com/%E7%AD%BE%E7%BA%A6%E6%89%BF%E8%AF%BA%E5%87%BD.doc")

                        Log.d("WZYAAA",call.toString())


                        call?.body()?.let { it1 -> writeResponseBodyToDisk(it1) }


                       // FileIOUtils.writeFileFromIS("my.doc",call.hashCode())

                        // FileIOUtils.java -> Test
                        //writeFileFromIS
                        //
//                        private void testPublicFile() {
//                            File rootFile = Environment.getExternalStorageDirectory();
//                            try {
//                                File file = new File(rootFile, "mytest.txt");
//                                FileOutputStream fos = new FileOutputStream(file);
//                                String content = "hello world\n";
//                                fos.write(content.getBytes());
//                                fos.flush();
//                                fos.close();
//                            } catch (Exception e) {
//                                Log.d("test", e.getLocalizedMessage());
//                            }
//                        }



                        //private static void writeResponseToDisk(String path, Response<ResponseBody  response, DownloadListener downloadListener) {
                        //    //从response获取输入流以及总大小
                        //    writeFileFromIS(new File(path), response.body().byteStream(), response.body().contentLength(), downloadListener);
                        //  }
                        //
                        //  private static int sBufferSize = 8192;
                        //
                        //  //将输入流写入文件
                        //  private static void writeFileFromIS(File file, InputStream is, long totalLength, DownloadListener downloadListener) {
                        //    //开始下载
                        //    downloadListener.onStart();
                        //
                        //    //创建文件
                        //    if (!file.exists()) {
                        //      if (!file.getParentFile().exists())
                        //        file.getParentFile().mkdir();
                        //      try {
                        //        file.createNewFile();
                        //      } catch (IOException e) {
                        //        e.printStackTrace();
                        //        downloadListener.onFail("createNewFile IOException");
                        //      }
                        //    }
                        //
                        //    OutputStream os = null;
                        //    long currentLength = 0;
                        //    try {
                        //      os = new BufferedOutputStream(new FileOutputStream(file));
                        //      byte data[] = new byte[sBufferSize];
                        //      int len;
                        //      while ((len = is.read(data, 0, sBufferSize)) != -1) {
                        //        os.write(data, 0, len);
                        //        currentLength += len;
                        //        //计算当前下载进度
                        //        downloadListener.onProgress((int) (100 * currentLength / totalLength));
                        //      }
                        //      //下载完成，并返回保存的文件路径
                        //      downloadListener.onFinish(file.getAbsolutePath());
                        //    } catch (IOException e) {
                        //      e.printStackTrace();
                        //      downloadListener.onFail("IOException");
                        //    } finally {
                        //      try {
                        //        is.close();
                        //      } catch (IOException e) {
                        //        e.printStackTrace();
                        //      }
                        //      try {
                        //        if (os != null) {
                        //          os.close();
                        //        }
                        //      } catch (IOException e) {
                        //        e.printStackTrace();
                        //      }
                        //    }
                        //  }


                    }
//                    downloadImage { result ->
//                        SnackbarUtils.with(applyShopHintTV)
//                            .setDuration(SnackbarUtils.LENGTH_LONG)
//                            .apply {
//                                if (result?.exists() == true) {
//                                    updateMedia(result!!)
//                                    setMessage("图片保存在" + result?.absolutePath).showSuccess(true)
//
//                                } else {
//                                    setMessage("保存失败.").showError(true)
//                                }
//                            }
//                    }

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