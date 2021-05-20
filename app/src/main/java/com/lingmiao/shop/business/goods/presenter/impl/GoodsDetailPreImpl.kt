package com.lingmiao.shop.business.goods.presenter.impl

import android.content.Context
import com.james.common.base.BasePreImpl
import com.lingmiao.shop.base.UserManager
import com.lingmiao.shop.business.goods.api.GoodsRepository
import com.lingmiao.shop.business.goods.api.bean.GoodsParamVo
import com.lingmiao.shop.business.goods.presenter.GoodsDetailPre
import kotlinx.coroutines.launch

/**
Create Date : 2021/3/69:59 AM
Auther      : Fox
Desc        :
 **/
class GoodsDetailPreImpl(var context: Context, var view: GoodsDetailPre.View) : BasePreImpl(view),GoodsDetailPre {


    var shopId : Int? = null;

    fun getSellerId() : Int? {
        if(shopId == null) {
            shopId = UserManager.getLoginInfo()?.shopId;
        }
        return shopId;
    }


    override fun loadInfoByCId(cid: String) {
        mCoroutine.launch {
            val resp = GoodsRepository.getInfoListOfCategory(cid!!, getSellerId())
            if (resp.isSuccess) {
                view.onLoadedInfoList(resp.data)
                return@launch
            }
            view.showToast("商品信息加载失败，请重试。")
        }
    }

    override fun addInfo(cId : String, str: String) {
        mCoroutine.launch {
            view?.showDialogLoading()
            val vo : GoodsParamVo = GoodsParamVo();
            vo.paramName = str;
            vo.categoryId = cId;
            vo.groupId = 0;
            vo.isIndex = 0;
            vo.paramType = 1
            vo.required = 0;
            val resp = GoodsRepository.addInfo(vo);
            handleResponse(resp) {
                view?.onInfoAdded(resp.data);
            }
            view?.hideDialogLoading()
        }
    }

}