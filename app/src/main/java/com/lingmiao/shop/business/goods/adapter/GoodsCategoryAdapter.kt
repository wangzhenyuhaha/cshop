package com.lingmiao.shop.business.goods.adapter

import android.widget.TextView
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.lingmiao.shop.R
import com.lingmiao.shop.business.goods.api.bean.CategoryVO

/**
 * Author : Elson
 * Date   : 2020/7/16
 * Desc   : 商品分类
 */
class GoodsCategoryAdapter :
    BaseQuickAdapter<CategoryVO, BaseViewHolder>(R.layout.goods_adapter_category) {

    private var selectedCategoryId: String? = null

    fun setSelectedItem(categoryId: String?) {
        this.selectedCategoryId = categoryId
    }

    override fun convert(helper: BaseViewHolder, item: CategoryVO?) {
        if (item == null) return

        helper.getView<TextView>(R.id.titleTv).apply {
            text = item.name
            isSelected = (item.categoryId == selectedCategoryId)
        }
    }

    fun clearData() {
        mData.clear()
        notifyDataSetChanged()
    }


}