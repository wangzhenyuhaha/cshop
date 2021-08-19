package com.lingmiao.shop.business.main.fragment

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
import com.lingmiao.shop.base.CommonRepository
import com.lingmiao.shop.business.common.pop.MediaMenuPop
import com.lingmiao.shop.business.main.ApplyShopInfoActivity
import com.lingmiao.shop.business.photo.GlideEngine
import com.lingmiao.shop.databinding.FragmentShopIdCardBinding
import com.lingmiao.shop.util.GlideUtils
import com.lingmiao.shop.util.OtherUtils
import com.luck.picture.lib.PictureSelector
import com.luck.picture.lib.config.PictureMimeType
import com.luck.picture.lib.entity.LocalMedia
import com.luck.picture.lib.listener.OnResultCallbackListener
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ShopIDCardFragment : BaseVBFragment<FragmentShopIdCardBinding, BasePresenter>() {

    private val model by activityViewModels<ApplyShopInfoActivity.ApplyShopInfoViewModel>()

    private var type: Int = 0

    override fun createPresenter(): BasePresenter {
        return BasePreImpl(this)
    }

    override fun getBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ) = FragmentShopIdCardBinding.inflate(inflater, container, false)


    override fun initViewsAndData(rootView: View) {

        model.setTitle("上传身份证照片")
        type = arguments?.getInt("type") ?: 0

        //  人像
        model.applyShopInfo.value?.legalBackImg?.also {
            Glide.with(requireActivity()).load(it).into(binding.legalBackImgImageView)
        }

        //  国徽
        model.applyShopInfo.value?.legalImg?.also {
            Glide.with(requireActivity()).load(it).into(binding.legalImgImageView)
        }

        //法人手持身份证照片
        model.applyShopInfo.value?.holdImg?.also {
            Glide.with(requireActivity()).load(it).into(binding.holdImgImageView)
        }

        initListener()
    }


    private fun initListener() {


        //国徽
        binding.legalImgImageView.setOnClickListener {
            setOnClickForPhoto(type, 1)
        }

        //人像
        binding.legalBackImgImageView.setOnClickListener {
            setOnClickForPhoto(type, 2)
        }

        //手持身份证
        binding.holdImgImageView.setOnClickListener {
            setOnClickForPhoto(type, 3)
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
                    ApplyShopInfoActivity.ID_CARD_FRONT -> {
                        when (number) {
                            1 -> {
                                //国徽
                                model.applyShopInfo.value?.legalImg = uploadFile.data.url
                                uploadFile.data.url?.also {
                                    Glide.with(requireActivity()).load(it)
                                        .into(binding.legalImgImageView)
                                }
                                model.applyShopInfo.value?.legalIDExpire = null
                                //启用OCR识别
                                model.getOCR.value = 2
                            }
                            2 -> {
                                //人像
                                model.applyShopInfo.value?.legalBackImg = uploadFile.data.url
                                uploadFile.data.url?.also {
                                    Glide.with(requireActivity()).load(it)
                                        .into(binding.legalBackImgImageView)
                                }
                                model.applyShopInfo.value?.also {
                                    it.legalName = ""
                                    it.legalSex = null
                                    it.legalId = ""
                                    it.legal_address = ""
                                }
                                //启用OCR识别
                                model.getOCR.value = 1
                            }
                            else -> {
                                //手持身份证
                                model.applyShopInfo.value?.holdImg = uploadFile.data.url
                                uploadFile.data.url?.also {
                                    Glide.with(requireActivity()).load(it)
                                        .into(binding.holdImgImageView)
                                }
                            }
                        }
                    }
                }
                showToast("上传成功")
            }
        }

    }

}