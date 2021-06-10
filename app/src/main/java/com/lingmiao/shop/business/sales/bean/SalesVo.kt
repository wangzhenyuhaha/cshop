package com.lingmiao.shop.business.sales.bean
import com.google.gson.annotations.SerializedName
import com.lingmiao.shop.business.goods.api.bean.GoodsVO
import java.io.Serializable


/**
Create Date : 2021/3/122:50 AM
Auther      : Fox
Desc        :
 **/
class SalesVo : Serializable {

    @SerializedName("fd_id")
    var id : String? = "";

    /**
     *
        WAIT ("等待"),

        UNDERWAY("进行中"),

        END("结束");
     */
    @SerializedName("status")
    var status : String? = ""

    @SerializedName("status_text")
    var statusText : String? = ""

    @SerializedName("title")
    var title: String? = ""
    /**
     * 描述
     */
    @SerializedName("description")
    var description: String? = ""


    /**
     * 是否打折 0为否1为是；因为现在只用满减，所以这个可以默认是0
     */
    @SerializedName("is_discount")
    var isDiscount: Int? = 0
    /**
     * 打多少折 这个可以不填
     */
    @SerializedName("discount_value")
    var discountValue: String? = ""
    /**
     *  活动是否减现金 0为否1为是 因为现在只用满减，所以这个可以默认是1
     */
    @SerializedName("is_full_minus")
    var isFullMinus: Int? = 1
    /**
     * 满减类型
     */
    @SerializedName("full_type")
    var fullType: String? = ""
    /**
     * 满多少钱
     */
    @SerializedName("full_money")
    var fullMoney: String? = ""

    /**
     * 满减减多少钱
     */
    @SerializedName("minus_value")
    var minusValue: String? = ""
    /**
     * 是否有赠品 0为否1为是
     */
    @SerializedName("is_send_gift")
    var isSendGift: Int? = 0
    /**
     * 赠品 id
     */
    @SerializedName("gift_id")
    var giftId: String? = ""

    /**
     * 是否邮费 0为否1为是
     */
    @SerializedName("is_free_ship")
    var isFreeShip: Int? = 0

    /**
     * 是否增优惠券 0为否1为是
     */
    @SerializedName("is_send_bonus")
    var isSendBonus: Int? = 0
    /**
     * 优惠券id
     */
    @SerializedName("bonus_id")
    var bonusId: String? = ""
    /**
     * 是否赠送积分 0为否1为是
     */
    @SerializedName("is_send_point")
    var isSendPoint: Int? = 0
    /**
     * 赠送多少积分
     */
    @SerializedName("point_value")
    var pointValue: Double? = 0.0
    /**
     * 活动开始时间
     */
    @SerializedName("start_time")
    var startTime: Long? = 0

    /**
     * 活动结束时间
     */
    @SerializedName("end_time")
    var endTime: Long? = 0

    @SerializedName("goods_list")
    var goodsList : List<GoodsVO>? = null

    /**
     * 是否全部商品参加活动，1表示全部商品，2表示部分商品
     */
    @SerializedName("range_type")
    var rangeType: Int? = 0

    fun convertDiscountItem() : SalesActivityItemVo {
        var it = SalesActivityItemVo();
        it.peach = fullMoney;
        it.least = minusValue;
        return it;
    }
}