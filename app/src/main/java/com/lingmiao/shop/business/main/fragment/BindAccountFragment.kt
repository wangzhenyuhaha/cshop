package com.lingmiao.shop.business.main.fragment

import android.content.Intent
import android.graphics.Color
import android.text.Spannable
import android.text.SpannableStringBuilder
import android.text.style.ForegroundColorSpan
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.blankj.utilcode.util.ActivityUtils
import com.blankj.utilcode.util.ToastUtils
import com.james.common.base.BasePreImpl
import com.james.common.base.BasePresenter
import com.james.common.base.BaseVBFragment
import com.james.common.utils.DialogUtils
import com.james.common.utils.exts.gone
import com.james.common.utils.exts.singleClick
import com.james.common.utils.exts.visiable
import com.lingmiao.shop.R
import com.lingmiao.shop.base.UserManager
import com.lingmiao.shop.business.main.ApplyShopInfoActivity
import com.lingmiao.shop.business.main.ApplyShopInfoViewModel
import com.lingmiao.shop.business.main.SubBranchActivity
import com.lingmiao.shop.business.main.bean.ApplyShopInfo
import com.lingmiao.shop.business.main.bean.BankDetail
import com.lingmiao.shop.business.main.bean.BindBankCardDTO
import com.lingmiao.shop.databinding.FragmentBindAccountBinding
import kotlinx.android.synthetic.main.fragment_bind_account.*
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import java.util.*

class BindAccountFragment : BaseVBFragment<FragmentBindAccountBinding, BasePresenter>() {


    private val model by activityViewModels<ApplyShopInfoViewModel>()

    companion object {

        //对公所属银行
        const val COMPANY_BANK = 0

        //对公所属支行
        const val COMPANY_SUB = 1

        //对私所属银行
        const val PERSONAL_BANK = 2

        //对私所属支行
        const val PERSONAL_SUB = 3
    }

    private var intent: Intent? = null

    //检查银行卡账户是否完整
    private fun isAccountReady(account: BindBankCardDTO?): Boolean {
        account?.also {
            return !(it.openAccountName.isNullOrEmpty() || it.cardNo.isNullOrEmpty() || it.bankCardType == null
                    || it.province.isNullOrEmpty() || it.city.isNullOrEmpty() || it.bankCode.isNullOrEmpty()
                    || it.bankName.isNullOrEmpty() || it.subBankName.isNullOrEmpty() || it.subBankCode.isNullOrEmpty()
                    || it.bankUrls.isNullOrEmpty())
        }
        return false
    }

    //将被绑定的银行卡赋值给申请店铺的接口
    private fun isCopy(info: ApplyShopInfo?, account: BindBankCardDTO) {

        info?.also {
            it.accttype = account.bankCardType
            it.bankAccountName = account.openAccountName
            it.bankNumber = account.cardNo
            it.bankNo = account.bankName
            it.bankName = account.subBankName
            it.bankUrls = account.bankUrls
            it.bankCode = account.bankCode
            it.subBankCode = account.subBankCode
            it.province = account.province
            it.city = account.city

            it.bankCard = account
        }
    }

    override fun initBundles() {
        super.initBundles()
        //跳转到银行选择界面
        intent = Intent(requireActivity(), SubBranchActivity::class.java)
    }

    override fun initViewsAndData(rootView: View) {

        model.setTitle("结算账户绑定")

        if (model.applyShopInfo.value?.shopType == 4) {
            //个人
            binding.personalModule.visiable()
            model.whichAccountToUse.value = 1
        }

        initListener()
        initObserver()
    }


