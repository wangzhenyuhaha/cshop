package com.lingmiao.shop.business.main.api

import com.lingmiao.shop.business.common.bean.PageVO
import com.lingmiao.shop.business.main.bean.*
import com.lingmiao.shop.business.me.bean.AccountSetting
import com.james.common.netcore.networking.http.annotations.WithHiResponse
import com.lingmiao.shop.business.me.bean.IdentityVo
import com.lingmiao.shop.business.me.bean.VipType
import com.lingmiao.shop.business.wallet.bean.DataVO
import retrofit2.Call
import retrofit2.http.*

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

    @POST("seller/shops/editShopOpenStatus/{shop_id}")
    @WithHiResponse
    fun editShopStatus(@Path("shop_id") id : Int, @Body status : OpenShopStatusVo) : Call<Unit>

    //开店 经营类目列表
    @GET("seller/goods/category/0/children")
    @WithHiResponse
    fun getApplyShopCategory():  Call<List<ApplyShopCategory>>

    // 会员申请店铺
    @POST("seller/shops/apply")
    @WithHiResponse
    fun applyShopInfo(@Body bean:ApplyShopInfo):  Call<ApplyShopInfoResponse>

    //    获取店铺
    @GET("seller/shops")
    @WithHiResponse
    fun getShop():Call<ApplyShopInfo>

    // 查询身份
    @GET("seller/shops/identity/queryShopIdentity")
    @WithHiResponse
    fun getShopIdentity() : Call<DataVO<IdentityVo>>;

    // vip充值列表
    @GET("seller/shops/identity/querySaleProductList")
    @WithHiResponse
    fun getVipList() : Call<PageVO<VipType>>

    // 发起支付
    @GET("seller/shops/identity/{shop_id}")
    @WithHiResponse
    fun getPayInfo() : Call<String>;

    //    检测升级
    @GET("seller/app/upgrade")
    @WithHiResponse
    fun getUpgrade(@Query("current_version") version:String,
                   @Query("current_version_code") versionCode:String): Call<AccountSetting>
}