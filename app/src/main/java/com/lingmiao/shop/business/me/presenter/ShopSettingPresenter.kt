package com.lingmiao.shop.business.me.presenter

import com.lingmiao.shop.business.main.bean.ApplyShopInfo

interface ShopSettingPresenter : ShopManagePresenter {

    fun updateShopSlogan(bean: ApplyShopInfo);

    fun updateShopNotice(bean: ApplyShopInfo);

    interface View : ShopManagePresenter.View {

        fun onUpdateSloganSuccess(string: String?);

        fun onUpdateNoticeSuccess(string: String?);
    }

}