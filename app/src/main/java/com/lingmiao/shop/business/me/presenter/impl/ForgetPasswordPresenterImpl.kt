package com.lingmiao.shop.business.me.presenter.impl
import com.james.common.base.BasePresenter
import com.james.common.base.BaseView
import com.lingmiao.shop.business.me.bean.ForgetPassword

import android.content.Context
import com.blankj.utilcode.util.GsonUtils
import com.lingmiao.shop.business.login.api.LoginRepository
import com.lingmiao.shop.business.login.bean.CaptchaAli
import  com.lingmiao.shop.business.me.presenter.ForgetPasswordPresenter
import com.james.common.base.BasePreImpl
import com.lingmiao.shop.business.me.api.MeRepository
import com.lingmiao.shop.business.me.bean.PersonInfoRequest
import com.lingmiao.shop.util.OtherUtils
import com.james.common.netcore.networking.http.core.awaitHiResponse
import kotlinx.coroutines.launch

class ForgetPasswordPresenterImpl(context: Context, private var view: ForgetPasswordPresenter.View) :
    BasePreImpl(view), ForgetPasswordPresenter{
	override fun requestPersonInfoData(request: ForgetPassword) {
		mCoroutine.launch {
			try {
				val resp = LoginRepository.apiService.forgetPassword(request)
				if (resp.isSuccessful) {
					view.onPersonInfoSuccess()
				} else {
					view.onPersonInfoError()
				}
			} catch (e: Exception) {
				e.printStackTrace()
			}


		}
	}

	override fun requestSmsCodeInfoData(phone: String) {
		mCoroutine.launch {
			try {
				val resp = MeRepository.apiService.getSmsCode(phone, CaptchaAli(mobile = phone))
				if (resp.isSuccessful) {
					view.onSmsCodeInfoSuccess()
				} else {
					view.onSmsCodeInfoError()
				}
			} catch (e: Exception) {
				e.printStackTrace()
			}


		}
	}
}