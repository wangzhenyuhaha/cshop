package com.lingmiao.shop.business.sales

import android.app.Activity
import android.content.Context
import android.content.Intent
import com.blankj.utilcode.util.ActivityUtils
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.james.common.base.loadmore.BaseLoadMoreActivity
import com.james.common.base.loadmore.core.IPage
import com.lingmiao.shop.R
import com.lingmiao.shop.business.main.bean.MemberOrderBean
import com.lingmiao.shop.business.order.bean.OrderList
import com.lingmiao.shop.business.sales.adapter.UserOrderAdapter
import com.lingmiao.shop.business.sales.bean.UserVo
import com.lingmiao.shop.business.sales.presenter.IUserOrderPresenter
import com.lingmiao.shop.business.sales.presenter.impl.UserOrderPreImpl
import com.lingmiao.shop.widget.EmptyView
import kotlinx.android.synthetic.main.sales_activity_user_order.*

/**
Create Date : 2021/3/101:22 AM
Auther      : Fox
Desc        :
 **/
class UserOrderDetailActivity : BaseLoadMoreActivity<OrderList, IUserOrderPresenter>(), IUserOrderPresenter.PubView {


    companion object {
        fun open(
            context: Context,
            item : UserVo?,
            resultValue: Int
        ) {
            if (context is Activity) {
                val intent = Intent(context, UserOrderDetailActivity::class.java)
                intent.putExtra("item", item)
                context.startActivityForResult(intent, resultValue)
            }
        }

    }

    var mItem : UserVo? = null;
    override fun initBundles() {
        mItem = intent?.getSerializableExtra("item") as UserVo;
    }

    override fun initAdapter(): BaseQuickAdapter<OrderList, BaseViewHolder> {
        return UserOrderAdapter().apply {
            setOnItemChildClickListener { adapter, view, position ->
                when(view.id) {
                    R.id.tvSalesDelete -> {
                        ActivityUtils.startActivity(SalesActivityEditActivity::class.java);
                    }
                }
            }
            setOnItemClickListener { adapter, view, position ->

            }
            emptyView = EmptyView(context).apply {
                setBackgroundResource(R.color.color_ffffff)
            }
        }
    }

    override fun createPresenter(): IUserOrderPresenter {
        return UserOrderPreImpl(this, this);
    }

    override fun getLayoutId(): Int {
        return R.layout.sales_activity_user_order;
    }

    override fun useLightMode(): Boolean {
        return false;
    }

    override fun initOthers() {
        mToolBarDelegate.setMidTitle(String.format("%s的订单", mItem?.nickname ?: "用户"))
    }

    override fun autoRefresh(): Boolean {
        return true;
    }

    override fun onSetOrderCount(memberOrder: MemberOrderBean) {
        userTotalBuyCountTv.setText(String.format("%s次", memberOrder?.totalOrderNum));
        userTotalAmountTv.setText(String.format("¥%s", memberOrder?.totalOrderPrice));

        userMonthBuyCountTv.setText(String.format("%s次", memberOrder?.monthOrderNum));
        userMonthAmountTv.setText(String.format("¥%s", memberOrder?.monthOrderPrice));

    }

    /**
     * 执行分页请求
     */
    override fun executePageRequest(page: IPage) {
        mPresenter.getOrderCount(mItem?.memberId.toString());
        mPresenter?.loadListData(page, mItem?.memberId.toString(), mAdapter.data);
    }
}