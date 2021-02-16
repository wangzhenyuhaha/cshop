package com.lingmiao.shop.business.main.api

import com.lingmiao.shop.business.common.bean.PageVO
import com.lingmiao.shop.business.main.bean.*
import com.lingmiao.shop.business.me.bean.AccountSetting
import com.lingmiao.shop.business.me.bean.ShopManage
import com.lingmiao.shop.business.order.bean.OrderList
import com.james.common.netcore.networking.http.annotations.WithHiResponse
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface MainApi {

//APP首页数据
    @GET("seller/statistics/dashboard/appIndex")
    @WithHiResponse
    fun getMainInfo(): Call<MainInfo>

//    查询店铺站内消息列表
    @GET("seller/shops/shop-notice-logs")
    @WithHiResponse
    fun getMessageCenterList(@Query("page_no") pageIndex:Int,@Query("page_size") pageSize:Int): Call<MessageCenterResponse>

    //    查询店铺状态
    @GET("seller/shops/statusInfo")
    @WithHiResponse
    fun getShopStatus(): Call<ShopStatus>

//开店 经营类目列表
    @GET("seller/goods/category/0/children")
    @WithHiResponse
    fun getApplyShopCategory():  Call<List<ApplyShopCategory>>

//    会员申请店铺
    @POST("seller/login/apply")
    @WithHiResponse
    fun applyShopInfo(@Body bean:ApplyShopInfo):  Call<ApplyShopInfoResponse>

    //    获取店铺
    @GET("seller/shops")
    @WithHiResponse
    fun getShop():Call<ApplyShopInfo>


    //    检测升级
    @GET("seller/app/upgrade")
    @WithHiResponse
    fun getUpgrade(@Query("current_version") version:String,
                   @Query("current_version_code") versionCode:String): Call<AccountSetting>
}