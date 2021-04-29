package com.lingmiao.shop.business.main.api

import com.lingmiao.shop.net.Fetch

object MemberRepository {

     val apiService by lazy {
        Fetch.createService(MemberApi::class.java)
    }

}