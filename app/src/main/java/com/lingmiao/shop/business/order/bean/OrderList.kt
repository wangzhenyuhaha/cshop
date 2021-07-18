package com.lingmiao.shop.business.order.bean

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class OrderList(
    @SerializedName("cancel_left_time")
    var cancelLeftTime: Long?,
    @SerializedName("cancel_reason")
    var cancelReason: String?,
    @SerializedName("client_type")
    var clientType: String?,
    @SerializedName("comment_status")
    var commentStatus: String?,
    @SerializedName("complete_time")
    var completeTime: Long?,
//    @SerializedName("coupon_list")
//    var couponList: Any?,
    @SerializedName("create_time")
    var createTime: Long?,

    @SerializedName("discount_price")
    var discountPrice: Double?,
//    @SerializedName("gift_list")
//    var giftList: Any?,
//    @SerializedName("item_list")
//    var itemList: Any?,
    @SerializedName("logi_id")
    var logiId: Int?,
    @SerializedName("logi_name")
    var logiName: String?,
    @SerializedName("member_id")
    var memberId: Int?,
    @SerializedName("member_name")
    var memberName: String?,
    @SerializedName("order_amount")
    var orderAmount: Double?,
    @SerializedName("order_id")
    var orderId: Int?,
    @SerializedName("order_status")
    var orderStatus: String?,
    @SerializedName("order_status_text")
    var orderStatusText: String?,
    @SerializedName("order_type")
    var orderType: String?,
    @SerializedName("pay_money")
    var payMoney: Double?,
    @SerializedName("pay_status")
    var payStatus: String?,
    @SerializedName("pay_status_text")
    var payStatusText: String?,
    @SerializedName("payment_method_name")
    var paymentMethodName: String?,
    @SerializedName("payment_name")
    var paymentName: String?,
    @SerializedName("payment_time")
    var paymentTime: Long?,
    @SerializedName("payment_type")
    var paymentType: String?,
    @SerializedName("ping_tuan_status")
    var pingTuanStatus: String?,//拼团状态
//    @SerializedName("promotion_tags")
//    var promotionTags: Any?,
    @SerializedName("remark")
    var remark: String?,
    @SerializedName("seller_id")
    var sellerId: Int?,
    @SerializedName("seller_logo")
    var sellerLogo: String?,
    @SerializedName("seller_name")
    var sellerName: String?,
    /**
     * /** 未申请 */
    NOT_APPLY("未申请"),

    /** 已申请 */
    APPLY("已申请"),

    /** 审核通过 */
    PASS("审核通过"),

    /** 审核未通过 */
    REFUSE("审核未通过"),

    /** 已失效或不允许申请售后 */
    EXPIRED("已失效不允许申请售后");
     */
    @SerializedName("service_status")
    var serviceStatus: String?,
    @SerializedName("ship_addr")
    var shipAddr: String?,
    @SerializedName("ship_city")
    var shipCity: String?,
    @SerializedName("ship_county")
    var shipCounty: String?,
    @SerializedName("ship_mobile")
    var shipMobile: String?,
    @SerializedName("rider_mobile")
    var riderMobile: String?,
    @SerializedName("seller_phone")
    var sellerMobile: String?,
    @SerializedName("ship_name")
    var shipName: String?,
    @SerializedName("ship_no")
    var shipNo: String?,
    @SerializedName("ship_province")
    var shipProvince: String?,
    @SerializedName("ship_status")
    var shipStatus: String?,
    @SerializedName("ship_status_text")
    var shipStatusText: String?,
    @SerializedName("ship_time")
    var shipTime: Long?,
    @SerializedName("ship_town")
    var shipTown: String?,
    @SerializedName("shipping_amount")
    var shippingAmount: Double?,
    @SerializedName("shipping_type")
    var shippingType: String?,
    @SerializedName("signing_time")
    var signingTime: Long?,
    @SerializedName("sku_list")
    var skuList: List<Sku>,
    @SerializedName("sn")
    var sn: String?,
    @SerializedName("order_sn")
    var orderSn: String?,
    @SerializedName("total_num")
    var totalNum: Int,
    @SerializedName("is_virtual_order")
    var isVirtualOrder: Int,
    @SerializedName("virtual_status")
    var virtualStatus: Int,
    @SerializedName("verification_code")
    var verificationCode: String?,
    @SerializedName("trade_sn")
    var tradeSn: String?,
    @SerializedName("waiting_group_nums")
    var waitingGroupNums: Int?,
    @SerializedName("order_operate_allowable_vo")
    var orderOperateAllowable:OrderOperateAllowable?,
    // 补充费用
    @SerializedName("replenish_price")
    var replenishPrice: Double? = 0.0,
    // 补充说明
    @SerializedName("replenish_remark")
    var replenishRemark : String? = "",

    @SerializedName("tableware_type")
    var tablewareType : Int?

//    @SerializedName("available_date")
//    var availableDate: String?,
//    @SerializedName("order_sku_list")
//    var orderSkuList: List<OrderSku>?,
//    @SerializedName("rog_left_time")
//    var rogLeftTime: Long?,//自动收货剩余时间，如果已经超时会为0
//
//    @SerializedName("order_operate_allowable_vo")
//    var orderOperateAllowableVo: OrderOperateAllowable?,
) : Serializable {
    fun isVirtualOrderTag() : Boolean {
        return isVirtualOrder == 1;
    }

    fun getSimpleAddress() : String {
        return shipTown.orEmpty() + shipAddr.orEmpty();
    }

    fun getFullAddress() : String {
        return shipProvince.orEmpty() + shipCity.orEmpty() +
                shipCounty.orEmpty() +
                shipTown.orEmpty() + shipAddr.orEmpty();
    }


    fun getTableAwareHint() : String {
        when(tablewareType) {
            0 -> {
                return "无需餐具";
            }
            100 -> {
                return "依据餐量提供餐具";
            }
            1 -> {
                return "1份";
            }
            2 -> {
                return "2份";
            }
            3 -> {
                return "3份";
            }
            else -> {
                return "";
            }
        }
    }
}


