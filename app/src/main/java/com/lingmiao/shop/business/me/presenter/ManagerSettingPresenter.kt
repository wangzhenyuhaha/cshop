package com.lingmiao.shop.business.me.presenter

import com.james.common.base.BasePresenter
import com.james.common.base.BaseView
import com.lingmiao.shop.business.main.bean.ApplyShopInfo

/**
Create Date : 2021/3/24:07 PM
Auther      : Fox
Desc        :
 **/
interface ManagerSettingPresenter : BasePresenter {

    fun loadShopInfo()

    interface View : BaseView {

        fun onLoadedShopInfo(bean: ApplyShopInfo)

    }
}