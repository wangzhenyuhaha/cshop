package com.lingmiao.shop.business.main.bean
import com.google.gson.annotations.SerializedName

data class MainInfo(
    @SerializedName("after_sale_order_num")
    var afterSaleOrderNum: String?,//待处理售后订单数
    @SerializedName("all_orders_num")
    var allOrdersNum: String?,//所有订单数
    @SerializedName("day_income")
    var dayIncome: Double?,//今日营业额
    @SerializedName("market_goods")
    var marketGoods: String?,//出售中的商品
    @SerializedName("new_visit_num")
    var newVisitNum: Int?,//新增访客数
    @SerializedName("pending_goods")
    var pendingGoods: String?,//待上架的商品
    @SerializedName("pending_member_ask")
    var pendingMemberAsk: String?,//待处理买家咨询
    @SerializedName("visit_num")
    var visitNum: Int?,//当日访客数
    @SerializedName("wait_delivery_order_num")
    var waitDeliveryOrderNum: String?,//待收货订单数
    @SerializedName("wait_pay_order_num")
    var waitPayOrderNum: String?,//待付款订单数
    @SerializedName("wait_ship_order_num")
    var waitShipOrderNum: String?,//待发货订单数
    /**
     * 团长总数
     */
    @SerializedName("distributor_num")
    var distributorNum: Int? = 0,
    /**
     * 团长订单数
     */
    @SerializedName("distributor_order_num")
    var distributorOrderNum: Double? = 0.0,
    /**
     * 团长出单率
     */
    @SerializedName("distributor_order_ratio")
    var distributorOrderRatio: Double? = 0.0,

    /**
     * 昨日新增团长
     */
    @SerializedName("last_day_new_distributor_num")
    var lastDayNewDistributorNum: Int? = 0,
    /**
     * 昨日新增会员
     */
    @SerializedName("last_day_new_member_num")
    var lastDayNewMemberNum: Int = 0,
    /**
     * 昨日新增自提点
     */
    @SerializedName("last_day_new_site_num")
    var lastDayNewSiteNum: Int? = 0,
    /**
     * 昨日客单价
     */
    @SerializedName("last_day_order_avg_price")
    var lastDayOrderAvgPrice: Double? = 0.0,
    /**
     * 昨日支付订单数
     */
    @SerializedName("last_day_paid_order_num")
    var lastDayPaidOrderNum: Int? = 0,
    /**
     * 昨日交易额
     */
    @SerializedName("last_day_trade_amount")
    var lastDayTradeAmount: Double? = 0.0,

    /**
     * 自提点总数
     */
    @SerializedName("site_num")
    var siteNum: Int? = 0,
    /**
     * 点均订单数
     */
    @SerializedName("site_order_num")
    var siteOrderNum: Double? = 0.0,
    /**
     * 自提点出单率
     */
    @SerializedName("site_order_ratio")
    var siteOrderRatio: Double? = 0.0,
    /**
     * 客单价
     */
    @SerializedName("order_avg_price")
    var orderAvgPrice: Double? = 0.0,
    /**
     * 支付订单数
     */
    @SerializedName("paid_order_num")
    var paidOrderNum: Int? = 0,
    /**
     * 交易额
     */
    @SerializedName("trade_amount")
    var tradeAmount: Double? = 0.0,
    /**
     * 是否是社区团购店铺
     */
    @SerializedName("community_shop")
    var communityShop: Int? = 0,
    /**
     * 昨日活跃团长
     */
    @SerializedName("last_day_active_distributor_num")
    var lastDayActiveDistributorNum: Int? = 0
)