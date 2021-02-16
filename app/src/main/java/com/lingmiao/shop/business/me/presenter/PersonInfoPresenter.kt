package com.lingmiao.shop.business.me.presenter
import com.lingmiao.shop.business.me.bean.My
import com.james.common.base.BasePresenter
import com.james.common.base.BaseView
import com.lingmiao.shop.business.me.bean.PersonInfo
import com.lingmiao.shop.business.me.bean.PersonInfoRequest

interface PersonInfoPresenter: BasePresenter{
 
	fun requestPersonInfoData(request:PersonInfoRequest)
	
	interface View : BaseView {
        fun onPersonInfoSuccess()
        fun onPersonInfoError()
    }
}