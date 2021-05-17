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

/**
 * Date   : 2020/7/31
 * Desc   : flow展示
 */
@SuppressLint("RestrictedApi")
abstract class AbsFlowLayout<T : FlowViewVo> @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : FlowLayout(context, attrs, defStyleAttr) {

    private var specKeyID: String? = null
    private val specValues: MutableList<T> = mutableListOf()
    private val inflate: LayoutInflater = LayoutInflater.from(context)

    var clickAddCallback: ((String) -> Unit)? = null
    var clickDeleteCallback: ((String?) -> Unit)? = null

    init {
        //createAddItem()
    }

    fun getSpecValueList(): List<T> {
        return specValues
    }

    fun addSpecValues(specKeyID: String?, valueList: List<T>?) {
        this.specKeyID = specKeyID
        valueList?.apply {
            specValues.addAll(this)
            this.forEach {
                createSpecValueItem(it)
            }
        }
    }

    private fun createSpecValueItem(valueVO: T) {
        val view = inflate.inflate(R.layout.goods_view_spec_setting_value_item, this, false)
        view.findViewById<TextView>(R.id.specValueTv).text = valueVO.getFName()
        view.findViewById<ImageView>(R.id.deleteIv).singleClick {
            DialogUtils.showDialog(context as Activity,
                "删除", "确定要删除？",
                "取消", "确定删除",
                null, View.OnClickListener {
                    removeChildView(view)
                    clickDeleteCallback?.invoke(view.tag as String?)
                })
        }
        view.tag = valueVO.getFId()
        addView(view, childCount - 1)
    }


    /**
     * 默认新增
     */
    private fun createAddItem() {
        val view = inflate.inflate(R.layout.goods_view_spec_setting_value_add, this, false)
        view.singleClick {
            specKeyID?.apply {
                clickAddCallback?.invoke(this)
            }
        }
        addView(view)
    }

    private fun removeChildView(view: View) {
        specValues.removeIf1 { it.getFId() == view.tag }
        this.removeView(view)
    }
}