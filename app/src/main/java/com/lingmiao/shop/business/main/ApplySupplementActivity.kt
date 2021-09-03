package com.lingmiao.shop.business.main

import android.annotation.SuppressLint
import android.content.Intent
import android.view.KeyEvent
import android.webkit.*
import com.james.common.base.BaseVBActivity
import com.lingmiao.shop.business.main.presenter.IApplySupplementPresenter
import com.lingmiao.shop.business.main.presenter.impl.ApplySupplementPresenterImpl
import com.lingmiao.shop.databinding.MainActivityElectricSignBinding
import com.lingmiao.shop.widget.PaxWebChromeClient


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

    lateinit var chromeClient : PaxWebChromeClient;

    override fun initView() {
        mToolBarDelegate?.setMidTitle("资料补充")
        initWebView();
        mPresenter?.getSupplementSign();
    }

    @SuppressLint("JavascriptInterface")
    fun initWebView() {
        val settings = mBinding.wvView.settings
        settings.cacheMode = WebSettings.LOAD_NO_CACHE
        settings.useWideViewPort = true
        settings.loadWithOverviewMode = true
        settings.domStorageEnabled = true
        settings.defaultTextEncodingName = "UTF-8"
        settings.allowContentAccess = true // 是否可访问Content Provider的资源，默认值 true
        settings.allowFileAccess = true // 是否可访问本地文件，默认值 true
        // 是否允许通过file url加载的Javascript读取本地文件，默认值 false
        settings.allowFileAccessFromFileURLs = false
        // 是否允许通过file url加载的Javascript读取全部资源(包括文件,http,https)，默认值 false
        settings.allowUniversalAccessFromFileURLs = false
        //开启JavaScript支持
        settings.javaScriptEnabled = true
        // 支持缩放
        settings.setSupportZoom(true)
        mBinding.wvView.addJavascriptInterface(this, "android");
        //辅助WebView设置处理关于页面跳转，页面请求等操作
        mBinding.wvView.webViewClient = object : WebViewClient() {
            override fun shouldOverrideUrlLoading(view: WebView, url: String): Boolean {
                view.loadUrl(url)
                return true
            }
        }
        //辅助WebView处理图片上传操作
        chromeClient = PaxWebChromeClient(this, mToolBarDelegate?.getMidTitle());
        mBinding.wvView.setWebChromeClient(chromeClient);
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

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        chromeClient.onActivityResult(requestCode,resultCode,data)
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        chromeClient.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    override fun onKeyDown(keyCode: Int, event: KeyEvent?): Boolean {
        //如果按下的是回退键且历史记录里确实还有页面
        if (keyCode == KeyEvent.KEYCODE_BACK && mBinding.wvView.canGoBack()) {
            mBinding.wvView.goBack()
            return true
        }
        return super.onKeyDown(keyCode, event)
    }

}

