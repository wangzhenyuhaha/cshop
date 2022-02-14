package com.lingmiao.shop.business.me.presenter.impl

import android.content.Context
import com.james.common.base.BasePreImpl
import com.james.common.netcore.networking.http.core.HiResponse
import com.james.common.netcore.networking.http.core.awaitHiResponse
import com.james.common.utils.exts.isNetUrl
import com.lingmiao.shop.base.CommonRepository
import com.lingmiao.shop.base.UserManager
import com.lingmiao.shop.business.common.bean.FileResponse
import com.lingmiao.shop.business.goods.api.bean.GoodsGalleryVO
import com.lingmiao.shop.business.main.bean.ApplyShopInfo
import com.lingmiao.shop.business.me.api.MeRepository
import com.lingmiao.shop.business.me.bean.BannerBean
import com.lingmiao.shop.business.me.presenter.ShopManagePresenter
import com.lingmiao.shop.business.me.presenter.ShopSettingPresenter
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.launch
import org.greenrobot.eventbus.EventBus
import java.io.File

class ShopSettingPresenterImpl(val context: Context, val view: ShopSettingPresenter.View) :
    BasePreImpl(view), ShopSettingPresenter {

    //更新Banner

    override fun setBanner(gallery: List<GoodsGalleryVO>?) {
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
                    view.showToast("保存成功")
                    view.hideDialogLoading()
                })
            })
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

    override fun getBanner() {
        mCoroutine.launch {
            val resp = MeRepository.apiService.getBanner().awaitHiResponse()
            handleResponse(resp) {
                view.onSetBanner(it)
            }
        }
    }

    override fun updateShopSlogan(bean: ApplyShopInfo) {
        mCoroutine.launch {
//            view.showDialogLoading()
            val resp = MeRepository.apiService.updateShop(bean).awaitHiResponse()
            if (resp.isSuccess) {
                view.onUpdateSloganSuccess(bean.shopSlogan)
            } else {
                view.onUpdateShopError(resp.code)
            }
            view.hideDialogLoading()
        }
    }

    override fun updateShopNotice(bean: ApplyShopInfo) {
        mCoroutine.launch {
           // view.showDialogLoading()
            val resp = MeRepository.apiService.updateShop(bean).awaitHiResponse()
            if (resp.isSuccess) {
                view.onUpdateNoticeSuccess(bean.shopNotice)
            } else {
                view.onUpdateShopError(resp.code)
            }
            view.hideDialogLoading()
        }
    }

    override fun requestShopManageData() {
        mCoroutine.launch {
          //  view.showDialogLoading()
            val resp = MeRepository.apiService.getShop().awaitHiResponse()
            if (resp.isSuccess) {
                view.onShopManageSuccess(resp.data)
            } else {
                view.onShopManageError(resp.code)
            }
            view.hideDialogLoading()
        }
    }


    override fun updateShopManage(bean: ApplyShopInfo) {
        mCoroutine.launch {
           // view.showDialogLoading()
            val resp = MeRepository.apiService.updateShop(bean).awaitHiResponse()
            if (resp.isSuccess) {
                val info = UserManager.getLoginInfo()
                bean.shopLogo?.apply {
                    info?.shopLogo = bean.shopLogo
                }
                bean.shopName?.apply {
                    info?.shopName = bean.shopName
                }
                info?.apply {
                    UserManager.setLoginInfo(info)
                    EventBus.getDefault().post(info)
                }
                view.showToast("修改成功")
                view.onUpdateShopSuccess(bean)
            } else {
                view.onUpdateShopError(resp.code)
            }
            view.hideDialogLoading()
        }
    }
}