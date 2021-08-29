package com.lingmiao.shop.business.main.api

import com.james.common.netcore.networking.http.annotations.WithHiResponse
import com.lingmiao.shop.business.common.bean.PageVO
import com.lingmiao.shop.business.main.bean.ElectricSign
import com.lingmiao.shop.business.main.bean.MemberOrderBean
import com.lingmiao.shop.business.sales.bean.UserVo
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

/**
Create Date : 2021/4/295:27 PM
Auther      : Fox
Desc        :
 **/
interface MemberApi {

    // 查询店铺会员列表 - 所有
    @GET("seller/shops/members/list-all")
    @WithHiResponse
    fun getMemberList(@Query("page_no") pageIndex:Int, @Query("page_size") pageSize:Int): Call<PageVO<UserVo>>


    // 查询店铺会员列表 - 所有
    @GET("seller/shops/members/list-recent")
    @WithHiResponse
    fun getMemberListOfNew(@Query("page_no") pageIndex:Int, @Query("page_size") pageSize:Int): Call<PageVO<UserVo>>

    @GET("seller/shops/members/member-buy-count")
    @WithHiResponse
    fun getBuyCountOfMember(@Query("member_id") memberId : String) : Call<MemberOrderBean>;

    @GET("seller/shops/allinpay/electSign/{shop_id}")
    @WithHiResponse
    fun electricSign(@Path("shop_id") shop_id : String) : Call<ElectricSign>

    @GET("/seller/shops/allinpay/repaircusrgcUrl/{shop_id}")
    @WithHiResponse
    fun applySupplementUrl(@Path("shop_id") shop_id : String) : Call<ElectricSign>

}