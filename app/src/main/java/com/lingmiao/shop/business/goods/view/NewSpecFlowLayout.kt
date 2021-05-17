package com.lingmiao.shop.business.goods.view

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.util.AttributeSet
import com.lingmiao.shop.business.goods.api.bean.GoodsSpecVo

/**
 * Author : Elson
 * Date   : 2020/7/31
 * Desc   : 规格值展示
 */
@SuppressLint("RestrictedApi")
class NewSpecFlowLayout @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : AbsFlowLayout<GoodsSpecVo>(context, attrs, defStyleAttr) {

}