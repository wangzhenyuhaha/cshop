package com.lingmiao.shop.business.me.presenter
import com.james.common.base.BasePresenter
import com.james.common.base.BaseView
import com.lingmiao.shop.business.me.bean.ForgetPassword
import com.lingmiao.shop.business.me.bean.PersonInfoRequest

interface ForgetPasswordPresenter: BasePresenter{

    fun requestPersonInfoData(request: ForgetPassword)
    fun requestSmsCodeInfoData(phone:String)

	interface View : BaseView {
        fun onPersonInfoSuccess()
        fun onPersonInfoError()
        fun onSmsCodeInfoSuccess()
        fun onSmsCodeInfoError()
    }
}