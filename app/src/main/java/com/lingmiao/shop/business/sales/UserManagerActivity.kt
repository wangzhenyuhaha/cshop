package com.lingmiao.shop.business.sales

import android.app.Activity
import android.content.Context
import android.content.Intent
import androidx.fragment.app.Fragment
import com.james.common.base.BaseActivity
import com.lingmiao.shop.R
import com.lingmiao.shop.base.IConstant
import com.lingmiao.shop.business.goods.adapter.GoodsHomePageAdapter
import com.lingmiao.shop.business.sales.bean.SalesVo
import com.lingmiao.shop.business.sales.fragment.UserStatusFragment
import com.lingmiao.shop.business.sales.presenter.IUserManagerPresenter
import com.lingmiao.shop.business.sales.presenter.impl.UserManagerPreImpl
import kotlinx.android.synthetic.main.tools_activity_logistics_tool.*

/**
Create Date : 2021/3/101:08 AM
Auther      : Fox
Desc        :
 **/
class UserManagerActivity : BaseActivity<IUserManagerPresenter>(), IUserManagerPresenter.PubView {

    private var mTabTitles = arrayOf("全部用户", "新增用户")

    private var mCurrentIndex : Int = ALL_USER;

    companion object {

        private const val NEW_USER = 1;
        private const val ALL_USER = 0;

        fun newUser(context: Context) {
            val intent = Intent(context, UserManagerActivity::class.java)
            intent.putExtra(IConstant.BUNDLE_KEY_OF_VIEW_TYPE, NEW_USER)
            context.startActivity(intent)
        }

        fun allUser(context: Context) {
            val intent = Intent(context, UserManagerActivity::class.java)
            intent.putExtra(IConstant.BUNDLE_KEY_OF_VIEW_TYPE, ALL_USER)
            context.startActivity(intent)
        }
    }

    override fun getLayoutId(): Int {
        return R.layout.sales_activity_stats;
    }

    override fun useLightMode(): Boolean {
        return false
    }

    override fun initBundles() {
        mCurrentIndex = intent?.getIntExtra(IConstant.BUNDLE_KEY_OF_VIEW_TYPE, ALL_USER)?:ALL_USER;
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

        viewPager.currentItem = mCurrentIndex;
    }

}