package com.lingmiao.shop.business.sales.bean

import com.google.gson.annotations.SerializedName
import java.io.Serializable

/**
Create Date : 2021/7/1412:15 上午
Auther      : Fox
Desc        :
 **/
class GoodsSalesRespBean : Serializable {

    @SerializedName("goodsSales")
    var goodsSales : GoodsSalesBean? = null;

    @SerializedName("salesGoodsTop10")
    var salesGoodsTop10 : List<SalesGoodsTop10?>? = null;

    @SerializedName("unsalesGoodsTop10")
    var unsalesGoodsTop10 : List<SalesGoodsTop10?>? = null;

    @SerializedName("categorySales")
    var categorySales : List<CategorySales?>? = null;


}


class CategorySales : Serializable{
    @SerializedName("category_name")
    var categoryName: String? = null;
    @SerializedName("num")
    var num : Int? = 0;
}

class SalesGoodsTop10 : Serializable{

    @SerializedName("goods_name")
    var goodsName: String? = null;
    @SerializedName("sum_goods_price")
    var sumGoodsPrice : Double? = 0.0;
    @SerializedName("sum_goods_num")
    var sumGoodsNum : Int? = 0;
}

class GoodsSalesBean {

    @SerializedName("sum_order_num")
    var sumOrderNum: Long? = 0;

    @SerializedName("sum_goods_num")
    var sumGoodsNum: Long? = 0;

    @SerializedName("per_ticket_sales")
    var perTicketSales: Double? = 0.0;
}

