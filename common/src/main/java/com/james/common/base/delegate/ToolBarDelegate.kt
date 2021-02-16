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
import com.james.common.utils.exts.visiable

/**
 * Author : Elson
 * Date   : 2020/7/12
 * Desc   : ToolBar 操作行为的委托类
 */
class ToolBarDelegate(var context: Context, val toolbar: Toolbar?,var lightMode:Boolean) {

    private var tvMidTitle: TextView? = null
    private var tvRightText: TextView? = null
    private var ivRight: ImageView? = null

    init {
        (context as? AppCompatActivity)?.apply {
            setSupportActionBar(toolbar)
            toolbar?.apply {
                tvMidTitle = findViewById(R.id.tv_mid_title)
                tvRightText = findViewById(R.id.tv_right_text)
                ivRight = findViewById(R.id.iv_right)
            }
            supportActionBar?.apply {
                setTitle("") //设置标题
                setDisplayHomeAsUpEnabled(true) //显示箭头图标
                if(lightMode){
                    toolbar?.setBackgroundColor(ContextCompat.getColor(context,R.color.white))
                    setHomeAsUpIndicator(R.mipmap.ic_back_new) //更改返回图标
                }else{
                    toolbar?.setBackgroundColor(ContextCompat.getColor(context,R.color.color_3870ea_c))
                    BarUtils.setStatusBarColor(context as Activity,ContextCompat.getColor(context,R.color.color_3870ea_c))
                    setHomeAsUpIndicator(R.mipmap.ic_back_new_white) //更改返回图标
                    tvMidTitle?.setTextColor(ContextCompat.getColor(context,R.color.white))
                    tvRightText?.setTextColor(ContextCompat.getColor(context,R.color.white))
                }
                BarUtils.setStatusBarLightMode(context as Activity,lightMode)
            }
        }


    }

    fun setToolbarBackground(context: Context, @ColorRes colorRes: Int) {
        toolbar?.setBackgroundColor(ContextCompat.getColor(context, colorRes))
    }

    fun setBackIcon() {
        toolbar
    }

    fun hideToolBar(){
        toolbar?.visibility = View.GONE
    }

    fun setMidTitle(midTitle: String?) {
        tvMidTitle?.text = midTitle ?: ""
    }

    fun hideArrow() {
        (context as? AppCompatActivity)?.getSupportActionBar()?.setDisplayHomeAsUpEnabled(false)
    }

    fun setRightIcon(@DrawableRes resId: Int, listener: View.OnClickListener?) {
        ivRight?.apply {
            visiable()
            setImageResource(resId)
            setOnClickListener(listener)
        }
    }

    fun setRightText(string: String, listener: View.OnClickListener?) {
        tvRightText?.apply {
            text = string;
            setOnClickListener(listener)
        }
    }

}