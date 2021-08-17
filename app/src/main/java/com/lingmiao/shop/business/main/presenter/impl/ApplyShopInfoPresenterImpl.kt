package com.lingmiao.shop.business.main.presenter.impl

import android.content.Context
import android.util.Log
import com.james.common.base.BasePreImpl
import com.james.common.netcore.networking.http.core.awaitHiResponse
import com.lingmiao.shop.base.UserManager
import com.lingmiao.shop.business.main.api.MainRepository
import com.lingmiao.shop.business.main.bean.*
import com.lingmiao.shop.business.main.presenter.ApplyShopInfoPresenter
import com.lingmiao.shop.util.OtherUtils
import kotlinx.coroutines.launch

class ApplyShopInfoPresenterImpl(context: Context, private var view: ApplyShopInfoPresenter.View) :
    BasePreImpl(view), ApplyShopInfoPresenter {

    //如果申请失败，加载已填写数据
    override fun requestShopInfoData() {
        mCoroutine.launch {
            val resp = MainRepository.apiService.getShop().awaitHiResponse()
            if (resp.isSuccess) {
                view.onShopInfoSuccess(resp.data)
            } else {
                view.onShopInfoError(resp.code)
            }
        }
    }

    //提交店铺申请
    override fun requestApplyShopInfoData(applyShopInfo: ApplyShopInfo) {
        mCoroutine.launch {
            val resp = MainRepository.apiService.applyShopInfo(applyShopInfo).awaitHiResponse()
            if (resp.isSuccess) {
                val loginInfo = UserManager.getLoginInfo()
                loginInfo?.let {
                    it.shopName = applyShopInfo.shopName
                    if (resp.data.shopId != null && resp.data.shopId != 0) {
                        it.shopId = resp.data.shopId
                    }
                    UserManager.setLoginInfo(it)
                }
                OtherUtils.setJpushAlias()
                view.onApplyShopInfoSuccess()
            } else {
                view.onApplyShopInfoError(resp.code)
            }

        }
    }

    //OCR
    override fun searchOCR(type: Int, url: String) {

        // 1身份证人像面   2身份证国徽面   6银行卡      8 营业执照
        mCoroutine.launch {
            val data = OCRDiscern()
            data.discernType = type
            data.imageURL = url

            when (type) {
                1 -> {
                    val resp = MainRepository.apiService.readOCRIDCardR(data).awaitHiResponse()
                    if (resp.isSuccess) {
                        resp.data.data?.let { view.onIDCardP(it) }
                    }
                }
                2 -> {

                    val resp = MainRepository.apiService.readOCRIDCardG(data).awaitHiResponse()
                    if (resp.isSuccess) {
                        resp.data.data?.let { view.onIDCardG(it) }
                    }
                }
                6 -> {

                    val resp = MainRepository.apiService.readOCRBankCard(data).awaitHiResponse()
                    if (resp.isSuccess) {
                        resp.data.data?.let { view.onBankCard(it) }
                    }
                }
                8 -> {
                    val resp = MainRepository.apiService.readOCRLicense(data).awaitHiResponse()
                    if (resp.isSuccess) {
                        resp.data.data?.let { view.onUpdateLicense(it) }
                    }

                }

            }
        }
    }


    //绑定银行卡
    override fun bindBankCard(company: BindBankCardDTO?, personal: BindBankCardDTO?) {

        mCoroutine.launch {
            company?.also {
                MainRepository.apiService.bindTestBankCard(it).awaitHiResponse()
            }
            personal?.also {
                MainRepository.apiService.bindTestBankCard(it).awaitHiResponse()
            }

        }
    }


    //查询银行卡
    override fun searchBankList(memberId: Int): List<BindBankCardDTO> {

        mCoroutine.launch {

            var company: BindBankCardDTO? = null
            var personal: BindBankCardDTO? = null

            val resp =
                MainRepository.apiService.queryTestBankCard(memberId).awaitHiResponse()

            //2021-08-17 15:37:56.567 3955-4251/com.lingmiao.shop D/OkHttp:
            //{"success":true,"code":"0","message":"查询成功","data":
            // [{"id":"1354037fae8c5c9ec51cdb54fd1c17af","member_id":"abc44bf2f2ab042805c359a9a5cda6d6","account_id":null,"mobile":"18359181698","bank_name":"建设银行","sub_bank_name":"中国建设银行股份有限公司株洲市分行","card_no":"64346446463734664","open_account_name":"王晓佳","bank_card_type":1,"bank_province":null,"bank_city":null,"bank_province_id":null,"bank_city_id":null,"is_default":0,"card_status":1,"bank_urls":"https://c-shop-test.oss-cn-hangzhou.aliyuncs.com/goodsshop/D35F14B6D3E644DFAD1558814DE58DEC.jpeg","sub_bank_code":"105552009166"},{"id":"5888c3b180ae609868960120ad17495b","member_id":"abc44bf2f2ab042805c359a9a5cda6d6","account_id":null,"mobile":"18359181698","bank_name":"中国银行","sub_bank_name":"中国银行股份有限公司江门骏景支行","card_no":"12131313","open_account_name":"汪振宇","bank_card_type":1,"bank_province":null,"bank_city":null,"bank_province_id":null,"bank_city_id":null,"is_default":1,"card_status":1,"bank_urls":"https://c-shop-test.oss-cn-hangzhou.aliyuncs.com/goodsshop/8A592BC915F94D85AE3B59DEE0D2D31A.jpeg","sub_bank_code":"104589037249"},{"id":"d5dadbafa4da0e11fb29b9f0d6b1c546","member_id":"abc44bf2f2ab042805c359a9a5cda6d6","account_id":null,"mobile":"18359181698","bank_name":"交通银行","sub_bank_name":"交通银行成都市金牛支行","card_no":"6434334646","open_account_name":"22222","bank_card_type":1,"bank_province":null,"bank_city":null,"bank_province_id":null,"bank_city_id":null,"is_default":0,"card_status":1,"bank_urls":"https://c-shop-test.oss-cn-hangzhou.aliyuncs.com/goodsshop/C849037A54D6495A89286DD14C1F2BB2.png","sub_bank_code":"301651000058"},{"id":"ee1369dd07b5d1a91bd1935f37f313d1","member_id":"abc44bf2f2ab042805c359a9a5cda6d6","account_id":null,"mobile":"18359181698","bank_name":"国家开发银行","sub_bank_name":"国家开发银行股份有限公司北京市分行","card_no":"64643494916643344","open_account_name":"王奕","bank_card_type":1,"bank_province":null,"bank_city":null,"bank_province_id":null,"bank_city_id":null,"is_default":0,"card_status":1,"bank_urls":"https://c-shop-test.oss-cn-hangzhou.aliyuncs.com/goodsshop/96F785B96BC945A4A21DB8D039B2DFB1.png","sub_bank_code":"201100000025"}]}

            for (i in resp.data.data) {
                Log.d("WZY Name", i.open_account_name)
            }

        }

        return listOf<BindBankCardDTO>()
    }

    override fun searchBankCode(bankName: String): BankDetail {
        mCoroutine.launch {

            val data = ApplyBank()
            data.body = ApplyBank.ApplyBankDetail().also {
                it.bafName = "农业银行"
            }
            val resp = MainRepository.apiService.searchBankCard(data).awaitHiResponse()


            if (resp.isSuccess) {
                Log.d(
                    "WZY ", "查询到" +
                            resp.data.records?.get(0)?.bankCode
                )
            } else {
                Log.d(
                    "WZY", "查询到" +
                            "失败了"
                )
            }
        }

        return BankDetail()
    }

    //查询银行卡所属银行编码


}