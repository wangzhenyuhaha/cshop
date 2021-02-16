package com.james.common.base

interface BaseView {
    // 页面内
    fun showPageLoading()
    fun hidePageLoading()
    fun showNoData()
    fun showNoNetwork()
    fun showDataError()

    // DialogLoading
    fun showDialogLoading()
    fun showDialogLoading(content: String?)
    fun hideDialogLoading()

    fun showToast(content: String?)
    fun showBottomToast(content: String?)
}