package com.lingmiao.shop.business.me.view

import android.app.Activity
import android.content.Context
import android.util.AttributeSet
import android.widget.FrameLayout
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.blankj.utilcode.util.ConvertUtils
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.lingmiao.shop.R
import com.lingmiao.shop.business.common.pop.MediaMenuPop
import com.lingmiao.shop.business.goods.api.bean.GoodsGalleryVO
import com.lingmiao.shop.business.photo.GlideEngine
import com.lingmiao.shop.business.photo.PhotoHelper
import com.lingmiao.shop.util.GlideUtils
import com.luck.picture.lib.PictureSelector
import com.luck.picture.lib.config.PictureMimeType
import com.luck.picture.lib.entity.LocalMedia
import com.luck.picture.lib.listener.OnResultCallbackListener
import com.luck.picture.lib.tools.ScreenUtils


class CommonGalleryRecyclerView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : RecyclerView(context, attrs, defStyleAttr) {


    //Adapter
    private var mAdapter: CommonGalleryAdapter? = null

    //
    private var mItemDecoration = 0

    //
    private var mMinCount = 0

    //
    private var mMaxCount = 0

    //数据
    private val mSelectPhotos: MutableList<GoodsGalleryVO> = mutableListOf()

    //裁剪的长
    private var x: Int? = null

    //裁剪的宽
    private var y: Int? = null


    init {
        //dp 转 px
        mItemDecoration = ConvertUtils.dp2px(3f)
        initRecyclerView()
    }

    //设置最少图片数量，最大图片数量
    fun setCountLimit(min: Int, max: Int) {
        mMinCount = min
        mMaxCount = max
        mAdapter?.mMaxCount = max
    }

    //.withAspectRatio(),设置需要的图片的长宽,设置时无法输入null
    fun setAspectRatio(x: Int, y: Int) {
        this.x = x
        this.y = y
    }

    fun addData(it: GoodsGalleryVO?) {
        if (it == null) {
            return
        }
        mSelectPhotos.add(it)
        mAdapter?.notifyDataSetChanged()
    }

    private fun addDataList(list: List<GoodsGalleryVO>?) {
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
        //增加分隔符
        this.addItemDecoration(GalleryItemDecoration(mItemDecoration))
        //初始化数据
        this.mAdapter = CommonGalleryAdapter(mSelectPhotos)

        this.adapter = mAdapter

        mAdapter?.setOnItemChildClickListener { adapter, view, position ->
            if (view.id == R.id.deleteIv) {
                adapter?.remove(position)
            }
        }

        mAdapter?.setOnItemClickListener { _, _, position ->
            if (mAdapter!!.data.size == position) {
                openGallery() //打开相册
            } else {
                //查看图片
                PhotoHelper.previewAlbum(context as Activity, position, mAdapter?.data)
            }
        }
    }

    private fun openGallery() {
        val menus = MediaMenuPop.TYPE_SELECT_PHOTO or MediaMenuPop.TYPE_PLAY_PHOTO
        MediaMenuPop(context, menus).apply {
            setOnClickListener { type ->
                when (type) {
                    //选择图片
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
                    //拍照
                    MediaMenuPop.TYPE_PLAY_PHOTO -> {
                        openCamera(context as Activity, x, y, null) {
                            addDataList(convert2GalleryVO(it))
                        }
                    }
                }
            }
        }.showPopupWindow()
    }

    //将LocalMedia转换成GoodsGalleryVO
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
            x: Int?, y: Int?,
            cancel: (() -> Unit)?,
            success: ((List<LocalMedia>) -> Unit)?
        ) {
            openAlbum(activity, maxCount, cancel, true, x, y, 500, success)
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
            x: Int?,
            y: Int?,
            size: Int,
            success: ((List<LocalMedia>) -> Unit)?
        ) {
            if (x != null && y != null) {
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
            } else {
                PictureSelector.create(activity)
                    .openGallery(PictureMimeType.ofImage())
                    .maxSelectNum(maxCount)
                    .isCompress(true)
                    .isEnableCrop(crop ?: false)
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


        /**
         * 拍照：图片大小超过500K，则开启压缩
         */
        @JvmStatic
        fun openCamera(
            activity: Activity,
            x: Int?,
            y: Int?,
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
            x: Int?,
            y: Int?,
            size: Int,
            success: ((List<LocalMedia>) -> Unit)?
        ) {
            if (x != null && y != null) {
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
            } else {
                PictureSelector.create(activity)
                    .openCamera(PictureMimeType.ofImage())
                    .isCompress(crop ?: false)
                    .isEnableCrop(true)
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

    //GalleryAdapter
    class CommonGalleryAdapter(list: List<GoodsGalleryVO?>) :
        BaseQuickAdapter<GoodsGalleryVO, BaseViewHolder>(
            R.layout.goods_adapter_goods_gallery,
            list
        ) {

        companion object {
            const val TYPE_Add = 1
            const val TYPE_PICTURE = 2
        }

        var mItemDecoration = 5
        var mMaxCount = 10

        override fun convert(helper: BaseViewHolder, item: GoodsGalleryVO?) {
            val imageView = helper.getView<ImageView>(R.id.imageView)
            //设置Item大小
            setImageViewLayoutParams(imageView)
            //设置点击事件
            helper.addOnClickListener(R.id.deleteIv)
            if (getItemPositionType(helper.adapterPosition) == TYPE_Add) {
                //点击上传图片
                helper.setGone(R.id.deleteIv, false)
                helper.setImageResource(R.id.imageView, R.mipmap.goods_selected_img)
            } else {
                //已上传图片
                helper.setGone(R.id.deleteIv, true)
                GlideUtils.setImageUrl1(imageView, item?.original)
            }
        }

        private fun getItemPositionType(position: Int): Int {
            return if (data.size == position) TYPE_Add else TYPE_PICTURE
        }

        override fun getItemCount(): Int {
            return if (data.size < mMaxCount) {
                super.getItemCount() + 1
            } else {
                super.getItemCount()
            }
        }

        override fun getItemViewType(position: Int): Int {
            return getDefItemViewType(position)
        }

        //设置Item的大小
        private fun setImageViewLayoutParams(view: ImageView) {
            //分隔符宽度
            val itemDecorationSum: Int = 3 * mItemDecoration
            //dp  变  px
            val paddingLeft: Int = ConvertUtils.dp2px(30f)
            //获取屏幕的宽度
            val screenWidth = ScreenUtils.getScreenWidth(mContext)

            val size = (screenWidth - itemDecorationSum - paddingLeft * 2) / 4
            //设置FrameLayout的宽高
            val params = FrameLayout.LayoutParams(size, size)
            //设置ImageView的宽高
            view.layoutParams = params
        }
    }

}