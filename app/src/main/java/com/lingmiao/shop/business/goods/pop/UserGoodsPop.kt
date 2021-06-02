package com.lingmiao.shop.business.goods.pop

import android.content.Context
import android.view.Gravity
import android.view.View
import android.view.animation.Animation
import android.widget.ImageView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.lingmiao.shop.R
import com.lingmiao.shop.util.hideYTranslateAnim
import com.lingmiao.shop.util.showYTranslateAnim
import com.james.common.utils.exts.gone
import com.james.common.utils.exts.visiable
import com.lingmiao.shop.business.goods.adapter.UserGoodsAdapter
import com.lingmiao.shop.business.goods.api.bean.CategoryVO
import razerdp.basepopup.BasePopupWindow

/**
 * Author : Elson
 * Date   : 2020/7/16
 * Desc   : 商品分组
 */
class UserGoodsPop(context: Context): BasePopupWindow(context) {

    private var lv1IndicatorIv: ImageView? = null
    private var lv2IndicatorIv: ImageView? = null
    private var level1Rv: RecyclerView? = null
    private var level2Rv: RecyclerView? = null

    private lateinit var lv1Adapter: UserGoodsAdapter
    private lateinit var lv2Adapter: UserGoodsAdapter

    private var mSelectList: MutableList<CategoryVO> = arrayListOf();
    private var groupName: String? = null
    var lv1Callback: ((List<CategoryVO>, String?) -> Unit)? = null

    override fun onCreateContentView(): View {
        return createPopupById(R.layout.goods_pop_group)
    }

    override fun onViewCreated(contentView: View) {
        setPopupGravity(Gravity.BOTTOM or Gravity.CENTER_VERTICAL)
        super.onViewCreated(contentView)
        contentView.findViewById<ImageView>(R.id.closeIv).setOnClickListener {
            dismiss()
        }
        lv1IndicatorIv = contentView.findViewById(R.id.level1IndicatorIv)
        lv2IndicatorIv = contentView.findViewById(R.id.level2IndicatorIv)
        lv1IndicatorIv?.visiable()

        level1Rv = contentView.findViewById(R.id.level1Rv)
        level2Rv = contentView.findViewById(R.id.level2Rv)

        lv1Adapter = UserGoodsAdapter()
        lv2Adapter = UserGoodsAdapter()
        initRecyclerView(level1Rv, lv1Adapter)
        initRecyclerView(level2Rv, lv2Adapter)

        lv1Adapter.setOnItemClickListener { adapter, view, position ->
            lv1Adapter.getItem(position)?.apply {
                if (this.children.isNullOrEmpty()) {
                    lv1Adapter.setGroupId(this.categoryId)
                    lv1Adapter.notifyDataSetChanged()
                    lv2Adapter.clearData()
                    lv1Callback?.invoke(arrayListOf(this), this.name)
                    dismiss()
                    return@apply
                }

                resetIndicator()
                lv1IndicatorIv?.visiable()
                lv1Adapter.setGroupId(this.categoryId)
                lv1Adapter.notifyDataSetChanged()
                lv2Adapter.replaceData(this.children!!)
                groupName = this.name

                mSelectList.clear();
                mSelectList.add(this);
            }
        }
        lv2Adapter.setOnItemClickListener { adapter, view, position ->
            lv2Adapter.getItem(position)?.apply {
                mSelectList.add(this);
                lv1Callback?.invoke(mSelectList, "${groupName}/${this.name}")
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

    private fun initRecyclerView(rv: RecyclerView?, lvAdapter: UserGoodsAdapter) {
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
}