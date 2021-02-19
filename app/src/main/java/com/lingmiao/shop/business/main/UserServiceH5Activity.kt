package com.lingmiao.shop.business.main

import android.app.Activity
import android.widget.LinearLayout
import android.widget.RelativeLayout
import com.lingmiao.shop.R
import com.lingmiao.shop.base.IConstant
import com.james.common.base.BaseActivity
import com.james.common.base.BasePreImpl
import com.james.common.base.BasePresenter
import com.just.agentweb.AgentWeb
import kotlinx.android.synthetic.main.activity_user_service_h5.*


/**
 * 用户协议
 */
class UserServiceH5Activity : BaseActivity<BasePresenter>() {


    override fun createPresenter(): BasePresenter {
        return BasePreImpl(this)
    }

    override fun initView() {
        mToolBarDelegate.setMidTitle("C店商家服务条款")

        val agentWeb = AgentWeb.with(this)
            .setAgentWebParent(
                (rlServiceRoot as RelativeLayout?)!!,
                RelativeLayout.LayoutParams(-1, -1)
            )
            .useDefaultIndicator()
            .createAgentWeb()
            .ready()
            .go(IConstant.getServiceH5())
        ivService.isSelected = true
        ivService.setOnClickListener { ivService.isSelected = !ivService.isSelected }
        tvAgreeService.setOnClickListener {
            if (!ivService.isSelected) {
                showToast("请同意服务条款")
                return@setOnClickListener
            }
            setResult(Activity.RESULT_OK)
            finish()
        }
    }

    override fun getLayoutId(): Int {
        return R.layout.activity_user_service_h5
    }
}