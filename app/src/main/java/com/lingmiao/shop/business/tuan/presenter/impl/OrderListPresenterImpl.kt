package com.lingmiao.shop.business.tuan.presenter.impl

import com.blankj.utilcode.util.LogUtils
import com.lingmiao.shop.business.tuan.bean.GoodsVo
import com.lingmiao.shop.business.tuan.bean.OrderVo
import com.lingmiao.shop.business.tuan.presenter.OrderListPresenter
import com.james.common.base.BasePreImpl
import com.james.common.base.loadmore.RefreshLoadMoreFacade
import com.james.common.base.loadmore.core.IPage
import kotlinx.coroutines.launch

class OrderListPresenterImpl(private var view : OrderListPresenter.View) : BasePreImpl(view), OrderListPresenter{
    override fun loadList(page: IPage, data: MutableList<OrderVo>) {
        mCoroutine.launch {

            view.showDialogLoading();
            LogUtils.dTag(RefreshLoadMoreFacade.TAG, "page=${page.getPageIndex()} loadData")

            var goods = arrayListOf<GoodsVo>();
            goods.add(GoodsVo())
            goods.add(GoodsVo())

            var order = OrderVo()
            order?.goods = goods;
            data.add(order);

            order = OrderVo()
            order?.goods = goods;
            data.add(order);

            order = OrderVo()
            order?.goods = goods;
            data.add(order);

            order = OrderVo()
            order?.goods = goods;
            data.add(order);

            order = OrderVo()
            order?.goods = goods;
            data.add(order);

            view.onLoadMoreSuccess(data, data.size > 10);

            view.hideDialogLoading()
            LogUtils.dTag(RefreshLoadMoreFacade.TAG, "page=${page.getPageIndex()} loadData end")
        }

    }

}