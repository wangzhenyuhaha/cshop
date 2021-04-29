package com.lingmiao.shop.business.goods.presenter.impl

import android.content.Context
import com.james.common.base.BasePreImpl
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
            val resp = GoodsRepository.loadCategory(PopCategoryPreImpl.LV1_CATEGORY_ID)
            if (resp.isSuccess) {
                view?.onListSuccess(resp.data)
            }
        }
    }

}