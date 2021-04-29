package com.lingmiao.shop.business.me.presenter

import com.james.common.base.BasePresenter
import com.james.common.base.BaseView
import com.lingmiao.shop.business.me.bean.IdentityVo
import com.lingmiao.shop.business.me.bean.VipType
import com.lingmiao.shop.business.wallet.presenter.MyWalletPresenter


interface ApplyVipPresenter : BasePresenter, MyWalletPresenter {

    fun getVipPriceList();
    fun getIdentity();

    interface View : BaseView, MyWalletPresenter.View {
        fun onSetVipPriceList(list : List<VipType>?);
        fun onSetVipInfo(item : IdentityVo?);
    }
}
