package com.lingmiao.shop.base

import android.text.TextUtils
import cn.jpush.android.api.JPushInterface
import com.blankj.utilcode.util.GsonUtils
import com.blankj.utilcode.util.SPUtils
import com.blankj.utilcode.util.Utils
import com.lingmiao.shop.business.login.bean.LoginInfo
import com.google.gson.Gson

class UserManager {
    companion object {
        private var hasLogin = false
        private var loginInfo: LoginInfo? = null



        fun getLoginInfo(): LoginInfo? {
            val loginInfoString: String? = SPUtils.getInstance().getString("login", null)
            if (!TextUtils.isEmpty(loginInfoString)) {
                loginInfo = Gson().fromJson(loginInfoString, LoginInfo::class.java)
            }
            return loginInfo
        }

        fun isLogin(): Boolean {
            return SPUtils.getInstance().getBoolean("isLogin", false)
        }

        fun setLogin(isLogin: Boolean) {
            hasLogin = isLogin
            SPUtils.getInstance().put("isLogin", isLogin)
        }

        fun setLoginInfo(info: LoginInfo) {
            SPUtils.getInstance().put("login",GsonUtils.toJson(info))
            SPUtils.getInstance().put("isLogin", true)
        }

        fun setLoginInfo(info: String) {
            SPUtils.getInstance().put("login",info)
            SPUtils.getInstance().put("isLogin", true)
        }


        fun loginOut() {
            hasLogin = false
            loginInfo = null
            SPUtils.getInstance().put("login", "")
            SPUtils.getInstance().put("isLogin", false)
            JPushInterface.deleteAlias(Utils.getApp(), 1)
        }


    }
}