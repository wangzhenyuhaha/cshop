package com.lingmiao.shop.business.goods.event

/**
 * Author : Elson
 * Date   : 2020/8/26
 * Desc   : 刷新商品管理列表
 */
class RefreshGoodsStatusEvent(val status: Int = 0) {

    fun isRefresh(currentStatus: Int?): Boolean {
        return status == 0 || status == currentStatus
    }
}