package com.lingmiao.shop.business.main.bean

import com.blankj.utilcode.util.StringUtils
import com.google.gson.annotations.SerializedName
import java.io.Serializable

//-----以上非注明皆为必填
data class ApplyShopInfo(

    //店铺类型:1,企业     3,个体户
    @SerializedName("shop_type")
    var shopType: Int? = null,

    //推广码(非必填，申请店铺时如果邀请码填错，无法申请店铺)
    @SerializedName("promo_code")
    var promoCode: String? = null,

    //营业执照电子版
    @SerializedName("licence_img")
    var licenceImg: String? = null,

    //店铺门头照片
    @SerializedName("storepic")
    var shopPhotoFront: String? = null,

    //店铺内景照片
    @SerializedName("storeinnerpic")
    var shopPhotoInside: String? = null,

    //  国徽
    @SerializedName("legal_img")
    var legalImg: String? = null,

    //  人像
    @SerializedName("legal_back_img")
    var legalBackImg: String? = null,

    //法人手持身份证照片
    @SerializedName("hold_img")
    var holdImg: String? = null,

    //店铺名称
    @SerializedName("shop_name")
    var shopName: String? = null,

    //店铺经营类目    ids
    @SerializedName("goods_management_category")
    var goodsManagementCategory: String? = null,

    //店铺经营类目 返回文本
    @SerializedName("category_names")
    var categoryNames: String? = null,


    //店铺所在省
    @SerializedName("shop_province")
    var shopProvince: String? = null,

    //店铺所在市
    @SerializedName("shop_city")
    var shopCity: String? = null,

    //店铺所在县
    @SerializedName("shop_county")
    var shopCounty: String? = null,

    //店铺所在镇
    @SerializedName("shop_town")
    var shopTown: String? = null,

    //店铺详细地址
    @SerializedName("shop_add")
    var shopAdd: String? = null,

    //店铺纬度
    @SerializedName("shop_lat")
    var shopLat: Double? = null,

    //店铺经度
    @SerializedName("shop_lng")
    var shopLng: Double? = null,


    //店铺联系人姓名(负责人)
    @SerializedName("link_name")
    var linkName: String? = null,

    //店铺联系人电话(负责人电话)
    @SerializedName("link_phone")
    var linkPhone: String? = null,

    //经营内容(法定经营范围)
    @SerializedName("scope")
    var scope: String? = null,


    //身份证信息
    //法人姓名
    @SerializedName("legal_name")
    var legalName: String? = null,

    //法人性别(法人性别,1为男，0为女)
    @SerializedName("legal_sex")
    var legalSex: Int? = null,

    //法人身份证号码
    @SerializedName("legal_id")
    var legalId: String? = null,

    //证件有效期(法人身份证过期时间)  Data
    @SerializedName("legalidexpire")
    var legalIDExpire: Long? = null,

    //如果是公司账户,填写企业资料
    //如果是个体户账户,填写商家资料


    //注册资金  注册资本  1: 注册资本<10万元 2: 10万元<注册资本<20万元 3: 20万元<注册资本<50万元 4: 50万元<注册资本<100万元 5: 注册资本>100万元
    @SerializedName("reg_money")
    var regMoney: Int? = null,

    //员工人数  商户性质为企业、个体户时必填 1: 员工数量<10 2: 10<员工数量<20 3: 20<员工数量<50 4: 50<员工数量<100 5:员工数量>100"
    @SerializedName("employee_num")
    var employeeNum: Int? = null,

    //经营区域  1：城区 2: 郊区 3：边远地区
    @SerializedName("operatelimit")
    var operateLimit: Int? = null,

    //经营地段  1：商业区 2：工业区 3：住宅区
    @SerializedName("inspect")
    var inspect: Int? = null,

    //三证合一  是否三证合一 1：是 0：否
    @SerializedName("thrcertflag")
    var thrcertflag: Int? = null,

    //三证合一   是
    //统一社会信用代码证
    @SerializedName("creditcode")
    var creditCode: String? = null,

    //社会信用代码证有效期
    @SerializedName("creditcodeexpire")
    var creditCodeExpire: Long? = null,


    //三证合一   否

    //税务登记号码
    @SerializedName("taxregcode")
    var taxRegCode: String? = null,

    //税务登记证日期
    @SerializedName("taxcodeexpire")
    var taxCodeExpire: Long? = null,

    //税务登记证照片
    @SerializedName("taxcodepic")
    var taxCodePic: String? = null,

    //营业执照号
    @SerializedName("license_num")
    var licenseNum: String? = null,

    //营业执照有效期结束
    @SerializedName("licence_end")
    var licenseEnd: Long? = null,

    //结算账户绑定
    //账户类型 0-对私 1-对公
    @SerializedName("accttype")
    var accttype: Int? = null,

    //账户名称或持卡人
    @SerializedName("bank_account_name")
    var bankAccountName: String? = null,

    //开户许可证照片或银行卡照片
    @SerializedName("bank_urls")
    var bankUrls: String? = null,

    //账户号或卡号
    @SerializedName("bank_number")
    var bankNumber: String? = null,

    //银行卡所在省
    @SerializedName("bank_province")
    var province: String? = null,

    //银行卡所在市
    @SerializedName("bank_city")
    var city: String? = null,

    //所属银行,开户银行号（通联）,名称
    @SerializedName("bank_no")
    var bankNo: String? = null,

    //所属支行,开户银行支行名称，名称
    @SerializedName("bank_name")
    var bankName: String? = null,

    //所属银行,开户银行号（通联）,号码
    @SerializedName("bankCode")
    var bankCode: String? = null,

    //所属支行,开户银行支行名称，号码
    @SerializedName("subBankCode")
    var subBankCode: String? = null,


    //店铺绑定的银行卡
    @SerializedName("bank_card")
    var bankCard: BindBankCardDTO? = null,


    //----------------------


//店铺logo
    @SerializedName("shop_logo")
    var shopLogo: String? = null,

    @SerializedName("shop_slogan")
    var shopSlogan: String? = null,

    @SerializedName("shop_notice")
    var shopNotice: String? = null,


    @SerializedName("shop_desc")
    var shopDesc: String? = null,

    @SerializedName("shop_id")
    var shopId: Int? = null,

// 未支付自动取消订单分钟数
    @SerializedName("unpaid_cancel_orders_time")
    var unpaidCancelOrderTime: Int? = null,

// 已接单自动取消订单分钟数
    @SerializedName("cancel_order_time")
    var cancelOrderTime: Int? = null,

// 自动接单
    @SerializedName("auto_accept")
    var autoAccept: Int? = null,

// 客服电话
    @SerializedName("company_phone")
    var companyPhone: String? = null,

// 营业时间
    @SerializedName("open_end_time")
    var openEndTime: String? = null,

    @SerializedName("open_start_time")
    var openStartTime: String? = null,

    @SerializedName("open_time_type")
    var openTimeType: Int? = null,

//    @SerializedName("ship_type")
//    var shipType: String?,
//    @SerializedName("shop_banner")
//    var shopBanner: String?,
//店铺客服qq
    @SerializedName("shop_qq")
    var shopQq: String? = null,

//店铺类型
//    @SerializedName("shop_tag")
//    var shopTag: String?=null,


    @SerializedName("shop_setting")
    var orderSetting: OrderSetting? = null,

    @SerializedName("shop_template_type")
    var shopTemplateType: String? = ""

) : Serializable {

    fun getShopTypeStr(): String {
        return if (StringUtils.equals("2", shopType.toString())) "单店" else "连锁店"
    }

    fun getFullAddress(): String {
        return String.format(
            "%s%s%s%s",
            if (isExistProvince()) "" else shopProvince,
            if (isExistCity()) "" else shopCity,
            if (isExistCounty()) "" else shopCounty,
            shopAdd
        )
    }

    fun isExistProvince(): Boolean {
        return shopProvince?.let { shopAdd?.indexOf(it) } ?: -1 > 0
    }

    fun isExistCity(): Boolean {
        return shopCity?.let { shopAdd?.indexOf(it) } ?: -1 > -1
    }

    fun isExistCounty(): Boolean {
        return shopCounty?.let { shopAdd?.indexOf(it) } ?: -1 > -1
    }

}


data class OrderSetting(
    @SerializedName("auto_accept")
    var autoAccept: Int? = null,
    /**
     * 已接单取消订单数
     */
    @SerializedName("cancel_order_day")
    var cancelOrderDay: Int? = 0,
    /**
     * 评价超时天数
     */
    @SerializedName("comment_order_day")
    var commentOrderDay: Int? = 0,
    /**
     * 订单完成天数
     */
    @SerializedName("complete_order_day")
    var completeOrderDay: Int? = 0,
    /**
     * 自动支付天数（对货到付款的订单有效)
     */
    @SerializedName("complete_order_pay")
    var completeOrderPay: Int? = 0,
    /**
     * 自动确认收货天数
     */
    @SerializedName("rog_order_day")
    var rogOrderDay: Int? = 0,
    /**
     * 售后失效天数
     */
    @SerializedName("service_expired_day")
    var serviceExpiredDay: Int? = 0,
    /**
     * 未支付自动取消订单分钟数
     */
    @SerializedName("unpaid_cancel_orders_min")
    var unpaidCancelOrdersMin: Int? = 0
) : Serializable