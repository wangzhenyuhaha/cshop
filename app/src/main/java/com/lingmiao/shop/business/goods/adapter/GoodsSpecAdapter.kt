package com.lingmiao.shop.business.goods.adapter

import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.lingmiao.shop.R
import com.lingmiao.shop.business.goods.api.bean.SpecKeyVO
import com.lingmiao.shop.business.goods.view.SpecFlowLayout

/**
Create Date : 2021/6/221:57 PM
Auther      : Fox
Desc        : 商品规格
 **/
class GoodsSpecAdapter(val callback : (Int,String?)-> Unit): BaseQuickAdapter<SpecKeyVO, BaseViewHolder>(R.layout.goods_adapter_spec) {
    /**
     * Implement this method and use the helper to adapt the view to the given item.
     *
     * @param helper A fully initialized helper.
     * @param item   The item that needs to be displayed.
     */
    override fun convert(helper: BaseViewHolder, item: SpecKeyVO?) {
        helper.setText(R.id.specNameTv, String.format("规格名：%s", item?.specName));
        helper.addOnClickListener(R.id.deleteSpecTv)
        helper.addOnClickListener(R.id.addSpecValueTv)
        helper.getView<SpecFlowLayout>(R.id.specFlowLayout).apply{
            removeAllViews();
            addSpecValues(item?.specId, item?.valueList);
            clickDeleteCallback = {
                callback.invoke(helper.adapterPosition, it);
            }
        }

    }
}