    private fun initListener() {

        //控制对公模块可见性
        binding.accountCompanyIV.setOnClickListener {
            if (model.companyModule.value == View.VISIBLE) {
                model.companyModule.value = View.GONE
            } else {
                model.companyModule.value = View.VISIBLE
            }
        }

        //控制对私模块可见性
        binding.accountPersonalIV.setOnClickListener {
            if (model.personalModule.value == View.VISIBLE) {
                model.personalModule.value = View.GONE
            } else {
                model.personalModule.value = View.VISIBLE
            }
        }


        if (!model.shopOpenOrNot) {

            //结算账户类型
            binding.account1.setOnClickListener {
                //对公账户结算
                model.whichAccountToUse.value = 0
            }

            binding.account2.setOnClickListener {
                //对私账户结算
                model.whichAccountToUse.value = 1
            }

            //账户名称
            binding.bankaccountnameC.setOnClickListener {

                DialogUtils.showInputDialogEmpty(
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

            //账户号
            binding.banknumberC.setOnClickListener {
                DialogUtils.showInputDialogEmptyNumber(
                    requireActivity(),
                    "账户号",
                    "",
                    "请输入",
                    model.companyAccount.value?.cardNo,
                    "取消",
                    "保存",
                    null
                ) {
                    if (it.isEmpty()) {
                        binding.banknumberCtv.text = "请填写账户号"
                        model.companyAccount.value?.also { bank ->
                            bank.cardNo = null
                            bank.province = null
                            bank.city = null
                            bank.bankCode = null
                            bank.bankName = null
                            bank.subBankName = null
                            bank.subBankCode = null
                        }
                        model.ocrCBankVisibilityU.value = 1
                    } else {
                        //获取到新的银行卡号时，先与之前的卡号进行对比，
                        //如果一样不做任何处理，不一样则主动调取银行卡所属银行名以及号码
                        if (model.companyAccount.value?.cardNo != it) {
                            //不一样，开始处理
                            binding.banknumberCtv.text = it
                            model.companyAccount.value?.cardNo = it
                            model.ocrCBankVisibility.value = 1
                        }
                    }
                }
            }

            //持卡人
            binding.bankaccountnameP.setOnClickListener {

                DialogUtils.showInputDialogEmpty(
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


            //商户签字授权书
            binding.authorpic.setOnClickListener {
                val bundle = bundleOf("type" to ApplyShopInfoActivity.AUTHOR_PIC)
                findNavController().navigate(
                    R.id.action_bindAccountFragment_to_shopPhotoFragment,
                    bundle
                )
            }

            //卡号
            binding.banknumberP.setOnClickListener {
                DialogUtils.showInputDialogEmptyNumber(
                    requireActivity(),
                    "卡号",
                    "",
                    "请输入",
                    model.personalAccount.value?.cardNo,
                    "取消",
                    "保存",
                    null
                ) {
                    if (it.isEmpty()) {
                        binding.banknumberPtv.text = "请输入卡号"
                        model.personalAccount.value?.also { bank ->
                            bank.cardNo = null
                            bank.province = null
                            bank.city = null
                            bank.bankCode = null
                            bank.bankName = null
                            bank.subBankName = null
                            bank.subBankCode = null
                        }
                        model.ocrPBankVisibilityU.value = 1
                    } else {
                        if (model.personalAccount.value?.cardNo != it) {
                            binding.banknumberPtv.text = it
                            model.personalAccount.value?.cardNo = it
                            model.ocrPBankVisibility.value = 0
                        }
                    }

                }
            }

            //对公，所属银行
            binding.bankC.setOnClickListener {
                intent?.apply {
                    putExtra("type", COMPANY_BANK)
                    putExtra("bank", "nothing")
                    ActivityUtils.startActivity(this)
                }
            }

            //对公，所属支行
            binding.subBankC.setOnClickListener {

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

            //对私，所属银行
            binding.bankP.setOnClickListener {
                intent?.apply {
                    putExtra("type", PERSONAL_BANK)
                    putExtra("bank", "nothing")
                    ActivityUtils.startActivity(this)
                }

            }

            //对私，所属支行
            binding.subBankP.setOnClickListener {
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

            //开户许可证照片
            binding.bankUrlC.setOnClickListener {
                val bundle = bundleOf("type" to ApplyShopInfoActivity.PICTURE_COMPANY_ACCOUNT)
                findNavController().navigate(
                    R.id.action_bindAccountFragment_to_shopPhotoFragment,
                    bundle
                )
            }

            //银行卡正面照
            binding.bankUrlP.setOnClickListener {
                val bundle = bundleOf("type" to ApplyShopInfoActivity.PICTURE_PERSONAL_ACCOUNT)
                findNavController().navigate(
                    R.id.action_bindAccountFragment_to_shopPhotoFragment,
                    bundle
                )
            }

        } else {
            binding.tvApplyShopInfoNext.text = "返回首页"
        }
        //申请店铺
        binding.tvApplyShopInfoNext.singleClick {

            if (!model.shopOpenOrNot) {

                //检查资料是否填写完整
                if (!binding.account1.isSelected && !binding.account2.isSelected) {
                    ToastUtils.showShort("请选择结算账户")
                    return@singleClick
                }

                //企业选择对私结算时上传授权承诺书
                if (model.applyShopInfo.value?.shopType == 1 && binding.account2.isSelected) {
                    if (model.applyShopInfo.value?.authorpic.isNullOrEmpty()) {
                        ToastUtils.showShort("请选择签约承诺函")
                        return@singleClick
                    }
                }

                //给memberId赋值
                UserManager.getLoginInfo()?.uid?.also {
                    model.companyAccount.value?.memberId = it
                    model.personalAccount.value?.memberId = it
                }

                //将其他营业资质照片上传
                model.applyShopInfo.value?.also {
                    it.other_certificates_imgs = null
                    if (it.other_Pic_One != null && it.other_Pic_Two != null) {
                        it.other_certificates_imgs = it.other_Pic_One + "," + it.other_Pic_Two
                    }
                    if (it.other_Pic_One != null && it.other_Pic_Two == null) {
                        it.other_certificates_imgs = it.other_Pic_One
                    }
                    if (it.other_Pic_One == null && it.other_Pic_Two != null) {
                        it.other_certificates_imgs = it.other_Pic_Two
                    }
                }

                //检查资料是否齐全,并且绑定银行卡   绑定银行卡 0 绑定对公账户， 1，绑定对私账户  2绑定对公和对私账户
                when (model.applyShopInfo.value?.shopType) {
                    1 -> {
                        //企业
                        if (model.whichAccountToUse.value == 0) {
                            //对公账户结算
                            model.companyAccount.value?.isDefault = 1
                            model.personalAccount.value?.isDefault = 0

                            if (!isAccountReady(model.companyAccount.value)) {
                                ToastUtils.showShort("请将对公账户填写完整")
                                return@singleClick
                            }
                            model.companyAccount.value?.also {
                                isCopy(model.applyShopInfo.value, it)
                            }
                            if (isAccountReady(model.personalAccount.value)) {
                                model.bindBandCard.value = 2
                            } else {
                                model.bindBandCard.value = 0
                            }

                        } else {
                            //对私账户结算
                            model.personalAccount.value?.isDefault = 1
                            model.companyAccount.value?.isDefault = 0

                            if (!isAccountReady(model.companyAccount.value)) {
                                ToastUtils.showShort("请将对公账户填写完整")
                                return@singleClick
                            }
                            if (!isAccountReady(model.personalAccount.value)) {
                                ToastUtils.showShort("请将对私账户填写完整")
                                return@singleClick
                            }
                            model.personalAccount.value?.also {
                                isCopy(model.applyShopInfo.value, it)
                            }
                            model.bindBandCard.value = 2
                        }
                    }
                    3 -> {
                        //个体户
                        if (model.whichAccountToUse.value == 0) {
                            //对公账户结算
                            model.companyAccount.value?.isDefault = 1
                            model.personalAccount.value?.isDefault = 0

                            if (!isAccountReady(model.companyAccount.value)) {
                                ToastUtils.showShort("请将对公账户填写完整")
                                return@singleClick
                            }
                            model.companyAccount.value?.also {
                                isCopy(model.applyShopInfo.value, it)
                            }
                            if (isAccountReady(model.personalAccount.value)) {
                                model.bindBandCard.value = 2
                            } else {
                                model.bindBandCard.value = 0
                            }

                        } else {
                            //对私账户结算
                            model.personalAccount.value?.isDefault = 1
                            model.companyAccount.value?.isDefault = 0

                            if (!isAccountReady(model.personalAccount.value)) {
                                ToastUtils.showShort("请将对私账户填写完整")
                                return@singleClick
                            }
                            model.personalAccount.value?.also {
                                isCopy(model.applyShopInfo.value, it)
                            }
                            if (isAccountReady(model.companyAccount.value)) {
                                model.bindBandCard.value = 2
                            } else {
                                model.bindBandCard.value = 1
                            }
                        }
                    }
                    4 -> {
                        //个人只有对私账户
                        model.personalAccount.value?.isDefault = 1

                        if (!isAccountReady(model.personalAccount.value)) {
                            ToastUtils.showShort("请将对私账户填写完整")
                            return@singleClick
                        }
                        model.personalAccount.value?.also {
                            isCopy(model.applyShopInfo.value, it)
                        }

                        model.bindBandCard.value = 1

                    }
                }
            } else {
                requireActivity().finish()
            }

        }

        //点击下载DOC文件
        applyShopHintTV.setOnClickListener {
            model.authorpic.value = 1
        }

    }


    private fun initObserver() {

        //重建View以后根据当前状态绘制UI
        model.applyShopInfo.observe(this, Observer { info ->
            when (info.shopType) {
                1 -> {
                    //企业
                    binding.shopType.text = "企业"
                    binding.shopTypeTextView.text = "对公账户必须填写"
                    binding.accountCompanyTVC.visibility = View.VISIBLE
                    binding.accountCompanyTVP.visibility = View.GONE

                    binding.applyShopHintTV.visiable()
                    //设置文字下载承诺函
                    val content = "若企业结算账户为对私，需下载《签约承诺函》并盖章，然后上传图片。点击可下载"
                    val builder = SpannableStringBuilder(content)
                    val blueSpan = ForegroundColorSpan(Color.parseColor("#3870EA"))
                    builder.setSpan(blueSpan, 32, 37, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
                    applyShopHintTV.text = builder
                }
                3 -> {
                    //个体户
                    binding.shopType.text = "个体户"
                    binding.shopTypeTextView.text = "（结算账户必须填写）"
                    binding.accountCompanyTVC.visibility = View.GONE
                    binding.accountCompanyTVP.visibility = View.VISIBLE
                    binding.applyShopHintTV.gone()
                }
                4 -> {
                    //结算账户
                    binding.shopType.text = "结算账户"
                    binding.shopTypeTextView.gone()
                    binding.accountPersonalTV.text = "（请绑定负责人本人的银行卡）"
                    binding.companyTitle.gone()
                    binding.view1.gone()
                    binding.companyModule.gone()
                    binding.personalTitle.gone()
                    binding.view2.gone()

                    binding.applyShopHintTV.gone()


                }
            }


            if (!info.authorpic.isNullOrEmpty()) {
                binding.authorpicTV.text = "已上传"
            }

        })

        //确定当前结算账户类型
        model.whichAccountToUse.observe(this, Observer {
            //对公账户作为结算账户
            if (it == 0) {
                binding.account1.isSelected = true
                binding.account2.isSelected = false
                model.companyModule.value = View.VISIBLE
                binding.authorpicView.gone()
                binding.authorpic.gone()
            }
            //对私账户作为结算账户
            if (it == 1) {
                binding.account1.isSelected = false
                binding.account2.isSelected = true

                when (model.applyShopInfo.value?.shopType) {
                    1 -> {
                        //企业
                        model.companyModule.value = View.VISIBLE
                        model.personalModule.value = View.VISIBLE
                        //需要上传承诺函
                        binding.authorpicView.visiable()
                        binding.authorpic.visiable()
                    }
                    3 -> {
                        //个体户
                        model.personalModule.value = View.VISIBLE
                        binding.authorpicView.gone()
                        binding.authorpic.gone()
                    }
                    4 -> {
                        //个人店铺
                        model.companyModule.value = View.GONE
                        model.personalModule.value = View.VISIBLE
                        binding.authorpicView.gone()
                        binding.authorpic.gone()

                    }
                }
            }
        })

        model.companyModule.observe(this, Observer {
            binding.companyModule.visibility = it
            binding.accountCompanyIV.isSelected = it == View.VISIBLE
        })

        model.personalModule.observe(this, Observer {
            binding.personalModule.visibility = it
            binding.accountPersonalIV.isSelected = it == View.VISIBLE

        })

        //观察对公账户P
        model.companyAccount.observe(this, Observer {
            if (!it.openAccountName.isNullOrEmpty()) {
                binding.bankaccountnameCtv.text = it.openAccountName
            }
            if (!it.bankUrls.isNullOrEmpty()) {
                binding.bankUrlCTV.text = "已上传"
            }
            if (!it.cardNo.isNullOrEmpty()) {
                binding.banknumberCtv.text = it.cardNo
            }
            //判断是否获取银行卡省市
            if (!(it.bankName.isNullOrEmpty() || it.bankCode.isNullOrEmpty())) {
                binding.bankCtv.text = it.bankName
            }

            if (!(it.subBankName.isNullOrEmpty() || it.subBankCode.isNullOrEmpty())) {
                binding.subBankCTV.text = it.subBankName
            }
        })

        //观察对私账户
        model.personalAccount.observe(this, Observer {
            if (!it.openAccountName.isNullOrEmpty()) {
                binding.bankaccountnamePtv.text = it.openAccountName
            }
            if (!it.bankUrls.isNullOrEmpty()) {
                binding.bankUrlPTV.text = "已上传"
            }
            if (!it.cardNo.isNullOrEmpty()) {
                binding.banknumberPtv.text = it.cardNo
            }
            //判断是否获取银行卡省市
            if (!(it.bankName.isNullOrEmpty() || it.bankCode.isNullOrEmpty())) {
                binding.bankPtv.text = it.bankName
            }
            if (!(it.subBankName.isNullOrEmpty() || it.subBankCode.isNullOrEmpty())) {
                binding.subBankPTV.text = it.subBankName
            }
        })

        model.ocrCBankVisibilityU.observe(this, Observer {
            //更新了卡号，支行信息肯定没了,需要重新选择支行信息
            binding.subBankCTV.text = "请选择所属支行"
            //确定所属银行信息
            if (!model.companyAccount.value?.bankName.isNullOrEmpty()) {
                binding.bankCtv.text = model.companyAccount.value?.bankName
            } else {
                binding.bankCtv.text = "请选择所属银行"
            }
        })

        model.ocrPBankVisibilityU.observe(this, Observer {

            if (model.personalAccount.value?.cardNo != null) {
                binding.banknumberPtv.text = model.personalAccount.value?.cardNo
            } else {
                binding.banknumberPtv.text = "请输入卡号"
            }

            //更新了卡号，支行信息肯定没了,需要重新选择支行信息
            binding.subBankPTV.text = "请选择所属支行"
            //确定所属银行信息
            if (!model.personalAccount.value?.bankName.isNullOrEmpty()) {
                binding.bankPtv.text = model.personalAccount.value?.bankName
            } else {
                binding.bankPtv.text = "请选择所属银行"
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
    fun updateBank(bank: BankDetail) {
        when (bank.type) {
            COMPANY_BANK -> {
                if (model.companyAccount.value?.bankName != bank.bafName) {
                    binding.subBankCTV.text = "请选择所属支行"
                    model.companyAccount.value?.subBankName = null
                    model.companyAccount.value?.subBankCode = null
                    model.companyAccount.value?.province = null
                    model.companyAccount.value?.city = null
                }
                binding.bankCtv.text = bank.bafName
                model.companyAccount.value?.bankName = bank.bafName
                model.companyAccount.value?.bankCode = bank.bankCode

            }
            COMPANY_SUB -> {
                binding.subBankCTV.text = bank.bankName
                model.companyAccount.value?.subBankName = bank.bankName
                model.companyAccount.value?.subBankCode = bank.netBankCode
                model.companyAccount.value?.province = bank.province
                model.companyAccount.value?.city = bank.city

            }

            PERSONAL_BANK -> {
                if (model.personalAccount.value?.bankName != bank.bafName) {
                    binding.subBankPTV.text = "请选择所属支行"
                    model.personalAccount.value?.subBankName = null
                    model.personalAccount.value?.subBankCode = null
                    model.personalAccount.value?.province = null
                    model.personalAccount.value?.city = null
                }
                binding.bankPtv.text = bank.bafName
                model.personalAccount.value?.bankName = bank.bafName
                model.personalAccount.value?.bankCode = bank.bankCode

            }
            PERSONAL_SUB -> {
                binding.subBankPTV.text = bank.bankName
                model.personalAccount.value?.subBankName = bank.bankName
                model.personalAccount.value?.subBankCode = bank.netBankCode
                model.personalAccount.value?.province = bank.province
                model.personalAccount.value?.city = bank.city
            }
        }
    }


    override fun createPresenter() = BasePreImpl(this)

    override fun getBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ) = FragmentBindAccountBinding.inflate(inflater, container, false)


    override fun useEventBus() = true


}