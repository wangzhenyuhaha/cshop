package com.lingmiao.shop.business.order.api

import com.lingmiao.shop.net.Fetch

object OrderRepository {
    public val apiService by lazy {
        Fetch.createService(OrderApi::class.java)
    }
}