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
class CateMenuPop(context: Context, var flags: Int = TYPE_EDIT) :
    BaseLazyPopupWindow(context) {

    companion object {
        const val TYPE_GOODS_INFO = 0x001       //商品信息
        const val TYPE_SPEC = 0x002             //规格
        const val TYPE_EDIT = 0x003             //编辑
        const val TYPE_CHILDREN = 0x004         //新增二级
        const val TYPE_DELETE = 0x005;          //删除
    }

    init {
        // 透明背景
        setBackgroundColor(0)
    }

    private var goodsInfoTv: TextView? = null
    private var specTv: TextView? = null
    private var cateEditTv: TextView? = null
    private var addChildrenTv: TextView? = null
    private var deleteTv: TextView? = null

    private var listener: ((Int) -> Unit)? = null

    fun setOnClickListener(listener: ((Int) -> Unit)?) {
        this.listener = listener
    }

    override fun onCreateContentView(): View {
        return createPopupById(R.layout.goods_pop_cate_menu)
    }

    override fun onViewCreated(contentView: View) {
        // 显示的位置
        setPopupGravity(Gravity.LEFT or Gravity.CENTER_VERTICAL)
        // 商品信息
        goodsInfoTv = contentView.findViewById<TextView>(R.id.goodsInfoTv).apply {
            show(flags and TYPE_GOODS_INFO != 0)
            setOnClickListener {
                listener?.invoke(TYPE_GOODS_INFO)
                dismiss()
            }
        }
        // 规格
        specTv = contentView.findViewById<TextView>(R.id.specTv).apply {
            show(flags and TYPE_SPEC != 0)
            setOnClickListener {
                listener?.invoke(TYPE_SPEC)
                dismiss()
            }
        }
        // 编辑
        cateEditTv = contentView.findViewById<TextView>(R.id.cateEditTv).apply {
            show(flags and TYPE_EDIT != 0)
            setOnClickListener {
                listener?.invoke(TYPE_EDIT)
                dismiss()
            }
        }

        // 新增二级
        addChildrenTv = contentView.findViewById<TextView>(R.id.addChildrenTv).apply {
            show(flags and TYPE_CHILDREN != 0)
            setOnClickListener {
                listener?.invoke(TYPE_CHILDREN)
                dismiss()
            }
        }

        // 删除
        deleteTv = contentView.findViewById<TextView>(R.id.deleteTv).apply {
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