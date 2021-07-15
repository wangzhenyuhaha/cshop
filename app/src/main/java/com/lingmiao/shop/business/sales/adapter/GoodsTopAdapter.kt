package com.lingmiao.shop.business.sales.adapter

import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.lingmiao.shop.R
import com.lingmiao.shop.business.sales.bean.GoodsItem
import com.lingmiao.shop.business.sales.bean.SalesGoodsTop10
import com.lingmiao.shop.util.formatDouble

/**
Create Date : 2021/4/71:27 PM
Auther      : Fox
Desc        :
 **/
class GoodsTopAdapter : BaseQuickAdapter<SalesGoodsTop10, BaseViewHolder>(R.layout.sales_adapter_goods_top) {

    override fun convert(helper: BaseViewHolder, item: SalesGoodsTop10?) {
        helper.setText(R.id.userCountTv, item?.goodsName)
        helper.setText(R.id.salesAverageTv, formatDouble(item?.sumGoodsPrice))
        helper.setText(R.id.salesCountTv, String.format("%s", item?.sumGoodsNum))
        //salesAmountTv
    }

}