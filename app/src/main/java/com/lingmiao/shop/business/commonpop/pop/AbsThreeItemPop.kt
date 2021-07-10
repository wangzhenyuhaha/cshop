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
import com.james.common.utils.exts.gone
import com.james.common.utils.exts.visiable
import com.lingmiao.shop.R
import com.lingmiao.shop.util.hideYTranslateAnimOfBottomToTop
import com.lingmiao.shop.util.showYTranslateAnimOfTopToBottom
import razerdp.basepopup.BasePopupWindow
import com.lingmiao.shop.business.commonpop.bean.ItemData
import com.lingmiao.shop.util.hideYTranslateAnim
import com.lingmiao.shop.util.showYTranslateAnim


/**
 * Desc   : 商品搜索POP
 */
abstract class AbsThreeItemPop<T : ItemData>(context: Context, open var title : String?) :
    BasePopupWindow(context) {

    private var popTitleRl : RelativeLayout? = null
    private var popCloseTv : ImageView? = null
    private var popTitleTv : TextView? = null
    private var popConfirmTv : TextView? = null

    private var level1Rv: RecyclerView? = null
    private var level2Rv: RecyclerView? = null
    private var level3Rv: RecyclerView? = null

    private lateinit var lv1Adapter: BaseQuickAdapter<T, BaseViewHolder>
    private lateinit var lv2Adapter: BaseQuickAdapter<T, BaseViewHolder>
    private lateinit var lv3Adapter: BaseQuickAdapter<T, BaseViewHolder>

    var lv1Callback: ((T) -> Unit)? = null
    var lv2Callback: ((T) -> Unit)? = null
    var lv3Callback: ((T) -> Unit)? = null

    var data1 : T ?= null;
    var data2 : T ?= null;
    var data3 : T ?= null;

    init {
        // 透明背景
        setBackgroundColor(0)
    }

    companion object {
        const val LEVEL_1 = 1;
        const val LEVEL_2 = 2;
        const val LEVEL_3 = 3;
    }

    override fun onCreateContentView(): View {
        return createPopupById(R.layout.pop_common_item)
    }

    override fun onViewCreated(contentView: View) {
        // 显示的位置
        popupGravity = Gravity.BOTTOM or Gravity.CENTER_VERTICAL

        level1Rv = contentView.findViewById(R.id.level1Rv)
        level2Rv = contentView.findViewById(R.id.level2Rv)
        level3Rv = contentView.findViewById(R.id.level3Rv)

        popTitleRl = contentView.findViewById(R.id.popTitleRl)
        popCloseTv = contentView.findViewById(R.id.popCloseTv)
        popTitleTv = contentView.findViewById(R.id.popTitleTv)
        popConfirmTv = contentView.findViewById(R.id.popConfirmTv)

        lv1Adapter = getAdapter1()
        lv2Adapter = getAdapter2()
        lv3Adapter = getAdapter3()

        initRecyclerView(level1Rv, lv1Adapter)
        initRecyclerView(level2Rv, lv2Adapter)
        initRecyclerView(level3Rv, lv3Adapter)

        when(getLevel()) {
            LEVEL_1 -> {
                level2Rv?.gone()
                level3Rv?.gone()
                initL1AdapterClick();
            }
            LEVEL_2 -> {
                level3Rv?.gone()
                initL2AdapterClick();
            }
            LEVEL_3 -> {
                initL3AdapterClick();
            }
        }

        popCloseTv?.setOnClickListener {
            dismiss();
        }

    }

    private fun initL3AdapterClick() {
        lv1Adapter.setOnItemClickListener { adapter, view, position ->
            lv1Adapter.getItem(position)?.apply {
                data1 = this;
                setLv2Data(getData2(this));
                lv1Callback?.invoke(this)

//                lv1Adapter.setSelectedItem(this.getValue())
                lv1Adapter.notifyDataSetChanged()
                lv3Adapter.data.clear()
                data2 = null;
                data3 = null;
                lv3Adapter.notifyDataSetChanged()
            }
        }
        lv2Adapter.setOnItemClickListener { adapter, view, position ->
            lv2Adapter.getItem(position)?.apply {
                data2 = this;
                setLv3Data(getData3(this));
                lv2Callback?.invoke(this)
//                lv2Adapter.setSelectedItem(this.getValue())
                lv2Adapter.notifyDataSetChanged()

                data3 = null;
            }
        }
        lv3Adapter.setOnItemClickListener { adapter, view, position ->
            lv3Adapter.getItem(position)?.apply {
                data3 = this;
                lv3Callback?.invoke(this)
//                lv3Adapter.setSelectedItem(this.getValue())
//                lv3Adapter.notifyDataSetChanged();
                dismiss()
            }
        }
    }

    private fun initL2AdapterClick() {
        lv1Adapter.setOnItemClickListener { adapter, view, position ->
            lv1Adapter.getItem(position)?.apply {
                data1 = this;
                setLv2Data(getData2(this));
                lv1Callback?.invoke(this)

//                lv1Adapter.setSelectedItem(this.getValue())
                lv1Adapter.notifyDataSetChanged()
                lv3Adapter.data.clear()
                data2 = null;
                data3 = null;
                lv3Adapter.notifyDataSetChanged()
            }
        }
        lv2Adapter.setOnItemClickListener { adapter, view, position ->
            lv2Adapter.getItem(position)?.apply {
                data2 = this;
                lv2Callback?.invoke(this)
                dismiss();
            }
        }
    }

    private fun initL1AdapterClick() {
        lv1Adapter.setOnItemClickListener { adapter, view, position ->
            lv1Adapter.getItem(position)?.apply {
                data1 = this;
                lv1Callback?.invoke(this)
                dismiss();
            }
        }
    }

    abstract fun getLevel() : Int;

    abstract fun getAdapter1() : BaseQuickAdapter<T, BaseViewHolder>;

    abstract fun getAdapter2() : BaseQuickAdapter<T, BaseViewHolder>;

    abstract fun getAdapter3() : BaseQuickAdapter<T, BaseViewHolder>;

    abstract fun getData2(data1 : T) : List<T>;

    abstract fun getData3(data2 : T) : List<T>;

    open fun hasTitle() : Boolean {
        return false;
    }

    fun setPopTitle(title : String?) {
        popTitleRl?.visiable();
        popTitleTv?.text = title;
    }

    fun showPopupWindow(title : String?) {
        setPopTitle(title);
        super.showPopupWindow();
    }

    private fun initRecyclerView(rv: RecyclerView?, lvAdapter: BaseQuickAdapter<T, BaseViewHolder>) {
        rv?.apply {
            adapter = lvAdapter
            layoutManager = LinearLayoutManager(context)
        }
    }

    fun setLv1Data(lv1List: List<T>) {
        lv1Adapter?.replaceData(lv1List)
    }

    fun setLv2Data(lv2List: List<T>) {
        lv2Adapter?.replaceData(lv2List)
    }

    fun setLv3Data(lv3List: List<T>) {
        lv3Adapter?.replaceData(lv3List)
    }

    override fun onCreateShowAnimation(): Animation? {
        return showYTranslateAnim(300)
    }

    override fun onCreateDismissAnimation(): Animation? {
        return hideYTranslateAnim(300)
    }

}