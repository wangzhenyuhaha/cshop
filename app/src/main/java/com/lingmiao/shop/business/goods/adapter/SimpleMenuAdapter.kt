package com.lingmiao.shop.business.goods.adapter

import android.widget.TextView
import com.blankj.utilcode.util.ActivityUtils
import com.chad.library.adapter.base.BaseItemDraggableAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.lingmiao.shop.R
import com.lingmiao.shop.business.goods.api.bean.ShopGroupVO
import com.lingmiao.shop.util.GlideUtils


class SimpleMenuAdapter :
    BaseItemDraggableAdapter<ShopGroupVO, BaseViewHolder>(
        R.layout.goods_adapter_simple_menu,
        null
    ) {

    private var selectGroupId: String? = null

    fun setGroupId(groupId: String?) {
        this.selectGroupId = groupId
    }

    override fun convert(helper: BaseViewHolder, item: ShopGroupVO?) {
        item?.apply {

            //图标
            helper.setGone(R.id.menuIconIv, !shopCatPic.isNullOrEmpty())
            GlideUtils.setImageUrl1(helper.getView(R.id.menuIconIv), shopCatPic)

            //菜单名称
            helper.getView<TextView>(R.id.menuName).apply {
                "${shopCatName}(${goods_num})".let {
                    text = it
                }
                isSelected = (item.catPath == selectGroupId)
            }


            //删除
            helper.setGone(R.id.deleteIv, isdeleted)
            helper.addOnClickListener(R.id.deleteIv)

            if (item.catPath == selectGroupId){
                //选中了
                helper.setBackgroundRes(R.id.container, R.color.color_FFFFFF)
            }else{
                helper.setBackgroundRes(R.id.container, R.color.color_CACACA)
            }

        }
    }


}