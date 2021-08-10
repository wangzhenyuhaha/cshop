package com.lingmiao.shop.business.main.fragment

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.james.common.base.BasePreImpl
import com.james.common.base.BasePresenter
import com.james.common.base.BaseVBFragment
import com.james.common.utils.exts.checkBoolean
import com.james.common.utils.exts.checkNotBlack
import com.lingmiao.shop.R
import com.lingmiao.shop.business.main.ApplyShopInfoActivity
import com.lingmiao.shop.business.main.bean.ApplyShopImageEvent
import com.lingmiao.shop.databinding.FragmentShopInfoBinding
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode

class ShopInfoFragment : BaseVBFragment<FragmentShopInfoBinding, BasePresenter>() {

    private val model by activityViewModels<ApplyShopInfoActivity.ApplyShopInfoViewModel>()

    override fun createPresenter(): BasePresenter {
        return BasePreImpl(this)
    }

    override fun getBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ) = FragmentShopInfoBinding.inflate(inflater, container, false)


    override fun useEventBus(): Boolean {
        return true
    }


    override fun initViewsAndData(rootView: View) {

        model.setTitle("资料提交")

//        if (model.applyShopInfo.value?.legalId == null)
//        {
//            Log.d("WZY 1","NULL")
//        }
//        if (model.applyShopInfo.value?.legalId?.isEmpty() == true)
//        {
//            Log.d("WZY 1","空的")
//        }

            initListener()
        initObserver()

    }

    private fun initListener() {

        //个体户
        binding.shopInfoSelf.setOnClickListener {
            binding.shopInfoSelfImageView.isSelected = true
            binding.shopInfoCompanyImageView.isSelected = false
            model.applyShopInfo.value?.shopType = 3
        }

        //企业
        binding.shopInfoCompany.setOnClickListener {
            binding.shopInfoSelfImageView.isSelected = false
            binding.shopInfoCompanyImageView.isSelected = true
            model.applyShopInfo.value?.shopType = 1
        }

        //营业执照
        binding.license.setOnClickListener {
            ApplyShopInfoActivity.goUploadImageActivity(
                requireActivity(),
                ApplyShopInfoActivity.LICENSE,
                "上传营业执照",
                model.applyShopInfo.value?.licenceImg
            )
        }

        //店铺照片
        binding.shopPhoto.setOnClickListener {
            findNavController().navigate(R.id.action_shopInfoFragment_to_shopPhotoFragment)
        }

        //法人身份证照片
        binding.IDCard.setOnClickListener {
            findNavController().navigate(R.id.action_shopInfoFragment_to_shopIDCardFragment)
        }

        //下一步
        binding.nextTextView.setOnClickListener {
            try {

                checkBoolean(model.applyShopInfo.value?.shopType != null) {
                    "请选择店铺类型"
                }
                checkNotBlack(model.applyShopInfo.value?.licenceImg) {
                    "请上传营业执照"
                }
                checkNotBlack(model.applyShopInfo.value?.shopPhotoFront) {
                    "请上传店铺门头照片"
                }
                checkNotBlack(model.applyShopInfo.value?.shopPhotoInside) {
                    "请上传店铺内景照片"
                }
                checkNotBlack(model.applyShopInfo.value?.legalImg) {
                    "请上传身份证国徽面照片"
                }
                checkNotBlack(model.applyShopInfo.value?.legalBackImg) {
                    "请上传身份证人像面照片"
                }
                checkNotBlack(model.applyShopInfo.value?.holdImg) {
                    "请上传手持身份证照片"
                }

                findNavController().navigate(R.id.action_shopInfoFragment_to_replenishInfoFragment)

            } catch (e: IllegalStateException) {
                e.printStackTrace()
                showToast(e.message ?: "")
            }
        }


    }


    private fun initObserver() {

        model.applyShopInfo.observe(this, Observer {
            if (model.applyShopInfo.value?.legalId == null)
            {
                Log.d("WZY 12","NULL")
            }
            if (model.applyShopInfo.value?.legalId?.isEmpty() == true)
            {
                Log.d("WZY 12","空的")
            }


            if (it.shopName != null) {

                //店铺类型
                if (it.shopType != null) {
                    if (it.shopType == 3) {
                        binding.shopInfoSelfImageView.isSelected = true
                        binding.shopInfoCompanyImageView.isSelected = false
                    } else {
                        binding.shopInfoSelfImageView.isSelected = false
                        binding.shopInfoCompanyImageView.isSelected = true
                    }
                }

                //基础资料
                if (!it.licenceImg.isNullOrEmpty()) binding.tvLicense.text = "已上传"
                if (!(it.shopPhotoFront.isNullOrEmpty() || it.shopPhotoInside.isNullOrEmpty())) binding.tvShopPhoto.text =
                    "已上传"
                if (!(it.legalImg.isNullOrEmpty() || it.legalBackImg.isNullOrEmpty() || it.holdImg.isNullOrEmpty())) binding.tvIDCard.text =
                    "已上传"
            }
        })


    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun getUploadImageUrl(event: ApplyShopImageEvent) {
        when (event.type) {
            ApplyShopInfoActivity.LICENSE -> {
                model.applyShopInfo.value?.licenceImg = event.remoteUrl
                binding.tvLicense.text = "已上传"
            }

        }
    }


}