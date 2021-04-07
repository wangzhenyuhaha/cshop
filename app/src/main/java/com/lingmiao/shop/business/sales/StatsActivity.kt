package com.lingmiao.shop.business.sales

import android.app.Activity
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.blankj.utilcode.util.BarUtils
import com.james.common.base.BaseActivity
import com.lingmiao.shop.R
import com.lingmiao.shop.business.goods.adapter.GoodsHomePageAdapter
import com.lingmiao.shop.business.sales.fragment.*
import com.lingmiao.shop.business.sales.presenter.IStatsPresenter
import com.lingmiao.shop.business.sales.presenter.IUserManagerPresenter
import com.lingmiao.shop.business.sales.presenter.impl.StatsPreImpl
import com.lingmiao.shop.business.sales.presenter.impl.UserManagerPreImpl
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.tools_activity_logistics_tool.*

/**
Create Date : 2021/3/101:09 AM
Auther      : Fox
Desc        :
 **/
class StatsActivity  : BaseActivity<IStatsPresenter>(), IStatsPresenter.PubView {

    private var mTabTitles = arrayOf("销售", "支付", "商品", "活动", "用户")

    override fun getLayoutId(): Int {
        return R.layout.sales_activity_stats;
    }

    override fun useLightMode(): Boolean {
        return false
    }

    override fun createPresenter(): IStatsPresenter {
        return StatsPreImpl(this, this);
    }

    override fun initView() {

        initTitle();

        initTabLayout();
    }

    private fun initTitle() {
        mToolBarDelegate.setMidTitle(getString(R.string.stats_analysis_title))
    }

    private fun initTabLayout() {
        val fragments = mutableListOf<Fragment>()
        fragments.add(StatsDateSalesFragment.new())
        fragments.add(StatsDatePayFragment.new())
        fragments.add(StatsDateGoodsFragment.new())
        fragments.add(StatsDateActivityFragment.new())
        fragments.add(StatsDateUserFragment.new())

        val fragmentAdapter = GoodsHomePageAdapter(supportFragmentManager, fragments, mTabTitles)
        viewPager.setAdapter(fragmentAdapter)
        tabLayout.setViewPager(viewPager)
    }

}