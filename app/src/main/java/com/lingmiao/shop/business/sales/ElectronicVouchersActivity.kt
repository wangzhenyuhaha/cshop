package com.lingmiao.shop.business.sales

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.james.common.base.loadmore.BaseLoadMoreActivity
import com.james.common.base.loadmore.core.IPage
import com.lingmiao.shop.business.sales.bean.Coupon
import com.lingmiao.shop.business.sales.bean.ElectronicVoucher
import com.lingmiao.shop.business.sales.presenter.DiscountPresenter
import com.lingmiao.shop.business.sales.presenter.ElectronicVoucherPresenter

@SuppressLint("NotifyDataSetChanged")
class ElectronicVouchersActivity : BaseLoadMoreActivity<ElectronicVoucher, ElectronicVoucherPresenter>(),
    ElectronicVoucherPresenter.View {
    override fun createPresenter(): ElectronicVoucherPresenter {
        TODO("Not yet implemented")
    }

    override fun initAdapter(): BaseQuickAdapter<ElectronicVoucher, BaseViewHolder> {
        TODO("Not yet implemented")
    }

    override fun executePageRequest(page: IPage) {
        TODO("Not yet implemented")
    }

}