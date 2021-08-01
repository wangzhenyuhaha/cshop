package com.lingmiao.shop.business.goods.api.bean

import com.lingmiao.shop.business.goods.pop.GoodsMenuPop
import com.google.gson.annotations.SerializedName
import java.io.Serializable


/**
 * Author : Elson
 * Date   : 2020/7/12
 * Desc   : 商品VO
 */

data class GoodsVO(
    @SerializedName("brand_id")
    var brandId: String?,
    @SerializedName("buy_count")
    var buyCount: String?,
    @SerializedName("create_time")
    var createTime: Long?,
    @SerializedName("quantity")
    var quantity: String? = null,
    @SerializedName("enable_quantity")
    var enableQuantity: Int = 0,
    @SerializedName("event_quantity")
    var eventQuantity: Int = 0,
    @SerializedName("goods_id")
    var goodsId: String?,
    @SerializedName("goods_name")
    var goodsName: String?,
    @SerializedName("goods_type")
    var goodsType: String?,
    @SerializedName("supplier_name")
    var supplierName: String?,
    /**
     * STATUS_MIX_0,STATUS_MIX_1,STATUS_MIX_2,STATUS_MIX_3
     */
    @SerializedName("goods_status_mix")
    var goodsStatusMix: Int = 0,
    /**
     * 0 未售馨
     * 1 已售馨
     */
    @SerializedName("goods_quantity_status_mix")
    var goodsQuantityStatusMix: Int = 0,
    /**
     *  3未审核通过 ；0，4审核中；1，2审核通过
     */
    @SerializedName("is_auth")
    var isAuth: Int = 0,
    @SerializedName("goods_status_text")
    var goodsStatusText: String = "",
    @SerializedName("auth_message")
    var authMessage: String = "",
    @SerializedName("market_enable")
    var marketEnable: Int = 0, //上架状态 1上架 0下架
    @SerializedName("price")
    var price: Double = 0.0,
    @SerializedName("event_price")
    var eventPrice: Double = 0.0,
    @SerializedName("priority")
    var priority: Int = 0,
    @SerializedName("seller_name")
    var sellerName: String?,
    @SerializedName("sn")
    var sn: String?,
    @SerializedName("thumbnail")
    var thumbnail: String?,
    @SerializedName("under_message")
    var underMessage: String?,
    /**
     * 轮播图
     */
    @SerializedName("goods_gallery_list")
    var goodsGalleryList: List<GoodsGalleryVO>? = listOf(),
    /**
     * 是否选中
     */
    var isChecked: Boolean? = false
) : Serializable {

    companion object {
        /**
         * 审核状态：0 待审核，1 不需要审核 2 需要审核且审核通过 3 需要审核且审核不通过 4 待编辑(中心库复制的)
         */
        const val AUTH_STATUS_WAITING = 0
        const val AUTH_STATUS_NO_CHECK = 1
        const val AUTH_STATUS_CHECK_AND_PASS = 2
        const val AUTH_STATUS_CHECK_AND_REJECT = 3
        const val AUTH_STATUS_EDITING = 4

        /**
         * 上架状态 1上架 0下架
         */
        const val MARKET_STATUS_DISABLE = 0
        const val MARKET_STATUS_ENABLE = 1

        /**
         * 0 待上架 1 已上架 2 已下架 3 待上架  4库存预警
         */
        const val STATUS_MIX_0 = 0
        const val STATUS_MIX_1 = 1
        const val STATUS_MIX_2 = 2
        const val STATUS_MIX_3 = 3


        fun getEnableAuth(): String {
            return String.format(
                "%s,%s",
                AUTH_STATUS_NO_CHECK,
                AUTH_STATUS_CHECK_AND_PASS
            );
        }

        fun getWaitAuth(): String {
            return String.format(
                "%s,%s,%s",
                AUTH_STATUS_WAITING,
                AUTH_STATUS_CHECK_AND_REJECT,
                AUTH_STATUS_EDITING
            );
        }

        fun getDisableAuth(): String {
            return String.format("%s,%s", AUTH_STATUS_NO_CHECK, AUTH_STATUS_CHECK_AND_PASS);
        }
    }

    fun isSellOut(): Boolean {
        return goodsQuantityStatusMix == 1;
    }

    fun getMenuType(): Int {
        return when (goodsStatusMix) {
            STATUS_MIX_0 -> (GoodsMenuPop.TYPE_QUANTITY)
            STATUS_MIX_1 -> (GoodsMenuPop.TYPE_EDIT or GoodsMenuPop.TYPE_DISABLE or GoodsMenuPop.TYPE_QUANTITY or GoodsMenuPop.TYPE_SHARE)
            STATUS_MIX_2 -> (GoodsMenuPop.TYPE_EDIT or GoodsMenuPop.TYPE_ENABLE or GoodsMenuPop.TYPE_DELETE)
            STATUS_MIX_3 -> (GoodsMenuPop.TYPE_ENABLE or (if (isAuth == 4) GoodsMenuPop.TYPE_EDIT else 0))
            else -> (GoodsMenuPop.TYPE_EDIT)
        }
    }
}