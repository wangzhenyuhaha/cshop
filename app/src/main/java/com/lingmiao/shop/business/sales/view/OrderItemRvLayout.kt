package com.lingmiao.shop.business.sales.view

import android.content.Context
import android.util.AttributeSet
import com.lingmiao.shop.business.commonpop.view.RecycleViewLayout
import com.lingmiao.shop.business.sales.adapter.OrderItemAdapter
import com.lingmiao.shop.business.sales.bean.OrderItemVo

/**
Create Date : 2021/3/125:03 AM
Auther      : Fox
Desc        :
 **/
class OrderItemRvLayout
@JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
)

    :
    RecycleViewLayout<OrderItemVo, OrderItemAdapter>(context, attrs, defStyleAttr) {
    override fun getAdapter(): OrderItemAdapter {
        return OrderItemAdapter().apply {

        };
    }

}