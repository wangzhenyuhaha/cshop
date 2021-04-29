package com.lingmiao.shop.business.wallet

import com.blankj.utilcode.util.ActivityUtils
import com.blankj.utilcode.util.SPUtils
import com.lingmiao.shop.R
import com.lingmiao.shop.business.wallet.api.WalletConstants
import com.lingmiao.shop.business.wallet.bean.AccountVo
import com.lingmiao.shop.business.wallet.bean.WalletVo
import com.lingmiao.shop.business.wallet.presenter.MyWalletPresenter
import com.lingmiao.shop.business.wallet.presenter.impl.MyWalletPresenterImpl
import com.lingmiao.shop.util.formatDouble
import com.james.common.base.BaseActivity
import kotlinx.android.synthetic.main.wallet_activity_my_wallet.*

/**
 * 我的钱包
 */
class MyWalletActivity : BaseActivity<MyWalletPresenter>(), MyWalletPresenter.View {

    override fun createPresenter(): MyWalletPresenter {
        return MyWalletPresenterImpl(this)
    }

    override fun getLayoutId(): Int {
        return R.layout.wallet_activity_my_wallet;
    }

    override fun useLightMode(): Boolean {
        return false;
    }

    override fun initView() {
        mToolBarDelegate.setMidTitle("我的钱包");

        initListener();

        mPresenter.onCreate();
        mPresenter.loadWalletData();
    }

    private fun initListener() {
        ll_wallet_balance.setOnClickListener {
            WalletInfoActivity.openBalanceActivity(this);
        };

        ll_wallet_deposit.setOnClickListener {
            WalletInfoActivity.openDepositActivity(this);
        };

        ll_wallet_third_part.setOnClickListener {
            AliPayAccountActivity.ali(this);
        };
        ll_wallet_wechat_part.setOnClickListener {
            AliPayAccountActivity.wechat(this);
        }
        ll_wallet_card.setOnClickListener {
            ActivityUtils.startActivity(BankCardListActivity::class.java);
        };
    }

    override fun onLoadWalletDataSuccess(vo: WalletVo?) {
        SPUtils.getInstance().put(WalletConstants.BALANCE_ACCOUNT_ID, vo?.getBalanceAccountId());
        SPUtils.getInstance().put(WalletConstants.DEPOSIT_ACCOUNT_ID, vo?.getDepositAccountId());


        val balanceVo: AccountVo? = vo?.balanceAccount;
        val depositVo: AccountVo? = vo?.depositAccount;

        val bAmount: Double? = balanceVo?.balanceAmount;
        val dAmount: Double? = depositVo?.balanceAmount;

        tv_wallet_balance.setText(getString(R.string.yuan_amount, formatDouble(bAmount)));
        tv_wallet_deposit.setText(getString(R.string.yuan_amount, formatDouble(dAmount)));

    }

    override fun onLoadWalletDataError(code: Int) {

    }

}