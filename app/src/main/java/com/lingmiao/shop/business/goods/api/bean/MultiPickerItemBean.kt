package com.lingmiao.shop.business.goods.api.bean

import com.google.gson.annotations.SerializedName

/**
Create Date : 2021/1/1011:42 AM
Auther      : Fox
Desc        :
 **/
class MultiPickerItemBean(

    var name: String? = null,

    var value : String ?= null,

    var isChecked : Boolean ?= false
) {
    companion object {

    }
}