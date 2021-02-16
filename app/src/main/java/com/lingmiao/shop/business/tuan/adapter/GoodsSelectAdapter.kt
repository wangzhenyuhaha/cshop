package com.lingmiao.shop.business.tuan.adapter

import android.widget.CompoundButton
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.IdRes
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.lingmiao.shop.R
import com.lingmiao.shop.business.goods.api.bean.GoodsVO
import com.lingmiao.shop.business.goods.config.GoodsConfig
import com.lingmiao.shop.business.tools.adapter.setOnCheckedChangeListener
import com.lingmiao.shop.util.GlideUtils
import com.lingmiao.shop.util.formatDouble
import com.james.common.utils.exts.check

class GoodsSelectAdapter : BaseQuickAdapter<GoodsVO, BaseViewHolder>(R.layout.tuan_adapter_goods_select) {

    override fun convert(helper: BaseViewHolder, item: GoodsVO?) {
        item?.apply {

            GlideUtils.setImageUrl1(helper.getView<ImageView>(R.id.tuanGoodsIv), thumbnail)

            helper.getView<TextView>(R.id.tuanGoodsNameTv).setCompoundDrawablesWithIntrinsicBounds(if(item?.goodsType == GoodsConfig.GOODS_TYPE_VIRTUAL) R.mipmap.ic_virtual else 0, 0, 0, 0)
            helper.setText(R.id.tuanGoodsNameTv, goodsName)

            helper.setText(
                R.id.tuanGoodsQuantityTv,
                mContext.getString(R.string.goods_home_quantity, quantity)
            )

            helper.setText(R.id.tuanGoodsSalesTv, mContext.getString(R.string.goods_home_sales, buyCount.check("0")))

            helper.setText(R.id.tuanGoodsPriceTv, formatDouble(price))

            setOnCheckedChangeListener(helper.getView(R.id.tuanMenuIv), isChecked ?: false) { buttonView: CompoundButton?, isChecked: Boolean ->
                item?.isChecked = isChecked;
            }

            helper.setImageResource(R.id.tuanGoodsStatusIv, getGoodsStatusIcon(item))
            helper.setGone(R.id.tuanGoodsStatusIv, true)
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