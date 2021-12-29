package com.lingmiao.shop.business.goods.pop

import android.content.Context
import android.view.Gravity
import android.view.View
import android.view.animation.Animation
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.james.common.utils.exts.singleClick
import com.lingmiao.shop.R
import com.lingmiao.shop.business.goods.adapter.MultiQuantityAndPriceAdapter
import com.lingmiao.shop.business.goods.api.bean.GoodsSkuVOWrapper
import com.lingmiao.shop.business.goods.api.request.QuantityPriceRequest
import com.lingmiao.shop.util.GlideUtils
import com.lingmiao.shop.util.hideYTranslateAnim
import com.lingmiao.shop.util.showYTranslateAnim
import razerdp.basepopup.BasePopupWindow

class GoodsNewQuantityPricePop(context: Context, val title: String?) : BasePopupWindow(context) {

    //顶端的标题
    private var titleTv: TextView? = null

    //顶端的取消按钮
    private var closeIv: ImageView? = null

    //商品图片
    private var goodsPic: ImageView? = null

    //商品名称
    private var goodsName: TextView? = null

    //商品查看详情
    private var goodsDetail: TextView? = null


    //RecyclerView
    private var recyclerView: RecyclerView? = null

    //定义Adapter
    private var mAdapter: MultiQuantityAndPriceAdapter? = null

    //确定按钮
    private var confirmTv: TextView? = null

    //注册点击监听
    private var listener: ((List<QuantityPriceRequest>?) -> Unit)? = null

    //查看商品详细
    private var goodsListener: (() -> Unit)? = null

    fun setConfirmListener(listener: ((List<QuantityPriceRequest>?) -> Unit)?) {
        this.listener = listener
    }

    fun setGoodsDetailListener(listener: (() -> Unit)?) {
        this.goodsListener = listener
    }

    fun setSkuList(list: List<GoodsSkuVOWrapper>) {

        //修改库存的列表，加上活动库存，活动价格
        val tempList = mutableListOf<QuantityPriceRequest>()
        list.forEach {
            // QuantityName字段获得
            tempList.add(QuantityPriceRequest.convert(it))

        }
        mAdapter?.replaceData(tempList)
    }

    fun setGoodsInfo(list: List<GoodsSkuVOWrapper>) {

        GlideUtils.setImageUrl1(goodsPic, list[0].thumbnail)
        goodsName?.text = list[0].goodsName
    }

    override fun onCreateContentView(): View {
        return createPopupById(R.layout.goods_pop_new_quantity)
    }

    override fun onViewCreated(contentView: View) {
        popupGravity = Gravity.BOTTOM or Gravity.CENTER_VERTICAL
        super.onViewCreated(contentView)
        //标题
        titleTv = contentView.findViewById(R.id.tvTitle)
        //取消按钮
        closeIv = contentView.findViewById(R.id.closeIv)
        recyclerView = contentView.findViewById(R.id.recyclerView)

        goodsPic = contentView.findViewById(R.id.goodsIv)
        goodsName = contentView.findViewById(R.id.goodsNameTv)
        goodsDetail = contentView.findViewById(R.id.goodsCheckSubmit)

        //自定义Adapter
        mAdapter = MultiQuantityAndPriceAdapter()


        recyclerView?.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = mAdapter
        }
        //确定
        confirmTv = contentView.findViewById(R.id.confirmTv)

        confirmTv?.setOnClickListener {
            listener?.invoke(mAdapter?.getUpdateQuantityList())
        }

        goodsDetail?.singleClick {
            goodsListener?.invoke()
        }

        closeIv?.setOnClickListener {
            dismiss()
        }
        if (title?.isNotEmpty() == true) {
            titleTv?.text = title
        }
    }

    override fun onCreateShowAnimation(): Animation {
        return showYTranslateAnim(300)
    }

    override fun getDismissAnimation(): Animation {
        return hideYTranslateAnim(300)
    }

}