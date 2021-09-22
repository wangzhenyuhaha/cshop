package com.lingmiao.shop.business.me.fragment

import android.content.ContentResolver
import android.content.ContentValues
import android.content.Intent
import android.graphics.Bitmap
import android.media.MediaScannerConnection
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.blankj.utilcode.util.ActivityUtils
import com.blankj.utilcode.util.ImageUtils
import com.blankj.utilcode.util.SnackbarUtils
import com.blankj.utilcode.util.ThreadUtils
import com.fox7.wx.WxShare
import com.james.common.base.BaseVBFragment
import com.james.common.utils.exts.singleClick
import com.lingmiao.shop.MyApp
import com.lingmiao.shop.base.IWXConstant
import com.lingmiao.shop.business.me.pop.ShopQRCodePop
import com.lingmiao.shop.business.me.presenter.QRPresenter
import com.lingmiao.shop.business.me.presenter.impl.QRCodeOfStickyPreImpl
import com.lingmiao.shop.business.me.presenter.impl.QRCodePreImpl
import com.lingmiao.shop.databinding.MeFragmentQrImageBinding
import com.lingmiao.shop.util.GlideUtils
import com.luck.picture.lib.config.PictureMimeType
import com.tencent.mm.opensdk.openapi.WXAPIFactory
import kotlinx.android.synthetic.main.me_fragment_qr_image.*
import java.io.File

/**
Create Date : 2021/8/2912:33 下午
Auther      : Fox
Desc        :
 **/
class QRImageFragment : BaseVBFragment<MeFragmentQrImageBinding, QRPresenter>(), QRPresenter.View {

    var url: String = ""
    var type: Int? = 0

    companion object {
        fun newInstance(type: Int): QRImageFragment {
            return QRImageFragment().apply {
                arguments = Bundle().apply {
                    putInt("type", type)
                }
            }
        }
    }

    override fun useBaseLayout(): Boolean {
        return false
    }

    override fun getBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): MeFragmentQrImageBinding {
        return MeFragmentQrImageBinding.inflate(inflater, container, false)
    }

    override fun createPresenter(): QRPresenter {
        return if (type == 1) QRCodeOfStickyPreImpl(requireContext(), this) else QRCodePreImpl(
            requireContext(),
            this
        )
    }

    override fun initBundles() {
        type = arguments?.getInt("type", 0)
    }

    override fun initViewsAndData(rootView: View) {
        mPresenter?.requestQRUrl()

        type?.also {
            if (it == 0) {
                val temp = "推荐打印尺寸10x15cm"
                binding.textViewTitle.text = temp
            } else {
                val temp = "推荐打印尺寸8x9cm"
                binding.textViewTitle.text = temp
            }
        }
        val pop = ShopQRCodePop(requireContext())
        //设置对PopWindow的点击
        //保存图片
        pop.setSaveListener {
            downloadImage { result ->
                SnackbarUtils.with(ivQRCode)
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
        }

        //分享二维码
        pop.setShareListener {
            downloadImage { result ->
                SnackbarUtils.with(ivQRCode)
                    .setDuration(SnackbarUtils.LENGTH_LONG)
                    .apply {
                        if (result?.exists() == true) {
                            val api = WXAPIFactory.createWXAPI(requireContext(), IWXConstant.APP_ID)
                            var share = WxShare(requireContext(), api)
                            share.shareToFriend()
                            share.shareFile(result.path)
                            pop.dismiss()
                        } else {
                            setMessage("分享失败.").showError(true)
                            pop.dismiss()
                        }
                    }
            }
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

    fun downloadImage(callback: (result: File?) -> Unit) {
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

    override fun setQRUrl(url: String) {
        this.url = url
        GlideUtils.setImageUrl12(ivQRCode, url)
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
            val contentResolver: ContentResolver = requireActivity().contentResolver
            contentResolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values)

        } else {
            MediaScannerConnection.scanFile(
                MyApp.getInstance().applicationContext,
                arrayOf<String>(file.absolutePath),
                arrayOf("image/jpeg"),
                { path, uri -> })
        }
    }

    override fun getQRUrlFailed() {

    }

}