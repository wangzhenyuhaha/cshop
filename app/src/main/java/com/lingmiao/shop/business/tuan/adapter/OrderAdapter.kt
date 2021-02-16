package com.lingmiao.shop.business.tuan.adapter

import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.lingmiao.shop.R
import com.lingmiao.shop.business.tuan.bean.OrderVo
import com.lingmiao.shop.business.tuan.view.OrderGoodsLayout

class OrderAdapter : BaseQuickAdapter<OrderVo, BaseViewHolder>(R.layout.tuan_adapter_order) {

    override fun convert(helper: BaseViewHolder, item: OrderVo?) {

        setStatusButton(helper, item);

    }

    fun setStatusButton(helper: BaseViewHolder, item: OrderVo?) {
        helper.addOnClickListener(R.id.orderRefuseTv);
        helper.addOnClickListener(R.id.orderConfirmTv);
        helper.addOnClickListener(R.id.orderPrintTv);
        helper.addOnClickListener(R.id.orderDeliveryTv);
        helper.addOnClickListener(R.id.orderExpressTv);
        helper.addOnClickListener(R.id.orderDeleteTv);

        helper.setGone(R.id.orderRefuseTv, false)
        helper.setGone(R.id.orderPrintTv, false);
        helper.setGone(R.id.orderConfirmTv, false);
        helper.setGone(R.id.orderDeliveryTv, false);
        helper.setGone(R.id.orderExpressTv, false);
        helper.setGone(R.id.orderDeleteTv, false);

        when(helper.adapterPosition) {
            0 -> {
                //待确认
                // 拒绝
                helper.setGone(R.id.orderRefuseTv, true);
                // 确认
                helper.setGone(R.id.orderConfirmTv, true);
            }
            1 -> {
                // 待发货
                // 打印
                helper.setGone(R.id.orderPrintTv, true);
                // 发货
                helper.setGone(R.id.orderDeliveryTv, true);
            }
            2 -> {
                // 待收货
                // 打印
                helper.setGone(R.id.orderPrintTv, true);
                // 查看物流
                helper.setGone(R.id.orderExpressTv, true)
            }
            3 -> {
                // 打印
                helper.setGone(R.id.orderPrintTv, true);
                // 查看物流
                helper.setGone(R.id.orderExpressTv, true)
            }
            4 -> {
                // 取消
                // 打印
                helper.setGone(R.id.orderPrintTv, true);
                // 删除
                helper.setGone(R.id.orderDeleteTv, true);
            }
        }
        helper.getView<OrderGoodsLayout>(R.id.orderGoodsLayout).addItems(item?.goods);
    }
}