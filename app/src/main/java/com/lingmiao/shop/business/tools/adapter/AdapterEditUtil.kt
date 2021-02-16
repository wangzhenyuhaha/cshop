package com.lingmiao.shop.business.tools.adapter

import android.text.Editable
import android.widget.CheckBox
import android.widget.CompoundButton
import android.widget.EditText
import com.lingmiao.shop.R
import com.lingmiao.shop.widget.TextWatcherAdapter

class SpecTextWatcher(private val callback: (String) -> Unit) : TextWatcherAdapter() {
    override fun afterTextChanged(s: Editable?) {
        callback.invoke(s.toString())
    }
}

inline fun addTextChangeListener(editText: EditText, input: String?, noinline callback: (String) -> Unit) {
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


class DefaultCompoundButtonChangeListener(private val callback : (buttonView: CompoundButton?, isChecked: Boolean) -> Unit) : CompoundButton.OnCheckedChangeListener {
    override fun onCheckedChanged(buttonView: CompoundButton?, isChecked: Boolean) {
        callback.invoke(buttonView, isChecked);
    }
}

inline fun setOnCheckedChangeListener(checkBox: CheckBox, boolean: Boolean, noinline callback: (buttonView: CompoundButton?, isChecked: Boolean) -> Unit) {
    val checkListener = DefaultCompoundButtonChangeListener(callback);
    checkBox.setOnCheckedChangeListener(checkListener);
    checkBox.isChecked = boolean;
}