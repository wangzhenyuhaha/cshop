package com.lingmiao.shop.business.tuan.adapter

import android.widget.BaseAdapter
import android.widget.ImageView
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.lingmiao.shop.R
import com.lingmiao.shop.business.tuan.bean.GoodsVo

/**
Create Date : 2021/1/162:01 PM
Auther      : Fox
Desc        :
 **/
class OrderGoodsAdapter : BaseQuickAdapter<GoodsVo, BaseViewHolder>(R.layout.tuan_adapter_order_goods) {
    /**
     * Implement this method and use the helper to adapt the view to the given item.
     *
     * @param helper A fully initialized helper.
     * @param item   The item that needs to be displayed.
     */
    override fun convert(helper: BaseViewHolder, item: GoodsVo?) {
        // 商品图
        helper.getView<ImageView>(R.id.orderGoodsIv)

        // 名称
        helper.setText(R.id.orderGoodsNameTv, "测试")

        // 价格
        helper.setText(R.id.orderGoodsPriceTv, "￥77")

        // 规格
        helper.setText(R.id.orderGoodsSpecTv, "颜色：白色，尺寸：L")

        // 数量
        helper.setText(R.id.orderGoodsCountTv, " x 1")
    }

}