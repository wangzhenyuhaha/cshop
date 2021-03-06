package com.lingmiao.shop.business.me.presenter

import com.james.common.base.BasePresenter
import com.james.common.base.BaseView

/**
Create Date : 2021/3/24:07 PM
Auther      : Fox
Desc        :
 **/
interface WeChatApprovePresenter : BasePresenter {

    fun approve();

    interface View : BaseView {
        fun approved();
    }
}