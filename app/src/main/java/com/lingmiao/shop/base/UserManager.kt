package com.lingmiao.shop.base

import android.text.TextUtils
import cn.jpush.android.api.JPushInterface
import com.blankj.utilcode.util.GsonUtils
import com.blankj.utilcode.util.SPUtils
import com.blankj.utilcode.util.Utils
import com.lingmiao.shop.business.login.bean.LoginInfo
import com.google.gson.Gson
import com.lingmiao.shop.business.main.bean.ApplyShopInfo
import com.lingmiao.shop.business.main.bean.BindBankCardDTO

class UserManager {
    companion object {
        private var hasLogin = false
        private var isAutoPrint = false
        private var loginInfo: LoginInfo? = null
        private var applyShopInfo: ApplyShopInfo? = null

        //保存的对公账户信息
        private var companyAccount: BindBankCardDTO? = null

        //存的对私账户信息
        private var personalAccount: BindBankCardDTO? = null


        fun getLoginInfo(): LoginInfo? {
            val loginInfoString: String? = SPUtils.getInstance().getString("login", null)
            if (!TextUtils.isEmpty(loginInfoString)) {
                loginInfo = Gson().fromJson(loginInfoString, LoginInfo::class.java)
            }
            return loginInfo
        }


        //获取推广码
        fun getPromCode(): String {
            return SPUtils.getInstance().getString("promCode", "")
        }

        //保存推广码
        fun setPromCode(promCode: String) {
            SPUtils.getInstance().put("promCode", promCode)
        }

        //获取ApplyShopInfo数据
        fun getApplyShopInfo(): ApplyShopInfo? {
            val applyShopInfoString: String? =
                SPUtils.getInstance().getString("applyShopInfo", null)
            if (!TextUtils.isEmpty(applyShopInfoString)) {
                applyShopInfo = Gson().fromJson(applyShopInfoString, ApplyShopInfo::class.java)
            }
            return applyShopInfo
        }

        //保存ApplyShopInfo数据
        fun setApplyShopInfo(applyShopInfo: ApplyShopInfo) {
            SPUtils.getInstance().put("applyShopInfo", GsonUtils.toJson(applyShopInfo))
        }

        //发出第一次申请后删除保存在本地的ApplyShopInfo，改用从云端获取ApplyShopInfo
        fun applyShopInfoOut() {
            applyShopInfo = null
            SPUtils.getInstance().put("applyShopInfo", "")
        }

        //保存对公账户信息
        fun setCompanyAccount(account: BindBankCardDTO) {
            SPUtils.getInstance().put("companyAccount", GsonUtils.toJson(account))
        }

        //获取对公账户信息
        fun getCompanyAccount(): BindBankCardDTO? {
            val companyAccountString: String? =
                SPUtils.getInstance().getString("companyAccount", null)
            if (!TextUtils.isEmpty(companyAccountString)) {
                companyAccount =
                    Gson().fromJson(companyAccountString, BindBankCardDTO::class.java)
            }
            return companyAccount
        }

        //删除对公账户信息
        fun companyAccountOut() {
            companyAccount = null
            SPUtils.getInstance().put("companyAccount", "")
        }

        //保存对私账户信息
        fun setPersonalAccount(account: BindBankCardDTO) {
            SPUtils.getInstance().put("personalAccount", GsonUtils.toJson(account))
        }

        //获取对私账户信息
        fun getPersonalAccount(): BindBankCardDTO? {
            val personalAccountString: String? =
                SPUtils.getInstance().getString("personalAccount", null)
            if (!TextUtils.isEmpty(personalAccountString)) {
                personalAccount =
                    Gson().fromJson(personalAccountString, BindBankCardDTO::class.java)
            }
            return personalAccount
        }

        //删除对私账户信息
        fun personalAccountOut() {
            personalAccount = null
            SPUtils.getInstance().put("personalAccount", "")
        }


        fun isLogin(): Boolean {
            return SPUtils.getInstance().getBoolean("isLogin", false)
        }

        fun setLogin(isLogin: Boolean) {
            hasLogin = isLogin
            SPUtils.getInstance().put("isLogin", isLogin)
        }

        fun setLoginInfo(info: LoginInfo) {
            SPUtils.getInstance().put("login", GsonUtils.toJson(info))
            SPUtils.getInstance().put("isLogin", true)
        }

        fun setLoginInfo(info: String) {
            SPUtils.getInstance().put("login", info)
            SPUtils.getInstance().put("isLogin", true)
        }

        fun isAutoPrint(): Boolean {
            return SPUtils.getInstance().getBoolean("isAutoPrint", false)
        }

        fun setAutoPrint(isAutoPrint: Boolean) {
            this.isAutoPrint = isAutoPrint
            SPUtils.getInstance().put("isAutoPrint", isAutoPrint)
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