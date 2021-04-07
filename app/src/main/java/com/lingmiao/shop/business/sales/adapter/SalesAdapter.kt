package com.lingmiao.shop.business.sales.adapter

import android.widget.TextView
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.lingmiao.shop.R
import com.lingmiao.shop.business.goods.api.bean.ShopGroupVO
import com.lingmiao.shop.util.GlideUtils
import com.james.common.utils.exts.isNotBlank
import com.lingmiao.shop.business.sales.bean.SalesActivityItemVo
import com.lingmiao.shop.business.sales.bean.SalesVo
import com.lingmiao.shop.business.sales.bean.UserVo
import com.lingmiao.shop.business.sales.view.SalesActivityRvLayout

/**
 * Author : Elson
 * Date   : 2020/7/18
 * Desc   : 商品分组管理列表
 */
class SalesAdapter() : BaseQuickAdapter<SalesVo, BaseViewHolder>(R.layout.sales_adapter_sales) {

    override fun convert(helper: BaseViewHolder, item: SalesVo?) {
        item?.apply {

            helper.setText(R.id.salesNameTv, "满减");
            helper.setText(R.id.salesStatusTv, "进行中");
            helper.getView<SalesActivityRvLayout>(R.id.salesActivityC).addItems(getItem());
            helper.addOnClickListener(R.id.salesEditTv);
        }
    }

    fun getItem() : MutableList<SalesActivityItemVo> {
        val list : MutableList<SalesActivityItemVo> = mutableListOf();
        list.add(SalesActivityItemVo());
        list.add(SalesActivityItemVo());
        list.add(SalesActivityItemVo());
        return list;
    }

}