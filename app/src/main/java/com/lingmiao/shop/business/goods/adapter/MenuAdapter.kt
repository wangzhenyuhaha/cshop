package com.lingmiao.shop.business.goods.adapter

import android.widget.CompoundButton
import com.chad.library.adapter.base.BaseItemDraggableAdapter
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.lingmiao.shop.R
import com.lingmiao.shop.business.goods.api.bean.ShopGroupVO
import com.lingmiao.shop.util.GlideUtils
import com.james.common.utils.exts.isNotBlank
import com.lingmiao.shop.business.tools.adapter.setOnCheckedChangeListener

/**
 * 菜单
 */
class MenuAdapter() :
    BaseItemDraggableAdapter<ShopGroupVO, BaseViewHolder>(R.layout.goods_adapter_menu, null) {

    private var isBatchEditModel: Boolean = false;

    override fun convert(helper: BaseViewHolder, item: ShopGroupVO?) {
        item?.apply {
            GlideUtils.setImageUrl1(helper.getView(R.id.menuIconIv), shopCatPic)
            helper.setText(R.id.menuNameTv, shopCatName)
            // helper.setText(R.id.menuDescTv, getShopCatDesc(item))
            // helper.setText(R.id.menuOrderTv, "排序：${sort}")
            // helper.setGone(R.id.marginView, helper.adapterPosition == data.size - 1)
            // helper.setGone(R.id.menuCheckCb, isBatchEditModel);
            // 删除选中
            helper.setChecked(R.id.menuCheckCb, isChecked?:false);
            helper.setGone(R.id.menuEditTv, !isBatchEditModel);
            helper.setGone(R.id.menuEditGoodsTv, !isBatchEditModel)
            helper.setGone(R.id.menuDeleteTv, !isBatchEditModel)
            helper.setGone(R.id.divider, !isBatchEditModel);
            helper.setGone(R.id.ivMenuTop, isBatchEditModel && helper.adapterPosition == 0);
            helper.setGone(R.id.menuTopTv, isBatchEditModel && helper.adapterPosition > 0);

            helper.addOnClickListener(R.id.menuEditTv)
            helper.addOnClickListener(R.id.menuDeleteTv)
            helper.addOnClickListener(R.id.menuEditGoodsTv)
            helper.addOnClickListener(R.id.menuTopTv);

            helper.setChecked(R.id.menuVisibleCb, disable == 1);
            helper.setText(R.id.menuVisibleCb, if(disable == 1) "显示" else "不显示")
            helper.setGone(R.id.menuVisibleCb, !isBatchEditModel);
            helper.addOnClickListener(R.id.menuVisibleCb);
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

    fun getBatchEdit() : Boolean {
        return  isBatchEditModel;
    }

}