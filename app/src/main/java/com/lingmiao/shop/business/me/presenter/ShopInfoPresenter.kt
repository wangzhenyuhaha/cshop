package com.lingmiao.shop.business.me.presenter

import com.james.common.base.BasePresenter
import com.james.common.base.BaseView
import com.lingmiao.shop.business.main.bean.ApplyShopInfo

interface ShopInfoPresenter : BasePresenter {

    fun loadShopInfo()
    fun updateShopManage(bean:ApplyShopInfo)

    interface View : BaseView {
        fun onLoadedShopInfo(bean: ApplyShopInfo)
        fun onUpdateShopSuccess(bean: ApplyShopInfo)
        fun onUpdateShopError(code: Int)
    }

}