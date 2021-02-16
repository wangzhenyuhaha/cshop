package com.lingmiao.shop.business.tuan.adapter

import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.lingmiao.shop.R
import com.lingmiao.shop.business.tuan.bean.GoodsSpecVo
import com.lingmiao.shop.business.tuan.bean.GoodsVo
import com.lingmiao.shop.business.tuan.view.GoodsSpecContainerLayout

class GoodsAdapter : BaseQuickAdapter<GoodsVo, BaseViewHolder>(R.layout.tuan_adapter_goods) {

    override fun convert(helper: BaseViewHolder, item: GoodsVo?) {
        helper.setText(R.id.tv_goods_name, "商品名称");
        helper.addOnClickListener(R.id.tv_goods_delete);
        helper.getView<GoodsSpecContainerLayout>(R.id.gs_layout).apply {
            addItems(getItems())
            deleteListener = { goodsSpecVo, position ->
                if(getSize() == 1) {
                    remove(position);
                } else {
                    removeItem(position)
                }
            }
        }.addItems(getItems());
    }

    fun getItems() : MutableList<GoodsSpecVo> {
        var list = arrayListOf<GoodsSpecVo>();
        list.add(GoodsSpecVo());
        list.add(GoodsSpecVo());
        return list;
    }
}