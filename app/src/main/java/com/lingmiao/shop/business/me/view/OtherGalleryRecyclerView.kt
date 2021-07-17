package com.lingmiao.shop.business.me.view

import android.app.Activity
import android.content.Context
import android.graphics.Rect
import android.util.AttributeSet
import android.util.Log
import android.view.View
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ItemDecoration
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.blankj.utilcode.util.ConvertUtils
import com.james.common.netcore.networking.http.core.HiResponse
import com.james.common.netcore.networking.http.core.awaitHiResponse
import com.james.common.utils.exts.isNetUrl
import com.lingmiao.shop.R
import com.lingmiao.shop.base.CommonRepository
import com.lingmiao.shop.business.common.bean.FileResponse
import com.lingmiao.shop.business.common.pop.MediaMenuPop
import com.lingmiao.shop.business.goods.adapter.GalleryAdapter
import com.lingmiao.shop.business.goods.api.bean.GoodsGalleryVO
import com.lingmiao.shop.business.me.api.MeRepository
import com.lingmiao.shop.business.me.fragment.ShopOperateSettingFragment
import com.lingmiao.shop.business.photo.GlideEngine
import com.lingmiao.shop.business.photo.PhotoHelper
import com.luck.picture.lib.PictureSelector
import com.luck.picture.lib.config.PictureMimeType
import com.luck.picture.lib.entity.LocalMedia
import com.luck.picture.lib.listener.OnResultCallbackListener
import kotlinx.coroutines.*
import java.io.File

/**
 * Author : Elson
 * Date   : 2020/8/18
 * Desc   : 图片选择
 */
class OtherGalleryRecyclerView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : RecyclerView(context, attrs, defStyleAttr) {

    private var mAdapter: GalleryAdapter? = null
    private var mItemDecoration = 0
    private var mMinCount = 0
    private var mMaxCount = 0
    private val mSelectPhotos: MutableList<GoodsGalleryVO> = mutableListOf()

    //裁剪的长
    private var x: Int = 1

    //裁剪的宽
    private var y: Int = 1

    init {
        mItemDecoration = ConvertUtils.dp2px(3f)
        initRecyclerView()
    }

    fun setCountLimit(min: Int, max: Int) {
        mMinCount = min
        mMaxCount = max
        mAdapter?.mMaxCout = max
    }

    //.withAspectRatio()
    fun setAspectRatio(x: Int, y: Int) {

        this.x = x
        this.y = y

    }

    fun addData(it: GoodsGalleryVO?) {
        if (it == null) {
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
                        openAlbum(
                            context as Activity,
                            mMaxCount - mSelectPhotos.size,
                            x, y,
                            null
                        ) {

                            addDataList(convert2GalleryVO(it))
                        }
                    }
                    MediaMenuPop.TYPE_PLAY_PHOTO -> {
                        openCamera(context as Activity, x, y, null) {

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



    companion object {


        /**
         * 获取图片，图片大小超过500K，则开启压缩
         */
        @JvmStatic
        fun openAlbum(
            activity: Activity,
            maxCount: Int,
            x: Int, y: Int,
            cancel: (() -> Unit)?,
            success: ((List<LocalMedia>) -> Unit)?
        ) {
            openAlbum(activity, maxCount, cancel, true, x, y, 500, success);
        }

        /**
         * 获取图片，图片大小超过500K，则开启压缩
         */
        @JvmStatic
        fun openAlbum(
            activity: Activity,
            maxCount: Int,
            cancel: (() -> Unit)?,
            crop: Boolean?,
            x: Int,
            y: Int,
            size: Int,
            success: ((List<LocalMedia>) -> Unit)?
        ) {
            PictureSelector.create(activity)
                .openGallery(PictureMimeType.ofImage())
                .maxSelectNum(maxCount)
                .isCompress(true)
                .isEnableCrop(crop ?: false)
                .withAspectRatio(x, y)
                .minimumCompressSize(size)
                .imageEngine(GlideEngine.createGlideEngine())
                .forResult(object : OnResultCallbackListener<LocalMedia> {
                    override fun onResult(result: List<LocalMedia>) { // 结果回调
                        success?.invoke(result)
                    }

                    override fun onCancel() { // 取消
                        cancel?.invoke()
                    }
                })
        }


        /**
         * 拍照：图片大小超过500K，则开启压缩
         */
        @JvmStatic
        fun openCamera(
            activity: Activity,
            x: Int,
            y: Int,
            cancel: (() -> Unit)?,
            success: ((List<LocalMedia>) -> Unit)?
        ) {
            openCamera(activity, cancel, true, x, y, 500, success);
        }

        /**
         * 拍照：图片大小超过500K，则开启压缩
         */
        @JvmStatic
        fun openCamera(
            activity: Activity,
            cancel: (() -> Unit)?,
            crop: Boolean? = false,
            x: Int,
            y: Int,
            size: Int,
            success: ((List<LocalMedia>) -> Unit)?
        ) {
            PictureSelector.create(activity)
                .openCamera(PictureMimeType.ofImage())
                .isCompress(crop ?: false)
                .isEnableCrop(true)
                .withAspectRatio(x, y)
                .minimumCompressSize(size)
                .imageEngine(GlideEngine.createGlideEngine())
                .forResult(object : OnResultCallbackListener<LocalMedia> {
                    override fun onResult(result: List<LocalMedia>) { // 结果回调
                        success?.invoke(result)
                    }

                    override fun onCancel() { // 取消
                        cancel?.invoke()
                    }
                })
        }


    }


}