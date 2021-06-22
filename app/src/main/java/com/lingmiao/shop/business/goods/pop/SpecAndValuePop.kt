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
 * Date   : 2020/7/15
 * Desc   : 规格设置
 */
class SpecAndValuePop(context: Context) : BasePopupWindow(context) {

    private var closeIv: ImageView? = null
    private var inputKeyEdt: EditText? = null
    private var inputValueEdt: EditText? = null
    private var confirmTv: TextView? = null

    private var listener: ((String?, String?) -> Unit)? = null

    override fun onCreateContentView(): View {
        return createPopupById(R.layout.goods_pop_spec_value)
    }

    override fun onViewCreated(contentView: View) {
        setPopupGravity(Gravity.BOTTOM or Gravity.CENTER_VERTICAL)
        super.onViewCreated(contentView)
        closeIv = contentView.findViewById(R.id.closeIv)
        inputKeyEdt = contentView.findViewById(R.id.inputKeyEdt)
        inputValueEdt = contentView.findViewById(R.id.inputValueEdt)
        confirmTv = contentView.findViewById(R.id.confirmSpecTv)
        confirmTv?.setOnClickListener {
            listener?.invoke(inputKeyEdt?.text.toString(), inputValueEdt?.text.toString())
            dismiss()
        }
        closeIv?.setOnClickListener {
            dismiss()
        }
    }

    fun setConfirmListener(listener: ((String?, String?) -> Unit)?) {
        this.listener = listener
    }

    fun setKey(quantity: String?) {
        inputKeyEdt?.setText(quantity)
    }

    fun clearKey() {
        this.inputKeyEdt?.setText("")
    }

    fun setValue(quantity: String?) {
        inputValueEdt?.setText(quantity)
    }

    fun clearValue() {
        this.inputValueEdt?.setText("")
    }

    override fun onCreateShowAnimation(): Animation? {
        return showYTranslateAnim(300)
    }

    override fun getDismissAnimation(): Animation? {
        return hideYTranslateAnim(300)
    }

}