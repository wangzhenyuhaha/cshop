package com.lingmiao.shop.business.goods.pop

import android.content.Context
import android.view.Gravity
import android.view.View
import android.view.animation.Animation
import android.widget.TextView
import com.lingmiao.shop.R
import com.lingmiao.shop.util.hideYTranslateAnimOfBottomToTop
import com.lingmiao.shop.util.showYTranslateAnimOfTopToBottom
import com.james.common.utils.exts.show
import razerdp.basepopup.BaseLazyPopupWindow


/**
 * Desc   : 商品搜索POP
 */
class StatusMenuPop(context: Context, var flags: Int = TYPE_GOODS_OWNER) :
    BaseLazyPopupWindow(context) {

    companion object {
        const val TYPE_GOODS_OWNER = 0x001 //供应商
        const val TYPE_GOODS_NAME = 0x002   //商品名称
    }

    init {
        // 透明背景
        setBackgroundColor(0)
    }

    private var goodsNameTv: TextView? = null
    private var goodsOwnerTv: TextView? = null

    private var listener: ((Int) -> Unit)? = null

    fun setOnClickListener(listener: ((Int) -> Unit)?) {
        this.listener = listener
    }

    override fun onCreateContentView(): View {
        return createPopupById(R.layout.goods_pop_search)
    }

    override fun onViewCreated(contentView: View) {
        // 显示的位置
        setPopupGravity(Gravity.BOTTOM or Gravity.CENTER_VERTICAL)
        // 商品名称
        goodsNameTv = contentView.findViewById<TextView>(R.id.goodsSearchNameTv).apply {
            show(flags and TYPE_GOODS_NAME != 0)
            setOnClickListener {
                listener?.invoke(TYPE_GOODS_NAME)
                dismiss()
            }
        }
        // 供应商
        goodsOwnerTv = contentView.findViewById<TextView>(R.id.goodsSearchOwnerTv).apply {
            show(flags and TYPE_GOODS_OWNER != 0)
            setOnClickListener {
                listener?.invoke(TYPE_GOODS_OWNER)
                dismiss()
            }
        }
    }

    override fun onCreateShowAnimation(): Animation? {
        return showYTranslateAnimOfTopToBottom(300)
    }

    override fun onCreateDismissAnimation(): Animation? {
        return hideYTranslateAnimOfBottomToTop(300)
    }
}