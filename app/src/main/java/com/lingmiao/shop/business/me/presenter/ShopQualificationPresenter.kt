package com.lingmiao.shop.business.me.presenter
import com.james.common.base.BasePresenter
import com.james.common.base.BaseView
import com.lingmiao.shop.business.me.bean.ShopQualification

interface ShopQualificationPresenter: BasePresenter{
 
	fun requestShopQualificationData()
	
	interface View : BaseView {
        fun onShopQualificationSuccess(bean: ShopQualification)
        fun onShopQualificationError(code: Int)
    }
}