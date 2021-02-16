package com.lingmiao.shop.business.goods.presenter.impl

import android.content.Context
import com.lingmiao.shop.business.goods.api.bean.GoodsTypeVO
import com.lingmiao.shop.business.goods.pop.GoodsTypePop
import com.james.common.base.BasePreImpl
import com.james.common.base.BaseView

/**
Create Date : 2021/1/1212:07 PM
Auther      : Fox
Desc        :
 **/
class GoodsTypePopPreImpl(view: BaseView) : BasePreImpl(view) {

    private var mExpire: GoodsTypePop? = null;

    private var mList: MutableList<GoodsTypeVO> = mutableListOf();

    fun showPop(context: Context, value: String?, callback: (GoodsTypeVO?) -> Unit) {
        mList = GoodsTypeVO.getGoodsTypeList();
        showPopWindow(context, value, mList, callback)
    }

    private fun showPopWindow(
        context: Context,
        value: String?,
        data: List<GoodsTypeVO>?,
        callback: (GoodsTypeVO?) -> Unit
    ) {
        mExpire = GoodsTypePop(context, value, data).apply {
            listener = { item: GoodsTypeVO ->
                callback.invoke(item);
            }
        }
        mExpire?.showPopupWindow()
    }

    override fun onDestroy() {
        mList.clear()
        super.onDestroy()
    }

}