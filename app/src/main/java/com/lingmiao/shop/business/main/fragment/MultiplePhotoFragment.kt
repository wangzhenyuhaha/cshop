package com.lingmiao.shop.business.main.fragment

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.james.common.base.BasePreImpl
import com.james.common.base.BasePresenter
import com.james.common.base.BaseVBFragment
import com.james.common.netcore.networking.http.core.HiResponse
import com.james.common.utils.exts.isNetUrl
import com.james.common.utils.exts.visiable
import com.lingmiao.shop.base.CommonRepository
import com.lingmiao.shop.business.common.bean.FileResponse
import com.lingmiao.shop.business.common.pop.MediaMenuPop
import com.lingmiao.shop.business.goods.api.bean.GoodsGalleryVO
import com.lingmiao.shop.business.main.ApplyShopInfoActivity
import com.lingmiao.shop.business.main.ApplyShopInfoViewModel
import com.lingmiao.shop.business.photo.GlideEngine
import com.lingmiao.shop.databinding.FragmentMultiplePhotoBinding
import com.lingmiao.shop.util.OtherUtils
import com.luck.picture.lib.PictureSelector
import com.luck.picture.lib.config.PictureMimeType
import com.luck.picture.lib.entity.LocalMedia
import com.luck.picture.lib.listener.OnResultCallbackListener
import kotlinx.android.synthetic.main.me_activity_feedback.*
import kotlinx.android.synthetic.main.me_activity_feedback.galleryRv
import kotlinx.android.synthetic.main.me_fragment_shop_operate_setting.*
import kotlinx.coroutines.*
import java.io.File

class MultiplePhotoFragment : BaseVBFragment<FragmentMultiplePhotoBinding, BasePresenter>() {

    private val model by activityViewModels<ApplyShopInfoViewModel>()

    private var type: Int = 0

    override fun createPresenter() = BasePreImpl(this)

    override fun getBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ) = FragmentMultiplePhotoBinding.inflate(inflater, container, false)

    override fun initViewsAndData(rootView: View) {
        type = arguments?.getInt("type") ?: 0

        when (type) {

            //获取已有图片
            ApplyShopInfoActivity.HIRE -> {

                model.applyShopInfo.value?.bizplacepic?.also {

                    val list = mutableListOf<GoodsGalleryVO>()
                    it.split(",").forEach { url ->
                        list.add(GoodsGalleryVO(original = url, sort = null))
                    }
                    galleryRv.addDataList(list)
                }

                model.setTitle("上传店铺租聘合同")
            }
            else -> {

            }
        }

        initListener()
    }

    private fun initListener() {
        //设置
        galleryRv.setCountLimit(1, 30)

        //获取图片地址
        val gallery = galleryRv.getSelectPhotos()

        binding.backTextView.setOnClickListener {
            //开始上传图片
            showDialogLoading("正在上传")

            uploadImages(gallery, fail = {
                showToast("图片上传失败，请重试")
                hideDialogLoading()
                return@uploadImages
            }, success = {
                //开始调用反馈接口
                val temp: StringBuilder = StringBuilder()

                gallery.also { list ->
                    for (i in list) {
                        temp.append(i.original.toString())
                        temp.append(",")
                    }
                }
                if (temp.isEmpty()) {
                    model.applyShopInfo.value?.bizplacepic = null
                } else {
                    temp.deleteCharAt(temp.indexOfLast { it == ',' })
                    model.applyShopInfo.value?.bizplacepic = temp.toString()
                }

                hideDialogLoading()
                findNavController().navigateUp()
            })


        }
    }

    //图片上传，
    private fun uploadImages(list: List<GoodsGalleryVO>?, fail: () -> Unit, success: () -> Unit) {
        lifecycleScope.launch(Dispatchers.Main) {
            val requestList = ArrayList<Deferred<HiResponse<FileResponse>>>()
            // 多张图片并行上传
            list!!.forEach {
                val request = async {
                    if (it.original.isNetUrl()) {
                        HiResponse(0, "", FileResponse("", "", it.original))
                    } else {
                        CommonRepository.uploadFile(
                            File(it.original!!),
                            true,
                            CommonRepository.SCENE_GOODS
                        )
                    }
                }
                requestList.add(request)
            }

            // 多个接口相互等待
            val respList = requestList.awaitAll()
            var isAllSuccess = true
            respList.forEachIndexed { index, it ->
                if (it.isSuccess) {
                    list[index].apply {
                        original = it.data?.url
                        sort = "$index"
                    }
                } else {
                    isAllSuccess = false
                }
            }
            if (isAllSuccess) {
                success.invoke()
            } else {
                fail.invoke()
            }
        }
    }


}