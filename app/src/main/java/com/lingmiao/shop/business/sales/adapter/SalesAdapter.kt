package com.lingmiao.shop.business.sales.adapter

import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.lingmiao.shop.R
import com.lingmiao.shop.business.sales.bean.SalesActivityItemVo
import com.lingmiao.shop.business.sales.bean.SalesVo
import com.lingmiao.shop.business.sales.view.SalesActivityRvLayout
import com.lingmiao.shop.util.MINUTES_TIME_FORMAT_OTHER
import com.lingmiao.shop.util.stampToDate

/**
 * Author : Elson
 * Date   : 2020/7/18
 * Desc   : 商品分组管理列表
 */
class SalesAdapter : BaseQuickAdapter<SalesVo, BaseViewHolder>(R.layout.sales_adapter_sales) {

    override fun convert(helper: BaseViewHolder, item: SalesVo?) {
        item?.apply {
            helper.setText(R.id.salesNameTv, "满 $fullMoney 元减 $minusValue 元")
            helper.setText(R.id.salesStatusTv, statusText)


            helper.setText(
                R.id.salesActivityC,
                "${
                    stampToDate(
                        startTime ?: 0,
                        MINUTES_TIME_FORMAT_OTHER
                    )
                } ~ ${stampToDate(endTime ?: 0, MINUTES_TIME_FORMAT_OTHER)}"
            )


            helper.addOnClickListener(R.id.tvSalesDelete)
            helper.addOnClickListener(R.id.tvSalesEnd)
            helper.addOnClickListener(R.id.tvSalesView)
            helper.addOnClickListener(R.id.tvSalesEdit)
            helper.addOnClickListener(R.id.salesGoodsTv)



            when (status) {
                "WAIT" -> {
                    helper.setGone(R.id.tvSalesView, false);
                    helper.setGone(R.id.tvSalesEdit, true);
                    helper.setGone(R.id.tvSalesDelete, true);
                    helper.setGone(R.id.tvSalesEnd, false);
                }
                "UNDERWAY" -> {
                    helper.setGone(R.id.tvSalesEnd, true);
                    helper.setGone(R.id.tvSalesView, true);
                    helper.setGone(R.id.tvSalesDelete, false);
                    helper.setGone(R.id.tvSalesEdit, false);
                }
                "END" -> {
                    helper.setGone(R.id.tvSalesEnd, false);
                    helper.setGone(R.id.tvSalesView, false);
                    helper.setGone(R.id.tvSalesDelete, true);
                    helper.setGone(R.id.tvSalesEdit, false);
                }
                else -> {
                    helper.setGone(R.id.tvSalesView, false);
                    helper.setGone(R.id.tvSalesDelete, false);
                    helper.setGone(R.id.tvSalesEnd, false);
                    helper.setGone(R.id.tvSalesEdit, false);
                }

            }
        }
    }

}