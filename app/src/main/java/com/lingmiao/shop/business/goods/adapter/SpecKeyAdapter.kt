package com.lingmiao.shop.business.goods.adapter

import android.widget.TextView
import com.chad.library.adapter.base.BaseMultiItemQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.chad.library.adapter.base.entity.MultiItemEntity
import com.lingmiao.shop.R
import com.lingmiao.shop.business.goods.api.bean.SpecKeyVO

/**
 * Author : Elson
 * Date   : 2020/7/19
 * Desc   : 规格名称
 */
class SpecKeyAdapter : BaseMultiItemQuickAdapter<MultiItemEntity, BaseViewHolder>(null) {

    companion object {
        const val TYPE_SPEC = 1
        const val TYPE_ADD_BTN = 0
    }

    init {
        addItemType(TYPE_SPEC, R.layout.goods_adapter_spec_key)
        addItemType(TYPE_ADD_BTN, R.layout.goods_adapter_spec_key_add)
    }

    override fun convert(helper: BaseViewHolder, item: MultiItemEntity?) {
        item?.apply {
            if (item.itemType == TYPE_SPEC) {
                val spec = (item as SpecKeyVO)
                helper.setText(R.id.specKeyTv, spec.specName)
                helper.getView<TextView>(R.id.specKeyTv).isSelected = spec.isSelected
            }
        }
    }

    fun addSpecKey(spceVo: SpecKeyVO) {
        addData(data.size - 1, spceVo)
    }
}