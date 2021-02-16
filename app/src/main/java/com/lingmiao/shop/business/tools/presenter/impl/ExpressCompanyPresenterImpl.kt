package com.lingmiao.shop.business.tools.presenter.impl

import com.blankj.utilcode.util.LogUtils
import com.lingmiao.shop.business.tools.api.ToolsRepository
import com.lingmiao.shop.business.tools.bean.ExpressCompanyVo
import com.lingmiao.shop.business.tools.presenter.ExpressCompanyPresenter
import com.james.common.base.BasePreImpl
import com.james.common.base.loadmore.RefreshLoadMoreFacade
import com.james.common.base.loadmore.core.IPage
import kotlinx.coroutines.launch

class ExpressCompanyPresenterImpl(var view : ExpressCompanyPresenter.View) : BasePreImpl(view), ExpressCompanyPresenter {


    companion object {
        const val TAG = "ExpressCompanyPresenterImpl"
    }

    override fun loadList(page: IPage, datas: List<ExpressCompanyVo>) {
        mCoroutine.launch {

            LogUtils.dTag(RefreshLoadMoreFacade.TAG, "page=${page.getPageIndex()} loadData")
            if (datas.isEmpty()) {
                view.showPageLoading()
            }
            val resp = ToolsRepository.logiCompanies();
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

    override fun clickItemView(item: ExpressCompanyVo?, position: Int) {
        mCoroutine.launch {

            view.showDialogLoading()

            if(item?.logisticsCompanyDo == null || item?.logisticsCompanyDo?.id == null) {
                view.hideDialogLoading()
                return@launch
            }
            item?.apply {
                if(item.open == true) {
                    val resp = ToolsRepository.closeCompany(item.logisticsCompanyDo?.id!!);
                    handleResponse(resp) {
                        view.onStatusEdit(false, position);
                    }
                } else {
                    val resp = ToolsRepository.openCompany(item.logisticsCompanyDo?.id!!);
                    handleResponse(resp) {
                        view.onStatusEdit(true, position);
                    }
                }
            }

            view.hideDialogLoading()
        }
    }
}