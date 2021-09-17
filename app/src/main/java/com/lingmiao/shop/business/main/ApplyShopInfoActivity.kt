package com.lingmiao.shop.business.main

import android.content.Intent
import android.os.Environment
import android.util.Log
import android.view.View
import androidx.activity.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import com.blankj.utilcode.util.GsonUtils
import com.blankj.utilcode.util.ToastUtils
import com.james.common.base.BaseActivity
import com.james.common.net.BaseResponse
import com.james.common.utils.DialogUtils
import com.james.common.utils.exts.check
import com.lingmiao.shop.R
import com.lingmiao.shop.base.CommonRepository
import com.lingmiao.shop.base.UserManager
import com.lingmiao.shop.business.main.bean.*
import com.lingmiao.shop.business.main.presenter.ApplyShopInfoPresenter
import com.lingmiao.shop.business.main.presenter.impl.ApplyShopInfoPresenterImpl
import com.lingmiao.shop.util.dateTime3Date
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.File
import java.io.FileOutputStream
import java.io.InputStream
import java.io.OutputStream
import java.util.*
import java.util.regex.Pattern

/**
 *   首页-提交资料
 */
class ApplyShopInfoActivity : BaseActivity<ApplyShopInfoPresenter>(), ApplyShopInfoPresenter.View {


    companion object {

        //营业执照
        const val LICENSE = 0

        //税务登记证照片
        const val TAXES = 1

        //组织机构代码证照片
        const val ORGAN = 2

        //店铺门头照片（内景）
        const val SHOP_FRONT = 3

        //上传身份证照片
        const val ID_CARD_FRONT = 4

        //上传其他照片
        const val OTHER_PIC = 5

        //开户许可证照片
        const val PICTURE_COMPANY_ACCOUNT = 6

        //银行卡正面照
        const val PICTURE_PERSONAL_ACCOUNT = 7

        //食品卫生许可证照片
        const val FOOD_ALLOW = 8

        //商户签约承诺函
        const val AUTHOR_PIC = 9

        //店铺租聘合同
        const val HIRE = 10

        //个人申请时店铺照片
        const val PERSONAL_SHOP = 11
    }

    private val viewModel by viewModels<ApplyShopInfoViewModel>()

    override fun getLayoutId() = R.layout.main_activity_apply_shop_info

    override fun createPresenter() = ApplyShopInfoPresenterImpl(this, this)

    override fun useLightMode() = false

    override fun initView() {

        //观察调用
        initObserver()

        val loginInfo = UserManager.getLoginInfo()

        //若店铺为开店中，则允许修改进件资料
        loginInfo?.also { it ->
            if (it.shopStatus == "OPEN") {
                //默认值为false
                viewModel.shopOpenOrNot = true
            }
        }

        loginInfo?.also { it ->
            if (it.shopStatus != null && it.shopStatus != "UN_APPLY") {

                //已申请过店铺
                viewModel.firstApplyShop = false

                //加载上次上传的信息,同时获取保存的邀请码
                mPresenter.requestShopInfoData()

                //获取已绑定的银行卡
                it.uid?.also { uid -> mPresenter.searchBankList(uid) }

            } else {

                //第一次申请店铺
                viewModel.firstApplyShop = true

                //从本地获取数据
                //从SP中获取ApplyShopInfo
                UserManager.getApplyShopInfo()?.also { info ->
                    viewModel.onShopInfoSuccess(info)
                    setOtherDate()
                }

                //获取对公账户信息
                UserManager.getCompanyAccount()?.also { account ->
                    viewModel.companyAccount.value = account
                }

                //获取对私账户信息
                UserManager.getPersonalAccount()?.also { account ->
                    viewModel.personalAccount.value = account
                }

                //考虑到极端情况，此处也许获取已绑定的银行卡
                it.uid?.also { uid -> mPresenter.searchBankList(uid) }
            }

        }

    }


    private fun setOtherDate() {

        //获取推广码
        viewModel.applyShopInfo.value?.promoCode =
            if (UserManager.getPromCode().isEmpty()) null
            else UserManager.getPromCode()

        //获取店铺类型，三证合一状态
        viewModel.thrcertflag.value = viewModel.applyShopInfo.value?.thrcertflag
        viewModel.shopType.value = viewModel.applyShopInfo.value?.shopType
    }


