package com.lingmiao.shop.widget

import android.app.Activity
import android.content.Context
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.view.View
import android.widget.RelativeLayout
import androidx.annotation.ColorRes
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.core.content.ContextCompat
import com.blankj.utilcode.util.BarUtils
import com.lingmiao.shop.R
import com.james.common.utils.exts.isNotBlank
import com.james.common.utils.exts.show
import kotlinx.android.synthetic.main.view_toolbar.view.*

/**
 * Author : Elson
 * Date   : 2020/7/19
 * Desc   : 自定义标题栏
 */
class ToolBarView @JvmOverloads constructor(
    context: Context?,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : RelativeLayout(context, attrs, defStyleAttr) {

    init {
        View.inflate(context, R.layout.view_toolbar, this)
        BarUtils.setStatusBarLightMode(context as Activity, true)
        leftIv.setOnClickListener {
            (context as? Activity)?.finish()
        }
    }

    fun setTitleContent(@StringRes titleText: Int) {
        titleTv.text = context.getString(titleText)
    }

    fun setTitleContent(titleText: String) {
        titleTv.text = titleText
    }

    fun showDivider(isShow: Boolean) {
        divider.show(isShow)
    }

    fun setLeftListener(@DrawableRes drawableRes: Int, callback: (() -> Unit)?) {
        leftIv.setImageResource(drawableRes)
        leftIv.setOnClickListener {
            callback?.invoke()
        }
    }

    fun setRightListener(@StringRes rightText: Int, @ColorRes colorRes: Int, callback: (() -> Unit)?) {
        setRightListener(context.getString(rightText), colorRes, callback)
    }

    fun setRightListener(drawableRes: Drawable?, rightText: String, @ColorRes colorRes: Int, callback: (() -> Unit)?) {
        rightTv.show(rightText.isNotBlank())
        rightTv.text = rightText
        rightTv.setTextColor(ContextCompat.getColor(context, colorRes))
        rightTv.setCompoundDrawables(drawableRes, null, null, null);
        rightTv.setOnClickListener{
            callback?.invoke();
        }
    }

    fun setRightListener(rightText: String?, @ColorRes colorRes: Int, callback: (() -> Unit)?) {
        rightTv.show(rightText.isNotBlank())
        rightTv.text = rightText
        rightTv.setTextColor(ContextCompat.getColor(context, colorRes))
        rightTv.setOnClickListener {
            callback?.invoke()
        }
    }


}