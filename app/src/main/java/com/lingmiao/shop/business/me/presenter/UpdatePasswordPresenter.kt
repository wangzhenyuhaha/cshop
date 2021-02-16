package com.lingmiao.shop.business.me.presenter
import com.lingmiao.shop.business.me.bean.PersonInfoRequest
import com.james.common.base.BasePresenter
import com.james.common.base.BaseView
import com.lingmiao.shop.business.me.bean.UpdatePassword

interface UpdatePasswordPresenter: BasePresenter{

    fun requestPersonInfoData(request: PersonInfoRequest)


    interface View : BaseView {
        fun onPersonInfoSuccess()
        fun onPersonInfoError()


    }
}