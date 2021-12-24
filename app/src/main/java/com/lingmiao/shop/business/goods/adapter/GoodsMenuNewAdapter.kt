package com.lingmiao.shop.business.goods.adapter

import android.widget.TextView
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.lingmiao.shop.R
import com.lingmiao.shop.business.goods.api.bean.GoodsVO
import com.lingmiao.shop.business.goods.config.GoodsConfig
import com.lingmiao.shop.util.GlideUtils
import com.lingmiao.shop.util.formatDouble

class GoodsMenuNewAdapter :
    BaseQuickAdapter<GoodsVO, BaseViewHolder>(R.layout.goods_adapter_goods_menu) {

    override fun convert(helper: BaseViewHolder, item: GoodsVO?) {
        item?.apply {

            //商品图片
            GlideUtils.setImageUrl1(helper.getView(R.id.goodsIv), thumbnail)

            //商品名
            helper.getView<TextView>(R.id.goodsNameTv).apply {
                setCompoundDrawablesWithIntrinsicBounds(
                    if (item.goodsType == GoodsConfig.GOODS_TYPE_VIRTUAL) R.mipmap.ic_virtual else 0,
                    0,
                    0,
                    0
                )
                text = goodsName
            }

            //库存
            helper.setText(
                R.id.goodsQuantityTv,
                if (isEvent)
                    mContext.getString(R.string.goods_home_event_quantity, eventQuantity.toString())
                else
                    mContext.getString(R.string.goods_home_quantity, quantity)
            )

            //价格
            helper.setText(
                R.id.goodsPriceTv,
                if (isEvent)
                    mContext.getString(
                        R.string.goods_home_event_price,
                        eventPrice.toFloat().toString()
                    ) else
                    mContext.getString(R.string.goods_home_price, price.toFloat().toString())
            )

            helper.setText(
                R.id.goods_adapter_goods_menu_marketEnableTextView,
                if (marketEnable == 1) {
                    "已上架"
                } else {
                    ""
                }
            )

            helper.setGone(R.id.goodsEventQuantityTv, false)
            helper.setGone(R.id.goodsEventPriceTv, false)

            helper.addOnClickListener(R.id.menuIv)
            helper.addOnClickListener(R.id.goodsPriceTv)
        }
    }

    fun shiftChecked(position: Int) {
        data[position]?.isChecked = !(data[position]?.isChecked ?: false)
        notifyItemChanged(position)
    }
}