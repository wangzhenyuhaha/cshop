package com.lingmiao.shop.business.goods.adapter

import com.chad.library.adapter.base.BaseMultiItemQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.lingmiao.shop.R
import com.lingmiao.shop.business.goods.api.bean.GoodsPublishTypeVo
import com.lingmiao.shop.business.tools.bean.RegionVo

class GoodsPublishTypeAdapter(mList : List<GoodsPublishTypeVo>?) : BaseMultiItemQuickAdapter<GoodsPublishTypeVo, BaseViewHolder>(mList) {


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

    override fun convert(helper: BaseViewHolder, item: GoodsPublishTypeVo?) {

        item?.apply {
            if(item.itemType == TYPE_LEVEL_0) {
                pPosition = helper.adapterPosition;
                val cItem : GoodsPublishTypeVo = item;
                helper.setText(R.id.typeTitleTv, cItem?.name);
                helper.setImageResource(R.id.expandIv, if(cItem.isExpanded) checkedDrawableRes else uncheckDrawable);

                helper.addOnClickListener(R.id.titleLL);
                helper.addOnClickListener(R.id.expandIv);
            } else if(item.itemType == TYPE_LEVEL_1) {
                val cItem : GoodsPublishTypeVo = item;
                cItem.pPosition = pPosition;
                helper.setText(R.id.itemTv, cItem?.name);
                helper.setImageResource(R.id.checkIv, if(cItem.isExpanded) checkedDrawableRes else uncheckDrawable);

                helper.addOnClickListener(R.id.itemLL);
                helper.addOnClickListener(R.id.checkIv);

            }

        }

    }


}