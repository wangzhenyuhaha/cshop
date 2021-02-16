package com.lingmiao.shop.business.wallet

import com.blankj.utilcode.util.ToastUtils
import com.lingmiao.shop.R
import com.lingmiao.shop.business.wallet.api.WalletConstants
import com.lingmiao.shop.business.wallet.bean.BankCardAccountBean
import com.lingmiao.shop.business.wallet.bean.BankCardVo
import com.lingmiao.shop.business.wallet.pop.BankCardListPop
import com.lingmiao.shop.business.wallet.pop.ItemListPop
import com.lingmiao.shop.business.wallet.presenter.AddBankCardPresenter
import com.lingmiao.shop.business.wallet.presenter.impl.AddBankCardPresenterImpl
import com.james.common.base.BaseActivity
import kotlinx.android.synthetic.main.wallet_activity_add_bank_card.*

/**
 * 绑定银行卡
 */
class AddBankCardActivity : BaseActivity<AddBankCardPresenter>(), AddBankCardPresenter.View {

    private var bankCardList = mutableListOf<String>();

    private var mAccountTypes = mutableListOf<String>();

    private var mAccountType : Int ? = WalletConstants.PUBLIC_PRIVATE;

    private var mItem : BankCardAccountBean? = null;

    override fun createPresenter(): AddBankCardPresenter {
        return AddBankCardPresenterImpl(this);
    }

    override fun getLayoutId(): Int {
        return R.layout.wallet_activity_add_bank_card;
    }

    override fun initBundles() {
        mItem = intent?.getSerializableExtra(WalletConstants.KEY_ITEM) as BankCardAccountBean?;
    }

    override fun initView() {
        mPresenter.onCreate();
        mToolBarDelegate.setMidTitle("银行卡");

        bankCardList.addAll(resources.getStringArray(R.array.bank_card_list));

        tv_bankcard_submit.setOnClickListener {
            if(tv_wallet_account_bank_deposit.text.length == 0) {
                ToastUtils.showLong("请先选择开户银行");
                return@setOnClickListener;
            }
            if(et_wallet_account_name.text.length == 0) {
                ToastUtils.showLong("请先输入账户名称");
                return@setOnClickListener;
            }
            if(et_wallet_account_no.text.length == 0) {
                ToastUtils.showLong("请先输入银行卡号");
                return@setOnClickListener;
            }
            var data = BankCardVo();
            data.bankCardType = mAccountType;
            data.bankName = tv_wallet_account_bank_deposit.text.toString();
            data.openAccountName = et_wallet_account_name.text.toString();
            data.cardNo = et_wallet_account_no.text.toString();

            if(mItem == null || mItem?.id == null || mItem?.id?.length == 0) {
                mPresenter?.submitBankCard(data);
            } else {
                 data.id = mItem?.id;
                mPresenter?.updateBankCard(data);
            }
        }
        ll_wallet_account_bank_deposit.setOnClickListener {
            val pop = BankCardListPop(this, bankCardList)
            pop.setOnClickListener { item ->
                run {
                    tv_wallet_account_bank_deposit.setText(item);
                }
            }
            pop.showPopupWindow()
        }

        mAccountTypes.add(WalletConstants.PUBLIC_PRIVATE_NAME);
        mAccountTypes.add(WalletConstants.PUBLIC_COMPANY_NAME);
        ll_wallet_account_type.setOnClickListener {
            val pop = ItemListPop(this, mAccountTypes)
            pop.setOnClickListener { position, item ->
                run {
                    mAccountType = position;
                    tv_wallet_account_type.setText(item);
                }
            }
            pop.showPopupWindow()
        }

        // 默认为个人
        tv_wallet_account_type.setText(WalletConstants.PUBLIC_PRIVATE_NAME);
        mAccountType = WalletConstants.PUBLIC_PRIVATE;

        if(mItem != null) {
            mAccountType = mItem?.bankCardType;
            tv_wallet_account_type.setText(if(mAccountType === WalletConstants.PUBLIC_PRIVATE) WalletConstants.PUBLIC_PRIVATE_NAME else WalletConstants.PUBLIC_COMPANY_NAME);
            tv_wallet_account_bank_deposit.setText(mItem?.bankName ?: "");
            et_wallet_account_name.setText(mItem?.openAccountName ?: "");
            et_wallet_account_no.setText(mItem?.cardNo ?: "");
        }
    }

    override fun submitBankCardSuccess() {
        finish();
    }

}