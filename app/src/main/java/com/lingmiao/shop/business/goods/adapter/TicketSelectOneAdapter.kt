package com.lingmiao.shop.business.goods.adapter

import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.lingmiao.shop.R
import com.lingmiao.shop.business.sales.bean.ElectronicVoucher
import com.lingmiao.shop.util.MINUTES_TIME_FORMAT_OTHER
import com.lingmiao.shop.util.formatString
import java.util.*

class TicketSelectOneAdapter :
    BaseQuickAdapter<ElectronicVoucher, BaseViewHolder>(R.layout.sales_adapter_ticket_selected) {

    private var selectedID: Int? = null

    fun setItem(coupon_id: Int?) {
        this.selectedID = coupon_id
    }

    override fun convert(helper: BaseViewHolder, item: ElectronicVoucher?) {

        //电子券面额
        helper.setText(R.id.jianPrice, "¥ ${item?.jianPrice.toString()}")

        //电子券名字
        helper.setText(R.id.title, item?.title)

        val number = (item?.createNum ?: 0) - (item?.receivedNum ?: 0)
        //电子券库存
        helper.setText(R.id.createNum, "（库存${number}）")

        //电子券状态
        val start = formatString(
            item?.useStartTime?.times(1000)?.let { Date(it) },
            MINUTES_TIME_FORMAT_OTHER
        )
        val end = formatString(
            item?.useEndTime?.times(1000)?.let { Date(it) },
            MINUTES_TIME_FORMAT_OTHER
        )
        helper.setText(R.id.couponStatus, "开始： $start\n结束： $end")

        //判断是否长期有效
        if (
            (
                    (end?.substring(0, 4)?.toInt() ?: 0) - (start?.substring(0, 4)?.toInt() ?: 0)
                    ) == 30
        ) {
            //长期有效
            helper.setText(R.id.couponStatus, "长期有效")
        }

        item?.apply {
            if (item.couponID == selectedID) {
                helper.setChecked(R.id.menuIv, true)
            } else {
                helper.setChecked(R.id.menuIv, false)
            }
            helper.addOnClickListener(R.id.menuIv)
        }
    }


}