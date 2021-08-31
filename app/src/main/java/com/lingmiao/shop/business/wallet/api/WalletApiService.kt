package com.lingmiao.shop.business.wallet.api

import com.lingmiao.shop.business.wallet.bean.*
import com.james.common.netcore.networking.http.annotations.WithHiResponse
import com.lingmiao.shop.business.common.bean.PageVO
import com.lingmiao.shop.business.order.bean.OrderList
import retrofit2.Call
import retrofit2.http.*

/**
 * Author : Elson
 * Date   : 2020/7/16
 * Desc   :
 */
interface WalletApiService {

    /**
     * 钱包首页
     */
    @WithHiResponse
    @GET("account/walletIndex")
    fun getWalletIndexInfo(): Call<DataVO<WalletVo>>;

    @WithHiResponse
    @GET("account/balanceIndex")
    fun getBalanceInfo(): Call<DataVO<WalletVo>>;

    @WithHiResponse
    @GET("account/depositIndex")
    fun getDepositInfo(): Call<DataVO<WalletVo>>;

    /**
     * 交易记录列表
     */
    @WithHiResponse
    @POST("account/queryTradeRecordList")
    fun queryTradeRecordList(@Body body: TradeReqVo): Call<DataVO<PageRecordVo<DepositVo>>>

    /**
     * 交易记录列表
     */
    @WithHiResponse
    @GET("seller/trade/orders")
    fun queryTradeOrdersList(
        @Query("page_no") pageNo: Int,
        @Query("page_size") pageSize: Int
    ): Call<PageVO<OrderList>>

    /**
     * 查询提现账号信息
     */
    @WithHiResponse
    @POST("account/withdraw/queryWithdrawAcount")
    fun queryWithdrawAccount(): Call<WithdrawAccountVo>;

    /**
     * 提现费率
     */
    @WithHiResponse
    @POST("account/withdraw/queryServiceChargeRate")
    fun queryServiceChargeRate(): Call<RateVo>;

    /**
     * 绑定提现账户信息（支付宝/微信)
     */
    @WithHiResponse
    @POST("account/submitWithdrawAccount")
    fun submitWithdrawAccount(@Body data: WithdrawAccountBean): Call<DataVO<Unit>>;

    /**
     * 修改提现账户信息
     */
    @WithHiResponse
    @POST("account/updateWithdrawAccount")
    fun updateWithdrawAccount(@Body data: WithdrawAccountBean): Call<DataVO<Unit>>;

    /**
     * 提现记录
     */
    @WithHiResponse
    @POST("account/withdraw/queryWithdrawList")
    fun queryWithdrawList(@Body body: MutableMap<String, Any>): Call<PageRecordVo<BalanceVo>>;

    /**
     * 绑定银行卡
     */
    @WithHiResponse
    @POST("account/bindBankCard")
    fun bindBankCard(@Body data: BankCardVo): Call<Unit>;

    /**
     * 解绑银行卡
     */
    @WithHiResponse
    @POST("account/unBindBankCard")
    fun unbindBankCard(@Body body: MutableMap<String, Any>): Call<Unit>;


    /**
     * 银行卡列表
     */
    @WithHiResponse
    @POST("account/queryBankCardList")
    fun queryBankCardList(): Call<PageListVo<BankCardAccountBean>>;

    /**
     * 申请提现
     */
    @POST("account/withdraw/applyWithdraw")
    @WithHiResponse
    fun applyWithdraw(@Body data: WithdrawApplyVo): Call<ApplyWithdrawVo>;

    //    订单详情
    @GET("seller/trade/orders/{order_sn}")
    @WithHiResponse
    fun getOrderDetail(@Path("order_sn") orderSn: String): Call<OrderList>

}