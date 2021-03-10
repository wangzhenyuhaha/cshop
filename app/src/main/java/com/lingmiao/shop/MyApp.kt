package com.lingmiao.shop


import android.os.Build
import android.webkit.WebView
import androidx.core.content.ContextCompat
import cn.jpush.android.api.JPushInterface
import com.blankj.utilcode.util.*
import com.lingmiao.shop.base.UserManager
import com.lingmiao.shop.net.Fetch
import com.james.common.BaseApplication
import com.james.common.R
import com.tencent.bugly.crashreport.CrashReport


class MyApp : BaseApplication() {

    companion object {
        private lateinit var application: MyApp


        fun getInstance(): MyApp {
            return application
        }
    }

    override fun onCreate() {
        super.onCreate()
        application = applicationContext as MyApp

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            val processName = ProcessUtils.getCurrentProcessName()
            if (BuildConfig.APPLICATION_ID != processName) {
                WebView.setDataDirectorySuffix(processName)
            }
        }

        if(ProcessUtils.isMainProcess()){
            // bugly
            CrashReport.initCrashReport(applicationContext, "3e394c9cea", BuildConfig.DEBUG)

            JPushInterface.setDebugMode(BuildConfig.DEBUG)
            JPushInterface.init(this)
            JPushInterface.stopCrashHandler(application)

            initUtils()
            // 初始化网络库
            Fetch.initNetConfig()
        }



    }

    private fun initUtils() {
        // 初始化 Util工具类,避免反射获取 Application。
        Utils.init(this)

        // 初始化Toast属性
        if(RomUtils.isXiaomi()){//修复在小米部分机型上toast高度异常问题
            ToastUtils.setBgColor(ContextCompat.getColor(this, R.color.color_CC000000))
        }else{
            ToastUtils.setBgResource(R.drawable.shape_toast_bg)
        }

        ToastUtils.setMsgColor(ContextCompat.getColor(this, R.color.common_ffffff))
    }

    override fun loginOutByToken() {
        UserManager.loginOut()
    }

}