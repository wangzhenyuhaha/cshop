package com.lingmiao.shop.business.goods

import android.app.Activity
import android.content.Context
import android.content.Intent
import com.lingmiao.shop.R
import com.lingmiao.shop.business.goods.api.request.VideoRequest
import com.lingmiao.shop.business.goods.presenter.GoodsVideoPre
import com.lingmiao.shop.business.goods.presenter.impl.GoodsVideoPreImpl
import com.lingmiao.shop.business.photo.PhotoHelper
import com.lingmiao.shop.business.photo.getImagePath
import com.lingmiao.shop.util.GlideUtils
import com.james.common.base.BaseActivity
import com.james.common.utils.exts.isNotBlank
import com.james.common.utils.exts.isNotEmpty
import com.james.common.utils.exts.singleClick
import kotlinx.android.synthetic.main.goods_activity_video.*

/**
 * Author : Elson
 * Date   : 2020/8/13
 * Desc   : 商品视频
 */
class GoodsVideoActivity : BaseActivity<GoodsVideoPre>(), GoodsVideoPre.VideoView {

    companion object {
        const val KEY_VIDEO_URL = "KEY_VIDEO_URL"

        fun openActivity(context: Context, requestCode: Int, videoUrl: String?) {
            if (context is Activity) {
                val intent = Intent(context, GoodsVideoActivity::class.java)
                intent.putExtra(KEY_VIDEO_URL, videoUrl)
                context.startActivityForResult(intent, requestCode)
            }
        }
    }

    private var videoRequest: VideoRequest? = null

    override fun useBaseLayout(): Boolean {
        return false
    }

    override fun getLayoutId(): Int {
        return R.layout.goods_activity_video
    }

    override fun initBundles() {
        intent.getStringExtra(KEY_VIDEO_URL)?.apply {
            videoRequest = VideoRequest(this)
        }
    }

    override fun createPresenter(): GoodsVideoPre {
        return GoodsVideoPreImpl(this)
    }

    override fun initView() {
        toolbarView.apply {
            setTitleContent("商品视频")
            setRightListener("删除", R.color.color_666666) {
                videoRequest = null
                videoIv.setImageResource(R.mipmap.goods_selected_img)
            }
        }

        if (videoRequest?.path.isNotBlank()) {
            GlideUtils.setImageUrl1(videoIv, videoRequest!!.path)
        }
        videoIv.singleClick {
            clickVideoView()
        }
        submitTv.singleClick {
            mPresenter.publishVideo(videoRequest)
        }
    }

    private fun clickVideoView() {
        PhotoHelper.openVideo(this, 1, null) { list ->
            if (list.isNotEmpty() && list.first().getImagePath().isNotBlank()) {
                videoRequest = VideoRequest.convert(list[0])
                GlideUtils.setImageUrl1(videoIv, videoRequest!!.path)
            }
        }
    }

    override fun onSuccess(url: String) {
        val intent = Intent()
        intent.putExtra(KEY_VIDEO_URL, url)
        setResult(Activity.RESULT_OK, intent)
        finish()
    }

}