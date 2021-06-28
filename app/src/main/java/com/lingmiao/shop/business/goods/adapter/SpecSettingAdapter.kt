package com.lingmiao.shop.business.goods.adapter

import android.text.Editable
import android.widget.CompoundButton
import android.widget.EditText
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.lingmiao.shop.R
import com.lingmiao.shop.business.goods.api.bean.GoodsSkuVOWrapper
import com.lingmiao.shop.business.goods.api.bean.SpecSettingParams
import com.lingmiao.shop.widget.TextWatcherAdapter

/**
 * Author : Elson
 * Date   : 2020/7/31
 * Desc   : 规格值列表
 */
class SpecSettingAdapter(val type : Int) :
    BaseQuickAdapter<GoodsSkuVOWrapper, BaseViewHolder>(R.layout.goods_adapter_spec_setting) {

    override fun convert(helper: BaseViewHolder, specVO: GoodsSkuVOWrapper?) {
        if (specVO == null) return

        helper.setText(R.id.specNameCheckbox, specVO.skuNameDesc)
        helper.setChecked(R.id.specNameCheckbox, specVO.isChecked)
        helper.setOnCheckedChangeListener(R.id.specNameCheckbox, object : CompoundButton.OnCheckedChangeListener {
            override fun onCheckedChanged(buttonView: CompoundButton?, isChecked: Boolean) {
                specVO.isChecked = isChecked
            }
        })

        addTextChangeListener(helper.getView(R.id.eventPriceEdt), specVO.eventPrice) {
            specVO.eventPrice = it
        }
        addTextChangeListener(helper.getView(R.id.eventQuantityEdt), specVO.eventQuantity) {
            specVO.eventQuantity = it
        }
        addTextChangeListener(helper.getView(R.id.goodsPriceEdt), specVO.price) {
            specVO.price = it
        }
        addTextChangeListener(helper.getView(R.id.goodsPriceEdt), specVO.price) {
            specVO.price = it
        }
        addTextChangeListener(helper.getView(R.id.marketPriceEdt), specVO.mktprice) {
            specVO.mktprice = it
        }
        addTextChangeListener(helper.getView(R.id.goodsQuantityEdt), specVO.quantity) {
            specVO.quantity = it
        }
        helper.setEnabled(R.id.goodsQuantityEdt, specVO.isEditable);
        addTextChangeListener(helper.getView(R.id.goodsCostEdt), specVO.cost) {
            specVO.cost = it
        }
        addTextChangeListener(helper.getView(R.id.goodsNoEdt), specVO.sn) {
            specVO.sn = it
        }
        addTextChangeListener(helper.getView(R.id.goodsWeightEdt), specVO.weight) {
            specVO.weight = it
        }
        addTextChangeListener(helper.getView(R.id.goodsSKUEdt), specVO.upSkuId) {
            specVO.upSkuId = it
        }

        // 最后一个Item展示下边距，避免太丑
        helper.setGone(R.id.space, (helper.adapterPosition == data.size - 1))

//        helper.setGone(R.id.vSpecNo, type != 2)
//        helper.setGone(R.id.llSpecNo, type != 2)
//        helper.setGone(R.id.vSpecSku, type != 2)
//        helper.setGone(R.id.llSpecSku, type != 2)
    }

    private fun addTextChangeListener(editText: EditText, input: String?, callback: (String) -> Unit) {
        // 移除原有的输入监听
        val tag = editText.tag
        if (tag is SpecTextWatcher) {
            editText.removeTextChangedListener(tag)
        }
        // 添加新的输入监听
        val textWatcher = SpecTextWatcher(callback)
        editText.addTextChangedListener(textWatcher)
        editText.tag = textWatcher
        editText.setText(input)
    }

    /**
     * 更新勾选状态
     */
    fun updateCheckStatus(isCheck: Boolean) {
        data.forEach {
            it.isChecked = isCheck
        }
        notifyDataSetChanged()
    }

    fun clear() {
        data.clear()
        notifyDataSetChanged()
    }

    fun updateSetting(specSettingParams: SpecSettingParams) {
        data.forEach {
            if (it.isChecked) {
                it.convert(specSettingParams)
            }
        }
        notifyDataSetChanged()
    }

    fun isSpecSelected(): Boolean {
        data.forEach {
            if (it.isChecked) {
                return true
            }
        }
        return false
    }

    class SpecTextWatcher(private val callback: (String) -> Unit) : TextWatcherAdapter() {

        override fun afterTextChanged(s: Editable?) {
            callback.invoke(s.toString())
        }

    }
}