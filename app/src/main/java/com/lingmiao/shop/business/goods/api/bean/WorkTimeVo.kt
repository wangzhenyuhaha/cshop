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

    @Deprecated("暂未启用")
    override fun isItemChecked(): Boolean? {
        return false;
    }

    @Deprecated("暂未启用")
    override fun shiftChecked(flag: Boolean?) {

    }

    fun getFullDayType() : Int {
        return if(isFullDay(itemValue?.toInt()?:0)) 1 else 0;
    }

    companion object {

        val DEFAULT_SECOND_DAY_BASE_VALUE = 100;

        fun isFullDay(value : Int) : Boolean {
            return value > DEFAULT_SECOND_DAY_BASE_VALUE;
        }

        fun getList(value : Int): List<WorkTimeVo> {
            val data = mutableListOf<WorkTimeVo>()
            var item: WorkTimeVo? = null;
            var position : Int = 0;
            var hour = "";
            for (index in 0..23) {
                position ++;

                hour = String.format("%s%s", if(value == 1) "第二天" else "", if (index > 9) index else "0" + index);

                item = WorkTimeVo();
                item.itemName = String.format("%s:00", hour);
                item.itemValue = String.format("%s", value * DEFAULT_SECOND_DAY_BASE_VALUE + position);
                data.add(item);


                item = WorkTimeVo();
                item.itemName = String.format("%s:30", hour);
                item.itemValue = String.format("%s", value * DEFAULT_SECOND_DAY_BASE_VALUE + position);
                data.add(item);

//                if(index < 24) {
//                    position ++;
//                    item = WorkTimeVo();
//                    item.itemName = String.format("%s%s:30",if(value == 1) "第二天" else "",  if (index > 9) index else "0" + index);
//                    item.itemValue = String.format("%s", value * DEFAULT_SECOND_DAY_BASE_VALUE + position);
//                    data.add(item);
//                }
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
//            list?.forEachIndexed { index, item ->
//                if (item?.getIValue() == value && index < list?.size) {
//                    newList.addAll(list?.subList(index + 1, list?.size));
//                }
//            }
            newList.addAll(getWorkTimeList());
            newList.addAll(getTomorrowWorkTimeList())
            return newList;
        }
    }
}