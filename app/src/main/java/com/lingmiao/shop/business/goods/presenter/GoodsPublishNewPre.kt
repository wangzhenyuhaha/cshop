package com.lingmiao.shop.business.goods.presenter

import com.lingmiao.shop.business.goods.Data

/**
Create Date : 2021/3/94:34 PM
Auther      : Fox
Desc        :
 **/
interface GoodsPublishNewPre : GoodsPublishPre {

    fun searchGoods(searchText: String)

    fun showDeliveryTypePop(str: String?);

    fun showDeliveryModelPop(str: String?);

    fun showGoodsWeightPop(id: String?);

    //添加条形码扫描记录
    fun addGoodsSkuBarCodeLog(id: Int, bar_code: String, url: String)

    fun showGoodsUnitPop(id: String?);

    fun loadGoodsInfoFromCenter(id: String)

    interface PublishView : GoodsPublishPre.PublishView {

        //搜索商品成功
        fun searchGoodsSuccess(list: List<Data>)

        //搜索商品失败
        fun searchGoodsFailed()

        //上传商品图片数据
        fun loadGoodsInfo(goods_id: String?)

        fun onUpdateSpeed(id: String?, name: String?);

        fun onUpdateModel(id: String?, name: String?);

        fun onUpdateGoodsWeight(id: String?, name: String?);

        fun onUpdateGoodsUnit(id: String?, name: String?);
    }
}