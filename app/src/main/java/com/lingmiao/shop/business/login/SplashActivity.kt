package com.lingmiao.shop.business.login

import com.blankj.utilcode.util.ActivityUtils
import com.lingmiao.shop.R
import com.lingmiao.shop.base.UserManager
import com.lingmiao.shop.util.OtherUtils
import com.james.common.base.BaseActivity
import com.james.common.base.BasePreImpl
import com.james.common.base.BasePresenter

/**
 * 启动页面
 */
class SplashActivity : BaseActivity<BasePresenter>() {


    override fun getLayoutId(): Int {
        return R.layout.activity_splash
    }

    override fun createPresenter(): BasePresenter {
        return BasePreImpl(this)
    }

    override fun initView() {
       if(UserManager.isLogin()){
            OtherUtils.goToMainActivity()
        }else{
            ActivityUtils.startActivity(LoginActivity::class.java)
        }
        finish()
    }
}

