package com.lingmiao.shop.business.main

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.annotation.IntDef
import com.blankj.utilcode.util.ActivityUtils
import com.james.common.base.BaseActivity
import com.james.common.base.BasePreImpl
import com.james.common.base.BasePresenter
import com.lingmiao.shop.R
import com.lingmiao.shop.util.OtherUtils

/**
 * 店铺等待审核
 */
class ShopWaitApplyActivity : BaseActivity<BasePresenter>() {

    override fun useLightMode(): Boolean {
        return false;
    }

    override fun useBaseLayout(): Boolean {
        return true;
    }

    override fun createPresenter(): BasePresenter {
        return BasePreImpl();
    }

    override fun getLayoutId(): Int {
        return R.layout.main_activity_shop_wait_apply;
    }

    override fun initView() {
        mToolBarDelegate.setBackIcon(R.mipmap.main_shop_logo_default)
//        mToolBarDelegate.setRightIcon(R.mipmap.main_message, View.OnClickListener {
//            ActivityUtils.startActivity(MessageCenterActivity::class.java)
//        });

        OtherUtils.setJpushAlias()
    }
}