package com.lingmiao.shop.business.login

import android.view.WindowManager
import com.blankj.utilcode.util.ActivityUtils
import com.james.common.base.BaseActivity
import com.james.common.base.BasePreImpl
import com.james.common.base.BasePresenter
import com.james.common.netcore.coroutine.CoroutineSupport
import com.lingmiao.shop.R
import com.lingmiao.shop.base.UserManager
import com.lingmiao.shop.util.OtherUtils
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

/**
 * 启动页面
 */
class SplashActivity : BaseActivity<BasePresenter>() {


    private val mCoroutine: CoroutineSupport by lazy { CoroutineSupport() }

    override fun getLayoutId(): Int {
        return R.layout.activity_splash
    }

    override fun createPresenter(): BasePresenter {
        return BasePreImpl(this)
    }

    override fun useBaseLayout(): Boolean {
        return false;
    }

    override fun initView() {
        window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)

        mCoroutine.launch {
            var againTime = 2
            while (againTime > 0) {
                againTime -= 1
                delay(1000)
            }
            if(UserManager.isLogin()){
                OtherUtils.goToMainActivity()
            }else{
                ActivityUtils.startActivity(LoginActivity::class.java)
            }
            finish()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        mCoroutine.destroy()
    }

}

