package com.lingmiao.shop.business.main.fragment

import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import com.blankj.utilcode.util.ActivityUtils
import com.blankj.utilcode.util.ToastUtils
import com.james.common.base.BasePreImpl
import com.james.common.base.BasePresenter
import com.james.common.base.BaseVBFragment
import com.james.common.netcore.networking.http.core.awaitHiResponse
import com.james.common.utils.DialogUtils
import com.james.common.utils.exts.checkBoolean
import com.lingmiao.shop.base.UserManager
import com.lingmiao.shop.business.main.ApplyShopInfoActivity
import com.lingmiao.shop.business.main.SubBranchActivity
import com.lingmiao.shop.business.main.api.MainRepository
import com.lingmiao.shop.business.main.bean.ApplyShopImageEvent
import com.lingmiao.shop.business.main.bean.BankDetail
import com.lingmiao.shop.databinding.FragmentBindAccountBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode

class BindAccountFragment : BaseVBFragment<FragmentBindAccountBinding, BasePresenter>() {

    //0表示对公账户结算，1表示对私账户结算
    private val selectAccount: MutableLiveData<Int> = MutableLiveData()

    private val model by activityViewModels<ApplyShopInfoActivity.ApplyShopInfoViewModel>()

    companion object {

        const val PICTURE_COMPANY = 0
        const val PICTURE_PERSONAL = 1

        const val COMPANY_BANK = 2
        const val COMPANY_SUB = 3

        const val PERSONAL_BANK = 4
        const val PERSONAL_SUB = 5
    }

    private var intent: Intent? = null

    //对公账户是否完整
    private fun isCompanyReady(): Boolean {
        model.companyAccount.also {
            return !(it.openAccountName.isNullOrEmpty() || it.cardNo.isNullOrEmpty() || it.bankCardType == null
                    || it.province.isNullOrEmpty() || it.city.isNullOrEmpty() || it.bankCode.isNullOrEmpty()
                    || it.bankName.isNullOrEmpty() || it.subBankName.isNullOrEmpty() || it.subBankCode.isNullOrEmpty())
                    || it.bankUrls.isNullOrEmpty()
        }
    }

