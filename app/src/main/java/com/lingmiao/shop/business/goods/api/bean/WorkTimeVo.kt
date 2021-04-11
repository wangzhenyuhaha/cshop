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

        fun getList(value : Int): List<WorkTimeVo> {
            val data = mutableListOf<WorkTimeVo>()
            var item: WorkTimeVo? = null;
            var position : Int = 0;
            for (index in 0..24) {
                position ++;
                item = WorkTimeVo();
                item.itemName = String.format("第二天%s:00", if (index > 9) index else "0" + index);
                item.itemValue = String.format("%s", value * 100 + position);
                data.add(item);


                if(index < 24) {
                    position ++;
                    item = WorkTimeVo();
                    item.itemName = String.format("第二天%s:30", if (index > 9) index else "0" + index);
                    item.itemValue = String.format("%s", value * 100 + position);
                    data.add(item);
                }
            }
            return data;
        }

        fun getTomorrowWorkTimeList(): List<WorkTimeVo> {
            return getList(1);
        }

        fun getWorkTimeList(): List<WorkTimeVo> {
            return getList(0);
        }

        fun getWorkTimeList(value: String?, list: List<WorkTimeVo>?): List<WorkTimeVo> {
            var newList : MutableList<WorkTimeVo> = mutableListOf();
            list?.forEachIndexed { index, item ->
                if (item?.getIValue() == value && index < list?.size) {
                    newList.addAll(list?.subList(index + 1, list?.size));
                }
            }
            newList.addAll(getTomorrowWorkTimeList())
            return newList;
        }
    }
}