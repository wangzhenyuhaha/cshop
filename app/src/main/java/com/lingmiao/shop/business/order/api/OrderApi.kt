package com.lingmiao.shop.business.order.api

import com.lingmiao.shop.business.common.bean.PageVO
import com.lingmiao.shop.business.order.bean.*
import com.lingmiao.shop.business.wallet.bean.DataVO
import com.james.common.net.BaseResponse
import com.james.common.netcore.networking.http.annotations.WithHiResponse
import com.james.common.netcore.networking.http.core.HiResponse
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.*

interface OrderApi {
    //    订单列表
    @GET("seller/trade/orders")
    @WithHiResponse
    fun getOrderList(
        @Query("page_no") pageNo: String, @Query("page_size") pageSize: String,
        @Query("order_status") orderStatus: String,
        @Query("start_time") start : Long?, @Query("end_time") end : Long?,
        @Query("channel") channel: String = "app"
    ): Call<PageVO<OrderList>>

    @GET("seller/trade/orders")
    @WithHiResponse
    fun memberOrderList(
        @Query("page_no") pageNo: Int, @Query("page_size") pageSize: Int,@Query("member_id") memberId: String = ""
    ): Call<PageVO<OrderList>>


    @GET("seller/trade/orders")
    @WithHiResponse
    fun search(
        @Query("page_no") pageNo: Int, @Query("page_size") pageSize: Int,@Query("keywords") keywords: String = ""
    ): Call<PageVO<OrderList>>

    //查询订单状态的数量
    @GET("seller/trade/orders/status-num")
    @WithHiResponse
    fun getOrderStatusNumber(): Call<OrderNumber>


    //    修改价格
    @PUT("seller/trade/orders/{order_sn}/app/price")
    suspend fun getUpdatePrice(
        @Path("order_sn") orderSn: String,
        @Query("goods_price") orderAmount: String, @Query("ship_price") shippingAmount: String
    ): Response<ResponseBody>

    //    订单详情
    @GET("seller/trade/orders/{order_sn}")
    @WithHiResponse
    fun getOrderDetail(@Path("order_sn") orderSn: String): Call<OrderDetail>

    //    取消订单
    @POST("seller/after-sales/sellerRefund/{order_sn}")
    suspend fun orderCancel(
        @Path("order_sn") orderId: String,
        @Query("cancelReason") cancelReason: String,
        @Query("cancel_reason") reason: String,
        @Query("remark") desc: String
    ): Response<ResponseBody>

    //    删除订单
    @DELETE("seller/trade/orders/{order_sn}")
    suspend fun deleteOrder(
        @Path("order_sn") orderSn: String
    ): Response<ResponseBody>

    // 接单/拒单
    @GET("/seller/trade/orders/{order_sn}/{is_accept}/accept")
    @WithHiResponse
    suspend fun takeOrder(@Path("order_sn") orderSn: String, @Path("is_accept") is_accept: Int): Response<ResponseBody>

    // 商家审核售后订单
    @POST("/seller/after-sales/audits/{sn}")
    @WithHiResponse
    suspend fun service(@Path("sn") orderSn: String, @Body service : OrderServiceVo): Response<ResponseBody>

    // 订单发货
    @GET("/seller/trade/orders/sellerApp/delivery/{sn}")
    @WithHiResponse
    suspend fun ship(@Path("sn") orderSn: String): Response<ResponseBody>

    // 备货完成
    @GET("/seller/trade/orders/prepare/{order_sn}")
    @WithHiResponse
    suspend fun prepare(@Path("order_sn") orderSn: String) : Response<ResponseBody>;

    //  商家订单已送达
    @GET("/seller/trade/orders/sellerApp/confirmRog/{sn}")
    @WithHiResponse
    suspend fun sign(@Path("sn") orderSn: String): Response<ResponseBody>

    //    发货接口
    @POST("seller/trade/orders/{order_sn}/delivery")
    suspend fun requestOrderSendGoodsData(
        @Path("order_sn") orderId: String, @Query("logi_name") logisticsCompany: String,
        @Query("ship_no") logisticsNumber: String, @Query("logi_id") logisticsId: String
    ): Response<ResponseBody>

    //    物流公司列表
    @GET("seller/shops/logi-companies")
    @WithHiResponse
    fun requestLogisticsCompanyList(): Call<List<LogisticsCompanyItem>>


    //    查询物流详细
    @GET("seller/express")
    @WithHiResponse
    fun requestLogisticsInfoData(
        @Query("num") shipNo: String,
        @Query("id") logiId: String
    ): Call<LogisticsInfo>

    //    获取售后/退款详情
    @GET("seller/after-sales/refunds/queryByOrderSn")
    @WithHiResponse
    fun getAfterSale(@Query("order_sn") orderId: String): Call<AfterSale>

    //    审核退款操作  agree 是否同意退款:同意 1，不同意 0
    @POST("seller/after-sales/audits/{sn}")
    suspend fun doAfterSaleAction(
        @Path("sn") refundId: String, @Body item : RefoundReqVo): Response<ResponseBody>


    //    入库操作
    @POST("seller/after-sales/stock-ins/{sn}")
    suspend fun doAfterSaleStock(@Path("sn") sn: String): Response<ResponseBody>

    //    点击核销按钮请求路径
    @GET("seller/trade/orders/verification_code")
    @WithHiResponse
    fun verificationCode(@Query("verification_code") code: String): Call<OrderList>

    //    点击核销按钮请求路径
    @GET("seller/trade/orders/virtualOrderShip/{verification_code}")
    @WithHiResponse
    fun verifyOrderShip(@Path("verification_code") code: String): Call<BaseDesc>

    //    点击核销按钮请求路径(自提)
    @POST("seller/trade/orders/{order_sn}/complete")
    @WithHiResponse
    fun verifyOrderShipSelf(@Path("order_sn") code: String): Call<Unit>

}