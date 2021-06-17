package com.lingmiao.shop.business.goods.presenter.impl

import android.content.Context
import com.james.common.base.BasePreImpl
import com.james.common.base.BaseView
import com.lingmiao.shop.business.commonpop.pop.AbsOneItemPop
import com.lingmiao.shop.business.goods.api.bean.GoodsDeliveryVo

/**
Create Date : 2021/3/92:55 PM
Auther      : Fox
Desc        :
 **/
class GoodsDeliveryPopPreImpl(view: BaseView) : BasePreImpl(view) {

    private var deliveryTypePop: AbsOneItemPop<GoodsDeliveryVo>? = null
    private var deliveryMoodPop: AbsOneItemPop<GoodsDeliveryVo>? = null

    fun showTypePop(context: Context, value : String?, callback : (String?, GoodsDeliveryVo?)-> Unit) {
        deliveryTypePop = AbsOneItemPop<GoodsDeliveryVo>(context, value, GoodsDeliveryVo.getTypeList(), "请选择配送时效").apply {
            listener = { items: List<GoodsDeliveryVo> ->
                if(items?.size > 0) {
                    val item = items.get(0);
                    callback.invoke(item?.name, item);
                }

            }
        }
        deliveryTypePop?.showPopupWindow();
    }

    fun showModelPop(context: Context, value : String?, callback : (String?, GoodsDeliveryVo?)-> Unit) {
        deliveryMoodPop = AbsOneItemPop<GoodsDeliveryVo>(context, value, GoodsDeliveryVo.getModeList(), "请选择配送方式").apply {
            listener = { items: List<GoodsDeliveryVo> ->
                if(items?.size > 0) {
                    val item = items.get(0);
                    callback.invoke(item?.name, item);
                }
            }
        }
        deliveryMoodPop?.showPopupWindow();
    }
}