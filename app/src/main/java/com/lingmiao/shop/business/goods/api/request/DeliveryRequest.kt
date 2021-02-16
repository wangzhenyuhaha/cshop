package com.lingmiao.shop.business.goods.api.request

import com.lingmiao.shop.business.goods.api.bean.GoodsVOWrapper
import com.lingmiao.shop.business.goods.config.GoodsConfig
import java.io.Serializable

/**
 * Author : Elson
 * Date   : 2020/8/15
 * Desc   : 发货方式
 */
class DeliveryRequest : Serializable {

    var isGlobal: Int = 0  //支持快递
    // 状态值：GoodsConfig
    var freightPricingWay: Int? = null //运费定价方式: 0=统一价 1=模板
    var freightUnifiedPrice: String? = null //统一运费
    var templateId: String? = "0" //运费模板id,不需要运费模板时值是0
    var templateName: String? = null //运费模板名称

    var isLocal: Int = 0  //支持同城
    // 状态值：GoodsConfig
    var localFreightPricingWay: Int? = null //运费定价方式: 0=统一价 1=模板
    var localFreightUnifiedPrice: String? = null //统一运费
    var localTemplateId: String? = "0"  //同城配送运费模板Id
    var localTemplateName: String? = null //运费模板名称

    var isSelfTake: Int = 0  //支持自提

    fun isSelectDeliveryWay(): Boolean {
        return isGlobal == 1 || isLocal == 1 || isSelfTake == 1
    }

    fun isCheckGlobal(): Boolean {
        return isGlobal == 1
    }

    fun isCheckGlobalOptions(): Boolean {
        return isCheckGlobalTempPriceWay() || isCheckGlobalCostPriceWay()
    }

    fun isCheckGlobalCostPriceWay(): Boolean {
        return GoodsConfig.DELIVERY_PRICE_WAY_COST == freightPricingWay
    }

    fun isCheckGlobalTempPriceWay(): Boolean {
        return GoodsConfig.DELIVERY_PRICE_WAY_TEMP == freightPricingWay
    }

    fun isCheckLocal(): Boolean {
        return isLocal == 1
    }

    fun isCheckLocalOptions(): Boolean {
        return isCheckLocalTempPriceWay() || isCheckLocalCostPriceWay()
    }

    fun isCheckLocalCostPriceWay(): Boolean {
        return GoodsConfig.DELIVERY_PRICE_WAY_COST == localFreightPricingWay
    }

    fun isCheckLocalTempPriceWay(): Boolean {
        return GoodsConfig.DELIVERY_PRICE_WAY_TEMP == localFreightPricingWay
    }

    fun isCheckSelfTake(): Boolean {
        return isSelfTake == 1
    }

    fun getCheckedStatus(isCheck: Boolean): Int {
        return if (isCheck) 1 else 0
    }

    fun reset() {
        freightPricingWay = null
        freightUnifiedPrice = null
        templateId = "0"
        templateName = null

        localFreightPricingWay = null
        localFreightUnifiedPrice = null
        localTemplateId = "0"
        localTemplateName = null
    }

    companion object {
        fun convert(goodsVO: GoodsVOWrapper?): DeliveryRequest {
            return if (goodsVO == null) {
                DeliveryRequest()
            } else {
                DeliveryRequest().apply {
                    this.isGlobal = goodsVO.isGlobal
                    this.templateId = goodsVO.templateId
                    this.freightPricingWay = goodsVO.freightPricingWay
                    this.freightUnifiedPrice = goodsVO.freightUnifiedPrice

                    this.isLocal = goodsVO.isLocal
                    this.localTemplateId = goodsVO.localTemplateId
                    this.localFreightPricingWay = goodsVO.localFreightPricingWay
                    this.localFreightUnifiedPrice = goodsVO.localFreightUnifiedPrice

                    this.isSelfTake = goodsVO.isSelfTake
                }
            }
        }
    }
}