    private fun initObserver() {

        //修改页面title
        viewModel.title.observe(this, Observer {
            mToolBarDelegate.setMidTitle(it)
        })

        //OCR识别
        viewModel.getOCR.observe(this, Observer {
            // 1身份证人像面   2身份证国徽面   6银行卡     8 营业执照
            val url: String? = when (it) {
                8 -> {
                    viewModel.applyShopInfo.value?.licenceImg
                }
                1 -> {
                    viewModel.applyShopInfo.value?.legalImg
                }
                2 -> {
                    viewModel.applyShopInfo.value?.legalBackImg
                }
                6 -> {
                    //识别银行卡
                    viewModel.personalAccount.value?.bankUrls
                }
                else -> {
                    null
                }
            }
            url?.let { it_url ->
                mPresenter.searchOCR(it, it_url)
            }
        })

        viewModel.ocrCBankVisibility.observe(this, Observer {
            //对公  0
            //获取了一个不一样的银行卡卡号

            //清理之前的数据
            viewModel.companyAccount.value?.also {
                it.bankName = null
                it.bankCode = null
                it.subBankName = null
                it.subBankCode = null
                it.province = null
                it.city = null
            }

            //开始获取所属银行名及号码
            viewModel.companyAccount.value?.cardNo?.also { id ->
                mPresenter.searchBankName(0, id)
            }
        })

        //查询银行卡名
        viewModel.ocrPBankVisibility.observe(this, Observer {
            //对私 1
            //获取了一个不一样的银行卡卡号

            //清理之前的数据
            viewModel.personalAccount.value?.also {
                it.bankName = null
                it.bankCode = null
                it.subBankName = null
                it.subBankCode = null
                it.province = null
                it.city = null
            }

            //开始获取所属银行名及号码
            viewModel.personalAccount.value?.cardNo?.also { id ->
                mPresenter.searchBankName(1, id)
            }
        })

        //绑定银行卡 0 绑定对公账户， 1，绑定对私账户  2绑定对公和对私账户
        viewModel.bindBandCard.observe(this, Observer {
            when (it) {
                0 -> {
                    mPresenter.bindBankCard(viewModel.companyAccount.value, null)
                }
                1 -> {
                    mPresenter.bindBankCard(null, viewModel.personalAccount.value)
                }
                2 -> {
                    mPresenter.bindBankCard(
                        viewModel.companyAccount.value,
                        viewModel.personalAccount.value
                    )
                }
            }

        })


        //监听银行卡绑定情况，如成功，则申请店铺
        viewModel.bindResult.observe(this, Observer { result ->
            if (result == 1) {
                //绑定成功，调用申请店铺
                viewModel.go.value = 1
            }
        })

        //注册店铺或者修改进件资料
        viewModel.go.observe(this, Observer {
            try {
                mPresenter?.requestApplyShopInfoData(
                    viewModel.shopOpenOrNot,
                    viewModel.applyShopInfo.value!!
                )
            } catch (e: Exception) {
                showToast("失败了")
            }
        })

        //下载承诺函
        viewModel.authorpic.observe(this, Observer {

            DialogUtils.showDialog(context, "承诺函下载", "是否确认下载承诺函？", "取消", "下载", null,
                View.OnClickListener {
                    lifecycleScope.launch(Dispatchers.IO)
                    {
                        withContext(Dispatchers.Main) {
                            showDialogLoading()
                        }

                        //获取InputStream
                        val call =
                            CommonRepository.download("https://c-shop-prod.oss-cn-hangzhou.aliyuncs.com/%E7%AD%BE%E7%BA%A6%E6%89%BF%E8%AF%BA%E5%87%BD.doc")

                        //目标文件
                        var externalFileRootDir: File? = getExternalFilesDir(null)
                        do {
                            externalFileRootDir =
                                Objects.requireNonNull(externalFileRootDir)?.parentFile
                        } while (Objects.requireNonNull(externalFileRootDir)?.absolutePath?.contains(
                                "/Android"
                            ) == true
                        )
                        val saveDir: String? =
                            Objects.requireNonNull(externalFileRootDir)?.absolutePath
                        val savePath = saveDir + "/" + Environment.DIRECTORY_DOWNLOADS

                        val destinationFile = File(savePath, "签约承诺函.doc")

                        var inputStream: InputStream? = null
                        var outputStream: OutputStream? = null

                        val data = ByteArray(2048)
                        var count: Int?


                        count = inputStream?.read(data)

                        try {
                            inputStream = call?.body()?.byteStream()
                            outputStream = FileOutputStream(destinationFile)

                            while (count != -1) {
                                if (count != null) {
                                    outputStream.write(data, 0, count)
                                }
                                count = inputStream?.read(data)
                            }

                            outputStream.flush()


                        } catch (e: Exception) {

                            withContext(Dispatchers.Main)
                            {
                                hideDialogLoading()
                                ToastUtils.showShort("下载失败")
                            }
                        } finally {
                            inputStream?.close()
                            outputStream?.close()
                            withContext(Dispatchers.Main)
                            {
                                hideDialogLoading()
                                DialogUtils.showDialog(
                                    context,
                                    "下载成功",
                                    "是否在文件夹中查看承诺函?",
                                    "取消",
                                    "查看",
                                    null,
                                    View.OnClickListener {
                                        //查看承诺函
                                        val intent = Intent(Intent.ACTION_OPEN_DOCUMENT)
                                        intent.type = "application/msword"
                                        intent.addCategory(Intent.CATEGORY_OPENABLE)
                                        startActivity(intent)
                                    })
                            }
                        }


                    }
                }
            )


        })

    }


