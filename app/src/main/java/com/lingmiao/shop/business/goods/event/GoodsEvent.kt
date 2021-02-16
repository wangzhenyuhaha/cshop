package com.lingmiao.shop.business.goods.event

import com.lingmiao.shop.business.goods.api.bean.ShopGroupVO

/**
 * Author : Elson
 * Date   : 2020/7/18
 * Desc   : 商品模块的事件类
 */

/**
 * 刷新商品管理页面事件
 */
class GroupRefreshEvent(val level: Int? = ShopGroupVO.LEVEL_1) {

    fun isRefreshLevel1() : Boolean {
        return level == ShopGroupVO.LEVEL_1
    }

    fun isRefreshLevel2() : Boolean {
        return level == ShopGroupVO.LEVEL_2
    }
}