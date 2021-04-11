package com.lingmiao.shop.business.me.adapter

import android.view.View
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.LinearLayout
import com.blankj.utilcode.util.ConvertUtils
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.lingmiao.shop.R
import com.lingmiao.shop.business.goods.api.bean.GoodsGalleryVO
import com.lingmiao.shop.business.me.bean.VipType
import com.luck.picture.lib.tools.ScreenUtils

/**
 * Author : Elson
 * Date   : 2020/8/18
 * Desc   : 发布商品页面图片选择
 */
class GalleryAdapter(list: List<VipType?>) :
    BaseQuickAdapter<VipType, BaseViewHolder>(R.layout.me_adapter_vip_type, list) {

    companion object {
        const val TYPE_Add = 1
        const val TYPE_PICTURE = 2
    }

    var checkedPosition : Int = 0;

    fun setChecked(position: Int) {
        checkedPosition = position;
        notifyDataSetChanged();
    }

    var mItemDecoration = 5
    var mMaxCout = 3

    override fun convert(helper: BaseViewHolder, item: VipType?) {
        item?.apply {
            helper.setChecked(R.id.vipItemLl, helper.adapterPosition == checkedPosition);
            helper.setGone(R.id.deleteIv, helper.adapterPosition == checkedPosition);
        }
        helper.addOnClickListener(R.id.vipItemLl)
    }

    private fun getItemPositionType(position: Int): Int {
        return if (data.size == position) TYPE_Add else TYPE_PICTURE
    }

//    override fun getItemCount(): Int {
//        if (data.size < mMaxCout) {
//            return super.getItemCount() + 1
//        } else {
//            return super.getItemCount()
//        }
//    }

    override fun getItemViewType(position: Int): Int {
        return getDefItemViewType(position)
    }

    private fun setImageViewLayoutParams(view: View) {
        val itemDercorationSum: Int = mMaxCout * mItemDecoration
        val paddingLeft: Int = ConvertUtils.dp2px(30f)
        val screenWidth = ScreenUtils.getScreenWidth(mContext)
        val size = (screenWidth - itemDercorationSum - mItemDecoration - paddingLeft * 2) / mMaxCout
        val params = FrameLayout.LayoutParams(size, size)
        view.layoutParams = params
    }
}