    //OCR识别营业执照
    override fun onUpdateLicense(data: License) {
        viewModel.applyShopInfo.value?.also { info ->

            if (!data.name.isNullOrEmpty()) {
                info.companyName = data.name
                viewModel.companyAccount.value?.openAccountName = data.name
            }
            if (!data.regNum.isNullOrEmpty()) info.licenseNum = data.regNum
            if (!data.business.isNullOrEmpty()) info.scope = data.business
            try {
                //判断注册资金
                if (!data.capital.isNullOrEmpty()) {
                    val index: Int = data.capital?.indexOfFirst { it == '.' } ?: 1
                    val temp: String = data.capital?.substring(0, index) ?: ""
                    val result: Int = Pattern.compile("[^0-9]").matcher(temp).replaceAll("").toInt()
                    info.regMoney = when {
                        result <= 10 -> 1
                        result <= 20 -> 2
                        result <= 50 -> 3
                        result <= 100 -> 4
                        else -> 5
                    }
                }

                if (!data.period.isNullOrEmpty()) {
                    val time: String? = data.period?.run {
                        substring(12, 16) + "." + substring(17, 19) + "." + substring(20, 22)
                    }
                    val s: Long? = time?.let { timeString ->
                        dateTime3Date("$timeString 00:00:00")?.time ?: 0
                    }
                    s?.also { timeLong ->
                        info.licenceEnd = timeLong / 1000
                    }
                }
            } catch (e: Exception) {

            }
        }

    }

    //OCR识别身份证国徽面
    override fun onIDCardG(data: IDCard) {
        viewModel.applyShopInfo.value?.also {
            try {
                val s = dateTime3Date(data.ValidDate?.substring(11, 21) + " 00:00:00")?.time ?: 0
                it.legalIDExpire = s / 1000
            } catch (e: Exception) {

            }
        }
    }

    //OCR识别身份证人像面
    override fun onIDCardP(data: IDCard) {
        viewModel.applyShopInfo.value?.also {
            if (!data.Name.isNullOrEmpty()) {
                it.legalName = data.Name
                viewModel.personalAccount.value?.openAccountName = data.Name
            }
            if (!data.Sex.isNullOrEmpty()) it.legalSex = if (data.Sex == "男") 1 else 0
            if (!data.Address.isNullOrEmpty()) it.legal_address = data.Address
            if (!data.IdNum.isNullOrEmpty()) it.legalId = data.IdNum
        }
    }

