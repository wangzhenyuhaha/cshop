package com.lingmiao.shop.business.goods.view

import android.annotation.SuppressLint
import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import com.blankj.utilcode.util.ToastUtils
import com.lingmiao.shop.R
import com.lingmiao.shop.business.goods.api.bean.SpecValueVO
import com.google.android.material.internal.FlowLayout
import com.james.common.utils.exts.removeIf1
import com.james.common.utils.exts.singleClick

/**
 * Author : Elson
 * Date   : 2020/7/31
 * Desc   : 规格值展示
 */
@SuppressLint("RestrictedApi")
class SpecFlowLayout @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : FlowLayout(context, attrs, defStyleAttr) {

    private var specKeyID: String? = null
    private val specValues: MutableList<SpecValueVO> = mutableListOf()
    private val inflate: LayoutInflater = LayoutInflater.from(context)

    var clickAddCallback: ((String) -> Unit)? = null
    var clickDeleteCallback: ((String?) -> Unit)? = null

    init {
        createAddItem()
    }

    fun getSpecValueList(): List<SpecValueVO> {
        return specValues
    }

    fun addSpecValues(specKeyID: String?, valueList: List<SpecValueVO>?) {
        this.specKeyID = specKeyID
        valueList?.apply {
            specValues.addAll(this)
            this.forEach {
                createSpecValueItem(it)
            }
        }
    }

    private fun createSpecValueItem(valueVO: SpecValueVO) {
        val view = inflate.inflate(R.layout.goods_view_spec_setting_value_item, this, false)
        view.findViewById<TextView>(R.id.specValueTv).text = valueVO.specValue
        view.findViewById<ImageView>(R.id.deleteIv).singleClick {
            removeChildView(view)
            clickDeleteCallback?.invoke(view.tag as String?)
        }
        view.tag = valueVO.specValueId
        addView(view, childCount - 1)
    }


    private fun createAddItem() {
        val view = inflate.inflate(R.layout.goods_view_spec_setting_value_add, this, false)
        view.singleClick {
            specKeyID?.apply {
                clickAddCallback?.invoke(this)
            }
//            ToastUtils.showShort("添加按钮被点击了")
        }
        addView(view)
    }

    private fun removeChildView(view: View) {
        ToastUtils.showShort("点击删除")
        specValues.removeIf1 { it.specValueId == view.tag }
        this.removeView(view)
    }
}