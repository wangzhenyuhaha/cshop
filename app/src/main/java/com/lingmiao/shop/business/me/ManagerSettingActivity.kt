package com.lingmiao.shop.business.me

import androidx.fragment.app.Fragment
import com.james.common.base.BaseActivity
import com.lingmiao.shop.R
import com.lingmiao.shop.business.goods.adapter.GoodsHomePageAdapter
import com.lingmiao.shop.business.me.fragment.ShopBaseSettingFragment
import com.lingmiao.shop.business.me.fragment.ShopOperateSettingFragment
import com.lingmiao.shop.business.me.presenter.ManagerSettingPresenter
import com.lingmiao.shop.business.me.presenter.impl.ManagerSettingPresenterImpl
import kotlinx.android.synthetic.main.tools_activity_logistics_tool.*

/**
Create Date : 2021/3/24:05 PM
Auther      : Fox
Desc        :
 **/
class ManagerSettingActivity : BaseActivity<ManagerSettingPresenter>(), ManagerSettingPresenter.View  {

    private var mTabTitles = arrayOf("基础", "运营")

    override fun getLayoutId(): Int {
        return R.layout.sales_activity_stats;
    }

    override fun useLightMode(): Boolean {
        return false
    }

    override fun createPresenter(): ManagerSettingPresenter {
        return ManagerSettingPresenterImpl(this)
    }

    override fun initView() {

        initTitle();

        initTabLayout();
    }

    private fun initTitle() {
        mToolBarDelegate.setMidTitle(getString(R.string.manager_setting_title))
    }

    private fun initTabLayout() {
        val fragments = mutableListOf<Fragment>()
        fragments.add(ShopBaseSettingFragment.newInstance())
        fragments.add(ShopOperateSettingFragment.newInstance())

        val fragmentAdapter = GoodsHomePageAdapter(supportFragmentManager, fragments, mTabTitles)
        viewPager.setAdapter(fragmentAdapter)
        tabLayout.setViewPager(viewPager)
    }

}