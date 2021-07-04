package com.lingmiao.shop.business.goods.fragment

import android.view.View
import androidx.fragment.app.Fragment
import androidx.viewpager.widget.ViewPager
import com.lingmiao.shop.R
import com.lingmiao.shop.business.goods.GroupManagerLv1Activity
import com.lingmiao.shop.business.goods.GoodsSearchActivity
import com.lingmiao.shop.business.goods.adapter.GoodsHomePageAdapter
import com.lingmiao.shop.business.goods.api.bean.DashboardDataVo
import com.lingmiao.shop.business.goods.event.GoodsHomeTabEvent
import com.lingmiao.shop.business.goods.event.RefreshGoodsStatusEvent
import com.lingmiao.shop.business.goods.presenter.GoodsTabNumberPre
import com.lingmiao.shop.business.goods.presenter.impl.GoodsTabNumberPreImpl
import com.lingmiao.shop.widget.IGoodsTabView
import com.james.common.base.BaseFragment
import com.lingmiao.shop.business.goods.GoodsPublishNewActivity
import kotlinx.android.synthetic.main.goods_fragment_goods_home.*
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import java.util.ArrayList


class GoodsNewFragment : BaseFragment<GoodsTabNumberPre>(), ViewPager.OnPageChangeListener, GoodsTabNumberPre.View {

    companion object {
        const val GOODS_STATUS_ALL = 1
        const val GOODS_STATUS_ENABLE = 2
        const val GOODS_STATUS_WAITING = 3
        const val GOODS_STATUS_DISABLE = 4
        const val GOODS_STATUS_SOLD_OUT = 5
        const val GOODS_STATUE_WARNING = 6

        fun newInstance(): GoodsNewFragment {
            return GoodsNewFragment()
        }
    }

    private val tvTabList: ArrayList<IGoodsTabView> = ArrayList()

    private var mTabTitles = arrayOf("全部", "已上架", "待上架", "已下架", "已售罄")

    override fun getLayoutId(): Int{
        return R.layout.goods_fragment_goods_home
    }

    override fun createPresenter(): GoodsTabNumberPre {
        return GoodsTabNumberPreImpl(this)
    }

    override fun useEventBus(): Boolean {
        return true
    }

    override fun initViewsAndData(rootView: View) {
        initToolbar()
        initTabLayout()
        mPresenter?.loadNumbers()
    }

    private fun initToolbar() {
        groupIv.setOnClickListener {
            GroupManagerLv1Activity.openActivity(mContext)
        }
        searchIv.setOnClickListener {
            GoodsSearchActivity.openActivity(mContext)
        }
        addGoodsTv.setOnClickListener {
            GoodsPublishNewActivity.openActivity(mContext, null)
        }
    }

    private fun initTabLayout() {
        tvTabAll.setOnClickListener {
            viewPager.currentItem = 0
            EventBus.getDefault().post(RefreshGoodsStatusEvent(GOODS_STATUS_ALL))
        }
        tvTabSelling.setOnClickListener {
            viewPager.currentItem = 1
            EventBus.getDefault().post(RefreshGoodsStatusEvent(GOODS_STATUS_ENABLE))
        }
        tvTabWaiting.setOnClickListener {
            viewPager.currentItem = 2
            EventBus.getDefault().post(RefreshGoodsStatusEvent(GOODS_STATUS_WAITING))
        }
        tvTabOffLine.setOnClickListener {
            viewPager.currentItem = 3
            EventBus.getDefault().post(RefreshGoodsStatusEvent(GOODS_STATUS_DISABLE))
        }
        tvTabSoldOut.setOnClickListener {
            viewPager.currentItem = 4
            EventBus.getDefault().post(RefreshGoodsStatusEvent(GOODS_STATUS_SOLD_OUT))
        }

        tvTabList.add(tvTabAll)
        tvTabList.add(tvTabSelling)
        tvTabList.add(tvTabWaiting)
        tvTabList.add(tvTabOffLine)
        tvTabList.add(tvTabSoldOut)

        val fragments = mutableListOf<Fragment>()
        fragments.add(GoodsStatusNewFragment.newInstance(GOODS_STATUS_ALL))
        fragments.add(GoodsStatusNewFragment.newInstance(GOODS_STATUS_ENABLE))
        fragments.add(GoodsStatusNewFragment.newInstance(GOODS_STATUS_WAITING))
        fragments.add(GoodsStatusNewFragment.newInstance(GOODS_STATUS_DISABLE))
        fragments.add(GoodsStatusNewFragment.newInstance(GOODS_STATUS_SOLD_OUT))
        val fragmentAdapter = GoodsHomePageAdapter(childFragmentManager, fragments, mTabTitles)
        viewPager.adapter = fragmentAdapter
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

    override fun loadNumberSuccess(vo : DashboardDataVo) {
//        tvTabAll.setTabNumber(0)
//        tvTabSelling.setTabNumber(vo?.marketGoods!!);
//        tvTabWaiting.setTabNumber(vo?.disabledGoods!!);
//        tvTabOffLine.setTabNumber(vo?.pendingGoods!!);
//        tvTabSoldOut.setTabNumber(0)
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onTabChangeEvent(event: GoodsHomeTabEvent) {
        viewPager.setCurrentItem(event.getNewTabIndex(), false)
    }

//    @Subscribe(threadMode = ThreadMode.MAIN)
//    fun refreshSingleGoodsTabNumber(event: GoodsNumberEvent) {
//        when (event.status) {
//            GOODS_STATUS_ALL -> {
//                tvTabAll.setTabNumber(event.number)
//            }
//            GOODS_STATUS_ENABLE -> {
//                tvTabSelling.setTabNumber(event.number)
//            }
//            GOODS_STATUS_WAITING -> {
//                tvTabWaiting.setTabNumber(event.number)
//            }
//            GOODS_STATUS_DISABLE -> {
//                tvTabOffLine.setTabNumber(event.number)
//            }
//            GOODS_STATUS_SOLD_OUT -> {
//                tvTabSoldOut.setTabNumber(event.number)
//            }
//        }
//    }

}