package com.lingmiao.shop.business.goods.presenter

/**
Create Date : 2021/3/94:34 PM
Auther      : Fox
Desc        :
 **/
interface GoodsPublishNewPre : GoodsPublishPre {

    fun showDeliveryTypePop(str : String?);

    fun showDeliveryModelPop(str : String?);

    fun showGoodsWeightPop(id : String?);

    fun showGoodsUnitPop(id : String?);

    fun loadGoodsInfoFromCenter(id:String)

    interface PublishView : GoodsPublishPre.PublishView {

        fun onUpdateSpeed(id: String?, name : String?);

        fun onUpdateModel(id: String?, name : String?);

        fun onUpdateGoodsWeight(id: String?, name : String?);

        fun onUpdateGoodsUnit(id: String?, name : String?);
    }
}