    //OCR识别银行卡
    override fun onBankCard(data: BankCard) {
        viewModel.personalAccount.value?.also {
            try {


                if (data.CardNo != null) {
                    //识别成功

                    if (it.cardNo != data.CardNo) {
                        //卡号不一样
                        it.cardNo = data.CardNo
                        viewModel.ocrPBankVisibility.value = 1
                    }
                    //卡号一样处理无意义
                } else {
                    //识别失败
                    it.cardNo = null
                    it.province = null
                    it.city = null
                    it.bankCode = null
                    it.bankName = null
                    it.subBankName = null
                    it.subBankCode = null
                    viewModel.ocrPBankVisibilityU.value = 1
                }


            } catch (e: Exception) {

            }
        }
    }


    //0绑定失败   1绑定成功
    override fun bindAccountYes() {
        viewModel.bindResult.value = 1
    }

    override fun bindAccountNO() {
        viewModel.bindResult.value = 0
        showToast("您的银行账户已存在,请重试")
    }

    //申请店铺成功
    override fun onApplyShopInfoSuccess() {
        hideDialogLoading()
        showToast("申请成功")
        //  EventBus.getDefault().post(ApplyShopInfoEvent())
        UserManager.applyShopInfoOut()
        UserManager.companyAccountOut()
        UserManager.personalAccountOut()
        finish()
    }

    //申请店铺失败
    override fun onApplyShopInfoError(code: String?) {
        code?.also {
            if (it.isNotBlank()) {
                try {
                    val errorResp = GsonUtils.fromJson(it, BaseResponse::class.java)
                    if (errorResp != null) {
                        showToast(errorResp.message.check())
                    }
                } catch (e: Exception) {
                    // 防止部分接口返回 非常规格式 的字符串，如html
                }
            }
        }
    }


    //更新所属银行信息
    override fun updateCompanyBank(name: String?, code: String?) {
        if (name != null && code != null) {
            //获取数据，更新数据
            viewModel.companyAccount.value?.also {
                it.bankName = name
                it.bankCode = code
            }
        }
        //根据数据获取情况更新页面
        viewModel.ocrCBankVisibilityU.value = 1

    }

    //更新所属银行信息
    override fun updatePersonalBank(name: String?, code: String?) {

        if (name != null && code != null) {
            //获取数据，更新数据
            viewModel.personalAccount.value?.also {
                it.bankName = name
                it.bankCode = code
            }
        }
        //根据数据获取情况更新页面
        viewModel.ocrPBankVisibilityU.value = 1
    }


    //申请失败后成功获取已上传数据
    override fun onShopInfoSuccess(applyShopInfo: ApplyShopInfo) {
        //把ApplyShopInfo保存在ViewModel中
        viewModel.onShopInfoSuccess(applyShopInfo)

        //获取邀请码,以首页填写的为准
        viewModel.applyShopInfo.value?.promoCode =
            if (UserManager.getPromCode().isEmpty()) null
            else UserManager.getPromCode()

        viewModel.thrcertflag.value = viewModel.applyShopInfo.value?.thrcertflag
        viewModel.shopType.value = viewModel.applyShopInfo.value?.shopType

        //获取其他资质图片
        try {
            val list = viewModel.applyShopInfo.value?.other_certificates_imgs?.split(",")
            list?.also {
                if (it.size == 1) {
                    viewModel.applyShopInfo.value?.other_Pic_One = list[0]
                }
                if (it.size == 2) {
                    viewModel.applyShopInfo.value?.other_Pic_One = list[0]
                    viewModel.applyShopInfo.value?.other_Pic_Two = list[1]
                }
            }
        } catch (e: Exception) {

        }

    }

    //申请失败后未获取已上传数据
    override fun onShopInfoError(code: Int) {
        //默认使用空数据
    }


    override fun updateBankList(
        company: BindBankCardDTO?,
        personal: BindBankCardDTO?
    ) {
        company?.also {
            viewModel.companyAccount.value = it
            // 1表示为结算账户
            if (it.isDefault == 1) {
                viewModel.whichAccountToUse.value = 0
            }
        }
        personal?.also {
            viewModel.personalAccount.value = it
            // 1表示为结算账户
            if (it.isDefault == 1) {
                viewModel.whichAccountToUse.value = 1
            }
        }
    }

}