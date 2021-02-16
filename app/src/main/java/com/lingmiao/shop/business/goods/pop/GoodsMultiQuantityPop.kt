package com.lingmiao.shop.business.goods.pop

import android.content.Context
import android.view.Gravity
import android.view.View
import android.view.animation.Animation
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.lingmiao.shop.R
import com.lingmiao.shop.business.goods.adapter.MultiQuantityAdapter
import com.lingmiao.shop.business.goods.api.bean.GoodsSkuVO
import com.lingmiao.shop.business.goods.api.bean.GoodsSkuVOWrapper
import com.lingmiao.shop.business.goods.api.request.QuantityRequest
import com.lingmiao.shop.util.hideYTranslateAnim
import com.lingmiao.shop.util.showYTranslateAnim
import razerdp.basepopup.BasePopupWindow

/**
 * Author : Elson
 * Date   : 2020/8/16
 * Desc   : 商品多规格库存
 */
class GoodsMultiQuantityPop(context: Context) : BasePopupWindow(context) {

    private var closeIv: ImageView? = null
    private var recyclerView: RecyclerView? = null
    private var mAdapter: MultiQuantityAdapter? = null
    private var confirmTv: TextView? = null

    private var listener: ((List<QuantityRequest>?) -> Unit)? = null

    fun setConfirmListener(listener: ((List<QuantityRequest>?) -> Unit)?) {
        this.listener = listener
    }

    fun setSkuList(list: List<GoodsSkuVOWrapper>) {
        val tempList = mutableListOf<QuantityRequest>()
        list.forEach {
            tempList.add(QuantityRequest.convert(it))
        }
        mAdapter?.replaceData(tempList)
    }

    override fun onCreateContentView(): View {
        return createPopupById(R.layout.goods_pop_quantity_multi)
    }

    override fun onViewCreated(contentView: View) {
        setPopupGravity(Gravity.BOTTOM or Gravity.CENTER_VERTICAL)
        super.onViewCreated(contentView)
        closeIv = contentView.findViewById(R.id.closeIv)
        recyclerView = contentView.findViewById(R.id.recyclerView)
        mAdapter = MultiQuantityAdapter()
        recyclerView?.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = mAdapter
        }
        confirmTv = contentView.findViewById(R.id.confirmTv)
        confirmTv?.setOnClickListener {
            listener?.invoke(mAdapter?.getUpdateQuantityList())
        }
        closeIv?.setOnClickListener {
            dismiss()
        }
    }

    override fun onCreateShowAnimation(): Animation? {
        return showYTranslateAnim(300)
    }

    override fun getDismissAnimation(): Animation? {
        return hideYTranslateAnim(300)
    }

}