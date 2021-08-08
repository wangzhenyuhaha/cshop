package com.fox7.wx

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Log
import androidx.annotation.DrawableRes
import com.fox7.wx.Util.Bitmap2Bytes
import com.tencent.mm.opensdk.modelbiz.WXLaunchMiniProgram
import com.tencent.mm.opensdk.modelmsg.*
import com.tencent.mm.opensdk.openapi.IWXAPI
import java.io.File
import java.net.URL


/**
Create Date : 2021/5/186:05 PM
Auther      : Fox
Desc        :
无法获知用户是否分享完成
https://open.weixin.qq.com/cgi-bin/announce?action=getannouncement&key=11534138374cE6li&version=&lang=zh_CN&token=2

 **/
class WxShare(val context: Context, val api : IWXAPI,var mTargetScene: Int = SendMessageToWX.Req.WXSceneSession) {

    var mTitle : String = "";

    var mDescription: String?=""

    var mTag: String?=""

    companion object {
        const val THUMB_SIZE = 150
    }

    fun checkApi() : Boolean {
        return api.isWXAppInstalled;
    }

    fun checkApiAndContext() : Boolean {
        return context != null && checkApi();
    }

    fun shareToFriend() {
        mTargetScene = SendMessageToWX.Req.WXSceneSession;
    }

    fun shareToPYQ() {
        mTargetScene = SendMessageToWX.Req.WXSceneTimeline;
    }

    /**
     * 收藏
     */
    fun shareToFavorite() {
        mTargetScene = SendMessageToWX.Req.WXSceneFavorite;
    }

    fun shareToContact() {
        mTargetScene = SendMessageToWX.Req.WXSceneSpecifiedContact;
    }

    fun shareText(mContent : String) {
        if(!checkApi()) {
            return
        }
        val textObj = WXTextObject()
        textObj.text = mContent

        val msg = WXMediaMessage()
        msg.mediaObject = textObj
        msg.title = mTitle
        msg.description = mDescription
        msg.mediaTagName = mTag

        val req = SendMessageToWX.Req()
        req.transaction = buildTransaction("text")
        req.message = msg
        req.scene = mTargetScene

        api.sendReq(req)
    }

    fun shareFile(path : String, mThumbSize : Int = THUMB_SIZE) {
        val file = File(path)
        if (!file.exists() || !checkApi()) {
            return;
        }

        val imgObj = WXImageObject()
        imgObj.setImagePath(path)

        val msg = WXMediaMessage()
        msg.mediaObject = imgObj

        val bmp = BitmapFactory.decodeFile(path)
        val thumbBmp = Bitmap.createScaledBitmap(
            bmp,
            mThumbSize,
            mThumbSize,
            true
        )
        bmp.recycle()
        msg.thumbData = Util.bmpToByteArray(thumbBmp, true)

        val req = SendMessageToWX.Req()
        req.transaction = buildTransaction("img")
        req.message = msg
        req.scene = mTargetScene
        api.sendReq(req)
    }

    fun shareImage(bmp : Bitmap, mThumbSize : Int = THUMB_SIZE) {
        if(!checkApiAndContext()) {
            return;
        }

        val msg = WXMediaMessage()
        msg.title = mTitle;
        msg.description = mDescription;

        var thumbBmp = Bitmap.createScaledBitmap(bmp, mThumbSize, mThumbSize, true);
        bmp.recycle();
        msg.thumbData = Bitmap2Bytes(thumbBmp)//Util.bmpToByteArray(thumbBmp, true)


        val imgObj = WXImageObject(thumbBmp)
        msg.mediaObject = imgObj


        val req = SendMessageToWX.Req()
        req.transaction = buildTransaction("img")
        req.message = msg
        req.scene = mTargetScene
        api.sendReq(req)
    }

    fun shareImage(mUrl : String, mThumbSize : Int = THUMB_SIZE) {
        if(!checkApiAndContext()) {
            return;
        }

        val msg = WXMediaMessage()
        msg.title = mTitle;
        msg.description = mDescription;


        //image网络图片
        var bmp = BitmapFactory.decodeStream(URL(mUrl).openStream());

        var thumbBmp = Bitmap.createScaledBitmap(bmp, mThumbSize, mThumbSize, true);
        bmp.recycle();
        msg.thumbData = Bitmap2Bytes(thumbBmp)//Util.bmpToByteArray(thumbBmp, true)


        val imgObj = WXImageObject(thumbBmp)
        msg.mediaObject = imgObj


        val req = SendMessageToWX.Req()
        req.transaction = buildTransaction("img")
        req.message = msg
        req.scene = mTargetScene
        api.sendReq(req)
    }

    fun shareImageResource(@DrawableRes mIconDrawable : Int = 0, mThumbSize : Int = THUMB_SIZE) {
        if(!checkApiAndContext()) {
            return;
        }
        val bmp = BitmapFactory.decodeResource(context.resources, mIconDrawable)
        val imgObj = WXImageObject(bmp)

        val msg = WXMediaMessage()
        msg.title = mTitle;
        msg.description = mDescription;
        msg.mediaObject = imgObj

        val thumbBmp = Bitmap.createScaledBitmap(
            bmp,
            mThumbSize,
            mThumbSize,
            true
        )
        bmp.recycle()
        msg.thumbData = Util.bmpToByteArray(thumbBmp, true)

        val req = SendMessageToWX.Req()
        req.transaction = buildTransaction("img")
        req.message = msg
        req.scene = mTargetScene
        api.sendReq(req)
    }


