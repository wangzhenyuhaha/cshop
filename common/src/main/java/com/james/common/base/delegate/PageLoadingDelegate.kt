package com.james.common.base.delegate

import android.app.Activity
import android.content.Context
import com.blankj.utilcode.util.LogUtils
import com.james.common.view.EmptyLayout

/**
 * Author : Elson
 * Date   : 2020/7/12
 * Desc   : 页面内 Loading 操作代理
 */
interface PageLoadingDelegate {
    fun setPageLoadingLayout(layout: EmptyLayout)
    fun showPageLoading()
    fun hidePageLoading()
    fun showNoData()
    fun showNoNetwork()
    fun showDataError()
}

class DefaultPageLoadingDelegate(val context: Context) : PageLoadingDelegate {

    private var elEmpty: EmptyLayout? = null

    override fun setPageLoadingLayout(layout: EmptyLayout) {
        elEmpty = layout
    }

    override fun showPageLoading() {
        LogUtils.w("showLoading")
        if (isFinishing()) return
        elEmpty?.setErrorType(EmptyLayout.LOADING)
    }

    override fun hidePageLoading() {
        if (isFinishing()) return
        elEmpty?.setErrorType(EmptyLayout.HIDE_LAYOUT)
    }

    override fun showNoData() {
        if (isFinishing()) return
        elEmpty?.setErrorType(EmptyLayout.NO_DATA)
    }

    override fun showNoNetwork() {
        if (isFinishing()) return
        elEmpty?.setErrorType(EmptyLayout.NO_NETWORK)
    }

    override fun showDataError() {
        if (isFinishing()) return
        elEmpty?.setErrorType(EmptyLayout.DATA_ERROR)
    }

    private fun isFinishing(): Boolean {
        return elEmpty == null ||  (context as Activity).isFinishing
    }
}