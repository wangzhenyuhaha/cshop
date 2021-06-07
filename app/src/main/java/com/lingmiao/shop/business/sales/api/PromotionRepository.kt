package com.lingmiao.shop.business.sales.api

import com.james.common.netcore.networking.http.core.HiResponse
import com.james.common.netcore.networking.http.core.awaitHiResponse
import com.lingmiao.shop.business.common.bean.PageVO
import com.lingmiao.shop.business.sales.bean.SalesVo
import com.lingmiao.shop.net.Fetch

/**
Create Date : 2021/6/34:44 PM
Auther      : Fox
Desc        :
 **/
object PromotionRepository {

    private val apiService by lazy {
        Fetch.createService(PromotionService::class.java)
    }

    suspend fun submitDiscount(item : SalesVo?) : HiResponse<SalesVo> {
        return apiService.submitDiscount(item).awaitHiResponse();
    }

    suspend fun getDiscounts(pageNo: Int): HiResponse<PageVO<SalesVo>> {
        val map = mutableMapOf<String, Any>()
        map.put("page_no", pageNo)
        map.put("page_size", 10)
        return apiService.getDiscounts(map).awaitHiResponse();
    }

    suspend fun deleteById(id : String) : HiResponse<Unit> {
        return apiService.deleteDiscountById(id).awaitHiResponse();
    }

}