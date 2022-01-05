package com.lingmiao.shop.business.sales

import com.blankj.utilcode.util.ActivityUtils
import com.james.common.base.BaseVBActivity
import com.lingmiao.shop.business.goods.MenuGoodsManagerActivity
import com.lingmiao.shop.business.sales.adapter.DiscountAdapter
import com.lingmiao.shop.business.sales.presenter.DiscountPresenter
import com.lingmiao.shop.business.sales.presenter.impl.DiscountPreImpl
import com.lingmiao.shop.databinding.ActivityDiscountBinding

class DiscountActivity : BaseVBActivity<ActivityDiscountBinding, DiscountPresenter>(),
    DiscountPresenter.View {

    override fun createPresenter() = DiscountPreImpl(this, this)

    override fun getViewBinding() = ActivityDiscountBinding.inflate(layoutInflater)

    private var adapter: DiscountAdapter? = null

    override fun initView() {

        mToolBarDelegate?.setMidTitle("优惠券")

        mToolBarDelegate?.setRightText("新增") {
            DiscountDetailActivity.openActivity(this, 1)
        }

    }


    override fun useLightMode() = false

}