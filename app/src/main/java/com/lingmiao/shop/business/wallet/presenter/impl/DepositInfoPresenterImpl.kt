package com.lingmiao.shop.business.wallet.presenter.impl

import com.blankj.utilcode.util.ActivityUtils
import com.blankj.utilcode.util.SPUtils
import com.lingmiao.shop.MyApp
import com.lingmiao.shop.R
import com.lingmiao.shop.business.wallet.ChargeDepositActivity
import com.lingmiao.shop.business.wallet.api.WalletConstants
import com.lingmiao.shop.business.wallet.api.WalletRepository
import com.lingmiao.shop.business.wallet.presenter.WalletInfoPresenter
import com.james.common.base.BasePreImpl
import kotlinx.coroutines.launch

class DepositInfoPresenterImpl (private var view : WalletInfoPresenter.View) : BasePreImpl(view),
    WalletInfoPresenter  {
    override fun loadInfo() {
        mCoroutine.launch {

            val resp = WalletRepository.getDepositInfo();

            handleResponse(resp) {
                if (resp?.isSuccess()) {
                    SPUtils.getInstance().put(WalletConstants.DEPOSIT_ACCOUNT_ID, resp?.data?.data?.getDepositAccountId());
                    view.loadInfoSuccess(resp?.data?.data?.depositAccount);
                } else {
                    view.loadInfoError(resp?.code);
                }
            }
        }
    }

    override fun getTypeHint(): String {
        return MyApp.getInstance().getString(R.string.wallet_deposit_hint);
    }

    override fun getOptionHint(): String {
        return MyApp.getInstance().getString(R.string.wallet_deposit_opt_fill);
    }

    override fun getDetailHint(): String {
        return MyApp.getInstance().getString(R.string.wallet_deposit_opt_detail);
    }

    override fun getTitleHint(): String {
        return MyApp.getInstance().getString(R.string.wallet_title_deposit);
    }

    override fun onOptionClicked() {
        ActivityUtils.startActivity(ChargeDepositActivity::class.java);
    }

    override fun getRate() {

    }

}