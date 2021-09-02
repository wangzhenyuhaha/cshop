package com.lingmiao.shop.business.main

import android.util.Log
import android.view.View
import androidx.activity.viewModels
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import com.blankj.utilcode.util.GsonUtils
import com.james.common.base.BaseActivity
import com.james.common.net.BaseResponse
import com.james.common.utils.exts.check
import com.lingmiao.shop.R
import com.lingmiao.shop.base.UserManager
import com.lingmiao.shop.business.main.bean.*
import com.lingmiao.shop.business.main.presenter.ApplyShopInfoPresenter
import com.lingmiao.shop.business.main.presenter.impl.ApplyShopInfoPresenterImpl
import com.lingmiao.shop.util.dateTime3Date
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
                }

                //获取推广码
                viewModel.applyShopInfo.value?.promoCode =
                    if (UserManager.getPromCode().isEmpty()) null
                    else UserManager.getPromCode()

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
                    viewModel.applyShopInfo.value?.legalBackImg
                }
                2 -> {
                    viewModel.applyShopInfo.value?.legalImg
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
                //身份证照片弄反了，国徽人像互换
                val temp = viewModel.applyShopInfo.value?.legalBackImg
                viewModel.applyShopInfo.value?.legalBackImg =
                    viewModel.applyShopInfo.value?.legalImg
                viewModel.applyShopInfo.value?.legalImg = temp
                mPresenter?.requestApplyShopInfoData(
                    viewModel.shopOpenOrNot,
                    viewModel.applyShopInfo.value!!
                )
            } catch (e: Exception) {
                showToast("失败了")
            }
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

    class ApplyShopInfoViewModel : ViewModel() {

        // 店铺是否处于开启中,用于修改进件资料
        var shopOpenOrNot: Boolean = false

        //店铺信息
        private val _applyShopInfo = MutableLiveData<ApplyShopInfo>().also {
            it.value = ApplyShopInfo()
        }

        val applyShopInfo: LiveData<ApplyShopInfo>
            get() = _applyShopInfo

        //成功加载Info
        fun onShopInfoSuccess(applyShopInfo: ApplyShopInfo) {
            _applyShopInfo.value = applyShopInfo
        }

        //初次申请店铺  true表示为第一次申请店铺
        var firstApplyShop: Boolean = true

        //页面的标题
        private val _title: MutableLiveData<String> = MutableLiveData()

        val title: LiveData<String>
            get() = _title

        //更改Title
        fun setTitle(title: String) {
            _title.value = title
        }


        //保存的对公对私账户
        //对公账户
        val companyAccount: MutableLiveData<BindBankCardDTO> =
            MutableLiveData<BindBankCardDTO>().also {
                val temp = BindBankCardDTO()
                temp.bankCardType = 1
                it.value = temp
            }

        //对私账户
        val personalAccount: MutableLiveData<BindBankCardDTO> =
            MutableLiveData<BindBankCardDTO>().also {
                val temp = BindBankCardDTO()
                temp.bankCardType = 0
                it.value = temp
            }


        //OCR识别的调用
        //调用OCR识别
        //1身份证人像面   2身份证国徽面   6银行卡      8 营业执照
        //不能给初始值
        val getOCR: MutableLiveData<Int> = MutableLiveData()


        //店铺经营类型
        private val _selectedCategoryListLiveData: MutableLiveData<List<ApplyShopCategory>> =
            MutableLiveData()

        val selectedCategoryListLiveData: LiveData<List<ApplyShopCategory>>
            get() = _selectedCategoryListLiveData

        //更改主营类目
        fun setCategory(list: List<ApplyShopCategory>) {
            _selectedCategoryListLiveData.value = list
        }


        //店铺地址
        private val _adInfo: MutableLiveData<AddressData> = MutableLiveData()

        val adInfo: LiveData<AddressData>
            get() = _adInfo

        //更改店铺地址
        fun setAddress(data: AddressData) {
            _adInfo.value = data
        }


        //绑定银行卡页面模块的可见性以及结算账户
        //对公账户
        val companyModule: MutableLiveData<Int> = MutableLiveData<Int>().also {
            it.value = View.VISIBLE
        }

        //对私账户
        val personalModule: MutableLiveData<Int> = MutableLiveData<Int>().also {
            it.value = View.GONE
        }

        //表示当前选那个账户作为结算账户
        //0  对公结算  1  对私结算
        val whichAccountToUse: MutableLiveData<Int> = MutableLiveData<Int>().also {
            it.value = 0
        }


        //根据需求获得银行卡号所代表的银行
        //获取对公账户的信息
        val ocrCBankVisibility: MutableLiveData<Int> = MutableLiveData()

        //获取对私账户的信息
        val ocrPBankVisibility: MutableLiveData<Int> = MutableLiveData()


        //更新银行卡号后更新对公UI
        val ocrCBankVisibilityU: MutableLiveData<Int> = MutableLiveData()

        //更新银行卡号后更新对私UI
        val ocrPBankVisibilityU: MutableLiveData<Int> = MutableLiveData()


        //绑定银行卡
        //0 绑定对公账户， 1，绑定对私账户  2绑定对公和对私账户
        val bindBandCard: MutableLiveData<Int> = MutableLiveData()


        //绑定银行卡结果
        // 0 绑定失败  1 绑定成功
        val bindResult: MutableLiveData<Int> = MutableLiveData()


        //申请店铺
        val go: MutableLiveData<Int> = MutableLiveData()

        //当法人和负责人发生变化时，判断是否相等
        val nameOfShopPerson: MutableLiveData<Int> = MutableLiveData<Int>().also {
            it.value = 1
        }

    }

}