package com.lingmiao.shop.business.me.presenter.impl

import android.content.Context
import com.james.common.base.BasePreImpl
import com.james.common.netcore.networking.http.core.awaitHiResponse
import com.lingmiao.shop.business.goods.api.bean.WorkTimeVo
import com.lingmiao.shop.business.goods.presenter.impl.ItemPopPreImpl
import com.lingmiao.shop.business.main.bean.ApplyShopInfo
import com.lingmiao.shop.business.me.api.MeRepository
import com.lingmiao.shop.business.me.presenter.ShopOperateSettingPresenter
import kotlinx.coroutines.launch

/**
Create Date : 2021/3/24:08 PM
Auther      : Fox
Desc        :
 **/
class ShopOperateSettingPresenterImpl (val context: Context,var view : ShopOperateSettingPresenter.View) : BasePreImpl(view) ,ShopOperateSettingPresenter {

    private val mItemPreImpl: ItemPopPreImpl by lazy { ItemPopPreImpl(view) }

    override fun showWorkTimePop(target: android.view.View) {
        mItemPreImpl?.showWorkTimePop(context, "") { item1 : WorkTimeVo?, item2 : WorkTimeVo? ->
            view?.onUpdateWorkTime(item1, item2);
        }
    }

    override fun setSetting(data: ApplyShopInfo) {
        mCoroutine.launch {
            val resp = MeRepository.apiService.updateShop(data).awaitHiResponse()
            if (resp.isSuccess) {
                view?.showToast("保存成功")
                view?.onSetSetting();
            } else {
                view?.showToast("保存失败")
            }
        }
    }

    override fun loadShopSetting() {
        mCoroutine.launch {
            val resp = MeRepository.apiService.getShop().awaitHiResponse()
            handleResponse(resp) {
                view.onLoadedShopSetting(resp.data)
            }
        }
    }

}