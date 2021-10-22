package com.lingmiao.shop.business.me.presenter.impl

import android.util.Log
import com.james.common.base.BasePreImpl
import com.james.common.netcore.networking.http.core.awaitHiResponse
import com.lingmiao.shop.business.main.api.MainRepository
import com.lingmiao.shop.business.main.bean.IdBean
import com.lingmiao.shop.business.me.bean.RechargeReqVo
import com.lingmiao.shop.business.me.presenter.ApplyVipPresenter
import com.lingmiao.shop.business.wallet.bean.WalletVo
import com.lingmiao.shop.business.wallet.presenter.MyWalletPresenter
import com.lingmiao.shop.business.wallet.presenter.impl.MyWalletPresenterImpl
import kotlinx.coroutines.launch


class ApplyVipPreImpl(private var view: ApplyVipPresenter.View) : BasePreImpl(view),
    ApplyVipPresenter {

    var mDepositMoney : Double?= 0.0

    private val mWallet: MyWalletPresenter by lazy { MyWalletPresenterImpl(view) }

    override fun getVipPriceList() {
        mCoroutine.launch {
            val resp = MainRepository.apiService.getVipList().awaitHiResponse()
            handleResponse(resp) {
                view.onSetVipPriceList(resp.data?.data?.shopProductList)
                mDepositMoney = resp.data?.data?.depositMoney?:0.0
            }
        }
    }

    override fun getIdentity() {
        mCoroutine.launch {
            val identity = MainRepository.apiService.getShopIdentity().awaitHiResponse()
            handleResponse(identity) {
                view.onSetVipInfo(identity.data?.data)
            }
        }
    }

    override fun apply(id: String) {
        mCoroutine.launch {
            view.showDialogLoading()
            val identity = MainRepository.apiService.getPayInfo(id).awaitHiResponse()
            handleResponse(identity) {
                view.onApplySuccess(identity.data)
            }
            view.hideDialogLoading()
        }
    }

    override fun depositApply(mWallet: WalletVo?) {
        mCoroutine.launch {
            view.showDialogLoading()
           // val recharge = RechargeReqVo(mWallet?.depositAccount?.id, mDepositMoney, RechargeReqVo.PAY_TRADE_CHANNEL_OF_WECHAT)
            val recharge = RechargeReqVo(mWallet?.depositAccount?.id, 0.01, RechargeReqVo.PAY_TRADE_CHANNEL_OF_WECHAT)
            val resp = MainRepository.apiService.recharge(recharge).awaitHiResponse()
            handleResponse(resp) {
                view.onDepositApplied(resp.data?.data)
            }
            view.hideDialogLoading()
        }
    }

    override fun ensureRefund(id: String) {
        mCoroutine.launch {
            view.showDialogLoading()
            val item = IdBean()
            item.id = id
            val identity = MainRepository.apiService.ensureRefund(item).awaitHiResponse()
            view.showToast(identity.msg)
            if(identity.isSuccess) {
                view.onRefundEnsured()
            } else {
                view.onRefundEnsureFail()
            }
            view.hideDialogLoading()
        }
    }

    /**
     * 加载钱包数据
     */
    override fun loadWalletData() {
        view.showDialogLoading()
        mWallet.loadWalletData()
    }

}