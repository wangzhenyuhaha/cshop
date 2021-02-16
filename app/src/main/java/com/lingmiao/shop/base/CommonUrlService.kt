package com.lingmiao.shop.base

import com.lingmiao.shop.business.goods.api.bean.GoodsUseExpireVo
import com.lingmiao.shop.business.wallet.bean.PageListVo
import com.james.common.netcore.networking.http.annotations.WithHiResponse
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

/**
Create Date : 2021/1/122:22 PM
Auther      : Fox
Desc        :
 **/
interface CommonUrlService {

    @WithHiResponse
    @POST("dict/query_list_by_type")
    fun queryListByType(@Query("type") json : String) : Call<PageListVo<GoodsUseExpireVo>>;
}