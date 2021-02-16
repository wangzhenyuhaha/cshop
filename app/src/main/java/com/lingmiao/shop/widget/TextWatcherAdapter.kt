package com.lingmiao.shop.widget

import android.text.Editable
import android.text.TextWatcher

/**
 * @author elson
 * @date 2020/8/1
 * @Desc 接口适配
 */
open class TextWatcherAdapter: TextWatcher {

    override fun afterTextChanged(s: Editable?) {
        // do nothing
    }

    override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
        // do nothing
    }

    override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
        // do nothing
    }
}