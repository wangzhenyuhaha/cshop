package com.lingmiao.shop.business.login

import android.animation.ObjectAnimator
import android.animation.PropertyValuesHolder
import android.app.Activity
import android.content.Intent
import android.text.TextUtils
import android.view.Gravity
import android.view.View
import android.view.animation.TranslateAnimation
import android.webkit.JavascriptInterface
import android.webkit.WebSettings
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.appcompat.app.AppCompatDialog
import androidx.core.content.ContextCompat
import com.allenliu.versionchecklib.v2.AllenVersionChecker
import com.allenliu.versionchecklib.v2.builder.UIData
import com.blankj.utilcode.util.*
import com.google.gson.reflect.TypeToken
import com.jaeger.library.StatusBarUtil
import com.james.common.base.BaseActivity
import com.james.common.net.BaseResponse
import com.james.common.netcore.coroutine.CoroutineSupport
import com.james.common.utils.DialogUtils
import com.lingmiao.shop.BuildConfig
import com.lingmiao.shop.R
import com.lingmiao.shop.base.IConstant
import com.lingmiao.shop.base.UserManager
import com.lingmiao.shop.business.login.bean.CaptchaAli
import com.lingmiao.shop.business.login.bean.LoginInfo
import com.lingmiao.shop.business.login.bean.LoginRequest
import com.lingmiao.shop.business.login.presenter.LoginPresenter
import com.lingmiao.shop.business.login.presenter.impl.LoginPresenterImpl
import com.lingmiao.shop.business.main.UserServiceH5Activity
import com.lingmiao.shop.business.me.ForgetPasswordActivity
import com.lingmiao.shop.business.me.bean.AccountSetting
import com.lingmiao.shop.util.OtherUtils
import kotlinx.android.synthetic.main.activity_login.*
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import java.util.*

/**
 * 登录
 */
class LoginActivity : BaseActivity<LoginPresenter>(), LoginPresenter.View {
    private var registerStatus = false
    private var loginType = LOGIN_BY_PASSWORD

    private val mCoroutine: CoroutineSupport by lazy { CoroutineSupport() }

    private var versionUpdateDialog: AppCompatDialog? = null
    private var accountSetting: AccountSetting? = null

    companion object {
        private const val LOGIN_BY_CODE = 0
        private const val LOGIN_BY_PASSWORD = 1
        private const val REGISTER = 2

        private const val REQUEST_CODE_SERVICE = 8
    }

    override fun useBaseLayout(): Boolean {
        return false;
    }

    override fun getLayoutId(): Int {
        return R.layout.activity_login
    }

    override fun useEventBus(): Boolean {
        return true
    }

    override fun initView() {

        StatusBarUtil.setColor(context, ContextCompat.getColor(context, R.color.colorPrimary));

        if(!IConstant.official) {
            tvVersionFlag.setText(String.format("测试-%s", BuildConfig.VERSION_NAME));
        }

        ToastUtils.setGravity(Gravity.BOTTOM, 0, 100)
        tvGetCode.setOnClickListener {
            if (TextUtils.isEmpty(etPhone.text.toString())) {
                showToast("请输入手机号码")
                return@setOnClickListener
            }
            if (!RegexUtils.isMobileSimple(etPhone.text.toString())) {
                showToast("请输入正确的手机号码")
                return@setOnClickListener
            }
            rlLoginCaptcha.visibility = View.VISIBLE
            wvCaptcha.loadUrl( "javascript:window.location.reload( true )" )
        }
        ivLoginCaptchaClose.setOnClickListener {
            rlLoginCaptcha.visibility = View.GONE

        }
        tvLogin.setOnClickListener {
            if (registerStatus) {
                register()
            } else {
                login()
            }
        }
        tvBottomRight.setOnClickListener { changeLoginType() }
        tvLoginHint.setOnClickListener { changeLoginRegister() }
        tvService.setOnClickListener {
            UserServiceH5Activity.service(this, REQUEST_CODE_SERVICE);
        }
        tvPrivacy.setOnClickListener {
            UserServiceH5Activity.privacy(this, REQUEST_CODE_SERVICE);
        }
        tvForgetPassword.setOnClickListener { ActivityUtils.startActivity(ForgetPasswordActivity::class.java) }
        initLoginTypeView()
        initCaptcha()

        mPresenter.requestAccountSettingData()
    }

