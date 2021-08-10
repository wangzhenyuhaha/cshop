package com.lingmiao.shop.business.main.fragment

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import com.google.gson.annotations.SerializedName
import com.james.common.base.BasePreImpl
import com.james.common.base.BasePresenter
import com.james.common.base.BaseVBFragment
import com.james.common.netcore.networking.http.core.awaitHiResponse
import com.james.common.utils.DialogUtils
import com.lingmiao.shop.MyApp
import com.lingmiao.shop.base.UserManager
import com.lingmiao.shop.business.main.ApplyShopInfoActivity
import com.lingmiao.shop.business.main.api.MainRepository
import com.lingmiao.shop.business.main.bean.ApplyShopImageEvent
import com.lingmiao.shop.business.wallet.api.WalletRepository
import com.lingmiao.shop.business.wallet.bean.BankCardVo
import com.lingmiao.shop.databinding.FragmentBindAccountBinding
import kotlinx.android.synthetic.main.fragment_replenish_info.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode

class BindAccountFragment : BaseVBFragment<FragmentBindAccountBinding, BasePresenter>() {

    //0表示对公账户，1表示对私账户
    private val selectAccount: MutableLiveData<Int> = MutableLiveData()

    private val model by activityViewModels<ApplyShopInfoActivity.ApplyShopInfoViewModel>()

    //测试所用
    private var test: Int? = null

    companion object {
        const val PICTURE_COMPANY = 0
        const val PICTURE_PERSONAL = 1
    }


    //对公账户
    val companyAccount = BindBankCardDTO ().also {
        it.bankCardType = 1
    }

