package com.lingmiao.shop.business.goods.pop

import android.content.Context
import android.view.Gravity
import android.view.View
import android.view.animation.Animation
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.blankj.utilcode.util.ColorUtils
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.lingmiao.shop.R
import com.lingmiao.shop.business.goods.api.bean.GoodsUseExpireVo
import com.lingmiao.shop.util.hideYTranslateAnim
import com.lingmiao.shop.util.showYTranslateAnim
import razerdp.basepopup.BaseLazyPopupWindow


/**
 * 使用时间列表
 */
class ExpireListPop(context: Context,var value: String?, var data: List<GoodsUseExpireVo>?) :
    BaseLazyPopupWindow(context) {

    var listener: ((GoodsUseExpireVo) -> Unit)? = null

    private var checkedPosition : Int = -1;

    fun setOnClickListener(listener: ((GoodsUseExpireVo) -> Unit)?) {
        this.listener = listener
    }

    override fun onCreateContentView(): View {
        return createPopupById(R.layout.goods_pop_use_time_list)
    }

    fun getIndex() : Int {
        if(value?.isNotBlank() == true) {
            data?.forEachIndexed { index, item ->
                if(value == item?.value) {
                    return index;
                }
            }
        }
        return -1;
    }

    override fun onViewCreated(contentView: View) {
        checkedPosition = getIndex();

        popupGravity = Gravity.BOTTOM or Gravity.CENTER_VERTICAL
        val rvLogisticsCompanyList = contentView.findViewById<RecyclerView>(R.id.rvUseTimeList)
        rvLogisticsCompanyList.layoutManager = LinearLayoutManager(context)
        val adapter = object :
            BaseQuickAdapter<GoodsUseExpireVo, BaseViewHolder>(R.layout.goods_adapter_expire) {
            override fun convert(helper: BaseViewHolder, item: GoodsUseExpireVo?) {
                helper.setText(R.id.tvExpireName, item?.label)
                helper.setTextColor(R.id.tvExpireName,
                    ColorUtils.getColor(
                        if(checkedPosition == helper.adapterPosition) R.color.color_3870EA else R.color.color_333333
                    )
                );
            }

        }
        rvLogisticsCompanyList.adapter = adapter
        adapter.setNewData(data)
        adapter.setOnItemClickListener { _, _, position ->
            run {
                checkedPosition = position;
                adapter.notifyDataSetChanged();
            }
        }

        contentView.findViewById<TextView>(R.id.useTimeConfirmTv).setOnClickListener {
            if(checkedPosition > -1 && checkedPosition < adapter?.data?.size) {
                listener?.invoke(adapter?.data?.get(checkedPosition))
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