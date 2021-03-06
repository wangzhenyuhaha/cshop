package com.lingmiao.shop.business.wallet

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.View
import com.blankj.utilcode.util.FragmentUtils
import com.blankj.utilcode.util.ToastUtils
import com.james.common.base.BaseActivity
import com.james.common.utils.DialogUtils
import com.james.common.utils.exts.visiable
import com.lingmiao.shop.R
import com.lingmiao.shop.base.UserManager
import com.lingmiao.shop.business.wallet.api.WalletConstants
import com.lingmiao.shop.business.wallet.bean.AccountVo
import com.lingmiao.shop.business.wallet.bean.RateVo
import com.lingmiao.shop.business.wallet.fragment.BalanceDetailFragment
import com.lingmiao.shop.business.wallet.fragment.DepositDetailFragment
import com.lingmiao.shop.business.wallet.presenter.WalletInfoPresenter
import com.lingmiao.shop.business.wallet.presenter.impl.BalanceInfoPresenterImpl
import com.lingmiao.shop.business.wallet.presenter.impl.DepositInfoPresenterImpl
import kotlinx.android.synthetic.main.wallet_view_wallet_sum.*

/**
 * 余额/押金
 */
class WalletInfoActivity : BaseActivity<WalletInfoPresenter>(), WalletInfoPresenter.View {

    private var viewType: Int? = VALUE_BALANCE

    companion object {

        //余额
        const val VALUE_BALANCE = 1

        //押金
        const val VALUE_DEPOSIT = 0

        //Deposit押金
        fun openDepositActivity(context: Context) {
            if (context is Activity) {
                val intent = Intent(context, WalletInfoActivity::class.java)
                intent.putExtra(WalletConstants.KEY_VIEW_TYPE, VALUE_DEPOSIT)
                context.startActivity(intent)
            }
        }

        //Balance余额
        fun openBalanceActivity(context: Context) {
            if (context is Activity) {
                val intent = Intent(context, WalletInfoActivity::class.java)
                intent.putExtra(WalletConstants.KEY_VIEW_TYPE, VALUE_BALANCE)
                context.startActivity(intent)
            }
        }
    }

    override fun initBundles() {
        //默认余额
        viewType = intent?.getIntExtra(WalletConstants.KEY_VIEW_TYPE, VALUE_BALANCE)
    }

    override fun getLayoutId() = R.layout.wallet_activity_wallet_info

    override fun createPresenter(): WalletInfoPresenter {
        return when (viewType) {
            //默认余额
            VALUE_BALANCE -> BalanceInfoPresenterImpl(this)
            else -> DepositInfoPresenterImpl(this)
        }
    }

    override fun initView() {

        mPresenter.onCreate()

        //余额
        mToolBarDelegate.setMidTitle(mPresenter.getTitleHint())

        tv_wallet_info_type.text = mPresenter?.getTypeHint()

        tv_wallet_info_detail_type.text = mPresenter.getDetailHint()

        tv_wallet_info_option_type.text = mPresenter?.getOptionHint()

        tv_wallet_info_option_type.setOnClickListener {
            mPresenter?.onOptionClicked()
        }

        tvRate.setOnClickListener {
            DialogUtils.showDialog(context, "提醒费率", "手续费每单最低收取1分钱", null, null)
        }

        if (!isBalanceView()) {
            tv_wallet_info_option_type.visibility = View.GONE
        }

        // load data
        mPresenter.loadInfo()
        // set fragment
        FragmentUtils.add(
            supportFragmentManager,
            if (viewType == VALUE_BALANCE) BalanceDetailFragment.newInstance() else DepositDetailFragment.newInstance(),
            R.id.f_wallet_info
        )

    }

    override fun loadInfoSuccess(accountVo: AccountVo?) {
        val amount = accountVo?.balanceAmount
        tv_wallet_info_value.text = String.format("%s", amount ?: 0)
    }

    override fun loadInfoError(code: Int) {
        finish()
    }

    override fun setRate(rate: RateVo?) {
        rateLayout.visiable()
        val temp = "${rate?.getRatePercentage()}%  "
        tvRate.text = temp
    }

    private fun isBalanceView(): Boolean {
        return viewType == VALUE_BALANCE && UserManager.getLoginInfo()?.showButton == 1
    }

}