package com.lingmiao.shop.business.me

import android.view.View
import androidx.appcompat.app.AppCompatDialog
import com.allenliu.versionchecklib.v2.AllenVersionChecker
import com.allenliu.versionchecklib.v2.builder.UIData
import com.blankj.utilcode.util.ActivityUtils
import com.blankj.utilcode.util.Utils
import com.lingmiao.shop.BuildConfig
import com.lingmiao.shop.R
import com.lingmiao.shop.base.UserManager
import com.lingmiao.shop.business.login.LoginActivity
import com.lingmiao.shop.business.me.bean.AccountSetting
import com.lingmiao.shop.business.me.presenter.AccountSettingPresenter
import com.lingmiao.shop.business.me.presenter.impl.AccountSettingPresenterImpl
import com.james.common.base.BaseActivity
import com.james.common.netcore.coroutine.CoroutineSupport
import com.james.common.utils.DialogUtils
import kotlinx.android.synthetic.main.me_activity_account_setting.*

/**
 *   账号设置
 */
class AccountSettingActivity : BaseActivity<AccountSettingPresenter>(),
    AccountSettingPresenter.View,
    View.OnClickListener {


    private val mCoroutine: CoroutineSupport by lazy { CoroutineSupport() }

    private var versionUpdateDialog: AppCompatDialog? = null
    private var accountSetting: AccountSetting? = null

    override fun getLayoutId(): Int {
        return R.layout.me_activity_account_setting
    }


    override fun useLightMode(): Boolean {
        return false
    }

    override fun initView() {
        mToolBarDelegate.setMidTitle("账号设置")
        // mToolBarDelegate.setToolbarBackgroundOfOtherTheme(context!!, R.color.color_secondary, R.color.white);
//        showPageLoading()
//
//        mPresenter.requestAccountSettingData()
        tvAccountVersion.text = "V" + BuildConfig.VERSION_NAME

        rlAccountUpdatePassword.setOnClickListener(this)
        rlAccountUpdatePhone.setOnClickListener(this)
        rlAccountVersion.setOnClickListener(this)
        tvAccountLoginOut.setOnClickListener(this)
        rlAboutUs.setOnClickListener(this)
    }


    override fun createPresenter(): AccountSettingPresenter {
        return AccountSettingPresenterImpl(this, this)
    }

    override fun onAccountSettingSuccess(bean: AccountSetting) {
        accountSetting = bean
        if(!bean.needUpgrade){
            showToast("已是最新版本")
            return
        }
        versionUpdateDialog = DialogUtils.showVersionUpdateDialog(this, "版本更新", bean.upgradeContent ?: "", null,
            View.OnClickListener {
                val builder = AllenVersionChecker
                    .getInstance()
                    .downloadOnly(crateUIData(bean))
                builder.isDirectDownload = true
                builder.isShowNotification = false
                builder.isShowDownloadingDialog = true
                builder.isShowDownloadFailDialog = false
//                builder.downloadAPKPath = PathUtils.getExternalAppFilesPath() +"/new.apk"
                builder.executeMission(Utils.getApp())
            }, bean.castUpdate
        )


    }

    private fun crateUIData(bean: AccountSetting): UIData? {
        return UIData.create().setTitle("版本更新").setContent(bean.upgradeContent)
            .setDownloadUrl(bean.downloadUrl)
    }

    override fun onAccountSettingError(code: Int) {

    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.rlAccountUpdatePassword -> {
                ActivityUtils.startActivity(UpdatePasswordActivity::class.java)
            }
            R.id.rlAccountUpdatePhone -> {
                ActivityUtils.startActivity(UpdatePhoneActivity::class.java)
            }
            R.id.rlAccountVersion -> {
                mPresenter?.requestAccountSettingData()

            }
            R.id.tvAccountLoginOut -> {
                DialogUtils.showDialog(this, "退出登录", "确定退出登录吗？", null, View.OnClickListener {
                    UserManager.loginOut()
                    ActivityUtils.startActivity(LoginActivity::class.java)
                    ActivityUtils.finishAllActivitiesExceptNewest()
                    finish()
                })

            }
            R.id.rlAboutUs -> {
                ActivityUtils.startActivity(AboutUsActivity::class.java)
            }
        }
    }

    override fun onResume() {
        super.onResume()
//        accountSetting?.let {
//            if(it.castUpdate && versionUpdateDialog!=null){
//                versionUpdateDialog?.dismiss()
//                onAccountSettingSuccess(it)
//            }
//        }
    }

}