    fun shareMusic(mUrl : String, @DrawableRes mIconDrawable : Int = 0, mThumbSize : Int = THUMB_SIZE) {
        if(!checkApiAndContext()) {
            return;
        }
        val music = WXMusicObject()
        music.musicUrl = mUrl

        val msg = WXMediaMessage()
        msg.mediaObject = music
        msg.title = mTitle
        msg.description = mDescription;

        val bmp = BitmapFactory.decodeResource(
            context.resources,
            mIconDrawable
        )
        val thumbBmp = Bitmap.createScaledBitmap(
            bmp,
            mThumbSize,
            mThumbSize,
            true
        )
        bmp.recycle()
        msg.thumbData = Util.bmpToByteArray(thumbBmp, true)

        val req = SendMessageToWX.Req()
        req.transaction = buildTransaction("music")
        req.message = msg
        req.scene = mTargetScene
        api.sendReq(req)
    }


    fun shareVideo(mUrl : String, @DrawableRes mIconDrawable : Int = 0, mThumbSize : Int = THUMB_SIZE) {
        val video = WXVideoObject()
        video.videoUrl = mUrl

        val msg = WXMediaMessage(video)

        msg.mediaTagName = mTag;
        msg.messageAction = "";

        msg.title = mTitle
        msg.description = mDescription
        val bmp = BitmapFactory.decodeResource(
            context.resources,
            mIconDrawable
        )
        val thumbBmp = Bitmap.createScaledBitmap(
            bmp,
            mThumbSize,
            mThumbSize,
            true
        )
        bmp.recycle()
        msg.thumbData = Util.bmpToByteArray(thumbBmp, true)

        val req = SendMessageToWX.Req()
        req.transaction = buildTransaction("video")
        req.message = msg
        req.scene = mTargetScene
        api.sendReq(req)
    }

    fun shareWeb(mUrl : String, @DrawableRes mIconDrawable : Int = 0, mThumbSize : Int = THUMB_SIZE) {
        val webpage = WXWebpageObject()
        webpage.webpageUrl = mUrl
        val msg = WXMediaMessage(webpage)
        msg.title = mTitle;
        msg.description = mDescription
        val bmp = BitmapFactory.decodeResource(context.resources, mIconDrawable)
        val thumbBmp = Bitmap.createScaledBitmap(
            bmp,
            mThumbSize,
            mThumbSize,
            true
        )
        bmp.recycle()
        msg.thumbData = Util.bmpToByteArray(thumbBmp, true)

        val req = SendMessageToWX.Req()
        req.transaction = buildTransaction("webpage")
        req.message = msg
        req.scene = mTargetScene
        api.sendReq(req)
    }

    // 正式版
    var mMiniProgramType : Int = WXLaunchMiniProgram.Req.MINIPTOGRAM_TYPE_RELEASE;

    /**
     * 开发版
     */
    fun miniTypeToTest() {
        mMiniProgramType = WXLaunchMiniProgram.Req.MINIPROGRAM_TYPE_TEST;
    }

    /**
     * 体验版
     */
    fun miniTypeToPreview() {
        mMiniProgramType = WXLaunchMiniProgram.Req.MINIPROGRAM_TYPE_PREVIEW;
    }

    /**
     * 正式版
     */
    fun miniTypeToRelease() {
        mMiniProgramType = WXLaunchMiniProgram.Req.MINIPTOGRAM_TYPE_RELEASE;
    }

    fun miniType(isPrd : Boolean) {
        if(isPrd) {
            miniTypeToRelease();
        } else {
            miniTypeToPreview();
        }
    }

    fun openMini(miniId : String, path : String) {
        val req = WXLaunchMiniProgram.Req()
        // 填小程序原始id
        req.userName = miniId;

        //拉起小程序页面的可带参路径，不填默认拉起小程序首页，对于小游戏，可以只传入 query 部分，来实现传参效果，如：传入 "?foo=bar"。
        req.path = path

        req.miniprogramType = mMiniProgramType

        api.sendReq(req)
    }

    // appId填移动应用(App)的 AppId，非小程序的 AppID
    fun shareMini(miniId : String, path : String, url : ByteArray) {
        val miniProgramObj = WXMiniProgramObject()
        miniProgramObj.webpageUrl = "http://www.c-dian.cn" // 兼容低版本的网页链接
        // 正式版:0，测试版:1，体验版:2
        miniProgramObj.miniprogramType = mMiniProgramType
        // 小程序原始id
        miniProgramObj.userName = miniId
        //小程序页面路径
        miniProgramObj.path = path

        val msg = WXMediaMessage(miniProgramObj)
        // 小程序消息title
        msg.title = mTitle
        // 小程序消息desc
        msg.description = mDescription

        // 小程序消息封面图片，小于128k
        msg.thumbData = url
        Log.d("i :", ".... " + (msg.thumbData == null))

        val req = SendMessageToWX.Req()
        req.transaction = buildTransaction("webpage")
        req.message = msg
        // 目前支持会话
        req.scene = SendMessageToWX.Req.WXSceneSession

        api.sendReq(req)
    }

    private fun buildTransaction(type: String?): String? {
        return if (type == null) System.currentTimeMillis()
            .toString() else type + System.currentTimeMillis()
    }

}