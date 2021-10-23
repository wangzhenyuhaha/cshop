package com.lingmiao.shop.business.me.presenter

import com.james.common.base.BasePresenter
import com.james.common.base.BaseView
import com.lingmiao.shop.business.goods.api.bean.WxPayReqVo
import com.lingmiao.shop.business.me.bean.IdentityVo
import com.lingmiao.shop.business.me.bean.VipType
import com.lingmiao.shop.business.me.presenter.impl.ApplyVipPreImpl
import com.lingmiao.shop.business.wallet.bean.WalletVo
import com.lingmiao.shop.business.wallet.presenter.MyWalletPresenter


interface ApplyVipPresenter : BasePresenter, MyWalletPresenter {

    fun getVipPriceList();
    fun getIdentity();
    fun apply(id: String);
    fun depositApply(mWallet: WalletVo?);
    fun ensureRefund(id: String);
    fun getApplyVipReason()
    interface View : BaseView, MyWalletPresenter.View {
        fun onSetVipPriceList(list: List<VipType>?);
        fun onSetVipInfo(item: IdentityVo?);
        fun onApplySuccess(value: WxPayReqVo?);
        fun onDepositApplied(value: WxPayReqVo?);
        fun onRefundEnsured();
        fun onRefundEnsureFail();
        fun onVipReason(data: ApplyVipPreImpl.ApplyVipReason)
    }
}
