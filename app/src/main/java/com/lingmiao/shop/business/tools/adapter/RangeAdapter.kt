package com.lingmiao.shop.business.tools.adapter

import androidx.core.content.ContextCompat
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.lingmiao.shop.MyApp
import com.lingmiao.shop.R
import com.lingmiao.shop.business.tools.bean.PeekTime

class RangeAdapter : BaseQuickAdapter<PeekTime, BaseViewHolder>(R.layout.tools_adapter_range) {

    override fun convert(helper: BaseViewHolder, item: PeekTime?) {
        helper.addOnClickListener(R.id.tv_model_range_delete);
        helper.setVisible(R.id.tv_model_range_delete, helper.adapterPosition != 0);

        helper.addOnClickListener(R.id.et_model_range_end);
        helper.addOnClickListener(R.id.et_model_range_start);
        helper.setTextColor(R.id.tv_model_range_delete, getColor(helper.adapterPosition));

        helper.setText(R.id.et_model_range_start, item?.peekTimeStart);
        helper.setText(R.id.et_model_range_end, item?.peekTimeEnd);
        helper.setText(R.id.et_model_range_price, String.format("%s", item?.peekTimePrice));

        addTextChangeListener(helper.getView(R.id.et_model_range_price), item?.peekTimePrice) {
            item?.peekTimePrice = it;
        }
    }

    fun getColor(position : Int): Int {
        return ContextCompat.getColor(MyApp.getInstance(), if(position == 0) R.color.color_999999 else R.color.red)
    }
}