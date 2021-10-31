package com.lingmiao.shop.business.goods.adapter

import com.chad.library.adapter.base.BaseMultiItemQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.lingmiao.shop.R
import com.lingmiao.shop.business.goods.api.bean.CategoryVO

/**
 * Desc   : 商品分组管理列表
 */
class CategoryAdapter(mList: List<CategoryVO>?) :
    BaseMultiItemQuickAdapter<CategoryVO, BaseViewHolder>(mList) {


    private var checkedDrawableRes: Int;
    private var uncheckDrawable: Int;

    private var isBatchEditModel: Boolean = false;


    companion object {
        val TYPE_LEVEL_0 = 0
        val TYPE_LEVEL_1 = 1
    }

    init {
        addItemType(TYPE_LEVEL_0, R.layout.goods_adapter_cate_title)
        addItemType(TYPE_LEVEL_1, R.layout.goods_adapter_cate_item)

        checkedDrawableRes = R.mipmap.goods_blue_arrow_down
        uncheckDrawable = R.mipmap.login_right_arrow
    }

    override fun convert(helper: BaseViewHolder, item: CategoryVO?) {
        item?.apply {
            if (item.itemType == TYPE_LEVEL_0) {
                // 菜单名
                helper.setText(R.id.cateTitleTv, item.name)
                helper.setText(R.id.cateNumTv, "（共${item.goodsCount.toString()}件商品）")
                helper.setImageResource(
                    R.id.cateExpandIv,
                    if (item.isExpanded) checkedDrawableRes else uncheckDrawable
                )

                helper.addOnClickListener(R.id.cateTitleTv)
                helper.addOnClickListener(R.id.cateExpandIv)
                helper.addOnClickListener(R.id.cateAddIv)
                // 更多
                helper.addOnClickListener(R.id.cateMoreIv);
            } else if (item.itemType == TYPE_LEVEL_1) {

                helper.setText(R.id.cateTitleTv, item.name)
                helper.setText(R.id.cateNumTv, "（${item.goodsCount.toString()}）")
                helper.addOnClickListener(R.id.cateMoreIv)
            }
        }

    }

    fun setBatchEditModel(flag: Boolean) {
        isBatchEditModel = flag;
        notifyDataSetChanged();
    }

}