package com.lingmiao.shop.business.main.fragment

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.james.common.base.BasePreImpl
import com.james.common.base.BasePresenter
import com.james.common.base.BaseVBFragment
import com.james.common.utils.DialogUtils
import com.james.common.utils.exts.checkBoolean
import com.james.common.utils.exts.checkNotBlack
import com.lingmiao.shop.R
import com.lingmiao.shop.business.main.ApplyShopInfoActivity
import com.lingmiao.shop.databinding.FragmentShopInfoBinding

class ShopInfoFragment : BaseVBFragment<FragmentShopInfoBinding, BasePresenter>() {

    private val model by activityViewModels<ApplyShopInfoActivity.ApplyShopInfoViewModel>()

    override fun createPresenter(): BasePresenter {
        return BasePreImpl(this)
    }

    override fun getBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ) = FragmentShopInfoBinding.inflate(inflater, container, false)


    override fun initViewsAndData(rootView: View) {

        model.setTitle("资料提交")

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

        //填写邀请码
        binding.shopInfopromoCode.setOnClickListener {
            DialogUtils.showInputDialog(
                requireActivity(),
                "推广码",
                "",
                "请输入",
                model.applyShopInfo.value?.promoCode,
                "取消",
                "保存",
                null
            ) {
                binding.shopInfopromoCodeTextView.text = it
                model.applyShopInfo.value?.promoCode = it
            }
        }

        //营业执照
        binding.license.setOnClickListener {
            val bundle = bundleOf("type" to ApplyShopInfoActivity.LICENSE)
            findNavController().navigate(R.id.action_shopInfoFragment_to_shopPhotoFragment, bundle)
        }

        //店铺照片
        binding.shopPhoto.setOnClickListener {
            val bundle = bundleOf("type" to ApplyShopInfoActivity.SHOP_FRONT)
            findNavController().navigate(R.id.action_shopInfoFragment_to_shopPhotoFragment, bundle)
        }

        //法人身份证照片
        binding.IDCard.setOnClickListener {
            val bundle = bundleOf("type" to ApplyShopInfoActivity.ID_CARD_FRONT)
            findNavController().navigate(R.id.action_shopInfoFragment_to_shopIDCardFragment,bundle)
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

            //店铺类型
            if (it.shopType != null) {
                if (it.shopType == 3) {
                    //企业
                    binding.shopInfoSelfImageView.isSelected = true
                    binding.shopInfoCompanyImageView.isSelected = false
                } else {
                    //个体户
                    binding.shopInfoSelfImageView.isSelected = false
                    binding.shopInfoCompanyImageView.isSelected = true
                }
            }
            //基础资料
            if (!it.promoCode.isNullOrEmpty()) binding.shopInfopromoCodeTextView.text = it.promoCode
            if (!it.licenceImg.isNullOrEmpty()) binding.tvLicense.text = "已上传"
            if (!(it.shopPhotoFront.isNullOrEmpty() || it.shopPhotoInside.isNullOrEmpty())) binding.tvShopPhoto.text =
                "已上传"
            if (!(it.legalImg.isNullOrEmpty() || it.legalBackImg.isNullOrEmpty() || it.holdImg.isNullOrEmpty())) binding.tvIDCard.text =
                "已上传"

        })


    }

}