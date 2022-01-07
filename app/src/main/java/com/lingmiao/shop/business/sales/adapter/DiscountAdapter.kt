package com.lingmiao.shop.business.sales.adapter

import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.lingmiao.shop.R
import com.lingmiao.shop.business.sales.bean.Coupon

class DiscountAdapter :
    BaseQuickAdapter<Coupon, BaseViewHolder>(R.layout.sales_adapter_discount_item) {


    override fun convert(helper: BaseViewHolder, item: Coupon?) {
        //优惠券面额
        helper.setText(R.id.jianPrice, "¥ ${item?.jianPrice.toString()}")
        //优惠券门槛
        helper.setText(R.id.manPrice, "满${item?.manPrice.toString()}可用")
        //优惠券名字
        helper.setText(R.id.title, item?.title)
        //优惠券库存
        helper.setText(R.id.createNum, "（库存${item?.createNum}）")
        //优惠券状态
        // helper.setText(R.id.orderGoodsNameTv, item?.title)
        //优惠券
    }

}