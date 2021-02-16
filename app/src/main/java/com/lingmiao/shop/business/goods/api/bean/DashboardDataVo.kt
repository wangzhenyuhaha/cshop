package com.lingmiao.shop.business.goods.api.bean
import com.google.gson.annotations.SerializedName


data class DashboardDataVo(
    @SerializedName("after_sale_order_num")
    var afterSaleOrderNum: String? = "",
    @SerializedName("all_orders_num")
    var allOrdersNum: String? = "",
    /**
     * 出售中的商品数
     */
    @SerializedName("market_goods")
    var marketGoods: Int? = 0,
    /**
     * 已下架的商品数
     */
    @SerializedName("pending_goods")
    var pendingGoods: Int? = 0,
    /**
     * 待审核商品数(服务端取错了暂时不改)
     */
    @SerializedName(value = "disabled_goods")
    var disabledGoods : Int? = 0,
    @SerializedName("pending_member_ask")
    var pendingMemberAsk: String? = "",
    @SerializedName("wait_delivery_order_num")
    var waitDeliveryOrderNum: String? = "",
    @SerializedName("wait_pay_order_num")
    var waitPayOrderNum: String? = "",
    @SerializedName("wait_ship_order_num")
    var waitShipOrderNum: String? = ""
)