package com.lingmiao.shop.business.me.presenter

import com.lingmiao.shop.business.me.bean.ShopManageRequest

/**
Create Date : 2021/3/24:07 PM
Auther      : Fox
Desc        :
 **/
interface ShopBaseSettingPresenter : ShopManagePresenter {

    fun updateShopSlogan(bean: ShopManageRequest);

    fun updateShopNotice(bean:ShopManageRequest);

    interface View : ShopManagePresenter.View {

        fun onUpdateSloganSuccess(string: String?);

        fun onUpdateNoticeSuccess(string: String?);
    }
}