package com.lingmiao.shop.business.goods.pop

import android.content.Context
import android.view.Gravity
import android.view.View
import android.view.animation.Animation
import android.widget.EditText
import android.widget.ImageView
import android.widget.LinearLayout
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
class GoodsRebatePop(context: Context) : BasePopupWindow(context) {

    private var closeIv: ImageView? = null
    private var etFirstRebate : EditText? = null
    private var etSecondRebate : EditText?= null;
    private var etInviteRebate : EditText?= null;
    private var confirmTv: TextView? = null
    private var etGoodsCount : TextView?= null;
    private var llBatchGoodsCount : LinearLayout ?= null;

    private var listener: ((String?, String?, String?) -> Unit)? = null

    fun setConfirmListener(listener: ((String?, String?, String?) -> Unit)?) {
        this.listener = listener
    }

    fun setFirstRebate(rebate: String?) {
        etFirstRebate?.setText(rebate)
    }

    fun setSecondRebate(rebate: String?) {
        etSecondRebate?.setText(rebate)
    }

    fun setInviteRebate(rebate: String?) {
        etInviteRebate?.setText(rebate);
    }

    fun setGoodsCount(count : Int?) {
        llBatchGoodsCount?.visibility = View.VISIBLE;
        etGoodsCount?.setText(String.format("%s", count));
    }

    fun clearInputEdt() {
        this.etFirstRebate?.setText("")
        this.etSecondRebate?.setText("")
        this.etInviteRebate?.setText("")
    }

    override fun onCreateContentView(): View {
        return createPopupById(R.layout.goods_pop_rebate)
    }

    override fun onViewCreated(contentView: View) {
        popupGravity = Gravity.BOTTOM or Gravity.CENTER_VERTICAL
        super.onViewCreated(contentView)
        closeIv = contentView.findViewById(R.id.closeIv)
        etGoodsCount = contentView.findViewById(R.id.tv_goods_count);
        llBatchGoodsCount = contentView.findViewById(R.id.ll_goods_batch_count);
        etFirstRebate = contentView.findViewById(R.id.et_goods_first_rebate)
        etSecondRebate = contentView.findViewById(R.id.et_goods_second_rebate)
        etInviteRebate = contentView.findViewById(R.id.et_goods_invite_rebate)
        confirmTv = contentView.findViewById(R.id.confirmTv)
        confirmTv?.setOnClickListener {
            listener?.invoke(etFirstRebate?.text.toString(), etSecondRebate?.text.toString(), etInviteRebate?.text.toString())
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