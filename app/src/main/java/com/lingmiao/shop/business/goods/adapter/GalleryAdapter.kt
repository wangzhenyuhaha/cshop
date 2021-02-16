package com.lingmiao.shop.business.goods.adapter

import android.widget.FrameLayout
import android.widget.ImageView
import com.blankj.utilcode.util.ConvertUtils
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.lingmiao.shop.R
import com.lingmiao.shop.business.goods.api.bean.GoodsGalleryVO
import com.lingmiao.shop.util.GlideUtils
import com.luck.picture.lib.tools.ScreenUtils

/**
 * Author : Elson
 * Date   : 2020/8/18
 * Desc   : 发布商品页面图片选择
 */
class GalleryAdapter(list: List<GoodsGalleryVO?>) :
    BaseQuickAdapter<GoodsGalleryVO, BaseViewHolder>(R.layout.goods_adapter_goods_gallery, list) {

    companion object {
        const val TYPE_Add = 1
        const val TYPE_PICTURE = 2
    }

    var mItemDecoration = 5
    var mMaxCout = 9

    override fun convert(helper: BaseViewHolder, item: GoodsGalleryVO?) {
        val imageView = helper.getView<ImageView>(R.id.imageView)
        setImageViewLayoutParams(imageView)
        helper.addOnClickListener(R.id.deleteIv)
        if (getItemPositionType(helper.adapterPosition) == TYPE_Add) {
            helper.setGone(R.id.deleteIv, false)
            helper.setImageResource(R.id.imageView, R.mipmap.goods_selected_img)
        } else {
            helper.setGone(R.id.deleteIv, true)
            GlideUtils.setImageUrl1(imageView, item?.original)
        }
    }

    private fun getItemPositionType(position: Int): Int {
        return if (data.size == position) TYPE_Add else TYPE_PICTURE
    }

    override fun getItemCount(): Int {
        if (data.size < mMaxCout) {
            return super.getItemCount() + 1
        } else {
            return super.getItemCount()
        }
    }

    override fun getItemViewType(position: Int): Int {
        return getDefItemViewType(position)
    }

    private fun setImageViewLayoutParams(view: ImageView) {
        val itemDercorationSum: Int = 3 * mItemDecoration
        val paddingLeft: Int = ConvertUtils.dp2px(30f)
        val screenWidth = ScreenUtils.getScreenWidth(mContext)
        val size = (screenWidth - itemDercorationSum - 5 - paddingLeft * 2) / 4
        val params = FrameLayout.LayoutParams(size, size)
        view.layoutParams = params
    }
}