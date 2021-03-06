package com.lingmiao.shop.business.sales.api

import com.james.common.netcore.networking.http.core.HiResponse
import com.james.common.netcore.networking.http.core.awaitHiResponse
import com.lingmiao.shop.business.common.bean.PageVO
import com.lingmiao.shop.business.sales.bean.Coupon
import com.lingmiao.shop.business.sales.bean.ElectronicVoucher
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

    suspend fun submitDiscount(item: SalesVo?): HiResponse<SalesVo> {
        return apiService.submitDiscount(item).awaitHiResponse()
    }

    suspend fun submitCoupons(item: Coupon): HiResponse<Unit> {
        val map = mutableMapOf<String, Any>()
        item.apply {
            title?.let { map.put("title", it) }
            couponStartTime?.let { map.put("start_time", it) }
            couponEndTime?.let { map.put("end_time", it) }
            useTimeType?.let { map.put("use_time_type", it) }
            useStartTime?.let { map.put("use_start_time", it) }
            useEndTime?.let { map.put("use_end_time", it) }
            usePeriod?.let { map.put("use_period", it) }
            createNum?.let { map.put("create_num", it) }
            manPrice?.let { map.put("coupon_threshold_price", it) }
            jianPrice?.let { map.put("coupon_price", it) }
            limitNum?.let { map["limit_num"] = it }
            map["coupon_type"] = "GOODS"
        }
        return apiService.submitCoupons(map).awaitHiResponse()
    }

    //默认满1.0减，默认没人限领与库存相等
    suspend fun submitElectronicVoucher(item: ElectronicVoucher): HiResponse<Unit> {
        val map = mutableMapOf<String, Any>()
        item.apply {
            title?.let { map.put("title", it) }
            couponStartTime?.let { map.put("start_time", it) }
            couponEndTime?.let { map.put("end_time", it) }
            useTimeType?.let { map.put("use_time_type", it) }
            useStartTime?.let { map.put("use_start_time", it) }
            useEndTime?.let { map.put("use_end_time", it) }
            map["create_num"] = 0
            map["coupon_threshold_price"] = 1.0
            jianPrice?.let { map.put("coupon_price", it) }
            map["limit_num"] = 0
            map["coupon_type"] = "TICKET"
            goodsID?.let { map["goods_id"] = it }
        }
        return apiService.submitCoupons(map).awaitHiResponse()
    }

    suspend fun searchCoupons(pageNo: Int): HiResponse<PageVO<Coupon>> {
        val map = mutableMapOf<String, Any>()
        map["page_no"] = pageNo
        map["page_size"] = 10
        map["coupon_type"] = "GOODS"
        return apiService.searchCoupons(map).awaitHiResponse()
    }

    suspend fun searchElectronicVoucher(pageNo: Int): HiResponse<PageVO<ElectronicVoucher>> {
        val map = mutableMapOf<String, Any>()
        map["page_no"] = pageNo
        map["page_size"] = 10
        map["coupon_type"] = "TICKET"
        return apiService.searchElectronicVoucher(map).awaitHiResponse()
    }

    //删除优惠券
    suspend fun deleteCoupons(id: Int): HiResponse<Unit> {
        return apiService.deleteCoupon(id).awaitHiResponse()
    }

    //修改优惠券
    suspend fun editCoupon(disabled: Int, id: Int): HiResponse<Unit> {
        return apiService.editCoupon(id, disabled).awaitHiResponse()
    }

    suspend fun getDiscounts(pageNo: Int): HiResponse<PageVO<SalesVo>> {
        val map = mutableMapOf<String, Any>()
        map["page_no"] = pageNo
        map["page_size"] = 10
        return apiService.getDiscounts(map).awaitHiResponse()
    }

    suspend fun getDiscountById(id: String): HiResponse<SalesVo> {
        return apiService.getDiscountById(id).awaitHiResponse()
    }

    suspend fun update(item: SalesVo?): HiResponse<SalesVo> {
        return apiService.update(item?.id!!, item).awaitHiResponse()
    }

    suspend fun deleteById(id: String): HiResponse<Unit> {
        return apiService.deleteDiscountById(id).awaitHiResponse()
    }

    suspend fun endDiscount(id: String): HiResponse<Unit> {
        return apiService.endDiscount(id).awaitHiResponse()
    }

}