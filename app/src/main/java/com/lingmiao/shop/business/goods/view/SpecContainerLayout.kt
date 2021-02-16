package com.lingmiao.shop.business.goods.view

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.TextView
import com.lingmiao.shop.R
import com.lingmiao.shop.business.goods.SpecKeyActivity
import com.lingmiao.shop.business.goods.api.bean.SpecKeyVO
import com.lingmiao.shop.business.goods.api.bean.SpecValueVO
import com.james.common.utils.exts.removeIf1
import com.james.common.utils.exts.show
import com.james.common.utils.exts.singleClick
import com.james.common.utils.exts.visiable
import com.james.common.view.ILinearLayout
import kotlinx.android.synthetic.main.goods_view_spec_setting_item.view.*

/**
 * Author : Elson
 * Date   : 2020/7/31
 * Desc   : 存放规格Item的容器
 */
class SpecContainerLayout @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : ILinearLayout(context, attrs, defStyleAttr) {

    /**
     * 添加规格值
     */
    var addSpecValueListener: ((String) -> Unit)? = null
    /**
     * 删除规格值
     */
    var deleteSpecValueListener: ((String?) -> Unit)? = null
    /**
     * 删除规格名
     */
    var deleteSpecItemListener: (() -> Unit)? = null
    /**
     * 关联添加规格按钮
     */
    private var addSpecBtn: View? = null
    /**
     * 缓存规格Item
     */
    private var specKeyViews: MutableList<View> = mutableListOf()
    private var specKeyList: MutableList<SpecKeyVO> = mutableListOf()

    private val inflate: LayoutInflater = LayoutInflater.from(context)

    fun getSpecList(): List<SpecKeyVO> {
        return specKeyList
    }

    fun getSpecKeyAndValueList():  List<SpecKeyVO> {
        specKeyList.forEachIndexed { index, specKeyVO ->
            specKeyVO.valueList = specKeyViews[index].specFlowLayout.getSpecValueList()
        }
        return specKeyList
    }

    fun bindAddSpecBtn(view: View?) {
        addSpecBtn = view
    }

    fun addSpecItems(specKeys: List<SpecKeyVO>?, showSpecValue: Boolean) {
        removeAllViews()
        if (specKeys.isNullOrEmpty()) {
            addSpecBtn?.visiable()
            return
        }
        // 移除不存在的
        specKeyViews.removeIf1 { view -> (-1 == specKeys.indexOfFirst { it.specId == view.tag }) }
        // 清除重新添加
        specKeyList.clear()
        specKeyList.addAll(specKeys)
        // 遍历
        specKeys.forEach { specKey ->
            val index = specKeyViews.indexOfFirst { specKey.specId == it.tag }
            if (index == -1) {
                createSpecItem(specKey, showSpecValue)
            } else {
                addView(specKeyViews[index])
            }
        }
        // 最多只有5
        addSpecBtn?.show(specKeyList.size < SpecKeyActivity.MAX_SPEC_SELECTED)
    }

    fun updateSpecValues(specKeyID: String, spceValueList: List<SpecValueVO>) {
        specKeyViews.forEach {
            if (it.tag == specKeyID) {
                it.specFlowLayout.addSpecValues(specKeyID, spceValueList)
            }
        }
    }

    /**
     * 创建规格Item
     */
    private fun createSpecItem(specKey: SpecKeyVO, showSpecValue: Boolean) {
        val view = inflate.inflate(R.layout.goods_view_spec_setting_item, this, false)
        view.findViewById<TextView>(R.id.specNameTv).text = "规格名：${specKey.specName}"
        view.findViewById<TextView>(R.id.deleteSpecTv).singleClick {
            removeChildItem(view)
            deleteSpecItemListener?.invoke()
        }
        view.findViewById<SpecFlowLayout>(R.id.specFlowLayout).let {
            it.clickAddCallback = this.addSpecValueListener
            it.clickDeleteCallback = this.deleteSpecValueListener
            it.addSpecValues(specKey.specId, if(showSpecValue) specKey.valueList else arrayListOf())
        }

        view.tag = specKey.specId
        this.addView(view)
        specKeyViews.add(view)
    }

    private fun removeChildItem(child: View) {
        val specId = child.tag as String
        specKeyList.removeIf1 { it.specId == specId }
        addSpecBtn?.visiable()
        this.removeView(child)
        specKeyViews.remove(child)
    }

}