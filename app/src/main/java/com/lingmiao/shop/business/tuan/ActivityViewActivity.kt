package com.lingmiao.shop.business.tuan

import android.annotation.SuppressLint
import android.webkit.WebSettings
import android.webkit.WebView
import android.webkit.WebViewClient
import com.blankj.utilcode.util.LogUtils
import com.lingmiao.shop.R
import com.lingmiao.shop.business.tuan.presenter.ActivityViewPresenter
import com.lingmiao.shop.business.tuan.presenter.impl.ActivityViewPresenterImpl
import com.james.common.base.BaseActivity
import com.james.common.net.RetrofitUtil
import kotlinx.android.synthetic.main.activity_login.*
import kotlinx.android.synthetic.main.tuan_activity_view_activity.*

class ActivityViewActivity : BaseActivity<ActivityViewPresenter>(), ActivityViewPresenter.View {

    override fun createPresenter(): ActivityViewPresenter {
        return ActivityViewPresenterImpl(this);
    }

    override fun initView() {
        mToolBarDelegate.setMidTitle("活动介绍");

        initWebView();
    }

    override fun getLayoutId(): Int {
        return R.layout.tuan_activity_view_activity;
    }

    @SuppressLint("JavascriptInterface")
    fun initWebView() {
        // 设置屏幕自适应。
        wvActivityView.settings.useWideViewPort = true
        wvActivityView.settings.loadWithOverviewMode = true
        // 建议禁止缓存加载，以确保在攻击发生时可快速获取最新的滑动验证组件进行对抗。
        wvActivityView.settings.cacheMode = WebSettings.LOAD_NO_CACHE
        wvActivityView.webViewClient = object : WebViewClient() {
            override fun shouldOverrideUrlLoading(view: WebView, url: String): Boolean {
                view.loadUrl(url)
                return true
            }
        }
        wvActivityView.settings.javaScriptEnabled = true
        wvActivityView.addJavascriptInterface(this, "android")
        wvActivityView.loadUrl(RetrofitUtil.getBaseUrl() + "/captcha.html")
//        wvCaptcha.loadUrl("http://t-api.seller.fisheagle.cn:7003/captcha.html")
        LogUtils.d(wvActivityView.url)
    }
}