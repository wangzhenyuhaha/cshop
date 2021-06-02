package com.lingmiao.shop.business.goods.api.bean

import com.google.gson.annotations.SerializedName
import java.io.Serializable


/**
 * Author : Elson
 * Date   : 2020/8/8
 * Desc   : SKU 信息
 */
open class GoodsSkuVO : Serializable {

    @SerializedName("cost")
    var cost: String? = null //成本价格

    @SerializedName("mktprice")
    var mktprice: String? = null //市场价格

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

    @SerializedName("category_id")
    var categoryId: String? = null

    @SerializedName("disabled")
    var disabled: String? = null

    @SerializedName("enable_quantity")
    var enableQuantity: String? = null //可用库存

    @SerializedName("freight_pricing_way")
    var freightPricingWay: String? = null //运费定价方式: 0=统一价 1=模板

    @SerializedName("freight_unified_price")
    var freightUnifiedPrice: String? = null //统一运费

    @SerializedName("goods_id")
    var goodsId: String? = null

    @SerializedName("goods_name")
    var goodsName: String? = null

    @SerializedName("goods_transfee_charge")
    var goodsTransfeeCharge: String? = null

    @SerializedName("goods_type")
    var goodsType: String? = null  //商品类型NORMAL普通POINT积分

    @SerializedName("hash_code")
    var hashCode: String? = null

    @SerializedName("is_global")
    var isGlobal: Int? = 0  //支持快递

    @SerializedName("is_local")
    var isLocal: Int? = 0  //支持同城

    @SerializedName("is_self")
    var isSelf: Int? = 0  //支持自提

    @SerializedName("last_modify")
    var lastModify: Long? = null

    @SerializedName("local_template_id")
    var localTemplateId: String? = null//同城配送运费模板Id

    @SerializedName("market_enable")
    var marketEnable: String? = null

    @SerializedName("pick_time")
    var pickTime: String? = null //提货时间

    @SerializedName("seller_id")
    var sellerId: String? = null

    @SerializedName("seller_name")
    var sellerName: String? = null

    @SerializedName("sku_id")
    var skuId: String? = null

    @SerializedName("spec_list")
    var specList: List<SpecValueVO>? = null

    @SerializedName("template_id")
    var templateId: String? = null //运费模板id,不需要运费模板时值是0

    @SerializedName("thumbnail")
    var thumbnail: String? = null

    @SerializedName("video_url")
    var videoUrl: String? = null  //主图视频


}