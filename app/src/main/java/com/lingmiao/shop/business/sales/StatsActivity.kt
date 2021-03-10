package com.lingmiao.shop.business.sales

import androidx.fragment.app.Fragment
import com.james.common.base.BaseActivity
import com.lingmiao.shop.R
import com.lingmiao.shop.business.goods.adapter.GoodsHomePageAdapter
import com.lingmiao.shop.business.sales.fragment.UserStatusFragment
import com.lingmiao.shop.business.sales.presenter.IStatsPresenter
import com.lingmiao.shop.business.sales.presenter.IUserManagerPresenter
import com.lingmiao.shop.business.sales.presenter.impl.StatsPreImpl
import com.lingmiao.shop.business.sales.presenter.impl.UserManagerPreImpl
import kotlinx.android.synthetic.main.tools_activity_logistics_tool.*

/**
Create Date : 2021/3/101:09 AM
Auther      : Fox
Desc        :
 **/
class StatsActivity  : BaseActivity<IStatsPresenter>(), IStatsPresenter.PubView {

    private var mTabTitles = arrayOf("置顶菜单", "常用菜单")

    override fun getLayoutId(): Int {
        return R.layout.tools_activity_logistics_tool;
    }

    override fun useLightMode(): Boolean {
        return false
    }

    override fun useBaseLayout(): Boolean {
        return false;
    }

    override fun createPresenter(): IStatsPresenter {
        return StatsPreImpl(this, this);
    }

    override fun initView() {

        initTitle();

        initTabLayout();
    }

    private fun initTitle() {
        toolbarView?.apply {
            setTitleContent(getString(R.string.user_manager_title))
        }
    }

    private fun initTabLayout() {
        val fragments = mutableListOf<Fragment>()
        fragments.add(UserStatusFragment.all())
        fragments.add(UserStatusFragment.new())

        val fragmentAdapter = GoodsHomePageAdapter(supportFragmentManager, fragments, mTabTitles)
        viewPager.setAdapter(fragmentAdapter)
        tabLayout.setViewPager(viewPager)
    }

}