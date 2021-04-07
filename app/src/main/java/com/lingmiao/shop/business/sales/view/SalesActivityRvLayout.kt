package com.lingmiao.shop.business.sales.view

import android.content.Context
import android.util.AttributeSet
import com.lingmiao.shop.business.commonpop.view.RecycleViewLayout
import com.lingmiao.shop.business.sales.adapter.SalesActivityItemAdapter
import com.lingmiao.shop.business.sales.bean.SalesActivityItemVo

/**
Create Date : 2021/3/125:03 AM
Auther      : Fox
Desc        :
 **/
class SalesActivityRvLayout
@JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
)

    :
    RecycleViewLayout<SalesActivityItemVo, SalesActivityItemAdapter>(context, attrs, defStyleAttr) {
    override fun getAdapter(): SalesActivityItemAdapter {
        return SalesActivityItemAdapter().apply {

        };
    }

}