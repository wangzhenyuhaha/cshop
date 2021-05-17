package com.lingmiao.shop.business.goods.presenter.impl

import com.lingmiao.shop.business.goods.api.GoodsRepository
import com.james.common.base.BasePreImpl
import com.lingmiao.shop.business.goods.presenter.GoodsSpecPre
import kotlinx.coroutines.launch


/**
 * Author : Elson
 * Date   : 2020/8/13
 * Desc   : 规格设置页
 */
class GoodsSpecPreImpl(val view: GoodsSpecPre.PublicView) : BasePreImpl(view),
    GoodsSpecPre {

    override fun add(cId: String, name: String) {
        mCoroutine.launch {
            view?.showDialogLoading()
            val resp = GoodsRepository.addSpec(cId, name);
            handleResponse(resp) {
                view?.onAdded(resp.data);
            }
            view?.hideDialogLoading()
        }
    }

    override fun loadList(id: String?) {
        // 编辑商品的场景下，goodsId 不为空
        if (id.isNullOrBlank()) {
            return
        }
        mCoroutine.launch {
            val resp = GoodsRepository.getSpecList(id)
            handleResponse(resp) {
                view?.onLoaded(resp.data)
            }
        }
    }

    override fun delete(id: String) {
        mCoroutine.launch {
            val resp = GoodsRepository.deleteInfo(id)
            handleResponse(resp) {
                view?.onDeleted(id);
            }
        }
    }

}