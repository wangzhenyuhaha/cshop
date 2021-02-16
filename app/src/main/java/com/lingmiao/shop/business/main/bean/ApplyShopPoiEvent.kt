package com.lingmiao.shop.business.main.bean

import com.tencent.lbssearch.httpresponse.AdInfo
import com.tencent.lbssearch.httpresponse.Poi

data class ApplyShopPoiEvent(var poi:Poi,var adInfo:AdInfo?=null) {
}