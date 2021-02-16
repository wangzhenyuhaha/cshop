package com.lingmiao.shop.business.tools.adapter

import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.lingmiao.shop.R
import com.lingmiao.shop.business.tools.bean.Item

class AreaOfCountAdapter : BaseQuickAdapter<Item, BaseViewHolder>(R.layout.tools_adapter_area_count) {
    override fun convert(helper: BaseViewHolder, item: Item?) {

        helper.addOnClickListener(R.id.tv_model_area_count_edit);
        helper.addOnClickListener(R.id.tv_model_area_count_delete);

        helper.setText(R.id.tv_model_count_value, item?.getAreaStr());
        helper.setText(R.id.et_count_first, String.format("%s", item?.firstCompany));
        helper.setText(R.id.et_count_first_fee, String.format("%s", item?.firstPrice));
        helper.setText(R.id.et_more_count_first, String.format("%s", item?.continuedCompany));
        helper.setText(R.id.et_more_count_first_fee, String.format("%s", item?.continuedPrice));

        addTextChangeListener(helper.getView(R.id.et_count_first), item?.firstCompany) {
            item?.firstCompany = it;
        }
        addTextChangeListener(helper.getView(R.id.et_count_first_fee), item?.firstPrice) {
            item?.firstPrice = it;
        }
        addTextChangeListener(helper.getView(R.id.et_more_count_first), item?.continuedCompany) {
            item?.continuedCompany = it;
        }
        addTextChangeListener(helper.getView(R.id.et_more_count_first_fee), item?.continuedPrice) {
            item?.continuedPrice = it;
        }
    }

}