package com.lingmiao.shop.business.goods

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.view.View
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.viewpager.widget.ViewPager
import com.blankj.utilcode.util.ActivityUtils
import com.james.common.base.BaseActivity
import com.lingmiao.shop.R
import com.lingmiao.shop.business.goods.adapter.GoodsHomePageAdapter
import com.lingmiao.shop.business.goods.api.bean.DashboardDataVo
import com.lingmiao.shop.business.goods.event.GoodsHomeTabEvent
import com.lingmiao.shop.business.goods.event.RefreshGoodsStatusEvent
import com.lingmiao.shop.business.goods.fragment.GoodsNewFragment
import com.lingmiao.shop.business.goods.fragment.GoodsStatusNewFragment
import com.lingmiao.shop.business.goods.presenter.GoodsTabNumberPre
import com.lingmiao.shop.business.goods.presenter.impl.GoodsTabNumberPreImpl
import com.lingmiao.shop.widget.IGoodsTabView
import kotlinx.android.synthetic.main.goods_activity_goods_list.*
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import java.util.*

/**
Create Date : 2021/3/99:40 AM
Auther      : Fox
Desc        :
 **/
class GoodsListActivity : BaseActivity<GoodsTabNumberPre>(), GoodsTabNumberPre.View,
    ViewPager.OnPageChangeListener {

    companion object {

        const val GOODS_STATUS_ALL = 1

        const val GOODS_STATUS_ENABLE = 2

        const val GOODS_STATUS_WAITING = 3

        const val GOODS_STATUS_DISABLE = 4

        const val GOODS_STATUS_SOLD_OUT = 5

        const val KEY_DESC = "KEY_DESC"

        fun openActivity(context: Context) {
            if (context is Activity) {
                val intent = Intent(context, GoodsListActivity::class.java)
//                intent.putExtra(KEY_DESC, content)
                context.startActivity(intent)
            }
        }
    }

    override fun createPresenter(): GoodsTabNumberPre {
        return GoodsTabNumberPreImpl(this)
    }

    private var mTabTitles = arrayOf("全部", "已上架", "待上架", "已下架", "已售罄")

    private val tvTabList: ArrayList<IGoodsTabView> = ArrayList()

    override fun getLayoutId() = R.layout.goods_activity_goods_list


    override fun useLightMode() = false


    override fun useEventBus() = true


    override fun initView() {
        initTitle()
        initTabLayout()
        mPresenter?.loadNumbers()
    }

    private fun initTitle() {
        mToolBarDelegate.setMidTitle(getString(R.string.goods_manager_title))
        mToolBarDelegate.setRightText(
            "新增",
            ContextCompat.getColor(context, R.color.white),
            {
                ActivityUtils.startActivity(GoodsPublishTypeActivity::class.java)
            })
    }

    private fun initTabLayout() {
        tvTabAll.setOnClickListener {
            viewPager.currentItem = 0
            EventBus.getDefault().post(RefreshGoodsStatusEvent(GoodsNewFragment.GOODS_STATUS_ALL))
        }
        tvTabSelling.setOnClickListener {
            viewPager.currentItem = 1
            EventBus.getDefault()
                .post(RefreshGoodsStatusEvent(GoodsNewFragment.GOODS_STATUS_ENABLE))
        }
        tvTabWaiting.setOnClickListener {
            viewPager.currentItem = 2
            EventBus.getDefault()
                .post(RefreshGoodsStatusEvent(GoodsNewFragment.GOODS_STATUS_WAITING))
        }
        tvTabOffLine.setOnClickListener {
            viewPager.currentItem = 3
            EventBus.getDefault()
                .post(RefreshGoodsStatusEvent(GoodsNewFragment.GOODS_STATUS_ENABLE))
        }
        tvTabSoldOut.setOnClickListener {
            viewPager.currentItem = 4
            EventBus.getDefault()
                .post(RefreshGoodsStatusEvent(GoodsNewFragment.GOODS_STATUS_SOLD_OUT))
        }

        tvTabList.add(tvTabAll)
        tvTabList.add(tvTabSelling)
        tvTabList.add(tvTabWaiting)
        tvTabList.add(tvTabOffLine)
        tvTabList.add(tvTabSoldOut)

        val fragments = mutableListOf<Fragment>()
        fragments.add(GoodsStatusNewFragment.newInstance(GoodsNewFragment.GOODS_STATUS_ALL))
        fragments.add(GoodsStatusNewFragment.newInstance(GoodsNewFragment.GOODS_STATUS_ENABLE))
        fragments.add(GoodsStatusNewFragment.newInstance(GoodsNewFragment.GOODS_STATUS_WAITING))
        fragments.add(GoodsStatusNewFragment.newInstance(GoodsNewFragment.GOODS_STATUS_DISABLE))
        fragments.add(GoodsStatusNewFragment.newInstance(GoodsNewFragment.GOODS_STATUS_SOLD_OUT))
        val fragmentAdapter = GoodsHomePageAdapter(supportFragmentManager, fragments, mTabTitles)
        viewPager.setAdapter(fragmentAdapter)
        viewPager.addOnPageChangeListener(this)
        viewPager.offscreenPageLimit = 1

    }


    override fun onPageScrollStateChanged(state: Int) {

    }

    override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {

    }

    override fun onPageSelected(position: Int) {
        setTabStatus(position)

        refreshListData()
    }

    private fun setTabStatus(tabIndex: Int) {
        tvTabAll.setTabSelected(false)
        tvTabSelling.setTabSelected(false)
        tvTabWaiting.setTabSelected(false)
        tvTabOffLine.setTabSelected(false)
        tvTabSoldOut.setTabSelected(false)
        viewPager.currentItem = tabIndex
        tvTabList[tabIndex].setTabSelected(true)
    }

    private fun refreshListData() {
        EventBus.getDefault().post(RefreshGoodsStatusEvent(viewPager.currentItem))
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onTabChangeEvent(event: GoodsHomeTabEvent) {
        viewPager.setCurrentItem(event.getNewTabIndex(), false)

        refreshListData()
    }

//    @Subscribe(threadMode = ThreadMode.MAIN)
//    fun refreshSingleGoodsTabNumber(event: GoodsNumberEvent) {
//        when (event.status) {
//            GoodsNewFragment.GOODS_STATUS_ALL -> {
//                tvTabAll.setTabNumber(event.number)
//            }
//            GoodsNewFragment.GOODS_STATUS_ENABLE -> {
//                tvTabSelling.setTabNumber(event.number)
//            }
//            GoodsNewFragment.GOODS_STATUS_WAITING -> {
//                tvTabWaiting.setTabNumber(event.number)
//            }
//            GoodsNewFragment.GOODS_STATUS_DISABLE -> {
//                tvTabOffLine.setTabNumber(event.number)
//            }
//            GoodsNewFragment.GOODS_STATUS_SOLD_OUT -> {
//                tvTabSoldOut.setTabNumber(event.number)
//            }
//        }
//    }

    override fun loadNumberSuccess(vo: DashboardDataVo) {

    }

}