package com.lingmiao.shop.business.tools.adapter

import android.widget.TextView
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.lingmiao.shop.R

class CitiesAdapter : BaseQuickAdapter<String, BaseViewHolder>(R.layout.tools_adapter_city) {
    override fun convert(helper: BaseViewHolder, item: String?) {

        helper.getView<TextView>(R.id.tv_city_name).setText(item);

    }
}