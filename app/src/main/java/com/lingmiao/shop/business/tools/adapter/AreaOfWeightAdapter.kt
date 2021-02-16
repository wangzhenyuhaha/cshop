package com.lingmiao.shop.business.tools.adapter

import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.lingmiao.shop.R
import com.lingmiao.shop.business.tools.bean.FreightVoItem
import com.lingmiao.shop.business.tools.bean.Item

class AreaOfWeightAdapter() : BaseQuickAdapter<Item, BaseViewHolder>(R.layout.tools_adapter_area_weight) {
    private var viewType : Int ? = null;

    init {
        viewType = FreightVoItem.TYPE_KM_COUNT;
    }

    fun setViewType(type : Int) {
        viewType = type;
        notifyDataSetChanged();
    }

    fun shiftWeightType() {
        setViewType(FreightVoItem.TYPE_WEIGHT);
    }

    fun shiftCountType() {
        setViewType(FreightVoItem.TYPE_COUNT);
    }

    fun isCountType() : Boolean {
        return viewType == FreightVoItem.TYPE_COUNT;
    }

    override fun convert(helper: BaseViewHolder, item: Item?) {

        helper.addOnClickListener(R.id.tv_model_area_weight_edit);
        helper.addOnClickListener(R.id.tv_model_area_weight_delete);

        helper.setText(R.id.tv_weight_first, if(isCountType()) "首件(个)：" else "首重(kg)：");
        helper.setText(R.id.tv_more_weight_first, if(isCountType()) "续件(个)：" else "续重(kg)：");

        helper.setText(R.id.tv_model_weight_value, item?.getAreaStr());
        helper.setText(R.id.et_weight_first, String.format("%s", item?.firstCompany));
        helper.setText(R.id.et_weight_first_fee, String.format("%s", item?.firstPrice));
        helper.setText(R.id.et_more_weight_first, String.format("%s", item?.continuedCompany));
        helper.setText(R.id.et_more_weight_first_fee, String.format("%s", item?.continuedPrice));

        addTextChangeListener(helper.getView(R.id.et_weight_first), item?.firstCompany) {
            item?.firstCompany = it;
        }
        addTextChangeListener(helper.getView(R.id.et_weight_first_fee), item?.firstPrice) {
            item?.firstPrice = it;
        }
        addTextChangeListener(helper.getView(R.id.et_more_weight_first), item?.continuedCompany) {
            item?.continuedCompany = it;
        }
        addTextChangeListener(helper.getView(R.id.et_more_weight_first_fee), item?.continuedPrice) {
            item?.continuedPrice = it;
        }
    }

}