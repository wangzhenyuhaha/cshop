package com.lingmiao.shop.business.goods.pop

import android.content.Context
import android.view.Gravity
import android.view.View
import android.view.animation.Animation
import android.widget.TextView
import com.lingmiao.shop.R
import com.lingmiao.shop.util.hideXTranslateAnim
import com.lingmiao.shop.util.showXTranslateAnim
import com.james.common.utils.exts.show
import razerdp.basepopup.BaseLazyPopupWindow


/**
 * Author : Elson
 * Date   : 2020/7/12
 * Desc   : 分类编辑
 */
class ChildrenMenuPop(context: Context, var flags: Int = TYPE_EDIT) :

    BaseLazyPopupWindow(context) {

    companion object {
        const val TYPE_EDIT = 0x001             //编辑
        const val TYPE_DELETE = 0x002;          //删除
    }

    init {
        // 透明背景
        setBackgroundColor(0)
    }

    private var cateEditTv: TextView? = null
    private var deleteTv: TextView? = null

    private var listener: ((Int) -> Unit)? = null

    fun setOnClickListener(listener: ((Int) -> Unit)?) {
        this.listener = listener
    }

    override fun onCreateContentView(): View {
        return createPopupById(R.layout.goods_pop_children_menu)
    }

    override fun onViewCreated(contentView: View) {
        // 显示的位置
        setPopupGravity(Gravity.LEFT or Gravity.CENTER_VERTICAL)

        // 编辑
        cateEditTv = contentView.findViewById<TextView>(R.id.childrenEditTv).apply {
            show(flags and TYPE_EDIT != 0)
            setOnClickListener {
                listener?.invoke(TYPE_EDIT)
                dismiss()
            }
        }

        // 删除
        deleteTv = contentView.findViewById<TextView>(R.id.childrenDeleteTv).apply {
            show(flags and TYPE_DELETE != 0)
            setOnClickListener {
                listener?.invoke(TYPE_DELETE)
                dismiss()
            }
        }

    }

    override fun onCreateShowAnimation(): Animation? {
        return showXTranslateAnim(300)
    }

    override fun onCreateDismissAnimation(): Animation? {
        return hideXTranslateAnim(300)
    }
}