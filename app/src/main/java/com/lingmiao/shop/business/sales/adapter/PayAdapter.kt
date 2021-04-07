package com.lingmiao.shop.business.sales.adapter

import android.widget.SeekBar
import com.chad.library.adapter.base.BaseMultiItemQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.lingmiao.shop.R
import com.lingmiao.shop.business.sales.bean.PayItem

/**
Create Date : 2021/4/79:30 AM
Auther      : Fox
Desc        :
 **/
class PayAdapter(list : List<PayItem>?) : BaseMultiItemQuickAdapter<PayItem, BaseViewHolder>(list) {

    init {
        addItemType(PayItem.TYPE_TITLE, R.layout.stats_adapter_pay_title);
        addItemType(PayItem.TYPE_IN, R.layout.stats_adapter_pay_hint);
        addItemType(PayItem.TYPE_IN_ITEM, R.layout.stats_adapter_pay_item);
        addItemType(PayItem.TYPE_OUT, R.layout.stats_adapter_pay_hint);
        addItemType(PayItem.TYPE_OUT_ITEM, R.layout.stats_adapter_pay_item);
    }

    override fun convert(helper: BaseViewHolder, item: PayItem?) {
        item?.apply {
            if (itemType == PayItem.TYPE_TITLE) {
                // payTypeTv
            } else if(itemType == PayItem.TYPE_IN) {
                helper.setText(R.id.payTypeHintTv, "收入")
                helper.setText(R.id.payAmountTv, "￥2000")
            } else if(itemType == PayItem.TYPE_IN_ITEM) {
                helper.setText(R.id.payTypeItemTv, "订单");
                helper.setText(R.id.payTypeItemPercentTv, "80%");
                helper.setText(R.id.payTypeItemAmountTv, "￥2000");
                helper.setText(R.id.payTypeItemCountTv, "共计12单");
                helper.getView<SeekBar>(R.id.payValueSb).progress = 30;
            } else if(itemType == PayItem.TYPE_OUT) {
                helper.setText(R.id.payTypeHintTv, "支出")
                helper.setText(R.id.payAmountTv, "￥2000")
            } else if(itemType == PayItem.TYPE_OUT_ITEM) {
                helper.setText(R.id.payTypeItemTv, "购买");
                helper.setText(R.id.payTypeItemPercentTv, "80%");
                helper.setText(R.id.payTypeItemAmountTv, "￥2000");
//                helper.getView<SeekBar>(R.id.payValueSb).progress = 30;
            }
        }
    }
}