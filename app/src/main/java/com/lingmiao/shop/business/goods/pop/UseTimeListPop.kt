package com.lingmiao.shop.business.goods.pop

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
import com.lingmiao.shop.business.goods.api.bean.MultiPickerItemBean
import com.lingmiao.shop.util.hideYTranslateAnim
import com.lingmiao.shop.util.showYTranslateAnim
import razerdp.basepopup.BaseLazyPopupWindow


/**
 * 使用时间列表
 */
class UseTimeListPop(context: Context, var data: List<MultiPickerItemBean>) :
    BaseLazyPopupWindow(context) {

    var listener: ((List<MultiPickerItemBean>) -> Unit)? = null

    fun setOnClickListener(listener: ((List<MultiPickerItemBean>) -> Unit)?) {
        this.listener = listener
    }

    override fun onCreateContentView(): View {
        return createPopupById(R.layout.goods_pop_use_time_list)
    }

    override fun onViewCreated(contentView: View) {
        if(data?.get(0)?.isChecked == true) {
            data?.forEachIndexed { index, item ->
                item?.isChecked = true;
            }
        }
        popupGravity = Gravity.BOTTOM or Gravity.CENTER_VERTICAL
        val rvLogisticsCompanyList =
            contentView.findViewById<RecyclerView>(R.id.rvUseTimeList)
        rvLogisticsCompanyList.layoutManager = LinearLayoutManager(context)
        val adapter = object :
            BaseQuickAdapter<MultiPickerItemBean, BaseViewHolder>(R.layout.goods_adapter_use_time) {
            override fun convert(helper: BaseViewHolder, item: MultiPickerItemBean?) {
                helper.setText(R.id.tvMultiName, item?.name)
                helper.setChecked(R.id.rbMultiCheck, item?.isChecked ?: false)
                helper.addOnClickListener(R.id.rbMultiCheck)
            }
        }
        rvLogisticsCompanyList.adapter = adapter
        adapter.setNewData(data)
        adapter.setOnItemChildClickListener { adapter, view, position ->
            if(view.id == R.id.rbMultiCheck) {
                run {
                    if(position == 0) {
                        if(data[position].isChecked == true) {
                            data?.forEachIndexed { index, item ->
                                item?.isChecked = false;
                            }
                        } else {
                            data?.forEachIndexed { index, item ->
                                item?.isChecked = true;
                            }
                        }
                    } else {
                        data[position].isChecked = !(data[position].isChecked?:false);
                    }

                    adapter.notifyDataSetChanged();
                }
            }

        }
        contentView.findViewById<TextView>(R.id.useTimeConfirmTv).setOnClickListener {
            var list : List<MultiPickerItemBean> ? = null;
            if(data?.get(0)?.isChecked == true) {
                list = data?.filterIndexed { index, item -> index > 0 && item?.isChecked == true };
            } else {
                list = data?.filter { it?.isChecked == true };
            }
            if(list?.size > 0) {
                listener?.invoke(list)
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