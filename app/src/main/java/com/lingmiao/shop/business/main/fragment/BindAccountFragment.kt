package com.lingmiao.shop.business.main.fragment

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.blankj.utilcode.util.ActivityUtils
import com.blankj.utilcode.util.ToastUtils
import com.james.common.base.BasePreImpl
import com.james.common.base.BasePresenter
import com.james.common.base.BaseVBFragment
import com.james.common.utils.DialogUtils
import com.james.common.utils.exts.singleClick
import com.lingmiao.shop.R
import com.lingmiao.shop.base.UserManager
import com.lingmiao.shop.business.main.ApplyShopInfoActivity
import com.lingmiao.shop.business.main.SubBranchActivity
import com.lingmiao.shop.business.main.bean.BankDetail
import com.lingmiao.shop.databinding.FragmentBindAccountBinding
import kotlinx.android.synthetic.main.fragment_bind_account.*
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode

class BindAccountFragment : BaseVBFragment<FragmentBindAccountBinding, BasePresenter>() {

    //0表示对公账户结算，1表示对私账户结算
    private val selectAccount: MutableLiveData<Int> = MutableLiveData()


    private val model by activityViewModels<ApplyShopInfoActivity.ApplyShopInfoViewModel>()

    //记录当前模块的可见状态
    //企业
    private var companyGVisible = View.VISIBLE
    private var companySVisible = View.GONE

    //个体户
    private var personalGVisible = View.GONE
    private var personalSVisible = View.GONE

    companion object {

        const val COMPANY_BANK = 2
        const val COMPANY_SUB = 3

        const val PERSONAL_BANK = 4
        const val PERSONAL_SUB = 5
    }

    private var intent: Intent? = null

    //对公账户是否完整
    private fun isCompanyReady(): Boolean {
        model.companyAccount.value?.also {
            return !(it.openAccountName.isNullOrEmpty() || it.cardNo.isNullOrEmpty() || it.bankCardType == null
                    || it.province.isNullOrEmpty() || it.city.isNullOrEmpty() || it.bankCode.isNullOrEmpty()
                    || it.bankName.isNullOrEmpty() || it.subBankName.isNullOrEmpty() || it.subBankCode.isNullOrEmpty()
                    || it.bankUrls.isNullOrEmpty())
        }
        return false
    }

    //对私账户是否完整
    private fun isPersonalReady(): Boolean {
        model.personalAccount.value?.also {
            return !(it.openAccountName.isNullOrEmpty() || it.cardNo.isNullOrEmpty() || it.bankCardType == null
                    || it.province.isNullOrEmpty() || it.city.isNullOrEmpty() || it.bankCode.isNullOrEmpty()
                    || it.bankName.isNullOrEmpty() || it.subBankName.isNullOrEmpty() || it.subBankCode.isNullOrEmpty()
                    || it.bankUrls.isNullOrEmpty())
        }
        return false
    }

    override fun createPresenter(): BasePresenter {
        return BasePreImpl(this)
    }

