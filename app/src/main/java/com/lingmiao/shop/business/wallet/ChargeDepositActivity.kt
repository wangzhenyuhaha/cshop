package com.lingmiao.shop.business.wallet

import com.blankj.utilcode.util.ToastUtils
import com.lingmiao.shop.R
import com.lingmiao.shop.business.wallet.api.WalletConstants
import com.lingmiao.shop.business.wallet.bean.AlipayAccountVo
import com.lingmiao.shop.business.wallet.presenter.ChargeDepositPresenter
import com.lingmiao.shop.business.wallet.presenter.impl.ChargeDepositPresenterImpl
import com.james.common.base.BaseActivity
import kotlinx.android.synthetic.main.wallet_activity_charge_deposit.*

/**
 * 押金充值
 */
class ChargeDepositActivity : BaseActivity<ChargeDepositPresenter>(), ChargeDepositPresenter.View {

    private var checkType: Int? = TYPE_ALI_PAY;

    private var accountData : AlipayAccountVo? = null;

    private var aliPayData : AlipayAccountVo? = null;


    companion object {
        const val TYPE_ALI_PAY = 1;
        const val TYPE_BALANCE = 2;
    }

    override fun createPresenter(): ChargeDepositPresenter {
        return ChargeDepositPresenterImpl(this);
    }

    override fun getLayoutId(): Int {
        return R.layout.wallet_activity_charge_deposit;
    }

    override fun initView() {
        mToolBarDelegate.setMidTitle(getString(R.string.wallet_deposit_opt_fill));

        ll_ali_pay.setOnClickListener {
            checkType = TYPE_ALI_PAY;
            ivApliPay.setImageResource(WalletConstants.CHECKED);
            ivBalancePay.setImageResource(WalletConstants.UNCHECK);
        }
        ll_account_pay.setOnClickListener {
            checkType = TYPE_BALANCE;
            ivApliPay.setImageResource(WalletConstants.UNCHECK);
            ivBalancePay.setImageResource(WalletConstants.CHECKED);
        }
        tvChargeSubmit.setOnClickListener {
            if(aliPayData == null && checkType == TYPE_ALI_PAY) {
                ToastUtils.showLong("请先绑定支付宝账号");
                return@setOnClickListener;
            }
            if (accountData == null && checkType == TYPE_BALANCE) {
                ToastUtils.showLong("请先设置支付宝账号");
                return@setOnClickListener;
            }
        }
    }

    override fun chargeSuccess() {

    }

    override fun chargeError(code: Int) {

    }

}