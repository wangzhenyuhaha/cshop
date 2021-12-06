package com.lingmiao.shop.business.commonpop.pop

import android.content.Context
import android.view.Gravity
import android.view.View
import android.view.animation.Animation
import android.widget.CheckBox
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.blankj.utilcode.util.ColorUtils
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.lingmiao.shop.R
import com.lingmiao.shop.business.commonpop.bean.ItemData
import com.lingmiao.shop.util.hideYTranslateAnim
import com.lingmiao.shop.util.showYTranslateAnim
import razerdp.basepopup.BaseLazyPopupWindow


// value  固定传一个“”，    data二级分类的列表    title 标题
class AbsOneItemAllPop<T : ItemData>(
    context: Context,
    var value: String?,
    var data: List<T>?,
    var title: String?
) :
    BaseLazyPopupWindow(context) {

    var listener: ((List<T>) -> Unit)? = null

    //useless
    private var checkedPosition: Int = -1

    //是否可选
    var isMultiChecked: Boolean = false

    //是否全选
    private var allSelect = true

    fun setOnClickListener(listener: ((List<T>) -> Unit)?) {
        this.listener = listener
    }

    override fun onCreateContentView(): View = createPopupById(R.layout.common_pop_s_a_l)

    private fun getIndex(): Int {
        if (value?.isNotBlank() == true) {
            data?.forEachIndexed { index, item ->
                if (value == item.getIValue()) {
                    return index
                }
            }
        }
        return -1
    }

    override fun onViewCreated(contentView: View) {

        //默认返回-1
        checkedPosition = getIndex()
        popupGravity = Gravity.BOTTOM or Gravity.CENTER_VERTICAL

        for (i in data as List<ItemData>) {
            if (i.isItemChecked() == false) {
                allSelect = false
            }
            contentView.findViewById<CheckBox>(R.id.allCheck).isChecked = allSelect == true
        }

        // title
        contentView.findViewById<TextView>(R.id.singleListTitleTv).text = title
        // list
        val singleListRv = contentView.findViewById<RecyclerView>(R.id.singleListRv)
        singleListRv.layoutManager = LinearLayoutManager(context)
        val adapter = object :
            BaseQuickAdapter<T, BaseViewHolder>(R.layout.common_adapter_single_list) {
            override fun convert(helper: BaseViewHolder, item: T?) {
                helper.setText(R.id.singleNameTv, item?.getIName())

                //是否显示选择
                helper.setGone(R.id.cbItemCheck, isMultiChecked)

                //默认不选中
                helper.setChecked(R.id.cbItemCheck, item?.isItemChecked() ?: false)

                //选中监听
                helper.addOnClickListener(R.id.cbItemCheck)

                //useless
                helper.setTextColor(
                    R.id.singleNameTv,
                    ColorUtils.getColor(
                        if (checkedPosition == helper.adapterPosition) R.color.color_3870EA else R.color.color_333333
                    )
                )
            }

        }
        singleListRv.adapter = adapter
        adapter.setNewData(data)

        adapter.setOnItemChildClickListener { _, view, position ->
            run {
                if (view.id == R.id.cbItemCheck) {
                    if (isMultiChecked) {
                        //可以多选
                        val item = adapter.data[position]
                        item?.shiftChecked(item.isItemChecked())
                        return@run
                    } else {
                        checkedPosition = position
                        if (checkedPosition > -1 && checkedPosition < adapter.data.size) {
                            listener?.invoke(listOf(adapter.data[checkedPosition]))
                            dismiss()
                        }
                    }
                }
            }
            for (i in data as List<ItemData>) {
                if (i.isItemChecked() == false) {
                    allSelect = false
                }
                contentView.findViewById<CheckBox>(R.id.allCheck).isChecked = allSelect == true
            }
        }


        // confirm
        contentView.findViewById<TextView>(R.id.singleListConfirmTv).apply {
            visibility = if (isMultiChecked) View.VISIBLE else View.GONE
        }.setOnClickListener {
            if (isMultiChecked) {
                val list = adapter.data.filter { it?.isItemChecked() == true }
                listener?.invoke(list)
            } else {
                //useless
                if (checkedPosition > -1 && checkedPosition < adapter.data.size) {
                    listener?.invoke(listOf(adapter.data[checkedPosition]))
                }
            }
            dismiss()
        }
        // close
        contentView.findViewById<ImageView>(R.id.singleListCloseTv)
            .setOnClickListener { dismiss() }

    }

    override fun onCreateShowAnimation(): Animation = showYTranslateAnim(300)

    override fun onCreateDismissAnimation(): Animation = hideYTranslateAnim(300)

}