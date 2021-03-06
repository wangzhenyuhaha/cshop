package com.lingmiao.shop.business.goods.adapter

import android.widget.CompoundButton
import android.widget.ImageView
import android.widget.TextView
import com.chad.library.adapter.base.BaseMultiItemQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.lingmiao.shop.R
import com.lingmiao.shop.business.goods.api.bean.MenuVo
import com.lingmiao.shop.business.goods.api.bean.ShopGroupVO
import com.lingmiao.shop.business.goods.config.GoodsConfig
import com.lingmiao.shop.business.tools.adapter.setOnCheckedChangeListener
import com.lingmiao.shop.util.GlideUtils
import com.lingmiao.shop.util.formatDouble

class UsedMenuAdapter(mList : List<ShopGroupVO>?) : BaseMultiItemQuickAdapter<ShopGroupVO, BaseViewHolder>(mList) {


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

    override fun convert(helper: BaseViewHolder, item: ShopGroupVO?) {

        item?.apply {
            if(item.itemType == TYPE_LEVEL_0) {
                pPosition = helper.adapterPosition;
                val cItem : ShopGroupVO = item;
                // 菜单名
                helper.setText(R.id.menuTitleTv, cItem?.shopCatName);
                // 折叠
                helper.setImageResource(R.id.menuExpandIv, if(cItem.isExpanded) checkedDrawableRes else uncheckDrawable);
                helper.addOnClickListener(R.id.menuExpandIv);
                // 菜单数
                // menuCountTv
                // 总行
                helper.addOnClickListener(R.id.titleLL);
                helper.addOnClickListener(R.id.menuAddGoodsIv);
                // 删除选中
                helper.setGone(R.id.menuCheckCb, isBatchEditModel);
                helper.setGone(R.id.menuAddGoodsIv, !isBatchEditModel);

                helper.setChecked(R.id.menuCheckCb, isChecked?:false);

                setOnCheckedChangeListener(helper.getView(R.id.menuCheckCb), isChecked ?: false) { buttonView: CompoundButton?, isChecked: Boolean ->
                    item?.isChecked = isChecked;
//                if(!isChecked) {
//                    EventBus.getDefault().post(RefreshGoodsStatusEvent(goodsStatus!!))
//                }
                }
            } else if(item.itemType == TYPE_LEVEL_1) {
                val cItem : ShopGroupVO = item;
                cItem.pPosition = pPosition;
//                helper.setText(R.id.itemTv, cItem?.name);
//                helper.setImageResource(R.id.checkIv, if(cItem.isExpanded) checkedDrawableRes else uncheckDrawable);

//                helper.addOnClickListener(R.id.itemLL);
//                helper.addOnClickListener(R.id.checkIv);

                GlideUtils.setImageUrl1(helper.getView<ImageView>(R.id.goodsIv), thumbnail)

//                helper.getView<TextView>(R.id.goodsNameTv).setCompoundDrawablesWithIntrinsicBounds(if(item?.goodsType == GoodsConfig.GOODS_TYPE_VIRTUAL) R.mipmap.ic_virtual else 0, 0, 0, 0)
                helper.setText(R.id.goodsNameTv, shopCatName)

                helper.setText(
                    R.id.goodsQuantityTv,
                    mContext.getString(R.string.goods_home_spec, "50ml")
                )

                helper.setText(R.id.goodsPriceTv, formatDouble(11.22))

                helper.setGone(R.id.menuIv, isBatchEditModel);
//            setOnCheckedChangeListener(helper.getView(R.id.menuIv), isChecked ?: false) { buttonView: CompoundButton?, isChecked: Boolean ->
//                item?.isChecked = isChecked;
//            }

                helper.setChecked(R.id.menuIv, item?.isChecked?:false);
                helper.addOnClickListener(R.id.menuIv);

            }

        }

    }

    fun setBatchEditModel(flag: Boolean) {
        isBatchEditModel = flag;
        notifyDataSetChanged();
    }

}