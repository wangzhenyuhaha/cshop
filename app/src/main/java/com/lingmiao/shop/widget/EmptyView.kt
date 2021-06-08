package com.lingmiao.shop.widget

import android.content.Context
import android.util.AttributeSet
import android.view.Gravity
import android.view.View
import android.widget.LinearLayout
import android.widget.TextView
import com.lingmiao.shop.R

/**
 * Author : Elson
 * Date   : 2020/8/23
 * Desc   :
 */
class EmptyView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : LinearLayout(context, attrs, defStyleAttr) {

    var emptyView : TextView? = null;

    init {
        val view: View = View.inflate(context, R.layout.view_empty_view, this)
        view.layoutParams = LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT)
        orientation = VERTICAL
        gravity = Gravity.CENTER
        emptyView = view.findViewById<TextView>(R.id.emptyView);
    }

    fun setMessage(text : String) : EmptyView {
        emptyView?.setText(text);
        return this;
    }

}