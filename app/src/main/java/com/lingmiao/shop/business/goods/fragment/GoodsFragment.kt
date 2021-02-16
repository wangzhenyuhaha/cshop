package com.lingmiao.shop.business.goods.fragment

import android.view.View
import androidx.fragment.app.Fragment
import androidx.viewpager.widget.ViewPager
import com.lingmiao.shop.R
import com.lingmiao.shop.business.goods.GoodsPublishActivity
import com.lingmiao.shop.business.goods.GroupManagerLv1Activity
import com.lingmiao.shop.business.goods.GoodsSearchActivity
import com.lingmiao.shop.business.goods.adapter.GoodsHomePageAdapter
import com.lingmiao.shop.business.goods.api.bean.DashboardDataVo
import com.lingmiao.shop.business.goods.event.GoodsHomeTabEvent
import com.lingmiao.shop.business.goods.event.GoodsNumberEvent
import com.lingmiao.shop.business.goods.event.RefreshGoodsStatusEvent
import com.lingmiao.shop.business.goods.presenter.GoodsTabNumberPre
import com.lingmiao.shop.business.goods.presenter.impl.GoodsTabNumberPreImpl
import com.lingmiao.shop.widget.IGoodsTabView
import com.lingmiao.shop.widget.IOrderTabView
import com.james.common.base.BaseFragment
import kotlinx.android.synthetic.main.goods_fragment_goods_home.*
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import java.util.ArrayList


class GoodsFragment : BaseFragment<GoodsTabNumberPre>(), ViewPager.OnPageChangeListener, GoodsTabNumberPre.View {

    companion object {
        const val GOODS_STATUS_ENABLE = 1
        const val GOODS_STATUS_DISABLE = 2
        const val GOODS_STATUS_IS_AUTH = 3

        fun newInstance(): GoodsFragment {
            return GoodsFragment()
        }
    }

    private val tvTabList: ArrayList<IGoodsTabView> = ArrayList();

    private var mTabTitles = arrayOf("出售中", "待审核", "已下架")

    override fun getLayoutId(): Int? {
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
        mPresenter?.loadNumbers();
    }

    private fun initToolbar() {
        groupIv.setOnClickListener {
            GroupManagerLv1Activity.openActivity(mContext)
        }
        searchIv.setOnClickListener {
            GoodsSearchActivity.openActivity(mContext)
        }
        addGoodsTv.setOnClickListener {
            GoodsPublishActivity.openActivity(mContext, null)
        }
    }

    private fun initTabLayout() {
        tvTabSelling.setOnClickListener {
            viewPager.currentItem = 0;
            EventBus.getDefault().post(RefreshGoodsStatusEvent(GOODS_STATUS_ENABLE))
        }
        tvTabWaiting.setOnClickListener {
            viewPager.currentItem = 1;
            EventBus.getDefault().post(RefreshGoodsStatusEvent(GOODS_STATUS_IS_AUTH))
        }
        tvTabOffLine.setOnClickListener {
            viewPager.currentItem = 2;
            EventBus.getDefault().post(RefreshGoodsStatusEvent(GOODS_STATUS_DISABLE))
        }

        tvTabList.add(tvTabSelling)
        tvTabList.add(tvTabWaiting)
        tvTabList.add(tvTabOffLine)

        val fragments = mutableListOf<Fragment>()
        fragments.add(GoodsStatusFragment.newInstance(GOODS_STATUS_ENABLE))
        fragments.add(GoodsStatusFragment.newInstance(GOODS_STATUS_IS_AUTH))
        fragments.add(GoodsStatusFragment.newInstance(GOODS_STATUS_DISABLE))
        val fragmentAdapter = GoodsHomePageAdapter(childFragmentManager, fragments, mTabTitles)
        viewPager.setAdapter(fragmentAdapter)
        viewPager.addOnPageChangeListener(this);
        viewPager.offscreenPageLimit = 1;
    }

    override fun onPageScrollStateChanged(state: Int) {

    }

    override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {

    }

    override fun onPageSelected(position: Int) {

        setTabStatus(position);

        refreshListData();
    }

    private fun setTabStatus(tabIndex: Int) {
        tvTabSelling.setTabSelected(false)
        tvTabWaiting.setTabSelected(false)
        tvTabOffLine.setTabSelected(false)
        viewPager.currentItem = tabIndex;
        tvTabList[tabIndex].setTabSelected(true)
    }

    private fun refreshListData() {
        EventBus.getDefault().post(RefreshGoodsStatusEvent(viewPager.currentItem))
    }

    override fun loadNumberSuccess(vo : DashboardDataVo) {
        tvTabSelling.setTabNumber(vo?.marketGoods!!);
        tvTabWaiting.setTabNumber(vo?.disabledGoods!!);
        tvTabOffLine.setTabNumber(vo?.pendingGoods!!);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onTabChangeEvent(event: GoodsHomeTabEvent) {
        viewPager.setCurrentItem(event.getTabIndex(), false)
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun refreshSingleGoodsTabNumber(event: GoodsNumberEvent) {
        when (event.status) {
            GOODS_STATUS_ENABLE -> {
                tvTabSelling.setTabNumber(event.number)
            }
            GOODS_STATUS_IS_AUTH -> {
                tvTabWaiting.setTabNumber(event.number)
            }
            GOODS_STATUS_DISABLE -> {
                tvTabOffLine.setTabNumber(event.number)
            }
        }
    }

}