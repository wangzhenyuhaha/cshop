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


    //绑定银行卡
    override fun bindBankCard(company: BindBankCardDTO?, personal: BindBankCardDTO?) {

        mCoroutine.launch {


            if (company != null) {
                val resp1 = MainRepository.apiService.bindTestBankCard(company).awaitHiResponse()
                if (resp1.isSuccess) {
                    view.companyYes()
                } else {
                    view.companyNO()
                }

            }

            if (personal != null) {
                val resp2 = MainRepository.apiService.bindTestBankCard(personal).awaitHiResponse()
                if (resp2.isSuccess) {
                    view.personalYes()
                } else {
                    view.personalNO()
                }

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


    //查询银行卡
    override fun searchBankList(memberId: Int) {

        mCoroutine.launch {

            var company: BindBankCardDTO? = null
            var personal: BindBankCardDTO? = null

            val resp =
                MainRepository.apiService.queryTestBankCard(memberId).awaitHiResponse()

        }


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


}