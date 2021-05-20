package com.lingmiao.shop.business.me

import android.text.TextUtils
import android.text.method.HideReturnsTransformationMethod
import android.text.method.PasswordTransformationMethod
import com.blankj.utilcode.util.EncryptUtils
import com.blankj.utilcode.util.RegexUtils
import com.blankj.utilcode.util.ToastUtils
import com.lingmiao.shop.R
import com.lingmiao.shop.business.me.bean.ForgetPassword
import com.lingmiao.shop.business.me.presenter.ForgetPasswordPresenter
import com.lingmiao.shop.business.me.presenter.impl.ForgetPasswordPresenterImpl
import com.james.common.base.BaseActivity
import com.james.common.netcore.coroutine.CoroutineSupport
import com.james.common.utils.exts.checkNotBlack
import kotlinx.android.synthetic.main.me_activity_forget_password.*
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.util.*

/**
*   忘记密码
*/
class ForgetPasswordActivity : BaseActivity<ForgetPasswordPresenter>(),ForgetPasswordPresenter.View {


    private val mCoroutine: CoroutineSupport by lazy { CoroutineSupport() }

    override fun getLayoutId(): Int {
        return R.layout.me_activity_forget_password
    }

    override fun useLightMode(): Boolean {
        return false
    }

    override fun initView() {
        mToolBarDelegate.setMidTitle("忘记密码")
        tvGetCode.setOnClickListener {
            if (TextUtils.isEmpty(etPhone.text.toString())) {
                showToast("请输入手机号码")
                return@setOnClickListener
            }
            if (!RegexUtils.isMobileSimple(etPhone.text.toString())) {
                showToast("请输入正确的手机号码")
                return@setOnClickListener
            }
            mPresenter?.requestSmsCodeInfoData(etPhone.text.toString())
            doGetCodeTextAction()
        }

        tvUpdatePasswordSubmit.setOnClickListener {
            try {
                checkNotBlack(etPhone.text.toString()) { "请输入手机号码" }
                checkNotBlack(etCode.text.toString()) { "请输入验证码" }
                checkNotBlack(etPassword.text.toString()) { "请输入新密码" }
                check(RegexUtils.isMobileSimple(etPhone.text.toString())) { "请输入正确的手机号码" }
                showDialogLoading()
                mPresenter.requestPersonInfoData(
                    ForgetPassword(
                    mobile = etPhone.text.toString(),
                    smsCode = etCode.text.toString(),
                    password =  EncryptUtils.encryptMD5ToString(etPassword.text.toString()).toLowerCase(
                        Locale.ROOT))
                )
            }catch (e: IllegalStateException){
                ToastUtils.showShort(e.message)
                e.printStackTrace()
            }
        }

        var showPassword = true
        ivPasswordVisible.setOnClickListener {
            showPassword = !showPassword
            if(showPassword){
                etPassword.transformationMethod = PasswordTransformationMethod.getInstance()
            }else{
                etPassword.transformationMethod = HideReturnsTransformationMethod.getInstance()
            }

        }
    }



    override fun createPresenter(): ForgetPasswordPresenter {
        return ForgetPasswordPresenterImpl(this, this)
    }




    private fun doGetCodeTextAction() {
        tvGetCode.isEnabled = false
        mCoroutine.launch {
            var againTime = 60
            while (againTime > 0) {
                againTime -= 1
                tvGetCode.text = againTime.toString() + "秒"
                delay(1000)
            }
            tvGetCode.text = getString(R.string.get_code)
            tvGetCode.isEnabled = true
        }
    }

    override fun onPersonInfoSuccess() {
        hideDialogLoading()
        showToast("修改密码成功")
        finish()
    }

    override fun onPersonInfoError() {
        hideDialogLoading()
    }

    override fun onSmsCodeInfoSuccess() {

    }

    override fun onSmsCodeInfoError() {

    }
}