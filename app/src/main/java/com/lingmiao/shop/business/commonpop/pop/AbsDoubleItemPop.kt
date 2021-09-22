package com.lingmiao.shop.business.commonpop.pop

import android.content.Context
import android.view.Gravity
import android.view.View
import android.view.animation.Animation
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.lingmiao.shop.R
import com.lingmiao.shop.business.commonpop.bean.ItemData
import com.lingmiao.shop.util.hideYTranslateAnim
import com.lingmiao.shop.util.showYTranslateAnim
import razerdp.basepopup.BasePopupWindow

/**
 * Author : Elson
 * Date   : 2020/7/15
 * Desc   : 商品类型
 */
abstract class AbsDoubleItemPop<T : ItemData>(context: Context) :
    BasePopupWindow(context) {

    var listener: ((T) -> Unit)? = null

    private var popTitleRl : RelativeLayout? = null
    private var popCloseTv : ImageView? = null
    private var popTitleTv : TextView? = null
    private var popConfirmTv : TextView? = null

    private var level1Rv: RecyclerView? = null
    private var level2Rv: RecyclerView? = null

    private lateinit var lv1Adapter: BaseQuickAdapter<T, BaseViewHolder>
    private lateinit var lv2Adapter: BaseQuickAdapter<T, BaseViewHolder>

    fun setOnClickListener(listener: ((T) -> Unit)?) {
        this.listener = listener
    }

    override fun onCreateContentView(): View {
        return createPopupById(R.layout.common_pop_double_list)
    }

    override fun onViewCreated(contentView: View) {
        popupGravity = Gravity.BOTTOM or Gravity.CENTER_VERTICAL

        // title
        popTitleTv = contentView.findViewById<TextView>(R.id.doubleListTitleTv);

        // list
        level1Rv = contentView.findViewById<RecyclerView>(R.id.firstListRv)
        level2Rv = contentView.findViewById<RecyclerView>(R.id.secondListRv)

        lv1Adapter = getFirstAdapter()
        lv2Adapter = getSecondAdapter()

        //初始化RecyclerView
        initRecyclerView(level1Rv, lv1Adapter)
        initRecyclerView(level2Rv, lv2Adapter)

        //初始化RecyclerView的点击
        initL2AdapterClick()

        // confirm
        popConfirmTv = contentView.findViewById<TextView>(R.id.doubleListConfirmTv);
//        popConfirmTv?.setOnClickListener {
//            if (checkedPosition > -1 && checkedPosition < lv1Adapter?.data?.size) {
//                listener?.invoke(lv1Adapter?.data?.get(checkedPosition))
//                dismiss()
//            }
//        }
        // close
        popCloseTv = contentView.findViewById<ImageView>(R.id.doubleListCloseTv);
        popCloseTv?.setOnClickListener { dismiss() }

    }

    private fun initRecyclerView(rv: RecyclerView?, lvAdapter: BaseQuickAdapter<T, BaseViewHolder>) {
        rv?.apply {
            adapter = lvAdapter
            layoutManager = LinearLayoutManager(context)
        }
    }

    abstract fun getFirstAdapter() : BaseQuickAdapter<T, BaseViewHolder>;
    abstract fun getSecondAdapter() : BaseQuickAdapter<T, BaseViewHolder>;

    var data1 : T ?= null
    var data2 : T ?= null

    abstract fun getData2(data1 : T) : List<T>

    var lv1Callback: ((T) -> Unit)? = null
    var lv2Callback: ((T) -> Unit)? = null

    private fun initL2AdapterClick() {
        lv1Adapter.setOnItemClickListener { adapter, view, position ->
            run {
                lv1Adapter.getItem(position)?.apply {
                    //data1为当前点击的Item
                    data1 = this
                    //RecyclerView2设置为对应数据
                    setLv2Data(getData2(this))

                    //额外操作
                    lv1Callback?.invoke(this)

//                lv1Adapter.setSelectedItem(this.getValue())
                    //更新RecyclerView1
                    lv1Adapter.notifyDataSetChanged()

                    //清空data2数据
                    data2 = null
                }
            }
        }

        lv2Adapter.setOnItemClickListener { adapter, view, position ->
            run {
                lv2Adapter.getItem(position)?.apply {
                    data2 = this
                    lv2Callback?.invoke(this)
                    dismiss()
                }
            }
        }
    }

    //设置菜单
    fun setPopTitle(title: String?) {
        popTitleTv?.text = title;
    }

    //设置RecyclerView1数据
    fun setLv1Data(lv1List: List<T>) {
        lv1Adapter?.replaceData(lv1List)
    }
    //设置RecyclerView2数据
    fun setLv2Data(lv2List: List<T>) {
        lv2Adapter?.replaceData(lv2List)
    }

    override fun onCreateShowAnimation(): Animation? {
        return showYTranslateAnim(300)
    }

    override fun onCreateDismissAnimation(): Animation? {
        return hideYTranslateAnim(300)
    }

}