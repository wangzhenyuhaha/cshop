package com.lingmiao.shop.business.tools.adapter

import androidx.core.content.ContextCompat
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.lingmiao.shop.MyApp
import com.lingmiao.shop.R
import com.lingmiao.shop.business.tools.bean.TimeSection

class TimeAdapter : BaseQuickAdapter<TimeSection, BaseViewHolder>(R.layout.tools_adapter_time) {

    override fun convert(helper: BaseViewHolder, item: TimeSection?) {
        //删除按钮点击
        helper.addOnClickListener(R.id.tv_model_time_delete)
        helper.setTextColor(R.id.tv_model_time_delete, getColor(helper.adapterPosition))

        helper.addOnClickListener(R.id.et_model_km_start)
        //送达上界
        helper.addOnClickListener(R.id.et_model_km_endo)
        //送达下届
        helper.addOnClickListener(R.id.et_model_km_end)
        helper.addOnClickListener(R.id.tv_model_time_type)

        //付款时间
        helper.setText(R.id.et_model_km_start, item?.shipTime)
        //送达时间上界
        helper.setText(R.id.et_model_km_endo, item?.arriveStartTime)
        //送达时间下届
        helper.setText(R.id.et_model_km_end, item?.arriveTime)
        //送达类型
        helper.setText(R.id.tv_model_time_type, item?.getTimeType())
    }

    fun getColor(position : Int): Int {
        return ContextCompat.getColor(MyApp.getInstance(), if(position == 0) R.color.color_999999 else R.color.red)
    }
}