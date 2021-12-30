package com.lingmiao.shop.business.goods.adapter

import android.widget.TextView
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

    private var selectGroupId: String? = null

    fun setGroupId(groupId: String?) {
        this.selectGroupId = groupId
    }

    override fun convert(helper: BaseViewHolder, item: ShopGroupVO?) {
        item?.apply {


            //菜单名称
            helper.getView<TextView>(R.id.menuName).apply {
                shopCatName.let {
                    text = it
                }
                isSelected = (item.catPath == selectGroupId)
            }

            //删除
            helper.setGone(R.id.deleteIv, if (isButton) false else isdeleted)
            helper.addOnClickListener(R.id.deleteIv)


        }
    }


}