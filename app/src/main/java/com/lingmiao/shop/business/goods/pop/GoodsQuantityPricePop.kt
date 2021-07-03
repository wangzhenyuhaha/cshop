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
import com.lingmiao.shop.business.goods.adapter.MultiQuantityAndPriceAdapter
import com.lingmiao.shop.business.goods.api.bean.GoodsSkuVOWrapper
import com.lingmiao.shop.business.goods.api.request.QuantityPriceRequest
import com.lingmiao.shop.util.hideYTranslateAnim
import com.lingmiao.shop.util.showYTranslateAnim
import razerdp.basepopup.BasePopupWindow

/**
 * Author : Elson
 * Date   : 2020/8/16
 * Desc   : 商品多规格库存
 */
class GoodsQuantityPricePop(context: Context,val title : String?) : BasePopupWindow(context) {

    private var titleTv: TextView? = null
    private var closeIv: ImageView? = null
    private var recyclerView: RecyclerView? = null
    private var mAdapter: MultiQuantityAndPriceAdapter? = null
    private var confirmTv: TextView? = null

    private var listener: ((List<QuantityPriceRequest>?) -> Unit)? = null

    fun setConfirmListener(listener: ((List<QuantityPriceRequest>?) -> Unit)?) {
        this.listener = listener
    }

    fun setSkuList(list: List<GoodsSkuVOWrapper>) {
        val tempList = mutableListOf<QuantityPriceRequest>()
        list.forEach {
            tempList.add(QuantityPriceRequest.convert(it))
        }
        mAdapter?.replaceData(tempList)
    }

    override fun onCreateContentView(): View {
        return createPopupById(R.layout.goods_pop_quantity_multi)
    }

    override fun onViewCreated(contentView: View) {
        setPopupGravity(Gravity.BOTTOM or Gravity.CENTER_VERTICAL)
        super.onViewCreated(contentView)
        titleTv = contentView.findViewById(R.id.tvTitle)
        closeIv = contentView.findViewById(R.id.closeIv)
        recyclerView = contentView.findViewById(R.id.recyclerView)
        mAdapter = MultiQuantityAndPriceAdapter()
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
        if(title?.isNotEmpty() == true) {
            titleTv?.setText(title);
        }
    }

    override fun onCreateShowAnimation(): Animation? {
        return showYTranslateAnim(300)
    }

    override fun getDismissAnimation(): Animation? {
        return hideYTranslateAnim(300)
    }

}