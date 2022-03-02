package com.lingmiao.shop.business.wallet

import android.view.View
import android.widget.TextView
import com.blankj.utilcode.util.ActivityUtils
import com.blankj.utilcode.util.SPUtils
import com.james.common.base.BaseActivity
import com.lingmiao.shop.R
import com.lingmiao.shop.base.UserManager
import com.lingmiao.shop.business.wallet.api.WalletConstants
import com.lingmiao.shop.business.wallet.bean.AccountVo
import com.lingmiao.shop.business.wallet.bean.WalletVo
import com.lingmiao.shop.business.wallet.bean.WithdrawAccountVo
import com.lingmiao.shop.business.wallet.presenter.MyWalletPresenter
import com.lingmiao.shop.business.wallet.presenter.impl.MyWalletPresenterImpl
import com.lingmiao.shop.util.formatDouble
import kotlinx.android.synthetic.main.wallet_activity_my_wallet.*

/**
 * 我的钱包
 */
class MyWalletActivity : BaseActivity<MyWalletPresenter>(), MyWalletPresenter.View {

    companion object {
        const val RESULT_WECHAT = 1001
    }

    override fun createPresenter(): MyWalletPresenter {
        return MyWalletPresenterImpl(this)
    }

    override fun getLayoutId(): Int {
        return R.layout.wallet_activity_my_wallet
    }

    override fun useLightMode(): Boolean {
        return false
    }

    override fun initView() {
        mToolBarDelegate.setMidTitle("我的钱包")

        initListener()

        UserManager.getLoginInfo()?.showButton?.also {
            if (it == 0) {
                findViewById<TextView>(R.id.accountBind).visibility = View.GONE
                findViewById<com.james.common.view.ILinearLayout>(R.id.accountBindWC).visibility =
                    View.GONE
            }
        }

        mPresenter.onCreate()

        mPresenter.loadWalletData()
    }

    private fun initListener() {
        ll_wallet_balance.setOnClickListener {
            WalletInfoActivity.openBalanceActivity(this)
        }

        ll_wallet_rider.setOnClickListener {
            MoneyForRiderActivity.openActivity(this)
        }

        ll_wallet_deposit.setOnClickListener {
            WalletInfoActivity.openDepositActivity(this)
        }

        ll_wallet_third_part.setOnClickListener {
            AliPayAccountActivity.ali(this)
        }
        ll_wallet_wechat_part.setOnClickListener {
            AliPayAccountActivity.wechat(this, mAccount?.wechatWithdrawAccount, RESULT_WECHAT)
        }
        ll_wallet_card.setOnClickListener {
            ActivityUtils.startActivity(BankCardListActivity::class.java)
        }
    }

    override fun onLoadWalletDataSuccess(data: WalletVo?) {
        SPUtils.getInstance().put(WalletConstants.BALANCE_ACCOUNT_ID, data?.getBalanceAccountId())
        SPUtils.getInstance().put(WalletConstants.DEPOSIT_ACCOUNT_ID, data?.getDepositAccountId())


        val balanceVo: AccountVo? = data?.balanceAccount
        val depositVo: AccountVo? = data?.depositAccount

        val bAmount: Double? = balanceVo?.balanceAmount
        val dAmount: Double? = depositVo?.balanceAmount

        tv_wallet_balance.text = getString(R.string.yuan_amount, formatDouble(bAmount))
        tv_wallet_deposit.text = getString(R.string.yuan_amount, formatDouble(dAmount))


    }

    var mAccount: WithdrawAccountVo? = null

    override fun onLoadedAccount(data: WithdrawAccountVo?) {
        mAccount = data
        if (data?.notExistWechatAccount() == false) {
            tv_wallet_wechat.text = "已设置"
        }
    }

    override fun onLoadWalletDataError(code: Int) {

    }

}