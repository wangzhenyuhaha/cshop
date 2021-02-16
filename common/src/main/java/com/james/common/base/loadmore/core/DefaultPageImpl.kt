package com.james.common.base.loadmore.core

/**
 * Author : Elson
 * Date   : 2020/7/11
 * Desc   : 页面加载状态
 */
class DefaultPageImpl(val defaultPageIndex: Int) : IPage {

    private var pageIndex = defaultPageIndex
    private var hasMore = true
    private var loadingStatus = IPage.STATUS_COMPLETE

    override fun isLoading(): Boolean {
        return loadingStatus == IPage.STATUS_LOADING
    }

    override fun isRefreshing(): Boolean {
        return pageIndex == defaultPageIndex
    }

    override fun setRefreshing(): IPage {
        loadingStatus = IPage.STATUS_LOADING
        pageIndex = defaultPageIndex
        return this
    }

    override fun nextPage(): IPage {
        if (hasMore && !isLoading()) {
            loadingStatus = IPage.STATUS_LOADING
            pageIndex++
        }
        return this
    }

    override fun getPageIndex(): Int {
        return pageIndex
    }

    override fun hasMore(): Boolean {
        return hasMore
    }

    override fun setHasMore(hasMore: Boolean): IPage {
        this.hasMore = hasMore
        return this
    }

    /**
     * 标记加载状态为已完成
     * @param success 有数据才算成功，因为这里需要进行页数更新处理。
     */
    override fun finishLoad(success: Boolean): IPage {
        loadingStatus = IPage.STATUS_COMPLETE
        if (!success && pageIndex > defaultPageIndex) {
            --pageIndex
        }
        return this
    }
}