    //对私账户
    val personalAccount = BindBankCardDTO ().also {
        it.bankCardType = 0
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

        //测试所用  3 个体户   1   企业
     //   model.applyShopInfo.value?.shopType = 3
        test = model.applyShopInfo.value?.shopType

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
        binding.accountCompany.setOnClickListener {
            //对公账户结算
            selectAccount.value = 0
        }

        binding.accountPersonal.setOnClickListener {
            //对私账户结算
            selectAccount.value = 1
        }


        //账户名称
        binding.bankaccountnameC.setOnClickListener {

            DialogUtils.showInputDialog(
                requireActivity(),
                "账户名称",
                "",
                "请输入",
                companyAccount.openAccountName,
                "取消",
                "保存",
                null
            ) {
                binding.bankaccountnameCtv.text = it
                companyAccount.openAccountName = it
            }
        }

        //账户号
        binding.banknumberC.setOnClickListener {

            DialogUtils.showInputDialog(
                requireActivity(),
                "账户号",
                "",
                "请输入",
                companyAccount.cardNo,
                "取消",
                "保存",
                null
            ) {
                binding.banknumberCtv.text = it
                companyAccount.cardNo = it
            }

        }

        //开户地区
        binding.districtcodeC.setOnClickListener {

            Toast.makeText(MyApp.getInstance() as Context, "暂不处理，地址默认安徽安庆", Toast.LENGTH_SHORT)
                .show()
//            DialogUtils.showInputDialog(
//                requireActivity(),
//                "开户地区",
//                "",
//                "请输入",
//                "测试",
//                "取消",
//                "保存",
//                null
//            ) {
//                binding.districtcodeCtv.text = it
//                companyAccount.bankprovince = "安徽"
//                companyAccount.bankcity = "安庆"
//            }

            binding.districtcodeCtv.text = "安徽安庆"
            companyAccount.province = "安徽"
            companyAccount.city = "安庆"
            companyAccount.placeCode = "1"

        }

        //所属银行
        binding.bankNoC.setOnClickListener {

            DialogUtils.showInputDialog(
                requireActivity(),
                "所属银行",
                "",
                "请输入",
                companyAccount.bankName,
                "取消",
                "保存",
                null
            ) {
                binding.bankNoCtv.text = it
                companyAccount.bankName = it
            }

        }

        //所属支行
        binding.bankNameC.setOnClickListener {

            DialogUtils.showInputDialog(
                requireActivity(),
                "所属支行",
                "",
                "请输入",
                companyAccount.subBankName,
                "取消",
                "保存",
                null
            ) {
                binding.bankNameCtv.text = it
                companyAccount.subBankName = it
            }

        }

        //开户许可证照片
        binding.acctlicensepic.setOnClickListener {

            ApplyShopInfoActivity.goUploadImageActivity(
                requireActivity(),
                BindAccountFragment.PICTURE_COMPANY,
                "上传开户许可证照片",
                companyAccount.bankUrls
            )


        }

        //持卡人
        binding.bankaccountnameP.setOnClickListener {
            DialogUtils.showInputDialog(
                requireActivity(),
                "持卡人",
                "",
                "请输入",
                personalAccount.openAccountName,
                "取消",
                "保存",
                null
            ) {
                binding.bankaccountnamePtv.text = it
                personalAccount.openAccountName = it
            }
        }

        //银行卡正面照
        binding.settleBankPic.setOnClickListener {
            ApplyShopInfoActivity.goUploadImageActivity(
                requireActivity(),
                BindAccountFragment.PICTURE_PERSONAL,
                "上传银行卡正面照",
                personalAccount.bankUrls
            )
        }

        //卡号
        binding.banknumberP.setOnClickListener {
            DialogUtils.showInputDialog(
                requireActivity(),
                "卡号",
                "",
                "请输入",
                personalAccount.cardNo,
                "取消",
                "保存",
                null
            ) {
                binding.bankaccountnamePtv.text = it
                personalAccount.cardNo = it
            }
        }

        //开户地区
        binding.districtcodeP.setOnClickListener {

        }

        //所属银行
        binding.bankNoP.setOnClickListener {
            DialogUtils.showInputDialog(
                requireActivity(),
                "所属银行",
                "",
                "请输入",
                personalAccount.bankName,
                "取消",
                "保存",
                null
            ) {
                binding.bankNoPtv.text = it
                personalAccount.bankName = it
            }


        }

        //所属支行
        binding.bankNameP.setOnClickListener {
            DialogUtils.showInputDialog(
                requireActivity(),
                "所属支行",
                "",
                "请输入",
                personalAccount.subBankName,
                "取消",
                "保存",
                null
            ) {
                binding.bankNoPtv.text = it
                personalAccount.subBankName = it
            }
        }


        //DialogUtils.showInputDialog(
        //                requireActivity(),
        //                "经营内容",
        //                "",
        //                "请输入",
        //                model.applyShopInfo.value?.scope,
        //                "取消",
        //                "保存",
        //                null
        //            ) {
        //                binding.scopeTextView.text = it
        //                model.applyShopInfo.value?.scope = it
        //            }


        //账户名称


        binding.tvApplyShopInfoNext.setOnClickListener {
            //测试 个体户  对私账户结算

            model.applyShopInfo.value?.also { applyShopInfo ->
                applyShopInfo.accttype = 0
                applyShopInfo.bankAccountName = companyAccount.openAccountName
                applyShopInfo.bankNumber = companyAccount.cardNo
                applyShopInfo.districtcode = companyAccount.placeCode
                applyShopInfo.bankNo = companyAccount.bankName
                applyShopInfo.bankName = companyAccount.subBankName
                applyShopInfo.acctlicensepic = companyAccount.bankUrls

            }

            val user = UserManager.getLoginInfo()
            val member = user?.uid

            Log.d("WZY", member.toString())
            companyAccount.memberId = member?.toInt()
            personalAccount.memberId = member?.toInt()


            if (model.applyShopInfo.value?.shopType == 1) {
                //企业
                if (selectAccount.value == 0) {
                    //对公账户结算

                        model.applyShopInfo.value?.bankCard = companyAccount
                    lifecycleScope.launch(Dispatchers.IO)
                    {
                        val response = MainRepository.apiService.bindTestBankCard(companyAccount)
                            .awaitHiResponse()

                        if (response.isSuccess) {
                            Log.d("WZYBB", "GIAO")
                        } else {
                            Log.d("WZYBB", "失败了")

                        }
                    }


                } else {
                    //对私账户结算

                    model.applyShopInfo.value?.bankCard = personalAccount

                    lifecycleScope.launch(Dispatchers.IO)
                    {
                        val response = MainRepository.apiService.bindTestBankCard(companyAccount)
                            .awaitHiResponse()

                        if (response.isSuccess) {
                            Log.d("WZYBB", "GIAO")
                        } else {
                            Log.d("WZYBB", "失败了")

                        }
                    }

                    lifecycleScope.launch(Dispatchers.IO)
                    {
                        val response = MainRepository.apiService.bindTestBankCard(personalAccount)
                            .awaitHiResponse()

                        if (response.isSuccess) {
                            Log.d("WZYBB", "GIAO")
                        } else {
                            Log.d("WZYBB", "失败了")

                        }
                    }

                }
            } else {
                //个体户
                if (selectAccount.value == 0) {
                    //对公账户结算
                    model.applyShopInfo.value?.bankCard = companyAccount

                    lifecycleScope.launch(Dispatchers.IO)
                    {
                        val response = MainRepository.apiService.bindTestBankCard(companyAccount)
                            .awaitHiResponse()

                        if (response.isSuccess) {
                            Log.d("WZYBB", "GIAO")
                        } else {
                            Log.d("WZYBB", "失败了")

                        }
                    }


                } else {
                    //对私账户结算

                    model.applyShopInfo.value?.bankCard = personalAccount
                    lifecycleScope.launch(Dispatchers.IO)
                    {
                        val response = MainRepository.apiService.bindTestBankCard(personalAccount)
                            .awaitHiResponse()

                        if (response.isSuccess) {
                            Log.d("WZYBB", "GIAO")
                        } else {
                            Log.d("WZYBB", "失败了")

                        }
                    }

                }
            }


              model.go.value = 1
//            val user = UserManager.getLoginInfo()
//            val member = user?.accountMemberId
//
//
//            val data = BankCardVo()
//            data.bankCardType = 0
//            //银行名
//            data.bankName = "中国工商银行"
//            data.openAccountName = "汪振宇"
//            data.cardNo = "1111122222"
//
//
//
//            lifecycleScope.launch(Dispatchers.Main) {
//                Log.d("AAAAA", "1")
//                val resp = WalletRepository.bindBankCard(data)
//                Log.d("AAAAA", "1")     // if ()
//            }


            //override fun submitBankCard(data: BankCardVo) {
            //        mCoroutine.launch {
            //
            //            view.showDialogLoading();
            //
            //            val resp = WalletRepository.bindBankCard(data);
            //
            //            handleResponse(resp) {
            //                ToastUtils.showShort("提交成功")
            //                view.submitBankCardSuccess();
            //            }
            //            view.hideDialogLoading()
            //        }
            //    }
        }
    }

