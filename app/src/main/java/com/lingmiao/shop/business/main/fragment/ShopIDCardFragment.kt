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
import com.james.common.utils.exts.gone
import com.james.common.utils.exts.visiable
import com.lingmiao.shop.R
import com.lingmiao.shop.base.CommonRepository
import com.lingmiao.shop.business.common.pop.MediaMenuPop
import com.lingmiao.shop.business.main.ApplyShopInfoActivity
import com.lingmiao.shop.business.main.ApplyShopInfoViewModel
import com.lingmiao.shop.business.photo.GlideEngine
import com.lingmiao.shop.databinding.FragmentShopIdCardBinding
import com.lingmiao.shop.util.OtherUtils
import com.luck.picture.lib.PictureSelector
import com.luck.picture.lib.config.PictureMimeType
import com.luck.picture.lib.entity.LocalMedia
import com.luck.picture.lib.listener.OnResultCallbackListener
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ShopIDCardFragment : BaseVBFragment<FragmentShopIdCardBinding, BasePresenter>() {

    private val model by activityViewModels<ApplyShopInfoViewModel>()

    private var type: Int = 0

    override fun createPresenter() = BasePreImpl(this)


    override fun getBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ) = FragmentShopIdCardBinding.inflate(inflater, container, false)


    override fun initViewsAndData(rootView: View) {

        type = arguments?.getInt("type") ?: 0

        when (type) {
            ApplyShopInfoActivity.ID_CARD_FRONT -> {

                model.setTitle("上传身份证照片")
                //  人像
                if (model.applyShopInfo.value?.legalBackImg == null) {
                    Glide.with(requireActivity()).load(R.mipmap.main_shop_hint_2)
                        .into(binding.legalBackImgImageView)
                } else {
                    Glide.with(requireActivity()).load(model.applyShopInfo.value?.legalBackImg)
                        .into(binding.legalBackImgImageView)
                }

                //国徽
                if (model.applyShopInfo.value?.legalImg == null) {
                    Glide.with(requireActivity()).load(R.mipmap.main_shop_hint_1)
                        .into(binding.legalImgImageView)
                } else {
                    Glide.with(requireActivity()).load(model.applyShopInfo.value?.legalImg)
                        .into(binding.legalImgImageView)
                }

                //法人手持身份证照片
                if (model.applyShopInfo.value?.holdImg == null) {
                    Glide.with(requireActivity()).load(R.mipmap.main_shop_hint_3)
                        .into(binding.holdImgImageView)
                } else {
                    Glide.with(requireActivity()).load(model.applyShopInfo.value?.holdImg)
                        .into(binding.holdImgImageView)
                }
            }
            ApplyShopInfoActivity.PERSONAL_SHOP -> {
                model.setTitle("上传店铺照片")
                binding.textView.gone()


                //店铺门头照片
                if (model.applyShopInfo.value?.shopPhotoFront == null) {
                    Glide.with(requireActivity()).load(R.mipmap.main_shop_image_upload)
                        .into(binding.legalBackImgImageView)
                } else {
                    Glide.with(requireActivity()).load(model.applyShopInfo.value?.shopPhotoFront)
                        .into(binding.legalBackImgImageView)
                }

                binding.textView1.text = "店铺门头照片"
                binding.textView1.visiable()

                //经营内景照片
                if (model.applyShopInfo.value?.shopPhotoInside == null) {
                    Glide.with(requireActivity()).load(R.mipmap.main_shop_image_upload)
                        .into(binding.legalImgImageView)
                } else {
                    Glide.with(requireActivity()).load(model.applyShopInfo.value?.shopPhotoInside)
                        .into(binding.legalImgImageView)
                }

                binding.textView2.text = "经营内景照片"
                binding.textView2.visiable()

                //经营者和店铺门头合照
                if (model.applyShopInfo.value?.peasonheadpic == null) {
                    Glide.with(requireActivity()).load(R.mipmap.main_shop_image_upload)
                        .into(binding.holdImgImageView)
                } else {
                    Glide.with(requireActivity()).load(model.applyShopInfo.value?.peasonheadpic)
                        .into(binding.holdImgImageView)
                }

                binding.textView3.text = "经营者和店铺门头合照"
                binding.textView3.visiable()

            }
        }



        initListener()
    }


    private fun initListener() {


        //内景,国徽
        binding.legalImgImageView.setOnClickListener {
            setOnClickForPhoto(type, 2)
        }

        //门头
        binding.legalBackImgImageView.setOnClickListener {
            setOnClickForPhoto(type, 1)
        }

        //合照，手持
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
                            2 -> {
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
                            1 -> {
                                //人像
                                model.applyShopInfo.value?.legalBackImg = uploadFile.data.url
                                uploadFile.data.url?.also {
                                    Glide.with(requireActivity()).load(it)
                                        .into(binding.legalBackImgImageView)
                                }
                                model.applyShopInfo.value?.also {
                                    it.legalName = null
                                    it.legalSex = null
                                    it.legalId = null
                                    it.legal_address = null
                                    model.personalAccount.value?.openAccountName = null
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
                    ApplyShopInfoActivity.PERSONAL_SHOP ->{
                        when(number){
                            1->{
                                //门头
                                model.applyShopInfo.value?.shopPhotoFront = uploadFile.data.url
                                uploadFile.data.url?.also {
                                    Glide.with(requireActivity()).load(it)
                                        .into(binding.legalBackImgImageView)
                                }

                            }
                            2->{
                                //内景
                                model.applyShopInfo.value?.shopPhotoInside = uploadFile.data.url
                                uploadFile.data.url?.also {
                                    Glide.with(requireActivity()).load(it)
                                        .into(binding.legalImgImageView)
                                }

                            }
                            3->{
                                //合照
                                model.applyShopInfo.value?.peasonheadpic = uploadFile.data.url
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