package com.lingmiao.shop.business.me.presenter

import com.james.common.base.BasePresenter
import com.james.common.base.BaseView
import com.lingmiao.shop.business.goods.api.bean.WorkTimeVo

/**
Create Date : 2021/3/24:07 PM
Auther      : Fox
Desc        :
 **/
interface ShopOperateSettingPresenter : BasePresenter {

    fun showWorkTimePop(target: android.view.View);

    interface View : BaseView {
        fun onUpdateWorkTime(workTimeVo1: WorkTimeVo?, workTimeVo2: WorkTimeVo?);
    }
}