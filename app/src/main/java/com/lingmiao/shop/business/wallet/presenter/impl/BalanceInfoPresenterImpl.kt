package com.lingmiao.shop.business.wallet.presenter.impl

import com.blankj.utilcode.util.ActivityUtils
import com.blankj.utilcode.util.SPUtils
import com.lingmiao.shop.MyApp
import com.lingmiao.shop.R
import com.lingmiao.shop.business.wallet.WithdrawActivity
import com.lingmiao.shop.business.wallet.api.WalletConstants
import com.lingmiao.shop.business.wallet.api.WalletRepository
import com.lingmiao.shop.business.wallet.presenter.WalletInfoPresenter
import com.james.common.base.BasePreImpl
import kotlinx.coroutines.launch

class BalanceInfoPresenterImpl(private var view: WalletInfoPresenter.View) : BasePreImpl(view),
    WalletInfoPresenter {

    override fun loadInfo() {
        mCoroutine.launch {

            val resp = WalletRepository.getBalanceInfo();

            getRate();
            handleResponse(resp) {
                if (resp?.isSuccess()) {
                    SPUtils.getInstance().put(WalletConstants.BALANCE_ACCOUNT_ID, resp?.data?.data?.getBalanceAccountId());
                    view.loadInfoSuccess(resp?.data?.data?.balanceAccount);
                } else {
                    view.loadInfoError(resp?.code);
                }
            }
        }
    }

    override fun getTypeHint(): String {
        return MyApp.getInstance().getString(R.string.wallet_deposit_balance)
    }

    override fun getOptionHint(): String {
        return MyApp.getInstance().getString(R.string.wallet_balance_opt_fill);
    }

    override fun getDetailHint(): String {
        return MyApp.getInstance().getString(R.string.wallet_balance_opt_detail);
    }

    override fun getTitleHint(): String {
        return MyApp.getInstance().getString(R.string.wallet_title_balance);
    }

    override fun onOptionClicked() {
        ActivityUtils.startActivity(WithdrawActivity::class.java);
    }

    override fun getRate() {
        mCoroutine.launch {
            val resp = WalletRepository.queryServiceChargeRate();
            handleResponse(resp) {
                view.setRate(resp.data);
            }
        }
    }

}