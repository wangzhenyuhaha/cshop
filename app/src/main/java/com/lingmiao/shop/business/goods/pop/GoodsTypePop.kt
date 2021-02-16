package com.lingmiao.shop.business.goods.pop

import android.content.Context
import android.view.Gravity
import android.view.View
import android.view.animation.Animation
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.blankj.utilcode.util.ColorUtils
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.lingmiao.shop.R
import com.lingmiao.shop.business.goods.api.bean.GoodsTypeVO
import com.lingmiao.shop.util.hideYTranslateAnim
import com.lingmiao.shop.util.showYTranslateAnim
import razerdp.basepopup.BaseLazyPopupWindow

/**
 * Author : Elson
 * Date   : 2020/7/15
 * Desc   : 商品类型
 */
class GoodsTypePop(context: Context, var value: String?, var data: List<GoodsTypeVO>?) :
    BaseLazyPopupWindow(context) {

    var listener: ((GoodsTypeVO) -> Unit)? = null

    private var checkedPosition: Int = -1;

    fun setOnClickListener(listener: ((GoodsTypeVO) -> Unit)?) {
        this.listener = listener
    }

    override fun onCreateContentView(): View {
        return createPopupById(R.layout.goods_pop_type)
    }

    fun getIndex(): Int {
        if (value?.isNotBlank() == true) {
            data?.forEachIndexed { index, item ->
                if (value == item.value) {
                    return index;
                }
            }
        }
        return -1;
    }

    override fun onViewCreated(contentView: View) {
        checkedPosition = getIndex();

        popupGravity = Gravity.BOTTOM or Gravity.CENTER_VERTICAL
        val rvLogisticsCompanyList = contentView.findViewById<RecyclerView>(R.id.goodsTypeListRv)
        rvLogisticsCompanyList.layoutManager = LinearLayoutManager(context)
        val adapter = object :
            BaseQuickAdapter<GoodsTypeVO, BaseViewHolder>(R.layout.goods_adapter_goods_type2) {
            override fun convert(helper: BaseViewHolder, item: GoodsTypeVO?) {
                helper.setText(R.id.tvGoodsTypeName, item?.typeName);
                helper.setText(R.id.tvGoodsTypeHint,
                    String.format("%s%s%s",
                        if(item?.typeDesc?.length?:0 > 0) "(" else "" ,
                        item?.typeDesc,
                        if(item?.typeDesc?.length?:0 > 0) ")" else ""
                    )
                );
                helper.setTextColor(
                    R.id.tvGoodsTypeName,
                    ColorUtils.getColor(
                        if (checkedPosition == helper.adapterPosition) R.color.color_3870EA else R.color.color_333333
                    )
                );
            }

        }
        rvLogisticsCompanyList.adapter = adapter
        adapter.setNewData(data)
        adapter.setOnItemClickListener { _, _, position ->
            run {
                checkedPosition = position;
                if (checkedPosition > -1 && checkedPosition < adapter?.data?.size) {
                    listener?.invoke(adapter?.data?.get(checkedPosition))
                    dismiss()
                }
//                adapter.notifyDataSetChanged();
            }
        }

        contentView.findViewById<TextView>(R.id.goodsTypeConfirmTv).setOnClickListener {
            if (checkedPosition > -1 && checkedPosition < adapter?.data?.size) {
                listener?.invoke(adapter?.data?.get(checkedPosition))
                dismiss()
            }
        }
        contentView.findViewById<ImageView>(R.id.goodsTypeCloseTv)
            .setOnClickListener { dismiss() }
    }

    override fun onCreateShowAnimation(): Animation? {
        return showYTranslateAnim(300)
    }

    override fun onCreateDismissAnimation(): Animation? {
        return hideYTranslateAnim(300)
    }

}