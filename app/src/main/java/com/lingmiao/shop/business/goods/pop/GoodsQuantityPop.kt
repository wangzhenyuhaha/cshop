package com.lingmiao.shop.business.goods.pop

import android.content.Context
import android.view.Gravity
import android.view.View
import android.view.animation.Animation
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import com.lingmiao.shop.R
import com.lingmiao.shop.util.hideYTranslateAnim
import com.lingmiao.shop.util.showYTranslateAnim
import razerdp.basepopup.BasePopupWindow

/**
 * Author : Elson
 * Date   : 2020/7/15
 * Desc   : 商品库存
 */
class GoodsQuantityPop(context: Context) : BasePopupWindow(context) {

    private var closeIv: ImageView? = null
    private var inputEdt: EditText? = null
    private var confirmTv: TextView? = null

    private var listener: ((String?) -> Unit)? = null

    fun setConfirmListener(listener: ((String?) -> Unit)?) {
        this.listener = listener
    }

    fun setQuantity(quantity: String?) {
        inputEdt?.setText(quantity)
    }

    fun clearInputEdt() {
        this.inputEdt?.setText("")
    }

    override fun onCreateContentView(): View {
        return createPopupById(R.layout.goods_pop_quantity)
    }

    override fun onViewCreated(contentView: View) {
        setPopupGravity(Gravity.BOTTOM or Gravity.CENTER_VERTICAL)
        super.onViewCreated(contentView)
        closeIv = contentView.findViewById(R.id.closeIv)
        inputEdt = contentView.findViewById(R.id.inputEdt)
        confirmTv = contentView.findViewById(R.id.confirmTv)
        confirmTv?.setOnClickListener {
            listener?.invoke(inputEdt?.text.toString())
        }
        closeIv?.setOnClickListener {
            dismiss()
        }
    }

    override fun onCreateShowAnimation(): Animation? {
        return showYTranslateAnim(300)
    }

    override fun getDismissAnimation(): Animation? {
        return hideYTranslateAnim(300)
    }

}