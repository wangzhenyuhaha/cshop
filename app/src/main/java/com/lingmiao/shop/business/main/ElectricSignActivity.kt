package com.lingmiao.shop.business.main

import android.annotation.SuppressLint
import android.webkit.WebSettings
import android.webkit.WebView
import android.webkit.WebViewClient
import com.blankj.utilcode.util.LogUtils
import com.james.common.base.BaseVBActivity
import com.james.common.net.RetrofitUtil
import com.lingmiao.shop.business.main.presenter.IElectricSignPresenter
import com.lingmiao.shop.business.main.presenter.impl.ElectricSignPresenterImpl
import com.lingmiao.shop.databinding.MainActivityElectricSignBinding

/**
Create Date : 2021/8/144:50 下午
Auther      : Fox
Desc        :
 **/
class ElectricSignActivity : BaseVBActivity<MainActivityElectricSignBinding, IElectricSignPresenter>(), IElectricSignPresenter.View {

    override fun createPresenter(): IElectricSignPresenter {
        return ElectricSignPresenterImpl(this, this);
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
        mToolBarDelegate?.setMidTitle("电子签约")
        initWebView();
        mPresenter?.getElectricSign();
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
                if(url.indexOf("signSuccess") > -1) {
                    finish();
                }
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

    override fun getSignUrlFailed() {
        showToast("获取签约信息失败，请重试")
        finish();
    }
}