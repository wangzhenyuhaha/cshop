package com.lingmiao.shop.business.goods.presenter.impl

import android.content.Context
import com.james.common.base.BasePreImpl
import com.lingmiao.shop.base.UserManager
import com.lingmiao.shop.business.goods.api.GoodsRepository
import com.lingmiao.shop.business.goods.presenter.GoodsPublishTypePre
import kotlinx.coroutines.launch

/**
Create Date : 2021/3/69:59 AM
Auther      : Fox
Desc        :
 **/
class GoodsPublishTypePreImpl(var context: Context, var view: GoodsPublishTypePre.View) : BasePreImpl(view),GoodsPublishTypePre {
    override fun loadList() {
        mCoroutine.launch {
            // 一级类目 categoryId=0
            val resp = GoodsRepository.loadUserCategory(PopCategoryPreImpl.LV1_CATEGORY_ID,0)
            if (resp.isSuccess) {
                view?.onListSuccess(resp.data)
            }
            val resp1 = GoodsRepository.loadUserCategory(PopCategoryPreImpl.LV1_CATEGORY_ID, UserManager.getLoginInfo()?.shopId)
            handleResponse(resp1) {
                view?.onSelfListSuccess(resp1.data);
            }
        }
    }

}