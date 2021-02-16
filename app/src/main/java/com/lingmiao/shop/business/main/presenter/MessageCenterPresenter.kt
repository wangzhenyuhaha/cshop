package com.lingmiao.shop.business.main.presenter
import com.james.common.base.BasePresenter
import com.james.common.base.BaseView
import com.lingmiao.shop.business.main.bean.MessageCenter
import com.james.common.base.loadmore.BaseLoadMoreView

interface MessageCenterPresenter: BasePresenter{
 
	fun requestMessageCenterData(pageIndex:Int,pageSize:Int)
	
	interface View : BaseView , BaseLoadMoreView<MessageCenter> {
        fun onMessageCenterSuccess(bean: MessageCenter)
        fun onMessageCenterError(code: Int)
    }
}