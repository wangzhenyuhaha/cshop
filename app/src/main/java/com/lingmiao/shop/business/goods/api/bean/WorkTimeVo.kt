package com.lingmiao.shop.business.goods.api.bean

import com.lingmiao.shop.business.commonpop.bean.ItemData

/**
Create Date : 2021/3/74:01 PM
Auther      : Fox
Desc        :
 **/
class WorkTimeVo : ItemData {

    var itemName: String? = null

    var itemHint: String? = null

    var itemValue: String? = null

    override fun getIValue(): String? {
        return itemValue;
    }

    override fun getIName(): String? {
        return itemName;
    }

    override fun getIHint(): String? {
        return itemHint;
    }

    companion object {

        fun getWorkTimeList(): List<WorkTimeVo> {
            val data = mutableListOf<WorkTimeVo>()
            var item: WorkTimeVo? = null;
            for (index in 0..23) {
                item = WorkTimeVo();
                item.itemName = String.format("%s:00", if (index > 9) index else "0" + index);
                item.itemValue = String.format("%s", index + 1);
                data.add(item);

                item = WorkTimeVo();
                item.itemName = String.format("%s:30", if (index > 9) index else "0" + index);
                item.itemValue = String.format("%s", (index + 1) * 100);
                data.add(item);
            }
            return data;
        }

        fun getWorkTimeList(value: String?, list: List<WorkTimeVo>?): List<WorkTimeVo> {
            list?.forEachIndexed { index, item ->
                if (item?.getIValue() == value && index < list?.size) {
                    return list?.subList(index + 1, list?.size);
                }
            }
            return mutableListOf();
        }
    }
}