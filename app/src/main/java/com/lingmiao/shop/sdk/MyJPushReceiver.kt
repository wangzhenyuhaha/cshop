package com.lingmiao.shop.sdk

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import cn.jpush.android.api.*
import cn.jpush.android.service.JPushMessageReceiver
import com.blankj.utilcode.util.GsonUtils
import com.blankj.utilcode.util.LogUtils
import com.blankj.utilcode.util.Utils
import com.lingmiao.shop.base.IConstant
import com.lingmiao.shop.base.UserManager
import com.lingmiao.shop.business.login.SplashActivity
import com.lingmiao.shop.business.main.MainActivity
import com.lingmiao.shop.business.main.ShopWaitApplyActivity
import com.lingmiao.shop.util.VoiceUtils
import org.json.JSONException
import org.json.JSONObject

class MyJPushReceiver: JPushMessageReceiver() {
    private val TAG = "PushMessageReceiver"
    override fun onMessage(
        context: Context,
        customMessage: CustomMessage
    ) {
        LogUtils.e(TAG, "[onMessage] $customMessage")
        processCustomMessage(context, customMessage)
    }

    override fun onNotifyMessageOpened(
        context: Context,
        message: NotificationMessage
    ) {
        LogUtils.e(TAG, "[onNotifyMessageOpened] $message")
        try {
            //打开自定义的Activity

            var i = Intent(context, MainActivity::class.java)
            val bundle = Bundle()
            bundle.putString(JPushInterface.EXTRA_NOTIFICATION_TITLE, message.notificationTitle)
            bundle.putString(JPushInterface.EXTRA_ALERT, message.notificationContent)
            val jpushExtra:JpushExtra? = GsonUtils.fromJson(message.notificationExtras, JpushExtra::class.java)
            jpushExtra?.let {
//                LogUtils.e(TAG, "jpushExtra:$jpushExtra")
                if(IConstant.MESSAGE_ORDER_PAY_SUCCESS == jpushExtra.type){
                    bundle.putString(IConstant.JPUSH_TYPE, IConstant.MESSAGE_ORDER_PAY_SUCCESS)
                } else if(IConstant.MESSAGE_APPLY_SHOP_REFUSE == jpushExtra.type){
                    i = Intent(context, ShopWaitApplyActivity::class.java)
                }
            }
            i.putExtras(bundle)
            //i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            i.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP
            context.startActivity(i)
        } catch (throwable: Throwable) {
            throwable.printStackTrace()
        }
    }

    override fun onMultiActionClicked(context: Context?, intent: Intent) {
        LogUtils.e(TAG, "[onMultiActionClicked] 用户点击了通知栏按钮")
        val nActionExtra =
            intent.extras?.getString(JPushInterface.EXTRA_NOTIFICATION_ACTION_EXTRA)

        //开发者根据不同 Action 携带的 extra 字段来分配不同的动作。
        if (nActionExtra == null) {
            LogUtils.d(TAG, "ACTION_NOTIFICATION_CLICK_ACTION nActionExtra is null")
            return
        }
        if (nActionExtra == "my_extra1") {
            LogUtils.e(TAG, "[onMultiActionClicked] 用户点击通知栏按钮一")
        } else if (nActionExtra == "my_extra2") {
            LogUtils.e(TAG, "[onMultiActionClicked] 用户点击通知栏按钮二")
        } else if (nActionExtra == "my_extra3") {
            LogUtils.e(TAG, "[onMultiActionClicked] 用户点击通知栏按钮三")
        } else {
            LogUtils.e(TAG, "[onMultiActionClicked] 用户点击通知栏按钮未定义")
        }
    }

    override fun onNotifyMessageArrived(
        context: Context?,
        message: NotificationMessage
    ) {
        LogUtils.e(TAG, "[onNotifyMessageArrived] $message")
        try {
            val jpushExtra:JpushExtra? = GsonUtils.fromJson(message.notificationExtras, JpushExtra::class.java)
            jpushExtra?.let {
//                LogUtils.e(TAG, "jpushExtra:$jpushExtra")
                if(IConstant.MESSAGE_ORDER_PAY_SUCCESS == jpushExtra.type){
                    VoiceUtils.playVoice(Utils.getApp())
                }
            }
        }catch (e:Exception){
            e.printStackTrace()
        }


    }

    override fun onNotifyMessageDismiss(
        context: Context?,
        message: NotificationMessage
    ) {
        LogUtils.e(TAG, "[onNotifyMessageDismiss] $message")
    }

    override fun onRegister(
        context: Context?,
        registrationId: String
    ) {
        LogUtils.e(TAG, "[onRegister] $registrationId")
    }

    override fun onConnected(
        context: Context?,
        isConnected: Boolean
    ) {
        LogUtils.e(TAG, "[onConnected] $isConnected")
    }

    override fun onCommandResult(
        context: Context?,
        cmdMessage: CmdMessage
    ) {
        LogUtils.d(TAG, "[onCommandResult] $cmdMessage")
    }

    override fun onTagOperatorResult(
        context: Context?,
        jPushMessage: JPushMessage?
    ) {
        TagAliasOperatorHelper.getInstance().onTagOperatorResult(context, jPushMessage)
        super.onTagOperatorResult(context, jPushMessage)
    }

    override fun onCheckTagOperatorResult(
        context: Context?,
        jPushMessage: JPushMessage?
    ) {
        TagAliasOperatorHelper.getInstance().onCheckTagOperatorResult(context, jPushMessage)
        super.onCheckTagOperatorResult(context, jPushMessage)
    }

    override fun onAliasOperatorResult(
        context: Context?,
        jPushMessage: JPushMessage?
    ) {
        TagAliasOperatorHelper.getInstance().onAliasOperatorResult(context, jPushMessage)
        super.onAliasOperatorResult(context, jPushMessage)
    }

    override fun onMobileNumberOperatorResult(
        context: Context?,
        jPushMessage: JPushMessage?
    ) {
        TagAliasOperatorHelper.getInstance().onMobileNumberOperatorResult(context, jPushMessage)
        super.onMobileNumberOperatorResult(context, jPushMessage)
    }

    //send msg to MainActivity
    private fun processCustomMessage(
        context: Context,
        customMessage: CustomMessage
    ) {
//        if (MainActivity.isForeground) {
//            val message = customMessage.message
//            val extras = customMessage.extra
//            val msgIntent = Intent(MainActivity.MESSAGE_RECEIVED_ACTION)
//            msgIntent.putExtra(MainActivity.KEY_MESSAGE, message)
//            if (!ExampleUtil.isEmpty(extras)) {
//                try {
//                    val extraJson = JSONObject(extras)
//                    if (extraJson.length() > 0) {
//                        msgIntent.putExtra(MainActivity.KEY_EXTRAS, extras)
//                    }
//                } catch (e: JSONException) {
//                }
//            }
//            LocalBroadcastManager.getInstance(context).sendBroadcast(msgIntent)
//        }
    }

    override fun onNotificationSettingsCheck(
        context: Context?,
        isOn: Boolean,
        source: Int
    ) {
        super.onNotificationSettingsCheck(context, isOn, source)
        LogUtils.e(TAG, "[onNotificationSettingsCheck] isOn:$isOn,source:$source")
    }
}