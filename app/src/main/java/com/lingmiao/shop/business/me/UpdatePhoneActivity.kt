package com.lingmiao.shop.business.me

import android.content.Intent
import android.text.TextUtils
import com.blankj.utilcode.util.RegexUtils
import com.blankj.utilcode.util.ToastUtils
import com.lingmiao.shop.R
import com.lingmiao.shop.base.UserManager
import com.lingmiao.shop.business.me.bean.PersonInfoRequest
import com.lingmiao.shop.business.me.bean.UpdatePhone
import com.lingmiao.shop.business.me.presenter.UpdatePhonePresenter
import com.lingmiao.shop.business.me.presenter.impl.UpdatePhonePresenterImpl
import com.james.common.base.BaseActivity
import com.james.common.netcore.coroutine.CoroutineSupport
import com.james.common.utils.exts.checkNotBlack
import kotlinx.android.synthetic.main.me_activity_update_phone.*
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.lang.IllegalStateException

/**
 *   修改手机
 */
class UpdatePhoneActivity : BaseActivity<UpdatePhonePresenter>(), UpdatePhonePresenter.View {


    private val mCoroutine: CoroutineSupport by lazy { CoroutineSupport() }

    private var newPhone = false

    override fun getLayoutId(): Int {
        return R.layout.me_activity_update_phone
    }

    override fun useLightMode(): Boolean {
        return false
    }

    override fun initView() {
        mToolBarDelegate.setMidTitle("修改手机")

        newPhone = intent.getBooleanExtra("newPhone", false)

        if (newPhone) {
            tvPhoneKey.text = "新手机号"
            etPhone.hint = "请输入新手机号"
            tvUpdatePhoneNext.text = "提交"
        } else {
            etPhone.isEnabled = false
            etPhone.setText(UserManager.getLoginInfo()?.mobile)
        }

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

        tvUpdatePhoneNext.setOnClickListener {
            try {
                if (newPhone) {
                    checkNotBlack(etPhone.text.toString()) { "请输入新手机号" }
                    checkNotBlack(etCode.text.toString()) { "请输入验证码" }
                    check(RegexUtils.isMobileSimple(etPhone.text.toString())) { "请输入正确的手机号码" }
                    showDialogLoading()
                    mPresenter.requestPersonInfoData(PersonInfoRequest(id=UserManager.getLoginInfo()?.clerkId?.toString(),
                        mobile = etPhone.text.toString(),smsCode = etCode.text.toString()))
                    return@setOnClickListener
                }
                checkNotBlack(etPhone.text.toString()) { "请输入原手机号" }
                checkNotBlack(etCode.text.toString()) { "请输入验证码" }
                check(RegexUtils.isMobileSimple(etPhone.text.toString())) { "请输入正确的手机号码" }
                showDialogLoading()
                mPresenter.checkSmsCodeData(etPhone.text.toString(), etCode.text.toString())

            } catch (e: IllegalStateException) {
                ToastUtils.showShort(e.message)
                e.printStackTrace()
            }
        }
    }


    override fun createPresenter(): UpdatePhonePresenter {
        return UpdatePhonePresenterImpl(this, this)
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
        showToast("修改手机号成功")
        finish()
    }

    override fun onPersonInfoError() {
        hideDialogLoading()
    }

    override fun onSmsCodeInfoSuccess() {
        showToast("验证码已发出")
    }

    override fun onSmsCodeInfoError() {
    }

    override fun onCheckSmsCodeSuccess() {
        hideDialogLoading()
        val intent = Intent(this, UpdatePhoneActivity::class.java)
        intent.putExtra("newPhone", true)
        startActivity(intent)
        finish()
    }

    override fun onCheckSmsCodeError() {
        hideDialogLoading()
    }
}