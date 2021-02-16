package com.lingmiao.shop.business.order.fragment

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import androidx.viewpager.widget.ViewPager
import com.blankj.utilcode.util.LogUtils
import com.lingmiao.shop.BuildConfig
import com.lingmiao.shop.R
import com.lingmiao.shop.base.IConstant
import com.lingmiao.shop.business.main.bean.TabChangeEvent
import com.lingmiao.shop.business.order.ScanOrderActivity
import com.lingmiao.shop.business.order.api.OrderRepository
import com.lingmiao.shop.business.order.bean.OrderNumber
import com.lingmiao.shop.business.order.bean.OrderNumberEvent
import com.lingmiao.shop.business.order.bean.OrderTabChangeEvent
import com.lingmiao.shop.business.order.bean.OrderTabNumberEvent
import com.lingmiao.shop.widget.IOrderTabView
import com.james.common.netcore.coroutine.CoroutineSupport
import com.james.common.netcore.networking.http.core.awaitHiResponse
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.fragment_order.*
import kotlinx.coroutines.launch
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import java.util.ArrayList

class OrderTabFragment : Fragment(), ViewPager.OnPageChangeListener {

    private val tvTabList: ArrayList<IOrderTabView> = ArrayList()
    private val titleList: ArrayList<String> = ArrayList()
    private lateinit var adapter: OrderFragmentAdapter
    private val mCoroutine: CoroutineSupport by lazy { CoroutineSupport() }

    companion object {
        private val fragmentList: ArrayList<Fragment> = ArrayList()
        fun newInstance(): OrderTabFragment {
            return OrderTabFragment()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view: View = inflater.inflate(R.layout.fragment_order, container, false)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
        initData()
    }


    private fun initView() {
        if (!EventBus.getDefault().isRegistered(this)) EventBus.getDefault().register(this)
        adapter = OrderFragmentAdapter(childFragmentManager)
        tvTabList.add(tvTab1)
        tvTabList.add(tvTab2)
        tvTabList.add(tvTab3)
        tvTabList.add(tvTab4)
        tvTabList.add(tvTab5)

        fragmentList.clear()
        //    ALL, WAIT_PAY, WAIT_SHIP, WAIT_ROG, CANCELLED, COMPLETE, WAIT_COMMENT, REFUND, WAIT_REFUN
//        全部ALL
//        待付款WAIT_PAY
//        待发货WAIT_SHIP
//        待收货WAIT_ROG
//        售后/退款 WAIT_REFUND
        fragmentList.add(SingleOrderListFragment.newInstance("ALL"))
        fragmentList.add(SingleOrderListFragment.newInstance("WAIT_PAY"))
        fragmentList.add(SingleOrderListFragment.newInstance("WAIT_SHIP"))
        fragmentList.add(SingleOrderListFragment.newInstance("WAIT_ROG"))
        fragmentList.add(SingleOrderListFragment.newInstance("WAIT_REFUND"))
        vpContent.adapter = adapter
        vpContent.offscreenPageLimit = 4
        vpContent.addOnPageChangeListener(this)

        tvTab1.setOnClickListener { vpContent.currentItem = 0 }
        tvTab2.setOnClickListener { vpContent.currentItem = 1 }
        tvTab3.setOnClickListener { vpContent.currentItem = 2 }
        tvTab4.setOnClickListener { vpContent.currentItem = 3 }
        tvTab5.setOnClickListener { vpContent.currentItem = 4 }

        LogUtils.d("initView:")
        tv_order_scan.setOnClickListener {
            val intent = Intent(context, ScanOrderActivity::class.java);
            startActivity(intent);
        }
    }

    private class OrderFragmentAdapter(fm: FragmentManager) :
        FragmentPagerAdapter(fm) {
        override fun getItem(position: Int): Fragment {
            return fragmentList[position]
        }

        override fun getCount(): Int {
            return fragmentList.size
        }
    }

    override fun onPageScrollStateChanged(p0: Int) {

    }

    override fun onPageScrolled(p0: Int, p1: Float, p2: Int) {
    }

    override fun onPageSelected(i: Int) {
        setTabStatus(i)
    }

    private fun setTabStatus(tabIndex: Int) {
        tvTab1.setTabSelected(false)
        tvTab2.setTabSelected(false)
        tvTab3.setTabSelected(false)
        tvTab4.setTabSelected(false)
        tvTab5.setTabSelected(false)
        tvTabList[tabIndex].setTabSelected(true)
    }

    private fun initData() {
        mCoroutine.launch {
            val resp = OrderRepository.apiService.getOrderStatusNumber().awaitHiResponse()
            if (resp.isSuccess && activity != null && !activity!!.isFinishing) {
                val orderNumber = resp.data
                tvTab2.setTabNumber(orderNumber.waitPayNum)
                tvTab3.setTabNumber(orderNumber.waitShipNum)
                tvTab5.setTabNumber(orderNumber.refundNum)
            }

        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun refreshOrderNumber(event: OrderNumberEvent) {
        initData()
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun refreshSingleOrderTabNumber(event: OrderTabNumberEvent) {
        LogUtils.d("refreshSingleOrderTabNumber")
        when (event?.status) {
            "WAIT_PAY" -> {
                tvTab2?.setTabNumber(event?.number)
            }
            "WAIT_SHIP" -> {
                tvTab3?.setTabNumber(event?.number)
            }
            "WAIT_REFUND" -> {
                tvTab5?.setTabNumber(event?.number)
            }
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun changeTabPosition(event: OrderTabChangeEvent) {
        LogUtils.d("changeTabPosition:" + event.type)
        if (event.type == IConstant.TAB_WAIT_SEND_GOODS) {
            vpContent.currentItem = 2
        } else {
            vpContent.currentItem = 4
        }
    }
}