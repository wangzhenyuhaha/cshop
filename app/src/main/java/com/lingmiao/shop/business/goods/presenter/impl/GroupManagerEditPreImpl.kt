package com.lingmiao.shop.business.goods.presenter.impl

import com.lingmiao.shop.base.CommonRepository
import com.lingmiao.shop.business.goods.api.GoodsRepository
import com.lingmiao.shop.business.goods.api.bean.ShopGroupVO
import com.lingmiao.shop.business.goods.event.GroupRefreshEvent
import com.lingmiao.shop.business.goods.presenter.GroupManagerEditPre
import com.james.common.base.BasePreImpl
import com.james.common.exception.BizException
import com.james.common.utils.exts.checkNotBlack
import com.james.common.utils.exts.isNetUrl
import kotlinx.coroutines.launch
import org.greenrobot.eventbus.EventBus
import java.io.File

/**
 * Author : Elson
 * Date   : 2020/7/18
 * Desc   :
 */
class GroupManagerEditPreImpl(var view: GroupManagerEditPre.GroupEditView) : BasePreImpl(view),
    GroupManagerEditPre {

    override fun submit(groupVO: ShopGroupVO, groupLevel: Int?) {
        try {
            checkNotBlack(groupVO.shopCatName) { "请输入菜单名称" }
            //checkNotBlack("${groupVO.sort}") { "请输入菜单排序" }

            view.showDialogLoading("正在提交...")
            mCoroutine.launch {
                uploadImg(groupVO.shopCatPic) {
                    groupVO.shopCatPic = it
                    val resp = GoodsRepository.submitShopGroup(groupVO)
                    handleResponse(resp) {
                        EventBus.getDefault().post(GroupRefreshEvent(groupLevel))
                        view.showToast("添加成功")
                        view.finish()
                    }
                }
                view.hideDialogLoading()
            }
        } catch (e: BizException) {
             view.showToast(e.message)
        }
    }

    override fun modifyGroup(groupVO: ShopGroupVO, groupLevel: Int?) {
        if (groupVO.shopCatId.isNullOrBlank()) {
            return
        }
        view.showDialogLoading("正在提交...")
        mCoroutine.launch {
            uploadImg(groupVO.shopCatPic) {
                groupVO.shopCatPic = it
                val resp = GoodsRepository.updateShopGroup(groupVO.shopCatId!!, groupVO)
                handleResponse(resp) {
                    EventBus.getDefault().post(GroupRefreshEvent(groupLevel))
                     view.showToast("编辑成功")
                    view.finish()
                }
            }
            view.hideDialogLoading()
        }
    }

    private suspend fun uploadImg(imgPath: String?, callback: suspend (String?) -> Unit) {
        if (imgPath.isNullOrBlank() || imgPath.isNetUrl()) {
            callback.invoke(imgPath)
            return
        }
        val resp = CommonRepository.uploadFile(File(imgPath), true, CommonRepository.SCENE_GOODS)
        if (resp.isSuccess) {
            callback.invoke(resp.data.url)
        } else {
            handleErrorMsg(resp.msg)
        }
    }
}