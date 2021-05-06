package com.lingmiao.shop.business.goods.adapter

import android.widget.CompoundButton
import android.widget.ImageView
import android.widget.TextView
import com.chad.library.adapter.base.BaseMultiItemQuickAdapter
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.lingmiao.shop.R
import com.lingmiao.shop.util.GlideUtils
import com.james.common.utils.exts.isNotBlank
import com.lingmiao.shop.business.goods.api.bean.CategoryVO
import com.lingmiao.shop.business.goods.api.bean.MenuVo
import com.lingmiao.shop.business.goods.event.RefreshGoodsStatusEvent
import com.lingmiao.shop.business.goods.view.CatItemRvLayout
import com.lingmiao.shop.business.tools.adapter.setOnCheckedChangeListener
import com.lingmiao.shop.util.formatDouble
import org.greenrobot.eventbus.EventBus

/**
 * Author : Elson
 * Date   : 2020/7/18
 * Desc   : 商品分组管理列表
 */
class CategoryAdapter(mList : List<CategoryVO>?) : BaseQuickAdapter<CategoryVO, BaseViewHolder>(R.layout.goods_adapter_category_list, mList) {


    private var checkedDrawableRes: Int;
    private var uncheckDrawable : Int;

    private var isBatchEditModel: Boolean = false;


    companion object {
        val TYPE_LEVEL_0 = 0
        val TYPE_LEVEL_1 = 1
    }

    init {
//        addItemType(TYPE_LEVEL_0, R.layout.goods_adapter_used_menu_title);
//        addItemType(TYPE_LEVEL_1, R.layout.goods_adapter_used_menu_item);

        checkedDrawableRes = R.mipmap.goods_blue_arrow_down;
        uncheckDrawable = R.mipmap.login_right_arrow;
    }

    override fun convert(helper: BaseViewHolder, item: CategoryVO?) {

        item?.apply {
            pPosition = helper.adapterPosition;
            val cItem : CategoryVO = item;
            // 菜单名
            helper.setText(R.id.menuTitleTv, cItem?.name);
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

            helper.setVisible(R.id.menuExpandIv, showLevel == 0);
            helper.setVisible(R.id.menuAddGoodsIv, showLevel == 0);
            helper.getView<CatItemRvLayout>(R.id.rvCatList).addItems(myC);

//            if(item.itemType == TYPE_LEVEL_0) {
//
//            } else if(item.itemType == TYPE_LEVEL_1) {
//                val cItem : CategoryVO = item;
//                cItem.pPosition = pPosition;
////                helper.setText(R.id.itemTv, cItem?.name);
////                helper.setImageResource(R.id.checkIv, if(cItem.isExpanded) checkedDrawableRes else uncheckDrawable);
//
////                helper.addOnClickListener(R.id.itemLL);
////                helper.addOnClickListener(R.id.checkIv);
//
//                //GlideUtils.setImageUrl1(helper.getView<ImageView>(R.id.goodsIv), thumbnail)
//
////                helper.getView<TextView>(R.id.goodsNameTv).setCompoundDrawablesWithIntrinsicBounds(if(item?.goodsType == GoodsConfig.GOODS_TYPE_VIRTUAL) R.mipmap.ic_virtual else 0, 0, 0, 0)
//                helper.setText(R.id.goodsNameTv, name)
//
//                helper.setText(
//                    R.id.goodsQuantityTv,
//                    mContext.getString(R.string.goods_home_spec, "50ml")
//                )
//
//                helper.setText(R.id.goodsPriceTv, formatDouble(11.22))
//
//                helper.setGone(R.id.menuIv, isBatchEditModel);
////            setOnCheckedChangeListener(helper.getView(R.id.menuIv), isChecked ?: false) { buttonView: CompoundButton?, isChecked: Boolean ->
////                item?.isChecked = isChecked;
////            }
//
//                helper.setChecked(R.id.menuIv, item?.isChecked?:false);
//                helper.addOnClickListener(R.id.menuIv);
//
//            }

        }

    }

    fun setBatchEditModel(flag: Boolean) {
        isBatchEditModel = flag;
        notifyDataSetChanged();
    }

}