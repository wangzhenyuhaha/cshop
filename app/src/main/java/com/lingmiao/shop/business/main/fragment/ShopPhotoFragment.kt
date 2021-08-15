package com.lingmiao.shop.business.main.fragment

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.james.common.base.BasePreImpl
import com.james.common.base.BasePresenter
import com.james.common.base.BaseVBFragment
import com.lingmiao.shop.base.CommonRepository
import com.lingmiao.shop.business.common.pop.MediaMenuPop
import com.lingmiao.shop.business.main.ApplyShopInfoActivity
import com.lingmiao.shop.business.photo.GlideEngine
import com.lingmiao.shop.databinding.FragmentShopPhotoBinding
import com.lingmiao.shop.util.GlideUtils
import com.lingmiao.shop.util.OtherUtils
import com.luck.picture.lib.PictureSelector
import com.luck.picture.lib.config.PictureMimeType
import com.luck.picture.lib.entity.LocalMedia
import com.luck.picture.lib.listener.OnResultCallbackListener
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ShopPhotoFragment : BaseVBFragment<FragmentShopPhotoBinding, BasePresenter>() {

    private val model by activityViewModels<ApplyShopInfoActivity.ApplyShopInfoViewModel>()

    private var type: Int = 0

    override fun createPresenter(): BasePresenter {
        return BasePreImpl(this)
    }

    override fun getBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ) = FragmentShopPhotoBinding.inflate(inflater, container, false)


    override fun initViewsAndData(rootView: View) {

        type = arguments?.getInt("type") ?: 0


        when (type) {
            ApplyShopInfoActivity.SHOP_FRONT -> {
                binding.linearLayout.visibility = View.VISIBLE
                binding.imageView.visibility = View.GONE

                model.applyShopInfo.value?.shopPhotoFront?.also {
                    GlideUtils.setImageUrl(binding.shopFront, it)
                }
                model.applyShopInfo.value?.shopPhotoInside?.also {
                    GlideUtils.setImageUrl(binding.shopInside, it)
                }
                model.setTitle("上传店铺照片")
            }
            ApplyShopInfoActivity.LICENSE -> {

                binding.linearLayout.visibility = View.GONE
                binding.imageView.visibility = View.VISIBLE

                model.applyShopInfo.value?.licenceImg?.also {
                    GlideUtils.setImageUrl(binding.imageView, it)
                }

                model.setTitle("上传营业执照")
            }
            else -> {

            }
        }





        initListener()

    }

    private fun initListener() {


        binding.shopFront.setOnClickListener {
            setOnClickForPhoto(type, 1)
        }


        binding.shopInside.setOnClickListener {
            setOnClickForPhoto(type, 2)
        }

        binding.imageView.setOnClickListener {
            setOnClickForPhoto(type, 1)
        }

        binding.backTextView.setOnClickListener {
            findNavController().navigateUp()
        }


    }

    private fun setOnClickForPhoto(type: Int, number: Int) {
        val pop =
            MediaMenuPop(
                requireActivity(),
                MediaMenuPop.TYPE_SELECT_PHOTO or MediaMenuPop.TYPE_PLAY_PHOTO
            )
        pop.setOnClickListener { flags ->
            run {
                when (flags) {
                    MediaMenuPop.TYPE_SELECT_PHOTO -> {
                        PictureSelector.create(this)
                            .openGallery(PictureMimeType.ofImage())
                            .maxSelectNum(1)
                            .loadImageEngine(GlideEngine.createGlideEngine())
                            .forResult(object : OnResultCallbackListener<LocalMedia?> {
                                override fun onResult(result: List<LocalMedia?>) {
                                    // 结果回调
                                    val localMedia = result[0]
                                    showAndUploadImage(localMedia, type, number)
                                }

                                override fun onCancel() {
                                    // 取消
                                }
                            })
                    }

                    MediaMenuPop.TYPE_PLAY_PHOTO -> {
                        PictureSelector.create(this)
                            .openCamera(PictureMimeType.ofImage())
                            .maxSelectNum(1)
                            .loadImageEngine(GlideEngine.createGlideEngine())
                            .forResult(object : OnResultCallbackListener<LocalMedia?> {
                                override fun onResult(result: List<LocalMedia?>) {
                                    // 结果回调
                                    val localMedia = result[0]
                                    showAndUploadImage(localMedia, type, number)
                                }

                                override fun onCancel() {
                                    // 取消
                                }
                            })
                    }
                }
            }
        }
        pop.showPopupWindow()
    }

    private fun showAndUploadImage(localMedia: LocalMedia?, type: Int, number: Int) {

        lifecycleScope.launch(Dispatchers.Main)
        {
            val uploadFile = CommonRepository.uploadFile(OtherUtils.getImageFile(localMedia), true)
            if (uploadFile.isSuccess) {
                when (type) {
                    ApplyShopInfoActivity.SHOP_FRONT -> {
                        if (number == 1) {
                            model.applyShopInfo.value?.shopPhotoFront = uploadFile.data.url
                            uploadFile.data.url?.also {
                                GlideUtils.setImageUrl(binding.shopFront, it, 12f)
                            }
                        } else {
                            model.applyShopInfo.value?.shopPhotoInside = uploadFile.data.url
                            uploadFile.data.url?.also {
                                GlideUtils.setImageUrl(binding.shopInside, it, 12f)
                            }
                        }
                    }
                    ApplyShopInfoActivity.LICENSE -> {
                        model.applyShopInfo.value?.licenceImg = uploadFile.data.url
                        uploadFile.data.url?.also {
                            GlideUtils.setImageUrl(binding.imageView, it, 12f)
                            model.getOCR.value = 8
                        }
                    }
                }
                showToast("上传成功")
            }
        }

    }


}