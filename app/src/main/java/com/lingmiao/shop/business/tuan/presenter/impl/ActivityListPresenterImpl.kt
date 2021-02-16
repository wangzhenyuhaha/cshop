package com.lingmiao.shop.business.tuan.presenter.impl

import com.blankj.utilcode.util.LogUtils
import com.lingmiao.shop.business.tuan.bean.ActivityVo
import com.lingmiao.shop.business.tuan.presenter.ActivityListPresenter
import com.james.common.base.BasePreImpl
import com.james.common.base.loadmore.RefreshLoadMoreFacade
import com.james.common.base.loadmore.core.IPage
import kotlinx.coroutines.launch

class ActivityListPresenterImpl(private  val view : ActivityListPresenter.View) : BasePreImpl(view), ActivityListPresenter {

    override fun loadList(page: IPage, data: MutableList<ActivityVo>) {

        mCoroutine.launch {

            view.showDialogLoading();
            LogUtils.dTag(RefreshLoadMoreFacade.TAG, "page=${page.getPageIndex()} loadData")

            data.add(ActivityVo());
            view.onLoadMoreSuccess(data, data.size > 10);

            view.hideDialogLoading()
            LogUtils.dTag(RefreshLoadMoreFacade.TAG, "page=${page.getPageIndex()} loadData end")
        }

    }

}