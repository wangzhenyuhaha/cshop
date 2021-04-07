package com.lingmiao.shop.business.sales

import com.blankj.utilcode.util.ActivityUtils
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.james.common.base.loadmore.BaseLoadMoreActivity
import com.james.common.base.loadmore.core.IPage
import com.lingmiao.shop.R
import com.lingmiao.shop.business.sales.adapter.OrderItemAdapter
import com.lingmiao.shop.business.sales.adapter.SalesAdapter
import com.lingmiao.shop.business.sales.adapter.UserOrderAdapter
import com.lingmiao.shop.business.sales.bean.OrderItemVo
import com.lingmiao.shop.business.sales.bean.UserOrderVo
import com.lingmiao.shop.business.sales.bean.UserVo
import com.lingmiao.shop.business.sales.presenter.ISalesSettingPresenter
import com.lingmiao.shop.business.sales.presenter.IUserOrderPresenter
import com.lingmiao.shop.business.sales.presenter.impl.UserOrderPreImpl
import kotlinx.android.synthetic.main.sales_activity_user_order.*

/**
Create Date : 2021/3/101:22 AM
Auther      : Fox
Desc        :
 **/
class UserOrderDetailActivity : BaseLoadMoreActivity<UserOrderVo, IUserOrderPresenter>(), IUserOrderPresenter.PubView {

    override fun initAdapter(): BaseQuickAdapter<UserOrderVo, BaseViewHolder> {
        return UserOrderAdapter().apply {
            setOnItemChildClickListener { adapter, view, position ->
                when(view.id) {
                    R.id.salesEditTv -> {
                        ActivityUtils.startActivity(SalesActivityEditActivity::class.java);
                    }
                }
            }
            setOnItemClickListener { adapter, view, position ->

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
        mToolBarDelegate.setMidTitle(getString(R.string.user_order_title))
    }

    override fun autoRefresh(): Boolean {
        return true;
    }
    /**
     * 执行分页请求
     */
    override fun executePageRequest(page: IPage) {
        mPresenter?.loadListData(page, mAdapter.data);
    }
}