package com.lingmiao.shop.business.goods.adapter

import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.lingmiao.shop.R
import com.lingmiao.shop.business.goods.api.bean.ShopGroupVO

/**
Create Date : 2021/5/304:13 PM
Auther      : Fox
Desc        :
 **/
class UserChildrenAdapter(items : List<ShopGroupVO>?) : BaseQuickAdapter<ShopGroupVO, BaseViewHolder>(R.layout.goods_adapter_user_children, items) {
    /**
     * Implement this method and use the helper to adapt the view to the given item.
     *
     * @param helper A fully initialized helper.
     * @param item   The item that needs to be displayed.
     */
    override fun convert(helper: BaseViewHolder, item: ShopGroupVO?) {
        helper.setText(R.id.cateChildrenTitleTv, item?.shopCatName);
        helper.addOnClickListener(R.id.cateChildrenMoreIv);
    }
}