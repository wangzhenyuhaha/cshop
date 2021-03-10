package com.lingmiao.shop.business.me.presenter.impl

import android.content.Context
import com.james.common.base.BasePreImpl
import com.lingmiao.shop.business.me.bean.ShopManageRequest
import com.lingmiao.shop.business.me.presenter.ManagerSettingPresenter
import com.lingmiao.shop.business.me.presenter.ShopBaseSettingPresenter
import com.lingmiao.shop.business.me.presenter.ShopManagePresenter
import com.lingmiao.shop.business.tuan.presenter.OrderIndexPresenter

/**
Create Date : 2021/3/24:08 PM
Auther      : Fox
Desc        :
 **/
class ShopBaseSettingPresenterImpl (context: Context, view : ShopBaseSettingPresenter.View) : BasePreImpl(view) ,ShopBaseSettingPresenter {

    private val shopPresenter: ShopManagePresenter by lazy {
        ShopManagePresenterImpl(context, view);
    }

    override fun requestShopManageData() {
        shopPresenter?.requestShopManageData();
    }

    override fun updateShopManage(bean: ShopManageRequest) {
        shopPresenter?.updateShopManage(bean);
    }

}