package com.lingmiao.shop.business.main

import android.app.Activity
import android.content.Intent
import android.util.Log
import androidx.activity.viewModels
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import com.james.common.base.BaseActivity
import com.lingmiao.shop.R
import com.lingmiao.shop.base.UserManager
import com.lingmiao.shop.business.main.bean.*
import com.lingmiao.shop.business.main.presenter.ApplyShopInfoPresenter
import com.lingmiao.shop.business.main.presenter.impl.ApplyShopInfoPresenterImpl
import com.lingmiao.shop.util.dateTime3Date

/**
 *   首页-提交资料
 */
class ApplyShopInfoActivity : BaseActivity<ApplyShopInfoPresenter>(), ApplyShopInfoPresenter.View {

    companion object {

        //营业执照
        const val LICENSE = 2

        //店铺门头照片
        const val SHOP_FRONT = 3

        //店铺内景照片
        const val SHOP_INSIDE = 4

        //上传国徽面照片
        const val ID_CARD_FRONT = 5

        //上传人像面照片
        const val ID_CARD_BACK = 6

        //上传手持身份证照片
        const val ID_CARD_HAND = 7

        //税务登记证照片
        const val TAX_CODE_PIC = 8

        //跳转传图Activity
        fun goUploadImageActivity(
            activity: Activity,
            type: Int,
            title: String,
            imageUrl: String? = null
        ) {
            val intent = Intent(activity, ApplyShopUploadActivity::class.java)
            intent.putExtra("type", type)
            intent.putExtra("title", title)
            intent.putExtra("imageUrl", imageUrl)
            activity.startActivity(intent)
        }

    }

    private val viewModel by viewModels<ApplyShopInfoViewModel>()

    override fun getLayoutId(): Int {
        return R.layout.main_activity_apply_shop_info
    }

    override fun createPresenter(): ApplyShopInfoPresenter {
        return ApplyShopInfoPresenterImpl(this, this)
    }

    override fun useLightMode(): Boolean {
        return false
    }

    override fun initView() {

        //观察调用
        initObserver()

        val loginInfo = UserManager.getLoginInfo()
        //审核失败，获取信息,暂时不处理银行卡
        loginInfo?.let {
            if (it.shopStatus != null && it.shopStatus != "UN_APPLY") mPresenter.requestShopInfoData()
        }

    }

    private fun initObserver() {

        //修改页面title
        viewModel.title.observe(this, Observer {
            mToolBarDelegate.setMidTitle(it)
        })


        viewModel.go.observe(this, Observer {
            try {
                mPresenter?.requestApplyShopInfoData(viewModel.applyShopInfo.value!!)
            } catch (e: Exception) {
                showToast("失败了")
            }
        })

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
                    viewModel.applyShopInfo.value?.bankUrls
                }
                else -> {
                    null
                }
            }
            url?.let { it_url ->
                mPresenter.searchOCR(it, it_url)
            }
        })

    }


    //申请失败后成功获取已上传数据
    override fun onShopInfoSuccess(applyShopInfo: ApplyShopInfo) {
        viewModel.onShopInfoSuccess(applyShopInfo)
    }

    //申请失败后未获取已上传数据
    override fun onShopInfoError(code: Int) {
        //默认使用空数据
    }


    //申请店铺成功
    override fun onApplyShopInfoSuccess() {
        hideDialogLoading()
        showToast("申请成功")
        //  EventBus.getDefault().post(ApplyShopInfoEvent())
        finish()
    }

    //申请店铺失败
    override fun onApplyShopInfoError(code: Int) {
        showToast("请填写正确的邀请码并重新填写数据")
        hideDialogLoading()
    }


    //OCR识别银行卡
    override fun onBankCard(data: BankCard) {
        viewModel.applyShopInfo.value?.also {
            Log.d("WZY", data.toString())
        }
    }


    //OCR识别身份证国徽面
    override fun onIDCardG(data: IDCard) {
        viewModel.applyShopInfo.value?.also {
            val s = dateTime3Date(data.ValidDate?.substring(11, 21) + " 00:00:00")?.time ?: 0
            it.legalIDExpire = s / 1000
        }
    }

    //OCR识别身份证人像面
    override fun onIDCardP(data: IDCard) {
        viewModel.applyShopInfo.value?.also {
            it.legalName = data.Name ?: ""
            it.legalSex = if (data.Sex == "男") 1 else 0
            it.legalId = data.IdNum ?: ""
        }
    }

    //OCR识别营业执照
    override fun onUpdateLicense(data: License) {
        viewModel.applyShopInfo.value?.also {
            it.scope = data.business ?: ""
            it.licenseNum = data.regNum ?: ""

            val time: String? = data.period?.run {
                substring(12, 16) + "." + substring(17, 19) + "." + substring(20, 22)
            }
            val s: Long? = time?.let { timeString ->
                dateTime3Date("$timeString 00:00:00")?.time ?: 0
            }
            s?.also { timeLong ->
                it.licenseEnd = timeLong / 1000
            }

        }

    }


    class ApplyShopInfoViewModel : ViewModel() {


        //-----------------------------------------------------------------------------------------------

        //店铺经营类型
        private val _selectedCategoryListLiveData: MutableLiveData<List<ApplyShopCategory>> =
            MutableLiveData()

        val selectedCategoryListLiveData: LiveData<List<ApplyShopCategory>>
            get() = _selectedCategoryListLiveData

        //更改主营类目
        fun setCategory(list: List<ApplyShopCategory>) {
            _selectedCategoryListLiveData.value = list
        }

        //-----------------------------------------------------------------------------------------------

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

        //-----------------------------------------------------------------------------------------------

        //店铺地址
        private val _adInfo: MutableLiveData<AddressData> = MutableLiveData()

        val adInfo: LiveData<AddressData>
            get() = _adInfo

        //更改店铺地址
        fun setAddress(data: AddressData) {
            _adInfo.value = data
        }

        //-----------------------------------------------------------------------------------------------

        //title
        private val _title: MutableLiveData<String> = MutableLiveData()

        val title: LiveData<String>
            get() = _title

        //更改Title
        fun setTitle(title: String) {
            _title.value = title
        }


        //-----------------------------------------------------------------------------------------------

        //标记是否申请店铺
        val go: MutableLiveData<Int> = MutableLiveData()


        //调用OCR识别
        //1身份证人像面   2身份证国徽面   6银行卡      8 营业执照
        val getOCR: MutableLiveData<Int> = MutableLiveData()

        //-----------------------------------------------------------------------------------------------

        //绑定银行卡
        //对公账户
        val companyAccount = BindBankCardDTO().also {
            it.bankCardType = 1
        }

        //对私账户
        val personalAccount = BindBankCardDTO().also {
            it.bankCardType = 0
        }


    }


}