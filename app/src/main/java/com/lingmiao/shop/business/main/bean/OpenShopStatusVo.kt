package com.lingmiao.shop.business.main.bean

import com.google.gson.annotations.SerializedName
import java.io.Serializable

/**
Create Date : 2021/4/285:04 PM
Auther      : Fox
Desc        :
 **/
class OpenShopStatusVo : Serializable {

    @SerializedName("open_status")
    var openStatus: Boolean? = null;
}