package com.lingmiao.shop.business.sales.api

import com.james.common.netcore.networking.http.annotations.WithHiResponse
import com.lingmiao.shop.business.common.bean.PageVO
import com.lingmiao.shop.business.sales.bean.SalesVo
import retrofit2.Call
import retrofit2.http.*

/**
Create Date : 2021/6/34:45 PM
Auther      : Fox
Desc        :
 **/
interface PromotionService {

    /**
     * 新增商品
     */
    @POST(" /seller/promotion/full-discounts")
    @WithHiResponse
    fun submitDiscount(@Body goods : SalesVo?): Call<SalesVo>

    @PUT("/seller/promotion/full-discounts/{id}")
    @WithHiResponse
    fun update(@Path(value = "id") id: String?, @Body item : SalesVo) : Call<SalesVo>

    @GET("/seller/promotion/full-discounts/{id}")
    @WithHiResponse
    fun getDiscountById(@Path(value = "id") id: String) : Call<SalesVo>

    /**
     * 删除
     */
    @DELETE("seller/promotion/full-discounts/{id}")
    @WithHiResponse
    fun deleteDiscountById(@Path(value = "id") id: String) : Call<Unit>

    /**
     * 修改满减优惠活动结束时间-立即结束
     */
    @PUT("seller/promotion/full-discounts/end/{id}")
    @WithHiResponse
    fun endDiscount(@Path(value = "id") id: String) : Call<Unit>

    /**
     * 获取列表
     */
    @GET(" /seller/promotion/full-discounts")
    @WithHiResponse
    fun getDiscounts(@QueryMap map: MutableMap<String, Any>): Call<PageVO<SalesVo>>

}