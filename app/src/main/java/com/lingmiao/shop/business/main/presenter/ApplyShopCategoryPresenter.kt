package com.lingmiao.shop.business.main.presenter
import com.james.common.base.BasePresenter
import com.james.common.base.BaseView
import com.lingmiao.shop.business.main.bean.ApplyShopCategory

interface ApplyShopCategoryPresenter: BasePresenter{
 
	fun requestApplyShopCategoryData()
	
	interface View : BaseView {
        fun onApplyShopCategorySuccess(list: List<ApplyShopCategory>)
        fun onApplyShopCategoryError(code: Int)
    }
}