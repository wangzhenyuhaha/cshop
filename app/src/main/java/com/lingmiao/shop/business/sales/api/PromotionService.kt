package com.lingmiao.shop.business.sales.api

import com.james.common.netcore.networking.http.annotations.WithHiResponse
import com.lingmiao.shop.business.common.bean.PageVO
import com.lingmiao.shop.business.sales.bean.Coupon
import com.lingmiao.shop.business.sales.bean.ElectronicVoucher
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
    fun submitDiscount(@Body goods: SalesVo?): Call<SalesVo>

    //新增优惠券/电子券
    @POST("/seller/promotion/coupons")
    @WithHiResponse
    fun submitCoupons(@QueryMap map: MutableMap<String, Any>): Call<Unit>

    //查询优惠券/电子券
    @GET("/seller/promotion/coupons")
    @WithHiResponse
    fun searchCoupons(@QueryMap map: MutableMap<String, Any>): Call<PageVO<Coupon>>

    //查询电子券
    @GET("/seller/promotion/coupons")
    @WithHiResponse
    fun searchElectronicVoucher(@QueryMap map: MutableMap<String, Any>): Call<PageVO<ElectronicVoucher>>

    //删除优惠券
    @DELETE("/seller/promotion/coupons/{id}")
    @WithHiResponse
    fun deleteCoupon(@Path(value = "id") id: Int): Call<Unit>

    //修改优惠券是否发放
    @GET("/seller/promotion/coupons/{id}/{disable}")
    @WithHiResponse
    fun editCoupon(
        @Path(value = "id") id: Int,
        @Path(value = "disable") disabled: Int
    ): Call<Unit>


    @PUT("/seller/promotion/full-discounts/{id}")
    @WithHiResponse
    fun update(@Path(value = "id") id: String?, @Body item: SalesVo): Call<SalesVo>

    @GET("/seller/promotion/full-discounts/{id}")
    @WithHiResponse
    fun getDiscountById(@Path(value = "id") id: String): Call<SalesVo>

    /**
     * 删除
     */
    @DELETE("seller/promotion/full-discounts/{id}")
    @WithHiResponse
    fun deleteDiscountById(@Path(value = "id") id: String): Call<Unit>

    /**
     * 修改满减优惠活动结束时间-立即结束
     */
    @PUT("seller/promotion/full-discounts/end/{id}")
    @WithHiResponse
    fun endDiscount(@Path(value = "id") id: String): Call<Unit>

    /**
     * 获取列表
     */
    @GET(" /seller/promotion/full-discounts")
    @WithHiResponse
    fun getDiscounts(@QueryMap map: MutableMap<String, Any>): Call<PageVO<SalesVo>>

}