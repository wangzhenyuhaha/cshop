package com.lingmiao.shop.business.main.api

import com.lingmiao.shop.net.Fetch

object MainRepository {
    val apiService by lazy {
        Fetch.createService(MainApi::class.java)
    }



}