package com.lingmiao.shop.business.sales.adapter

import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.lingmiao.shop.R
import com.lingmiao.shop.business.sales.bean.SalesActivityItemVo
import com.lingmiao.shop.business.sales.bean.SalesVo
import com.lingmiao.shop.business.sales.view.SalesActivityRvLayout

/**
 * Author : Elson
 * Date   : 2020/7/18
 * Desc   : 商品分组管理列表
 */
class SalesAdapter() : BaseQuickAdapter<SalesVo, BaseViewHolder>(R.layout.sales_adapter_sales) {

    override fun convert(helper: BaseViewHolder, item: SalesVo?) {
        item?.apply {

            helper.setText(R.id.salesNameTv, title);
            helper.setText(R.id.salesStatusTv, statusText);
            helper.getView<SalesActivityRvLayout>(R.id.salesActivityC).addOneItem(SalesActivityItemVo.convert(item));
            helper.addOnClickListener(R.id.tvSalesDelete);
            helper.addOnClickListener(R.id.tvSalesEnd);
            helper.addOnClickListener(R.id.tvSalesView);
            helper.addOnClickListener(R.id.tvSalesEdit);
            helper.addOnClickListener(R.id.salesGoodsTv);

            when(status) {
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