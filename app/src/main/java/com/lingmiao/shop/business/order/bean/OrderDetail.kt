package com.lingmiao.shop.business.order.bean
import android.view.View
import com.lingmiao.shop.util.GlideUtils
import com.google.gson.annotations.SerializedName
import kotlinx.android.synthetic.main.order_activity_order_detail.*
import java.text.SimpleDateFormat
import java.util.*

data class OrderDetail(
    @SerializedName("address_id")
    var addressId: Int?,//收货地址ID
    @SerializedName("admin_remark")
    var adminRemark: Int?,//管理员备注
    @SerializedName("bill_sn")
    var billSn: String?,//结算单号
    @SerializedName("bill_status")
    var billStatus: Int?,//结算状态
    @SerializedName("cancel_left_time")
    var cancelLeftTime: Long?,//自动取消剩余秒数，如果已经超时会为0
    @SerializedName("cancel_reason")
    var cancelReason: String?,//订单取消原因
    @SerializedName("cash_back")
    var cashBack: Double?,//返现金额
    @SerializedName("client_type")
    var clientType: String?,//订单来源
    @SerializedName("comment_status")
    var commentStatus: String?,//评论是否完成
    @SerializedName("complete_time")
    var completeTime: Long?,//完成时间
    @SerializedName("coupon_price")
    var couponPrice: Double?,//优惠券抵扣金额
    @SerializedName("create_time")
    var createTime: Long?,//订单创建时间
    @SerializedName("disabled")
    var disabled: Int?,//	i是否被删除
    @SerializedName("discount_price")
    var discountPrice: Double?,//优惠金额
    @SerializedName("full_minus")
    var fullMinus: Double?,//满减金额
//    @SerializedName("gift_coupon")
//    var giftCoupon: GiftCoupon?,
//    @SerializedName("gift_list")
//    var giftList: List<Gift>?,
    @SerializedName("gift_point")
    var giftPoint: Int?,//赠送的积分
    @SerializedName("goods_num")
    var goodsNum: Int?,//商品数量
    @SerializedName("goods_price")
    var goodsPrice: Double?,//商品总额
    @SerializedName("package_price")
    var package_price: Double?,//包装费
    @SerializedName("items_json")
    var itemsJson: String?,//货物列表json?
    @SerializedName("leader")
    var leader: Leader?,
    @SerializedName("leader_id")
    var leaderId: Int?,//团长
    @SerializedName("logi_id")
    var logiId: Int?,//物流公司ID
    @SerializedName("logi_name")
    var logiName: String?,//物流公司名称
    @SerializedName("member_id")
    var memberId: Int?,//会员ID
    @SerializedName("member_name")
    var memberName: String?,//买家账号
    @SerializedName("need_pay_money")
    var needPayMoney: Double?,//应付金额
    @SerializedName("need_receipt")
    var needReceipt: Int?,//是否需要发票,0：否，1：是
    @SerializedName("order_operate_allowable_vo")
    var orderOperateAllowableVo: OrderOperateAllowable?,
    @SerializedName("order_price")
    var orderPrice: Double?,//订单总额
    @SerializedName("order_sku_list")
    var orderSkuList: List<OrderSku>?,
    @SerializedName("order_status")
    var orderStatus: String?,//订单状态
    @SerializedName("order_status_text")
    var orderStatusText: String?,//订单状态文字
    @SerializedName("order_type")
    var orderType: String?,//订单类型
    @SerializedName("pay_money")
    var payMoney: Double?,//已支付金额
    @SerializedName("pay_order_no")
    var payOrderNo: String?,//支付方式返回的交易号
    @SerializedName("pay_status")
    var payStatus: String?,//付款状态
    @SerializedName("pay_status_text")
    var payStatusText: String?,//付款状态文字
    @SerializedName("payment_method_id")
    var paymentMethodId: String?,
    @SerializedName("payment_method_name")
    var paymentMethodName: String?,//支付方式名称
    @SerializedName("payment_name")
    var paymentName: String?,//支付方式
    @SerializedName("payment_plugin_id")
    var paymentPluginId: String?,
    @SerializedName("payment_time")
    var paymentTime: Long?,//支付时间
    @SerializedName("payment_type")
    var paymentType: String?,
    @SerializedName("pick_addr")
    var pickAddr: String?,
    @SerializedName("ping_tuan_status")
    var pingTuanStatus: String?,//拼团订单状态
//    @SerializedName("receipt_history")
//    var receiptHistory: ReceiptHistory?,
    @SerializedName("receive_time")
    var receiveTime: String?,//收货时间
    @SerializedName("remark")
    var remark: String?,//订单备注
    @SerializedName("rog_left_time")
    var rogLeftTime: Long?,//自动收货剩余时间，如果已经超时会为0
    @SerializedName("seller_id")
    var sellerId: Int?,
    @SerializedName("seller_logo")
    var sellerLogo: String?,//店铺logo
    @SerializedName("seller_name")
    var sellerName: String?,
    @SerializedName("service_status")
    var serviceStatus: String?,//售后状态
    @SerializedName("service_status_text")
    var serviceStatusText: String?,//售后状态文字
    @SerializedName("ship_addr")
    var shipAddr: String?,//收货地址
    @SerializedName("ship_city")
    var shipCity: String?,//配送地区-城市
    @SerializedName("ship_city_id")
    var shipCityId: Int?,
    @SerializedName("ship_county")
    var shipCounty: String?,
    @SerializedName("ship_county_id")
    var shipCountyId: Int?,
    @SerializedName("ship_mobile")
    var shipMobile: String?,//收货人手机
    @SerializedName("ship_name")
    var shipName: String?,//收货人姓名
    @SerializedName("ship_no")
    var shipNo: String?,//发货单号
    @SerializedName("ship_province")
    var shipProvince: String?,//配送地区-省份
    @SerializedName("ship_province_id")
    var shipProvinceId: Int?,
    @SerializedName("ship_status")
    var shipStatus: String?,//货运状态
    @SerializedName("ship_status_text")
    var shipStatusText: String?,//发货状态文字
    @SerializedName("ship_tel")
    var shipTel: String?,//收货人电话
    @SerializedName("ship_time")
    var shipTime: Long?,//送货时间
    @SerializedName("ship_town")
    var shipTown: String?,//配送街道
    @SerializedName("ship_town_id")
    var shipTownId: Int?,
    @SerializedName("ship_zip")
    var shipZip: String?,//收货人邮编
    @SerializedName("shipping_id")
    var shippingId: Int?,//配送方式ID
    @SerializedName("shipping_price")
    var shippingPrice: Double?,//配送费用
    @SerializedName("shipping_type")
    var shippingType: String?,//配送方式
    @SerializedName("signing_time")
    var signingTime: Long?,//签收时间
    @SerializedName("sn")
    var sn: String?,//订单编号
    @SerializedName("the_sign")
    var theSign: String?,//签收人
    @SerializedName("trade_sn")
    var tradeSn: String?,//交易编号
    @SerializedName("use_point")
    var usePoint: Int?,
    @SerializedName("warehouse_id")
    var warehouseId: Int?,
    @SerializedName("is_virtual_order")
    var isVirtualOrder: Int,
    @SerializedName("virtual_status")
    var virtualStatus: Int,
    @SerializedName("verification_code")
    var verificationCode: String?,
    @SerializedName("available_date")
    var availableDate: String?,
    @SerializedName("expiry_day")
    var expiryDay: String?,
    @SerializedName("expiry_day_timestamp")
    var expiryDayTimestamp: Long? = 0,
    @SerializedName("weight")
    var weight: Double?//订单商品总重量
) {

    fun convertLongToTime (time: Long): String {
        if(time == null) {
            return "";
        } else {
            val date = Date(time)
            val format = SimpleDateFormat("yyyy-M-dd")
            return format.format(date)
        }
    }

    fun getExpireTime() : String {
        if(expiryDayTimestamp == null) {
            return "";
        }
        return convertLongToTime(expiryDayTimestamp!!);
    }


    fun isVirtualOrderTag() : Boolean {
        return isVirtualOrder == 1;
    }
}

