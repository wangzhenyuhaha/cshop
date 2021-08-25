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
import com.james.common.utils.exts.checkBoolean
import com.james.common.utils.exts.checkNotBlack
import com.lingmiao.shop.R
import com.lingmiao.shop.base.UserManager
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

        //处理点击事件
        initListener()
        //处理数据的显示
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

        //是否三证合一
        //Yes
        binding.thrcertflagYes.setOnClickListener {
            model.applyShopInfo.value?.thrcertflag = 1
            binding.thrcertflagYes.isSelected = true
            binding.thrcertflagNO.isSelected = false
            binding.taxes.visibility = View.GONE
            binding.organ.visibility = View.GONE
            binding.view1.visibility = View.GONE
            binding.view2.visibility = View.GONE
        }

        //No
        binding.thrcertflagNO.setOnClickListener {
            model.applyShopInfo.value?.thrcertflag = 0
            binding.thrcertflagYes.isSelected = false
            binding.thrcertflagNO.isSelected = true
            binding.taxes.visibility = View.VISIBLE
            binding.organ.visibility = View.VISIBLE
            binding.view1.visibility = View.VISIBLE
            binding.view2.visibility = View.VISIBLE
        }

        //营业执照
        binding.license.setOnClickListener {
            val bundle = bundleOf("type" to ApplyShopInfoActivity.LICENSE)
            findNavController().navigate(R.id.action_shopInfoFragment_to_shopPhotoFragment, bundle)
        }

        //税务登记证照片
        binding.taxes.setOnClickListener {
            val bundle = bundleOf("type" to ApplyShopInfoActivity.TAXES)
            findNavController().navigate(R.id.action_shopInfoFragment_to_shopPhotoFragment, bundle)
        }

        //组织机构代码证照片
        binding.organ.setOnClickListener {
            val bundle = bundleOf("type" to ApplyShopInfoActivity.ORGAN)
            findNavController().navigate(R.id.action_shopInfoFragment_to_shopPhotoFragment, bundle)
        }

        //食品经营许可证照片
        binding.foodAllow.setOnClickListener {
            val bundle = bundleOf("type" to ApplyShopInfoActivity.FOOD_ALLOW)
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
            findNavController().navigate(R.id.action_shopInfoFragment_to_shopIDCardFragment, bundle)
        }

        binding.otherPIC.setOnClickListener {
            val bundle = bundleOf("type" to ApplyShopInfoActivity.OTHER_PIC)
            findNavController().navigate(R.id.action_shopInfoFragment_to_shopPhotoFragment, bundle)
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
                if (model.applyShopInfo.value?.thrcertflag == 0) {
                    checkNotBlack(model.applyShopInfo.value?.taxes_certificate_img) {
                        "请上传税务登记证书"
                    }
                    checkNotBlack(model.applyShopInfo.value?.orgcodepic) {
                        "请上传组织机构代码证照片"
                    }
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

            //是否三证合一
            if (it.thrcertflag == null) {
                it.thrcertflag = 1
                binding.thrcertflagYes.isSelected = true
                binding.thrcertflagNO.isSelected = false
                binding.taxes.visibility = View.GONE
                binding.organ.visibility = View.GONE
                binding.view1.visibility = View.GONE
                binding.view2.visibility = View.GONE
            } else {
                if (it.thrcertflag == 1) {
                    binding.thrcertflagYes.isSelected = true
                    binding.thrcertflagNO.isSelected = false
                    binding.taxes.visibility = View.GONE
                    binding.organ.visibility = View.GONE
                    binding.view1.visibility = View.GONE
                    binding.view2.visibility = View.GONE
                } else {
                    binding.thrcertflagYes.isSelected = false
                    binding.thrcertflagNO.isSelected = true
                    binding.taxes.visibility = View.VISIBLE
                    binding.organ.visibility = View.VISIBLE
                    binding.view1.visibility = View.VISIBLE
                    binding.view2.visibility = View.VISIBLE
                }
            }

            //店铺营业执照是否已经上传
            if (!it.licenceImg.isNullOrEmpty()) binding.licenseTV.text = "已上传"

            //税务登记证照片是否已经上传
            if (!it.taxes_certificate_img.isNullOrEmpty()) binding.taxesTV.text = "已上传"

            //组织机构代码证照片是否已经上传
            if (!it.orgcodepic.isNullOrEmpty()) binding.organTV.text = "已上传"

            //食品经营许可证照片
            if (!it.foodAllow.isNullOrEmpty()) binding.foodAllowTV.text = "已上传"

            if (!(it.shopPhotoFront.isNullOrEmpty() || it.shopPhotoInside.isNullOrEmpty())) binding.tvShopPhoto.text =
                "已上传"
            if (!(it.legalImg.isNullOrEmpty() || it.legalBackImg.isNullOrEmpty() || it.holdImg.isNullOrEmpty())) binding.IDCardTV.text =
                "已上传"
            if (!it.other_Pic_One.isNullOrEmpty()) {
                binding.otherPICTV.text = "已上传"
            }
            if (!it.other_Pic_Two.isNullOrEmpty()) {
                binding.otherPICTV.text = "已上传"
            }
        })

    }

    override fun onPause() {
        super.onPause()
        if (model.firstApplyShop) {
            model.applyShopInfo.value?.also {
                UserManager.setApplyShopInfo(it)
            }
        }

    }

}