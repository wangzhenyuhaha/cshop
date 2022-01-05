package com.lingmiao.shop.business.sales

import com.james.common.base.BaseVBActivity
import com.lingmiao.shop.business.sales.presenter.DiscountDetailPre
import com.lingmiao.shop.business.sales.presenter.impl.DiscountDetailPreImpl
import com.lingmiao.shop.databinding.ActivityDiscountDetailBinding

class DiscountDetailActivity : BaseVBActivity<ActivityDiscountDetailBinding, DiscountDetailPre>(),
    DiscountDetailPre.View {

    override fun createPresenter() = DiscountDetailPreImpl(this, this)

    override fun getViewBinding() = ActivityDiscountDetailBinding.inflate(layoutInflater)

    override fun initView() {

    }

    override fun useLightMode() = false

}