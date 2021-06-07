package com.lingmiao.shop.business.sales.adapter

import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.lingmiao.shop.R
import com.lingmiao.shop.business.sales.bean.SalesActivityItemVo

/**
Create Date : 2021/3/125:04 AM
Auther      : Fox
Desc        :
 **/
class SalesActivityItemAdapter : BaseQuickAdapter<SalesActivityItemVo, BaseViewHolder>(R.layout.sales_adapter_sales_activity) {

    override fun convert(helper: BaseViewHolder, item: SalesActivityItemVo?) {
        helper.setText(R.id.activityNameTv, String.format("满%s减%s", item?.peach, item?.least))
        helper.setText(R.id.activityHintTv, String.format("%s-%s", item?.startTime, item?.endTime))
    }
}