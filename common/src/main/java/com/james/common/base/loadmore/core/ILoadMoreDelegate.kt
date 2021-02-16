package com.james.common.base.loadmore.core

/**
 * Author : Elson
 * Date   : 2020/7/11
 * Desc   :
 */
interface IRefreshLoadMoreDelegate: RefreshListener, LoadMoreListener, IResponseStatus {

    fun initConfig(config: LoadMoreConfig)

    /**
     * 执行下拉刷新操作
     */
    fun refresh()
    fun getPage(): IPage

    override fun onRefresh()
    override fun onLoadMore()
    override fun loadFinish(hasMore: Boolean, success: Boolean)
}

/**
 * 监听上拉加载操作
 */
interface RefreshListener {
    fun onRefresh()
}

/**
 * 监听上拉加载操作的
 */
interface LoadMoreListener {
    fun onLoadMore()
}

interface IRequestCommand {
    /**
     * 执行分页请求
     */
    fun executePageRequest(page: IPage)
}

interface IResponseStatus {
    /**
     * 结束刷新/加载状态
     */
    fun loadFinish(hasMore: Boolean, success: Boolean)
}
