package com.lingmiao.shop.business.me

import android.app.Activity
import android.content.Intent
import android.util.Log
import android.view.MotionEvent
import androidx.fragment.app.Fragment
import com.blankj.utilcode.util.KeyboardUtils
import com.james.common.base.BaseActivity
import com.lingmiao.shop.R
import com.lingmiao.shop.base.IConstant
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
class DeliveryManagerActivity : BaseActivity<ManagerSettingPresenter>(),
    ManagerSettingPresenter.View {

    private var mTabTitles = arrayOf("商家配送", "骑手配送")

    private var mTabTitles2 = arrayOf("商家配送")

    //  1   2
    var mViewType: Int? = 0

    // 0 不显示棋手配送   1显示
    //默认显示骑手配送
    var type: Int = 1

    var mItem: FreightVoItem? = null

    companion object {

        //配送模板
        const val KEY_ITEM = "KEY_ITEM"

        //1商家配送 2骑手配送
        const val KEY_VIEW_TYPE = "KEY_VIEW_TYPE"

        //0不显示棋手配送   1显示骑手配送
        const val KEY_TYPE = "KEY_TYPE"

        fun shop(context: Activity, item: FreightVoItem?, type: Int) {
            val intent = Intent(context, DeliveryManagerActivity::class.java)
            intent.putExtra(KEY_ITEM, item)
            intent.putExtra(KEY_VIEW_TYPE, 1)
            intent.putExtra(KEY_TYPE, type)

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
        //获取模板
        mItem = intent?.getSerializableExtra(KEY_ITEM) as FreightVoItem?
        //获取模板类型，默认为商家配送
        mViewType = intent?.getIntExtra(KEY_VIEW_TYPE, 1)
        if (mViewType == 1) {
            type = intent?.getIntExtra(KEY_TYPE, 1) ?: 1
        }
    }

    override fun getLayoutId() = R.layout.sales_activity_stats


    override fun useLightMode() = false


    override fun createPresenter() = ManagerSettingPresenterImpl(this)


    override fun initView() {

        initTitle()

        initTabLayout()

    }

    private fun initTitle() {
        mToolBarDelegate.setMidTitle(getString(R.string.delivery_manager_title))
    }

    override fun dispatchTouchEvent(ev: MotionEvent?): Boolean {
        if (ev?.action == MotionEvent.ACTION_DOWN) {
            // 当键盘未关闭时先拦截事件
            if (KeyboardUtils.isSoftInputVisible(context)) {
                KeyboardUtils.hideSoftInput(context)
                return true
            }
        }
        return super.dispatchTouchEvent(ev)
    }

    override fun onLoadedShopInfo(bean: ApplyShopInfo) {

    }

    private fun initTabLayout() {
        val fragments = mutableListOf<Fragment>()
        fragments.add(DeliveryInTimeFragment.newInstance(mItem))
        if (type == 1) {
            fragments.add(DeliveryOfRiderFragment.newInstance(mItem))
        }


        val fragmentAdapter = GoodsHomePageAdapter(
            supportFragmentManager,
            fragments,
            if (type != 1) mTabTitles2 else mTabTitles
        )
        viewPager.adapter = fragmentAdapter
        tabLayout.setViewPager(viewPager)

        when (mViewType) {
            2 -> {
                viewPager.currentItem = 1
            }
            else -> {
                viewPager.currentItem = 0
            }
        }
    }

}