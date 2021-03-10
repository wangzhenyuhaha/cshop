package com.lingmiao.shop.business.goods.adapter

import android.widget.CompoundButton
import com.chad.library.adapter.base.BaseMultiItemQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.lingmiao.shop.R
import com.lingmiao.shop.business.goods.api.bean.MenuVo
import com.lingmiao.shop.business.tools.adapter.setOnCheckedChangeListener

class UsedMenuAdapter(mList : List<MenuVo>?) : BaseMultiItemQuickAdapter<MenuVo, BaseViewHolder>(mList) {


    private var checkedDrawableRes: Int;
    private var uncheckDrawable : Int;

    private var isBatchEditModel: Boolean = false;


    companion object {
        val TYPE_LEVEL_0 = 0
        val TYPE_LEVEL_1 = 1
    }

    init {
        addItemType(TYPE_LEVEL_0, R.layout.goods_adapter_used_menu_title);
        addItemType(TYPE_LEVEL_1, R.layout.goods_adapter_used_menu_item);

        checkedDrawableRes = R.mipmap.goods_blue_arrow_down;
        uncheckDrawable = R.mipmap.login_right_arrow;
    }

    override fun convert(helper: BaseViewHolder, item: MenuVo?) {

        item?.apply {
            if(item.itemType == TYPE_LEVEL_0) {
                pPosition = helper.adapterPosition;
                val cItem : MenuVo = item;
                // 菜单名
                helper.setText(R.id.menuTitleTv, cItem?.name);
                // 折叠
                helper.setImageResource(R.id.menuExpandIv, if(cItem.isExpanded) checkedDrawableRes else uncheckDrawable);
                helper.addOnClickListener(R.id.menuExpandIv);
                // 菜单数
                // menuCountTv
                // 总行
                helper.addOnClickListener(R.id.titleLL);
                // 删除选中
                helper.setGone(R.id.menuCheckCb, isBatchEditModel);

                helper.setChecked(R.id.menuCheckCb, isChecked?:false);

                setOnCheckedChangeListener(helper.getView(R.id.menuCheckCb), isChecked ?: false) { buttonView: CompoundButton?, isChecked: Boolean ->
                    item?.isChecked = isChecked;
//                if(!isChecked) {
//                    EventBus.getDefault().post(RefreshGoodsStatusEvent(goodsStatus!!))
//                }
                }
            } else if(item.itemType == TYPE_LEVEL_1) {
                val cItem : MenuVo = item;
                cItem.pPosition = pPosition;
                helper.setText(R.id.itemTv, cItem?.name);
                helper.setImageResource(R.id.checkIv, if(cItem.isExpanded) checkedDrawableRes else uncheckDrawable);

                helper.addOnClickListener(R.id.itemLL);
                helper.addOnClickListener(R.id.checkIv);

            }

        }

    }

    fun setBatchEditModel(flag: Boolean) {
        isBatchEditModel = flag;
        notifyDataSetChanged();
    }

}