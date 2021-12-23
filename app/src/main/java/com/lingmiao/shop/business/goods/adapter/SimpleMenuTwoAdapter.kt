package com.lingmiao.shop.business.goods.adapter

import com.chad.library.adapter.base.BaseItemDraggableAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.lingmiao.shop.R
import com.lingmiao.shop.business.goods.api.bean.ShopGroupVO
import com.lingmiao.shop.util.GlideUtils

class SimpleMenuTwoAdapter :
    BaseItemDraggableAdapter<ShopGroupVO, BaseViewHolder>(
        R.layout.goods_adapter_title,
        null
    ) {


    override fun convert(helper: BaseViewHolder, item: ShopGroupVO?) {
        item?.apply {


            //菜单名称
            helper.setText(R.id.menuName, shopCatName)

            //删除
            helper.setGone(R.id.deleteIv, isdeleted)
            helper.addOnClickListener(R.id.deleteIv)


        }
    }


}