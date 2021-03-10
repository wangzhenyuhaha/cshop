package com.lingmiao.shop.business.goods.api.bean

import com.chad.library.adapter.base.entity.AbstractExpandableItem
import com.chad.library.adapter.base.entity.MultiItemEntity

/**
Create Date : 2021/3/104:43 PM
Auther      : Fox
Desc        :
 **/
class MenuVo : AbstractExpandableItem<MenuVo>(),MultiItemEntity{

    var pPosition : Int = 0;

    var showLevel: Int = 0;

    var name : String? = "";

    /**
     * 是否选中
     */
    var isChecked : Boolean ? = false

    override fun getItemType(): Int {
        return showLevel;
    }

    override fun getLevel(): Int {
        return (showLevel ?: 1) - 1;
    }

}