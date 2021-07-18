package com.lingmiao.shop.business.sales.adapter

import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.lingmiao.shop.R
import com.lingmiao.shop.MyApp
import com.lingmiao.shop.business.order.bean.OrderList
import com.lingmiao.shop.business.order.view.GoodsItemRvLayout
import com.lingmiao.shop.util.stampToDate

/**
 * Author : Elson
 * Date   : 2020/7/18
 * Desc   : 商品分组管理列表
 */
class UserOrderAdapter() : BaseQuickAdapter<OrderList, BaseViewHolder>(R.layout.sales_adapter_user_order) {

    override fun convert(helper: BaseViewHolder, item: OrderList?) {
        item?.apply {

            helper.setText(R.id.userOrderNoTv, "订单编号："+item.sn);
            helper.setText(R.id.userOrderTimeTv, "下单时间："+ stampToDate(item.createTime));
            helper.setText(R.id.userOrderStatusTv, item.orderStatusText);
            helper.setText(
                R.id.userOrderPriceTv,
                MyApp.getInstance().getString(R.string.order_money_new, item.orderAmount.toString())
            )

            helper.getView<GoodsItemRvLayout>(R.id.userOrderItemC).addItems(item.skuList);
            helper.addOnClickListener(R.id.tvSalesDelete);
        }
    }

}