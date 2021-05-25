package com.lingmiao.shop.business.main.presenter
import com.james.common.base.BasePresenter
import com.james.common.base.BaseView
import com.lingmiao.shop.business.goods.api.bean.CategoryVO

interface ApplyShopCategoryPresenter: BasePresenter{
 
	fun requestApplyShopCategoryData()
	
	interface View : BaseView {
        fun onApplyShopCategorySuccess(list: List<CategoryVO>)
        fun onApplyShopCategoryError(code: Int)
    }
}