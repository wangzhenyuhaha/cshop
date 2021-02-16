package com.james.common.base.list

import com.james.common.base.loadmore.core.IPage
import com.james.common.base.loadmore.core.IRequestCommand

interface BaseListView<T> : IRequestCommand {
    fun onLoadSuccess(list: List<T>?, hasMore: Boolean)
    fun onLoadFailed()
    fun onLoadFailed(success: Boolean)

    override fun executePageRequest(page: IPage)
}