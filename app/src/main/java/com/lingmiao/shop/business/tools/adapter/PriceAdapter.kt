package com.lingmiao.shop.business.tools.adapter

import androidx.core.content.ContextCompat
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.lingmiao.shop.MyApp
import com.lingmiao.shop.R
import com.lingmiao.shop.business.tools.bean.DistanceSection

class PriceAdapter : BaseQuickAdapter<DistanceSection, BaseViewHolder>(R.layout.tools_adapter_price) {

    override fun convert(helper: BaseViewHolder, item: DistanceSection?) {
        helper.addOnClickListener(R.id.tv_model_price_delete);
        helper.setVisible(R.id.tv_model_price_delete, helper.adapterPosition != 0);
        helper.setTextColor(R.id.tv_model_price_delete, getColor(helper.adapterPosition));

        helper.setEnabled(R.id.et_model_price_start, helper.adapterPosition != 0);
        if(helper.adapterPosition == 0) {
            item?.startDistance = "0";
        }

        helper.setText(R.id.et_model_price_start, String.format("%s", item?.startDistance));
        helper.setText(R.id.et_model_price_end, String.format("%s", item?.endDistance));
        helper.setText(R.id.et_model_price, String.format("%s", item?.shipPrice));

        addTextChangeListener(helper.getView(R.id.et_model_price_start), item?.startDistance) {
            item?.startDistance = it;
        }
        addTextChangeListener(helper.getView(R.id.et_model_price_end), item?.endDistance) {
            item?.endDistance = it;
        }
        addTextChangeListener(helper.getView(R.id.et_model_price), item?.shipPrice) {
            item?.shipPrice = it;
        }
    }

    fun getColor(position : Int): Int {
        return ContextCompat.getColor(MyApp.getInstance(), if(position == 0) R.color.color_999999 else R.color.red)
    }


}