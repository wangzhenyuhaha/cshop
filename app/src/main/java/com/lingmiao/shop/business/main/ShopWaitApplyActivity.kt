package com.lingmiao.shop.business.main

import android.view.KeyEvent
import androidx.core.content.ContextCompat
import com.blankj.utilcode.util.ActivityUtils
import com.blankj.utilcode.util.AppUtils
import com.blankj.utilcode.util.BarUtils
import com.blankj.utilcode.util.ToastUtils
import com.james.common.base.BaseActivity
import com.james.common.base.BasePreImpl
import com.james.common.base.BasePresenter
import com.james.common.utils.exts.singleClick
import com.lingmiao.shop.R
import com.lingmiao.shop.business.me.HelpDocActivity
import com.lingmiao.shop.util.OtherUtils
import kotlinx.android.synthetic.main.main_activity_shop_wait_apply.*

/**
 * 店铺等待审核
 */
class ShopWaitApplyActivity : BaseActivity<BasePresenter>() {

    override fun useLightMode(): Boolean {
        return false;
    }

    override fun useBaseLayout(): Boolean {
        return false;
    }

    override fun createPresenter(): BasePresenter {
        return BasePreImpl();
    }

    override fun getLayoutId(): Int {
        return R.layout.main_activity_shop_wait_apply;
    }

    override fun initView() {
        BarUtils.transparentStatusBar(this)
        val statusBarHeight = BarUtils.getStatusBarHeight()
        viStatusBar.layoutParams.height = statusBarHeight
        viStatusBar.setBackgroundColor(ContextCompat.getColor(this, R.color.primary))
        tvHelpCenter.singleClick {
            ActivityUtils.startActivity(HelpDocActivity::class.java)
        }
        OtherUtils.setJpushAlias()
    }

    //双击退出应用
    private var mExitTime: Long = 0
    override fun onKeyDown(keyCode: Int, event: KeyEvent?): Boolean {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
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