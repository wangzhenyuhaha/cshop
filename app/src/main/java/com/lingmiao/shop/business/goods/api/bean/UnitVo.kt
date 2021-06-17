package com.lingmiao.shop.business.goods.api.bean

import com.lingmiao.shop.business.commonpop.bean.ItemData

/**
Create Date : 2021/3/95:07 PM
Auther      : Fox
Desc        :
 **/
class UnitVo : ItemData {
    var name : String?= "";
    var value : String?= "";

    override fun getIValue(): String? {
        return value;
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

    companion object {
        fun getWeightList() : MutableList<UnitVo> {
            var list = mutableListOf<UnitVo>();

            var item = UnitVo();
            item.name = "g"
            item.value = "0";
            list.add(item);

            item = UnitVo();
            item.name = "Kg"
            item.value = "1";
            list.add(item);
            return list;
        }

        fun getUnitList() :  MutableList<UnitVo> {
            var list = mutableListOf<UnitVo>();

            var item = UnitVo();
            item.name = "个"
            item.value = "0";
            list.add(item);

            item = UnitVo();
            item.name = "盒"
            item.value = "1";
            list.add(item);

            item = UnitVo();
            item.name = "袋"
            item.value = "2";
            list.add(item);
            return list;
        }
    }
}