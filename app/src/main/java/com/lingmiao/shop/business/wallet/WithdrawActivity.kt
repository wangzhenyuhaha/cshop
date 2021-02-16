package com.lingmiao.shop.business.wallet

import android.app.Activity
import android.content.Intent
import android.view.View
import com.blankj.utilcode.util.ActivityUtils
import com.blankj.utilcode.util.SPUtils
import com.blankj.utilcode.util.ToastUtils
import com.lingmiao.shop.R
import com.lingmiao.shop.business.wallet.api.WalletConstants
import com.lingmiao.shop.business.wallet.bean.*
import com.lingmiao.shop.business.wallet.event.RefreshListEvent
import com.lingmiao.shop.business.wallet.presenter.WithdrawPresenter
import com.lingmiao.shop.business.wallet.presenter.impl.WithdrawPresenterImpl
import com.james.common.base.BaseActivity
import kotlinx.android.synthetic.main.wallet_activity_withdraw.*
import kotlinx.android.synthetic.main.wallet_activity_withdraw.et_wallet_account_name
import kotlinx.android.synthetic.main.wallet_activity_withdraw.et_wallet_account_no
import kotlinx.android.synthetic.main.wallet_activity_withdraw.ll_wallet_account_bank_deposit
import kotlinx.android.synthetic.main.wallet_activity_withdraw.tv_wallet_account_bank_deposit
import org.greenrobot.eventbus.EventBus

/**
 * 提现
 */
class WithdrawActivity : BaseActivity<WithdrawPresenter>(), WithdrawPresenter.View {

    private var checkType: Int? = TYPE_ALI_PAY;

    private var accountId : String ? = "";

    private var accountInfo: WithdrawAccountVo? = null;

    companion object {
        const val TYPE_ALI_PAY = 1;
        const val TYPE_CARD_PAY = 2;
        const val RESULT_CHECK_BANK_CARD = 3;
    }

    override fun getLayoutId(): Int {
        return R.layout.wallet_activity_withdraw;
    }

    override fun createPresenter(): WithdrawPresenter {
        return WithdrawPresenterImpl(this);
    }

    override fun initView() {
        mToolBarDelegate.setMidTitle(getString(R.string.wallet_title_withdraw));

        mPresenter.onCreate();
        mPresenter.getWithdrawAccount();

        accountId = SPUtils.getInstance().getString(WalletConstants.BALANCE_ACCOUNT_ID);

        ll_ali_pay.setOnClickListener {
            setAliPayChecked();
        }
        ll_card_pay.setOnClickListener {
            setBankCardChecked();
//            BankCardListActivity.withdrawToActivity(this);
        }
        ll_wallet_bank_card.setOnClickListener{
            ActivityUtils.startActivityForResult(BankCardListActivity.withdrawBundle(this), this, BankCardListActivity::class.java, RESULT_CHECK_BANK_CARD);
        }
        tv_submit.setOnClickListener {
            if (et_wallet_account_amount.text.length === 0) {
                ToastUtils.showLong("请先输入提现金额");
                return@setOnClickListener;
            }
            if(checkType === TYPE_ALI_PAY && accountInfo?.notExistAliPayAccount() === true) {
                ToastUtils.showLong("请先设置支付宝账号");
                return@setOnClickListener;
            }
            if(checkType === TYPE_CARD_PAY && accountInfo?.notExistBankAccount() == true) {
                ToastUtils.showLong("请先添加银行卡账号");
                return@setOnClickListener;
            }

            if (checkType === TYPE_ALI_PAY) {
                applyAliPayWithdraw();
            } else if (checkType === TYPE_CARD_PAY) {
                applyBankWithdraw();
            }
        }
    }

    private fun applyAliPayWithdraw() {
        var data = WithdrawApplyVo();
        data.accountId = accountId;
        data.amount = et_wallet_account_amount.text.toString().toDouble();
        data.withdrawChannel = 2;
        data.otherAccountTypeName = "支付宝";
        data.otherAccountName = et_wallet_account_name.text.toString();
        data.otherAccountNo = et_wallet_account_no.text.toString();

        mPresenter.applyWithdraw(data);
    }

