package com.lingmiao.shop.business.me.presenter.impl

import android.content.Context
import com.lingmiao.shop.business.me.api.MeRepository
import com.lingmiao.shop.business.me.bean.Feedback
import  com.lingmiao.shop.business.me.presenter.FeedbackPresenter
import com.james.common.base.BasePreImpl
import kotlinx.coroutines.launch

class FeedbackPresenterImpl(context: Context, private var view: FeedbackPresenter.View) :
    BasePreImpl(view), FeedbackPresenter{
	override fun requestFeedbackData(type: Int, content: String) {
				mCoroutine.launch {
					val resp = MeRepository.apiService.feedback(Feedback(type,content))
					if (resp.isSuccessful) {
						view.onFeedbackSuccess()
					}else{
						view.onFeedbackError()
					}

				}
	}
}