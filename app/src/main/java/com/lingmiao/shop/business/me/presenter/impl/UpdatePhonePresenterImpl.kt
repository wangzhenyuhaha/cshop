package com.lingmiao.shop.business.me.presenter.impl

import android.content.Context
import com.blankj.utilcode.util.GsonUtils
import com.lingmiao.shop.business.login.bean.CaptchaAli
import com.lingmiao.shop.business.me.api.MeRepository
import com.lingmiao.shop.business.me.bean.PersonInfoRequest
import com.lingmiao.shop.business.me.presenter.UpdatePhonePresenter
import com.lingmiao.shop.util.OtherUtils
import com.james.common.base.BasePreImpl
import kotlinx.coroutines.launch

class UpdatePhonePresenterImpl(context: Context, private var view: UpdatePhonePresenter.View) :
    BasePreImpl(view), UpdatePhonePresenter {


    override fun requestPersonInfoData(request: PersonInfoRequest) {
        mCoroutine.launch {
            try {
                val map = OtherUtils.tranPersonBeanToMap(request)
                val resp = MeRepository.apiService.updatePersonInfo(request.id ?: "", map)
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

    override fun checkSmsCodeData(phone: String, code: String) {
        mCoroutine.launch {
            try {
                val resp = MeRepository.apiService.checkSmsCode(phone, code)
                if (resp.isSuccessful) {
                    view.onCheckSmsCodeSuccess()
                } else {
                    view.onCheckSmsCodeError()
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }


        }
    }
}