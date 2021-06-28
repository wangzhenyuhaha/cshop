package com.lingmiao.shop.business.goods.presenter.impl

import android.content.Context
import com.lingmiao.shop.business.goods.api.GoodsRepository
import com.james.common.base.BasePreImpl
import com.lingmiao.shop.business.goods.api.bean.CateSpecAndValueVo
import com.lingmiao.shop.business.goods.api.bean.GoodsSpecVo
import com.lingmiao.shop.business.goods.pop.SpecAndValuePop
import com.lingmiao.shop.business.goods.presenter.GoodsSpecPre
import kotlinx.coroutines.launch


/**
 * Author : Elson
 * Date   : 2020/8/13
 * Desc   : 规格设置页
 */
class GoodsSpecPreImpl(val context: Context, val view: GoodsSpecPre.PublicView) : BasePreImpl(view),
    GoodsSpecPre {

    override fun addSpecValue(specKeyId: String, valueNames: String) {
        mCoroutine.launch {
            view.showDialogLoading()
            val resp = GoodsRepository.submitSpceValues(specKeyId, valueNames)
            handleResponse(resp) {
                view.onAddSpecValueSuccess(specKeyId, resp.data)
            }
            view.hideDialogLoading()
        }
    }

    override fun add(id: String, key: String, value: String) {
        mCoroutine.launch {
            view?.showDialogLoading()

            val item = CateSpecAndValueVo();
            item.categoryId = id;
            item.specName = key;
            item.valueString = value;
            val resp = GoodsRepository.addSpecAndValue(item);
            handleResponse(resp) {
                val _it = GoodsSpecVo();
                _it.specName = it.specName;
                view?.onAdded(_it);
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

    fun addRequest(id: String, key: String, value: String,callback : (CateSpecAndValueVo) -> Unit) {
        mCoroutine.launch {
            view?.showDialogLoading()

            val item = CateSpecAndValueVo();
            item.categoryId = id;
            item.specName = key;
            item.valueString = value;

            val resp = GoodsRepository.addSpecAndValue(item);

            handleResponse(resp) {
                val _it = GoodsSpecVo();
                _it.specName = it.specName;
                view?.onAdded(_it);

                callback?.invoke(item);
            }
            view?.hideDialogLoading()
        }
    }

    override fun delete(id: String) {
        mCoroutine.launch {
            val resp = GoodsRepository.delSpecName(id)
            handleResponse(resp) {
                view?.onDeleted(id);
            }
        }
    }

    override fun deleteValue(position : Int , id : String) {
        mCoroutine.launch {
            val resp = GoodsRepository.delSpecValue(id)
            handleResponse(resp) {
                view?.onDeletedValue(position, id);
            }
        }
    }

    override fun showAddPop(cId: String) {
        val pop = SpecAndValuePop(context);
        pop.setConfirmListener { s1, s2 ->
            if(s1?.isNotEmpty() == true) {
                addRequest(cId, s1?:"", s2?:"") {
                    pop.dismiss();
                }
            }
        }
        pop.showPopupWindow()
    }
}