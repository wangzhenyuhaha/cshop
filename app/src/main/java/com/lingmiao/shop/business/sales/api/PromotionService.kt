package com.lingmiao.shop.business.sales.api

import com.james.common.netcore.networking.http.annotations.WithHiResponse
import com.lingmiao.shop.business.common.bean.PageVO
import com.lingmiao.shop.business.goods.api.bean.GoodsVOWrapper
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


    /**
     * 新增商品
     */
    @GET(" /seller/promotion/full-discounts")
    @WithHiResponse
    fun getDiscounts(@QueryMap map: MutableMap<String, Any>): Call<PageVO<SalesVo>>


    /**
     * 删除
     */
    @DELETE("seller/promotion/full-discounts/{id}")
    @WithHiResponse
    fun deleteDiscountById(@Path(value = "id") id: String) : Call<Unit>
}