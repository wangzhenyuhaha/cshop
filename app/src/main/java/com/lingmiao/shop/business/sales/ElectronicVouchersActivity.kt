package com.lingmiao.shop.business.sales

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.core.content.ContextCompat
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.james.common.base.loadmore.BaseLoadMoreActivity
import com.james.common.base.loadmore.core.IPage
import com.lingmiao.shop.R
import com.lingmiao.shop.business.sales.adapter.DiscountAdapter
import com.lingmiao.shop.business.sales.adapter.ElectronicVouchersAdapter
import com.lingmiao.shop.business.sales.bean.Coupon
import com.lingmiao.shop.business.sales.bean.ElectronicVoucher
import com.lingmiao.shop.business.sales.presenter.DiscountPresenter
import com.lingmiao.shop.business.sales.presenter.ElectronicVoucherPresenter
import com.lingmiao.shop.business.sales.presenter.impl.ElectronicVoucherPreImpl

@SuppressLint("NotifyDataSetChanged")
class ElectronicVouchersActivity :
    BaseLoadMoreActivity<ElectronicVoucher, ElectronicVoucherPresenter>(),
    ElectronicVoucherPresenter.View {

    override fun useLightMode() = false

    override fun createPresenter() = ElectronicVoucherPreImpl(this, this)

    override fun initAdapter(): BaseQuickAdapter<ElectronicVoucher, BaseViewHolder> {
        return ElectronicVouchersAdapter()
    }

    override fun executePageRequest(page: IPage) {

    }

    override fun initView() {
        super.initView()

        mToolBarDelegate?.setMidTitle("电子券")
        mToolBarDelegate?.setRightText("新增") {
            EVouchersDetailActivity.openActivity(this, 1)
        }
        mSmartRefreshLayout.setEnableRefresh(true)
        mSmartRefreshLayout.setBackgroundColor(ContextCompat.getColor(this, R.color.common_bg))
    }
}