    private fun applyBankWithdraw() {
        var data = WithdrawApplyVo();
        data.accountId = accountId;
        data.amount = et_wallet_account_amount.text.toString().toDouble();
        data.withdrawChannel = 1;
        data.bankName = tv_wallet_account_bank_deposit.text.toString();
        data.cardNo = et_wallet_account_no.text.toString();
        data.openAccountName = et_wallet_account_name.text.toString();

        mPresenter.applyWithdraw(data);
    }

    private fun setAliPayChecked() {
        checkType = TYPE_ALI_PAY;
        ivApliPay.setImageResource(WalletConstants.CHECKED);
        ivCardPay.setImageResource(WalletConstants.UNCHECK);


        // 有一些设置没有删除，等方案稳定了再删除
        ll_wallet_ali_pay_info.visibility = View.VISIBLE;
        ll_wallet_bank_card.visibility = View.GONE;
        ll_wallet_account_bank_deposit.visibility = View.GONE;
        v_wallet_account_bank_deposit.visibility = View.GONE;
        tv_withdraw_account_type.setText(getString(R.string.withdraw_ali_pay_account));
        tv_withdraw_account_name.setText(getString(R.string.withdraw_real_name));
        tv_withdraw_account_no.setText(getString(R.string.withdraw_ali_pay_account_no));

        et_wallet_account_name.setText(accountInfo?.alipayWithdrawAccount?.accountName ?: "");
        et_wallet_account_no.setText(accountInfo?.alipayWithdrawAccount?.accountNo ?: "");

        setAliPayEnable(accountInfo == null || accountInfo?.alipayWithdrawAccount == null);
    }

    private fun setBankCardChecked() {
        checkType = TYPE_CARD_PAY;
        ivCardPay.setImageResource(WalletConstants.CHECKED);
        ivApliPay.setImageResource(WalletConstants.UNCHECK);

        // 有一些设置没有删除，等方案稳定了再删除
        ll_wallet_ali_pay_info.visibility = View.GONE;
        ll_wallet_bank_card.visibility = View.VISIBLE;
        ll_wallet_account_bank_deposit.visibility = View.VISIBLE;
        v_wallet_account_bank_deposit.visibility = View.VISIBLE;
        tv_withdraw_account_type.setText(getString(R.string.withdraw_bank_account));
        tv_withdraw_account_name.setText(getString(R.string.withdraw_bank_name));
        tv_withdraw_account_no.setText(getString(R.string.withdraw_bank_no));

        tv_wallet_bank_info.setText(accountInfo?.bankCard?.getBankInfo() ?: "请选择收款银行账号");

        tv_wallet_account_bank_deposit.setText(accountInfo?.bankCard?.bankName ?: "");
        et_wallet_account_name.setText(accountInfo?.bankCard?.openAccountName ?: "");
        et_wallet_account_no.setText(accountInfo?.bankCard?.cardNo ?: "");

        setBankEnable(accountInfo == null || accountInfo?.bankCard == null);
    }

    private fun setBankEnable(flag : Boolean) {
        tv_wallet_account_bank_deposit.isEnabled = flag;
        et_wallet_account_name.isEnabled = flag;
        et_wallet_account_no.isEnabled = flag;
    }

    private fun setAliPayEnable(flag : Boolean) {
        et_wallet_account_name.isEnabled = flag;
        et_wallet_account_no.isEnabled = flag;
    }

    override fun getWithdrawAccountSuccess(info: WithdrawAccountVo) {
        if (info != null) {
            this.accountInfo = info;
            setAliPayChecked();
        }
    }

    override fun applySuccess() {
        EventBus.getDefault().post(RefreshListEvent(WalletInfoActivity.VALUE_BALANCE));
        finish();
    }

    override fun applyFailed(msg: String) {
        showToast(msg);
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(resultCode == Activity.RESULT_OK && requestCode == RESULT_CHECK_BANK_CARD) {
            var item : BankCardAccountBean = data?.getSerializableExtra(WalletConstants.KEY_ITEM) as BankCardAccountBean;
            if(item != null) {
                accountInfo?.bankCard = item;
            }
            setBankCardChecked();
        }
    }
}