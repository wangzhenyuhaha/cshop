package com.lingmiao.shop.business.goods.adapter

import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.lingmiao.shop.R
import com.lingmiao.shop.business.goods.Data

class SimpleAdapter :
    BaseQuickAdapter<Data, BaseViewHolder>(R.layout.adapter_one_textview) {

    override fun convert(helper: BaseViewHolder, goodsVO: Data?) {
        goodsVO?.apply {
            helper.setText(R.id.goodsNameTv, goodsName)
        }
    }
}