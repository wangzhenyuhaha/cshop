package com.lingmiao.shop.business.goods.config

import com.lingmiao.shop.business.goods.api.bean.GoodsVO

/**
 * Author : Elson
 * Date   : 2020/8/15
 * Desc   : 常量类管理
 */
class GoodsConfig {

    companion object {
        /**
         * 运费定价方式：0=统一价 1=模板
         */
        const val DELIVERY_PRICE_WAY_COST = 0
        const val DELIVERY_PRICE_WAY_TEMP = 1

        const val GOODS_TYPE_VIRTUAL = "VIRTUAL"
        const val GOODS_TYPE_NORMAL = "NORMAL"

    }
}
