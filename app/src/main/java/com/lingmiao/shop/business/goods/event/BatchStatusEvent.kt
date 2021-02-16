package com.lingmiao.shop.business.goods.event

class BatchStatusEvent(val status : Int = 0) {
    fun isRefresh(currentStatus: Int?): Boolean {
        return status == 0 || status == currentStatus
    }
}
