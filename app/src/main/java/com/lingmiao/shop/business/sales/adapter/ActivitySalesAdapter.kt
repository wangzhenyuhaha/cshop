package com.lingmiao.shop.business.sales.adapter

import androidx.core.content.ContextCompat
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.lingmiao.shop.MyApp
import com.lingmiao.shop.R
import com.lingmiao.shop.business.sales.bean.SalesActivityItemVo
import com.lingmiao.shop.business.tools.adapter.addTextChangeListener
import com.lingmiao.shop.business.tools.bean.DistanceSection
import com.lingmiao.shop.util.formatDouble

class ActivitySalesAdapter : BaseQuickAdapter<SalesActivityItemVo, BaseViewHolder>(R.layout.sales_adapter_activity_discount) {

    override fun convert(helper: BaseViewHolder, item: SalesActivityItemVo?) {
        helper.addOnClickListener(R.id.discountDeleteTv);
        helper.setVisible(R.id.discountDeleteTv, helper.adapterPosition != 0);
        helper.setTextColor(R.id.discountDeleteTv, getColor(helper.adapterPosition));

        helper.setText(R.id.discountPeachTv, String.format("%s", item?.peach));
        helper.setText(R.id.discountLeastTv, String.format("%s", item?.least));

        addTextChangeListener(helper.getView(R.id.discountPeachTv), String.format("%s", item?.peach?:0)) {
            item?.peach = it?.toDouble();
        }
        addTextChangeListener(helper.getView(R.id.discountLeastTv), String.format("%s",item?.least?:0)) {
            item?.least = it?.toDouble();
        }
    }

    fun getColor(position : Int): Int {
        return ContextCompat.getColor(MyApp.getInstance(), if(position == 0) R.color.color_999999 else R.color.red)
    }


}