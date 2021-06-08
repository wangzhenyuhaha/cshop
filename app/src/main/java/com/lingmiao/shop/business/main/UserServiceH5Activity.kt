package com.lingmiao.shop.business.main

import android.app.Activity
import android.content.Intent
import android.widget.LinearLayout
import android.widget.RelativeLayout
import com.lingmiao.shop.R
import com.lingmiao.shop.base.IConstant
import com.james.common.base.BaseActivity
import com.james.common.base.BasePreImpl
import com.james.common.base.BasePresenter
import com.just.agentweb.AgentWeb
import com.lingmiao.shop.business.sales.SalesActivityEditActivity
import kotlinx.android.synthetic.main.activity_user_service_h5.*


/**
 * 用户协议
 */
class UserServiceH5Activity : BaseActivity<BasePresenter>() {

    var mType : Int? = null;

    companion object {

        const val KEY_VIEW_TYPE = "KEY_VIEW_TYPE"
        const val VIEW_PRIVACY = 1;
        const val VIEW_SERVICE = 2;
        const val VIEW_SECURITY = 2;

        fun service(context: Activity, result : Int) {
            val intent = Intent(context, UserServiceH5Activity::class.java)
            intent.putExtra(KEY_VIEW_TYPE, VIEW_SERVICE)
            context.startActivityForResult(intent, result)
        }

        fun privacy(context: Activity, result : Int) {
            val intent = Intent(context, UserServiceH5Activity::class.java)
            intent.putExtra(KEY_VIEW_TYPE, VIEW_PRIVACY)
            context.startActivityForResult(intent, result)
        }

        fun security(context: Activity, result : Int) {
            val intent = Intent(context, UserServiceH5Activity::class.java)
            intent.putExtra(KEY_VIEW_TYPE, VIEW_SECURITY)
            context.startActivityForResult(intent, result)
        }
    }

    override fun initBundles() {
        mType = intent?.getIntExtra(KEY_VIEW_TYPE, VIEW_PRIVACY);
    }

    override fun createPresenter(): BasePresenter {
        return BasePreImpl(this)
    }

    override fun initView() {
        mToolBarDelegate.setMidTitle("C店商家服务条款")


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
        var url = IConstant.getUserServiceH5();
        when(mType) {
            VIEW_PRIVACY -> {
                url = IConstant.getPrivacyServiceH5();
            }
            VIEW_SECURITY -> {
                url = IConstant.getSecurityH5();
            }
        }
        val agentWeb = AgentWeb.with(this)
            .setAgentWebParent(
                (rlServiceRoot as RelativeLayout?)!!,
                RelativeLayout.LayoutParams(-1, -1)
            )
            .useDefaultIndicator()
            .createAgentWeb()
            .ready()
            .go(url)

    }

    override fun getLayoutId(): Int {
        return R.layout.activity_user_service_h5
    }
}