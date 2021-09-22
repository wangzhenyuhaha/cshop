package com.lingmiao.shop.business.me.pop

import android.content.Context
import android.view.Gravity
import android.view.View
import android.view.animation.Animation
import android.widget.TextView
import com.lingmiao.shop.R
import com.lingmiao.shop.business.goods.api.request.QuantityPriceRequest
import com.lingmiao.shop.util.hideYTranslateAnim
import com.lingmiao.shop.util.showYTranslateAnim
import razerdp.basepopup.BasePopupWindow

class ShopQRCodePop(context: Context) : BasePopupWindow(context) {

    //save
    private var saveTv: TextView? = null

    //share
    private var shareTv: TextView? = null

    //watch
    private var watchTv: TextView? = null


    //save
    private var saveListener: (() -> Unit)? = null

    //share
    private var shareListener: (() -> Unit)? = null

    //watch
    private var watchListener: (() -> Unit)? = null


    fun setSaveListener(listener: (() -> Unit)?) {
        this.saveListener = listener
    }

    fun setShareListener(listener: (() -> Unit)?) {
        this.shareListener = listener
    }

    fun setWatchListener(listener: (() -> Unit)?) {
        this.watchListener = listener
    }

    override fun onCreateContentView(): View = createPopupById(R.layout.shop_pop_q_r_code)

    override fun onViewCreated(contentView: View) {
        popupGravity = Gravity.BOTTOM or Gravity.CENTER_VERTICAL
        super.onViewCreated(contentView)

        saveTv = contentView.findViewById(R.id.shop_pop_q_r_code_save)
        shareTv = contentView.findViewById(R.id.shop_pop_q_r_code_share)
        watchTv = contentView.findViewById(R.id.shop_pop_q_r_code_watch_photo)

        saveTv?.setOnClickListener {
            saveListener?.invoke()
        }
        shareTv?.setOnClickListener { shareListener?.invoke() }
        watchTv?.setOnClickListener { watchListener?.invoke() }
    }

    override fun onCreateShowAnimation(): Animation {
        return showYTranslateAnim(300)
    }

    override fun getDismissAnimation(): Animation {
        return hideYTranslateAnim(300)
    }
}