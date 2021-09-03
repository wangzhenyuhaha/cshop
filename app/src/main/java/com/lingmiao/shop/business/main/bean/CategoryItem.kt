package com.lingmiao.shop.business.main.bean

import com.lingmiao.shop.business.commonpop.bean.ItemData


class CategoryItem : ItemData {

    var itemName: String? = ""

    var itemHint: String? = ""

    var itemValue: String? = ""

    var id: String = ""
    var name: String = ""


    override fun getIValue(): String {
        return id
    }

    override fun getIName(): String {
        return name
    }

    override fun getIHint(): String? {
        return itemHint
    }

    override fun isItemChecked(): Boolean {
        return false
    }

    override fun shiftChecked(flag: Boolean?) {

    }

}