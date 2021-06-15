package com.lingmiao.shop.base

import com.blankj.utilcode.util.ConvertUtils
import com.blankj.utilcode.util.Utils
import com.lingmiao.shop.business.common.bean.FileResponse
import com.lingmiao.shop.business.goods.api.bean.GoodsUseExpireVo
import com.lingmiao.shop.business.wallet.bean.PageListVo
import com.lingmiao.shop.net.Fetch
import com.james.common.netcore.networking.http.core.HiResponse
import com.james.common.netcore.networking.http.core.awaitHiResponse
import id.zelory.compressor.Compressor
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.await
import retrofit2.awaitResponse
import java.io.File

object CommonRepository {

    const val SCENE_GOODS = "goods"
    const val SCENE_SHOP = "shop"
    const val SCENE_MEMBER = "member"
    const val SCENE_OTHER = "other"

    /**
     *  上传图片/视频
     *
     *  isImage 默认是上传图片类型,可选
     *  业务场景  goods, shop, member, other
     */
    suspend fun uploadFile(beforeFile: File, isImage:Boolean = true, scene: String = "shop"): HiResponse<FileResponse> {
        // {"code":"901","message":"不允许上传的文件格式，请上传gif,jpg,png,jpeg,mp4,pdf格式文件。"}
        var file = beforeFile
        var imageType = "image/png"
        val videoType = "video/mp4"
        if (file.name.endsWith("jpg")) {
            imageType = "image/jpg"
        } else if (file.name.endsWith("jpeg")) {
            imageType = "image/jpeg"
        }
        if(isImage) {
            file = Compressor.compress(Utils.getApp(), beforeFile)
        }
        val currentType = if(isImage) imageType else videoType
        val requestFile: RequestBody = RequestBody.create(
            MediaType.parse(currentType),
            file
        )

        val multipartBody =
            MultipartBody.Part.createFormData(
                "file",
                file.name,
                requestFile
            )
        return Fetch.apiService().uploadFile(
            IConstant.getUploadFileUrl(),
            multipartBody,
            scene
        ).awaitHiResponse()
    }


    suspend fun download(url : String?) : Response<ResponseBody?>? {
        return Fetch.apiService().download(url)?.execute();
    }

    private val apiService by lazy {
        Fetch.createOtherService(CommonUrlService::class.java)
    }

    suspend fun queryListByType(type : String) : HiResponse<PageListVo<GoodsUseExpireVo>> {
        return apiService.queryListByType(type).awaitHiResponse();
    }

}