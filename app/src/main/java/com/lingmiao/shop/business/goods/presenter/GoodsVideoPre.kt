package com.lingmiao.shop.business.goods.presenter

import com.lingmiao.shop.business.goods.api.bean.GoodsVO
import com.lingmiao.shop.business.goods.api.request.VideoRequest
import com.james.common.base.BasePresenter
import com.james.common.base.BaseView

/**
 * @author elson
 * @date 2020/8/13
 * @Desc 商品视频
 */
interface GoodsVideoPre : BasePresenter {

    fun publishVideo(request: VideoRequest?)

    interface VideoView : BaseView {
        fun onSuccess(url: String)
    }
}