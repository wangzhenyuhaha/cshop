package com.lingmiao.shop.business.goods.api.bean

import com.lingmiao.shop.business.commonpop.bean.ItemData

/**
Create Date : 2021/3/92:56 PM
Auther      : Fox
Desc        :
 **/
class GoodsDeliveryVo : ItemData{
    var name : String?= "";
    var value : String?= "";

    override fun getIValue(): String? {
        return value;
    }

    override fun getIName(): String? {
        return name;
    }

    override fun getIHint(): String? {
        return name;
    }

    companion object {
        fun getTypeList() : MutableList<GoodsDeliveryVo> {
            var list = mutableListOf<GoodsDeliveryVo>();

            var item = GoodsDeliveryVo();
            item.name = "即时配送"
            item.value = "0";
            list.add(item);

            item = GoodsDeliveryVo();
            item.name = "预约配送"
            item.value = "1";
            list.add(item);
            return list;
        }

        fun getModeList() :  MutableList<GoodsDeliveryVo> {
            var list = mutableListOf<GoodsDeliveryVo>();

            var item = GoodsDeliveryVo();
            item.name = "商家配送"
            item.value = "0";
            list.add(item);

            item = GoodsDeliveryVo();
            item.name = "骑手配送"
            item.value = "1";
            list.add(item);

            item = GoodsDeliveryVo();
            item.name = "骑手配送转商家配送"
            item.value = "2";
            list.add(item);
            return list;
        }
    }

}