package com.lingmiao.shop.business.goods.view

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import com.blankj.utilcode.util.ToastUtils
import com.lingmiao.shop.R
import com.google.android.material.internal.FlowLayout
import com.james.common.utils.DialogUtils
import com.james.common.utils.exts.removeIf1
import com.james.common.utils.exts.singleClick
import com.lingmiao.shop.business.goods.api.bean.CategoryVO
import com.lingmiao.shop.business.goods.api.bean.GoodsParamVo
import com.lingmiao.shop.business.goods.api.bean.SpecValueVO

/**
 * Author : Elson
 * Date   : 2020/7/31
 * Desc   : 规格值展示
 */
@SuppressLint("RestrictedApi")
class InfoFlowLayout @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : AbsFlowLayout<GoodsParamVo>(context, attrs, defStyleAttr) {

}