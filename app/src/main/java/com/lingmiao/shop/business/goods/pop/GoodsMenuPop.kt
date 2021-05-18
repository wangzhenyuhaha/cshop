package com.lingmiao.shop.business.goods.pop

import android.content.Context
import android.view.Gravity
import android.view.View
import android.view.animation.Animation
import android.view.animation.DecelerateInterpolator
import android.view.animation.TranslateAnimation
import android.widget.TextView
import com.lingmiao.shop.R
import com.lingmiao.shop.util.hideXTranslateAnim
import com.lingmiao.shop.util.showXTranslateAnim
import com.james.common.utils.exts.show
import razerdp.basepopup.BaseLazyPopupWindow


/**
 * Author : Elson
 * Date   : 2020/7/12
 * Desc   : 商品列表编辑
 */
class GoodsMenuPop(context: Context, var flags: Int = TYPE_EDIT) :
    BaseLazyPopupWindow(context) {

    companion object {
        const val TYPE_EDIT = 0x001     //编辑
        const val TYPE_DISABLE = 0x002  //下架
        const val TYPE_QUANTITY = 0x004 //库存
        const val TYPE_REBATE = 0x007   //佣金
        const val TYPE_ENABLE = 0x008   //上架
        const val TYPE_DELETE = 0x010   //删除
    }

    init {
        // 透明背景
        setBackgroundColor(0)
    }

    private var goodsEditTv: TextView? = null
    private var goodsDisableTv: TextView? = null
    private var goodsQuantityTv: TextView? = null
    private var goodsEnableTv: TextView? = null
    private var goodsDeleteTv: TextView? = null
    private var goodsRebateTv: TextView? = null

    private var listener: ((Int) -> Unit)? = null

    fun setOnClickListener(listener: ((Int) -> Unit)?) {
        this.listener = listener
    }

    override fun onCreateContentView(): View {
        return createPopupById(R.layout.goods_pop_menu)
    }

    override fun onViewCreated(contentView: View) {
        // 显示的位置
        setPopupGravity(Gravity.LEFT or Gravity.CENTER_VERTICAL)
        // 编辑
        goodsEditTv = contentView.findViewById<TextView>(R.id.goodsEditTv).apply {
            show(flags and TYPE_EDIT != 0)
            setOnClickListener {
                listener?.invoke(TYPE_EDIT)
                dismiss()
            }
        }
        // 下架
        goodsDisableTv = contentView.findViewById<TextView>(R.id.goodsDisableTv).apply {
            show(flags and TYPE_DISABLE != 0)
            setOnClickListener {
                listener?.invoke(TYPE_DISABLE)
                dismiss()
            }
        }
        // 库存
        goodsQuantityTv = contentView.findViewById<TextView>(R.id.goodsQuantityTv).apply {
            show(flags and TYPE_QUANTITY != 0)
            setOnClickListener {
                listener?.invoke(TYPE_QUANTITY)
                dismiss()
            }
        }
        // 上架
        goodsEnableTv = contentView.findViewById<TextView>(R.id.goodsEnableTv).apply {
            show(flags and TYPE_ENABLE != 0)
            setOnClickListener {
                listener?.invoke(TYPE_ENABLE)
                dismiss()
            }
        }
        // 分销
        goodsRebateTv = contentView.findViewById<TextView>(R.id.goodsRebateTv).apply {
            //show(flags and TYPE_REBATE != 0);
            setOnClickListener {
                listener?.invoke(TYPE_REBATE)
                dismiss()
            }
        }
        // 删除
        goodsDeleteTv = contentView.findViewById<TextView>(R.id.goodsDeleteTv).apply {
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