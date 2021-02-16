package com.james.common.base.loadmore.core

/**
 * Author : Elson
 * Date   : 2020/7/11
 * Desc   :
 */
interface IPage {

    companion object {
        const val STATUS_COMPLETE = 0
        const val STATUS_LOADING = 1
    }

    /**
     * 当前是是否为加载状态
     */
    fun isLoading(): Boolean

    /**
     * 是否是第1页
     */
    fun isRefreshing(): Boolean

    /**
     * 重置为第1页
     */
    fun setRefreshing(): IPage

    /**
     * 切换到下一页
     */
    fun nextPage(): IPage

    /**
     * 获取当前是第几页
     */
    fun getPageIndex(): Int

    fun hasMore(): Boolean

    fun setHasMore(hasMore: Boolean): IPage

    /**
     * 标记加载状态为已完成
     */
    fun finishLoad(success: Boolean): IPage
}