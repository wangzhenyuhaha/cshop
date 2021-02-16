package com.lingmiao.shop.business.login.presenter

import com.lingmiao.shop.business.login.bean.CaptchaAli
import com.lingmiao.shop.business.login.bean.LoginInfo
import com.lingmiao.shop.business.login.bean.LoginRequest
import com.lingmiao.shop.business.me.bean.AccountSetting
import com.james.common.base.BasePresenter
import com.james.common.base.BaseView
import com.james.common.net.BaseResponse

interface LoginPresenter : BasePresenter {
    fun getLoginSmsCode(bean: CaptchaAli)
    fun login(loginRequest: LoginRequest)

    fun getRegisterSmsCode(bean: CaptchaAli)
    fun register(phone: String,password:String,code:String,username:String)

    fun requestAccountSettingData()

    interface View : BaseView {
        fun onLoginSmsCodeSuccess()
        fun onLoginSmsCodeError(body: BaseResponse?)

        fun onLoginSuccess(body: LoginInfo?)
        fun onLoginError()

        fun onRegisterSmsCodeSuccess()
        fun onRegisterSmsCodeError()

        fun onRegisterSuccess(body: LoginInfo?)
        fun onRegisterError()

        fun onAccountSettingSuccess(bean: AccountSetting)
        fun onAccountSettingError(code: Int)
    }
}