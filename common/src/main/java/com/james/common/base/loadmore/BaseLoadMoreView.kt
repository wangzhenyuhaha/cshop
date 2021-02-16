package com.james.common.base.loadmore

import com.james.common.base.loadmore.core.IPage
import com.james.common.base.loadmore.core.IRequestCommand

/**
 * Author : Elson
 * Date   : 2020/7/11
 * Desc   : 分页加载页面的 View 接口需要继承它
 */
interface BaseLoadMoreView<T> : IRequestCommand {
    fun onLoadMoreSuccess(list: List<T>?, hasMore: Boolean)
    fun onLoadMoreFailed()
    fun onLoadMoreFailed(hasMore: Boolean, success: Boolean)

    override fun executePageRequest(page: IPage)
}