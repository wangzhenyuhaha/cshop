package com.lingmiao.shop.business.goods.adapter

import com.chad.library.adapter.base.BaseMultiItemQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.lingmiao.shop.R
import com.lingmiao.shop.business.goods.api.bean.CategoryVO

class GoodsPublishTypeAdapter(mList : List<CategoryVO>?) : BaseMultiItemQuickAdapter<CategoryVO, BaseViewHolder>(mList) {

    private var checkedDrawableRes: Int;
    private var uncheckDrawable : Int;

    companion object {
        val TYPE_LEVEL_0 = 0
        val TYPE_LEVEL_1 = 1
    }

    init {
        addItemType(TYPE_LEVEL_0, R.layout.goods_adapter_type_title);
        addItemType(TYPE_LEVEL_1, R.layout.goods_adapter_type_item);

        checkedDrawableRes = R.mipmap.goods_blue_arrow_down;
        uncheckDrawable = R.mipmap.login_right_arrow;
    }

    override fun convert(helper: BaseViewHolder, item: CategoryVO?) {

        item?.apply {
            if(item.itemType == TYPE_LEVEL_0) {
                pPosition = helper.adapterPosition;
                val cItem : CategoryVO = item;
                helper.setText(R.id.typeTitleTv, cItem?.name);
                helper.setImageResource(R.id.expandIv, if(cItem.isExpanded) checkedDrawableRes else uncheckDrawable);

                helper.addOnClickListener(R.id.titleLL);
                helper.addOnClickListener(R.id.expandIv);
            } else if(item.itemType == TYPE_LEVEL_1) {
                val cItem : CategoryVO = item;
                cItem.pPosition = pPosition;
                helper.setText(R.id.itemTv, cItem?.name);
                helper.setImageResource(R.id.checkIv, if(cItem.isExpanded) checkedDrawableRes else uncheckDrawable);

                helper.addOnClickListener(R.id.itemLL);
                helper.addOnClickListener(R.id.checkIv);

            }

        }

    }


}