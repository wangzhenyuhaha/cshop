package com.lingmiao.shop.business.sales

import androidx.fragment.app.Fragment
import com.james.common.base.BaseActivity
import com.lingmiao.shop.R
import com.lingmiao.shop.business.goods.adapter.GoodsHomePageAdapter
import com.lingmiao.shop.business.sales.fragment.UserStatusFragment
import com.lingmiao.shop.business.sales.presenter.IUserManagerPresenter
import com.lingmiao.shop.business.sales.presenter.impl.UserManagerPreImpl
import kotlinx.android.synthetic.main.goods_fragment_goods_top_menu.*
import kotlinx.android.synthetic.main.tools_activity_logistics_tool.*

/**
Create Date : 2021/3/101:08 AM
Auther      : Fox
Desc        :
 **/
class UserManagerActivity : BaseActivity<IUserManagerPresenter>(), IUserManagerPresenter.PubView {

    private var mTabTitles = arrayOf("全部用户", "新增用户")

    override fun getLayoutId(): Int {
        return R.layout.sales_activity_stats;
    }

    override fun useLightMode(): Boolean {
        return false
    }

    override fun createPresenter(): IUserManagerPresenter {
        return UserManagerPreImpl(this, this);
    }

    override fun initView() {

        initTitle();

        initTabLayout();
    }

    private fun initTitle() {
        mToolBarDelegate.setMidTitle(getString(R.string.user_manager_title));
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