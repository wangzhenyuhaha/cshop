package com.lingmiao.shop.business.me

import android.app.Activity
import android.content.Intent
import android.view.MotionEvent
import androidx.fragment.app.Fragment
import com.blankj.utilcode.util.KeyboardUtils
import com.james.common.base.BaseActivity
import com.lingmiao.shop.R
import com.lingmiao.shop.business.goods.adapter.GoodsHomePageAdapter
import com.lingmiao.shop.business.main.bean.ApplyShopInfo
import com.lingmiao.shop.business.me.fragment.DeliveryInTimeFragment
import com.lingmiao.shop.business.me.fragment.DeliveryOfRiderFragment
import com.lingmiao.shop.business.me.presenter.ManagerSettingPresenter
import com.lingmiao.shop.business.me.presenter.impl.ManagerSettingPresenterImpl
import com.lingmiao.shop.business.sales.SalesActivityEditActivity
import com.lingmiao.shop.business.sales.bean.SalesVo
import com.lingmiao.shop.business.tools.bean.FreightVoItem
import kotlinx.android.synthetic.main.tools_activity_logistics_tool.*

/**
Create Date : 2021/3/24:05 PM
Auther      : Fox
Desc        :
 **/
class DeliveryManagerActivity : BaseActivity<ManagerSettingPresenter>(), ManagerSettingPresenter.View  {

    private var mTabTitles = arrayOf("商家配送", "骑手配送")

    var mViewType : Int? = 0

    var mItem : FreightVoItem? = null;

    companion object {

        const val KEY_ITEM = "KEY_ITEM"

        const val KEY_VIEW_TYPE = "KEY_VIEW_TYPE"

        fun shop(context: Activity, item: FreightVoItem?) {
            val intent = Intent(context, DeliveryManagerActivity::class.java)
            intent.putExtra(KEY_ITEM, item)
            intent.putExtra(KEY_VIEW_TYPE, 1)
            context.startActivity(intent)
        }

        fun rider(context: Activity, item: FreightVoItem?) {
            val intent = Intent(context, DeliveryManagerActivity::class.java)
            intent.putExtra(KEY_ITEM, item)
            intent.putExtra(KEY_VIEW_TYPE, 2)
            context.startActivity(intent)
        }
    }

    override fun initBundles() {
        mItem = intent?.getSerializableExtra(KEY_ITEM) as FreightVoItem?;
        mViewType = intent?.getIntExtra(KEY_VIEW_TYPE, 1);
    }

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

    override fun onLoadedShopInfo(bean: ApplyShopInfo) {

    }

    private fun initTabLayout() {
        val fragments = mutableListOf<Fragment>()
        fragments.add(DeliveryInTimeFragment.newInstance(mItem))
        fragments.add(DeliveryOfRiderFragment.newInstance(mItem))

        val fragmentAdapter = GoodsHomePageAdapter(supportFragmentManager, fragments, mTabTitles)
        viewPager.setAdapter(fragmentAdapter)
        tabLayout.setViewPager(viewPager)

        when(mViewType) {
            2 -> {
                viewPager.currentItem = 1;
            }
            else -> {
                viewPager.currentItem = 0;
            }
        }
    }

}