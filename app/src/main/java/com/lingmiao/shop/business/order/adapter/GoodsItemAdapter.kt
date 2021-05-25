package com.lingmiao.shop.business.order.adapter

import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.lingmiao.shop.R
import com.lingmiao.shop.business.order.bean.Sku
import com.lingmiao.shop.util.GlideUtils

/**
Create Date : 2021/3/125:04 AM
Auther      : Fox
Desc        :
 **/
class GoodsItemAdapter : BaseQuickAdapter<Sku, BaseViewHolder>(R.layout.sales_adapter_order_item) {

    override fun convert(helper: BaseViewHolder, item: Sku?) {
        helper.setText(R.id.orderGoodsNameTv, item?.name)
        helper.setText(R.id.orderGoodsQuantityTv, "x" + item?.num)

        GlideUtils.setImageUrl(helper.getView(R.id.orderGoodsIv), item?.goodsImage)
    }
}