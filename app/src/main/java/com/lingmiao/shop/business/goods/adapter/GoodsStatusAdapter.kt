package com.lingmiao.shop.business.goods.adapter

import android.annotation.SuppressLint
import android.view.View
import android.widget.CompoundButton
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.LayoutRes
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.lingmiao.shop.R
import com.lingmiao.shop.business.goods.api.bean.GoodsVO
import com.lingmiao.shop.business.goods.config.GoodsConfig
import com.lingmiao.shop.business.goods.event.RefreshGoodsStatusEvent
import com.lingmiao.shop.business.tools.adapter.setOnCheckedChangeListener
import com.lingmiao.shop.util.GlideUtils
import com.lingmiao.shop.util.formatDouble
import com.james.common.utils.exts.check
import org.greenrobot.eventbus.EventBus

/**
 * Author : Elson
 * Date   : 2020/7/11
 * Desc   : 商品状态列表(出售中、待审核、已下架)
 */
open class GoodsStatusAdapter(val goodsStatus: Int, @LayoutRes layout: Int = R.layout.goods_adapter_goods_status) :
    BaseQuickAdapter<GoodsVO, BaseViewHolder>(layout) {

    private var isBatchEditModel : Boolean = false;

    @SuppressLint("StringFormatMatches")
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
            helper.setText(R.id.enableGoodsQuantityTv, mContext.getString(R.string.goods_home_enable_quantity, enableQuantity));
            val salesCount = mContext.getString(R.string.goods_home_sales, buyCount.check("0"))
            helper.setText(R.id.goodsSalesTv, salesCount)
            helper.setText(R.id.goodsPriceTv, formatDouble(price))
            helper.addOnClickListener(R.id.menuIv)

            setOnCheckedChangeListener(helper.getView(R.id.cb_goods_check), isChecked ?: false) { buttonView: CompoundButton?, isChecked: Boolean ->
                goodsVO?.isChecked = isChecked;
                if(!isChecked) {
                    EventBus.getDefault().post(RefreshGoodsStatusEvent(goodsStatus!!))
                }
            }
            helper.setGone(R.id.cb_goods_check, isBatchEditModel);
            helper.setGone(R.id.menuIv, !isBatchEditModel);

            helper.setText(R.id.goodsOwnerTv, String.format("[来源：%s]", supplierName));
            helper.setGone(R.id.goodsOwnerTv, supplierName?.length?:0 > 0);
        }

    }

    fun updateQuantity(quantity: String?, position: Int) {
        val goods = data[position]
        goods.quantity = quantity
        notifyItemChanged(position)
    }

    fun setBatchEditModel(flag : Boolean) {
        isBatchEditModel = flag;
        notifyDataSetChanged();
    }

    fun shiftChecked(position: Int) {
        data[position]?.isChecked = !(data[position]?.isChecked ?: false);
        notifyItemChanged(position)
    }

}
