package com.lingmiao.shop.business.goods.api.request

import com.lingmiao.shop.business.photo.getImagePath
import com.luck.picture.lib.entity.LocalMedia
import java.io.Serializable

/**
 * @author elson
 * @date 2020/8/6
 * @Desc 视频
 */
class VideoRequest(var path: String? = null, var size: Long = DEFAULT_SIZE): Serializable {

    companion object {
        const val DEFAULT_SIZE = -1L
        const val MAX_SIZE = 30 * 1024 * 1024

        fun convert(localMedia: LocalMedia): VideoRequest {
            return VideoRequest(localMedia.getImagePath(), localMedia.size)
        }
    }
}