package com.lingmiao.shop.business.goods.adapter

import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.lingmiao.shop.R
import com.lingmiao.shop.business.goods.api.bean.GoodsInfoVo
import com.lingmiao.shop.business.tools.bean.TimeSection

/**
Create Date : 2021/3/63:03 PM
Auther      : Fox
Desc        :
 **/
class GoodsInfoAdapter : BaseQuickAdapter<GoodsInfoVo, BaseViewHolder>(R.layout.goods_adapter_goods_info) {
    /**
     * Implement this method and use the helper to adapt the view to the given item.
     *
     * @param helper A fully initialized helper.
     * @param item   The item that needs to be displayed.
     */
    override fun convert(helper: BaseViewHolder, item: GoodsInfoVo?) {

        helper.addOnClickListener(R.id.infoDeleteTv);
        helper.setVisible(R.id.infoDeleteTv, helper.adapterPosition != 0);
    }
}