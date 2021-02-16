package com.james.common.base.loadmore.core

/**
 * Author : Elson
 * Date   : 2020/7/11
 * Desc   :
 */
class LoadMoreConfig {

    var page: IPage = DefaultPageImpl(1)

    var tag: String? = null

    var autoRefresh: Boolean = false

    var requestCommand: IRequestCommand? = null

    var layoutFactory: ILoadMoreLayoutFactory? = null
}