    override fun getBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ) = FragmentBindAccountBinding.inflate(inflater, container, false)


    override fun useEventBus(): Boolean {
        return true
    }

    override fun initBundles() {
        super.initBundles()
        selectAccount.value = 0
    }

    override fun initViewsAndData(rootView: View) {

        model.setTitle("结算账户绑定")


        //跳转到银行选择界面
        intent = Intent(requireActivity(), SubBranchActivity::class.java)

        //对UI整体进行处理
        if (model.applyShopInfo.value?.shopType == 1) {
            //企业
            binding.shopType.text = "企业账户"
            binding.shopTypeTextView.text = "（对公账户必须填写，对私账户选填；且勾选的账户为店铺结算账户）"
            binding.accountCompanyTVForCompany.visibility = View.VISIBLE
            binding.accountCompanyTVForPersonal.visibility = View.GONE

            binding.accountPersonalTVForCompany.visibility = View.VISIBLE
            binding.accountPersonalTVForPersonal.visibility = View.GONE

            binding.companyModule.visibility = companyGVisible
            binding.personalModule.visibility = companySVisible

        } else {
            //个体  3
            binding.shopType.text = "个体户账户"
            binding.shopTypeTextView.text = "（二选一进行填写，且勾选的账户为店铺结算账户）"
            binding.accountCompanyTVForCompany.visibility = View.GONE
            binding.accountCompanyTVForPersonal.visibility = View.VISIBLE

            binding.accountPersonalTVForCompany.visibility = View.GONE
            binding.accountPersonalTVForPersonal.visibility = View.VISIBLE

            binding.companyModule.visibility = personalGVisible
            binding.personalModule.visibility = personalSVisible
        }

        initListener()
        initObserver()


    }


    private fun initListener() {

        //结算账户类型
        binding.accountCompany2.setOnClickListener {
            //对公账户结算
            selectAccount.value = 0
        }

        binding.accountPersonal2.setOnClickListener {
            //对私账户结算
            selectAccount.value = 1
        }


        binding.accountCompany1.setOnClickListener {
            val temp = binding.companyModule.visibility
            if (model.applyShopInfo.value?.shopType == 1) {
                //企业
                if (temp == View.GONE) {
                    companyGVisible = View.VISIBLE
                    binding.companyModule.visibility = companyGVisible
                    binding.accountCompany1.isSelected = true
                } else {
                    companyGVisible = View.GONE
                    binding.companyModule.visibility = companyGVisible
                    binding.accountCompany1.isSelected = false
                }

            } else {
                //个体户
                if (temp == View.GONE) {
                    personalGVisible = View.VISIBLE
                    binding.companyModule.visibility = personalGVisible
                    binding.accountCompany1.isSelected = true
                } else {
                    personalGVisible = View.GONE
                    binding.companyModule.visibility = personalGVisible
                    binding.accountCompany1.isSelected = false
                }

            }

        }

        binding.accountPersonal1.setOnClickListener {
            val temp = binding.personalModule.visibility
            if (model.applyShopInfo.value?.shopType == 1) {
                //企业
                if (temp == View.GONE) {
                    companySVisible = View.VISIBLE
                    binding.personalModule.visibility = companySVisible
                    binding.accountPersonal1.isSelected = true
                } else {
                    companySVisible = View.GONE
                    binding.personalModule.visibility = companySVisible
                    binding.accountPersonal1.isSelected = false
                }

            } else {
                //个体户
                if (temp == View.GONE) {
                    personalSVisible = View.VISIBLE
                    binding.personalModule.visibility = personalSVisible
                    binding.accountPersonal1.isSelected = true
                } else {
                    personalSVisible = View.GONE
                    binding.personalModule.visibility = personalSVisible
                    binding.accountPersonal1.isSelected = false
                }

            }
        }

        //账户名称
        binding.bankaccountnameC.setOnClickListener {
            DialogUtils.showInputDialog(
                requireActivity(),
                "账户名称",
                "",
                "请输入",
                model.companyAccount.value?.openAccountName,
                "取消",
                "保存",
                null
            ) {
                binding.bankaccountnameCtv.text = it
                model.companyAccount.value?.openAccountName = it
            }
        }

        //开户许可证照片
        binding.acctlicensepic.setOnClickListener {
            val bundle = bundleOf("type" to ApplyShopInfoActivity.PICTURE_COMPANY_ACCOUNT)
            findNavController().navigate(
                R.id.action_bindAccountFragment_to_shopPhotoFragment,
                bundle
            )
        }

        //账户号
        binding.banknumberC.setOnClickListener {

            DialogUtils.showInputDialog(
                requireActivity(),
                "账户号",
                "",
                "请输入",
                model.companyAccount.value?.cardNo,
                "取消",
                "保存",
                null
            ) {
                binding.banknumberCtv.text = it
                model.companyAccount.value?.cardNo = it
            }

        }


        //对公，所属银行
        binding.bankNoC.setOnClickListener {
            intent?.apply {
                putExtra("type", COMPANY_BANK)
                putExtra("bank", "nothing")
                ActivityUtils.startActivity(this)
            }
        }

        //对私，所属银行
        binding.bankNoP.setOnClickListener {
            intent?.apply {
                putExtra("type", PERSONAL_BANK)
                putExtra("bank", "nothing")
                ActivityUtils.startActivity(this)
            }

        }

        //对公，所属支行
        binding.bankNameC.setOnClickListener {

            if (model.companyAccount.value?.bankCode.isNullOrEmpty()) {
                ToastUtils.showShort("请先选择银行")
                return@setOnClickListener
            }
            intent?.apply {
                putExtra("type", COMPANY_SUB)
                putExtra("bank", model.companyAccount.value?.bankCode)
                ActivityUtils.startActivity(this)
            }
        }

        //对私，所属支行
        binding.bankNameP.setOnClickListener {

            if (model.personalAccount.value?.bankCode.isNullOrEmpty()) {
                ToastUtils.showShort("请先选择银行")
                return@setOnClickListener
            }

            intent?.apply {
                putExtra("type", PERSONAL_SUB)
                putExtra("bank", model.personalAccount.value?.bankCode)
                ActivityUtils.startActivity(this)
            }
        }


        //持卡人
        binding.bankaccountnameP.setOnClickListener {
            DialogUtils.showInputDialog(
                requireActivity(),
                "持卡人",
                "",
                "请输入",
                model.personalAccount.value?.openAccountName,
                "取消",
                "保存",
                null
            ) {
                binding.bankaccountnamePtv.text = it
                model.personalAccount.value?.openAccountName = it
            }
        }

        //银行卡正面照
        binding.settleBankPic.setOnClickListener {
            val bundle = bundleOf("type" to ApplyShopInfoActivity.PICTURE_PERSONAL_ACCOUNT)
            findNavController().navigate(
                R.id.action_bindAccountFragment_to_shopPhotoFragment,
                bundle
            )
        }

        //卡号
        binding.banknumberP.setOnClickListener {
            DialogUtils.showInputDialog(
                requireActivity(),
                "卡号",
                "",
                "请输入",
                model.personalAccount.value?.cardNo,
                "取消",
                "保存",
                null
            ) {
                binding.bankaccountnamePtv.text = it
                model.personalAccount.value?.cardNo = it
            }
        }


//        binding.tvApplyShopInfoNext.singleClick{
////             model.test.value = 1
//
//
//          //  model.test.value = 8
////
//            UserManager.getLoginInfo()?.uid?.also {
//                model.companyAccount.value?.memberId = it
//                model.personalAccount.value?.memberId = it
//            }
//            model.companyAccount.value?.isDefault = 1
//            Log.d("WZY",model.companyAccount.value.toString())
//            Log.d("WZY",model.personalAccount.value.toString())
//            model.bindBandCard.value = 2
//        }


        //isDefault  需要重新设置避免出现两个1
        binding.tvApplyShopInfoNext.singleClick {
            //测试 个体户  对私账户结算


            //给memberId赋值
            UserManager.getLoginInfo()?.uid?.also {
                model.companyAccount.value?.memberId = it
                model.personalAccount.value?.memberId = it
            }


            //检查资料是否齐全,并且绑定银行卡
            if (model.applyShopInfo.value?.shopType == 1) {
                //企业
                if (selectAccount.value == 0) {
                    //对公账户结算
                    model.companyAccount.value?.isDefault = 1
                    model.personalAccount.value?.isDefault = 0

                    if (!isCompanyReady()) {
                        ToastUtils.showShort("请将对公账户填写完整")
                        return@singleClick
                    }
                    if (isPersonalReady()) {
                        model.bindBandCard.value = 2
                    } else {
                        model.bindBandCard.value = 0
                    }

                } else {
                    //对私账户结算
                    model.personalAccount.value?.isDefault = 1

                    model.companyAccount.value?.isDefault = 0

                    if (!isCompanyReady()) {
                        ToastUtils.showShort("请将对公账户填写完整")
                        return@singleClick
                    }
                    if (!isPersonalReady()) {
                        ToastUtils.showShort("请将对私账户填写完整")
                        return@singleClick
                    }

                    model.bindBandCard.value = 2

                }
            } else {
                //个体户
                if (selectAccount.value == 0) {
                    //对公账户结算
                    model.companyAccount.value?.isDefault = 1

                    model.personalAccount.value?.isDefault = 0


                    if (!isCompanyReady()) {
                        ToastUtils.showShort("请将对公账户填写完整")
                        return@singleClick
                    }

                    if (isPersonalReady()) {
                        model.bindBandCard.value = 2
                    } else {
                        model.bindBandCard.value = 0
                    }

                } else {
                    //对私账户结算
                    model.personalAccount.value?.isDefault = 1
                    model.companyAccount.value?.isDefault = 0


                    if (!isPersonalReady()) {
                        ToastUtils.showShort("请将对私账户填写完整")
                        return@singleClick
                    }

                    if (isCompanyReady()) {
                        model.bindBandCard.value = 2
                    } else {
                        model.bindBandCard.value = 1
                    }

                }
            }


            //申请店铺
            if (selectAccount.value == 0) {

                model.applyShopInfo.value?.also {
                    it.accttype = 1
                    it.bankAccountName = model.companyAccount.value?.openAccountName
                    it.bankNumber = model.companyAccount.value?.cardNo
                    it.bankNo = model.companyAccount.value?.bankName
                    it.bankName = model.companyAccount.value?.subBankName
                    it.bankUrls = model.companyAccount.value?.bankUrls

                    it.bankCode = model.companyAccount.value?.bankCode
                    it.subBankCode = model.companyAccount.value?.subBankCode
                    it.province = model.companyAccount.value?.province
                    it.city = model.companyAccount.value?.city
                    it.bankCard = model.companyAccount.value
                }


                model.go.value = 1


            } else {


                model.applyShopInfo.value?.also {
                    it.accttype = 0
                    it.bankAccountName = model.personalAccount.value?.openAccountName
                    it.bankNumber = model.personalAccount.value?.cardNo
                    it.bankNo = model.personalAccount.value?.bankName
                    it.bankName = model.personalAccount.value?.subBankName
                    it.bankUrls = model.personalAccount.value?.bankUrls

                    it.bankCode = model.personalAccount.value?.bankCode
                    it.subBankCode = model.personalAccount.value?.subBankCode
                    it.province = model.personalAccount.value?.province
                    it.city = model.personalAccount.value?.city
                    it.bankCard = model.personalAccount.value
                }


                model.go.value = 1


            }

        }
    }

    private fun initObserver() {

        //确定当前结算账户类型
        selectAccount.observe(this, Observer {
            //对公账户作为结算账户
            if (it == 0) {
                binding.accountCompany2.isSelected = true
                binding.accountPersonal2.isSelected = false
            }
            //对私账户作为结算账户
            if (it == 1) {
                binding.accountCompany2.isSelected = false
                binding.accountPersonal2.isSelected = true
            }
        })

        //观察对公账户
        model.companyAccount.observe(this, Observer {
            if (!it.openAccountName.isNullOrEmpty()) {
                binding.bankaccountnameCtv.text = it.openAccountName
            }

            if (!it.bankUrls.isNullOrEmpty()) {
                binding.acctlicensepictv.text = "已上传"
            }

            if (!it.cardNo.isNullOrEmpty()) {
                binding.banknumberCtv.text = it.cardNo
            }

            //判断是否获取银行卡
            if (!(it.bankName.isNullOrEmpty() || it.bankCode.isNullOrEmpty())) {
                binding.bankNoCtv.text = it.bankName
            }

            if (!(it.subBankName.isNullOrEmpty() || it.subBankCode.isNullOrEmpty())) {
                binding.bankNameCtv.text = it.subBankName
            }

        })

        //观察对私账户
        model.personalAccount.observe(this, Observer {

            if (!it.openAccountName.isNullOrEmpty()) {
                binding.bankaccountnamePtv.text = it.openAccountName
            }

            if (!it.bankUrls.isNullOrEmpty()) {
                binding.settleBankPictv.text = "已上传"
            }

            if (!it.cardNo.isNullOrEmpty()) {
                binding.banknumberPtv.text = it.cardNo
            }


            //判断是否获取银行卡
            if (!(it.bankName.isNullOrEmpty() || it.bankCode.isNullOrEmpty())) {
                binding.bankNoPtv.text = it.bankName
            }

            if (!(it.subBankName.isNullOrEmpty() || it.subBankCode.isNullOrEmpty())) {
                binding.bankNamePtv.text = it.subBankName
            }

        })

    }


    @Subscribe(threadMode = ThreadMode.MAIN)
    fun updateBank(bank: BankDetail) {

        when (bank.type) {
            COMPANY_BANK -> {
                binding.bankNoCtv.text = bank.bafName
                model.companyAccount.value?.bankName = bank.bafName
                model.companyAccount.value?.bankCode = bank.bankCode
                model.companyAccount.value?.province = bank.province
                model.companyAccount.value?.city = bank.city
            }
            COMPANY_SUB -> {
                binding.bankNameCtv.text = bank.bankName
                model.companyAccount.value?.subBankName = bank.bankName
                model.companyAccount.value?.subBankCode = bank.netBankCode
            }

            PERSONAL_BANK -> {
                binding.bankNoPtv.text = bank.bafName
                model.personalAccount.value?.bankName = bank.bafName
                model.personalAccount.value?.bankCode = bank.bankCode
                model.personalAccount.value?.province = bank.province
                model.personalAccount.value?.city = bank.city
            }
            PERSONAL_SUB -> {
                binding.bankNamePtv.text = bank.bankName
                model.personalAccount.value?.subBankName = bank.bankName
                model.personalAccount.value?.subBankCode = bank.netBankCode
            }
        }
    }


}