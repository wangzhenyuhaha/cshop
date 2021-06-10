package com.lingmiao.shop.business.me.presenter

import com.james.common.base.BasePresenter
import com.james.common.base.BaseView
import com.lingmiao.shop.business.goods.api.bean.WorkTimeVo
import com.lingmiao.shop.business.main.bean.ApplyShopInfo
import com.lingmiao.shop.business.me.bean.ShopManageRequest
import com.lingmiao.shop.business.tools.bean.FreightVoItem

/**
Create Date : 2021/3/24:07 PM
Auther      : Fox
Desc        :
 **/
interface ShopOperateSettingPresenter : BasePresenter {

    fun showWorkTimePop(target: android.view.View);

    fun setSetting(data : ApplyShopInfo);

    fun loadShopSetting();

    fun loadTemplate();

    interface View : BaseView {

        fun onUpdateWorkTime(workTimeVo1: WorkTimeVo?, workTimeVo2: WorkTimeVo?);

        fun onSetSetting();

        fun onLoadedShopSetting(vo : ApplyShopInfo);

        fun onLoadedTemplate(tcItem : FreightVoItem?, qsItem : FreightVoItem?);
    }
}