    private fun initCaptcha() {
        // 设置屏幕自适应。
        wvCaptcha.settings.useWideViewPort = true
        wvCaptcha.settings.loadWithOverviewMode = true
        // 建议禁止缓存加载，以确保在攻击发生时可快速获取最新的滑动验证组件进行对抗。
        wvCaptcha.settings.cacheMode = WebSettings.LOAD_NO_CACHE
        wvCaptcha.webViewClient = object : WebViewClient() {
            override fun shouldOverrideUrlLoading(view: WebView, url: String): Boolean {
                view.loadUrl(url)
                return true
            }
        }
        wvCaptcha.settings.javaScriptEnabled = true
        wvCaptcha.addJavascriptInterface(this, "android")
//        wvCaptcha.loadUrl("http://47.117.112.134:9527/captcha.html")
        wvCaptcha.loadUrl(IConstant.getCaptchaUrl())
//        wvCaptcha.loadUrl("http://t-api.seller.fisheagle.cn:7003/captcha.html")
        LogUtils.d(wvCaptcha.url)
    }


    private fun changeLoginRegister() {
        registerStatus = !registerStatus
        if (registerStatus) {
            loginType = REGISTER
        } else {
            loginType = LOGIN_BY_CODE
        }
        initLoginTypeView()
    }

    private fun changeLoginType() {
        if (loginType == LOGIN_BY_CODE) {
            loginType = LOGIN_BY_PASSWORD
        } else if (loginType == LOGIN_BY_PASSWORD) {
            loginType = LOGIN_BY_CODE
        }
        initLoginTypeView()
    }

    fun startAnimation(view : View) {
        val translationX: PropertyValuesHolder =
            PropertyValuesHolder.ofFloat("translationX", ScreenUtils.getScreenWidth().toFloat(), 0f)//

        val btnAnimator: ObjectAnimator =
            ObjectAnimator.ofPropertyValuesHolder(view, translationX)

        btnAnimator.duration = 1000
        btnAnimator.start();
    }

    fun outAnimation(view : View) {
        val translationX: PropertyValuesHolder =
            PropertyValuesHolder.ofFloat("translationX", 0f, ScreenUtils.getScreenWidth().toFloat())

        val btnAnimator: ObjectAnimator =
            ObjectAnimator.ofPropertyValuesHolder(view, translationX)

        btnAnimator.duration = 800
        btnAnimator.start();
    }

    private fun initLoginTypeView() {
        viRegisterDivide.visibility = View.GONE
        llPhone.visibility = View.GONE
        llCode.visibility = View.GONE
        llAccount.visibility = View.GONE
        llPassword.visibility = View.GONE
        tvForgetPassword.visibility = View.GONE
        tvBottomRight.visibility = View.GONE

        tvLoginType.text = getString(R.string.login_by_phone)
        tvLoginHint.text = getString(R.string.no_account_register)
        tvLogin.text = "登录"
        etPassword.hint = "请输入密码"
        etPassword.setText("")
        etCode.setText("")
        etPhone.setText("")
        etAccount.setText("")
        hintLoginTv.setText("使用已注册得手机号登录")

        if (loginType == LOGIN_BY_CODE) {
            llPhone.visibility = View.VISIBLE
            llCode.visibility = View.VISIBLE

            tvForgetPassword.visibility = View.VISIBLE
            tvBottomRight.visibility = View.VISIBLE
            tvBottomRight.text = "密码登录"
        } else if (loginType == LOGIN_BY_PASSWORD) {
            llAccount.visibility = View.VISIBLE
            llPassword.visibility = View.VISIBLE


            tvForgetPassword.visibility = View.VISIBLE
            tvBottomRight.visibility = View.VISIBLE
            tvBottomRight.text = "短信登录"
        } else if (loginType == REGISTER) {
            viRegisterDivide.visibility = View.VISIBLE
            llPhone.visibility = View.VISIBLE
            llCode.visibility = View.VISIBLE
            llPassword.visibility = View.VISIBLE

            tvLoginType.text = getString(R.string.register_by_phone)
            tvLoginHint.text = getString(R.string.has_account_login)
            tvLogin.text = "注册"
            etPassword.hint = "请设置密码"
            hintLoginTv.setText("注册后可免费体验C店全部解决方案")
        }
    }


