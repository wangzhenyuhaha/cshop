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
 * Desc   : 常用菜单，二级菜单的更多选项
 */
class ChildrenGoodsMenuPop(context: Context, var flags: Int = TYPE_PRICE) :
    BaseLazyPopupWindow(context) {

    companion object {
        const val TYPE_PRICE = 0x001            //库存/价格
        const val TYPE_EDIT = 0x002;            //编辑
        const val TYPE_DELETE = 0x004;          //删除
    }

    init {
        // 透明背景
        setBackgroundColor(0)
    }

    private var childrenPriceTv: TextView? = null
    private var childrenDeleteTv : TextView? = null
    private var childrenEditTv : TextView? = null

    private var listener: ((Int) -> Unit)? = null

    fun setOnClickListener(listener: ((Int) -> Unit)?) {
        this.listener = listener
    }

    override fun onCreateContentView(): View {
        return createPopupById(R.layout.goods_pop_children_goods_menu)
    }

    override fun onViewCreated(contentView: View) {
        // 显示的位置
        setPopupGravity(Gravity.LEFT or Gravity.CENTER_VERTICAL)

        // 编辑
        childrenPriceTv = contentView.findViewById<TextView>(R.id.childrenPriceTv).apply {
            show(flags and TYPE_PRICE != 0)
            setOnClickListener {
                listener?.invoke(TYPE_PRICE)
                dismiss()
            }
        }
        childrenDeleteTv = contentView.findViewById<TextView>(R.id.childrenDeleteTv).apply {
            show(flags and TYPE_DELETE != 0)
            setOnClickListener {
                listener?.invoke(TYPE_DELETE)
                dismiss()
            }
        }

        childrenEditTv = contentView.findViewById<TextView>(R.id.childrenEditTv).apply {
            show(flags and TYPE_EDIT != 0)
            setOnClickListener {
                listener?.invoke(TYPE_EDIT)
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