package com.lingmiao.shop.business.me.view

import android.app.Activity
import android.content.Context
import android.graphics.Rect
import android.util.AttributeSet
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ItemDecoration
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.blankj.utilcode.util.ConvertUtils
import com.lingmiao.shop.R
import com.lingmiao.shop.business.common.pop.MediaMenuPop
import com.lingmiao.shop.business.goods.api.bean.GoodsGalleryVO
import com.lingmiao.shop.business.me.adapter.GalleryAdapter
import com.lingmiao.shop.business.me.bean.VipType
import com.lingmiao.shop.business.photo.PhotoHelper
import com.luck.picture.lib.entity.LocalMedia

/**
 * Author : Elson
 * Date   : 2020/8/18
 * Desc   : 图片选择
 */
class GalleryRecyclerView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : RecyclerView(context, attrs, defStyleAttr) {

    private var mAdapter: GalleryAdapter? = null
    private var mItemDecoration = 0
    private var mMinCount = 0
    private var mMaxCount = 0
    private val mSelectPhotos: MutableList<VipType> = mutableListOf()

    init {
        mItemDecoration = ConvertUtils.dp2px(3f)
        initRecyclerView()
    }

    fun setCountLimit(min: Int, max: Int) {
        mMinCount = min
        mMaxCount = max
        mAdapter?.mMaxCout = max
    }

    fun setDataList(list: List<VipType>?) {
        mSelectPhotos?.clear();
        addDataList(list);
    }

    fun addDataList(list: List<VipType>?) {
        if (list.isNullOrEmpty()) {
            return
        }
        mSelectPhotos.addAll(list)
        mAdapter?.notifyDataSetChanged()
    }

    fun getSelectItems(): List<VipType> {
        return mSelectPhotos
    }

    private fun initRecyclerView() {
        this.layoutManager = StaggeredGridLayoutManager(3, StaggeredGridLayoutManager.VERTICAL)
        this.addItemDecoration(GalleryItemDecoration(mItemDecoration))
        this.mAdapter = GalleryAdapter(mSelectPhotos)
        this.adapter = mAdapter
        mAdapter?.setOnItemChildClickListener { adapter, view, position ->
             val item = adapter.data[position] as VipType;
            if (view.id == R.id.vipItemLl) {
                mAdapter?.setChecked(position);
            }
        }
        mAdapter?.setOnItemClickListener { adapter, view, position ->
            // val item = adapter.data[position] as VipType;
            // if (view.id == R.id.vipItemLl) {
                // mAdapter?.setChecked(position);
            //}
        }
    }


}

class GalleryItemDecoration(private val space: Int) : ItemDecoration() {

    override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {
        outRect.left = space
        outRect.right = space
        outRect.bottom = space
        outRect.top = space
    }
}