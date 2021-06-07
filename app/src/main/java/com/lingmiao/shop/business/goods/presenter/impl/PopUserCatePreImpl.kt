package com.lingmiao.shop.business.goods.presenter.impl

import android.content.Context
import com.lingmiao.shop.business.goods.api.GoodsRepository
import com.james.common.base.BasePreImpl
import com.james.common.base.BaseView
import com.lingmiao.shop.base.UserManager
import com.lingmiao.shop.business.goods.api.bean.CategoryVO
import com.lingmiao.shop.business.goods.pop.UserGoodsPop
import kotlinx.coroutines.launch

/**
 * @author elson
 * @date 2020/7/16
 * @Desc 商品分组
 */
class PopUserCatePreImpl(val view: BaseView) : BasePreImpl(view) {

    private var goodsGroupPop: UserGoodsPop? = null

    private var lv1Cache: List<CategoryVO>? = null

    fun showGoodsGroupPop(context: Context,cateId : String?, callback: (List<CategoryVO>?, String?) -> Unit) {
        if(lv1Cache != null) {
            showPopWindow(context, lv1Cache!!, callback)
        } else {
            mCoroutine.launch {
                view?.showDialogLoading();
                val resp = GoodsRepository.loadUserCategory(cateId,String.format("%s", UserManager?.getLoginInfo()?.goodsCateId?:"0"));
                lv1Cache = resp.data;
                if (resp.isSuccess) {
                    showPopWindow(context, resp.data, callback)
                }
                view?.hideDialogLoading()
            }
        }
    }

    private fun showPopWindow(
        context: Context,
        list: List<CategoryVO>,
        callback: (List<CategoryVO>?, String?) -> Unit
    ) {
        goodsGroupPop = UserGoodsPop(context).apply {
            lv1Callback = { groupVO, groupName ->
                callback.invoke(groupVO, groupName)
            }
        }
        goodsGroupPop?.setLv1Data(list)
        goodsGroupPop?.showPopupWindow()
    }

    override fun onDestroy() {
        lv1Cache = null;
        super.onDestroy()
    }
}