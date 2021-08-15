package com.lingmiao.shop.business.main.presenter.impl

import android.content.Context
import com.james.common.base.BasePreImpl
import com.james.common.netcore.networking.http.core.awaitHiResponse
import com.lingmiao.shop.base.UserManager
import com.lingmiao.shop.business.main.api.MainRepository
import com.lingmiao.shop.business.main.bean.ApplyShopInfo
import com.lingmiao.shop.business.main.bean.OCRDiscern
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

    //查询银行卡

    //修改银行卡


}