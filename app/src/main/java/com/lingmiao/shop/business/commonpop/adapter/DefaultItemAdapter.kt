package com.lingmiao.shop.business.commonpop.adapter

import android.widget.TextView
import androidx.annotation.ColorInt
import androidx.core.content.ContextCompat
import com.chad.library.adapter.base.BaseViewHolder
import com.lingmiao.shop.MyApp
import com.lingmiao.shop.R
import com.lingmiao.shop.business.commonpop.bean.ItemData

/**
 * Author : Elson
 * Date   : 2020/7/16
 * Desc   : 商品分类
 */
class DefaultItemAdapter<T : ItemData> : AbsItemAdapter<T>(R.layout.pop_item_default) {

    private var selectedId: String? = null

    private var selectStr : String? = null

    fun setSelectedItem(categoryId: String?) {
        this.selectedId = categoryId
        notifyDataSetChanged();
    }

    fun setSelectedStr(str: String?) {
        this.selectStr = str
    }

    override fun convert(helper: BaseViewHolder, item: T?) {
        if (item == null) return

        helper.getView<TextView>(R.id.titleTv).apply {
            text = item.getIName()
            isSelected = (item.getIValue() == selectedId || item.getIName().equals(selectStr));
        }
        helper.setTextColor(R.id.titleTv, getColor(item));
    }

    fun getColor(item: T) : Int {
        return ContextCompat.getColor(MyApp.getInstance() ,if(isSelected(item)) R.color.primary else R.color.color_666666);
    }

    fun isSelected(item: T) : Boolean {
        return item.getIValue() == selectedId || item.getIName().equals(selectStr);
    }

}