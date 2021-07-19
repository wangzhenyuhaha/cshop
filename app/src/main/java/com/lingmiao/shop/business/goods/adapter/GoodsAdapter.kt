package com.lingmiao.shop.business.goods.adapter

import android.annotation.SuppressLint
import android.widget.CompoundButton
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.LayoutRes
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.lingmiao.shop.R
import com.lingmiao.shop.business.goods.api.bean.GoodsVO
import com.lingmiao.shop.business.goods.config.GoodsConfig
import com.lingmiao.shop.business.tools.adapter.setOnCheckedChangeListener
import com.lingmiao.shop.util.GlideUtils
import com.lingmiao.shop.util.formatDouble
import com.james.common.utils.exts.check
import com.lingmiao.shop.business.goods.fragment.GoodsNewFragment
import com.lingmiao.shop.business.goods.pop.GoodsMenuPop

/**
 * Author : Elson
 * Date   : 2020/7/11
 * Desc   : 商品状态列表(出售中、待审核、已下架)
 */
open class GoodsAdapter(
    val goodsStatus: Int,
    @LayoutRes layout: Int = R.layout.goods_adapter_goods
) :
    BaseQuickAdapter<GoodsVO, BaseViewHolder>(layout) {

    private var isBatchEditModel: Boolean = false;

    @SuppressLint("StringFormatMatches")
    override fun convert(helper: BaseViewHolder, goodsVO: GoodsVO?) {
        goodsVO?.apply {
            val goodsIconIv = helper.getView<ImageView>(R.id.goodsIv)
            GlideUtils.setImageUrl1(goodsIconIv, thumbnail)
            helper.addOnClickListener(R.id.goodsIv);

            helper.getView<TextView>(R.id.goodsNameTv).setCompoundDrawablesWithIntrinsicBounds(
                if (goodsVO?.goodsType == GoodsConfig.GOODS_TYPE_VIRTUAL) R.mipmap.ic_virtual else 0,
                0,
                0,
                0
            )
            helper.setText(R.id.goodsNameTv, goodsName)
            helper.setText(
                R.id.goodsQuantityTv,
                mContext.getString(R.string.goods_home_quantity, eventQuantity + enableQuantity)
            )
            // helper.setText(R.id.goodsSpecTv, "规格：50ml");
//            helper.setText(R.id.enableGoodsQuantityTv, mContext.getString(R.string.goods_home_enable_quantity, enableQuantity));
            val salesCount = mContext.getString(R.string.goods_home_sales, buyCount.check("0"))
            helper.setText(R.id.goodsSalesTv, salesCount)
            helper.setText(R.id.goodsPriceTv, String.format("售价：%s", formatDouble(price)))
            helper.addOnClickListener(R.id.menuIv)

            setOnCheckedChangeListener(
                helper.getView(R.id.cb_goods_check),
                isChecked ?: false
            ) { buttonView: CompoundButton?, isChecked: Boolean ->
                goodsVO?.isChecked = isChecked;
            }
            helper.setGone(R.id.cb_goods_check, isBatchEditModel);
            helper.setGone(
                R.id.menuIv, if (goodsStatus == GoodsNewFragment.GOODS_STATUE_WARNING) {
                    true
                } else {
                    !isBatchEditModel && goodsStatusMix != GoodsVO.STATUS_MIX_0
                }
            );

            helper.setGone(R.id.goodsSourceC, false);

            helper.setGone(R.id.goodsSoldOutTv, isSellOut());
            when (isAuth) {
                3 -> {
                    // 未通过
                    helper.setGone(R.id.goodsStatusTv, true)
                    helper.setText(R.id.goodsStatusTv, "审核未通过")
                    helper.setText(R.id.goodsAuthStatusTv, authMessage)
                    helper.setGone(R.id.goodsAuthStatusTv, true)
                }
                0 -> {
                    // 审核中
                    helper.setGone(R.id.goodsStatusTv, true);
                    helper.setText(R.id.goodsStatusTv, "审核中")
                    helper.setGone(R.id.goodsAuthStatusTv, false)
                }
                4 -> {
                    // 审核中
                    helper.setGone(R.id.goodsStatusTv, true);
                    helper.setText(R.id.goodsStatusTv, "审核中")
                    helper.setGone(R.id.goodsAuthStatusTv, false)

                    helper.setGone(R.id.goodsSourceC, true);
                    helper.setText(R.id.goodsSourceIv, "库")
                }
                else -> {
                    helper.setGone(R.id.goodsStatusTv, false);
                    helper.setGone(R.id.goodsAuthStatusTv, false);
                }
            }
            helper.setText(R.id.goodsStatusTv, goodsStatusText)

            if (goodsStatus == GoodsNewFragment.GOODS_STATUS_ALL) {
                showGoodsStatusIcon(helper, this)
            }
            //helper.setGone(R.id.goodsDiscountC, helper.adapterPosition % 3 == 1);
//            helper.setText(R.id.goodsOwnerTv, String.format("[来源：%s]", supplierName));
//            helper.setGone(R.id.goodsOwnerTv, supplierName?.length?:0 > 0);
        }

    }

    fun updateQuantity(quantity: String?, position: Int) {
        val goods = data[position]
        goods.quantity = quantity
        goods.goodsQuantityStatusMix = if (quantity?.toInt() ?: 0 > 0) 1 else 0;
        notifyItemChanged(position)
    }

    fun setBatchEditModel(flag: Boolean) {
        isBatchEditModel = flag;
        notifyDataSetChanged();
    }

    fun shiftChecked(position: Int) {
        data[position]?.isChecked = !(data[position]?.isChecked ?: false);
        notifyItemChanged(position)
    }

    private fun showGoodsStatusIcon(helper: BaseViewHolder, goodsVO: GoodsVO) {
        val drawableRes = when (goodsVO.goodsStatusMix) {
            GoodsVO.STATUS_MIX_0 -> R.mipmap.goods_status_0
            GoodsVO.STATUS_MIX_1 -> R.mipmap.goods_status_1
            GoodsVO.STATUS_MIX_2 -> R.mipmap.goods_status_2
//            GoodsVO.STATUS_MIX_3 -> R.mipmap.goods_status_3
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
