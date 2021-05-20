package com.lingmiao.shop.business.goods.view

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
import com.lingmiao.shop.business.goods.adapter.GalleryAdapter
import com.lingmiao.shop.business.goods.api.bean.GoodsGalleryVO
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
    private val mSelectPhotos: MutableList<GoodsGalleryVO> = mutableListOf()

    init {
        mItemDecoration = ConvertUtils.dp2px(3f)
        initRecyclerView()
    }

    fun setCountLimit(min: Int, max: Int) {
        mMinCount = min
        mMaxCount = max
        mAdapter?.mMaxCout = max
    }

    fun addData(it : GoodsGalleryVO?) {
        if(it == null) {
            return;
        }
        mSelectPhotos.add(it);
        mAdapter?.notifyDataSetChanged()
    }

    fun addDataList(list: List<GoodsGalleryVO>?) {
        if (list.isNullOrEmpty()) {
            return
        }
        mSelectPhotos.addAll(list)
        mAdapter?.notifyDataSetChanged()
    }

    fun getSelectPhotos(): List<GoodsGalleryVO> {
        return mSelectPhotos
    }

    private fun initRecyclerView() {
        this.layoutManager = StaggeredGridLayoutManager(4, StaggeredGridLayoutManager.VERTICAL)
        this.addItemDecoration(GalleryItemDecoration(mItemDecoration))
        this.mAdapter = GalleryAdapter(mSelectPhotos)
        this.adapter = mAdapter
        mAdapter?.setOnItemChildClickListener { adapter, view, position ->
            if (view.id == R.id.deleteIv) {
                adapter?.remove(position)
            }
        }
        mAdapter?.setOnItemClickListener { adapter, view, position ->
            if (mAdapter!!.data.size == position) {
                openGallery() //打开相册
            } else {
                PhotoHelper.previewAlbum(context as Activity, position, mAdapter?.data)
            }
        }
    }

    private fun openGallery() {
        val menus = MediaMenuPop.TYPE_SELECT_PHOTO or MediaMenuPop.TYPE_PLAY_PHOTO
        MediaMenuPop(context, menus).apply {
            setOnClickListener { type ->
                when (type) {
                    MediaMenuPop.TYPE_SELECT_PHOTO -> {
                        PhotoHelper.openAlbum(context as Activity, mMaxCount - mSelectPhotos.size, null) {
                            addDataList(convert2GalleryVO(it))
                        }
                    }
                    MediaMenuPop.TYPE_PLAY_PHOTO -> {
                        PhotoHelper.openCamera(context as Activity, null) {
                            addDataList(convert2GalleryVO(it))
                        }
                    }
                }
            }
        }.showPopupWindow()
    }

    private fun convert2GalleryVO(list: List<LocalMedia>): List<GoodsGalleryVO> {
        val galleryList = mutableListOf<GoodsGalleryVO>()
        list.forEach {
            galleryList.add(GoodsGalleryVO.convert(it))
        }
        return galleryList
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