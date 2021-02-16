package com.lingmiao.shop.business.tools.pop

import android.content.Context
import android.view.Gravity
import android.view.View
import android.view.animation.Animation
import android.widget.ImageView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.lingmiao.shop.R
import com.lingmiao.shop.util.hideYTranslateAnim
import com.lingmiao.shop.util.showYTranslateAnim
import razerdp.basepopup.BaseLazyPopupWindow


/**
 * 日期类开
 */
class DayPop(context: Context, var data: List<String>) :
    BaseLazyPopupWindow(context) {

    private var listener: ((String, Int) -> Unit)? = null

    fun setOnClickListener(listener: ((String, Int) -> Unit)?) {
        this.listener = listener
    }

    override fun onCreateContentView(): View {
        return createPopupById(R.layout.wallet_pop_bank_card_list)
    }

    override fun onViewCreated(contentView: View) {
        popupGravity = Gravity.BOTTOM or Gravity.CENTER_VERTICAL
        val rvLogisticsCompanyList =
            contentView.findViewById<RecyclerView>(R.id.rv_bank_card)
        rvLogisticsCompanyList.layoutManager = LinearLayoutManager(context)
        val adapter = object :
            BaseQuickAdapter<String, BaseViewHolder>(R.layout.order_adapter_logistics_company) {
            override fun convert(helper: BaseViewHolder, item: String?) {
                helper.setText(R.id.tvLogisticsCompanyName, item)
            }

        }
        rvLogisticsCompanyList.adapter = adapter
        adapter.setNewData(data)
        adapter.setOnItemClickListener { _, _, position ->
            run {
                listener?.invoke(data[position], position);
                dismiss()
            }
        }

        contentView.findViewById<ImageView>(R.id.iv_bank_card_close).setOnClickListener { dismiss() }
    }

    override fun onCreateShowAnimation(): Animation? {
        return showYTranslateAnim(300)
    }

    override fun onCreateDismissAnimation(): Animation? {
        return hideYTranslateAnim(300)
    }
}