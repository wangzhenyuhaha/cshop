package com.lingmiao.shop.business.goods.presenter.impl

import com.lingmiao.shop.base.CommonRepository
import com.lingmiao.shop.business.goods.api.request.VideoRequest
import com.lingmiao.shop.business.goods.presenter.GoodsVideoPre
import com.james.common.base.BasePreImpl
import com.james.common.utils.exts.isNetUrl
import kotlinx.coroutines.launch
import java.io.File

/**
 * Author : Elson
 * Date   : 2020/8/13
 * Desc   : 商品视频
 */
class GoodsVideoPreImpl(val view: GoodsVideoPre.VideoView) : BasePreImpl(view), GoodsVideoPre {

    override fun publishVideo(request: VideoRequest?) {
        if (request == null || request.path.isNullOrBlank()) {
            view.showToast("请先选择视频")
            return
        }
        if (request.size >= VideoRequest.MAX_SIZE) {
             view.showToast("上传的视频不能超过30M")
            return
        }
        if (request.path.isNetUrl()) {
            view.onSuccess(request.path!!)
            return
        }
        mCoroutine.launch {
            view.showDialogLoading("正在上传视频...")
            val resp = CommonRepository.uploadFile(File(request.path), false, CommonRepository.SCENE_GOODS)
            handleResponse(resp) {
                it.url?.apply {
                    view.onSuccess(this)
                }
            }
            view.hideDialogLoading()
        }
    }
}