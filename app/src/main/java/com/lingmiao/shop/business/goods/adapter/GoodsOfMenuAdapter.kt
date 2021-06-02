package com.lingmiao.shop.business.goods.adapter

import android.widget.CompoundButton
import android.widget.ImageView
import android.widget.TextView
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.james.common.utils.exts.check
import com.lingmiao.shop.R
import com.lingmiao.shop.business.goods.api.bean.GoodsVO
import com.lingmiao.shop.business.goods.config.GoodsConfig
import com.lingmiao.shop.business.tools.adapter.setOnCheckedChangeListener
import com.lingmiao.shop.util.GlideUtils
import com.lingmiao.shop.util.formatDouble

class GoodsOfMenuAdapter : BaseQuickAdapter<GoodsVO, BaseViewHolder>(R.layout.goods_adapter_goods_menu) {

    override fun convert(helper: BaseViewHolder, goodsVO: GoodsVO?) {
        goodsVO?.apply {

            val goodsIconIv = helper.getView<ImageView>(R.id.goodsIv)
            GlideUtils.setImageUrl1(goodsIconIv, thumbnail)
            helper.addOnClickListener(R.id.goodsIv);

            helper.getView<TextView>(R.id.goodsNameTv).setCompoundDrawablesWithIntrinsicBounds(if(goodsVO?.goodsType == GoodsConfig.GOODS_TYPE_VIRTUAL) R.mipmap.ic_virtual else 0, 0, 0, 0)
            helper.setText(R.id.goodsNameTv, goodsName)
            helper.setText(
                R.id.goodsQuantityTv,
                mContext.getString(R.string.goods_home_quantity, quantity)
            )
            val salesCount = mContext.getString(R.string.goods_home_sales, buyCount.check("0"))
            helper.setText(R.id.goodsSalesTv, salesCount)
            helper.setText(R.id.goodsPriceTv,  String.format("售价：%s", formatDouble(price)))
            helper.addOnClickListener(R.id.menuIv)

            if(helper.adapterPosition % 3 == 2) {
                helper.setText(R.id.goodsSourceIv, "自有")
            } else if(helper.adapterPosition %5 == 2) {
                helper.setText(R.id.goodsSourceIv, "库")
            }
            helper.setGone(R.id.cb_goods_check, false);
        }
    }

}