package com.lingmiao.shop.business.commonpop.pop

import android.content.Context
import android.view.Gravity
import android.view.View
import android.view.animation.Animation
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.lingmiao.shop.R
import com.lingmiao.shop.business.commonpop.adapter.AbsItemAdapter
import com.lingmiao.shop.util.hideYTranslateAnimOfBottomToTop
import com.lingmiao.shop.util.showYTranslateAnimOfTopToBottom
import com.lingmiao.shop.business.commonpop.adapter.ItemAdapter
import razerdp.basepopup.BasePopupWindow

import com.lingmiao.shop.business.commonpop.bean.ItemData

/**
 * Desc   : 商品搜索POP
 */
abstract class TwoItemPop1<T : ItemData>(context: Context, var title : String?, open var value: String?, open var data: List<T>?) :
    BasePopupWindow(context) {

    private var level1Rv: RecyclerView? = null
    private var level2Rv: RecyclerView? = null
    private var level3Rv: RecyclerView? = null

    private lateinit var lv1Adapter: AbsItemAdapter<T>
    private lateinit var lv2Adapter: AbsItemAdapter<T>
    private lateinit var lv3Adapter: AbsItemAdapter<T>

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

    override fun onCreateContentView(): View {
        return createPopupById(R.layout.goods_pop_mananger_type)
    }

    abstract fun getAdapter() : AbsItemAdapter<T>;

    abstract fun getData2(data1 : T) : List<T>;

    override fun onViewCreated(contentView: View) {
        // 显示的位置
        setPopupGravity(Gravity.BOTTOM or Gravity.CENTER_VERTICAL)

        level1Rv = contentView.findViewById(R.id.level1Rv)
        level2Rv = contentView.findViewById(R.id.level2Rv)
        level3Rv = contentView.findViewById(R.id.level3Rv)

        lv1Adapter = getAdapter()
        lv2Adapter = getAdapter()
        lv3Adapter = getAdapter()

        initRecyclerView(level1Rv, lv1Adapter)
        initRecyclerView(level2Rv, lv2Adapter)
        initRecyclerView(level3Rv, lv3Adapter)

        lv1Adapter.setOnItemClickListener { adapter, view, position ->
            lv1Adapter.getItem(position)?.apply {
                data1 = this;
                setLv2Data(getData2(this));
                lv1Callback?.invoke(this)

//                lv1Adapter.setSelectedItem(this.getValue())
                lv1Adapter.notifyDataSetChanged()
                lv3Adapter.data.clear()
                lv3Adapter.notifyDataSetChanged()
            }
        }
        lv2Adapter.setOnItemClickListener { adapter, view, position ->
            lv2Adapter.getItem(position)?.apply {
                data2 = this;
                lv2Callback?.invoke(this)
                dismiss()
            }
        }
        lv3Adapter.setOnItemClickListener { adapter, view, position ->
            lv3Adapter.getItem(position)?.apply {
                lv3Callback?.invoke(this)

                dismiss()
            }
        }
    }

    private fun initRecyclerView(rv: RecyclerView?, lvAdapter: AbsItemAdapter<T>) {
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
        return showYTranslateAnimOfTopToBottom(300)
    }

    override fun onCreateDismissAnimation(): Animation? {
        return hideYTranslateAnimOfBottomToTop(300)
    }
}