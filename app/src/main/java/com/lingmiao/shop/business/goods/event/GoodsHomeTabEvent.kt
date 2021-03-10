package com.lingmiao.shop.business.goods.event

import com.lingmiao.shop.business.goods.fragment.GoodsFragment
import com.lingmiao.shop.business.goods.fragment.GoodsNewFragment

/**
 * Author : Elson
 * Date   : 2020/8/26
 * Desc   : 商品管理切换Tab事件
 */
class GoodsHomeTabEvent(val status: Int = GoodsFragment.GOODS_STATUS_ENABLE) {

    /**
     * 出售中
     */
    fun getTabIndex() : Int {
        return when (status) {
            GoodsFragment.GOODS_STATUS_ENABLE -> 0
            GoodsFragment.GOODS_STATUS_IS_AUTH -> 1
            GoodsFragment.GOODS_STATUS_DISABLE -> 2
            else -> 0
        }
    }


    /**
     * 出售中
     */
    fun getNewTabIndex() : Int {
        return when (status) {
            GoodsNewFragment.GOODS_STATUS_ALL -> 0
            GoodsNewFragment.GOODS_STATUS_ENABLE -> 1
            GoodsNewFragment.GOODS_STATUS_WAITING -> 2
            GoodsNewFragment.GOODS_STATUS_DISABLE -> 3
            GoodsNewFragment.GOODS_STATUS_SOLD_OUT -> 4
            else -> 0
        }
    }
}