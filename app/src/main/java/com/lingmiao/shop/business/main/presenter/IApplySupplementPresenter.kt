package com.lingmiao.shop.business.main.presenter

import com.james.common.base.BasePresenter
import com.james.common.base.BaseView

/**
Create Date : 2021/7/2710:43 上午
Auther      : Fox
Desc        :
 **/
interface IApplySupplementPresenter : BasePresenter {

    fun getSupplementSign();

    interface View : BaseView {
        fun setUrl(url :String?);
        fun getSupplementUrlFailed();
    }
}