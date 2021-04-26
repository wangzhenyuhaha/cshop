package com.lingmiao.shop.business.me

import com.blankj.utilcode.util.EncryptUtils
import com.blankj.utilcode.util.ToastUtils
import com.lingmiao.shop.R
import com.lingmiao.shop.base.UserManager
import com.lingmiao.shop.business.me.bean.PersonInfoRequest
import com.lingmiao.shop.business.me.presenter.UpdatePasswordPresenter
import com.lingmiao.shop.business.me.presenter.impl.UpdatePasswordPresenterImpl
import com.james.common.base.BaseActivity
import com.james.common.netcore.coroutine.CoroutineSupport
import com.james.common.utils.exts.checkNotBlack
import kotlinx.android.synthetic.main.me_activity_update_password.*
import java.util.*


/**
 *   修改密码
 */
class UpdatePasswordActivity : BaseActivity<UpdatePasswordPresenter>(),
    UpdatePasswordPresenter.View {


    private val mCoroutine: CoroutineSupport by lazy { CoroutineSupport() }

    override fun getLayoutId(): Int {
        return R.layout.me_activity_update_password
    }

    override fun useLightMode(): Boolean {
        return false
    }

    override fun initView() {
        mToolBarDelegate.setMidTitle("修改密码")
        mToolBarDelegate.setToolbarBackgroundOfOtherTheme(context!!, R.color.color_secondary, R.color.white);

        tvUpdatePasswordSubmit.setOnClickListener {
            try {
                checkNotBlack(etOldPassword.text.toString()) { "请输入原密码" }
                checkNotBlack(etNewPassword.text.toString()) { "请输入新密码" }
                checkNotBlack(etConfirmNewPassword.text.toString()) { "请确认新密码" }
                check(etConfirmNewPassword.text.toString() == etNewPassword.text.toString()) { "2次新密码不一样" }
                showDialogLoading()
                mPresenter.requestPersonInfoData(PersonInfoRequest(id=UserManager.getLoginInfo()?.clerkId?.toString(),
                    mobile = UserManager.getLoginInfo()?.mobile,
                    oldPassword =  EncryptUtils.encryptMD5ToString(etOldPassword.text.toString()).toLowerCase(
                        Locale.ROOT),
                    password =  EncryptUtils.encryptMD5ToString(etNewPassword.text.toString()).toLowerCase(
                        Locale.ROOT)))
            } catch (e: IllegalStateException) {
                ToastUtils.showShort(e.message)
                e.printStackTrace()
            }
        }


    }


    override fun createPresenter(): UpdatePasswordPresenter {
        return UpdatePasswordPresenterImpl(this, this)
    }

    override fun onPersonInfoSuccess() {
        hideDialogLoading()
        showToast("修改成功")
        finish()
    }

    override fun onPersonInfoError() {
        hideDialogLoading()
    }


}