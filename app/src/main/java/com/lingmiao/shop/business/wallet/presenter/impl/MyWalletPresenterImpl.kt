package com.lingmiao.shop.business.wallet.presenter.impl

import com.blankj.utilcode.util.SPUtils
import com.james.common.base.BasePreImpl
import com.lingmiao.shop.business.wallet.api.WalletConstants
import com.lingmiao.shop.business.wallet.api.WalletRepository
import com.lingmiao.shop.business.wallet.presenter.MyWalletPresenter
import kotlinx.coroutines.launch

class MyWalletPresenterImpl(private var view : MyWalletPresenter.View) :
    BasePreImpl(view), MyWalletPresenter {

    override fun loadWalletData() {
        mCoroutine.launch {

            val resp = WalletRepository.getWalletIndexInfo()
            val wechatData = WalletRepository.getWithdrawAccountInfo()

            if(resp.isSuccess) {
                view.onLoadWalletDataSuccess(resp.data?.data)
            } else {
                view.onLoadWalletDataError(resp.code)
            }
            //useless
            if(wechatData.isSuccess) {
                view.onLoadedAccount(wechatData.data)
            }
        }
    }

    override fun loadRiderMoneyInfo() {
        mCoroutine.launch {
            val resp = WalletRepository.getRiderMoney()

            handleResponse(resp) {
                if (resp.isSuccess) {
                    SPUtils.getInstance().put(WalletConstants.RIDER_ACCOUNT_ID, resp.data?.data?.getRiderAccountId())
                    view.loadRiderMoneySuccess(resp.data?.data?.riderDepositAccountVO)
                }
            }
        }
    }


}