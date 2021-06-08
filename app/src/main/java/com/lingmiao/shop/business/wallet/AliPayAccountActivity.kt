package com.lingmiao.shop.business.wallet

import android.app.Activity
import android.content.Context
import android.content.Intent
import com.blankj.utilcode.util.ToastUtils
import com.lingmiao.shop.R
import com.lingmiao.shop.business.wallet.api.WalletConstants
import com.lingmiao.shop.business.wallet.bean.AlipayAccountVo
import com.lingmiao.shop.business.wallet.bean.WithdrawAccountBean
import com.lingmiao.shop.business.wallet.bean.WithdrawAccountVo
import com.lingmiao.shop.business.wallet.pop.ItemListPop
import com.lingmiao.shop.business.wallet.presenter.ThirdAccountPresenter
import com.lingmiao.shop.business.wallet.presenter.impl.AliPayAccountPresenterImpl
import com.james.common.base.BaseActivity
import com.lingmiao.shop.business.wallet.presenter.impl.WechatAccountPresenterImpl
import kotlinx.android.synthetic.main.wallet_activity_set_third_account.*

/**
 * 设置三方账户
 */
class AliPayAccountActivity : BaseActivity<ThirdAccountPresenter>(),
    ThirdAccountPresenter.View {

    var type : Int? = WithdrawAccountBean.TYPE_WECHAT;

    var name : String? = "微信";

    private var aLiAccount : AlipayAccountVo? = null;

    private var mAccountTypes = mutableListOf<String>();

    private var mAccountType : Int ? = WalletConstants.PUBLIC_PRIVATE;

    companion object {
        fun wechat(context: Context, account: AlipayAccountVo?) {
            if (context is Activity) {
                val intent = Intent(context, AliPayAccountActivity::class.java)
                intent.putExtra("type", WithdrawAccountBean.TYPE_WECHAT)
                intent.putExtra("item", account)
                context.startActivity(intent)
            }
        }

        fun ali(context: Context) {
            if (context is Activity) {
                val intent = Intent(context, AliPayAccountActivity::class.java)
                intent.putExtra("type", WithdrawAccountBean.TYPE_ALI_PAY)
                context.startActivity(intent)
            }
        }
    }

    override fun initBundles() {
        type = intent.getIntExtra("type", WithdrawAccountBean.TYPE_WECHAT);
        aLiAccount = intent.getSerializableExtra("item") as AlipayAccountVo?;
        if(type == WithdrawAccountBean.TYPE_ALI_PAY) {
            name = "支付宝"
        }
    }

    override fun useLightMode(): Boolean {
        return false;
    }

    override fun createPresenter(): ThirdAccountPresenter {
        return when(type) {
            WithdrawAccountBean.TYPE_ALI_PAY -> AliPayAccountPresenterImpl(this);
            else -> WechatAccountPresenterImpl(this);
        }
    }

    override fun getLayoutId(): Int {
        return R.layout.wallet_activity_set_third_account;
    }

    override fun initView() {
        mPresenter.onCreate();
        mToolBarDelegate.setMidTitle(name);

        tv_wallet_third_account_detail_name.setText(String.format("%s账号", name));

        tv_account_submit.setOnClickListener {
            if (et_wallet_third_account_name.text.length === 0) {
                ToastUtils.showShort("请输入真实姓名")
                return@setOnClickListener
            }
            if (et_wallet_third_account_detail_name.text.length === 0) {
                ToastUtils.showShort(String.format("请输入%s名称", name))
                return@setOnClickListener
            }
            if(aLiAccount === null || aLiAccount?.id === null) {
                var data = WithdrawAccountBean();
                data.type = type;
                data.ofPublic = mAccountType;
                data.accountNo = et_wallet_third_account_detail_name.text.toString();
                data.accountName = et_wallet_third_account_name.text.toString();
                mPresenter.submitAccountInfo(data);
            } else {
                var data = WithdrawAccountBean();
                data.type = type;
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

        if(aLiAccount == null) {
            mPresenter?.getWithdrawAccount();
        } else {
            setUiData(aLiAccount);
        }
    }

    override fun setAccountSuccess() {
        finish();
    }

    override fun getWithdrawAccountSuccess(account: WithdrawAccountVo) {
        aLiAccount = account?.wechatWithdrawAccount ?: null;
        setUiData(aLiAccount);
    }

    fun setUiData(aLiAccount : AlipayAccountVo?) {
        tv_account_submit.setText(if(aLiAccount == null) "确定提交" else "确定修改")
        tv_wallet_third_account_type.setText(aLiAccount?.getPublicName());
        et_wallet_third_account_detail_name.setText(aLiAccount?.accountNo ?: "");
        et_wallet_third_account_name.setText(aLiAccount?.accountName ?: "");
    }
}