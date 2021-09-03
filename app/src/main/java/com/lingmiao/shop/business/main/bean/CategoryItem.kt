package com.lingmiao.shop.business.main.bean

import com.lingmiao.shop.business.commonpop.bean.ItemData


class CategoryItem : ItemData {

    var itemName: String? = ""

    var itemHint: String? = ""

    var itemValue: String? = ""

    var id: String = ""
    var name: String = ""

    //店铺经营类目    ids
    var goodsManagementCategory: String = ""

    //店铺经营类目 返回文本
    var categoryNames: String = ""

    //所属行业id（通联）
    var mccid: String = ""

    //所属行业名（通联
    var mcc_name: String = ""


    override fun getIValue(): String? {
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

    fun getFullDayType(): Int {
        return if (isFullDay(itemValue?.toInt() ?: 0)) 1 else 0
    }

    companion object {

        val DEFAULT_SECOND_DAY_BASE_VALUE = 100

        fun isSecondDay(first: String?, second: String?): Boolean {
            if (first.isNullOrEmpty() || second.isNullOrEmpty()) {
                return false
            }
            val f2 = second?.replace("第二天", "")
            return first?.replace(":", "")?.toInt() < f2?.replace(":", "")?.toInt()
        }

        fun isFullDay(value: Int): Boolean {
            return value > DEFAULT_SECOND_DAY_BASE_VALUE
        }

        fun getList(value: Int): List<CategoryItem> {
            val data = mutableListOf<CategoryItem>()
            var item: CategoryItem? = null
            var position = 0
            var hour = ""
            for (index in 0..23) {
                // 小时
                hour = String.format(
                    "%s%s",
                    if (value == 1) "第二天" else "",
                    if (index > 9) index else "0" + index
                )

                // 整点小时
                position++
                item = CategoryItem()
                item.itemName = String.format("%s:00", hour);
                item.itemValue =
                    String.format("%s", value * DEFAULT_SECOND_DAY_BASE_VALUE + position)
                data.add(item)

                // 半小时
                position++
                item = CategoryItem()
                item.itemName = String.format("%s:30", hour);
                item.itemValue =
                    String.format("%s", value * DEFAULT_SECOND_DAY_BASE_VALUE + position);
                data.add(item)
            }
            return data
        }

        fun getTomorrowWorkTimeList(): List<CategoryItem> {
            return getList(1)
        }

        fun getWorkTimeList(): List<CategoryItem> {
            return getList(0)
        }

        fun getWorkTimeList(value: CategoryItem?, list: List<CategoryItem>?): List<CategoryItem> {
            var newList: MutableList<CategoryItem> = mutableListOf();
            // 截取当天
            list?.forEachIndexed { index, item ->
                if (item?.getIValue()!! == value?.itemValue && index < list?.size) {
                    newList.addAll(list?.subList(index + 1, list?.size));
                }
            }
            // 截取第二天
            newList.addAll(getTomorrowWorkTimeList().subList(0, 48 - newList.size));
            return newList;
        }
    }
}