package com.lingmiao.shop.business.goods.adapter

import android.text.Editable
import android.widget.EditText
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.lingmiao.shop.R
import com.lingmiao.shop.business.goods.api.request.QuantityRequest
import com.lingmiao.shop.widget.TextWatcherAdapter
import com.james.common.utils.exts.isNotBlank

/**
 * Author : Elson
 * Date   : 2020/8/16
 * Desc   : 多规格库存设置
 */
class MultiQuantityAdapter :
    BaseQuickAdapter<QuantityRequest, BaseViewHolder>(R.layout.goods_adapter_multi_quantity) {

    override fun convert(helper: BaseViewHolder, quantityVO: QuantityRequest?) {
        if (quantityVO == null) return

        helper.setText(R.id.quantityNameTv, quantityVO.quantitiyName)
        helper.setText(R.id.quantityEdt, quantityVO.quantityCount)
        addTextChangeListener(helper.getView(R.id.quantityEdt), quantityVO.quantityCount) {
            quantityVO.quantityCount = it
        }
    }

    fun getUpdateQuantityList(): List<QuantityRequest> {
        return data.filter { it.quantityCount.isNotBlank() }
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


    class SpecTextWatcher(private val callback: (String) -> Unit) : TextWatcherAdapter() {

        override fun afterTextChanged(s: Editable?) {
            callback.invoke(s.toString())
        }

    }
}