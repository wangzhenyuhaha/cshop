package com.lingmiao.shop.business.goods.adapter

import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.lingmiao.shop.R
import com.lingmiao.shop.business.goods.api.bean.GoodsInfoVo
import com.lingmiao.shop.business.goods.api.bean.GoodsParamVo
import com.lingmiao.shop.business.tools.adapter.addTextChangeListener
import com.lingmiao.shop.business.tools.bean.TimeSection

/**
Create Date : 2021/3/63:03 PM
Auther      : Fox
Desc        :
 **/
class GoodsInfoAdapter(list: List<GoodsParamVo>?) : BaseQuickAdapter<GoodsParamVo, BaseViewHolder>(R.layout.goods_adapter_goods_info) {
    /**
     * Implement this method and use the helper to adapt the view to the given item.
     *
     * @param helper A fully initialized helper.
     * @param item   The item that needs to be displayed.
     */
    override fun convert(helper: BaseViewHolder, item: GoodsParamVo?) {

        helper.addOnClickListener(R.id.infoDeleteTv);
        helper.setText(R.id.goodsInfoNameTv, item?.paramName);

        addTextChangeListener(helper.getView(R.id.goodsInfoContentTv), item?.paramValue) {
            item?.paramValue = it
        }
       // helper.setVisible(R.id.infoDeleteTv, helper.adapterPosition != 0);
    }
}