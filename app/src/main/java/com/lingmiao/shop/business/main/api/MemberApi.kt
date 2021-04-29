package com.lingmiao.shop.business.main.api

import com.james.common.netcore.networking.http.annotations.WithHiResponse
import com.lingmiao.shop.business.common.bean.PageVO
import com.lingmiao.shop.business.me.bean.MemberVo
import com.lingmiao.shop.business.sales.bean.UserVo
import com.lingmiao.shop.business.wallet.bean.DataVO
import retrofit2.Call
import retrofit2.http.GET
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

}