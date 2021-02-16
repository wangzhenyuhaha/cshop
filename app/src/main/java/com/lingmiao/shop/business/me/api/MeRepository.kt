package com.lingmiao.shop.business.me.api

import com.lingmiao.shop.net.Fetch

object MeRepository {
    public val apiService by lazy {
        Fetch.createService(MeApi::class.java)
    }
}