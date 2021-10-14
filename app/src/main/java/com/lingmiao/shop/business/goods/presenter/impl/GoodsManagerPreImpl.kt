package com.lingmiao.shop.business.goods.presenter.impl

import android.content.Context
import android.util.Log
import android.view.View
import com.james.common.base.BasePreImpl
import com.james.common.base.loadmore.core.IPage
import com.james.common.utils.exts.isNotEmpty
import com.lingmiao.shop.base.UserManager
import com.lingmiao.shop.business.goods.api.GoodsRepository
import com.lingmiao.shop.business.goods.presenter.GoodsManagerPre
import kotlinx.coroutines.launch

/**
Create Date : 2021/3/69:59 AM
Auther      : Fox
Desc        :
 **/
class GoodsManagerPreImpl(var context: Context, var view: GoodsManagerPre.View) : BasePreImpl(view),GoodsManagerPre {

    private val mCategoryPreImpl: PopCategoryPreImpl by lazy { PopCategoryPreImpl(view) }

    override fun showCategoryPop(target: View) {
        val id = UserManager?.getLoginInfo()?.goodsCateId?:"0"
        mCategoryPreImpl?.showCenterCategoryPop(context, id) { items,str ->
            if(items?.size?:0 > 0) {
                val item = items?.get(items?.size!! - 1)
                view.onUpdateCategory(item)
            }
        }
    }

    override fun loadListData(page: IPage, oldDatas: List<*>, cId: String) {
        mCoroutine.launch {
            Log.d("WZYUUI","AAA")
            if (oldDatas.isEmpty()) {
                view.showPageLoading()
            }
            val resp = GoodsRepository.getCenterGoods(page.getPageIndex(), cId)
            Log.d("WZYUUI",page.getPageIndex().toString())
            if (resp.isSuccess) {
                val goodsList = resp.data.data

                //??
                view.onLoadMoreSuccess(goodsList, goodsList.isNotEmpty())
            } else {
                view.onLoadMoreFailed()
            }
            view.hidePageLoading()
        }


        //
        //            val resp = GoodsRepository.loadGoodsListByName(page.getPageIndex(), if(isGoodsName) searchText else null, if(isGoodsName) null else searchText)
        //            if (resp.isSuccess) {
        //                val goodsList = resp.data.data
        //                view.onLoadMoreSuccess(goodsList, goodsList.isNotEmpty())
        //            } else {
        //                view.onLoadMoreFailed()
        //            }
        //            view.hidePageLoading()
        //        }
    }

    override fun add(ids: String) {
        mCoroutine.launch {
            view.showDialogLoading()

            val resp = GoodsRepository.addGoodsOfCenter(ids)

            handleResponse(resp) {
                view.onAddSuccess()
            }
            view.hideDialogLoading()
        }
    }

}