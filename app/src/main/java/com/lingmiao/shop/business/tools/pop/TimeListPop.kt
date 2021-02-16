package com.lingmiao.shop.business.tools.pop

import android.content.Context
import android.view.Gravity
import android.view.View
import android.view.animation.Animation
import android.widget.ImageView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.lingmiao.shop.MyApp
import com.lingmiao.shop.R
import com.lingmiao.shop.business.tools.bean.TimeValue
import com.lingmiao.shop.util.hideYTranslateAnim
import com.lingmiao.shop.util.showYTranslateAnim
import razerdp.basepopup.BaseLazyPopupWindow


/**
 * 快递公司列表
 */
class TimeListPop(context: Context, var data: List<TimeValue>) :
    BaseLazyPopupWindow(context) {

    private lateinit var rvLogisticsCompanyList : RecyclerView;
    private lateinit var adapter : BaseQuickAdapter<TimeValue, BaseViewHolder>;
    private var listener: ((TimeValue) -> Unit)? = null
    private var type : Boolean = false;
    private var mPosition : Int = -1;

    fun  shiftStartTime(position : Int) {
        type = true;
        mPosition = position;
    }

    fun shiftEndTime(position: Int) {
        type = false;
        mPosition = position;
    }

    fun setOnClickListener(listener: ((TimeValue) -> Unit)?) {
        this.listener = listener
    }

    override fun onCreateContentView(): View {
        return createPopupById(R.layout.tools_pop_time_list)
    }

    fun isStartEnd() : Boolean {
        return type;
    }

    override fun onViewCreated(contentView: View) {
        popupGravity = Gravity.BOTTOM or Gravity.CENTER_VERTICAL
        rvLogisticsCompanyList =
            contentView.findViewById<RecyclerView>(R.id.rvTimeList)
        rvLogisticsCompanyList.layoutManager = LinearLayoutManager(context)
        adapter = object :
            BaseQuickAdapter<TimeValue, BaseViewHolder>(R.layout.tools_adapter_time_value) {
            override fun convert(helper: BaseViewHolder, item: TimeValue?) {
                if(type) {
                    if(mPosition > item?.value ?: 0) {
                        helper.setEnabled(R.id.tvTimeName, true);
                        helper.setTextColor(R.id.tvTimeName, ContextCompat.getColor(MyApp.getInstance(), R.color.color_333333));
                    } else {
                        helper.setEnabled(R.id.tvTimeName, false);
                        helper.setTextColor(R.id.tvTimeName, ContextCompat.getColor(MyApp.getInstance(), R.color.color_999999));
                    }
                } else {
                    if(mPosition < item?.value ?:0) {
                        helper.setEnabled(R.id.tvTimeName, true);
                        helper.setTextColor(R.id.tvTimeName, ContextCompat.getColor(MyApp.getInstance(), R.color.color_333333));
                    } else {
                        helper.setEnabled(R.id.tvTimeName, false);
                        helper.setTextColor(R.id.tvTimeName, ContextCompat.getColor(MyApp.getInstance(), R.color.color_999999));
                    }
                }
                helper.setEnabled(R.id.tvTimeName, if(type) mPosition < item?.value ?: 0 else mPosition > item?.value ?:0);
                helper.setText(R.id.tvTimeName, item?.name);
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

        contentView.findViewById<ImageView>(R.id.ivTimeListClose).setOnClickListener { dismiss() }
    }

    override fun onCreateShowAnimation(): Animation? {
        return showYTranslateAnim(300)
    }

    override fun onCreateDismissAnimation(): Animation? {
        return hideYTranslateAnim(300)
    }
}