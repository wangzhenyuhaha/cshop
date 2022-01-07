package com.lingmiao.shop.business.sales.adapter

import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.lingmiao.shop.R
import com.lingmiao.shop.business.sales.bean.Coupon

class DiscountAdapter :
    BaseQuickAdapter<Coupon, BaseViewHolder>(R.layout.sales_adapter_discount_item) {


    override fun convert(helper: BaseViewHolder, item: Coupon?) {
        helper.setText(R.id.orderGoodsNameTv, item?.title)
    }

}