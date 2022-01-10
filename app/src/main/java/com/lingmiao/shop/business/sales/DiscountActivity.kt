package com.lingmiao.shop.business.sales

import android.annotation.SuppressLint
import androidx.core.content.ContextCompat
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.james.common.base.loadmore.BaseLoadMoreActivity
import com.james.common.base.loadmore.core.IPage
import com.james.common.utils.DialogUtils
import com.lingmiao.shop.R
import com.lingmiao.shop.business.sales.adapter.DiscountAdapter
import com.lingmiao.shop.business.sales.bean.Coupon
import com.lingmiao.shop.business.sales.presenter.DiscountPresenter
import com.lingmiao.shop.business.sales.presenter.impl.DiscountPreImpl

@SuppressLint("NotifyDataSetChanged")
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

    override fun deleteCouponSuccess(position: Int) {
        mAdapter.remove(position)
        mAdapter.notifyDataSetChanged()
        showToast("成功删除该优惠券")
    }

    override fun editCouponSuccess(position: Int) {
        mLoadMoreDelegate?.refresh()

    }

    override fun executePageRequest(page: IPage) {
        mPresenter.loadListData(page, mAdapter.data)
    }

    override fun initAdapter(): BaseQuickAdapter<Coupon, BaseViewHolder> {
        return DiscountAdapter().apply {

            setOnItemChildClickListener { adapter, view, position ->

                val item = adapter.getItem(position) as Coupon

                when (view.id) {
                    R.id.couponDetail -> {
                        DiscountDetailActivity.openActivity(context, 0, item)
                    }

                    R.id.couponBegin -> {
                        val disabled = if (item.disabled == 0) -1 else 0
                        item.couponID?.let { mPresenter.editCoupon(disabled, it, position) }
                    }

                    R.id.couponDelete -> {
                        DialogUtils.showDialog(context,
                            "提示",
                            "确定删除优惠券${item.title}吗？",
                            "取消",
                            "确定",
                            {

                            }, {
                                item.couponID?.let { mPresenter.deleteCoupon(it, position) }
                            })
                    }
                }


            }

        }
    }

    override fun onResume() {
        super.onResume()
        mLoadMoreDelegate?.refresh()
    }
}