    private fun login() {
        if (loginType == LOGIN_BY_CODE) {
            if (TextUtils.isEmpty(etPhone.text.toString())) {
                showToast("请输入手机号码")
                return
            }
            if (!RegexUtils.isMobileSimple(etPhone.text.toString())) {
                showToast("请输入正确的手机号码")
                return
            }
            if (TextUtils.isEmpty(etCode.text.toString())) {
                showToast("请输入验证")
                return
            }
        } else {
            if (TextUtils.isEmpty(etAccount.text.toString())) {
                showToast("请输入账号/手机号")
                return
            }
            if (TextUtils.isEmpty(etPassword.text.toString())) {
                showToast("请输入密码")
                return
            }
        }
        if (!ivService.isChecked) {
            showToast("请同意服务条款")
            return
        }
        var loginInfo = LoginRequest(
            loginType,
            etPhone.text.toString(),
            EncryptUtils.encryptMD5ToString(etPassword.text.toString()).toLowerCase(Locale.ROOT),
            etCode.text.toString(),
            etAccount.text.toString()
        )
        showDialogLoading()
        mPresenter?.login(loginInfo)
    }

    private fun register() {
        if (TextUtils.isEmpty(etPhone.text.toString())) {
            showToast("请输入手机号码")
            return
        }
        if (!RegexUtils.isMobileSimple(etPhone.text.toString())) {
            showToast("请输入正确的手机号码")
            return
        }
        if (TextUtils.isEmpty(etCode.text.toString())) {
            showToast("请输入验证")
            return
        }

        if (TextUtils.isEmpty(etPassword.text.toString())) {
            showToast("请输入密码")
            return
        }
        if (!ivService.isChecked) {
            showToast("请同意服务条款")
            return
        }
        showDialogLoading()
        var account = etPhone.text.toString()
        mPresenter?.register(
            etPhone.text.toString(),
            EncryptUtils.encryptMD5ToString(etPassword.text.toString()).toLowerCase(Locale.ROOT),
            etCode.text.toString(),
            account
        )
    }


    override fun createPresenter(): LoginPresenter {
        return LoginPresenterImpl(this)
    }

    override fun onLoginSmsCodeSuccess() {
        showToast("验证码已发出")
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

    override fun onLoginSmsCodeError(body: BaseResponse?) {
        if (body != null && body.code == "929") {

        }
    }

    override fun onLoginSuccess(body: LoginInfo?) {
        hideDialogLoading()
        UserManager.setLoginInfo(body!!)
        OtherUtils.goToMainActivity()
        finish()
    }

    override fun onLoginError() {
        hideDialogLoading()
    }

    override fun onRegisterSmsCodeSuccess() {
        hideDialogLoading()
        showToast("验证码已发出")
    }

    override fun onRegisterSmsCodeError() {
        hideDialogLoading()
    }

    override fun onRegisterSuccess(body: LoginInfo?) {
        hideDialogLoading()
        UserManager.setLoginInfo(body!!)
        OtherUtils.goToMainActivity()
        finish()
    }

    override fun onRegisterError() {
        hideDialogLoading()
    }

    @Subscribe
    fun getCaptchaAli(bean: CaptchaAli) {
        bean.mobile = etPhone.text.toString()
        doGetCodeTextAction()
        if (registerStatus) {
            mPresenter?.getRegisterSmsCode(bean)
        } else {
            mPresenter?.getLoginSmsCode(bean)
        }

    }

    @JavascriptInterface
    fun getSlideData(callData: String?) {
        LogUtils.d(callData)
        wvCaptcha.postDelayed(Runnable {
            rlLoginCaptcha.visibility = View.GONE
            if (!TextUtils.isEmpty(callData)) {
                val bean = GsonUtils.fromJson<CaptchaAli>(
                    callData,
                    object : TypeToken<CaptchaAli?>() {}.type
                )
                bean.session_id = bean.sessionid
                EventBus.getDefault().post(bean)
            } else {
            }
        }, 1000)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_CODE_SERVICE && resultCode == Activity.RESULT_OK) {
            ivService.isChecked = true
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        mCoroutine.destroy()
    }

    override fun onAccountSettingSuccess(bean: AccountSetting) {
        accountSetting = bean
        if(!bean.needUpgrade) return
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

}