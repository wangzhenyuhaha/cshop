package com.lingmiao.shop.business.tools.adapter

import com.chad.library.adapter.base.BaseMultiItemQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.chad.library.adapter.base.entity.MultiItemEntity
import com.lingmiao.shop.R
import com.lingmiao.shop.business.tools.bean.RegionVo

class AreasAdapter(mList : List<MultiItemEntity>) : BaseMultiItemQuickAdapter<MultiItemEntity, BaseViewHolder>(mList) {

    /**
     * 所处的省position
     */
    private var pPosition : Int? = -1;

    /**
     * 所处的市position
     */
    private var cPosition : Int? = -1;

    private var checkedDrawableRes: Int;
    private var uncheckDrawable : Int;

    companion object {
        val TYPE_LEVEL_0 = 0
        val TYPE_LEVEL_1 = 1
        val TYPE_AREA = 2
    }

    init {
        addItemType(TYPE_LEVEL_0, R.layout.tools_adapter_province);
        addItemType(TYPE_LEVEL_1, R.layout.tools_adapter_city);
        addItemType(TYPE_AREA, R.layout.tools_adapter_area);

        checkedDrawableRes = R.mipmap.tools_collapse;
        uncheckDrawable = R.mipmap.tools_expand;
    }

    override fun convert(helper: BaseViewHolder, item: MultiItemEntity?) {

        item?.apply {
            if(item.itemType == TYPE_LEVEL_0) {
                pPosition = helper.adapterPosition;
                val cItem : RegionVo = item as RegionVo;
                helper.setText(R.id.tv_province_name, cItem?.localName);
                helper.setImageResource(R.id.iv_province_expand, if(cItem.isExpanded) checkedDrawableRes else uncheckDrawable);

                helper.addOnClickListener(R.id.ll_province);
                helper.addOnClickListener(R.id.iv_province_expand);
                helper.addOnClickListener(R.id.iv_province_check);

                helper.setChecked(R.id.iv_province_check, cItem.isCheck);

//                if(cItem?.isCheck) {
//                    expand(pPosition!!);
//                } else {
//                    collapse(pPosition!!);
//                }
            } else if(item.itemType == TYPE_LEVEL_1) {
                cPosition = helper.adapterPosition;
                val cItem : RegionVo = item as RegionVo;
                cItem.pPosition = pPosition;
                helper.setText(R.id.tv_city_name, cItem?.localName);
                helper.setImageResource(R.id.iv_city_expand, if(cItem.isExpanded) checkedDrawableRes else uncheckDrawable);

                helper.addOnClickListener(R.id.ll_city);
                helper.addOnClickListener(R.id.iv_city_expand);
                helper.addOnClickListener(R.id.iv_city_check);

                helper.setChecked(R.id.iv_city_check, cItem.isCheck);

//                if(cItem?.isCheck) {
//                    expand(pPosition!!);
//                } else {
//                    collapse(pPosition!!);
//                }
            } else if(item.itemType == TYPE_AREA) {
                val cItem : RegionVo = item as RegionVo;
                cItem.pPosition = pPosition;
                cItem.cPosition = cPosition;
                helper.setText(R.id.tv_area_name, cItem?.localName);

                helper.addOnClickListener(R.id.ll_area);
                helper.addOnClickListener(R.id.iv_area_check);

                helper.setChecked(R.id.iv_area_check, cItem.isCheck);

//                if(cItem?.isCheck) {
//                    expand(pPosition!!);
//                } else {
//                    collapse(pPosition!!);
//                }
            }

        }

    }


}