    //对私账户是否完整
    private fun isPersonalReady(): Boolean {
        model.personalAccount.also {
            return !(it.openAccountName.isNullOrEmpty() || it.cardNo.isNullOrEmpty() || it.bankCardType == null
                    || it.province.isNullOrEmpty() || it.city.isNullOrEmpty() || it.bankCode.isNullOrEmpty()
                    || it.bankName.isNullOrEmpty() || it.subBankName.isNullOrEmpty() || it.subBankCode.isNullOrEmpty())
                    || it.bankUrls.isNullOrEmpty()
        }
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

    override fun initViewsAndData(rootView: View) {

        model.setTitle("结算账户绑定")

        //跳转到银行选择界面
        intent = Intent(requireActivity(), SubBranchActivity::class.java)

        //对UI整体进行处理
        if (model.applyShopInfo.value?.shopType == 1) {
            //企业
            binding.shopType.text = "企业账户"
            binding.shopTypeTextView.text = "（对公账户必须填写，对私选填；且勾选的账户为店铺结算账户）"
            binding.accountCompanyTVForCompany.visibility = View.VISIBLE
            binding.accountCompanyTVForPersonal.visibility = View.GONE

            binding.accountPersonalTVForCompany.visibility = View.VISIBLE
            binding.accountPersonalTVForPersonal.visibility = View.GONE

            binding.companyModule.visibility = View.VISIBLE
            binding.personalModule.visibility = View.GONE

        } else {
            //个体  3
            binding.shopType.text = "个体户账户"
            binding.shopTypeTextView.text = "（二选一进行填写，且勾选的账户为店铺结算账户）"
            binding.accountCompanyTVForCompany.visibility = View.GONE
            binding.accountCompanyTVForPersonal.visibility = View.VISIBLE

            binding.accountPersonalTVForCompany.visibility = View.GONE
            binding.accountPersonalTVForPersonal.visibility = View.VISIBLE

            binding.companyModule.visibility = View.GONE
            binding.personalModule.visibility = View.GONE
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
            if (temp == View.GONE) {
                binding.companyModule.visibility = View.VISIBLE
                binding.accountCompany1.isSelected = true
            } else {
                binding.companyModule.visibility = View.GONE
                binding.accountCompany1.isSelected = false
            }

        }

        binding.accountPersonal1.setOnClickListener {
            val temp = binding.personalModule.visibility
            if (temp == View.GONE) {
                binding.personalModule.visibility = View.VISIBLE
                binding.accountPersonal1.isSelected = true
            } else {
                binding.personalModule.visibility = View.GONE
                binding.accountPersonal1.isSelected = false
            }
        }


        //账户名称
        binding.bankaccountnameC.setOnClickListener {

            DialogUtils.showInputDialog(
                requireActivity(),
                "账户名称",
                "",
                "请输入",
                model.companyAccount.openAccountName,
                "取消",
                "保存",
                null
            ) {
                binding.bankaccountnameCtv.text = it
                model.companyAccount.openAccountName = it
            }
        }


        //开户许可证照片
        binding.acctlicensepic.setOnClickListener {

            ApplyShopInfoActivity.goUploadImageActivity(
                requireActivity(),
                BindAccountFragment.PICTURE_COMPANY,
                "上传开户许可证照片",
                model.companyAccount.bankUrls
            )

        }

        //账户号
        binding.banknumberC.setOnClickListener {

            DialogUtils.showInputDialog(
                requireActivity(),
                "账户号",
                "",
                "请输入",
                model.companyAccount.cardNo,
                "取消",
                "保存",
                null
            ) {
                binding.banknumberCtv.text = it
                model.companyAccount.cardNo = it
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

            if (model.companyAccount.bankCode.isNullOrEmpty()) {
                ToastUtils.showShort("请先选择银行")
                return@setOnClickListener
            }
            intent?.apply {
                putExtra("type", COMPANY_SUB)
                putExtra("bank", model.companyAccount.bankCode)
                ActivityUtils.startActivity(this)
            }
        }

        //对私，所属支行
        binding.bankNameP.setOnClickListener {

            if (model.personalAccount.bankCode.isNullOrEmpty()) {
                ToastUtils.showShort("请先选择银行")
                return@setOnClickListener
            }

            intent?.apply {
                putExtra("type", PERSONAL_SUB)
                putExtra("bank", model.personalAccount.bankCode)
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
                model.personalAccount.openAccountName,
                "取消",
                "保存",
                null
            ) {
                binding.bankaccountnamePtv.text = it
                model.personalAccount.openAccountName = it
            }
        }

        //银行卡正面照
        binding.settleBankPic.setOnClickListener {
            ApplyShopInfoActivity.goUploadImageActivity(
                requireActivity(),
                BindAccountFragment.PICTURE_PERSONAL,
                "上传银行卡正面照",
                model.personalAccount.bankUrls
            )
        }

        //卡号
        binding.banknumberP.setOnClickListener {
            DialogUtils.showInputDialog(
                requireActivity(),
                "卡号",
                "",
                "请输入",
                model.personalAccount.cardNo,
                "取消",
                "保存",
                null
            ) {
                binding.bankaccountnamePtv.text = it
                model.personalAccount.cardNo = it
            }
        }


        binding.tvApplyShopInfoNext.setOnClickListener {
            //测试 个体户  对私账户结算


            val user = UserManager.getLoginInfo()
            val member = user?.uid

            model.companyAccount.memberId = member
            model.personalAccount.memberId = member

            if (model.applyShopInfo.value?.shopType == 1) {
                //企业
                if (selectAccount.value == 0) {
                    //对公账户结算
                    model.applyShopInfo.value?.also { applyShopInfo ->
                        applyShopInfo.accttype = 1
                        applyShopInfo.bankAccountName = model.companyAccount.openAccountName ?: "1"
                        applyShopInfo.bankNumber = model.companyAccount.cardNo ?: "1"
                        applyShopInfo.bankNo = model.companyAccount.bankName ?: "1"
                        applyShopInfo.bankName = model.companyAccount.subBankName ?: "1"
                        applyShopInfo.bankUrls = model.companyAccount.bankUrls ?: "1"

                        applyShopInfo.bankCode = model.companyAccount.bankCode
                        applyShopInfo.subBankCode = model.companyAccount.subBankCode
                        applyShopInfo.province = model.companyAccount.province
                        applyShopInfo.city = model.companyAccount.city
                    }
                    model.applyShopInfo.value?.bankCard = model.companyAccount
                    checkBoolean(isCompanyReady())
                    {
                        "请将对公账户填写完整"
                    }
                    lifecycleScope.launch(Dispatchers.IO)
                    {
                        model.companyAccount.isDefault = 1
                        MainRepository.apiService.bindTestBankCard(model.companyAccount)
                            .awaitHiResponse()
                    }

                } else {
                    //对私账户结算
                    model.applyShopInfo.value?.also { applyShopInfo ->
                        applyShopInfo.accttype = 0
                        applyShopInfo.bankAccountName = model.personalAccount.openAccountName ?: "1"
                        applyShopInfo.bankNumber = model.personalAccount.cardNo ?: "1"

                        applyShopInfo.bankNo = model.personalAccount.bankName ?: "1"
                        applyShopInfo.bankName = model.personalAccount.subBankName ?: "1"
                        applyShopInfo.bankUrls = model.personalAccount.bankUrls ?: "1"

                        applyShopInfo.bankCode = model.personalAccount.bankCode
                        applyShopInfo.subBankCode = model.personalAccount.subBankCode
                        applyShopInfo.province = model.personalAccount.province
                        applyShopInfo.city = model.personalAccount.city

                    }

                    model.personalAccount.isDefault = 1
                    model.applyShopInfo.value?.bankCard = model.personalAccount
                    checkBoolean(isCompanyReady())
                    {
                        "请将对公账户填写完整"
                    }
                    checkBoolean(isPersonalReady())
                    {
                        "请将对私账户填写完整"
                    }
                    lifecycleScope.launch(Dispatchers.IO)
                    {

                        MainRepository.apiService.bindTestBankCard(model.personalAccount)
                            .awaitHiResponse()

                    }
                    lifecycleScope.launch(Dispatchers.IO)
                    {

                        MainRepository.apiService.bindTestBankCard(model.companyAccount)
                            .awaitHiResponse()
                    }
                }
            } else {
                //个体户
                if (selectAccount.value == 0) {
                    //对公账户结算
                    model.applyShopInfo.value?.also { applyShopInfo ->
                        applyShopInfo.accttype = 1
                        applyShopInfo.bankAccountName = model.companyAccount.openAccountName ?: "1"
                        applyShopInfo.bankNumber = model.companyAccount.cardNo ?: "1"

                        applyShopInfo.bankNo = model.companyAccount.bankName ?: "1"
                        applyShopInfo.bankName = model.companyAccount.subBankName ?: "1"
                        applyShopInfo.bankUrls = model.companyAccount.bankUrls ?: "1"

                        applyShopInfo.bankCode = model.companyAccount.bankCode
                        applyShopInfo.subBankCode = model.companyAccount.subBankCode
                        applyShopInfo.province = model.companyAccount.province
                        applyShopInfo.city = model.companyAccount.city


                    }
                    model.applyShopInfo.value?.bankCard = model.companyAccount
                    model.companyAccount.isDefault = 1
                    checkBoolean(isCompanyReady())
                    {
                        "请将对公账户填写完整"
                    }
                    lifecycleScope.launch(Dispatchers.IO)
                    {
                        Log.d("WZY TEst 4", model.companyAccount.bankUrls.toString())
                        Log.d("WZY TEst 5", model.companyAccount.province.toString())
                        Log.d("WZY TEst 5", model.companyAccount.city.toString())
                        MainRepository.apiService.bindTestBankCard(model.companyAccount)
                            .awaitHiResponse()
                    }
                } else {
                    //对私账户结算
                    model.applyShopInfo.value?.also { applyShopInfo ->
                        applyShopInfo.accttype = 0
                        applyShopInfo.bankAccountName = model.personalAccount.openAccountName ?: "1"
                        applyShopInfo.bankNumber = model.personalAccount.cardNo ?: "1"
                        applyShopInfo.bankNo = model.personalAccount.bankName ?: "1"
                        applyShopInfo.bankName = model.personalAccount.subBankName ?: "1"
                        applyShopInfo.bankUrls = model.personalAccount.bankUrls ?: "1"

                        applyShopInfo.bankCode = model.personalAccount.bankCode
                        applyShopInfo.subBankCode = model.personalAccount.subBankCode
                        applyShopInfo.province = model.personalAccount.province
                        applyShopInfo.city = model.personalAccount.city


                    }
                    model.applyShopInfo.value?.bankCard = model.personalAccount
                    model.personalAccount.isDefault = 1
                    checkBoolean(isPersonalReady())
                    {
                        "请将对私账户填写完整"
                    }
                    lifecycleScope.launch(Dispatchers.IO)
                    {
                        MainRepository.apiService.bindTestBankCard(model.personalAccount)
                            .awaitHiResponse()
                    }
                }
            }





            model.go.value = 1

        }
    }

    private fun initObserver() {


        //确定当前结算账户类型
        selectAccount.observe(this, Observer {
            //对公账户作为结算账户
            if (it == 0) {
                //企业账户     个体户账户


                binding.accountCompany2.isSelected = true

                binding.accountPersonal2.isSelected = false

            }
            //对私账户作为结算账户
            if (it == 1) {


                binding.accountCompany2.isSelected = false

                binding.accountPersonal2.isSelected = true

            }


        })


    }


    @Subscribe(threadMode = ThreadMode.MAIN)
    fun updateBank(bank: BankDetail) {

        when (bank.type) {
            COMPANY_BANK -> {
                binding.bankNoCtv.text = bank.bafName
                model.companyAccount.bankName = bank.bafName
                model.companyAccount.bankCode = bank.bankCode
                model.companyAccount.province = bank.province
                model.companyAccount.city = bank.city
            }
            COMPANY_SUB -> {
                binding.bankNameCtv.text = bank.bankName
                model.companyAccount.subBankName = bank.bankName
                model.companyAccount.subBankCode = bank.netBankCode
            }

            PERSONAL_BANK -> {
                binding.bankNoPtv.text = bank.bafName
                model.personalAccount.bankName = bank.bafName
                model.personalAccount.bankCode = bank.bankCode
                model.personalAccount.province = bank.province
                model.personalAccount.city = bank.city
            }
            PERSONAL_SUB -> {
                binding.bankNamePtv.text = bank.bankName
                model.personalAccount.subBankName = bank.bankName
                model.personalAccount.subBankCode = bank.netBankCode
            }
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun getUploadImageUrl(event: ApplyShopImageEvent) {
        when (event.type) {
            PICTURE_COMPANY -> {
                model.companyAccount.bankUrls = event.remoteUrl
                binding.acctlicensepictv.text = "已上传"
            }
            PICTURE_PERSONAL -> {
                model.personalAccount.bankUrls = event.remoteUrl
                binding.settleBankPictv.text = "已上传"
            }

        }
    }


}