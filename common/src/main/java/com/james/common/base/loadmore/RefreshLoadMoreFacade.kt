package com.james.common.base.loadmore

import com.blankj.utilcode.util.LogUtils
import com.james.common.base.loadmore.core.*

/**
 * Author : Elson
 * Date   : 2020/7/11
 * Desc   : 门面类：对上拉加载和下拉刷新的逻辑进行封装
 */
class RefreshLoadMoreFacade : IRefreshLoadMoreDelegate {

    companion object {
        const val TAG = "RefreshLoadMoree"
    }

    private lateinit var mConfig: LoadMoreConfig
    private var mRequestCommand: IRequestCommand? = null
    private var mLayoutFactory: ILoadMoreLayoutFactory? = null

    override fun initConfig(config: LoadMoreConfig) {
        this.mConfig = config
        this.mRequestCommand = config.requestCommand

        this.mLayoutFactory = config.layoutFactory
        this.mLayoutFactory?.setListener(this, this)
    }

    override fun refresh() {
        if (mConfig.autoRefresh) {
            LogUtils.dTag(TAG, "page=${mConfig.tag} ${TAG} refresh auto")
            mLayoutFactory?.autoRefresh() //有动画的刷新，会触发 onRefresh
        } else {
            LogUtils.dTag(TAG, "page=${mConfig.tag} ${TAG} refresh")
            onRefresh() // 无动画，手动触发 onRefresh
        }
    }

    override fun onRefresh() {
        if (mConfig.page.isLoading()) {
            LogUtils.dTag(TAG, "page=${mConfig.tag} ${TAG} onRefresh reject loading")
            return
        }
        LogUtils.dTag(TAG, "page=${mConfig.tag} ${TAG} onRefresh can loading")
        val page = mConfig.page.setRefreshing()
        mRequestCommand?.executePageRequest(page)
    }

    override fun onLoadMore() {
        LogUtils.dTag(TAG, "page=${mConfig.tag} ${TAG} onLoadMore start")
        val currentPage = mConfig.page
        if (mRequestCommand != null && currentPage.hasMore()) {
            // 判断当前是否正在执行加载操作
            if (!currentPage.isLoading()) {
                LogUtils.dTag(TAG, "page=${mConfig.tag} ${TAG} onLoadMore can loading")
                val nextPage = currentPage.nextPage()
                mRequestCommand!!.executePageRequest(nextPage)
            } else {
                LogUtils.dTag(TAG, "page=${mConfig.tag} ${TAG} onLoadMore reject loading")
            }
        } else {
            LogUtils.dTag(TAG, "page=${mConfig.tag} ${TAG} onLoadMore no more")
            loadFinish(false, false)
        }
    }

    override fun loadFinish(hasMore: Boolean, success: Boolean) {
        LogUtils.dTag(
            TAG,
            "page=${mConfig.tag} ${TAG} loadFinish hasMore=${hasMore}; success=${success}"
        )
        val page = mConfig.page
        mLayoutFactory?.loadFinish(page.isRefreshing(), hasMore)
        page.setHasMore(hasMore).finishLoad(success)
    }

    override fun getPage(): IPage {
        return mConfig.page
    }

}