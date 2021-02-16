package com.lingmiao.shop.business.main

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import cn.jpush.android.api.JPushInterface
import com.blankj.utilcode.util.BarUtils
import com.blankj.utilcode.util.LogUtils
import com.blankj.utilcode.util.Utils
import com.lingmiao.shop.R
import com.lingmiao.shop.base.UserManager
import com.lingmiao.shop.util.OtherUtils

/**
 * 店铺等待审核
 */
class ShopWaitApplyActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main_activity_shop_wait_apply)
        BarUtils.setStatusBarLightMode(this, true)

        OtherUtils.setJpushAlias()
    }
}