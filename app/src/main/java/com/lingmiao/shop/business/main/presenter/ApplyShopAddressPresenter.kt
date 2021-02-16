package com.lingmiao.shop.business.main.presenter
import com.james.common.base.BasePresenter
import com.james.common.base.BaseView
import com.lingmiao.shop.business.main.bean.ApplyShopAddress

interface ApplyShopAddressPresenter: BasePresenter{
 
	fun requestApplyShopAddressData()
	
	interface View : BaseView {
        fun onApplyShopAddressSuccess(bean: ApplyShopAddress)
        fun onApplyShopAddressError(code: Int)
    }
}