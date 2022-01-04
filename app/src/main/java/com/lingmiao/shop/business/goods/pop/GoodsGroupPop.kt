package com.lingmiao.shop.business.goods.pop

import android.content.Context
import android.view.Gravity
import android.view.View
import android.view.animation.Animation
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.lingmiao.shop.R
import com.lingmiao.shop.business.goods.adapter.GroupAdapter
import com.lingmiao.shop.business.goods.api.bean.ShopGroupVO
import com.lingmiao.shop.util.hideYTranslateAnim
import com.lingmiao.shop.util.showYTranslateAnim
import com.james.common.utils.exts.gone
import com.james.common.utils.exts.visiable
import razerdp.basepopup.BasePopupWindow

/**
 * Author : Elson
 * Date   : 2020/7/16
 * Desc   : 商品分组
 */
class GoodsGroupPop(context: Context): BasePopupWindow(context) {

    private var lv1IndicatorIv: ImageView? = null
    private var lv2IndicatorIv: ImageView? = null
    private var level1Rv: RecyclerView? = null
    private var level2Rv: RecyclerView? = null

    private var groupNameTv : TextView? = null
    private var groupHintTv : TextView? = null

    private lateinit var lv1Adapter: GroupAdapter
    private lateinit var lv2Adapter: GroupAdapter

    private var mSelectList: MutableList<ShopGroupVO> = arrayListOf();
    private var groupName: String? = null
    var lv1Callback: ((List<ShopGroupVO>, String?) -> Unit)? = null

    override fun onCreateContentView(): View {
        return createPopupById(R.layout.goods_pop_group)
    }

    override fun onViewCreated(contentView: View) {
        setPopupGravity(Gravity.BOTTOM or Gravity.CENTER_VERTICAL)
        super.onViewCreated(contentView)
        contentView.findViewById<ImageView>(R.id.closeIv).setOnClickListener {
            dismiss()
        }
        groupNameTv = contentView.findViewById(R.id.groupNameTv)
        groupHintTv = contentView.findViewById(R.id.groupHintTv)
        lv1IndicatorIv = contentView.findViewById(R.id.level1IndicatorIv)
        lv2IndicatorIv = contentView.findViewById(R.id.level2IndicatorIv)
        lv1IndicatorIv?.visiable()

        level1Rv = contentView.findViewById(R.id.level1Rv)
        level2Rv = contentView.findViewById(R.id.level2Rv)

        lv1Adapter = GroupAdapter()
        lv2Adapter = GroupAdapter()
        initRecyclerView(level1Rv, lv1Adapter)
        initRecyclerView(level2Rv, lv2Adapter)

        lv1Adapter.setOnItemClickListener { adapter, view, position ->
            lv1Adapter.getItem(position)?.apply {
                if (this.children.isNullOrEmpty()) {
                    lv1Adapter.setGroupId(this.shopCatId)
                    lv1Adapter.notifyDataSetChanged()
                    lv2Adapter.clearData()
                    lv1Callback?.invoke(arrayListOf(this), this.shopCatName)
                    dismiss()
                    return@apply
                }

                resetIndicator()
                lv1IndicatorIv?.visiable()
                lv1Adapter.setGroupId(this.shopCatId)
                lv1Adapter.notifyDataSetChanged()
                lv2Adapter.replaceData(this.children!!)
                groupName = this.shopCatName

                mSelectList.clear();
                mSelectList.add(this);
            }
        }
        lv2Adapter.setOnItemClickListener { adapter, view, position ->
            lv2Adapter.getItem(position)?.apply {
                mSelectList.add(this);
                lv1Callback?.invoke(mSelectList, "${groupName}/${this.shopCatName}")
                resetIndicator()
                lv2IndicatorIv?.visiable()
                dismiss()
            }
        }
    }

    private fun resetIndicator() {
        lv1IndicatorIv?.gone()
        lv2IndicatorIv?.gone()
    }

    private fun initRecyclerView(rv: RecyclerView?, lvAdapter: GroupAdapter) {
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

    fun setLv1Data(lv1List: List<ShopGroupVO>) {
        lv1Adapter.replaceData(lv1List)
    }

    fun setTitle(name: String?, hint: String?) {
        groupNameTv?.text = name?:"请选择"
        groupHintTv?.text = hint?:""
    }

}