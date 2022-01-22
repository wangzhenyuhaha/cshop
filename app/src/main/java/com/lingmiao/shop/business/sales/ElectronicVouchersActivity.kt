package com.lingmiao.shop.business.sales

import android.annotation.SuppressLint
import androidx.core.content.ContextCompat
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.james.common.base.loadmore.BaseLoadMoreActivity
import com.james.common.base.loadmore.core.IPage
import com.james.common.utils.DialogUtils
import com.lingmiao.shop.R
import com.lingmiao.shop.business.sales.adapter.ElectronicVouchersAdapter
import com.lingmiao.shop.business.sales.bean.Coupon
import com.lingmiao.shop.business.sales.bean.ElectronicVoucher
import com.lingmiao.shop.business.sales.presenter.ElectronicVoucherPresenter
import com.lingmiao.shop.business.sales.presenter.impl.ElectronicVoucherPreImpl

@SuppressLint("NotifyDataSetChanged")
class ElectronicVouchersActivity :
    BaseLoadMoreActivity<ElectronicVoucher, ElectronicVoucherPresenter>(),
    ElectronicVoucherPresenter.View {

    override fun useLightMode() = false

    override fun createPresenter() = ElectronicVoucherPreImpl(this, this)

    override fun initView() {
        super.initView()

        mToolBarDelegate?.setMidTitle("电子券")
        mToolBarDelegate?.setRightText("新增") {
            EVouchersDetailActivity.openActivity(this, 1)
        }
        mSmartRefreshLayout.setEnableRefresh(true)
        mSmartRefreshLayout.setBackgroundColor(ContextCompat.getColor(this, R.color.common_bg))
    }

    override fun initAdapter(): BaseQuickAdapter<ElectronicVoucher, BaseViewHolder> {
        return ElectronicVouchersAdapter().apply {
            setOnItemChildClickListener { adapter, view, position ->
                val item = adapter.getItem(position) as ElectronicVoucher

                when (view.id) {
                    R.id.couponDetail -> {
                        EVouchersDetailActivity.openActivity(context, 0, item)
                    }

                    R.id.couponBegin -> {
                        val disabled = if (item.disabled == 0) -1 else 0
                        item.couponID?.let {
                            mPresenter.editElectronicVoucher(
                                disabled,
                                it,
                                position
                            )
                        }
                    }

                    R.id.couponDelete -> {
                        DialogUtils.showDialog(context,
                            "提示",
                            "确定删除优惠券${item.title}吗？",
                            "取消",
                            "确定",
                            {

                            }, {
                                item.couponID?.let {
                                    mPresenter.deleteElectronicVoucher(
                                        it,
                                        position
                                    )
                                }
                            })
                    }
                }

            }
        }
    }

    override fun executePageRequest(page: IPage) {
        mPresenter.loadListData(page, mAdapter.data)
    }

    override fun deleteCouponSuccess(position: Int) {
        mAdapter.remove(position)
        mAdapter.notifyDataSetChanged()
        showToast("成功删除优电子券")
    }

    override fun editCouponSuccess(position: Int) {
        mLoadMoreDelegate?.refresh()
    }

    override fun onResume() {
        super.onResume()
        mLoadMoreDelegate?.refresh()
    }
}