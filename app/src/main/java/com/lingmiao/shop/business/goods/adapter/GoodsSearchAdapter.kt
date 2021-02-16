package com.lingmiao.shop.business.goods.adapter

import android.annotation.SuppressLint
import com.chad.library.adapter.base.BaseViewHolder
import com.lingmiao.shop.R
import com.lingmiao.shop.business.goods.api.bean.GoodsVO
import com.lingmiao.shop.business.goods.pop.GoodsMenuPop

/**
 * Author : liuqi
 * Date   : 2020/8/8
 * Desc   : 商品搜索页
 */
class GoodsSearchAdapter : GoodsStatusAdapter(R.layout.goods_adapter_goods_search) {

    @SuppressLint("StringFormatMatches")
    override fun convert(helper: BaseViewHolder, goodsVO: GoodsVO?) {
        super.convert(helper, goodsVO)
        goodsVO?.apply {
            showGoodsStatusIcon(helper, this)
        }
    }

    private fun showGoodsStatusIcon(helper: BaseViewHolder, goodsVO: GoodsVO) {
        val drawableRes = when (goodsVO.goodsStatusMix) {
            GoodsVO.STATUS_MIX_0 -> R.mipmap.goods_status_0
            GoodsVO.STATUS_MIX_1 -> R.mipmap.goods_status_1
            GoodsVO.STATUS_MIX_2 -> R.mipmap.goods_status_2
            GoodsVO.STATUS_MIX_3 -> R.mipmap.goods_status_3
            else -> (GoodsMenuPop.TYPE_EDIT)
        }
        if (drawableRes != GoodsMenuPop.TYPE_EDIT) {
            helper.setGone(R.id.searchGoodsStatusIv, drawableRes != 0)
            helper.setImageResource(R.id.searchGoodsStatusIv, drawableRes)
        } else {
            helper.setGone(R.id.searchGoodsStatusIv, false)
        }
    }
}