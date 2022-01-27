package com.lingmiao.shop.business.goods.api.bean

import com.lingmiao.shop.business.goods.api.request.DeliveryRequest
import com.lingmiao.shop.business.goods.config.GoodsConfig
import com.google.gson.annotations.SerializedName
import java.io.Serializable


/**
 * Author : liuqi
 * Date   : 2020/8/8
 * Desc   : 商品发布
 */
class GoodsVOWrapper : Serializable {

    //券包  买一个商品送几张票
    @SerializedName("num")
    var num: Int? = null

    //券包  关联的票的ID
    @SerializedName("coupon_id")
    var couponID: Int? = null

    @SerializedName("bar_code")
    var bar_code: String? = null //条形码


    @SerializedName("cost")
    var cost: String? = null //成本价格

    @SerializedName("mktprice")
    var mktprice: String? = null //市场价格

    @SerializedName("package_price")
    var packagePrice: Double? = 0.00 // 打包费

    @SerializedName("price")
    var price: String? = null //商品价格

    @SerializedName("quantity")
    var quantity: String? = null //库存

    @SerializedName("event_price")
    var eventPrice: String? = null //活动价格

    @SerializedName("event_quantity")
    var eventQuantity: String? = null //活动库存

    @SerializedName("sn")
    var sn: String? = null  //货号

    @SerializedName("spec_value_id")
    var specValueId: String? = null //规格值Id

    @SerializedName("up_sku_id")
    var upSkuId: String? = null  //外部sku编码

    @SerializedName("weight")
    var weight: String? = null //重量

    // ---------------------------------


    //商品分类
    @SerializedName("category_id")
    var categoryId: String? = null

    @SerializedName("category_name")
    var categoryName: String? = null // 分类名称

    /**
     * @see GoodsConfig.DELIVERY_PRICE_WAY_DEFAULT
     */
    @SerializedName("freight_pricing_way")
    var freightPricingWay: Int? = null  //快递运费定价方式: 0=统一价 1=模板

    @SerializedName("freight_unified_price")
    var freightUnifiedPrice: String? = null  //快递统一运费

    @SerializedName("local_freight_pricing_way")
    var localFreightPricingWay: Int? = null //同城运费定价方式: 0=统一价 1=模板

    @SerializedName("local_freight_unified_price")
    var localFreightUnifiedPrice: String? = null //同城统一运费

    @SerializedName("goods_gallery_list")
    var goodsGalleryList: List<GoodsGalleryVO>? = null  //商品主图 List

    @SerializedName("thumbnail")
    var thumbnail: String? = null

    @SerializedName("goods_id")
    var goodsId: String? = null

    @SerializedName("goods_name")
    var goodsName: String? = null

    @SerializedName("goods_transfee_charge")
    var goodsTransfeeCharge: String? = null

    @SerializedName("has_changed")
    var hasChanged: String? = null

    @SerializedName("have_spec")
    var hasSpec: Int? = 0  //是否有规格：0=没有；1=有

    @SerializedName("is_global")
    var isGlobal: Int = 0

    @SerializedName("is_local")
    var isLocal: Int = 0

    @SerializedName("is_self_take")
    var isSelfTake: Int = 0

    @SerializedName("local_template_id")
    var localTemplateId: String? = null

    @SerializedName("market_enable")
    var marketEnable: String? = null

    @SerializedName("selling")
    var selling: String? = null //商品卖点

    @SerializedName("shop_cat_id")
    var shopCatId: String? = null //菜单id

    @SerializedName("shop_cat_name")
    var shopCatName: String? = null //菜单

    @SerializedName("sku_list")
    var skuList: List<GoodsSkuVO>? = null //sku列表

    @SerializedName("supplier_name")
    var supplierName: String? = null

    @SerializedName("template_id")
    var templateId: String? = null

    @SerializedName("out_goods_id")
    var outGoodsId: String? = null //外部编码

    @SerializedName("up_goods_id")
    var upGoodsId: String? = null

    @SerializedName("video_url")
    var videoUrl: String? = null


    /**
     *  3未审核通过 ；0，4审核中；1，2审核通过
     */
    @SerializedName("is_auth")
    var isAuth: Int = 0

    @SerializedName("auth_message")
    var authMessage: String = ""

    // 配送时效
    var goodsDeliveryType: String? = ""

    // 配送方式
    var goodsDeliveryModel: String? = ""


    // 配送时效
    var goodsWeight: String? = ""

    // 配送方式
    var goodsUnit: String? = ""

    // ------------虚拟商品---------------------
    // VIRTUAL
    // NORMAL
    @SerializedName("goods_type")
    var goodsType: String? = ""

    // 虚拟商品的可用日期
    @SerializedName("available_date")
    var availableDate: String? = ""

    // 虚拟商品自购买日后几天可用
    @SerializedName("expiry_day")
    var expiryDay: String? = ""

    @SerializedName("expiry_day_text")
    var expiryDayText: String? = ""


    // ---------------------------------

    // ------ 业务字段 --------
    // 是否设置过规格
    var isAddSpec: Boolean = false
    var specKeyList: List<SpecKeyVO>? = null


    @SerializedName("goods_params_list")
    var goodsParamsList: List<GoodsParamVo>? = null

    /**
     * 详情
     */
    @SerializedName("meta_description")
    var metaDescription: String? = null

    /**
     * 图片
     */
    @SerializedName("intro")
    var intro: String? = null

    fun convert(deliveryVO: DeliveryRequest?) {
        deliveryVO?.let {
            isGlobal = it.isGlobal
            freightPricingWay = it.freightPricingWay
            freightUnifiedPrice = it.freightUnifiedPrice
            templateId = it.templateId

            isLocal = it.isLocal
            localFreightPricingWay = it.localFreightPricingWay
            localFreightUnifiedPrice = it.localFreightUnifiedPrice
            localTemplateId = it.localTemplateId

            isSelfTake = it.isSelfTake
        }
    }

    fun isSelectDeliveryWay(): Boolean {
        return isGlobal == 1 || isLocal == 1 || isSelfTake == 1
    }

    /**
     * 是否是多规格
     * @return true = 多规格
     */
    fun hasMultiSpec(): Boolean {
        return 1 == hasSpec
    }

    fun isVirtualGoods(): Int {
        return when (goodsType) {
            GoodsConfig.GOODS_TYPE_NORMAL -> {
                0
            }
            GoodsConfig.GOODS_TYPE_VIRTUAL -> {
                1
            }
            GoodsConfig.GOODS_TYPE_TICKET -> {
                2
            }
            else -> {
                0
            }
        }
    }

    @SerializedName("goods_coupon")
    var goodsCoupon: GoodsCoupon? = null
}
