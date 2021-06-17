package com.lingmiao.shop.business.goods.presenter.impl

import android.content.Context
import com.james.common.base.BasePreImpl
import com.james.common.base.BaseView
import com.lingmiao.shop.business.commonpop.pop.AbsOneItemPop
import com.lingmiao.shop.business.goods.api.bean.GoodsDeliveryVo
import com.lingmiao.shop.business.goods.api.bean.UnitVo

/**
Create Date : 2021/3/92:55 PM
Auther      : Fox
Desc        :
 **/
class GoodsUnitPopPreImpl(view: BaseView) : BasePreImpl(view) {

    private var goodsWeightPop: AbsOneItemPop<UnitVo>? = null

    private var goodsUnitPop: AbsOneItemPop<UnitVo>? = null

    fun showWeightPop(context: Context, value : String?, callback : (String?, UnitVo?)-> Unit) {
        goodsWeightPop = AbsOneItemPop<UnitVo>(context, value, UnitVo.getWeightList(), "请选择重量").apply {
            listener = { items: List<UnitVo> ->
                if(items?.size > 0) {
                    val item = items.get(0);
                    callback.invoke(item?.name, item);
                }

            }
        }
        goodsWeightPop?.showPopupWindow();
    }

    fun showUnitPop(context: Context, value : String?, callback : (String?, UnitVo?)-> Unit) {
        goodsUnitPop = AbsOneItemPop<UnitVo>(context, value, UnitVo.getUnitList(), "请选择单位").apply {
            listener = { items: List<UnitVo> ->
                if(items?.size > 0) {
                    val item = items.get(0);
                    callback.invoke(item?.name, item);
                }
            }
        }
        goodsUnitPop?.showPopupWindow();
    }
}