package com.lingmiao.shop.business.sales.adapter

import android.widget.TextView
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.lingmiao.shop.R
import com.lingmiao.shop.business.goods.api.bean.ShopGroupVO
import com.lingmiao.shop.util.GlideUtils
import com.james.common.utils.exts.isNotBlank
import com.lingmiao.shop.business.sales.bean.*
import com.lingmiao.shop.business.sales.view.OrderItemRvLayout
import com.lingmiao.shop.business.sales.view.SalesActivityRvLayout

/**
 * Author : Elson
 * Date   : 2020/7/18
 * Desc   : 商品分组管理列表
 */
class UserOrderAdapter() : BaseQuickAdapter<UserOrderVo, BaseViewHolder>(R.layout.sales_adapter_user_order) {

    override fun convert(helper: BaseViewHolder, item: UserOrderVo?) {
        item?.apply {

            helper.setText(R.id.userOrderNoTv, "订单编号：1111");
            helper.setText(R.id.userOrderTimeTv, "订单编号：1111");
            helper.setText(R.id.userOrderStatusTv, "进行中");
            helper.setText(R.id.userOrderPriceTv, "¥：88.00");
            helper.getView<OrderItemRvLayout>(R.id.userOrderItemC).addItems(getItem());
            helper.addOnClickListener(R.id.salesEditTv);
        }
    }

    fun getItem() : MutableList<OrderItemVo> {
        val list : MutableList<OrderItemVo> = mutableListOf();
        list.add(OrderItemVo());
        list.add(OrderItemVo());
        list.add(OrderItemVo());
        return list;
    }

}