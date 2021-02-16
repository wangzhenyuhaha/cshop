package com.lingmiao.shop.business.order.bean
import com.google.gson.annotations.SerializedName


data class AfterSale(
    @SerializedName("refund")
    var refund: Refund?,
    @SerializedName("refund_goods_dto")
    var refundGoods: List<RefundGood>?
)

data class Refund(
    @SerializedName("account_type")
    var accountType: String?,
    @SerializedName("account_type_text")
    var accountTypeText: String?,
    @SerializedName("after_sale_operate_allowable")
    var afterSaleOperateAllowable: AfterSaleOperateAllowable?,
    @SerializedName("bank_account_name")
    var bankAccountName: String?,
    @SerializedName("bank_account_number")
    var bankAccountNumber: String?,
    @SerializedName("bank_deposit_name")
    var bankDepositName: String?,
    @SerializedName("bank_name")
    var bankName: String?,
    @SerializedName("create_time")
    var createTime: Long?,
    @SerializedName("customer_remark")
    var customerRemark: String?,
    @SerializedName("finance_remark")
    var financeRemark: String?,
    @SerializedName("id")
    var id: Int?,
    @SerializedName("is_shipped")
    var isShipped: Int?,
    @SerializedName("member_id")
    var memberId: Int?,
    @SerializedName("member_name")
    var memberName: String?,
    @SerializedName("nick_name")
    var nickName: String?,
    @SerializedName("order_sn")
    var orderSn: String?,
    @SerializedName("order_status")
    var orderStatus: String?,
    @SerializedName("pay_order_no")
    var payOrderNo: String?,
    @SerializedName("payment_type")
    var paymentType: String?,
    @SerializedName("refund_fail_reason")
    var refundFailReason: String?,
    @SerializedName("refund_gift")
    var refundGift: String?,
    @SerializedName("refund_point")
    var refundPoint: Int?,
    @SerializedName("refund_price")
    var refundPrice: Double?,
    @SerializedName("refund_reason")
    var refundReason: String?,
    @SerializedName("refund_status")
    var refundStatus: String?,
    @SerializedName("refund_status_text")
    var refundStatusText: String?,
    @SerializedName("refund_time")
    var refundTime: Long?,
    @SerializedName("refund_type")
    var refundType: String?,
    @SerializedName("refund_type_text")
    var refundTypeText: String?,
    @SerializedName("refund_way")
    var refundWay: String?,
    @SerializedName("refuse_reason")
    var refuseReason: String?,
    @SerializedName("refuse_type")
    var refuseType: String?,
    @SerializedName("refuse_type_text")
    var refuseTypeText: String?,
    @SerializedName("return_account")
    var returnAccount: String?,
    @SerializedName("seller_id")
    var sellerId: Int?,
    @SerializedName("seller_name")
    var sellerName: String?,
    @SerializedName("seller_remark")
    var sellerRemark: String?,
    @SerializedName("sn")
    var sn: String?,
    @SerializedName("trade_sn")
    var tradeSn: String?,
    @SerializedName("warehouse_remark")
    var warehouseRemark: String?
)

data class RefundGood(
    @SerializedName("goods_id")
    var goodsId: Int?,
    @SerializedName("goods_image")
    var goodsImage: String?,
    @SerializedName("goods_name")
    var goodsName: String?,
    @SerializedName("goods_sn")
    var goodsSn: String?,
    @SerializedName("id")
    var id: Int?,
    @SerializedName("price")
    var price: Double?,
    @SerializedName("refund_sn")
    var refundSn: String?,
    @SerializedName("return_num")
    var returnNum: Int?,
    @SerializedName("ship_num")
    var shipNum: Int?,
    @SerializedName("sku_id")
    var skuId: Int?,
    @SerializedName("spec_json")
    var specJson: String?,
    @SerializedName("storage_num")
    var storageNum: Int?
)

data class AfterSaleOperateAllowable(
    @SerializedName("allow_admin_refund")
    var allowAdminRefund: Boolean?,
    @SerializedName("allow_apply")
    var allowApply: Boolean?,
    @SerializedName("allow_cancel")
    var allowCancel: Boolean?,
    @SerializedName("allow_seller_approval")
    var allowSellerApproval: Boolean?,
    @SerializedName("allow_seller_refund")
    var allowSellerRefund: Boolean?,
    @SerializedName("allow_stock_in")
    var allowStockIn: Boolean?
)

data class AfterSaleSpec(
    var specId: Int?,
    var specImage: String?,
    var specName: String?,
    var specType: Int?,
    var specValue: String?,
    var specValueId: Int?
)

//account_type	string
//退款账户类型
//
//account_type_text	string
//退款账户类型文字描述
//
//after_sale_operate_allowable	AfterSaleOperateAllowable{...}
//bank_account_name	string
//银行开户名
//
//bank_account_number	string
//银行账号
//
//bank_deposit_name	string
//银行开户行
//
//bank_name	string
//银行名称
//
//create_time	integer($int64)
//创建时间
//
//customer_remark	string
//客户备注
//
//finance_remark	string
//财务备注
//
//is_shipped	integer($int32)
//member_id	integer($int32)
//会员id
//
//member_name	string
//会员名称
//
//order_sn	string
//订单编号
//
//order_status	string
//pay_order_no	string
//支付结果交易号
//
//payment_type	string
//订单类型(在线支付,货到付款)
//
//refund_fail_reason	string
//退款失败原因
//
//refund_gift	string
//赠品信息
//
//refund_point	integer($int32)
//退还积分
//
//refund_price	number($double)
//退款金额
//
//refund_reason	string
//退款原因
//
//refund_status	string
//退(货)款状态
//
//refund_status_text	string
//退货(款)单状态文字描述
//
//refund_type	string
//售后类型(取消订单,申请售后)
//
//refund_type_text	string
//售后类型文字描述:取消订单，申请售后
//
//refund_way	string
//退款方式(原路退回，线下支付)
//
//refuse_reason	string
//拒绝原因
//
//refuse_type_text	string
//退(货)款类型文字描述:退款，退货
//
//return_account	string
//退款账户
//
//seller_id	integer($int32)
//卖家id
//
//seller_name	string
//卖家姓名
//
//seller_remark	string
//客服备注
//
//sn	string
//退货(款)单编号
//
//trade_sn	string
//交易编号
//
//warehouse_remark	string
//库管备注



//[
//退货商品
//
//RefundGoodsDO{
//    goods_id	integer($int32)
//    商品id
//
//    goods_image	string
//            商品图片
//
//    goods_name	string
//            商品名称
//
//    goods_sn	string
//            商品编号
//
//    price	number($double)
//    商品价格
//
//    refund_sn	string
//            退货(款)编号
//
//            return_num	integer($int32)
//    退货数量
//
//    ship_num	integer($int32)
//    发货数量
//
//    sku_id	integer($int32)
//    产品id
//
//    spec_json	string
//            规格数据
//
//    storage_num	integer($int32)
//    入库数量
//
//}]