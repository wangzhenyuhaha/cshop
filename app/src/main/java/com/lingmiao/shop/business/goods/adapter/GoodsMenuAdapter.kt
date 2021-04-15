package com.lingmiao.shop.business.goods.adapter

import android.widget.ImageView
import android.widget.TextView
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.lingmiao.shop.R
import com.lingmiao.shop.business.goods.api.bean.GoodsVO
import com.lingmiao.shop.business.goods.config.GoodsConfig
import com.lingmiao.shop.util.GlideUtils
import com.lingmiao.shop.util.formatDouble

class GoodsMenuAdapter : BaseQuickAdapter<GoodsVO, BaseViewHolder>(R.layout.goods_adapter_goods_menu) {

    override fun convert(helper: BaseViewHolder, item: GoodsVO?) {
        item?.apply {

            GlideUtils.setImageUrl1(helper.getView<ImageView>(R.id.goodsIv), thumbnail)

            helper.getView<TextView>(R.id.goodsNameTv).setCompoundDrawablesWithIntrinsicBounds(if(item?.goodsType == GoodsConfig.GOODS_TYPE_VIRTUAL) R.mipmap.ic_virtual else 0, 0, 0, 0)
            helper.setText(R.id.goodsNameTv, goodsName)

            helper.setText(
                R.id.goodsQuantityTv,
                mContext.getString(R.string.goods_home_spec, quantity)
            )

            helper.setText(R.id.goodsPriceTv, formatDouble(price))

//            setOnCheckedChangeListener(helper.getView(R.id.menuIv), isChecked ?: false) { buttonView: CompoundButton?, isChecked: Boolean ->
//                item?.isChecked = isChecked;
//            }

            // helper.setChecked(R.id.menuIv, item?.isChecked?:false);
            helper.addOnClickListener(R.id.menuIv);
        }
    }

    private fun getGoodsStatusIcon(goodsVO: GoodsVO) : Int {
        val drawableRes = when (goodsVO.goodsStatusMix) {
            GoodsVO.STATUS_MIX_0 -> R.mipmap.goods_status_0
            GoodsVO.STATUS_MIX_1 -> R.mipmap.goods_status_1
            GoodsVO.STATUS_MIX_2 -> R.mipmap.goods_status_2
            GoodsVO.STATUS_MIX_3 -> R.mipmap.goods_status_3
            else -> 0
        }
        return drawableRes;
    }


    fun shiftChecked(position: Int) {
        data[position]?.isChecked = !(data[position]?.isChecked ?: false);
        notifyItemChanged(position)
    }
}