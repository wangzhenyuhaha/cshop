package com.lingmiao.shop.business.me.presenter
import com.james.common.base.BasePresenter
import com.james.common.base.BaseView
import com.lingmiao.shop.business.me.bean.Feedback

interface FeedbackPresenter: BasePresenter{
 
	fun requestFeedbackData(type: Int, content: String)
	
	interface View : BaseView {
        fun onFeedbackSuccess()
        fun onFeedbackError()
    }
}