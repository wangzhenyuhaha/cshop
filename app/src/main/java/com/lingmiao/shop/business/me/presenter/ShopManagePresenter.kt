package com.lingmiao.shop.business.me.presenter
import com.james.common.base.BasePresenter
import com.james.common.base.BaseView
import com.lingmiao.shop.business.main.bean.ApplyShopInfo
import com.lingmiao.shop.business.me.bean.ShopManageRequest

interface ShopManagePresenter: BasePresenter{
 
	fun requestShopManageData()
	fun updateShopManage(bean:ApplyShopInfo)

	interface View : BaseView {
        fun onShopManageSuccess(bean: ApplyShopInfo)
        fun onShopManageError(code: Int)

        fun onUpdateShopSuccess(bean: ApplyShopInfo)
        fun onUpdateShopError(code: Int)
    }
}