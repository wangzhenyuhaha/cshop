package com.lingmiao.shop.business.goods.adapter

import android.widget.CompoundButton
import com.chad.library.adapter.base.BaseItemDraggableAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.james.common.utils.exts.isNotBlank
import com.lingmiao.shop.R
import com.lingmiao.shop.business.goods.api.bean.ShopGroupVO
import com.lingmiao.shop.business.tools.adapter.setOnCheckedChangeListener
import com.lingmiao.shop.util.GlideUtils


class SimpleMenuAdapter() :
    BaseItemDraggableAdapter<ShopGroupVO, BaseViewHolder>(R.layout.goods_adapter_simple_menu, null) {

    private var isBatchEditModel: Boolean = false

    override fun convert(helper: BaseViewHolder, item: ShopGroupVO?) {
        item?.apply {
            //图标
            helper.setGone(R.id.menuIconIv, !shopCatPic.isNullOrEmpty())
            GlideUtils.setImageUrl1(helper.getView(R.id.menuIconIv), shopCatPic)
            helper.addOnClickListener(R.id.menuIconIv)
            //菜单名称
            helper.setText(R.id.menuName, shopCatName)
            helper.addOnClickListener(R.id.menuName)
          //删除
          helper.setGone(R.id .deleteIv,isdeleted)
            helper.addOnClickListener(R.id.deleteIv)




        }
    }




}