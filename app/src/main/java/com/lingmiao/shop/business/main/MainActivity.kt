package com.lingmiao.shop.business.main

import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.Gravity
import android.view.KeyEvent
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.viewModels
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
import com.lingmiao.shop.business.main.bean.TabChangeEvent
import com.lingmiao.shop.business.main.fragment.NewMainFragment
import com.lingmiao.shop.business.me.fragment.NewMyFragment
import com.lingmiao.shop.business.order.bean.OrderTabChangeEvent
import com.lingmiao.shop.business.order.fragment.OrderTabFragment
import com.lingmiao.shop.printer.PrinterModule
import com.lingmiao.shop.printer.PrinterPermission
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

    private val viewModel by viewModels<MainViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        initView()
        initData()
        initPrintSetting();
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
        titleList.add("?????????")
        titleList.add("??????")
        titleList.add("??????")
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

        // ????????????   ???????????????????????????????????????????????????
        val jpushType: String? = intent.extras?.getString(IConstant.JPUSH_TYPE)
        LogUtils.d("jpushType:$jpushType")
        if (jpushType == IConstant.MESSAGE_ORDER_PAY_SUCCESS) {
            changeTabPosition(TabChangeEvent(IConstant.TAB_WAIT_SEND_GOODS))
        } else if (jpushType == IConstant.MESSAGE_ORDER_CANCEL) {
            changeTabPosition(TabChangeEvent(IConstant.TAB_WAIT_REFUND))
        }
    }

    // ??????????????????
    // 1???????????????
    // 2?????????????????????
    private fun initPrintSetting() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (!PrinterPermission.isBluetoothPermissionGranted(this)) {
                PrinterPermission.askForBluetoothPermissions(this)
            } else {
                handPrintSetting()
            }
        } else {
            handPrintSetting()
        }
    }

    private fun handPrintSetting() {
        if (PrinterPermission.isBluetoothEnable()) {
            // ???????????????????????????
            PrinterModule.bind(this)
        } else {
            PrinterPermission.checkBluetoothEnable(this);
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == RESULT_OK && requestCode == PrinterPermission.PERMISSION_ASK) {
            handPrintSetting()
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String?>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            PrinterPermission.PERMISSION_ASK -> if (grantResults[0] == PackageManager.PERMISSION_GRANTED && grantResults[1] == PackageManager.PERMISSION_GRANTED) {
                // all requested permissions were granted
                // perform your task here
                handPrintSetting()
            } else {
                // permissions not granted
                // DO NOT PERFORM THE TASK, it will fail/crash
            }
            else -> super.onRequestPermissionsResult(requestCode, permissions, grantResults)
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

    //??????????????????
    private var mExitTime: Long = 0
    override fun onKeyDown(keyCode: Int, event: KeyEvent?): Boolean {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (vpMain.currentItem > 0) {
                vpMain.currentItem = 0;
                return true
            }
            if (System.currentTimeMillis() - mExitTime > 2000) {
                ToastUtils.showLong("????????????????????????")
                mExitTime = System.currentTimeMillis()
            } else {
                AppUtils.exitApp();
            }
            return true
        }
        return super.onKeyDown(keyCode, event)
    }

}