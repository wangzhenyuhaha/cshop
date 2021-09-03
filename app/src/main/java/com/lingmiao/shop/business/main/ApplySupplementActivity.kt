package com.lingmiao.shop.business.main

import android.annotation.SuppressLint
import android.view.View
import android.webkit.WebSettings
import android.webkit.WebView
import android.webkit.WebViewClient
import com.james.common.base.BaseVBActivity
import com.lingmiao.shop.business.main.presenter.IApplySupplementPresenter
import com.lingmiao.shop.business.main.presenter.impl.ApplySupplementPresenterImpl
import com.lingmiao.shop.databinding.MainActivityElectricSignBinding

/**
Create Date : 2021/8/2910:52 上午
Auther      : Fox
Desc        :
 **/
class ApplySupplementActivity : BaseVBActivity<MainActivityElectricSignBinding, IApplySupplementPresenter>(), IApplySupplementPresenter.View {

    override fun createPresenter(): IApplySupplementPresenter {
        return ApplySupplementPresenterImpl(this, this);
    }

    override fun getViewBinding(): MainActivityElectricSignBinding {
        return MainActivityElectricSignBinding.inflate(layoutInflater);
    }


    override fun useBaseLayout(): Boolean {
        return true;
    }

    override fun useLightMode(): Boolean {
        return false;
    }


    override fun initView() {
        mToolBarDelegate?.setMidTitle("资料补充")
        initWebView();
        mPresenter?.getSupplementSign();
    }

    @SuppressLint("JavascriptInterface")
    fun initWebView() {
        // 设置屏幕自适应。
        mBinding.wvView.settings.useWideViewPort = true
        mBinding.wvView.settings.loadWithOverviewMode = true
        // 建议禁止缓存加载，以确保在攻击发生时可快速获取最新的滑动验证组件进行对抗。
        mBinding.wvView.settings.cacheMode = WebSettings.LOAD_NO_CACHE
        mBinding.wvView.webViewClient = object : WebViewClient() {
            override fun shouldOverrideUrlLoading(view: WebView, url: String): Boolean {
                view.loadUrl(url)
                return true
            }
        }
        mBinding.wvView.settings.javaScriptEnabled = true
        mBinding.wvView.addJavascriptInterface(this, "android")
    }

    override fun setUrl(url: String?) {
        mBinding.wvView.loadUrl(url)
    }

    override fun getSupplementUrlFailed() {
        showToast("获取补充信息失败，请重试")
        finish();
    }

    override fun onSupportNavigateUp(): Boolean {
        if(mBinding.wvView.canGoBack()) {
            return false;
        }
        return super.onSupportNavigateUp();
    }

}