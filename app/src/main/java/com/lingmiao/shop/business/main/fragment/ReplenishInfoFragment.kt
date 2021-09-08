package com.lingmiao.shop.business.main.fragment

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.blankj.utilcode.util.RegexUtils
import com.blankj.utilcode.util.ToastUtils
import com.james.common.base.BaseVBFragment
import com.james.common.utils.DialogUtils
import com.james.common.utils.exts.checkBoolean
import com.james.common.utils.exts.checkNotBlack
import com.james.common.utils.exts.gone
import com.lingmiao.shop.R
import com.lingmiao.shop.base.UserManager
import com.lingmiao.shop.business.main.ApplyShopInfoViewModel
import com.lingmiao.shop.business.main.ShopAddressActivity
import com.lingmiao.shop.business.main.bean.ApplyShopPoiEvent
import com.lingmiao.shop.business.main.bean.CategoryItem
import com.lingmiao.shop.business.main.presenter.ReplenishInfoPresenter
import com.lingmiao.shop.business.main.presenter.impl.ReplenishInfoPresenterImpl
import com.lingmiao.shop.databinding.FragmentReplenishInfoBinding
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode

class ReplenishInfoFragment :
    BaseVBFragment<FragmentReplenishInfoBinding, ReplenishInfoPresenter>(),
    ReplenishInfoPresenter.View {


    private val model by activityViewModels<ApplyShopInfoViewModel>()

    //判断身份证信息是否完整
    private fun isIDCardReady(): Boolean {

        model.applyShopInfo.value?.also {

            return !(it.legalName.isNullOrEmpty() || it.legalSex == null || it.legalId.isNullOrEmpty() || it.legal_address.isNullOrEmpty()
                    || it.legalIDExpire == null || it.legalImg.isNullOrEmpty() || it.legalBackImg.isNullOrEmpty() || it.holdImg.isNullOrEmpty())
        }
        return false
    }

    //判断企业信息是否完整
    private fun isCompanyInfoReady(): Boolean {

        model.applyShopInfo.value?.also {

            return if (it.thrcertflag == 1) {
                //是
                !(it.regMoney == null || it.employeeNum == null || it.operateLimit == null || it.inspect == null || it.thrcertflag == null
                        || it.companyName.isNullOrEmpty() || it.licenseNum.isNullOrEmpty() || it.licenceImg.isNullOrEmpty() || it.licenceEnd == null
                        )
            } else {
                //否
                !(it.regMoney == null || it.employeeNum == null || it.operateLimit == null || it.inspect == null || it.thrcertflag == null
                        || it.companyName.isNullOrEmpty() || it.licenseNum.isNullOrEmpty() || it.licenceImg.isNullOrEmpty() || it.licenceEnd == null
                        || it.taxes_certificate_num.isNullOrEmpty() || it.taxes_certificate_img.isNullOrEmpty() || it.taxes_distinguish_expire == null
                        || it.organcode.isNullOrEmpty() || it.orgcodepic.isNullOrEmpty() || it.organexpire == null
                        )
            }

        }

        return true
    }


    private fun showWorkCategoryPop() {
        mPresenter?.showCategoryPop(
            requireContext()
        ) { item1: CategoryItem?, item2: CategoryItem? ->
            onUpdateCategory(item1, item2)
        }
    }


    private fun onUpdateCategory(item1: CategoryItem?, item2: CategoryItem?) {
        val name = item1?.name + "/" + item2?.name
        binding.goodsManagementCategoryTextView.text = name

        model.applyShopInfo.value?.also {
            it.goodsManagementCategory = item1?.id
            it.categoryNames = item1?.name
            try {
                it.mccid = item2?.id?.toInt()
            } catch (e: Exception) {
            }
            it.mcc_name = item2?.name
        }
    }


    private fun initListener() {
        //店铺名称
        binding.shopName.setOnClickListener {
            DialogUtils.showInputDialog(
                requireActivity(),
                "店铺名称",
                "",
                "请输入",
                model.applyShopInfo.value?.shopName,
                "取消",
                "保存",
                null
            ) {
                binding.shopNameTextView.text = it
                model.applyShopInfo.value?.shopName = it
            }
        }

        //主营类目
        binding.goodsManagementCategory.setOnClickListener {
            showWorkCategoryPop()
        }

        //店铺地址
        binding.shopAddress.setOnClickListener {
            ShopAddressActivity.openActivity(requireActivity(), model.adInfo.value)
        }

        //法人
        binding.legalNameTV.setOnClickListener {
            DialogUtils.showInputDialog(
                requireActivity(),
                "法人姓名",
                "",
                "请输入",
                model.applyShopInfo.value?.legalName,
                "取消",
                "保存",
                null
            ) {
                if (RegexUtils.isZh(it)) {
                    binding.legalNameTV.text = it
                    model.applyShopInfo.value?.legalName = it
                    model.personalAccount.value?.openAccountName = it
                    model.nameOfShopPerson.value = 1
                } else {
                    ToastUtils.showShort("请输入正确的法人姓名")
                }
            }
        }

        //法人电话
        binding.legalPhone.setOnClickListener {
            DialogUtils.showInputDialog(
                requireActivity(),
                "法人电话",
                "",
                "请输入",
                model.applyShopInfo.value?.legal_phone,
                "取消",
                "保存",
                null
            ) {
                if (RegexUtils.isMobileSimple(it)) {
                    binding.legalPhoneTV.text = it
                    model.applyShopInfo.value?.legal_phone = it
                } else {
                    ToastUtils.showShort("请输入正确的法人电话")
                }
            }
        }

        //负责人
        binding.linkName.setOnClickListener {
            DialogUtils.showInputDialog(
                requireActivity(),
                "负责人姓名",
                "",
                "请输入",
                model.applyShopInfo.value?.linkName,
                "取消",
                "保存",
                null
            ) {
                if (RegexUtils.isZh(it)) {
                    binding.linkNameTextView.text = it
                    model.applyShopInfo.value?.linkName = it
                    model.nameOfShopPerson.value = 1
                } else {
                    ToastUtils.showShort("请输入正确的负责人姓名")
                }
            }
        }

        //负责人电话
        binding.linkPhone.setOnClickListener {
            DialogUtils.showInputDialog(
                requireActivity(),
                "负责人电话",
                "",
                "请输入",
                model.applyShopInfo.value?.linkPhone,
                "取消",
                "保存",
                null
            ) {
                if (RegexUtils.isMobileSimple(it)) {
                    binding.linkPhoneTextView.text = it
                    model.applyShopInfo.value?.linkPhone = it
                } else {
                    ToastUtils.showShort("请输入正确的负责人电话")
                }
            }
        }

        //同步法人与负责人
        binding.legalNameSync.setOnClickListener {
            model.applyShopInfo.value?.also {
                if (!it.legalName.isNullOrEmpty()) {
                    binding.linkNameTextView.text = it.legalName
                    model.applyShopInfo.value?.linkName = it.legalName
                }

                if (!it.legal_phone.isNullOrEmpty()) {
                    binding.linkPhoneTextView.text = it.legal_phone
                    model.applyShopInfo.value?.linkPhone = it.legal_phone
                }
                model.nameOfShopPerson.value = 1
            }
        }

        //经营内容
        binding.scope.setOnClickListener {
            DialogUtils.showMultInputDialog(
                requireActivity(),
                "经营内容",
                "",
                "请输入",
                model.applyShopInfo.value?.scope,
                "取消",
                "保存",
                null
            ) {
                binding.scopeTextView.text = it
                model.applyShopInfo.value?.scope = it
            }

        }

        //法人身份证信息   或者   经营者身份证信息
        binding.legalInfo.setOnClickListener {
            findNavController().navigate(R.id.action_replenishInfoFragment_to_identityInfoFragment)
        }

        //企业信息  或  企业信息（个体户）
        binding.companyInfo.setOnClickListener {
            findNavController().navigate(R.id.action_replenishInfoFragment_to_companyInfoFragment)
        }

        //下一步
        binding.tvApplyShopInfoNext.setOnClickListener {
            try {
                checkNotBlack(model.applyShopInfo.value?.shopName) {
                    "请输入店铺名称"
                }
                checkNotBlack(model.applyShopInfo.value?.goodsManagementCategory) {
                    "请选择主营类目"
                }
                checkNotBlack(model.applyShopInfo.value?.categoryNames) {
                    "请选择主营类目"
                }
                checkNotBlack(model.applyShopInfo.value?.mcc_name) {
                    "请选择主营类目"
                }
                checkNotBlack(model.applyShopInfo.value?.shopAdd) {
                    "请输入店铺地址"
                }
                if (model.applyShopInfo.value?.shopType != 4) {
                    checkNotBlack(model.applyShopInfo.value?.legalName) {
                        "请输入法人姓名"
                    }
                    checkNotBlack(model.applyShopInfo.value?.legal_phone) {
                        "请输入法人号码"
                    }
                    checkNotBlack(model.applyShopInfo.value?.scope) {
                        "请输入主营内容"
                    }
                    checkBoolean(isCompanyInfoReady()) {
                        "请输入完整的企业信息"
                    }
                }
                checkNotBlack(model.applyShopInfo.value?.linkName) {
                    "请输入负责人姓名"
                }
                checkNotBlack(model.applyShopInfo.value?.linkPhone) {
                    "请输入负责人号码"
                }
                checkBoolean(isIDCardReady()) {
                    "请输入完整的身份证资料"
                }
                findNavController().navigate(R.id.action_replenishInfoFragment_to_bindAccountFragment)

            } catch (e: IllegalStateException) {
                e.printStackTrace()
                showToast(e.message ?: "")
            }
        }

    }


    private fun initObserver() {

        model.applyShopInfo.observe(this, Observer { info ->


            if (info.shopType != 4) {
                //注册资本
                if (info.regMoney == null) {
                    info.regMoney = 1
                }

                //员工人数
                if (info.employeeNum == null) {
                    info.employeeNum = 1
                }

                //经营区域
                if (info.operateLimit == null) {
                    info.operateLimit = 1
                }

                //经营地段
                if (info.inspect == null) {
                    info.inspect = 3
                }
            }


            //店铺名字
            info.shopName?.also {
                if (it.isNotEmpty()) binding.shopNameTextView.text = it
            }

            //店铺经营类目
            if (!info.categoryNames.isNullOrEmpty()) {
                val temp = info.categoryNames + "/" + info.mcc_name
                binding.goodsManagementCategoryTextView.text = temp
            }

            //店铺地址
            if (!info.shopAdd.isNullOrEmpty()) {
                (info.shopProvince.orEmpty() +
                        info.shopCity.orEmpty() +
                        info.shopCounty.orEmpty() +
                        info.shopTown.orEmpty() +
                        info.shopAdd).also { address -> binding.shopAddressTextView.text = address }
            }

            //法人姓名
            if (!info.legalName.isNullOrEmpty()) binding.legalNameTV.text = info.legalName

            //法人电话
            if (!info.legal_phone.isNullOrEmpty()) binding.legalPhoneTV.text = info.legal_phone

            //负责人姓名
            info.linkName?.also {
                if (it.isNotEmpty()) binding.linkNameTextView.text = it
            }
            //负责人电话
            info.linkPhone?.also {
                if (it.isNotEmpty()) binding.linkPhoneTextView.text = it
            }

            //经营内容
            info.scope?.also {
                if (it.isNotEmpty()) binding.scopeTextView.text = it
            }

            if (isIDCardReady()) {
                binding.legalInfoTextView.text = "已上传"
            }

            if (isCompanyInfoReady()) {
                binding.companyInfoTextView.text = "已上传"
            }

        })

        model.nameOfShopPerson.observe(this, Observer {
            if (!(model.applyShopInfo.value?.legalName.isNullOrEmpty() || model.applyShopInfo.value?.linkName.isNullOrEmpty())) {
                binding.legalNameSync.isSelected =
                    model.applyShopInfo.value?.legalName == model.applyShopInfo.value?.linkName
            } else {
                binding.legalNameSync.isSelected = false
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


    @Subscribe(threadMode = ThreadMode.MAIN)
    fun updateShopAddress(event: ApplyShopPoiEvent) {
        //poi = event.poi
        model.applyShopInfo.value?.shopLat = event.adInfo?.latLng?.latitude
        model.applyShopInfo.value?.shopLng = event.adInfo?.latLng?.longitude
        model.applyShopInfo.value?.shopAdd = event.adInfo?.address
        binding.shopAddressTextView.text = model.applyShopInfo.value?.shopAdd
        if (event.adInfo != null) {
            model.setAddress(event.adInfo!!)
            model.applyShopInfo.value?.shopProvince = model.adInfo.value?.province
            model.applyShopInfo.value?.shopCity = model.adInfo.value?.city
            model.applyShopInfo.value?.shopCounty = model.adInfo.value?.district
//            applyShopInfo.shopProvince?.let {
//                applyShopInfo.shopAdd = applyShopInfo.shopAdd?.replace(it, "")
//            }
//            applyShopInfo.shopCity?.let {
//                applyShopInfo.shopAdd = applyShopInfo.shopAdd?.replace(it, "")
//            }
//            applyShopInfo.shopCounty?.let {
//                applyShopInfo.shopAdd = applyShopInfo.shopAdd?.replace(it, "")
//            }
        }
    }

//    @Subscribe(threadMode = ThreadMode.MAIN)
//    fun updateShopAdInfo(event: AdInfo) {
//        adInfo = event
//        applyShopInfo.shopProvince = event.province
//        applyShopInfo.shopCity = event.city
//        applyShopInfo.shopCounty = event.district
//
//    }


    override fun createPresenter() = ReplenishInfoPresenterImpl(this)

    override fun getBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ) = FragmentReplenishInfoBinding.inflate(inflater, container, false)

    override fun useEventBus() = true

    override fun initViewsAndData(rootView: View) {
        model.setTitle("补充资料")
        when (model.applyShopInfo.value?.shopType) {
            1 -> {
                //企业
                binding.companyInfoTitle.text = "企业信息"
            }
            3 -> {
                //个体户
                binding.companyInfoTitle.text = "企业信息（个体户）"
            }
            4 -> {
                binding. legalName1.text = "负责人"
                binding.legalNameSync.text = "设为联系人"
                binding.legalNameTV.text = "请输负责人名称"
                binding.legalPhoneTV1.text ="负责人电话"
                binding.legalPhoneTV.text="请输入负责人电话"

                binding.linkName1.text="联系人"
                binding.linkNameTextView.text="请输入联系人名称"
                binding.linkPhoneText1 .text="联系人电话"
                binding.linkPhoneTextView .text="请输入联系人电话"

                //经营内容
                binding.view3.gone()
                binding.scope.gone()
                binding.legalInfoName.text = "经营者身份证信息"
                binding.view4.gone()
                binding.companyInfo.gone()
            }
        }
        initListener()
        initObserver()
    }

}