    private fun initObserver() {

        Log.d("WZY initObserver ", model.applyShopInfo.value?.shopType.toString())

        //确定当前结算账户类型
        selectAccount.observe(this, Observer {
            //对公账户作为结算账户
            if (it == 0) {
                //企业账户     个体户账户
                binding.companyModule.visibility = View.VISIBLE
                binding.personalModule.visibility = View.GONE

                binding.accountCompany1.isSelected = true
                binding.accountCompany2.isSelected = true

                binding.accountPersonal1.isSelected = false
                binding.accountPersonal2.isSelected = false

            }
            //对私账户作为结算账户
            if (it == 1) {

                Log.d("WZY", model.applyShopInfo.value?.shopType.toString())
                if (test == 1) {
//                if (model.applyShopInfo.value?.shopType == 1) {
                    //企业账户

                    binding.companyModule.visibility = View.VISIBLE
                    binding.personalModule.visibility = View.VISIBLE

                } else {
                    //个体户账户

                    binding.companyModule.visibility = View.GONE
                    binding.personalModule.visibility = View.VISIBLE
                }

                binding.accountCompany1.isSelected = false
                binding.accountCompany2.isSelected = false

                binding.accountPersonal1.isSelected = true
                binding.accountPersonal2.isSelected = true


            }


        })


    }


    @Subscribe(threadMode = ThreadMode.MAIN)
    fun getUploadImageUrl(event: ApplyShopImageEvent) {
        when (event.type) {
            PICTURE_COMPANY -> {
                companyAccount.bankUrls = event.remoteUrl
                binding.acctlicensepictv.text = "已上传"
            }
            PICTURE_PERSONAL -> {
                personalAccount.bankUrls = event.remoteUrl
                binding.settleBankPictv.text = "已上传"
            }

        }
    }

    //银行卡账户
    data class BindBankCardDTO (

        //账户名(账户名称）
        @SerializedName("open_account_name")
        var openAccountName: String? = null,

        //卡号（账户号）
        @SerializedName("card_no")
        var cardNo: String? = null,

        //账号类型  银行卡类型，0对私 1对公
        @SerializedName("bank_card_type")
        var bankCardType: Int? = null,

        //所属银行省
        @SerializedName("bank_province")
        var province: String? = null,

        //所属银行市
        @SerializedName("bank_city")
        var city: String? = null,

        //所属银行编码
        @SerializedName("bank_code")
        var bankCode: String? = null,

        //所属银行名
        @SerializedName("bank_name")
        var bankName: String? = null,

        //所属支行名
        @SerializedName("sub_bank_name")
        var subBankName: String? = null,

        //所属支行号
        @SerializedName("sub_bank_code")
        var subBankCode: String? = null,

        //是否默认卡  0不是 1是
        @SerializedName("is_default")
        var isDefault: Int? = null,

        //用户ID
        @SerializedName("member_id")
        var memberId: Int? = null,

        var bankUrls: String? = null,

        var placeCode: String? = null
    )



}