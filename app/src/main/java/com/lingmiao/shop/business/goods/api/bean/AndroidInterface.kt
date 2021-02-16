package com.lingmiao.shop.business.goods.api.bean

import android.content.Context
import android.webkit.JavascriptInterface
import com.blankj.utilcode.util.LogUtils
import com.just.agentweb.AgentWeb


class AndroidInterface(private val agent: AgentWeb, private val context: Context) {
    @JavascriptInterface
    fun callAndroid() {
        LogUtils.d("callAndroid")
    }

}