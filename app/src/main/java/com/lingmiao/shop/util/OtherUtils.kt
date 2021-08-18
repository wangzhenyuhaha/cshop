package com.lingmiao.shop.util

import android.app.Activity
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.net.Uri
import android.os.Build
import android.text.TextUtils
import android.view.View
import android.view.WindowManager
import cn.jpush.android.api.JPushInterface
import com.blankj.utilcode.util.ActivityUtils
import com.blankj.utilcode.util.LogUtils
import com.blankj.utilcode.util.ToastUtils
import com.blankj.utilcode.util.Utils
import com.lingmiao.shop.base.ShopStatusConstants
import com.lingmiao.shop.base.UserManager
import com.lingmiao.shop.business.main.MainActivity
import com.lingmiao.shop.business.main.ShopWaitApplyActivity
import com.lingmiao.shop.business.me.bean.PersonInfoRequest
import com.luck.picture.lib.entity.LocalMedia
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.io.File

object OtherUtils {

    /**
     * 复制到剪切板
     */
    fun copyToClipData(info: String?) {
        if (TextUtils.isEmpty(info)) return
        val cm = Utils.getApp().getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
        val clipData = ClipData.newPlainText("info", info)
        cm.setPrimaryClip(clipData)
        ToastUtils.showShort("复制成功")
    }

    /**
     *  跳转到拨号盘
     */
    fun goToDialApp(activity: Activity?, phone: String?) {
        if (TextUtils.isEmpty(phone)) {
            ToastUtils.showShort("手机号码不能为空")
            return
        }
        val intent = Intent(Intent.ACTION_DIAL, Uri.parse("tel:$phone"))
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
        activity?.startActivity(intent)

    }

    fun setStatusBarTransparent(activity: Activity) {
        val window = activity.window
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS or WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS)
        window.decorView.systemUiVisibility =
            View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN or View.SYSTEM_UI_FLAG_LAYOUT_STABLE
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            window.statusBarColor = Color.TRANSPARENT
        }
    }

    fun goToMainActivity() {
        val loginInfo = UserManager.getLoginInfo() ?: return

        if(ShopStatusConstants.isAuthed(loginInfo.shopStatus)) {
            ActivityUtils.startActivity(MainActivity::class.java)
        } else {
            ActivityUtils.startActivity(ShopWaitApplyActivity::class.java)
        }
    }

    fun setJpushAlias() {
        if (UserManager.getLoginInfo()?.shopId == null) {
            return
        }
        MainScope().launch {
            delay(2000)
//            JPushInterface.deleteAlias(Utils.getApp(), 1)
            JPushInterface.setAlias(
                Utils.getApp(),
                1,
                "mkt_s_" + UserManager.getLoginInfo()?.shopId.toString()
            )
            LogUtils.d("shopId:" + UserManager.getLoginInfo()?.shopId+",Registration ID:"+JPushInterface.getRegistrationID(Utils.getApp()))
        }
    }

    fun getImageFile(localMedia: LocalMedia?): File {
        var path :String?=null
        path = if(Build.VERSION.SDK_INT>=29){
            localMedia?.androidQToPath
        }else{
            localMedia?.path
        }
        return File(path)
    }

     fun tranPersonBeanToMap(request: PersonInfoRequest): HashMap<String, String?> {
        val map = HashMap<String, String?>()
        if (request.face != null) map["face"] = request.face
        if (request.uname != null) map["uname"] = request.uname
        if (request.password != null) map["password"] = request.password
        if (request.oldPassword != null) map["oldPassword"] = request.oldPassword
        if (request.smsCode != null) map["smsCode"] = request.smsCode
        if (request.mobile != null) map["mobile"] = request.mobile
        return map
    }
}