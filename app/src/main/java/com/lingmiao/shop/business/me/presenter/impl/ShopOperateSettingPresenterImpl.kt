package com.lingmiao.shop.business.me.presenter.impl

import android.content.Context
import com.james.common.base.BasePreImpl
import com.lingmiao.shop.business.goods.api.bean.WorkTimeVo
import com.lingmiao.shop.business.goods.presenter.impl.ItemPopPreImpl
import com.lingmiao.shop.business.goods.presenter.impl.PopCategoryPreImpl
import com.lingmiao.shop.business.me.presenter.ManagerSettingPresenter
import com.lingmiao.shop.business.me.presenter.ShopOperateSettingPresenter
import com.lingmiao.shop.business.tuan.presenter.OrderIndexPresenter

/**
Create Date : 2021/3/24:08 PM
Auther      : Fox
Desc        :
 **/
class ShopOperateSettingPresenterImpl (val context: Context,var view : ShopOperateSettingPresenter.View) : BasePreImpl(view) ,ShopOperateSettingPresenter {

    private val mItemPreImpl: ItemPopPreImpl by lazy { ItemPopPreImpl(view) }

    override fun showWorkTimePop(target: android.view.View) {
        mItemPreImpl?.showWorkTimePop(target, context,"请选择营业时间", "") { item1 : WorkTimeVo?, item2 : WorkTimeVo? ->
            view?.onUpdateWorkTime(item1, item2);
        }
    }

}