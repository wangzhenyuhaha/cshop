package com.lingmiao.shop.business.order.presenter.impl

import android.content.Context
import com.blankj.utilcode.util.ResourceUtils
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.james.common.utils.exts.isNotEmpty
import com.james.common.base.BasePreImpl
import com.james.common.base.loadmore.core.IPage
import com.lingmiao.shop.business.common.bean.PageVO
import com.lingmiao.shop.business.order.bean.OrderList
import com.lingmiao.shop.business.order.presenter.OrderSearchPre
import kotlinx.coroutines.launch

/**
 * Author : Elson
 * Date   : 2020/7/11
 * Desc   : 商品出售中列表
 */
class OrderSearchPreImpl(var context: Context, var view: OrderSearchPre.StatusView) : BasePreImpl(view), OrderSearchPre {

    override fun loadListData(page: IPage, oldDatas: List<*>, searchText: String?, isGoodsName : Boolean) {
        if (searchText.isNullOrBlank()) {
            view.showToast("请输入搜索文字")
            return
        }

        mCoroutine.launch {
            if (oldDatas.isEmpty()) {
                view.showPageLoading()
            }

//            val resp = GoodsRepository.loadGoodsListByName(page.getPageIndex(), if(isGoodsName) searchText else null, if(isGoodsName) null else searchText)
//            if (resp.isSuccess) {
//                val goodsList = resp.data.data
//                view.onLoadMoreSuccess(goodsList, goodsList.isNotEmpty())
//            } else {
//                view.onLoadMoreFailed()
//            }
            val goodsList = getTempList();
            view.onLoadMoreSuccess(goodsList, goodsList.isNotEmpty())

            view.hidePageLoading()
        }
    }

    fun getTempList() : List<OrderList>? {
        val regionsType = object : TypeToken<PageVO<OrderList>>(){}.type;
        var json = Gson().fromJson<PageVO<OrderList>>(ResourceUtils.readAssets2String("order.json"), regionsType);
//        json?.data?.forEachIndexed { index, orderList ->
//            orderList?.orderStatus = status;
//        }
        return json?.data;
    }

}