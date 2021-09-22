package com.lingmiao.shop.business.main.adapter

import com.amap.api.services.core.PoiItem
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.lingmiao.shop.R

/**
Create Date : 2021/7/273:16 下午
Auther      : Fox
Desc        :
 **/
class AddressAdapter : BaseQuickAdapter<PoiItem, BaseViewHolder>(R.layout.main_adapter_shop_address) {
    var mCheckedPosition = -1;

    fun setCheckPosition(position : Int) {
        mCheckedPosition = position;
        notifyDataSetChanged();
    }
    /**
     * Implement this method and use the helper to adapt the view to the given item.
     *
     * @param helper A fully initialized helper.
     * @param item   The item that needs to be displayed.
     */
    override fun convert(helper: BaseViewHolder, item: PoiItem?) {
        helper.setText(R.id.tvShopAddressTitle, item?.title);
        helper.setText(R.id.tvShopAddressDesc, item?.adName + item?.snippet);
//        helper.setText(R.id.tvShopAddressDesc, );
//        helper.setChecked(R.id.ivShopAddressSelected, mCheckedPosition == helper.adapterPosition)
        helper.setImageResource(R.id.ivShopAddressSelected, if(mCheckedPosition == helper.adapterPosition) R.mipmap.goods_spec_checked else R.mipmap.goods_spec_uncheck)//selector_goods_delivery_child_checkbox
    }
}