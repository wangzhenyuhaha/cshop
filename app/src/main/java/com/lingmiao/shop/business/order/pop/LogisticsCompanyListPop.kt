package com.lingmiao.shop.business.order.pop

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
import com.lingmiao.shop.business.order.bean.LogisticsCompanyItem
import com.lingmiao.shop.util.hideYTranslateAnim
import com.lingmiao.shop.util.showYTranslateAnim
import razerdp.basepopup.BaseLazyPopupWindow


/**
 * 快递公司列表
 */
class LogisticsCompanyListPop(context: Context, var data: List<LogisticsCompanyItem>) :
    BaseLazyPopupWindow(context) {

    private var listener: ((LogisticsCompanyItem) -> Unit)? = null

    fun setOnClickListener(listener: ((LogisticsCompanyItem) -> Unit)?) {
        this.listener = listener
    }

    override fun onCreateContentView(): View {
        return createPopupById(R.layout.order_pop_logistics_company_list)
    }

    override fun onViewCreated(contentView: View) {
        popupGravity = Gravity.BOTTOM or Gravity.CENTER_VERTICAL
        val rvLogisticsCompanyList =
            contentView.findViewById<RecyclerView>(R.id.rvLogisticsCompanyList)
        rvLogisticsCompanyList.layoutManager = LinearLayoutManager(context)
        val adapter = object :
            BaseQuickAdapter<LogisticsCompanyItem, BaseViewHolder>(R.layout.order_adapter_logistics_company) {
            override fun convert(helper: BaseViewHolder, item: LogisticsCompanyItem?) {
                helper.setText(R.id.tvLogisticsCompanyName, item?.logisticsCompanyDo?.name)
            }

        }
        rvLogisticsCompanyList.adapter = adapter
        adapter.setNewData(data)
        adapter.setOnItemClickListener { _, _, position ->
            run {
                listener?.invoke(data[position])
                dismiss()
            }
        }

        contentView.findViewById<ImageView>(R.id.ivLogisticsCompanyListClose).setOnClickListener { dismiss() }
    }

    override fun onCreateShowAnimation(): Animation? {
        return showYTranslateAnim(300)
    }

    override fun onCreateDismissAnimation(): Animation? {
        return hideYTranslateAnim(300)
    }
}