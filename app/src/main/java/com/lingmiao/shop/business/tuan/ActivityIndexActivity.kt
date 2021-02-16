package com.lingmiao.shop.business.tuan

import androidx.fragment.app.Fragment
import com.lingmiao.shop.R
import com.lingmiao.shop.business.goods.adapter.GoodsHomePageAdapter
import com.lingmiao.shop.business.tuan.fragment.ActivityListFragment
import com.lingmiao.shop.business.tuan.presenter.ActivitiesPresenter
import com.lingmiao.shop.business.tuan.presenter.impl.ActivitiesPresenterImpl
import com.james.common.base.BaseActivity
import kotlinx.android.synthetic.main.tools_activity_logistics_tool.*

class ActivityIndexActivity : BaseActivity<ActivitiesPresenter>(), ActivitiesPresenter.View {

    private var mTabTitles = arrayOf("全部团购活动", "我报名的活动")

    override fun getLayoutId(): Int {
        return R.layout.tools_activity_logistics_tool;
    }

    override fun createPresenter(): ActivitiesPresenter {
        return ActivitiesPresenterImpl(this);
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
            setTitleContent(getString(R.string.tuan_title_activity_index))
        }
    }

    private fun initTabLayout() {
        val fragments = mutableListOf<Fragment>()
        fragments.add(ActivityListFragment.allActivities())
        fragments.add(ActivityListFragment.myActivities())

        val fragmentAdapter = GoodsHomePageAdapter(supportFragmentManager, fragments, mTabTitles)
        viewPager.setAdapter(fragmentAdapter)
        tabLayout.setViewPager(viewPager)
    }


}