data class Sku(
    @SerializedName("actual_pay_total")
    var actualPayTotal: Double?,
    @SerializedName("cat_id")
    var catId: Int?,
    @SerializedName("goods_id")
    var goodsId: Int?,
    @SerializedName("goods_image")
    var goodsImage: String?,
//    @SerializedName("goods_operate_allowable_vo")
//    var goodsOperateAllowableVo: GoodsOperateAllowableVo?,
    @SerializedName("goods_weight")
    var goodsWeight: Double?,
//    @SerializedName("group_list")
//    var groupList: Any?,
    @SerializedName("name")
    var name: String?,
    @SerializedName("num")
    var num: Int?,
    @SerializedName("original_price")
    var originalPrice: Double?,
    @SerializedName("point")
    var point: Int?,
    @SerializedName("promotion_tags")
    var promotionTags: List<String>?,
    @SerializedName("purchase_num")
    var purchaseNum: String?,
    @SerializedName("purchase_price")
    var purchasePrice: Double?,
    @SerializedName("seller_id")
    var sellerId: Int?,
    @SerializedName("seller_name")
    var sellerName: String?,
    @SerializedName("service_status")
    var serviceStatus: String?,
//    @SerializedName("shetuan_goods_id")
//    var shetuanGoodsId: Any?,
//    @SerializedName("single_list")
//    var singleList: Any?,
    @SerializedName("sku_id")
    var skuId: Int?,
    @SerializedName("sku_sn")
    var skuSn: String?,
    @SerializedName("snapshot_id")
    var snapshotId: Int?,
    @SerializedName("spec_list")
    var specList: List<SpecBean>?,
    @SerializedName("subtotal")
    var subtotal: Double?
) : Serializable {

    fun getSpecValues() : String? {
        return specList?.map { it?.specValue }?.joinToString(separator = "/");
    }
}

data class GoodsOperateAllowableVo(
    @SerializedName("allow_apply_service")
    var allowApplyService: Boolean?
) : Serializable

data class SpecBean(
//    @SerializedName("big")
//    var big: Any?,
    @SerializedName("seller_id")
    var sellerId: String?,
    @SerializedName("sku_id")
    var skuId: String?,
//    @SerializedName("small")
//    var small: String?,
    @SerializedName("spec_id")
    var specId: Int?,
    @SerializedName("spec_image")
    var specImage: String?,
    @SerializedName("spec_name")
    var specName: String?,//规格名称 比如颜色
    @SerializedName("spec_type")
    var specType: Int?,
    @SerializedName("spec_value")
    var specValue: String?,//规格值
    @SerializedName("spec_value_id")
    var specValueId: Int?
//    @SerializedName("thumbnail")
//    var thumbnail: Any?,
//    @SerializedName("tiny")
//    var tiny: Any?
) : Serializable {

}

data class OrderOperateAllowable(
    @SerializedName("allow_apply_service")
    var allowApplyService: Boolean = false,//是否允许申请售后
    @SerializedName("allow_audit_after_sell")
    var allowAuditAfterSell: Boolean = false,//是否允许处理售后
    @SerializedName("allow_cancel")
    var allowCancel: Boolean = false,//是否允许被取消
    @SerializedName("allow_delete")
    var allowDelete: Boolean = false,//是否允许被删除
    @SerializedName("allow_check_express")
    var allowCheckExpress: Boolean = false,//是否允许查看物流信息
    @SerializedName("allow_comment")
    var allowComment: Boolean = false,//是否允许被评论
    @SerializedName("allow_complete")
    var allowComplete: Boolean = false,//是否允许被完成
    @SerializedName("allow_confirm")
    var allowConfirm: Boolean = false,//是否允许被确认
    @SerializedName("allow_edit_consignee")
    var allowEditConsignee: Boolean = false,//是否允许更改收货人信息
    @SerializedName("allow_edit_price")
    var allowEditPrice: Boolean = false,//是否允许更改价格
    @SerializedName("allow_pay")
    var allowPay: Boolean = false,//是否允许被支付
    @SerializedName("allow_rog")
    var allowRog: Boolean = false,//是否允许被收货
    @SerializedName("allow_service_cancel")
    var allowServiceCancel: Boolean = false,//是否允许取消(售后)
    @SerializedName("allow_ship")
    var allowShip: Boolean = false//是否允许被发货
)  : Serializable