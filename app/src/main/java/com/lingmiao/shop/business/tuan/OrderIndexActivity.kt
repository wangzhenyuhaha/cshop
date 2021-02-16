package com.lingmiao.shop.business.tuan

import androidx.fragment.app.Fragment
import com.lingmiao.shop.R
import com.lingmiao.shop.business.goods.adapter.GoodsHomePageAdapter
import com.lingmiao.shop.business.tuan.fragment.OrderListFragment
import com.lingmiao.shop.business.tuan.presenter.OrderIndexPresenter
import com.lingmiao.shop.business.tuan.presenter.impl.OrderIndexPresenterImpl
import com.james.common.base.BaseActivity
import kotlinx.android.synthetic.main.tools_activity_logistics_tool.*

class OrderIndexActivity : BaseActivity<OrderIndexPresenter>(), OrderIndexPresenter.View {

    private var mTabTitles = arrayOf("全部", "待确认", "待发货", "待收货", "已完成")

    override fun getLayoutId(): Int {
        return R.layout.tools_activity_logistics_tool;
    }

    override fun createPresenter(): OrderIndexPresenter {
        return OrderIndexPresenterImpl(this);
    }

    override fun useBaseLayout(): Boolean {
        return false
    }

    override fun initView() {

        initTitle();

        initTabLayout();
    }

    private fun initTitle() {
        toolbarView?.apply {
            setTitleContent(getString(R.string.tuan_title_order_index))
        }
    }

    private fun initTabLayout() {
        val fragments = mutableListOf<Fragment>()
        fragments.add(OrderListFragment.allOrder())
        fragments.add(OrderListFragment.confirm())
        fragments.add(OrderListFragment.delivery())
        fragments.add(OrderListFragment.sign())

        val fragmentAdapter = GoodsHomePageAdapter(supportFragmentManager, fragments, mTabTitles)
        viewPager.setAdapter(fragmentAdapter)
        tabLayout.setViewPager(viewPager)
    }

}