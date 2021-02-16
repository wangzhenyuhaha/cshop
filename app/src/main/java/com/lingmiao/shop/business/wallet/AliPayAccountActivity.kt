package com.lingmiao.shop.business.wallet

import com.blankj.utilcode.util.ToastUtils
import com.lingmiao.shop.R
import com.lingmiao.shop.business.wallet.api.WalletConstants
import com.lingmiao.shop.business.wallet.bean.AlipayAccountVo
import com.lingmiao.shop.business.wallet.bean.WithdrawAccountBean
import com.lingmiao.shop.business.wallet.bean.WithdrawAccountVo
import com.lingmiao.shop.business.wallet.pop.ItemListPop
import com.lingmiao.shop.business.wallet.presenter.AliPayAccountPresenter
import com.lingmiao.shop.business.wallet.presenter.impl.AliPayAccountPresenterImpl
import com.james.common.base.BaseActivity
import kotlinx.android.synthetic.main.wallet_activity_set_third_account.*

/**
 * 设置三方账户
 */
class AliPayAccountActivity : BaseActivity<AliPayAccountPresenter>(),
    AliPayAccountPresenter.View {

    private var aLiAccount : AlipayAccountVo? = null;

    private var mAccountTypes = mutableListOf<String>();

    private var mAccountType : Int ? = WalletConstants.PUBLIC_PRIVATE;

    override fun createPresenter(): AliPayAccountPresenter {
        return AliPayAccountPresenterImpl(this);
    }

    override fun getLayoutId(): Int {
        return R.layout.wallet_activity_set_third_account;
    }

    override fun initView() {
        mPresenter.onCreate();
        mToolBarDelegate.setMidTitle("支付宝");

        tv_account_submit.setOnClickListener {
            if (et_wallet_third_account_name.text.length === 0) {
                ToastUtils.showShort("请输入真实姓名")
                return@setOnClickListener
            }
            if (et_wallet_third_account_detail_name.text.length === 0) {
                ToastUtils.showShort("请输入支付宝名称")
                return@setOnClickListener
            }
            if(aLiAccount === null || aLiAccount?.id === null) {
                var data = WithdrawAccountBean();
                data.type = WithdrawAccountBean.TYPE_ALI_PAY;
                data.ofPublic = mAccountType;
                data.accountNo = et_wallet_third_account_detail_name.text.toString();
                data.accountName = et_wallet_third_account_name.text.toString();
                mPresenter.submitAccountInfo(data);
            } else {
                var data = WithdrawAccountBean();
                data.type = WithdrawAccountBean.TYPE_ALI_PAY;
                data.ofPublic = mAccountType;
                data.accountNo = et_wallet_third_account_detail_name.text.toString();
                data.accountName = et_wallet_third_account_name.text.toString();
                data.id = aLiAccount?.id;
                mPresenter.updateAccountInfo(data);
            }
        };

        mAccountTypes.add(WalletConstants.PUBLIC_PRIVATE_NAME);
        mAccountTypes.add(WalletConstants.PUBLIC_COMPANY_NAME);
        ll_wallet_third_account_type.setOnClickListener {
            val pop = ItemListPop(this, mAccountTypes)
            pop.setOnClickListener { position, item ->
                run {
                    mAccountType = position;
                    tv_wallet_third_account_type.setText(item);
                }
            }
            pop.showPopupWindow()
        }

        mPresenter?.getWithdrawAccount();
    }

    override fun setAccountSuccess() {
        finish();
    }

    override fun getWithdrawAccountSuccess(account: WithdrawAccountVo) {
        aLiAccount = account?.alipayWithdrawAccount ?: null;
        tv_wallet_third_account_type.setText(account?.alipayWithdrawAccount?.getPublicName());
        et_wallet_third_account_detail_name.setText(account?.alipayWithdrawAccount?.accountNo ?: "");
        et_wallet_third_account_name.setText(account?.alipayWithdrawAccount?.accountName ?: "");
    }

}