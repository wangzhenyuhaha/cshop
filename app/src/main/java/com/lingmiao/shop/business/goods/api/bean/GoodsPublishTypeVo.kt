package com.lingmiao.shop.business.goods.api.bean

import com.chad.library.adapter.base.entity.AbstractExpandableItem
import com.chad.library.adapter.base.entity.MultiItemEntity
import com.lingmiao.shop.business.commonpop.bean.ItemData

/**
Create Date : 2021/3/95:41 AM
Auther      : Fox
Desc        :
 **/
class GoodsPublishTypeVo : AbstractExpandableItem<GoodsPublishTypeVo>(), ItemData, MultiItemEntity  {

    var id : String? = "";

    var name : String? = "";

    var showLevel: Int = 0;

    var pPosition : Int = 0;

    var parentLevel: Int = 0;

    var children : List<GoodsPublishTypeVo>? = mutableListOf();

    override fun getIValue(): String? {
        return id;
    }

    override fun getIName(): String? {
        return name;
    }

    override fun getIHint(): String? {
        return name;
    }

    @Deprecated("暂未启用")
    override fun isItemChecked(): Boolean? {
        return false;
    }

    @Deprecated("暂未启用")
    override fun shiftChecked(flag: Boolean?) {

    }

    override fun getItemType(): Int {
        return showLevel;
    }

    override fun getLevel(): Int {
        return (showLevel ?: 1) - 1;
    }

}