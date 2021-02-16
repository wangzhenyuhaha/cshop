package com.lingmiao.shop.business.goods.presenter.impl

import android.content.Context
import com.lingmiao.shop.base.CommonRepository
import com.lingmiao.shop.business.goods.api.bean.GoodsUseExpireVo
import com.lingmiao.shop.business.goods.pop.ExpireListPop
import com.james.common.base.BasePreImpl
import com.james.common.base.BaseView
import kotlinx.coroutines.launch

/**
Create Date : 2021/1/1212:07 PM
Auther      : Fox
Desc        :
 **/
class ExpirePopPreImpl(view: BaseView) : BasePreImpl(view) {

    private var mExpire : ExpireListPop ? = null;

    private var mList : MutableList<GoodsUseExpireVo> = mutableListOf();

    fun showPop(context: Context, value : String?, callback: (GoodsUseExpireVo?) -> Unit) {
        mCoroutine.launch {
            val resp = CommonRepository.queryListByType("expiry_date")
            if (resp.isSuccess) {
                showPopWindow(context, value, resp?.data?.data, callback)
            }
        }
    }

    private fun showPopWindow(
        context: Context,
        value : String?,
        data: List<GoodsUseExpireVo>?,
        callback: (GoodsUseExpireVo?) -> Unit
    ) {
        mExpire = ExpireListPop(context, value, data).apply {
            listener = { item : GoodsUseExpireVo ->
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