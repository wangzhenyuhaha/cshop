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
import com.james.common.utils.exts.gone
import com.james.common.utils.exts.visiable
import com.lingmiao.shop.R
import com.lingmiao.shop.base.UserManager
import com.lingmiao.shop.business.main.ApplyShopInfoActivity
import com.lingmiao.shop.business.main.ApplyShopInfoViewModel
import com.lingmiao.shop.databinding.FragmentShopInfoBinding

class ShopInfoFragment : BaseVBFragment<FragmentShopInfoBinding, BasePresenter>() {

    private val model by activityViewModels<ApplyShopInfoViewModel>()

    override fun createPresenter() = BasePreImpl(this)

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

        if (!model.shopOpenOrNot) {//个体户
            binding.shopInfoSelf.setOnClickListener {
                model.applyShopInfo.value?.shopType = 3
                model.shopType.value = 3
            }

            //企业
            binding.shopInfoCompany.setOnClickListener {
                model.applyShopInfo.value?.shopType = 1
                model.shopType.value = 1
            }

            //个人
            binding.shopInfoPersonal.setOnClickListener {
                model.applyShopInfo.value?.shopType = 4
                model.shopType.value = 4
            }

            //是否三证合一
            //Yes
            binding.thrcertflagYes.setOnClickListener {
                model.applyShopInfo.value?.thrcertflag = 1
                model.thrcertflag.value = 1
            }

            //No
            binding.thrcertflagNO.setOnClickListener {
                model.applyShopInfo.value?.thrcertflag = 0
                model.thrcertflag.value = 0
            }
        } else {
            binding.nextTextView.text = "下一页"
        }

