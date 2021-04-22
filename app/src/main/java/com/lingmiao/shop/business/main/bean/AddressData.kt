package com.lingmiao.shop.business.main.bean

import com.amap.api.maps.model.LatLng
import java.io.Serializable


/**
Create Date : 2021/4/211:14 PM
Auther      : Fox
Desc        :
 **/
class AddressData : Serializable {
    var latLng: LatLng? = null
    var province: String? = null
    var city: String? = null
    var district: String? = null
    var address: String? = null
}