data class Leader(
    @SerializedName("address")
    var address: String?,//地址
    @SerializedName("cell_name")
    var cellName: String?,//小区名称
    @SerializedName("doorplate")
    var doorplate: String?,
    @SerializedName("facade_pic_url")
    var facadePicUrl: String?,//门面图片
    @SerializedName("leader_mobile")
    var leaderMobile: String?,//团长手机
    @SerializedName("leader_name")
    var leaderName: String?,
    @SerializedName("site_name")
    var siteName: String?,//站点名称
    @SerializedName("street")
    var street: String?
)


data class OrderSku(
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
//    var groupList: List<Group>?,
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
    var purchaseNum: Int?,
    @SerializedName("purchase_price")
    var purchasePrice: Double?,
    @SerializedName("seller_id")
    var sellerId: Int?,
    @SerializedName("seller_name")
    var sellerName: String?,
    @SerializedName("service_status")
    var serviceStatus: String?,
    @SerializedName("shetuan_goods_id")
    var shetuanGoodsId: Int?,
//    @SerializedName("single_list")
//    var singleList: List<Single>?,
    @SerializedName("sku_id")
    var skuId: Int?,
    @SerializedName("sku_sn")
    var skuSn: String?,
    @SerializedName("snapshot_id")
    var snapshotId: Int?,
    @SerializedName("spec_list")
    var specList: List<Spec>?,
    @SerializedName("subtotal")
    var subtotal: Double?
)

data class Spec(
    @SerializedName("spec_id")
    var specId: Int?,
    @SerializedName("spec_image")
    var specImage: String?,
    @SerializedName("spec_name")
    var specName: String?,
    @SerializedName("spec_type")
    var specType: Int?,
    @SerializedName("spec_value")
    var specValue: String?,
    @SerializedName("spec_value_id")
    var specValueId: Int?
)