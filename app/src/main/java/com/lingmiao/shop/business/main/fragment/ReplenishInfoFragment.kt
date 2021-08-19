package com.lingmiao.shop.business.main.fragment

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
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
import com.lingmiao.shop.business.goods.api.bean.CategoryVO
import com.lingmiao.shop.business.main.ApplyShopCategoryActivity
import com.lingmiao.shop.business.main.ApplyShopInfoActivity
import com.lingmiao.shop.business.main.ShopAddressActivity
import com.lingmiao.shop.business.main.bean.ApplyShopCategory
import com.lingmiao.shop.business.main.bean.ApplyShopPoiEvent
import com.lingmiao.shop.databinding.FragmentReplenishInfoBinding
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode

class ReplenishInfoFragment : BaseVBFragment<FragmentReplenishInfoBinding, BasePresenter>() {


    private val model by activityViewModels<ApplyShopInfoActivity.ApplyShopInfoViewModel>()

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
            val intent = Intent(requireActivity(), ApplyShopCategoryActivity::class.java)
            intent.putExtra(
                "goodsManagementCategory",
                model.applyShopInfo.value?.goodsManagementCategory
            )
            startActivity(intent)
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
                binding.legalNameTV.text = it
                model.applyShopInfo.value?.legalName = it
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
                binding.legalPhoneTV.text = it
                model.applyShopInfo.value?.legal_phone = it
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
                binding.linkNameTextView.text = it
                model.applyShopInfo.value?.linkName = it
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
                binding.linkPhoneTextView.text = it
                model.applyShopInfo.value?.linkPhone = it
            }
        }

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


        //法人身份证信息
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
                checkNotBlack(model.applyShopInfo.value?.shopAdd) {
                    "请输入店铺地址"
                }
                checkNotBlack(model.applyShopInfo.value?.legalName) {
                    "请输入法人姓名"
                }
                checkNotBlack(model.applyShopInfo.value?.legal_phone) {
                    "请输入法人号码"
                }
                checkNotBlack(model.applyShopInfo.value?.linkName) {
                    "请输入负责人姓名"
                }
                checkNotBlack(model.applyShopInfo.value?.linkPhone) {
                    "请输入负责人号码"
                }
                checkNotBlack(model.applyShopInfo.value?.scope) {
                    "请输入主营内容"
                }
                checkBoolean(isIDCardReady()) {
                    "请输入完整的身份证资料"
                }
                checkBoolean(isCompanyInfoReady()) {
                    "请输入完整的企业信息"
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

            //店铺名字
            info.shopName?.also {
                if (it.isNotEmpty()) binding.shopNameTextView.text = it
            }

            //店铺经营类目
            if (!info.categoryNames.isNullOrEmpty()) {
                binding.goodsManagementCategoryTextView.text =
                    info.categoryNames?.replace(" ", "/")
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

    }


    // 单选
    @Subscribe(threadMode = ThreadMode.MAIN)
    fun updateShopCategory(applyShopCategory: CategoryVO) {
        val categoryIdSB = StringBuilder()
        val categoryNameSB = StringBuilder()
        categoryIdSB.append(applyShopCategory.categoryId)
        categoryNameSB.append(applyShopCategory.name)
        model.applyShopInfo.value?.goodsManagementCategory = categoryIdSB.toString()
        model.applyShopInfo.value?.categoryNames = categoryNameSB.toString()
        binding.goodsManagementCategoryTextView.text = categoryNameSB.toString()
    }

    // 多选
    @Subscribe(threadMode = ThreadMode.MAIN)
    fun updateShopCategory(event: List<ApplyShopCategory>) {
        model.setCategory(event)
        val categoryIdSB = StringBuilder()
        val categoryNameSB = StringBuilder()
        model.selectedCategoryListLiveData.value?.forEachIndexed { _, applyShopCategory ->
            categoryIdSB.append(applyShopCategory.categoryId)
            categoryNameSB.append(applyShopCategory.name)
//            if (index < (selectedCategoryList?.size ?: 0) - 1) {
//                categoryIdSB.append(",")
//                categoryNameSB.append("/")
//            }
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


    override fun createPresenter(): BasePresenter {
        return BasePreImpl(this)
    }

    override fun getBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ) = FragmentReplenishInfoBinding.inflate(inflater, container, false)


    override fun useEventBus(): Boolean {
        return true
    }


    override fun initViewsAndData(rootView: View) {
        model.setTitle("补充资料")
        if (model.applyShopInfo.value?.shopType == 1) {
            //企业
            binding.companyInfoTitle.text = "企业信息"
        } else {
            //个体户
            binding.companyInfoTitle.text = "企业信息（个体户）"
        }
        initListener()
        initObserver()
    }

}