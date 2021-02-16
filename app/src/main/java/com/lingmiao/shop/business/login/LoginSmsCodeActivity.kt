package com.lingmiao.shop.business.login

import android.os.Bundle

import androidx.appcompat.app.AppCompatActivity
import com.blankj.utilcode.util.ActivityUtils
import com.blankj.utilcode.util.LogUtils
import com.lingmiao.shop.R
import kotlinx.android.synthetic.main.activity_login_sms_code.*

/**
 * 手机验证码页面
 */
class LoginSmsCodeActivity : AppCompatActivity() {
    private var registerStatus: Boolean = false
    private var phone: String? = ""

    companion object {
        fun start(registerStatus: Boolean, phone: String) {
            var bundle = Bundle()

            bundle.putBoolean("registerStatus", registerStatus)
            bundle.putString("phone", phone)
            ActivityUtils.startActivity(LoginSmsCodeActivity::class.java, bundle)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login_sms_code)
//        registerStatus = intent.getBooleanExtra("registerStatus", true)
        phone = intent?.getStringExtra("phone")
        LogUtils.d("phone:" + phone)
        tvPhone.text = phone
        if(registerStatus){
            tvLoginHint.text = getString(R.string.has_account_login)
        }else{
            tvLoginHint.text = getString(R.string.no_account_register)

        }

    }
}