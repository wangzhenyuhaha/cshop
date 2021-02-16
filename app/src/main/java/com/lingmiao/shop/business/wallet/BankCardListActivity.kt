package com.lingmiao.shop.business.wallet
import android.app.Activity
import android.content.Context
import android.os.Bundle
import com.blankj.utilcode.util.ActivityUtils
import com.blankj.utilcode.util.FragmentUtils
import com.lingmiao.shop.R
import com.lingmiao.shop.business.wallet.api.WalletConstants
import com.lingmiao.shop.business.wallet.fragment.BankCardListFragment
import com.james.common.base.BaseActivity
import com.james.common.base.BasePreImpl
import com.james.common.base.BasePresenter
import com.james.common.base.BaseView
import kotlinx.android.synthetic.main.wallet_activity_bank_card_list.*

class BankCardListActivity : BaseActivity<BasePresenter>(), BaseView {

    private var viewType: Int? = VALUE_WITHDRAW;

    companion object {

        const val VALUE_WITHDRAW = 1;

        const val VALUE_DEFAULT = 0;

        fun withdrawBundle(context: Context) : Bundle {
            val bundle = Bundle();
            if (context is Activity) {
                bundle.putInt(WalletConstants.KEY_VIEW_TYPE, VALUE_WITHDRAW);
            }
            return bundle;
        }
    }

    override fun initBundles() {
        viewType = intent?.getIntExtra(
            WalletConstants.KEY_VIEW_TYPE,
            VALUE_DEFAULT
        );
    }

    override fun createPresenter(): BasePresenter {
        return BasePreImpl(this);
    }

    override fun getLayoutId(): Int {
        return R.layout.wallet_activity_bank_card_list;
    }

    override fun initView() {
        mPresenter.onCreate();
        mToolBarDelegate.setMidTitle(getString(R.string.wallet_bank_card_title));

        tv_bankcard_add.setOnClickListener {
            ActivityUtils.startActivity(AddBankCardActivity::class.java);
        }
    }

    override fun onResume() {
        super.onResume()

        // set fragment
        FragmentUtils.replace(
            supportFragmentManager,
            BankCardListFragment.newInstance(viewType),
            R.id.f_wallet_bank_card_list
        );

    }

}