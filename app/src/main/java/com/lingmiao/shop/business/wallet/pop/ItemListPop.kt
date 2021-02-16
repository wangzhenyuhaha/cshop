package com.lingmiao.shop.business.wallet.pop

import android.content.Context
import android.view.Gravity
import android.view.View
import android.view.animation.Animation
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.lingmiao.shop.R
import com.lingmiao.shop.util.hideYTranslateAnim
import com.lingmiao.shop.util.showYTranslateAnim
import razerdp.basepopup.BaseLazyPopupWindow


/**
 * 开户行列表
 */
class ItemListPop(context: Context, var data: List<String>) :
    BaseLazyPopupWindow(context) {

    private var listener: ((Int, String) -> Unit)? = null

    private var title: String ? = "请选择"

    fun setOnClickListener(listener: ((Int, String) -> Unit)?) {
        this.listener = listener
    }

    override fun onCreateContentView(): View {
        return createPopupById(R.layout.wallet_pop_item_list)
    }

    override fun onViewCreated(contentView: View) {
        popupGravity = Gravity.BOTTOM or Gravity.CENTER_VERTICAL
        val rvLogisticsCompanyList =
            contentView.findViewById<RecyclerView>(R.id.rv_item_list)
        rvLogisticsCompanyList.layoutManager = LinearLayoutManager(context)
        val adapter = object :
            BaseQuickAdapter<String, BaseViewHolder>(R.layout.wallet_adapter_item) {
            override fun convert(helper: BaseViewHolder, item: String?) {
                helper.setText(R.id.tvItemName, item)
                helper.setVisible(R.id.v_item_line, helper.adapterPosition < itemCount-1)
            }

        }
        rvLogisticsCompanyList.adapter = adapter
        adapter.setNewData(data)
        adapter.setOnItemClickListener { _, _, position ->
            run {
                listener?.invoke(position, data[position])
                dismiss()
            }
        }

        contentView.findViewById<TextView>(R.id.tv_item_pop_hint).setText(title);

        contentView.findViewById<ImageView>(R.id.iv_item_close).setOnClickListener { dismiss() }
    }

//    override fun onCreateShowAnimation(): Animation? {
//        return showYTranslateAnim(300)
//    }
//
//    override fun onCreateDismissAnimation(): Animation? {
//        return hideYTranslateAnim(300)
//    }
}