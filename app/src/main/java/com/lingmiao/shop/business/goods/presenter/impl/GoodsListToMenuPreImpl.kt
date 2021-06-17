package com.lingmiao.shop.business.goods.presenter.impl

import android.content.Context
import android.view.View
import com.james.common.base.BasePreImpl
import com.james.common.base.loadmore.core.IPage
import com.lingmiao.shop.business.goods.api.GoodsRepository
import com.lingmiao.shop.business.goods.api.bean.GoodsVO
import com.lingmiao.shop.business.goods.presenter.GoodsListToMenuPre
import kotlinx.coroutines.launch

/**
Create Date : 2021/6/19:59 AM
Auther      : Fox
Desc        :
 **/
class GoodsListToMenuPreImpl(val context: Context, val view : GoodsListToMenuPre.View) : BasePreImpl(), GoodsListToMenuPre{

    private val mCatePopPreImpl : PopUserCatePreImpl by lazy { PopUserCatePreImpl(view) }

    override fun showCategoryPop(cid : String?,target: View) {
        mCatePopPreImpl.showGoodsGroupPop(context, cid) { its, name ->
            view.onUpdatedCategory(its, name)
        }
    }

    override fun loadListData(path : String?, page: IPage, list: List<*>) {
        mCoroutine.launch {
            val resp = GoodsRepository.loadGoodsListOfCatePath(1, GoodsVO.MARKET_STATUS_ENABLE.toString(), GoodsVO.getEnableAuth(), path)
            if (resp.isSuccess) {
                val goodsList = resp.data.data
                view.onLoadMoreSuccess(goodsList, goodsList?.size?:0 >= 10)
            } else {
                view.onLoadMoreFailed()
            }
        }
    }

    override fun bindGoods(ids: List<Int?>?, id: String) {
        mCoroutine.launch {
            val resp = GoodsRepository.bindGoods(ids, id);
            handleResponse(resp) {
                view?.showToast("添加成功");
            }
        }
    }

}