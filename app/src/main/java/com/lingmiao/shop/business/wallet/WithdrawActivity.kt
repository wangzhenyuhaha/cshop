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
import com.james.common.utils.DialogUtils
import com.james.common.utils.exts.visiable
import com.lingmiao.shop.business.wallet.adapter.WithdrawTypeAdapter
import com.lingmiao.shop.util.initAdapter
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

    private var accountId : String ? = "";

    private var accountInfo: WithdrawAccountVo? = null;

    lateinit var mAdapter : WithdrawTypeAdapter;

    companion object {
        const val RESULT_WECHAT = 100;
        const val RESULT_CHECK_BANK_CARD = 900;
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

        mAdapter = WithdrawTypeAdapter().apply{
            setOnItemChildClickListener { adapter, view, position ->
                val it = adapter.data[position] as WithdrawTypeVo;
                when(view.id) {
                    R.id.tvTypeHint -> {
                        shiftType(it);
                        shiftCheckPosition(position);
                        if(it.type == IPayConstants.CHANNEL_CARD) {
                            setBankCardChecked();
                        } else if(it.type == IPayConstants.CHANNEL_ALI) {
                            setAliPayChecked();
                        } else if(it.type == IPayConstants.CHANNEL_WECHAT) {
                            setWechatChecked();
                        }
                        notifyDataSetChanged()
                    }
                }
            }
            addData(WithdrawTypeVo.getWechat())
            rvWithdrawType.initAdapter(this);
        };


        ll_wallet_bank_card.setOnClickListener{
            // 选择银行卡
            ActivityUtils.startActivityForResult(BankCardListActivity.withdrawBundle(this), this, BankCardListActivity::class.java, RESULT_CHECK_BANK_CARD);
        }
        tv_submit.setOnClickListener {
            if (et_wallet_account_amount.text.length === 0) {
                ToastUtils.showLong("请先输入提现金额");
                return@setOnClickListener;
            }
            if(mAdapter.mCheckPosition < 0) {
                ToastUtils.showLong("请先选择提现方式");
                return@setOnClickListener;
            }
            var it = mAdapter.data[mAdapter.mCheckPosition];

            if(it.type === IPayConstants.CHANNEL_ALI && accountInfo?.notExistAliPayAccount() === true) {
                ToastUtils.showLong("请先设置支付宝账号");
                return@setOnClickListener;
            }
            if(it.type === IPayConstants.CHANNEL_WECHAT && accountInfo?.notExistWechatAccount() == true) {
                DialogUtils.showDialog(context, "设置微信帐号", "请先设置微信帐号?",
                    "取消", "确定", View.OnClickListener { }, View.OnClickListener {
                        AliPayAccountActivity.wechat(this, accountInfo?.wechatWithdrawAccount, RESULT_WECHAT);
                    })
                //ToastUtils.showLong("请先设置微信账号");
                return@setOnClickListener;
            }
            if(it.type === IPayConstants.CHANNEL_CARD && accountInfo?.notExistBankAccount() == true) {
                ToastUtils.showLong("请先添加银行卡账号");
                return@setOnClickListener;
            }

            if (it.type === IPayConstants.CHANNEL_CARD) {
                applyBankWithdraw();
            } else {
                applyWithdraw(it);
            }
        }
    }

    fun applyWithdraw(item : WithdrawTypeVo) {
        var data = WithdrawApplyVo();
        data.accountId = accountId;
        data.amount = et_wallet_account_amount.text.toString().toDouble();
        data.withdrawChannel = item.type;
        data.otherAccountTypeName = item.hint;
        data.otherAccountName = et_wallet_account_name.text.toString();
        data.otherAccountNo = et_wallet_account_no.text.toString();

        mPresenter.applyWithdraw(data);
    }

    private fun applyBankWithdraw() {
        var data = WithdrawApplyVo();
        data.accountId = accountId;
        data.amount = et_wallet_account_amount.text.toString().toDouble();
        data.withdrawChannel = IPayConstants.CHANNEL_CARD;
        data.bankName = tv_wallet_account_bank_deposit.text.toString();
        data.cardNo = et_wallet_account_no.text.toString();
        data.openAccountName = et_wallet_account_name.text.toString();

        mPresenter.applyWithdraw(data);
    }

    private fun setWechatChecked() {
        setThirdPayChecked(accountInfo?.wechatWithdrawAccount);
    }

    private fun setAliPayChecked() {
        setThirdPayChecked(accountInfo?.alipayWithdrawAccount);
    }

    private fun setThirdPayChecked(account : ThirdPayAccountVo?) {
        // 有一些设置没有删除，等方案稳定了再删除
        ll_wallet_ali_pay_info.visibility = View.VISIBLE;
        ll_wallet_bank_card.visibility = View.GONE;
        ll_wallet_account_bank_deposit.visibility = View.GONE;
        v_wallet_account_bank_deposit.visibility = View.GONE;

        et_wallet_account_name.setText(account?.accountName ?: "");
        et_wallet_account_no.setText(account?.accountNo ?: "");

        setThirdPayEnable(accountInfo == null || account == null);
    }


    private fun setBankCardChecked() {
        // 有一些设置没有删除，等方案稳定了再删除
        ll_wallet_ali_pay_info.visibility = View.GONE;
        ll_wallet_bank_card.visibility = View.VISIBLE;
        ll_wallet_account_bank_deposit.visibility = View.VISIBLE;
        v_wallet_account_bank_deposit.visibility = View.VISIBLE;

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

    private fun setThirdPayEnable(flag : Boolean) {
        et_wallet_account_name.isEnabled = flag;
        et_wallet_account_no.isEnabled = flag;
    }

    fun shiftType(it : WithdrawTypeVo) {
        tv_withdraw_account_type.setText(String.format("%s账户", it?.hint))
        tv_withdraw_account_no.setText(String.format("%s账户", it?.hint))
    }

    override fun getWithdrawAccountSuccess(info: WithdrawAccountVo) {
        if (info != null) {
            this.accountInfo = info;

            val position = 0;
            shiftType(mAdapter.data[position]);

            mAdapter.shiftCheckPosition(position)
            mAdapter.notifyDataSetChanged()

            setWechatChecked();
        }
    }

    override fun setRate(rate: RateVo?) {
        tvRechargeFee.visiable();
        tvRechargeFee.setText("提现费率：${rate?.getRatePercentage()}%")
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
        if(resultCode == Activity.RESULT_OK) {
            if(requestCode == RESULT_CHECK_BANK_CARD) {
                var item : BankCardAccountBean = data?.getSerializableExtra(WalletConstants.KEY_ITEM) as BankCardAccountBean;
                if(item != null) {
                    accountInfo?.bankCard = item;
                }
                setBankCardChecked();
            } else if(requestCode == RESULT_WECHAT) {
                mPresenter.getWithdrawAccount();
            }
        }

    }
}