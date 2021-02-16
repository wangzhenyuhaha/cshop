package com.lingmiao.shop.business.login.presenter.impl

import com.blankj.utilcode.util.DeviceUtils
import com.blankj.utilcode.util.LogUtils
import com.lingmiao.shop.BuildConfig
import com.lingmiao.shop.base.UserManager
import com.lingmiao.shop.business.login.api.LoginRepository
import com.lingmiao.shop.business.login.bean.CaptchaAli
import com.lingmiao.shop.business.login.bean.LoginInfo
import com.lingmiao.shop.business.login.bean.LoginRequest
import com.lingmiao.shop.business.login.presenter.LoginPresenter
import com.lingmiao.shop.business.main.api.MainRepository
import com.james.common.base.BasePreImpl
import com.james.common.net.BaseResponse
import com.james.common.netcore.networking.http.core.awaitHiResponse
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LoginPresenterImpl(private var view: LoginPresenter.View) :
    BasePreImpl(view), LoginPresenter {
    override fun getLoginSmsCode(bean: CaptchaAli) {
        LoginRepository.apiService.getLoginSmsCode(bean).enqueue(object : Callback<BaseResponse> {
            override fun onFailure(call: Call<BaseResponse>, t: Throwable) {
                view.hideDialogLoading()
            }

            override fun onResponse(call: Call<BaseResponse>, response: Response<BaseResponse>) {
                view.hideDialogLoading()
                if (response.isSuccessful) {
                    view.onLoginSmsCodeSuccess()
                } else {
                    view.onLoginSmsCodeError(response.body())
                }
            }

        })
    }

    override fun login(loginRequest: LoginRequest) {
        loginRequest.uuid = DeviceUtils.getAndroidID()
        LoginRepository.apiService.login(loginRequest).enqueue(object : Callback<LoginInfo> {
            override fun onFailure(call: Call<LoginInfo>, t: Throwable) {
                view.hideDialogLoading()
            }

            override fun onResponse(call: Call<LoginInfo>, response: Response<LoginInfo>) {
                view.hideDialogLoading()
                if (response.isSuccessful) {
                    if (response.body() != null) {
                        view.onLoginSuccess(response.body())
                    }

                }
            }

        })
    }

    override fun getRegisterSmsCode(bean: CaptchaAli) {
        mCoroutine.launch {
            val resp = LoginRepository.apiService.getRegisterSmsCode(bean).awaitHiResponse()
            if (resp.isSuccess) {
                view.onRegisterSmsCodeSuccess()
            }else{
                view.onRegisterSmsCodeError()
            }

        }
    }

    override fun register(phone: String, password: String, code: String, username: String) {
        mCoroutine.launch {
            val resp = LoginRepository.apiService.register(phone,password,code).awaitHiResponse()
            if (resp.isSuccess) {
                view.onRegisterSuccess(resp.data)
            }else{
                view.onRegisterError()
            }

        }
    }

    override fun requestAccountSettingData() {
        mCoroutine.launch {
            val resp = MainRepository.apiService.getUpgrade(
                BuildConfig.VERSION_NAME,
                BuildConfig.VERSION_CODE.toString()
            ).awaitHiResponse()
            if (resp.isSuccess) {
                view.onAccountSettingSuccess(resp.data)
            } else {
                view.onAccountSettingError(resp.code)
            }

        }
    }

}