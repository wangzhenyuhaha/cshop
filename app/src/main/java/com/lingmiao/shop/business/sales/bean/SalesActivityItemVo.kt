package com.lingmiao.shop.business.sales.bean

import com.lingmiao.shop.util.DATE_TIME_FORMAT_OTHER
import com.lingmiao.shop.util.longToDate
import com.lingmiao.shop.util.stampToDate

/**
Create Date : 2021/3/125:04 AM
Auther      : Fox
Desc        :
 **/
class SalesActivityItemVo {
    var peach : String?=""
    var least : String?=""
    var startTime : String? = ""
    var endTime : String? = ""

    companion object {
        fun convert(item: SalesVo) : SalesActivityItemVo {
            var it = SalesActivityItemVo();
            it.peach = item.fullMoney
            it.least = item.minusValue
            it.startTime = stampToDate(item?.startTime?:0, DATE_TIME_FORMAT_OTHER);
            it.endTime = stampToDate(item?.endTime?:0, DATE_TIME_FORMAT_OTHER);
            return it;
        }
    }
}