        //店铺租聘合同
        binding.hire.setOnClickListener {
            val bundle = bundleOf("type" to ApplyShopInfoActivity.HIRE)
            findNavController().navigate(R.id.action_shopInfoFragment_to_shopPhotoFragment, bundle)
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
            if (model.applyShopInfo.value?.shopType == 4) {
                //个人申请
                val bundle = bundleOf("type" to ApplyShopInfoActivity.PERSONAL_SHOP)
                findNavController().navigate(
                    R.id.action_shopInfoFragment_to_shopIDCardFragment,
                    bundle
                )
            } else {
                val bundle = bundleOf("type" to ApplyShopInfoActivity.SHOP_FRONT)
                findNavController().navigate(
                    R.id.action_shopInfoFragment_to_shopPhotoFragment,
                    bundle
                )
            }

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
                if (model.applyShopInfo.value?.shopType == 4) {
                    //个人
                    //检查店铺租聘合同

                    if (model.applyShopInfo.value?.bizplacepic == null) {
                        showToast("请上传店铺租聘合同")
                        return@setOnClickListener
                    }

                    model.applyShopInfo.value?.bizplacepic?.split(",")?.size?.also { size ->
                        when {
                            size > 1 -> {
                                //nothing to do
                            }
                            size == 1 -> {
//                                showToast("请上传完整的店铺租聘合同")
//                                return@setOnClickListener
                            }
                            else -> {
                                showToast("请上传店铺租聘合同")
                                return@setOnClickListener
                            }
                        }
                    }
                    checkNotBlack(model.applyShopInfo.value?.shopPhotoFront) {
                        "请上传店铺门头照片"
                    }
                    checkNotBlack(model.applyShopInfo.value?.shopPhotoInside) {
                        "请上传店铺内景照片"
                    }
                    checkNotBlack(model.applyShopInfo.value?.peasonheadpic) {
                        "请上传经营者和店铺门头合照"
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

                } else {
                    //个体户，企业
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
                        "请上传身份证人像面照片"
                    }
                    checkNotBlack(model.applyShopInfo.value?.legalBackImg) {
                        "请上传身份证国徽面照片"
                    }
                    checkNotBlack(model.applyShopInfo.value?.holdImg) {
                        "请上传手持身份证照片"
                    }
                    findNavController().navigate(R.id.action_shopInfoFragment_to_replenishInfoFragment)
                }
            } catch (e: IllegalStateException) {
                e.printStackTrace()
                showToast(e.message ?: "")
            }
        }


    }

    private fun initObserver() {

        model.shopType.observe(this, Observer {
            // 1,企业     3,个体户   4,个人店铺
            when (it) {
                1 -> {
                    binding.shopInfoSelfImageView.isSelected = false
                    binding.shopInfoCompanyImageView.isSelected = true
                    binding.shopInfoPersonalImageView.isSelected = false

                    //设置模块可见性
                    binding.hire.gone()
                    binding.thrcertflag.visiable()
                    binding.license1.visiable()
                    binding.license2.gone()
                    //根据三证合一显示模块
                    if (model.applyShopInfo.value?.thrcertflag == 1) {
                        model.thrcertflag.value = 1
                    } else {
                        model.thrcertflag.value = 0
                    }

                    binding.IDCardName.text = "法人身份证照片"

                }
                3 -> {
                    //个体户
                    binding.shopInfoSelfImageView.isSelected = true
                    binding.shopInfoCompanyImageView.isSelected = false
                    binding.shopInfoPersonalImageView.isSelected = false

                    //设置模块可见性
                    binding.hire.gone()
                    binding.thrcertflag.visiable()
                    binding.license1.visiable()
                    binding.license2.gone()

                    //根据三证合一显示模块
                    if (model.applyShopInfo.value?.thrcertflag == 1) {
                        model.thrcertflag.value = 1
                    } else {
                        model.thrcertflag.value = 0
                    }

                    binding.IDCardName.text = "法人身份证照片"
                }
                4 -> {
                    binding.shopInfoSelfImageView.isSelected = false
                    binding.shopInfoCompanyImageView.isSelected = false
                    binding.shopInfoPersonalImageView.isSelected = true

                    //设置模块可见性
                    binding.hire.visiable()
                    binding.thrcertflag.gone()
                    binding.license1.gone()
                    binding.license2.visiable()

                    binding.taxes.gone()
                    binding.organ.gone()
                    binding.view1.gone()
                    binding.view2.gone()

                    binding.IDCardName.text = "负责人身份证照片"
                }
            }
        })

        model.thrcertflag.observe(this, Observer {
            if (model.applyShopInfo.value?.shopType == 4) {
                binding.taxes.gone()
                binding.organ.gone()
                binding.view1.gone()
                binding.view2.gone()
            } else {
                when (it) {
                    1 -> {
                        //三证合一
                        binding.thrcertflagYes.isSelected = true
                        binding.thrcertflagNO.isSelected = false
                        binding.taxes.gone()
                        binding.organ.gone()
                        binding.view1.gone()
                        binding.view2.gone()
                    }
                    0 -> {
                        //非三证合一
                        binding.thrcertflagYes.isSelected = false
                        binding.thrcertflagNO.isSelected = true
                        binding.taxes.visiable()
                        binding.organ.visiable()
                        binding.view1.visiable()
                        binding.view2.visiable()
                    }
                }
            }
        })

        model.applyShopInfo.observe(this, Observer {

            //对店铺租聘合同的观察
            if (it.bizplacepic == null) {
                binding.hireTV.text = "店铺租聘合同"
            }
            it.bizplacepic?.split(",")?.size?.also { size ->
                when {
                    size > 1 -> {
                        binding.hireTV.text = "已上传"
                    }
                    size == 1 -> {
//                        binding.hireTV.text = "请上传完整"
                        binding.hireTV.text = "已上传"
                    }
                    else -> {
                        binding.hireTV.text = "店铺租聘合同"
                    }
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

            if (it.shopType == 4) {
                if (!(it.shopPhotoFront.isNullOrEmpty() || it.shopPhotoInside.isNullOrEmpty() || it.peasonheadpic.isNullOrEmpty())) binding.tvShopPhoto.text =
                    "已上传"
            } else {
                if (!(it.shopPhotoFront.isNullOrEmpty() || it.shopPhotoInside.isNullOrEmpty())) binding.tvShopPhoto.text =
                    "已上传"
            }

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
            model.companyAccount.value?.also {
                UserManager.setCompanyAccount(it)
            }
            model.personalAccount.value?.also {
                UserManager.setPersonalAccount(it)
            }
        }
    }

}