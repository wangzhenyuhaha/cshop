package com.lingmiao.shop.business.me.presenter
import com.james.common.base.BasePresenter
import com.james.common.base.BaseView
import com.lingmiao.shop.business.me.bean.AccountSetting

interface AccountSettingPresenter: BasePresenter{
 
	fun requestAccountSettingData()

    interface View : BaseView {
        fun onAccountSettingSuccess(bean: AccountSetting)
        fun onAccountSettingError(code: Int)
    }
}