package com.lingmiao.shop.business.main.presenter
import com.james.common.base.BasePresenter
import com.james.common.base.BaseView
import com.lingmiao.shop.business.main.bean.ApplyShopHint

interface ApplyShopHintPresenter: BasePresenter{
 
	fun requestApplyShopHintData()
	
	interface View : BaseView {
        fun onApplyShopHintSuccess(bean: ApplyShopHint)
        fun onApplyShopHintError(code: Int)
    }
}