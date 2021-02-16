package com.lingmiao.shop.business.goods.adapter

import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.lingmiao.shop.R
import com.lingmiao.shop.business.goods.api.bean.DeliveryTempVO

/**
 * Author : Elson
 * Date   : 2020/8/7
 * Desc   : 配送模板
 */
class DeliveryTempAdapter :
    BaseQuickAdapter<DeliveryTempVO, BaseViewHolder>(R.layout.goods_adpater_delivery_temp) {

    var selectTempVO: DeliveryTempVO? = null

    override fun convert(helper: BaseViewHolder, item: DeliveryTempVO) {
        helper.setText(R.id.nameTv, item.name)
        helper.setImageResource(R.id.imageView, getSelectedRes(item))
        helper.setChecked(R.id.nameTv, item.isChecked)
    }

    private fun getSelectedRes(item: DeliveryTempVO): Int {
        return if (item.isChecked) R.mipmap.goods_spec_checked else R.mipmap.goods_spec_uncheck
    }

    fun selectTemplate(position: Int) {
        val tempVO = data[position]
        // 取消之前选中的模板
        if (tempVO.isChecked) {
            selectTempVO = null
            tempVO.isChecked = false
            notifyItemChanged(position)
        } else {
            // 选中其它的模板
            selectTempVO = tempVO
            data.forEachIndexed { index, tempVO ->
                tempVO.isChecked = index == position
            }
            notifyDataSetChanged()
        }
    }
}