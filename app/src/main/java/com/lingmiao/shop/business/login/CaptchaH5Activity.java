package com.lingmiao.shop.business.login;

import android.os.Bundle;
import android.text.TextUtils;
import android.webkit.JavascriptInterface;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import androidx.appcompat.app.AppCompatActivity;

import com.blankj.utilcode.util.GsonUtils;
import com.lingmiao.shop.R;
import com.lingmiao.shop.business.login.bean.CaptchaAli;
import com.google.gson.reflect.TypeToken;

import org.greenrobot.eventbus.EventBus;

public class CaptchaH5Activity extends AppCompatActivity {
     private WebView testWebview;
     @Override
     protected void onCreate(Bundle savedInstanceState) {
         super.onCreate(savedInstanceState);
         setContentView(R.layout.activity_captcha);
         initView();
     }
     private void initView() {
         // 页面布局。
         testWebview = (WebView) findViewById(R.id.webview);
         // 设置屏幕自适应。
         testWebview.getSettings().setUseWideViewPort(true);
         testWebview.getSettings().setLoadWithOverviewMode(true);
         // 建议禁止缓存加载，以确保在攻击发生时可快速获取最新的滑动验证组件进行对抗。
         testWebview.getSettings().setCacheMode(WebSettings.LOAD_NO_CACHE);
         // 设置不使用默认浏览器，而直接使用WebView组件加载页面。
         testWebview.setWebViewClient(new WebViewClient(){
             @Override
             public boolean shouldOverrideUrlLoading(WebView view, String url) {
                 view.loadUrl(url);
                 return true;
             }
         });
         // 设置WebView组件支持加载JavaScript。
         testWebview.getSettings().setJavaScriptEnabled(true);
         // 建立JavaScript调用Java接口的桥梁。
         testWebview.addJavascriptInterface(this, "android");
         // 加载业务页面。
//         testWebview.loadUrl("file:////android_asset/captcha.html");
         testWebview.loadUrl("http://192.168.0.6:12345/captcha.html");


     }

    @JavascriptInterface
    public void getSlideData(String callData) {
        System.out.println(callData);
        testWebview.postDelayed(new Runnable() {
            @Override
            public void run() {
                finish();
                if(!TextUtils.isEmpty(callData)){
                    CaptchaAli bean = GsonUtils.fromJson(callData, new TypeToken<CaptchaAli>() {
                    }.getType());
                    bean.setSession_id(bean.getSessionid());
                    EventBus.getDefault().post(bean);
                }else{

                }
            }
        },1000);


    }

 }

