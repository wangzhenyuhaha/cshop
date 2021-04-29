package com.lingmiao.shop.business.me.presenter

import com.lingmiao.shop.business.me.bean.My
import com.james.common.base.BasePresenter
import com.james.common.base.BaseView
import com.lingmiao.shop.business.me.bean.IdentityVo
import com.lingmiao.shop.business.wallet.presenter.MyWalletPresenter


interface MyPresenter : BasePresenter, MyWalletPresenter {
    fun getMyData()
    fun getIdentity();

    interface View : BaseView, MyWalletPresenter.View {
        fun onSetVipInfo(item : IdentityVo?);
        fun onMyDataSuccess(bean: My)
        fun ontMyDataError()
    }
}
