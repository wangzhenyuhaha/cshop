package com.lingmiao.shop.business.sales

import android.util.Log
import androidx.core.content.ContextCompat
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.james.common.base.loadmore.BaseLoadMoreActivity
import com.james.common.base.loadmore.core.IPage
import com.lingmiao.shop.R
import com.lingmiao.shop.business.goods.CateGoryGoodsAdapter
import com.lingmiao.shop.business.sales.adapter.DiscountAdapter
import com.lingmiao.shop.business.sales.bean.Coupon
import com.lingmiao.shop.business.sales.presenter.DiscountPresenter
import com.lingmiao.shop.business.sales.presenter.impl.DiscountPreImpl

class DiscountActivity : BaseLoadMoreActivity<Coupon, DiscountPresenter>(),
    DiscountPresenter.View {

    override fun createPresenter() = DiscountPreImpl(this, this)

    override fun initView() {
        super.initView()

        mToolBarDelegate?.setMidTitle("优惠券")
        mToolBarDelegate?.setRightText("新增") {
            DiscountDetailActivity.openActivity(this, 1)
        }
        mSmartRefreshLayout.setEnableRefresh(true)
        mSmartRefreshLayout.setBackgroundColor(ContextCompat.getColor(this, R.color.common_bg))

    }


    override fun useLightMode() = false

    override fun executePageRequest(page: IPage) {
        mPresenter.loadListData(page, mAdapter.data)
    }

    override fun initAdapter(): BaseQuickAdapter<Coupon, BaseViewHolder> {
        return DiscountAdapter().apply {
        }
    }

}