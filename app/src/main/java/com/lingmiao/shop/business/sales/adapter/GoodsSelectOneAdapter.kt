package com.lingmiao.shop.business.sales.adapter

import android.util.Log
import android.widget.TextView
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.lingmiao.shop.R
import com.lingmiao.shop.business.goods.api.bean.GoodsVO
import com.lingmiao.shop.business.goods.config.GoodsConfig
import com.lingmiao.shop.util.GlideUtils
import com.lingmiao.shop.util.formatDouble

class GoodsSelectOneAdapter :
    BaseQuickAdapter<GoodsVO, BaseViewHolder>(R.layout.goods_adapter_goods_check) {

    private var goodsID: String? = null

    fun setItem(goods_id: String?) {
        this.goodsID = goods_id
    }

    fun getItem(item: String?): String? {
        return this.goodsID
    }

    override fun convert(helper: BaseViewHolder, item: GoodsVO?) {
        item?.apply {

            GlideUtils.setImageUrl1(helper.getView(R.id.goodsIv), thumbnail)

            helper.getView<TextView>(R.id.goodsNameTv).setCompoundDrawablesWithIntrinsicBounds(
                if (item.goodsType == GoodsConfig.GOODS_TYPE_VIRTUAL) R.mipmap.ic_virtual else 0,
                0,
                0,
                0
            )
            helper.setText(R.id.goodsNameTv, goodsName)

            helper.setText(
                R.id.goodsQuantityTv,
                ""
            )


            helper.setText(R.id.goodsPriceTv, formatDouble(price))

            if (item.goodsId == goodsID) {
                helper.setChecked(R.id.menuIv, true)
            } else {
                helper.setChecked(R.id.menuIv, false)
            }

            helper.addOnClickListener(R.id.menuIv)
        }
    }


    fun shiftChecked(position: Int) {
        data[position]?.isChecked = !(data[position]?.isChecked ?: false)
        notifyItemChanged(position)
    }
}