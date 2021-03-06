package com.lingmiao.shop.business.goods

import androidx.fragment.app.Fragment
import com.james.common.base.BaseActivity
import com.james.common.base.BasePreImpl
import com.james.common.base.BasePresenter
import com.james.common.base.BaseView
import com.lingmiao.shop.R
import com.lingmiao.shop.business.goods.adapter.GoodsHomePageAdapter
import com.lingmiao.shop.business.goods.fragment.TopMenuFragment
import com.lingmiao.shop.business.goods.fragment.UserMenuFragment
import kotlinx.android.synthetic.main.tools_activity_logistics_tool.*

/**
Create Date : 2021/3/1012:48 AM
Author      : Fox
Desc        :
 **/
class MenuManagerActivity : BaseActivity<BasePresenter>(), BaseView {

    private var mTabTitles = arrayOf("置顶菜单", "常用菜单")

    override fun getLayoutId() = R.layout.sales_activity_stats

    override fun useLightMode() = false

    override fun createPresenter() = BasePreImpl(this)

    override fun initView() {
        //设置页面标题
        initTitle()
        //
        initTabLayout()
    }

    private fun initTitle() {
        mToolBarDelegate.setMidTitle(getString(R.string.goods_menu_title))
    }

    private lateinit var topMenu: TopMenuFragment
    private lateinit var usedMenu: UserMenuFragment

    private fun initTabLayout() {
        val fragments = mutableListOf<Fragment>()
        topMenu = TopMenuFragment.newInstance(1)
        usedMenu = UserMenuFragment.newInstance(0)
        fragments.add(topMenu)
        fragments.add(usedMenu)
        val fragmentAdapter = GoodsHomePageAdapter(supportFragmentManager, fragments, mTabTitles)
        viewPager.adapter = fragmentAdapter
        tabLayout.setViewPager(viewPager)
    }

    override fun onBackPressed() {
        if (viewPager.currentItem == 0) {
            if (topMenu.isBatchModel()) {
                topMenu.setFinishSort()
                return
            }
        } else {
            if (usedMenu.isBatchModel()) {
                usedMenu.setFinishSort()
                return
            }
        }
        super.onBackPressed()
    }

}