package com.lingmiao.shop.business.me.presenter.impl

import android.content.Context
import android.util.Log
import com.james.common.base.BasePreImpl
import com.james.common.netcore.networking.http.core.HiResponse
import com.james.common.netcore.networking.http.core.awaitHiResponse
import com.james.common.utils.exts.isNetUrl
import com.lingmiao.shop.base.CommonRepository
import com.lingmiao.shop.business.common.bean.FileResponse
import com.lingmiao.shop.business.goods.api.bean.GoodsGalleryVO
import com.lingmiao.shop.business.main.api.MainRepository
import com.lingmiao.shop.business.me.api.MeRepository
import com.lingmiao.shop.business.me.bean.Feedback
import com.lingmiao.shop.business.me.presenter.FeedbackPresenter
import kotlinx.android.synthetic.main.me_activity_feedback.*
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.launch
import java.io.File

class FeedbackPresenterImpl(context: Context, private var view: FeedbackPresenter.View) :
    BasePreImpl(view), FeedbackPresenter {

    override fun requestFeedbackData(type: Int, content: String, gallery: List<GoodsGalleryVO>?) {
        mCoroutine.launch {
            view.showDialogLoading("正在上传")
            //开始上传图片
            uploadImages(gallery, fail = {
                view.showToast("图片上传失败，请重试")
                view.hideDialogLoading()
                return@uploadImages
            }, success = {
                //开始调用反馈接口
                val temp: StringBuilder = StringBuilder()

                gallery?.also { list ->
                    for (i in list) {
                        temp.append(i.original.toString())
                        temp.append(",")
                    }
                }
                if (temp.isEmpty()) {
                    loadData(type, content, null)
                } else {
                    temp.deleteCharAt(temp.indexOfLast { it == ',' })
                    loadData(type, content, temp.toString())
                }
            })

        }
    }

    override fun getCompanyWeChat() {
        mCoroutine.launch {
            val resp = MeRepository.apiService.getCompanyWeChat().awaitHiResponse()
            if (resp.isSuccess) {
                view.onSuccessWeChat(resp.data.data.toString())
            }
        }
    }

    private fun loadData(type: Int?, content: String?, list: String?) {
        mCoroutine.launch {
            val resp = MeRepository.apiService.feedback(Feedback(type, content, list))
            if (resp.isSuccessful) {
                view.onFeedbackSuccess()
            } else {
                view.onFeedbackError()
            }
        }
    }

    //图片上传，
    private fun uploadImages(list: List<GoodsGalleryVO>?, fail: () -> Unit, success: () -> Unit) {
        mCoroutine.launch {
            val requestList = ArrayList<Deferred<HiResponse<FileResponse>>>()
            // 多张图片并行上传
            list!!.forEach {
                val request = async {
                    if (it.original.isNetUrl()) {
                        HiResponse(0, "", FileResponse("", "", it.original))
                    } else {
                        CommonRepository.uploadFile(
                            File(it.original!!),
                            true,
                            CommonRepository.SCENE_GOODS
                        )
                    }
                }
                requestList.add(request)
            }

            // 多个接口相互等待
            val respList = requestList.awaitAll()
            var isAllSuccess = true
            respList.forEachIndexed { index, it ->
                if (it.isSuccess) {
                    list[index].apply {
                        original = it.data?.url
                        sort = "$index"
                    }
                } else {
                    isAllSuccess = false
                }
            }
            if (isAllSuccess) {
                success.invoke()
            } else {
                fail.invoke()
            }
        }
    }

}