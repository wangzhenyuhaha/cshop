package com.lingmiao.shop.business.wallet

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.view.View
import com.blankj.utilcode.util.FragmentUtils
import com.blankj.utilcode.util.ToastUtils
import com.lingmiao.shop.R
import com.lingmiao.shop.business.wallet.api.WalletConstants
import com.lingmiao.shop.business.wallet.bean.AccountVo
import com.lingmiao.shop.business.wallet.fragment.BalanceDetailFragment
import com.lingmiao.shop.business.wallet.fragment.DepositDetailFragment
import com.lingmiao.shop.business.wallet.presenter.WalletInfoPresenter
import com.lingmiao.shop.business.wallet.presenter.impl.BalanceInfoPresenterImpl
import com.lingmiao.shop.business.wallet.presenter.impl.DepositInfoPresenterImpl
import com.james.common.base.BaseActivity
import com.james.common.utils.exts.visiable
import com.lingmiao.shop.business.wallet.bean.RateVo
import kotlinx.android.synthetic.main.wallet_view_wallet_sum.*
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode

/**
 * 余额/押金
 */
class WalletInfoActivity : BaseActivity<WalletInfoPresenter>(), WalletInfoPresenter.View {

    private var viewType: Int? = VALUE_BALANCE;

    companion object {

        const val VALUE_BALANCE = 1;

        const val VALUE_DEPOSIT = 0;

        fun openDepositActivity(context: Context) {
            if (context is Activity) {
                val intent = Intent(context, WalletInfoActivity::class.java)
                intent.putExtra(WalletConstants.KEY_VIEW_TYPE, VALUE_DEPOSIT)
                context.startActivity(intent)
            }
        }

        fun openBalanceActivity(context: Context) {
            if (context is Activity) {
                val intent = Intent(context, WalletInfoActivity::class.java)
                intent.putExtra(WalletConstants.KEY_VIEW_TYPE, VALUE_BALANCE)
                context.startActivity(intent)
            }
        }
    }

    override fun initBundles() {
        viewType = intent?.getIntExtra(WalletConstants.KEY_VIEW_TYPE, VALUE_BALANCE);
    }

    override fun getLayoutId(): Int {
        return R.layout.wallet_activity_wallet_info;
    }

    override fun createPresenter(): WalletInfoPresenter {
        return when (viewType) {
            VALUE_BALANCE -> BalanceInfoPresenterImpl(this)
            else -> DepositInfoPresenterImpl(this)
        }
    }

    override fun initView() {
        mPresenter.onCreate();

        // set title
        mToolBarDelegate.setMidTitle(mPresenter.getTitleHint());

        // set hint
        tv_wallet_info_type.setText(mPresenter?.getTypeHint());
        tv_wallet_info_detail_type.setText(mPresenter.getDetailHint());
        tv_wallet_info_option_type.setText(mPresenter?.getOptionHint());
        tv_wallet_info_option_type.setOnClickListener {
            mPresenter?.onOptionClicked();
        }

        if(!isBalanceView()) {
            tv_wallet_info_option_type.visibility = View.GONE;
        }

        // load data
        mPresenter.loadInfo();

        // set fragment
        FragmentUtils.add(
            supportFragmentManager,
            if (isBalanceView()) BalanceDetailFragment.newInstance() else DepositDetailFragment.newInstance(),
            R.id.f_wallet_info
        );

    }

    override fun loadInfoSuccess(accountVo: AccountVo ?) {
        var amount = accountVo?.balanceAmount;
        tv_wallet_info_value.text = String.format("%s", amount ?: 0);
    }

    override fun loadInfoError(code: Int) {
        finish();
    }

    override fun setRate(rate: RateVo?) {
        rateLayout.visiable();
        tvRate.setText("${rate?.getRatePercentage()}%");
    }

    private fun isBalanceView(): Boolean {
        return viewType === VALUE_BALANCE;
    }

}