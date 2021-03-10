package com.lingmiao.shop.business.goods.adapter

import android.widget.CompoundButton
import android.widget.TextView
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.lingmiao.shop.R
import com.lingmiao.shop.business.goods.api.bean.ShopGroupVO
import com.lingmiao.shop.util.GlideUtils
import com.james.common.utils.exts.isNotBlank
import com.lingmiao.shop.business.goods.event.RefreshGoodsStatusEvent
import com.lingmiao.shop.business.tools.adapter.setOnCheckedChangeListener
import org.greenrobot.eventbus.EventBus

/**
 * Author : Elson
 * Date   : 2020/7/18
 * Desc   : 商品分组管理列表
 */
class MenuAdapter() :
    BaseQuickAdapter<ShopGroupVO, BaseViewHolder>(R.layout.goods_adapter_menu) {

    private var isBatchEditModel: Boolean = false;


    override fun convert(helper: BaseViewHolder, item: ShopGroupVO?) {
        item?.apply {
            GlideUtils.setImageUrl1(helper.getView(R.id.menuIconIv), shopCatPic)
            helper.setText(R.id.menuNameTv, shopCatName)
            // helper.setText(R.id.menuDescTv, getShopCatDesc(item))
            // helper.setText(R.id.menuOrderTv, "排序：${sort}")
            helper.setChecked(R.id.showRadio, isGroupShow());
            helper.setChecked(R.id.hideRadio, !isGroupShow());
            helper.setGone(R.id.marginView, helper.adapterPosition == data.size - 1)
            helper.setGone(R.id.menuCheckCb, isBatchEditModel);
            // 删除选中
            helper.setChecked(R.id.menuCheckCb, isChecked?:false);
            helper.setGone(R.id.menuEditTv, !isBatchEditModel);
            helper.setGone(R.id.menuRg, !isBatchEditModel);
            helper.addOnClickListener(R.id.menuEditTv)


            setOnCheckedChangeListener(helper.getView(R.id.menuCheckCb), isChecked ?: false) { buttonView: CompoundButton?, isChecked: Boolean ->
                item?.isChecked = isChecked;
//                if(!isChecked) {
//                    EventBus.getDefault().post(RefreshGoodsStatusEvent(goodsStatus!!))
//                }
            }
        }
    }

    private fun getShopCatDesc(item: ShopGroupVO): String {
        if (item.shopCatDesc.isNotBlank()) {
            return "(${item.shopCatDesc})"
        }
        return ""
    }

    fun setBatchEditModel(flag: Boolean) {
        isBatchEditModel = flag;
        notifyDataSetChanged();
    }

}