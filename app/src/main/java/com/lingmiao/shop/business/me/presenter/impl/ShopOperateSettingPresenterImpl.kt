package com.lingmiao.shop.business.me.presenter.impl

import android.content.Context
import com.james.common.base.BasePreImpl
import com.james.common.netcore.networking.http.core.HiResponse
import com.james.common.netcore.networking.http.core.awaitHiResponse
import com.james.common.utils.exts.isNetUrl
import com.lingmiao.shop.base.CommonRepository
import com.lingmiao.shop.business.common.bean.FileResponse
import com.lingmiao.shop.business.goods.api.bean.GoodsGalleryVO
import com.lingmiao.shop.business.goods.api.bean.WorkTimeVo
import com.lingmiao.shop.business.goods.presenter.impl.ItemPopPreImpl
import com.lingmiao.shop.business.main.bean.ApplyShopInfo
import com.lingmiao.shop.business.me.api.MeRepository
import com.lingmiao.shop.business.me.bean.BannerBean
import com.lingmiao.shop.business.me.presenter.ShopOperateSettingPresenter
import com.lingmiao.shop.business.tools.api.ToolsRepository
import com.lingmiao.shop.business.tools.bean.FreightVoItem
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.launch
import java.io.File

/**
Create Date : 2021/3/24:08 PM
Auther      : Fox
Desc        :
 **/
class ShopOperateSettingPresenterImpl(
    val context: Context,
    var view: ShopOperateSettingPresenter.View
) : BasePreImpl(view), ShopOperateSettingPresenter {

    private val mItemPreImpl: ItemPopPreImpl by lazy { ItemPopPreImpl(view) }

    override fun showWorkTimePop(target: android.view.View) {
        mItemPreImpl?.showWorkTimePop(context, "") { item1: WorkTimeVo?, item2: WorkTimeVo? ->
            view?.onUpdateWorkTime(item1, item2);
        }
    }

    override fun setSetting(data: ApplyShopInfo, gallery: List<GoodsGalleryVO>?) {
        mCoroutine.launch {
            view.showDialogLoading()
            uploadImages(gallery, fail = {
                view.showToast("图片上传失败，请重试")
                view.hideDialogLoading()
                return@uploadImages
            }, success = {
                updateBanner(gallery!!, fail = {
                    view.showToast("图片上传失败，请重试")
                    view.hideDialogLoading()
                }, success = {
                    updateShop(data)
                })
            })
            view.hideDialogLoading()
        }
    }

    private fun uploadImages(list: List<GoodsGalleryVO>?, fail: () -> Unit, success: () -> Unit) {
        mCoroutine.launch {
            val requestList = ArrayList<Deferred<HiResponse<FileResponse>>>()
            // 多张图片并行上传
            list?.forEach {
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
                    list?.get(index)?.apply {
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

    private fun updateShop(data: ApplyShopInfo) {
        mCoroutine.launch {
            val resp = MeRepository.apiService.updateShop(data).awaitHiResponse()
            if (resp.isSuccess) {
                view.showToast("保存成功")
                view.onSetSetting()
            } else {
                view.showToast("保存失败")
            }
        }
    }

    private fun updateBanner(gallery: List<GoodsGalleryVO>, fail: () -> Unit, success: () -> Unit) {
        mCoroutine.launch {

            val banners = mutableListOf<BannerBean>()
            gallery.let {
                it.forEachIndexed { _, vo ->
                    banners.add(BannerBean(vo.original))
                }
            }
            val resp = MeRepository.apiService.updateBanner(banners).awaitHiResponse()
            if (resp.isSuccess) {
                success.invoke()
            } else {
                fail.invoke()
            }
        }
    }

    override fun loadShopSetting() {
        mCoroutine.launch {
            val resp = MeRepository.apiService.getShop().awaitHiResponse()
            handleResponse(resp) {
                view.onLoadedShopSetting(resp.data)
            }
        }
    }

    override fun loadTemplate() {
        mCoroutine.launch {
            val tcsp = ToolsRepository.shipTemplates(FreightVoItem.TYPE_LOCAL);
            val qssp = ToolsRepository.shipTemplates(FreightVoItem.TYPE_QISHOU);
            if (tcsp.isSuccess && qssp.isSuccess) {
                val tcItem = if (tcsp?.data?.size ?: 0 > 0) tcsp?.data?.get(0) else null;
                val qsItem = if (qssp?.data?.size ?: 0 > 0) qssp?.data?.get(0) else null;
                view.onLoadedTemplate(tcItem, qsItem)
            }
        }
    }

    override fun getBanner() {
        mCoroutine.launch {
            val resp = MeRepository.apiService.getBanner().awaitHiResponse()
            handleResponse(resp) {
                view.onSetBanner(it);
            }
        }
    }

}