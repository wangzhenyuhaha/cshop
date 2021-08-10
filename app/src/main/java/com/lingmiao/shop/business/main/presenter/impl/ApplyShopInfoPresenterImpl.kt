package com.lingmiao.shop.business.main.presenter.impl

import com.lingmiao.shop.business.main.bean.ApplyShopInfo

import android.content.Context
import android.util.Log
import com.lingmiao.shop.base.UserManager
import com.lingmiao.shop.business.main.api.MainRepository
import  com.lingmiao.shop.business.main.presenter.ApplyShopInfoPresenter
import com.lingmiao.shop.util.OtherUtils
import com.james.common.base.BasePreImpl
import com.james.common.netcore.networking.http.core.awaitHiResponse
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

    override fun requestApplyShopInfoData(applyShopInfo: ApplyShopInfo) {
        mCoroutine.launch {
            val resp = MainRepository.apiService.applyShopInfo(applyShopInfo).awaitHiResponse()
            Log.d("ABC",resp.msg)
            Log.d("ABC",resp.code.toString())
            Log.d("ABC",resp.data.toString())

            if (resp.isSuccess) {
                Log.d("ABC","Success")
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
            } else {Log.d("ABC","FFF")
                view.onApplyShopInfoError(resp.code)
            }

        }
    }
}