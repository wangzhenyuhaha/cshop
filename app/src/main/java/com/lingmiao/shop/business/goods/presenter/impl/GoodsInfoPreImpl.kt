package com.lingmiao.shop.business.goods.presenter.impl

import com.lingmiao.shop.business.goods.api.GoodsRepository
import com.james.common.base.BasePreImpl
import com.lingmiao.shop.base.UserManager
import com.lingmiao.shop.business.goods.api.bean.GoodsParamVo
import com.lingmiao.shop.business.goods.presenter.GoodsInfoPre
import kotlinx.coroutines.launch


/**
 * Author : Elson
 * Date   : 2020/8/13
 * Desc   : 规格设置页
 */
class GoodsInfoPreImpl(val view: GoodsInfoPre.PublicView) : BasePreImpl(view),
    GoodsInfoPre {

    override fun addInfo(cId: String, name: String) {
        mCoroutine.launch {
            view?.showDialogLoading()
            val vo : GoodsParamVo = GoodsParamVo();
            vo.paramName = name;
            vo.categoryId = cId;
            vo.groupId = 0;
            vo.isIndex = 0;
            vo.paramType = 1
            vo.required = 0;
            val resp = GoodsRepository.addInfo(vo);
            handleResponse(resp) {
                view?.onAddInfo(resp.data);
            }
            view?.hideDialogLoading()
        }
    }


    var shopId : Int? = null;

    fun getSellerId() : Int? {
        if(shopId == null) {
            shopId = UserManager.getLoginInfo()?.shopId;
        }
        return shopId;
    }


    override fun loadInfoList(id: String?) {
        // 编辑商品的场景下，goodsId 不为空
        if (id.isNullOrBlank()) {
            return
        }
        mCoroutine.launch {
            val resp = GoodsRepository.getInfoListOfCategory(id, getSellerId())
            handleResponse(resp) {
                view?.onLoadInfoListSuccess(resp.data)
            }
        }
    }

    override fun deleteInfo(id: String) {
        mCoroutine.launch {
            val resp = GoodsRepository.deleteInfo(id)
            handleResponse(resp) {
                view?.onInfoDeleted(id);
            }
        }
    }

}