package com.lingmiao.shop.business.tools.presenter.impl

import com.blankj.utilcode.util.LogUtils
import com.lingmiao.shop.business.tools.api.ToolsRepository
import com.lingmiao.shop.business.tools.bean.FreightVoItem
import com.lingmiao.shop.business.tools.presenter.FreightModelPresenter
import com.james.common.base.BasePreImpl
import com.james.common.base.loadmore.RefreshLoadMoreFacade
import com.james.common.base.loadmore.core.IPage
import kotlinx.coroutines.launch

class FreightModelPresenterImpl(var view : FreightModelPresenter.View) : BasePreImpl(view), FreightModelPresenter {

    override fun loadList(page: IPage, datas: MutableList<FreightVoItem>) {
        mCoroutine.launch {
            LogUtils.dTag(RefreshLoadMoreFacade.TAG, "page=${page.getPageIndex()} loadData")
            if (datas.isEmpty()) {
                view.showPageLoading()
            }
            val resp = ToolsRepository.shipTemplates("");
            if (resp?.isSuccess) {
                val list = resp?.data ?: arrayListOf();
                view.onLoadMoreSuccess(list, false)
            } else {
                view.onLoadMoreFailed();
            }
            view.hidePageLoading()
            LogUtils.dTag(RefreshLoadMoreFacade.TAG, "page=${page.getPageIndex()} loadData end")
        }
    }

    override fun onDeleteItem(item: FreightVoItem?, position: Int) {
        mCoroutine.launch {
            LogUtils.dTag("delete model" + item?.id);
            val resp = ToolsRepository.deleteShipTemplates(item?.id!!);
            handleResponse(resp) {
                view.onModelDeleted(position);
            }
            view.hidePageLoading()
        }
    }
}