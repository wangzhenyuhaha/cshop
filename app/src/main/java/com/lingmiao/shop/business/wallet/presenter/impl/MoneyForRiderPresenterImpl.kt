package com.lingmiao.shop.business.wallet.presenter.impl

import android.content.Context
import android.util.Log
import com.blankj.utilcode.util.SPUtils
import com.james.common.base.BasePreImpl
import com.james.common.netcore.networking.http.core.awaitHiResponse
import com.lingmiao.shop.business.main.api.MainRepository
import com.lingmiao.shop.business.me.bean.RechargeReqVo
import com.lingmiao.shop.business.wallet.api.WalletConstants
import com.lingmiao.shop.business.wallet.api.WalletRepository
import com.lingmiao.shop.business.wallet.bean.AccountVo
import com.lingmiao.shop.business.wallet.bean.WalletVo
import com.lingmiao.shop.business.wallet.presenter.MoneyForRiderPresenter
import kotlinx.coroutines.launch

class MoneyForRiderPresenterImpl(val context: Context, val view: MoneyForRiderPresenter.View) :
    BasePreImpl(view), MoneyForRiderPresenter {


    override fun loadInfo() {

        mCoroutine.launch {
            val resp = WalletRepository.getRiderMoney()

            handleResponse(resp) {
                if (resp.isSuccess) {
                    SPUtils.getInstance()
                        .put(WalletConstants.RIDER_ACCOUNT_ID, resp.data?.data?.getRiderAccountId())
                    view.loadInfoSuccess(resp.data?.data?.riderDepositAccountVO)
                } else {
                    view.loadInfoError(resp.code)
                }
            }
        }
    }

    override fun riderMoneyApply(mWallet: AccountVo?, value: Double) {
        if (mWallet == null) {
            return
        }
        mCoroutine.launch {
            view.showDialogLoading()
            val recharge = RechargeReqVo(
                mWallet.id,
                value,
                RechargeReqVo.PAY_TRADE_CHANNEL_OF_WECHAT
            )
            val resp = MainRepository.apiService.recharge(recharge).awaitHiResponse()
            if (resp.isSuccess) {
                //充值成功
                view.onApplied(resp.data?.data)
            } else {
                //充值失败
                view.showToast("支付失败")
            }
            view.hideDialogLoading()
        }
    }

    override fun riderMoneyWithdrawal(value: Double) {
        mCoroutine.launch {
            view.showDialogLoading()
            val resp = MainRepository.apiService.riderWithdraw(value).awaitHiResponse()
            if (resp.isSuccess) {
                view.onWithdrawSuccess()
            }
            view.hideDialogLoading()
        }
    }

}