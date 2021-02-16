package com.lingmiao.shop.business.tools.adapter

import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.lingmiao.shop.R
import com.lingmiao.shop.business.tools.bean.FreightVoItem

class FreightModelAdapter : BaseQuickAdapter<FreightVoItem, BaseViewHolder>(R.layout.tools_adapter_freight_model) {

    override fun convert(helper: BaseViewHolder, item: FreightVoItem?) {
        helper.setText(R.id.tv_model_name, item?.name);
        helper.setText(R.id.tv_model_type, item?.getTemplateTypeName());
        helper.setText(R.id.tv_model_fee_type, item?.getTypeName());
        helper.addOnClickListener(R.id.tv_model_edit);
        helper.addOnClickListener(R.id.tv_model_delete);
    }

}