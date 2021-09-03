package com.lingmiao.shop.business.main

import android.annotation.SuppressLint
import android.content.Intent
import android.view.View
import android.webkit.WebSettings
import android.webkit.WebView
import android.webkit.WebViewClient
import com.blankj.utilcode.util.ActivityUtils
import com.james.common.base.BaseVBActivity
import com.lingmiao.shop.business.main.presenter.IElectricSignPresenter
import com.lingmiao.shop.business.main.presenter.impl.ElectricSignPresenterImpl
import com.lingmiao.shop.business.me.ShopWeChatApproveActivity
import com.lingmiao.shop.databinding.MainActivityElectricSignBinding
import com.lingmiao.shop.widget.PaxWebChromeClient

/**
Create Date : 2021/8/144:50 下午
Auther      : Fox
Desc        :
 **/
class ElectricSignActivity : BaseVBActivity<MainActivityElectricSignBinding, IElectricSignPresenter>(), IElectricSignPresenter.View {

    lateinit var chromeClient : PaxWebChromeClient;

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
                if(url.startsWith("http://www.c-dian.cn")
                    || url.startsWith("http://www.c-dian.cn/")
                    || url.indexOf("/signSuccess?signType=allinpay") > -1) {
                    mPresenter?.getShopStatus();
                    return false;
                }
                view.loadUrl(url)
                return true
            }
        }
        mBinding.wvView.settings.javaScriptEnabled = true
        mBinding.wvView.addJavascriptInterface(this, "android")
        //辅助WebView处理图片上传操作
        chromeClient = PaxWebChromeClient(this, mToolBarDelegate?.getMidTitle());
        mBinding.wvView.setWebChromeClient(chromeClient);
    }

    override fun setUrl(url: String?) {
        mBinding.wvView.loadUrl(url)
    }

    override fun getSignUrlFailed() {
        showToast("获取签约信息失败，请重试")
        finish();
    }

    override fun onSignSuccess() {
        showToast("签约成功，请进行商户认证")
        ActivityUtils.startActivity(ShopWeChatApproveActivity::class.java);
        finish();
    }

    override fun onSigning() {
        showToast("签约已提交，请稍后刷新...")
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

}