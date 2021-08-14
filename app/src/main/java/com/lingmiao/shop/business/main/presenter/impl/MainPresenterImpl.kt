package com.lingmiao.shop.business.main.presenter.impl

import android.content.Context
import android.util.Log
import com.lingmiao.shop.BuildConfig
import com.lingmiao.shop.base.UserManager
import com.lingmiao.shop.business.main.api.MainRepository
import com.lingmiao.shop.business.main.presenter.MainPresenter
import com.lingmiao.shop.business.me.api.MeRepository
import com.james.common.base.BasePreImpl
import com.james.common.netcore.networking.http.core.awaitHiResponse
import com.lingmiao.shop.base.ShopStatusConstants
import com.lingmiao.shop.business.goods.api.GoodsRepository
import com.lingmiao.shop.business.main.bean.OpenShopStatusVo
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class MainPresenterImpl(context: Context, private var view: MainPresenter.View) : BasePreImpl(view),
    MainPresenter {
    override fun requestMainInfoData() {
        mCoroutine.launch {
            val shopStatusResp = MainRepository.apiService.getShopStatus().awaitHiResponse()
            if (shopStatusResp.isSuccess) {
                val loginInfo = UserManager.getLoginInfo()
                if (loginInfo != null) {
                    loginInfo.shopStatus = shopStatusResp.data.shopStatus
                    loginInfo.statusReason = shopStatusResp.data.statusReason
                    loginInfo.openStatus = shopStatusResp.data.openStatus == 1
                    UserManager.setLoginInfo(loginInfo)
                }
                if (shopStatusResp.data.shopStatus == ShopStatusConstants.OPEN
                    ||shopStatusResp.data.shopStatus == ShopStatusConstants.ALLINPAY_APPLYING
                    ||shopStatusResp.data.shopStatus == ShopStatusConstants.ALLINPAY_APPROVED
                    ||shopStatusResp.data.shopStatus == ShopStatusConstants.ALLINPAY_ELECTSIGN_REFUSED
                    ||shopStatusResp.data.shopStatus == ShopStatusConstants.ALLINPAY_ELECTSIGN_APPROVED) {
                    val resp = MainRepository.apiService.getMainData().awaitHiResponse()
                    if (resp.isSuccess) {
                        view.onMainDataSuccess(resp.data)
                    } else {
                        view.onMainInfoError(resp.code)
                    }
                } else {
                    view.onMainDataSuccess(null)
                }
            } else {
                view.onMainInfoError(-1)
            }


        }
    }

    override fun requestAccountSettingData() {
        mCoroutine.launch {
            val resp = MainRepository.apiService.getUpgrade(
                BuildConfig.VERSION_NAME,
                BuildConfig.VERSION_CODE.toString()
            ).awaitHiResponse()
            if (resp.isSuccess) {
                view.onAccountSettingSuccess(resp.data)
            } else {
                view.onAccountSettingError(resp.code)
            }

        }
    }

    override fun editShopStatus(flag: Int) {
        val loginInfo = UserManager.getLoginInfo();
        val id = loginInfo?.shopId;
        if (id != null) {
            mCoroutine.launch {
                val body = OpenShopStatusVo();
//				body.openStatus = flag;

                val resp = MainRepository.apiService.editShopStatus(flag, body).awaitHiResponse()
                handleResponse(resp) {
                    loginInfo.openStatus = flag == 1;
                    UserManager.setLoginInfo(loginInfo)
                    view.onShopStatusEdited()
                }
            }
        }
    }

    //加载库存预警数量
    //获取当前预警商品的数量
    override fun getWaringNumber() {

        mCoroutine.launch {
            val resp =
                GoodsRepository.loadWarningGoodsList(1)
            if (resp.isSuccess) {
                view.onWarningNumber(resp.data.dataTotal)
            } else {
                handleErrorMsg(resp.msg)
            }
        }
    }

}
