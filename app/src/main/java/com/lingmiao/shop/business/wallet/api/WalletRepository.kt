package com.lingmiao.shop.business.wallet.api

import com.lingmiao.shop.base.IConstant
import com.lingmiao.shop.business.wallet.bean.*
import com.lingmiao.shop.net.Fetch
import com.james.common.netcore.networking.http.core.HiResponse
import com.james.common.netcore.networking.http.core.awaitHiResponse
import com.lingmiao.shop.business.common.bean.PageVO
import com.lingmiao.shop.business.order.bean.OrderList
import retrofit2.Call

/**
 * Author : Elson
 * Date   : 2020/7/16
 * Desc   :
 */
object WalletRepository {

    private val apiService by lazy {
        Fetch.createService(WalletApiService::class.java)
    }

    /**
     * 钱包首页
     */
    suspend fun getWalletIndexInfo(): HiResponse<DataVO<WalletVo>> {
        return apiService.getWalletIndexInfo().awaitHiResponse();
    }

    /**
     * 余额数据
     */
    suspend fun getBalanceInfo(): HiResponse<DataVO<WalletVo>> {
        return apiService.getBalanceInfo().awaitHiResponse();
    }

    //骑手备付金
    suspend fun getRiderMoney(): HiResponse<DataVO<WalletVo>> {
        return apiService.getRiderMoney().awaitHiResponse()
    }

    /**
     * 押金数据
     */
    suspend fun getDepositInfo(): HiResponse<DataVO<WalletVo>> {
        return apiService.getDepositInfo().awaitHiResponse();
    }

    /**
     * 交易记录列表
     */
    suspend fun getRecordList(
        pageNo: Int,
        accountId: String
    ): HiResponse<DataVO<PageRecordVo<DepositVo>>> {
        val body = TradeReqBody();
        body.accountId = accountId;

        val req = TradeReqVo();
        req.pageNum = pageNo;
        req.pageSize = IConstant.PAGE_SIZE;
        req.body = body;
        return apiService.queryTradeRecordList(req).awaitHiResponse()
    }

    //与骑手的交易记录列表
    suspend fun getRiderRecordList(
        pageNo: Int,
        accountId: String
    ): HiResponse<DataVO<PageRecordVo<DepositVo>>> {
        val body = TradeReqBody()
        body.accountId = accountId

        val req = TradeReqVo()
        req.pageNum = pageNo
        req.pageSize = IConstant.PAGE_SIZE
        req.body = body
        return apiService.queryRiderRecordList(req).awaitHiResponse()
    }

    /**
     *
    查询会员订单列表
     */
    suspend fun getOrderList(pageNo: Int): HiResponse<PageVO<OrderList>> {
        return apiService.queryTradeOrdersList(pageNo, 10).awaitHiResponse()
    }

    /**
     * 提现费率
     */
    suspend fun queryServiceChargeRate(): HiResponse<RateVo> {
        return apiService.queryServiceChargeRate().awaitHiResponse()
    }

    /**
     * 查询账户信息
     */
    suspend fun getWithdrawAccountInfo(): HiResponse<WithdrawAccountVo> {
        return apiService.queryWithdrawAccount().awaitHiResponse();
    }

    /**
     * 绑定提现账户信息
     */
    suspend fun submitWithdrawAccount(data: WithdrawAccountBean): HiResponse<DataVO<Unit>> {
        return apiService.submitWithdrawAccount(data).awaitHiResponse();
    }

    /**
     * 修改提现账户信息
     */
    suspend fun updateWithdrawAccount(data: WithdrawAccountBean): HiResponse<DataVO<Unit>> {
        return apiService.updateWithdrawAccount(data).awaitHiResponse();
    }

    /**
     * 绑银行卡
     */
    suspend fun bindBankCard(data: BankCardVo): HiResponse<Unit> {
        return apiService.bindBankCard(data).awaitHiResponse();
    }

    //申请店铺时绑卡
    suspend fun bindBankCardMember(data: BankCardVo): HiResponse<Unit> {
        return apiService.bindBankCard(data).awaitHiResponse();
    }

    /**
     * 解绑
     */
    suspend fun unbindBankCard(id: String): HiResponse<Unit> {
        val map = mutableMapOf<String, Any>()
        map.put("bank_card_id", id)
        return apiService.unbindBankCard(map).awaitHiResponse();
    }

    /**
     * 银行卡列表
     */
    suspend fun queryBankCardList(): HiResponse<PageListVo<BankCardAccountBean>> {
        return apiService.queryBankCardList().awaitHiResponse();
    }

    /**
     * 申请提现
     */
    suspend fun applyWithdraw(data: WithdrawApplyVo): HiResponse<ApplyWithdrawVo> {
        return apiService.applyWithdraw(data).awaitHiResponse();
    }

    /**
     * 提现记录
     */
    suspend fun queryWithdrawList(pageNo: Int): HiResponse<PageRecordVo<BalanceVo>> {
        val map = mutableMapOf<String, Any>()
        map.put("page_num", pageNo)
        map.put("page_size", IConstant.PAGE_SIZE);
        map.put("body", Any());
        return apiService.queryWithdrawList(map).awaitHiResponse();
    }

    //获取订单详情
    suspend fun queryOrderDetail(orderNo: String): HiResponse<OrderList> {
        return apiService.getOrderDetail(orderNo).awaitHiResponse()
    }
}