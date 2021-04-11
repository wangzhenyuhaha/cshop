package com.lingmiao.shop.business.order.view

import android.content.Context
import android.util.AttributeSet
import com.lingmiao.shop.business.commonpop.view.RecycleViewLayout
import com.lingmiao.shop.business.order.adapter.GoodsItemAdapter
import com.lingmiao.shop.business.order.bean.Sku

/**
Create Date : 2021/3/125:03 AM
Auther      : Fox
Desc        :
 **/
class GoodsItemRvLayout
@JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
)

    :
    RecycleViewLayout<Sku, GoodsItemAdapter>(context, attrs, defStyleAttr) {
    override fun getAdapter(): GoodsItemAdapter {
        return GoodsItemAdapter().apply {

        };
    }

}