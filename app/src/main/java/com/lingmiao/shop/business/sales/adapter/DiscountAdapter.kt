package com.lingmiao.shop.business.sales.adapter

import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.lingmiao.shop.R
import com.lingmiao.shop.business.sales.bean.Coupon
import com.lingmiao.shop.util.MINUTES_TIME_FORMAT_OTHER
import com.lingmiao.shop.util.formatString
import java.util.*

class DiscountAdapter :
    BaseQuickAdapter<Coupon, BaseViewHolder>(R.layout.sales_adapter_discount_item) {


    override fun convert(helper: BaseViewHolder, item: Coupon?) {

        //优惠券面额
        helper.setText(R.id.jianPrice, "¥ ${item?.jianPrice.toString()}")

        //优惠券门槛
        helper.setText(R.id.manPrice, "满 ${item?.manPrice.toString()} 可用")

        //优惠券名字
        helper.setText(R.id.title, item?.title)

        val number =  (item?.createNum?:0 )- (item?.receivedNum ?:0)
        //优惠券库存
        helper.setText(R.id.createNum, "（库存${number}）")

        //优惠券状态
        val start = formatString(
            item?.couponStartTime?.times(1000)?.let { Date(it) },
            MINUTES_TIME_FORMAT_OTHER
        )
        val end = formatString(
            item?.couponEndTime?.times(1000)?.let { Date(it) },
            MINUTES_TIME_FORMAT_OTHER
        )
        val now = Date().time
        if (now > item?.couponEndTime?.times(1000) ?: 0) {
            //过期了
            helper.setText(R.id.couponStatus, "优惠券已过期")
            helper.setGone(R.id.couponDetail, true)
            helper.setGone(R.id.couponBegin, false)
            helper.setGone(R.id.couponDelete, true)
        } else {
            //没过期，可发放
            helper.setText(R.id.couponStatus, "开始： $start\n结束： $end")
            helper.setText(R.id.couponBegin, if (item?.disabled == -1) "开始发放" else "停止发放")
            helper.setGone(R.id.couponDetail, true)
            helper.setGone(R.id.couponBegin, true)
            helper.setGone(R.id.couponDelete, item?.disabled == -1)
        }

        //优惠券是否发放


        //点击操作


        //查看详情
        helper.addOnClickListener(R.id.couponDetail)

        //开始/停止发放
        helper.addOnClickListener(R.id.couponBegin)

        //删除
        helper.addOnClickListener(R.id.couponDelete)
    }

}