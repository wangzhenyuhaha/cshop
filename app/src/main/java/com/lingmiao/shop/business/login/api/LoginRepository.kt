package com.lingmiao.shop.business.login.api

import com.lingmiao.shop.net.Fetch

object LoginRepository {
    public val apiService by lazy {
        Fetch.createService(LoginApiService::class.java)
    }
}