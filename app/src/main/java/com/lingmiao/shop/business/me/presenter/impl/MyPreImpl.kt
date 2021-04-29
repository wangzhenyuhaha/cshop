package com.lingmiao.shop.business.me.presenter.impl

import com.lingmiao.shop.business.me.api.MeRepository
import com.lingmiao.shop.business.me.presenter.MyPresenter
import com.james.common.base.BasePreImpl
import com.james.common.netcore.networking.http.core.awaitHiResponse
import com.lingmiao.shop.business.main.api.MainRepository
import com.lingmiao.shop.business.wallet.presenter.MyWalletPresenter
import com.lingmiao.shop.business.wallet.presenter.impl.MyWalletPresenterImpl
import kotlinx.coroutines.launch


class MyPreImpl(private var view: MyPresenter.View) : BasePreImpl(view), MyPresenter {

	private val mWallet: MyWalletPresenter by lazy { MyWalletPresenterImpl(view) }

    override fun getMyData() {
		mCoroutine.launch {
			val resp = MeRepository.apiService.getMyData().awaitHiResponse()
			if (resp.isSuccess) {
				view.onMyDataSuccess(resp.data)
			}else{
				view.ontMyDataError()
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

	/**
	 * 加载钱包数据
	 */
	override fun loadWalletData() {
		mWallet.loadWalletData();
	}

}