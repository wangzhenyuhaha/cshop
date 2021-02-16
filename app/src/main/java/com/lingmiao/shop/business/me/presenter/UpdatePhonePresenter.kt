package com.lingmiao.shop.business.me.presenter
import com.lingmiao.shop.business.me.bean.PersonInfoRequest
import com.james.common.base.BasePresenter
import com.james.common.base.BaseView
import com.lingmiao.shop.business.me.bean.UpdatePhone

interface UpdatePhonePresenter: BasePresenter{
    fun requestPersonInfoData(request: PersonInfoRequest)
    fun requestSmsCodeInfoData(phone:String)
    fun checkSmsCodeData(phone:String,code:String)


    interface View : BaseView {
        fun onPersonInfoSuccess()
        fun onPersonInfoError()
        fun onSmsCodeInfoSuccess()
        fun onSmsCodeInfoError()

        fun onCheckSmsCodeSuccess()
        fun onCheckSmsCodeError()

    }
}