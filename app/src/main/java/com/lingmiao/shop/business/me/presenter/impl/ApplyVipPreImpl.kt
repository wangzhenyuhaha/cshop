package com.lingmiao.shop.business.me.presenter.impl

import com.james.common.base.BasePreImpl
import com.james.common.netcore.networking.http.core.awaitHiResponse
import com.lingmiao.shop.business.main.api.MainRepository
import com.lingmiao.shop.business.me.presenter.ApplyVipPresenter
import com.lingmiao.shop.business.wallet.presenter.MyWalletPresenter
import com.lingmiao.shop.business.wallet.presenter.impl.MyWalletPresenterImpl
import kotlinx.coroutines.launch


class ApplyVipPreImpl(private var view: ApplyVipPresenter.View) : BasePreImpl(view),
    ApplyVipPresenter {

    private val mWallet: MyWalletPresenter by lazy { MyWalletPresenterImpl(view) }

    override fun getVipPriceList() {
        mCoroutine.launch {
            val resp = MainRepository.apiService.getVipList().awaitHiResponse()
            handleResponse(resp) {
                view.onSetVipPriceList(resp?.data?.data)
            }
        }
    }

    override fun getIdentity() {
        mCoroutine.launch {
            val identity = MainRepository.apiService.getShopIdentity().awaitHiResponse()
            handleResponse(identity) {
                view.onSetVipInfo(identity?.data?.data);
            }
        }
    }

    override fun apply(id: String) {
        mCoroutine.launch {
            view?.showDialogLoading()
            val identity = MainRepository.apiService.getPayInfo(id).awaitHiResponse()
            handleResponse(identity) {
                //view.onSetVipInfo(identity?.data?.data);
            }
            view?.hideDialogLoading()
        }
    }

    /**
     * 加载钱包数据
     */
    override fun loadWalletData() {
        mWallet.loadWalletData();
    }

}