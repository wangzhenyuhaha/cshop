package com.james.common.base.delegate

import android.app.Activity
import android.content.Context
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.ColorRes
import androidx.annotation.DrawableRes
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.content.ContextCompat
import com.blankj.utilcode.util.BarUtils
import com.james.common.R
import com.james.common.utils.exts.gone
import com.james.common.utils.exts.visiable

/**
 * Author : Elson
 * Date   : 2020/7/12
 * Desc   : ToolBar 操作行为的委托类
 */
class ToolBarDelegate(var context: Context, val toolbar: Toolbar?, var lightMode: Boolean) {

    private var tvMidTitle: TextView? = null
    private var tvRightText: TextView? = null
    private var ivRight: ImageView? = null
    private var ivRight2: ImageView? = null
    private var vLine : View? = null

    init {
        (context as? AppCompatActivity)?.apply {
            setSupportActionBar(toolbar)
            toolbar?.apply {
                tvMidTitle = findViewById(R.id.tv_mid_title)
                tvRightText = findViewById(R.id.tv_right_text)
                ivRight = findViewById(R.id.iv_right)
                ivRight2 = findViewById(R.id.iv_right2)
                vLine = findViewById(R.id.vi_top_line);
            }
            supportActionBar?.apply {
                setTitle("") //设置标题
                setDisplayHomeAsUpEnabled(true) //显示箭头图标
                if (lightMode) {
                    toolbar?.setBackgroundColor(ContextCompat.getColor(context, R.color.white))
                    setHomeAsUpIndicator(R.mipmap.ic_back_new) //更改返回图标
                } else {
                    toolbar?.setBackgroundColor(
                        ContextCompat.getColor(
                            context,
                            R.color.color_207AE1
                        )
                    )
                    BarUtils.setStatusBarColor(
                        context as Activity,
                        ContextCompat.getColor(context, R.color.color_207AE1)
                    )
                    setHomeAsUpIndicator(R.mipmap.ic_back_new_white) //更改返回图标
                    tvMidTitle?.setTextColor(ContextCompat.getColor(context, R.color.white))
                    tvRightText?.setTextColor(ContextCompat.getColor(context, R.color.white))
                }
                BarUtils.setStatusBarLightMode(context as Activity, lightMode)
            }
        }
    }

    fun setToolbarBackground(context: Context, @ColorRes colorRes: Int) {
        toolbar?.setBackgroundColor(ContextCompat.getColor(context, colorRes))
    }

    fun setStatusBarColor(context: Context, @ColorRes colorRes: Int) {
        BarUtils.setStatusBarColor(
            context as Activity,
            ContextCompat.getColor(context, colorRes)
        )
    }

    fun setToolbarBackgroundOfOtherTheme(context: Context, @ColorRes colorRes: Int, @ColorRes textColorRes: Int) {
        toolbar?.setBackgroundColor(ContextCompat.getColor(context, colorRes))
        tvMidTitle?.setTextColor(ContextCompat.getColor(context, textColorRes))
        (context as? AppCompatActivity)?.apply {
            supportActionBar?.apply {
                setHomeAsUpIndicator(R.mipmap.ic_back_new_white) //更改返回图标
            }
        }
        BarUtils.setStatusBarColor(
            context as Activity,
            ContextCompat.getColor(context, colorRes)
        )
    }

    fun setBackIcon(@DrawableRes resId: Int) {
        (context as? AppCompatActivity)?.getSupportActionBar()?.setHomeAsUpIndicator(resId)
    }

    fun hideToolBar() {
        toolbar?.visibility = View.GONE
    }

    fun showToolBar() {
        toolbar?.visibility = View.VISIBLE
    }

    fun setMidTitle(midTitle: String?) {
        tvMidTitle?.text = midTitle ?: ""
    }

    fun hideArrow() {
        (context as? AppCompatActivity)?.getSupportActionBar()?.setDisplayHomeAsUpEnabled(false)
    }

    fun setRightIcon(@DrawableRes resId: Int, listener:View.OnClickListener?) {
        ivRight?.apply {
            visiable()
            setImageResource(resId)
            setOnClickListener(listener)
        }
    }


    fun setRightIcon2(@DrawableRes resId: Int, listener: View.OnClickListener?) {
        ivRight2?.apply {
            visiable()
            setImageResource(resId)
            setOnClickListener(listener)
        }
    }


    fun setRightText(string: String, listener: View.OnClickListener?) {
        tvRightText?.apply {
            visiable()
            text = string;
            setOnClickListener(listener)
        }
    }

    fun setRightText(string: String, color: Int, listener: View.OnClickListener?) {
        tvRightText?.apply {
            visiable()
            setTextColor(color)
            text = string;
            setOnClickListener(listener)
        }
    }

    fun setRightTwoIcon(@DrawableRes resId: Int, listener: View.OnClickListener?, @DrawableRes resId2: Int, listener2: View.OnClickListener?) {
        setRightIcon2(resId, listener);
        setRightIcon(resId2, listener2);
    }

    fun setLineGone() {
        vLine?.gone();
    }

    fun showLine() {
        vLine?.visiable();
    }
}