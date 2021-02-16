package com.lingmiao.shop.business.main.presenter.impl
import com.james.common.base.BasePresenter
import com.james.common.base.BaseView
import com.lingmiao.shop.business.main.bean.MessageCenter

import android.content.Context
import com.lingmiao.shop.business.goods.api.GoodsRepository
import  com.lingmiao.shop.business.main.presenter.MessageCenterPresenter
import com.james.common.base.BasePreImpl
import com.lingmiao.shop.business.main.api.MainRepository
import com.james.common.netcore.networking.http.core.awaitHiResponse
import com.james.common.utils.exts.isNotEmpty
import kotlinx.coroutines.launch

class MessageCenterPresenterImpl(context: Context, private var view: MessageCenterPresenter.View) :
    BasePreImpl(view), MessageCenterPresenter{
	override fun requestMessageCenterData(pageIndex: Int, pageSize: Int) {
		mCoroutine.launch {
			val resp = MainRepository.apiService.getMessageCenterList(pageIndex,pageSize).awaitHiResponse()
			if (resp.isSuccess) {
				view.onLoadMoreSuccess(resp.data.data, resp.data.data.isNotEmpty())
			}
		}
	}

}