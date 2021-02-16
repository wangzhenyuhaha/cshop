package com.lingmiao.shop.business.tuan.adapter

import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.lingmiao.shop.R
import com.lingmiao.shop.business.tuan.bean.GoodsSpecVo
import com.lingmiao.shop.business.tuan.bean.GoodsVo

class GoodsSpecItemAdapter : BaseQuickAdapter<GoodsSpecVo, BaseViewHolder>(R.layout.tuan_adapter_goods_spec) {
    override fun convert(helper: BaseViewHolder, item: GoodsSpecVo?) {
        helper.addOnClickListener(R.id.tv_tuan_goods_spec_option);

        helper.setText(R.id.tv_tuan_goods_spec_sku, "规格1");
        helper.setText(R.id.tv_tuan_goods_spec_sale_price, "21.22");
        helper.setText(R.id.tv_tuan_goods_spec_price, "23.22");
        helper.setText(R.id.tv_tuan_goods_spec_quantity, "1");
        helper.setText(R.id.tv_tuan_goods_spec_count, "2");
    }
}