package com.james.common.base.loadmore.core

import com.scwang.smartrefresh.layout.SmartRefreshLayout

/**
 * Author : Elson
 * Date   : 2020/7/11
 * Desc   :
 */
interface ILoadMoreLayoutFactory {

    fun autoRefresh()

    fun setListener(refresh: RefreshListener, loadMore: LoadMoreListener)

    fun loadFinish(isRefresh: Boolean, hasMore: Boolean)
}

class DefaultLayoutFactory(val refreshLayout: SmartRefreshLayout): ILoadMoreLayoutFactory {

    override fun autoRefresh() {
        refreshLayout.autoRefresh()
    }

    override fun setListener(refresh: RefreshListener, loadMore: LoadMoreListener) {
        refreshLayout.setOnRefreshListener {
            refresh.onRefresh()
        }
        refreshLayout.setOnLoadMoreListener {
            loadMore.onLoadMore()
        }
    }

    override fun loadFinish(isRefresh: Boolean, hasMore: Boolean) {
        if (isRefresh) {
            refreshLayout.finishRefresh()
        }
        if (hasMore) {
            refreshLayout.finishLoadMore()
            refreshLayout.setNoMoreData(false)
        } else {
            // 设置后，不会再触发加载事件
            refreshLayout.finishLoadMoreWithNoMoreData()
        }
    }

}