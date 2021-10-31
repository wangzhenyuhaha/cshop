package com.lingmiao.shop.business.goods.adapter

import android.widget.TextView
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.lingmiao.shop.R
import com.lingmiao.shop.business.goods.api.bean.ShopGroupVO

/**
 * Author : Elson
 * Date   : 2020/7/16
 * Desc   : 商品分类
 */
class GroupAdapter :
    BaseQuickAdapter<ShopGroupVO, BaseViewHolder>(R.layout.goods_adapter_category) {

    private var selectedGroupId: String? = null

    fun setGroupId(categoryId: String?) {
        this.selectedGroupId = categoryId
    }

    override fun convert(helper: BaseViewHolder, item: ShopGroupVO?) {
        if (item == null) return

        helper.getView<TextView>(R.id.titleTv).apply {
            text = item.shopCatName
            isSelected = (item.shopCatId == selectedGroupId)
        }

        val temp = "(${item.goods_num.toString()})"

        helper.getView<TextView>(R.id.titleNum).apply {
            text = temp
        }
    }

    fun clearData() {
        mData.clear()
        notifyDataSetChanged()
    }


}