package com.lingmiao.shop.business.goods.view

import android.content.Context
import android.util.AttributeSet
import com.lingmiao.shop.business.commonpop.view.RecycleViewLayout
import com.lingmiao.shop.business.goods.adapter.CategoryAdapter
import com.lingmiao.shop.business.goods.api.bean.CategoryVO

/**
Create Date : 2021/3/125:03 AM
Auther      : Fox
Desc        :
 **/
class CatItemRvLayout
@JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
)

    :
    RecycleViewLayout<CategoryVO, CategoryAdapter>(context, attrs, defStyleAttr) {
    override fun getAdapter(): CategoryAdapter {
        return CategoryAdapter(null).apply {

        };
    }

}