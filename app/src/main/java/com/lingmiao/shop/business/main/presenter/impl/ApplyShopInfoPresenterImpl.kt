package com.lingmiao.shop.business.main.presenter.impl

import android.content.Context
import android.util.Log
import com.james.common.base.BasePreImpl
import com.james.common.netcore.networking.http.core.awaitHiResponse
import com.lingmiao.shop.base.UserManager
import com.lingmiao.shop.business.main.api.MainRepository
import com.lingmiao.shop.business.main.bean.ApplyShopInfo
import com.lingmiao.shop.business.main.bean.BindBankCardDTO
import com.lingmiao.shop.business.main.bean.OCRDiscern
import com.lingmiao.shop.business.main.presenter.ApplyShopInfoPresenter
import com.lingmiao.shop.util.OtherUtils
import kotlinx.coroutines.launch

class ApplyShopInfoPresenterImpl(context: Context, private var view: ApplyShopInfoPresenter.View) :
    BasePreImpl(view), ApplyShopInfoPresenter {

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
                        resp.data.data?.let {
                            view.onBankCard(it)
                        }
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

    //查询银行卡名
    override fun searchBankName(type: Int, cardID: String) {
        mCoroutine.launch {

            val resp = MainRepository.apiService.searchBankCardName(cardID).awaitHiResponse()

            if (resp.isSuccess) {
                try {
                    if (type == 1) {
                        //对私
                        if (resp.data.size == 0) {
                            //表示为查询到相关信息
                            //放回null表示查询失败
                            view.updatePersonalBank(null, null)
                        } else {
                            //查询到了银行卡信息
                            //默认将第一个结果使用
                            resp.data[0].also {
                                view.updatePersonalBank(it.bank_name, it.bank_code)
                            }
                        }

                    }
                    if (type == 0) {
                        //对公
                        if (resp.data.size == 0) {
                            //表示为查询到相关信息
                            //放回null表示查询失败
                            view.updateCompanyBank(null, null)
                        } else {
                            //查询到了银行卡信息
                            //默认将第一个结果使用
                            resp.data[0].also {
                                view.updateCompanyBank(it.bank_name, it.bank_code)
                            }
                        }
                    }
                } catch (e: Exception) {

                }
            }
        }
    }


    //绑定银行卡
    override fun bindBankCard(company: BindBankCardDTO?, personal: BindBankCardDTO?) {

        mCoroutine.launch {
            view.showDialogLoading()
            if (company == null && personal != null) {
                //只绑定对公账户
                val resp1 =
                    MainRepository.apiService.bindTestBankCard(arrayOf(personal)).awaitHiResponse()
                if (resp1.isSuccess) {
                    view.bindAccountYes()
                } else {
                    view.hideDialogLoading()
                    view.bindAccountNO()
                }
            } else if (personal == null && company != null) {
                //只绑定对私账户
                val resp2 =
                    MainRepository.apiService.bindTestBankCard(arrayOf(company)).awaitHiResponse()
                if (resp2.isSuccess) {
                    view.bindAccountYes()
                } else {
                    view.hideDialogLoading()
                    view.bindAccountNO()
                }
            } else if (company != null && personal != null) {
                //都需要绑定
                val resp3 =
                    MainRepository.apiService.bindTestBankCard(arrayOf(company, personal))
                        .awaitHiResponse()
                if (resp3.isSuccess) {
                    view.bindAccountYes()
                } else {
                    view.hideDialogLoading()
                    view.bindAccountNO()
                }
            }


        }
    }

    //提交店铺申请
    override fun requestApplyShopInfoData(openOrNot: Boolean, applyShopInfo: ApplyShopInfo) {

        if (openOrNot) {
            //修改进件资料
            Log.d("WZYTesy", "没有接口")
        } else {
            //申请店铺或者重新提交资料
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
                    //失败，hideDialogLoading()，并显示失败原因,hideDialogLoading()在查询银行卡结束后才调用
                    //查询银行卡，以便重新提交
                    UserManager.getLoginInfo()?.uid?.also { uid -> searchBankList(uid) }
                    //显示错误原因
                    view.onApplyShopInfoError(resp.msg)
                }
            }
        }


    }

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


    //查询银行卡
    override fun searchBankList(memberId: Int) {

        mCoroutine.launch {
            val resp =
                MainRepository.apiService.queryTestBankCard(memberId).awaitHiResponse()

            if (resp.isSuccess) {
                //11个字段需要处理
                if (resp.data.data.isNotEmpty()) {
                    for (i in resp.data.data) {

                        if (i.bank_card_type == 0) {
                            //对私
                            val personal = BindBankCardDTO()
                            i.apply {
                                personal.also {
                                    it.id = id
                                    it.openAccountName = open_account_name
                                    it.cardNo = card_no
                                    it.bankCardType = bank_card_type
                                    it.province = province
                                    it.city = city
                                    it.bankCode = bank_code
                                    it.bankName = bank_name
                                    it.subBankName = sub_bank_name
                                    it.subBankCode = sub_bank_code
                                    it.bankUrls = bank_urls
                                    it.isDefault = is_default
                                }
                            }
                            view.updateBankList(null, personal)

                        }
                        if (i.bank_card_type == 1) {
                            //对公
                            val company = BindBankCardDTO()
                            i.apply {
                                company.also {
                                    it.id = id
                                    it.openAccountName = open_account_name
                                    it.cardNo = card_no
                                    it.bankCardType = bank_card_type
                                    it.province = province
                                    it.city = city
                                    it.bankCode = bank_code
                                    it.bankName = bank_name
                                    it.subBankName = sub_bank_name
                                    it.subBankCode = sub_bank_code
                                    it.bankUrls = bank_urls
                                    it.isDefault = is_default
                                }
                            }
                            view.updateBankList(company, null)
                        }


                    }
                }
            }
            view.hideDialogLoading()
        }


    }


}