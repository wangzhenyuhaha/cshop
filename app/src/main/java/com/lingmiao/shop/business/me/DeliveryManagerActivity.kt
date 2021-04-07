package com.lingmiao.shop.business.me

import android.view.MotionEvent
import androidx.fragment.app.Fragment
import com.blankj.utilcode.util.KeyboardUtils
import com.james.common.base.BaseActivity
import com.lingmiao.shop.R
import com.lingmiao.shop.business.goods.adapter.GoodsHomePageAdapter
import com.lingmiao.shop.business.me.fragment.DeliveryInTimeFragment
import com.lingmiao.shop.business.me.presenter.ManagerSettingPresenter
import com.lingmiao.shop.business.me.presenter.impl.ManagerSettingPresenterImpl
import kotlinx.android.synthetic.main.tools_activity_logistics_tool.*

/**
Create Date : 2021/3/24:05 PM
Auther      : Fox
Desc        :
 **/
class DeliveryManagerActivity : BaseActivity<ManagerSettingPresenter>(), ManagerSettingPresenter.View  {

    private var mTabTitles = arrayOf("即时配送", "预约配送")

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
        mToolBarDelegate.setMidTitle(getString(R.string.delivery_manager_title))
    }

    private fun initTabLayout() {
        val fragments = mutableListOf<Fragment>()
        fragments.add(DeliveryInTimeFragment.newInstance())
        fragments.add(DeliveryInTimeFragment.newInstance())

        val fragmentAdapter = GoodsHomePageAdapter(supportFragmentManager, fragments, mTabTitles)
        viewPager.setAdapter(fragmentAdapter)
        tabLayout.setViewPager(viewPager)
    }

    override fun dispatchTouchEvent(ev: MotionEvent?): Boolean {
        if (ev?.action == MotionEvent.ACTION_DOWN) {
            // 当键盘未关闭时先拦截事件
            if(KeyboardUtils.isSoftInputVisible(context)) {
                KeyboardUtils.hideSoftInput(context);
                return true;
            }
        }
        return super.dispatchTouchEvent(ev)
    }

}