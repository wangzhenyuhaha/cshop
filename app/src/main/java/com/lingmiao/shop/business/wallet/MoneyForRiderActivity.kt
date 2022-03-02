package com.lingmiao.shop.business.wallet

import android.content.Context
import android.content.Intent
import com.james.common.base.BaseVBActivity
import com.lingmiao.shop.business.wallet.presenter.MoneyForRiderPresenter
import com.lingmiao.shop.business.wallet.presenter.impl.MoneyForRiderPresenterImpl
import com.lingmiao.shop.databinding.ActivityMoneyForRiderBinding

class MoneyForRiderActivity :
    BaseVBActivity<ActivityMoneyForRiderBinding, MoneyForRiderPresenter>(),
    MoneyForRiderPresenter.View {

    companion object {
        fun openActivity(context: Context) {
            val intent = Intent(context, MoneyForRiderActivity::class.java)
            context.startActivity(intent)
        }
    }

    override fun createPresenter() = MoneyForRiderPresenterImpl(this, this)

    override fun getViewBinding() = ActivityMoneyForRiderBinding.inflate(layoutInflater)

    override fun useLightMode() = false

    override fun initView() {


    }


}