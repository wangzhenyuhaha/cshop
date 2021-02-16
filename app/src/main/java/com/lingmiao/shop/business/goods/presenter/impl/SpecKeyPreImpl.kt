package com.lingmiao.shop.business.goods.presenter.impl

import com.blankj.utilcode.util.ToastUtils
import com.chad.library.adapter.base.entity.MultiItemEntity
import com.lingmiao.shop.business.goods.api.GoodsRepository
import com.lingmiao.shop.business.goods.api.bean.GoodsSpecKeyAddVO
import com.lingmiao.shop.business.goods.api.bean.SpecKeyVO
import com.lingmiao.shop.business.goods.presenter.SpecKeyPre
import com.james.common.base.BasePreImpl
import com.james.common.exception.BizException
import com.james.common.utils.exts.checkNotBlack
import kotlinx.coroutines.launch

/**
 * Author : Elson
 * Date   : 2020/7/19
 * Desc   : 获取规格名列表
 */
class SpecKeyPreImpl(val view: SpecKeyPre.SpceKeyView) : BasePreImpl(view),
    SpecKeyPre {

    override fun loadGoodsSpecKey(categoryId: String?, specList: List<SpecKeyVO>?) {
        if (categoryId.isNullOrBlank()) {
            return
        }
        mCoroutine.launch {
            val resp = GoodsRepository.loadSpecKey(categoryId)
            if (resp.isSuccess) {
                val list = mutableListOf<MultiItemEntity>()
                restoreSelectStatus(resp.data, specList)
                list.addAll(resp.data)
                list.add(GoodsSpecKeyAddVO())
                view.onLoadSuccess(list)
            } else {
                ToastUtils.showShort("网络异常，请重试")
            }
        }
    }

    override fun submitSpecKey(categoryId: String?, specName: String?) {
        try {
            checkNotBlack(categoryId) { "" }
            checkNotBlack(specName) { "请输入规格名" }

            mCoroutine.launch {
                val resp = GoodsRepository.submitSpceKey(categoryId!!, specName!!)
                if (resp.isSuccess) {
                    view.onAddSpecSuccess(resp.data)
                    return@launch
                }
                view.showToast("规格名称失败，请重试。")
            }
        } catch (e: BizException) {
            view.showToast(e.message)
        }
    }

    /**
     * 还原规格名选中状态
     */
    private fun restoreSelectStatus(specList: List<SpecKeyVO>?, selectedList: List<SpecKeyVO>?) {
        if (specList.isNullOrEmpty() || selectedList.isNullOrEmpty()) {
            return
        }
        selectedList.forEach { selectedSpec ->
            specList.forEach { specVO ->
                if (specVO.specId == selectedSpec.specId) {
                    specVO.isSelected = true
                }
            }
        }
    }

}