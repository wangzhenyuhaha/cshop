package com.lingmiao.shop.business.tools.adapter

import androidx.core.content.ContextCompat
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.lingmiao.shop.MyApp
import com.lingmiao.shop.R
import com.lingmiao.shop.business.tools.bean.PeekTime

class RangeAdapter : BaseQuickAdapter<PeekTime, BaseViewHolder>(R.layout.tools_adapter_range) {

    override fun convert(helper: BaseViewHolder, item: PeekTime?) {
        //删除按钮
        helper.addOnClickListener(R.id.tv_model_range_delete)
        //第一行删除按钮隐藏
        helper.setVisible(R.id.tv_model_range_delete, helper.adapterPosition != 0)
        //结束时间
        helper.addOnClickListener(R.id.et_model_range_end)
        //开始时间
        helper.addOnClickListener(R.id.et_model_range_start)
        //设置删除按钮字体颜色
        helper.setTextColor(R.id.tv_model_range_delete, getColor(helper.adapterPosition))
        //开始时间
        helper.setText(R.id.et_model_range_start, item?.peekTimeStart)
        //结束时间
        helper.setText(R.id.et_model_range_end, item?.peekTimeEnd)
        //加收价格
        helper.setText(R.id.et_model_range_price, String.format("%s", item?.peekTimePrice))
        helper.setEnabled(R.id.et_model_range_price, item?.enable ?: true)
        //监控更改的价格
        addTextChangeListener(helper.getView(R.id.et_model_range_price), item?.peekTimePrice) {
            item?.peekTimePrice = it
        }
    }

    fun getColor(position: Int): Int {
        return ContextCompat.getColor(
            MyApp.getInstance(),
            if (position == 0) R.color.color_999999 else R.color.red
        )
    }
}