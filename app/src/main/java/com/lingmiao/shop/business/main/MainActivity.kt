package com.lingmiao.shop.business.main

import android.content.Intent
import android.os.Bundle
import android.view.Gravity
import android.view.KeyEvent
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.blankj.utilcode.util.AppUtils
import com.blankj.utilcode.util.BarUtils
import com.blankj.utilcode.util.LogUtils
import com.blankj.utilcode.util.ToastUtils
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import com.james.common.utils.exts.doIntercept
import com.james.common.utils.permission.interceptor.StorageInterceptor
import com.lingmiao.shop.R
import com.lingmiao.shop.base.IConstant
import com.lingmiao.shop.business.main.bean.ApplyShopInfoEvent
import com.lingmiao.shop.business.main.bean.TabChangeEvent
import com.lingmiao.shop.business.main.fragment.NewMainFragment
import com.lingmiao.shop.business.me.fragment.NewMyFragment
import com.lingmiao.shop.business.order.bean.OrderTabChangeEvent
import com.lingmiao.shop.business.order.fragment.OrderTabFragment
import com.lingmiao.shop.util.OtherUtils
import com.lingmiao.shop.util.VoiceUtils
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import java.util.*

class MainActivity : AppCompatActivity() {

    private val fragmentList: ArrayList<Fragment> = ArrayList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        initView()
        initData()
    }


    private fun initData() {
        EventBus.getDefault().register(this)
        OtherUtils.setJpushAlias()
        doIntercept(StorageInterceptor(this), failed = {}) {

        }
    }

    private fun initView() {
        ToastUtils.setGravity(Gravity.CENTER, 0, 0)
        BarUtils.transparentStatusBar(this)
        val statusBarHeight = BarUtils.getStatusBarHeight()
        viStatusBar.layoutParams.height = statusBarHeight
        viStatusBar.setBackgroundColor(ContextCompat.getColor(this, R.color.primary))
//        BarUtils.setStatusBarLightMode(this, false)
        val mainFragment = NewMainFragment.newInstance(true)
        mainFragment.setFromMain(true)
        fragmentList.clear()
        fragmentList.add(mainFragment)
        fragmentList.add(OrderTabFragment.newInstance())
//        fragmentList.add(GoodsNewFragment.newInstance())
        fragmentList.add(NewMyFragment.newInstance())

        val titleList = ArrayList<String>()
        titleList.add("工作台")
        titleList.add("订单")
        titleList.add("我的")
        val tabIconList = ArrayList<Int>()
        tabIconList.add(R.drawable.selector_tab_1)
//        tabIconList.add(R.drawable.selector_tab_3)
        tabIconList.add(R.drawable.selector_tab_2)
        tabIconList.add(R.drawable.selector_tab_4)



        vpMain.adapter = object : FragmentStateAdapter(this) {
            override fun getItemCount(): Int {
                return fragmentList.size
            }

            override fun createFragment(position: Int): Fragment {
                return fragmentList[position]
            }
        }
        tbMain.tabMode = TabLayout.MODE_FIXED
        TabLayoutMediator(tbMain, vpMain, true, false) { tab, position ->
            tab.setCustomView(R.layout.tab_main)
            val tvTitle = tab.customView?.findViewById<TextView>(R.id.tvTitle)
            val ivIcon = tab.customView?.findViewById<ImageView>(R.id.ivIcon)
            tvTitle?.text = titleList[position]
            ivIcon?.setImageResource(tabIconList[position])
        }.attach()
        vpMain.isUserInputEnabled = false
        vpMain.offscreenPageLimit = 1
        tbMain.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab) {
//                if(tab.position == 0) {
//                    EventBus.getDefault().post(ApplyShopInfoEvent());
//                }
//                if(tab.position==2){
////                    BarUtils.setStatusBarLightMode(this@MainActivity, false)
//                    viStatusBar.setBackgroundColor(ContextCompat.getColor(this@MainActivity,R.color.color_secondary))
//                }else{
//                    viStatusBar.setBackgroundColor(ContextCompat.getColor(this@MainActivity,R.color.primary))
////                    BarUtils.setStatusBarLightMode(this@MainActivity, true)
//                }
            }

            override fun onTabReselected(tab: TabLayout.Tab?) {
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {
            }
        })

//      极光推送   支付新订单跳转到订单列表待发货界面
        val jpushType: String? = intent.extras?.getString(IConstant.JPUSH_TYPE)
        LogUtils.d("jpushType:$jpushType")
        if (jpushType == IConstant.MESSAGE_ORDER_PAY_SUCCESS) {
            changeTabPosition(TabChangeEvent(IConstant.TAB_WAIT_SEND_GOODS))
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        EventBus.getDefault().unregister(this)
        VoiceUtils.release()
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun changeTabPosition(event: TabChangeEvent) {
        tbMain.selectTab(tbMain.getTabAt(1))
        MainScope().launch {
            delay(100)
            EventBus.getDefault().post(OrderTabChangeEvent(event.type))
        }
    }

    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent);
        setIntent(intent);
    }

    //双击退出应用
    private var mExitTime: Long = 0
    override fun onKeyDown(keyCode: Int, event: KeyEvent?): Boolean {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if(vpMain.currentItem > 0) {
                vpMain.currentItem = 0;
                return true
            }
            if(System.currentTimeMillis() - mExitTime > 2000) {
                ToastUtils.showLong("再按一次返回桌面")
                mExitTime = System.currentTimeMillis()
            } else {
                AppUtils.exitApp();
            }
            return true
        }
        return super.onKeyDown(keyCode, event)
    }

}