package com.lingmiao.shop.business.me.api

import com.james.common.netcore.networking.http.annotations.WithHiResponse
import com.lingmiao.shop.business.login.bean.CaptchaAli
import com.lingmiao.shop.business.main.bean.ApplyShopInfo
import com.lingmiao.shop.business.me.bean.*
import com.lingmiao.shop.business.wallet.bean.PageRecordVo
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.*

interface MeApi {
    //    我的页面数据
    @GET("seller/shops/clerks/detail")
    @WithHiResponse
    fun getMyData(): Call<My>

    //    检测升级
    @GET("seller/app/upgrade")
    @WithHiResponse
    fun getUpgrade(
        @Query("current_version") version: String,
        @Query("current_version_code") versionCode: String
    ): Call<AccountSetting>

    //    APP修改店员信息
    @PUT("seller/shops/clerks/app/{id}")
    suspend fun updatePersonInfo(
        @Path("id") id: String,
        @QueryMap map: Map<String, String?>
    ): Response<ResponseBody>

    //    建议反馈
    @POST("seller/app/feedback/addFeedback")
    suspend fun feedback(@Body request: Feedback): Response<ResponseBody>

    //获取企业微信二维码
    @GET("seller/app/feedback/getContactWay")
    @WithHiResponse
    suspend  fun getCompanyWeChat():  Response<String>

    //    发送手机号码
    @POST("/seller/shops/clerks/smscode/edit/{mobile}")
    @Headers("Content-type:application/json;charset=UTF-8")
    suspend fun getSmsCode(
        @Path("mobile") mobile: String,
        @Body bean: CaptchaAli
    ): Response<ResponseBody>

    //    验证手机号码
    @GET("/seller/shops/clerks/smscode/{mobile}")
    @Headers("Content-type:application/json;charset=UTF-8")
    suspend fun checkSmsCode(
        @Path("mobile") mobile: String,
        @Query("sms_code") smsCode: String
    ): Response<ResponseBody>

    //    获取店铺
    @GET("seller/shops")
    @WithHiResponse
    fun getShop(): Call<ApplyShopInfo>

    //    修改店铺
    @POST("seller/shops/editShop")
    @WithHiResponse
    fun updateShop(@Body bean: ApplyShopInfo): Call<Unit>


    @GET("seller/shops/getQRCode")
    @WithHiResponse
    suspend fun getQRCode(): Response<ResponseBody>


    // 获取店铺海报二维码
    @GET("seller/shops/getPosterQRCode")
    @WithHiResponse
    suspend fun getNewQRCode(): Response<ResponseBody>

    //获取Banner图
    @GET("seller/shops/shopBanner")
    @WithHiResponse
    fun getBanner(): Call<List<BannerBean>>


    //上传banner图
    @POST("seller/shops/shopBanner/addShopBanner")
    @WithHiResponse
    fun updateBanner(@Body bean: List<BannerBean>): Call<Unit>

    // 内容列表
    @POST("/seller/app/content/queryContentList")
    @WithHiResponse
    fun queryContentList(@Body body: HelpDocReq): Call<PageRecordVo<HelpDocItemVo>>

    //
    @GET("/seller/shops/finalOpenShop")
    @WithHiResponse
    fun queryFinalOpenShop(): Call<Unit>

}