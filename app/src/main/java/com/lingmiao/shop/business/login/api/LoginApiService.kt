package com.lingmiao.shop.business.login.api

import com.lingmiao.shop.business.login.bean.CaptchaAli
import com.lingmiao.shop.business.login.bean.LoginInfo
import com.lingmiao.shop.business.login.bean.LoginRequest
import com.lingmiao.shop.business.login.bean.RefreshTokenResponse
import com.lingmiao.shop.business.me.bean.ForgetPassword
import com.james.common.net.BaseResponse
import com.james.common.netcore.networking.http.annotations.WithHiResponse
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.Headers
import retrofit2.http.POST
import retrofit2.http.Query

interface LoginApiService {
    @POST("/seller/login/smscode")
    @Headers("Content-type:application/json;charset=UTF-8")
    fun getLoginSmsCode(@Body bean: CaptchaAli): Call<BaseResponse>

    @POST("/seller/register/smscode")
    @Headers("Content-type:application/json;charset=UTF-8")
    @WithHiResponse
    fun getRegisterSmsCode(@Body bean: CaptchaAli): Call<BaseResponse>

    @POST("/seller/register/app")
    @Headers("Content-type:application/json;charset=UTF-8")
    @WithHiResponse
    fun register(
        @Query("mobile") mobile: String, @Query("password") password: String,
        @Query("sms_code") smsCode: String
    ): Call<LoginInfo>

    @POST("/seller/login/app")
    fun login(@Body loginRequest: LoginRequest): Call<LoginInfo>

    @POST("/seller/check/token")
    fun refreshToken(@Query("refresh_token") token: String): Call<RefreshTokenResponse>


    //    忘记密码
    @POST("/seller/login/changePassword")
    @Headers("Content-type:application/json;charset=UTF-8")
    suspend fun forgetPassword(@Body bean: ForgetPassword): Response<ResponseBody>
}