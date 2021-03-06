package com.lingmiao.shop.business.main.fragment

import android.graphics.BitmapFactory
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.james.common.base.BasePreImpl
import com.james.common.base.BasePresenter
import com.james.common.base.BaseVBFragment
import com.james.common.utils.exts.visiable
import com.lingmiao.shop.R
import com.lingmiao.shop.base.CommonRepository
import com.lingmiao.shop.business.common.pop.MediaMenuPop
import com.lingmiao.shop.business.main.ApplyShopInfoActivity
import com.lingmiao.shop.business.main.ApplyShopInfoViewModel
import com.lingmiao.shop.business.photo.GlideEngine
import com.lingmiao.shop.databinding.FragmentShopPhotoBinding
import com.lingmiao.shop.util.OtherUtils
import com.luck.picture.lib.PictureSelector
import com.luck.picture.lib.config.PictureMimeType
import com.luck.picture.lib.entity.LocalMedia
import com.luck.picture.lib.listener.OnResultCallbackListener
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ShopPhotoFragment : BaseVBFragment<FragmentShopPhotoBinding, BasePresenter>() {

    private val model by activityViewModels<ApplyShopInfoViewModel>()

    private var type: Int = 0

    override fun createPresenter() = BasePreImpl(this)


    override fun getBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ) = FragmentShopPhotoBinding.inflate(inflater, container, false)

    override fun initViewsAndData(rootView: View) {
        type = arguments?.getInt("type") ?: 0

        when (type) {

            ApplyShopInfoActivity.HIRE -> {
                binding.textView.visiable()
                binding.linearLayout.visibility = View.GONE
                binding.imageView.visibility = View.VISIBLE
                model.applyShopInfo.value?.bizplacepic?.also {
                    Glide.with(requireActivity()).load(it).into(binding.imageView)
                }
                model.setTitle("????????????????????????")
            }

            ApplyShopInfoActivity.LICENSE -> {
                binding.textView.visiable()
                binding.linearLayout.visibility = View.GONE
                binding.imageView.visibility = View.VISIBLE
                model.applyShopInfo.value?.licenceImg?.also {
                    Glide.with(requireActivity()).load(it).into(binding.imageView)
                }
                model.setTitle("??????????????????")
            }

            ApplyShopInfoActivity.TAXES -> {
                binding.textView.visiable()
                binding.linearLayout.visibility = View.GONE
                binding.imageView.visibility = View.VISIBLE
                model.applyShopInfo.value?.taxes_certificate_img?.also {
                    Glide.with(requireActivity()).load(it).into(binding.imageView)
                }
                model.setTitle("???????????????????????????")
            }

            ApplyShopInfoActivity.ORGAN -> {
                binding.textView.visiable()
                binding.linearLayout.visibility = View.GONE
                binding.imageView.visibility = View.VISIBLE
                model.applyShopInfo.value?.orgcodepic?.also {
                    Glide.with(requireActivity()).load(it).into(binding.imageView)
                }
                model.setTitle("?????????????????????????????????")
            }

            ApplyShopInfoActivity.SHOP_FRONT -> {
                binding.linearLayout.visibility = View.VISIBLE
                binding.imageView.visibility = View.GONE
                model.applyShopInfo.value?.shopPhotoFront?.also {
                    Glide.with(requireActivity()).load(it).into(binding.shopFront)
                }
                model.applyShopInfo.value?.shopPhotoInside?.also {
                    Glide.with(requireActivity()).load(it).into(binding.shopInside)
                }
                model.setTitle("??????????????????")
            }

            ApplyShopInfoActivity.OTHER_PIC -> {
                binding.textView.visiable()
                binding.linearLayout.visibility = View.VISIBLE
                binding.imageView.visibility = View.GONE
                model.applyShopInfo.value?.other_Pic_One?.also {
                    Glide.with(requireActivity()).load(it).into(binding.shopFront)
                }
                model.applyShopInfo.value?.other_Pic_Two?.also {
                    Glide.with(requireActivity()).load(it).into(binding.shopInside)
                }
                binding.textView1.visibility = View.INVISIBLE
                binding.textView2.visibility = View.INVISIBLE
                model.setTitle("????????????????????????")
            }

            ApplyShopInfoActivity.FOOD_ALLOW -> {
                binding.textView.visiable()
                binding.linearLayout.visibility = View.GONE
                binding.imageView.visibility = View.VISIBLE
                model.applyShopInfo.value?.foodAllow?.also {
                    Glide.with(requireActivity()).load(it).into(binding.imageView)
                }
                model.setTitle("?????????????????????????????????")
            }

            ApplyShopInfoActivity.PICTURE_COMPANY_ACCOUNT -> {
                binding.linearLayout.visibility = View.GONE
                binding.imageView.visibility = View.VISIBLE
                model.companyAccount.value?.bankUrls?.also {
                    Glide.with(requireActivity()).load(it).into(binding.imageView)
                }
                model.setTitle("???????????????????????????")
            }

            ApplyShopInfoActivity.PICTURE_PERSONAL_ACCOUNT -> {
                binding.linearLayout.visibility = View.GONE
                binding.imageView.visibility = View.VISIBLE
                model.personalAccount.value?.bankUrls?.also {
                    Glide.with(requireActivity()).load(it).into(binding.imageView)
                }
                model.setTitle("????????????????????????")
            }

            ApplyShopInfoActivity.AUTHOR_PIC -> {
                binding.linearLayout.visibility = View.GONE
                binding.imageView.visibility = View.VISIBLE
                model.applyShopInfo.value?.authorpic?.also {
                    Glide.with(requireActivity()).load(it).into(binding.imageView)
                }
                model.setTitle("???????????????????????????")
            }

            else -> {

            }
        }

        initListener()
    }

    private fun initListener() {

        //????????????????????????
        if (type == ApplyShopInfoActivity.LICENSE) {
            binding.deleteTextView.visiable()
            binding.deleteTextView.setOnClickListener {
//  model.applyShopInfo.value?.licenceImg = uploadFile.data.url


                model.applyShopInfo.value?.licenceImg = null
                //???????????????????????????????????????????????????
                model.applyShopInfo.value?.also { info ->
                    info.companyName = null
                    info.licenseNum = null
                    info.licenceEnd = null
                    info.scope = null
                    info.regMoney = 1
                }
                model.companyAccount.value?.openAccountName = null
                binding.imageView.setImageBitmap(null)
                binding.imageView.setImageBitmap(
                    BitmapFactory.decodeResource(
                        resources,
                        R.mipmap.main_shop_image_upload,
                        null
                    )
                )
            }
        }

        if (!model.shopOpenOrNot) {
            binding.shopFront.setOnClickListener {
                setOnClickForPhoto(type, 1)
            }

            binding.shopInside.setOnClickListener {
                setOnClickForPhoto(type, 2)
            }

            //?????????????????????
            binding.imageView.setOnClickListener {
                setOnClickForPhoto(type, 1)
            }
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
                                    // ????????????
                                    val localMedia = result[0]
                                    showAndUploadImage(localMedia, type, number)
                                }

                                override fun onCancel() {
                                    // ??????
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
                                    // ????????????
                                    val localMedia = result[0]
                                    showAndUploadImage(localMedia, type, number)
                                }

                                override fun onCancel() {
                                    // ??????
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
                                Glide.with(requireActivity()).load(it).into(binding.shopFront)
                            }
                        } else {
                            model.applyShopInfo.value?.shopPhotoInside = uploadFile.data.url
                            uploadFile.data.url?.also {
                                Glide.with(requireActivity()).load(it).into(binding.shopInside)
                            }
                        }
                    }


                    ApplyShopInfoActivity.OTHER_PIC -> {
                        if (number == 1) {
                            //?????????
                            model.applyShopInfo.value?.other_Pic_One = uploadFile.data.url
                            uploadFile.data.url?.also {
                                Glide.with(requireActivity()).load(it).into(binding.shopFront)
                            }
                        } else {
                            //?????????
                            model.applyShopInfo.value?.other_Pic_Two = uploadFile.data.url
                            uploadFile.data.url?.also {
                                Glide.with(requireActivity()).load(it).into(binding.shopInside)
                            }
                        }
                    }
                    //??????????????????
                    ApplyShopInfoActivity.HIRE -> {
                        model.applyShopInfo.value?.bizplacepic = uploadFile.data.url
                        uploadFile.data.url?.also {
                            Glide.with(requireActivity()).load(it).into(binding.imageView)
                        }

                    }

                    //??????????????????
                    ApplyShopInfoActivity.LICENSE -> {
                        model.applyShopInfo.value?.licenceImg = uploadFile.data.url
                        uploadFile.data.url?.also {
                            //????????????OCR?????????????????????
                            model.applyShopInfo.value?.also { info ->
                                info.companyName = null
                                info.licenseNum = null
                                info.licenceEnd = null
                                info.scope = null
                                info.regMoney = 1
                            }
                            model.companyAccount.value?.openAccountName = null
                            Glide.with(requireActivity()).load(it).into(binding.imageView)
                            model.getOCR.value = 8
                        }
                    }


                    ApplyShopInfoActivity.TAXES -> {
                        model.applyShopInfo.value?.taxes_certificate_img = uploadFile.data.url
                        uploadFile.data.url?.also {
                            Glide.with(requireActivity()).load(it).into(binding.imageView)
                        }
                    }


                    ApplyShopInfoActivity.ORGAN -> {
                        model.applyShopInfo.value?.orgcodepic = uploadFile.data.url
                        uploadFile.data.url?.also {
                            Glide.with(requireActivity()).load(it).into(binding.imageView)
                        }
                    }

                    ApplyShopInfoActivity.FOOD_ALLOW -> {
                        model.applyShopInfo.value?.foodAllow = uploadFile.data.url
                        uploadFile.data.url?.also {
                            Glide.with(requireActivity()).load(it).into(binding.imageView)
                        }
                    }


                    ApplyShopInfoActivity.PICTURE_COMPANY_ACCOUNT -> {
                        model.companyAccount.value?.bankUrls = uploadFile.data.url
                        uploadFile.data.url?.also {
                            Glide.with(requireActivity()).load(it).into(binding.imageView)
                        }
                    }
                    ApplyShopInfoActivity.PICTURE_PERSONAL_ACCOUNT -> {
                        model.personalAccount.value?.bankUrls = uploadFile.data.url
                        uploadFile.data.url?.also {
                            Glide.with(requireActivity()).load(it).into(binding.imageView)
                            model.getOCR.value = 6
                        }

                    }
                    ApplyShopInfoActivity.AUTHOR_PIC -> {
                        model.applyShopInfo.value?.authorpic = uploadFile.data.url
                        uploadFile.data.url?.also {
                            Glide.with(requireActivity()).load(it).into(binding.imageView)
                        }
                    }
                }
                showToast("????????????")
            }
        }

    }


}