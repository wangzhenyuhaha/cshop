package com.lingmiao.shop.business.tuan.adapter

import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.lingmiao.shop.R
import com.lingmiao.shop.business.tuan.bean.GoodsVo

/**
Create Date : 2021/1/163:25 PM
Auther      : Fox
Desc        :
 **/
class OrderGoodsDetailAdapter : BaseQuickAdapter<GoodsVo, BaseViewHolder>(R.layout.tuan_adapter_order_goods_detail){
    /**
     * Implement this method and use the helper to adapt the view to the given item.
     *
     * @param helper A fully initialized helper.
     * @param item   The item that needs to be displayed.
     */
    override fun convert(helper: BaseViewHolder, item: GoodsVo?) {

    }
}