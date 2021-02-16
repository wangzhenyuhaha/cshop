package com.lingmiao.shop.business.goods.presenter.impl

import com.blankj.utilcode.util.GsonUtils
import com.lingmiao.shop.base.CommonRepository
import com.lingmiao.shop.business.common.bean.FileResponse
import com.lingmiao.shop.business.goods.presenter.GoodsDescPre
import com.lingmiao.shop.business.photo.getImagePath
import com.james.common.base.BasePreImpl
import com.james.common.netcore.networking.http.core.HiResponse
import com.luck.picture.lib.entity.LocalMedia
import kotlinx.coroutines.*
import java.io.File

/**
 * Author : Elson
 * Date   : 2020/9/1
 * Desc   : 商品详情页面
 */
class GoodsDescPreImpl(var view: GoodsDescPre.DescView) : BasePreImpl(view), GoodsDescPre {

    override fun uploadImages(list: List<LocalMedia>?) {
        if (list.isNullOrEmpty()) {
            return
        }
        view.showDialogLoading("正在上传图片...")
        upload(list) { resultList ->
            val jsonStr = GsonUtils.toJson(resultList)
            view.onUploadSuccess(jsonStr)
            view.hideDialogLoading()
        }

    }


    private fun upload(images: List<LocalMedia>, success: (List<String?>) -> Unit) {
        mCoroutine.launch {
            val requestList = ArrayList<Deferred<HiResponse<FileResponse>>>()
            // 多张图片并行上传
            images.forEach {
                val request =
                    async { CommonRepository.uploadFile(File(it.getImagePath()), true, CommonRepository.SCENE_GOODS) }
                requestList.add(request)
            }
            // 多个接口相互等待
            val respList = requestList.awaitAll()
            var isAllSuccess: Boolean = true
            val resultList = mutableListOf<String?>()
            respList.forEachIndexed { index, it ->
                if (it.isSuccess) {
                    resultList.add(index, it.data.url)
                } else {
                    isAllSuccess = false
                }
            }
            if (isAllSuccess) {
                success.invoke(resultList)
            } else {
                view.showToast("图片上传失败")
            }
        }
    }
}