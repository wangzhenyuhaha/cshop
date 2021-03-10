package com.lingmiao.shop.business.commonpop.presenter

import android.content.Context
import com.james.common.base.BasePresenter
import com.james.common.base.BaseView
import com.lingmiao.shop.business.commonpop.bean.ItemData

/**
Create Date : 2021/3/811:17 AM
Auther      : Fox
Desc        :
 **/
interface ItemPopPresenter<T : ItemData> : BasePresenter {

    fun showItemPop(
        context: Context,
        targetView: android.view.View,
        callback: (T?, T?, T?) -> Unit
    );

    interface View : BaseView {

    }

}