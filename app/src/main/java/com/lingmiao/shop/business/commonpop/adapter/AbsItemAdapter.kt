package com.lingmiao.shop.business.commonpop.adapter

import android.widget.TextView
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.lingmiao.shop.R
import com.lingmiao.shop.business.commonpop.bean.ItemData

/**
 * Author : Elson
 * Date   : 2020/7/16
 * Desc   : 商品分类
 */
abstract class AbsItemAdapter<T : ItemData>(layoutId : Int) : BaseQuickAdapter<T, BaseViewHolder>(layoutId) {

//    private var selectedCategoryId: String? = null
//
//    fun setSelectedItem(categoryId: String?) {
//        this.selectedCategoryId = categoryId
//    }

//    override fun convert(helper: BaseViewHolder, item: T?) {
//        if (item == null) return
//
//        helper.getView<TextView>(R.id.titleTv).apply {
//            text = item.getName()
//            isSelected = (item.getValue() == selectedCategoryId)
//        }
//    }

}