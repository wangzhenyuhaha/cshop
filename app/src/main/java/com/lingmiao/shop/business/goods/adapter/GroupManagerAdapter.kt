package com.lingmiao.shop.business.goods.adapter

import android.widget.TextView
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.lingmiao.shop.R
import com.lingmiao.shop.business.goods.api.bean.ShopGroupVO
import com.lingmiao.shop.util.GlideUtils
import com.james.common.utils.exts.isNotBlank

/**
 * Author : Elson
 * Date   : 2020/7/18
 * Desc   : 商品分组管理列表
 */
class GroupManagerAdapter(layoutId:Int) :
    BaseQuickAdapter<ShopGroupVO, BaseViewHolder>(layoutId) {


    override fun convert(helper: BaseViewHolder, item: ShopGroupVO?) {
        item?.apply {

            GlideUtils.setImageUrl1(helper.getView(R.id.groupIconIv), shopCatPic)
            helper.setText(R.id.groupNameTv, shopCatName)
            helper.setText(R.id.groupDescTv, getShopCatDesc(item))
            helper.setText(R.id.groupOrderTv, "排序：${sort}")
            helper.getView<TextView>(R.id.showTagTv).apply {
                isSelected = isGroupShow()
                text = if (isGroupShow()) "显示" else "不显示"
            }
            helper.addOnClickListener(R.id.groupEditTv)
            helper.addOnClickListener(R.id.groupDeleteTv)
            helper.setGone(R.id.marginView, helper.adapterPosition == data.size - 1)
        }
    }

    private fun getShopCatDesc(item: ShopGroupVO): String {
        if (item.shopCatDesc.isNotBlank()) {
            return "(${item.shopCatDesc})"
        }
        return ""
    }
}