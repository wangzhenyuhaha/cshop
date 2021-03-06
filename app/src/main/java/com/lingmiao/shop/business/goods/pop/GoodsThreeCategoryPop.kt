package com.lingmiao.shop.business.goods.pop

import android.content.Context
import android.util.Log
import android.view.Gravity
import android.view.View
import android.view.animation.Animation
import android.widget.ImageView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.james.common.utils.exts.gone
import com.james.common.utils.exts.visiable
import com.lingmiao.shop.R
import com.lingmiao.shop.business.goods.adapter.GoodsCategoryAdapter
import com.lingmiao.shop.business.goods.api.bean.CategoryVO
import com.lingmiao.shop.util.hideYTranslateAnim
import com.lingmiao.shop.util.showYTranslateAnim
import razerdp.basepopup.BasePopupWindow

class GoodsThreeCategoryPop(context: Context) : BasePopupWindow(context) {

    private var lv1IndicatorIv: ImageView? = null
    private var lv2IndicatorIv: ImageView? = null
    private var lv3IndicatorIv: ImageView? = null
    private var level1Rv: RecyclerView? = null
    private var level2Rv: RecyclerView? = null
    private var level3Rv: RecyclerView? = null

    private lateinit var lv1Adapter: GoodsCategoryAdapter
    private lateinit var lv2Adapter: GoodsCategoryAdapter
    private lateinit var lv3Adapter: GoodsCategoryAdapter

    var lv1Callback: ((List<CategoryVO>, String?) -> Unit)? = null
    var query: ((String, adapter: GoodsCategoryAdapter) -> Unit)? = null

    private var groupName: String? = null
    private var mSelectList: MutableList<CategoryVO> = arrayListOf()

    override fun onCreateContentView(): View {
        return createPopupById(R.layout.goods_pop_three_category)
    }

    override fun onViewCreated(contentView: View) {

        popupGravity = Gravity.BOTTOM or Gravity.CENTER_VERTICAL
        super.onViewCreated(contentView)
        contentView.findViewById<ImageView>(R.id.closeIv).setOnClickListener {
            dismiss()
        }
        lv1IndicatorIv = contentView.findViewById(R.id.level1IndicatorIv)
        lv2IndicatorIv = contentView.findViewById(R.id.level2IndicatorIv)
        lv3IndicatorIv = contentView.findViewById(R.id.level3IndicatorIv)
        lv1IndicatorIv?.visiable()

        level1Rv = contentView.findViewById(R.id.level1Rv)
        level2Rv = contentView.findViewById(R.id.level2Rv)
        level3Rv = contentView.findViewById(R.id.level3Rv)

        lv1Adapter = GoodsCategoryAdapter()
        lv2Adapter = GoodsCategoryAdapter()
        lv3Adapter = GoodsCategoryAdapter()
        initRecyclerView(level1Rv, lv1Adapter)
        initRecyclerView(level2Rv, lv2Adapter)
        initRecyclerView(level3Rv, lv3Adapter)

        //??????????????????
        //1
        lv1Adapter.setOnItemClickListener { _, _, position ->
            lv1Adapter.getItem(position)?.apply {
                if (this.children.isNullOrEmpty()) {
                    lv1Adapter.setSelectedItem(this.categoryId)
                    lv1Adapter.notifyDataSetChanged()
                    lv2Adapter.clearData()
                    lv3Adapter.clearData()
                    lv1Callback?.invoke(listOf(this), this.name)
                    dismiss()
                    return@apply
                }
                //??????????????????
                //???1??????
                resetIndicator()
                lv1IndicatorIv?.visiable()
                lv1Adapter.setSelectedItem(this.categoryId)
                lv1Adapter.notifyDataSetChanged()

                //???2?????????
                query?.invoke(categoryId, lv2Adapter)

                //???3?????????
                lv3Adapter.replaceData(listOf<CategoryVO>())
                lv3Adapter.notifyDataSetChanged()

                //????????????????????????
                groupName = this.name

                mSelectList.clear()
                mSelectList.add(this)

            }
        }

        //2
        lv2Adapter.setOnItemClickListener { adapter, view, position ->

            lv2Adapter.getItem(position)?.apply {
                if (this.children.isNullOrEmpty()) {
                    lv2Adapter.setSelectedItem(this.categoryId)
                    lv2Adapter.notifyDataSetChanged()
                    lv3Adapter.clearData()
                    mSelectList.add(this)
                    lv1Callback?.invoke(mSelectList, "${groupName}/${this.name}")
                    dismiss()
                    return@apply
                }
                resetIndicator()
                lv2IndicatorIv?.visiable()
                //1
                //nothing to do

                //2
                lv2Adapter.setSelectedItem(this.categoryId)
                lv2Adapter.notifyDataSetChanged()

                //3
                lv3Adapter.replaceData(this.children!!)

                //??????
                groupName= "${groupName}/${this.name}"
                mSelectList.add(this)
            }

        }

        //3
        lv3Adapter.setOnItemClickListener { adapter, view, position ->
            lv3Adapter.getItem(position)?.apply {
                mSelectList.add(this)
                lv1Callback?.invoke(mSelectList, "${groupName}/${this.name}")
                resetIndicator()
                lv3IndicatorIv?.visiable()
                dismiss()
            }
        }
    }

    private fun resetIndicator() {
        lv1IndicatorIv?.gone()
        lv2IndicatorIv?.gone()
        lv3IndicatorIv?.gone()
    }

    private fun initRecyclerView(rv: RecyclerView?, lvAdapter: GoodsCategoryAdapter) {
        rv?.apply {
            adapter = lvAdapter
            layoutManager = LinearLayoutManager(context)
        }
    }

    override fun onCreateShowAnimation(): Animation {
        return showYTranslateAnim(350)
    }

    override fun onCreateDismissAnimation(): Animation {
        return hideYTranslateAnim(350)
    }

    fun setLv1Data(lv1List: List<CategoryVO>) {
        lv1Adapter.replaceData(lv1List)
    }

    fun setLv2Data(lv2List: List<CategoryVO>) {
        lv2Adapter.replaceData(lv2List)
    }

    fun setLv3Data(lv3List: List<CategoryVO>) {
        lv3Adapter.replaceData(lv3List)
    }
}