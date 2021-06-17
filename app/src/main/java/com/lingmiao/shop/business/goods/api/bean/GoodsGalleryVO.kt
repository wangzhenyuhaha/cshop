package com.lingmiao.shop.business.goods.api.bean

import com.lingmiao.shop.business.photo.getImagePath
import com.google.gson.annotations.SerializedName
import com.james.common.utils.exts.checkNotBlank
import com.luck.picture.lib.entity.LocalMedia
import java.io.Serializable

/**
 * Author : liuqi
 * Date   : 2020/8/8
 * Desc   :
 */
data class GoodsGalleryVO(
    @SerializedName("img_id")
    var imgId: String? = "-1",
    @SerializedName("original")
    var original: String?,
    @SerializedName("sort")
    var sort: String?
) : Serializable {
    companion object {
        fun convert(localMedia: LocalMedia): GoodsGalleryVO {
            val imagePath = localMedia.compressPath.checkNotBlank(localMedia.getImagePath())
            return GoodsGalleryVO(null, imagePath, null)
        }
    }

    fun convert2LocalMedia() : LocalMedia {
        return LocalMedia().apply {
            path = original
            androidQToPath = original
        }
    }
}
