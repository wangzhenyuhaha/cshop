package com.lingmiao.shop.business.goods.api

import StatsSalesVo
import com.lingmiao.shop.business.common.bean.PageVO
import com.lingmiao.shop.business.goods.api.bean.*
import com.lingmiao.shop.business.goods.api.request.QuantityRequest
import com.lingmiao.shop.business.goods.api.bean.DashboardDataVo
import com.james.common.netcore.networking.http.annotations.WithHiResponse
import com.lingmiao.shop.business.goods.api.request.PriceAndQuantity
import com.lingmiao.shop.business.goods.api.request.QuantityPriceRequest
import com.lingmiao.shop.business.sales.bean.GoodsSalesRespBean
import retrofit2.Call
import retrofit2.http.*

/**
 * Author : Elson
 * Date   : 2020/7/8
 * Desc   : 商品相关接口
 */
interface GoodsApiService {

    /**
     * 查询商品数
     */
    @GET("seller/statistics/dashboard/shop")
    @WithHiResponse
    fun getDashboardData() : Call<DashboardDataVo>;

    /**
     * 查询预警商品
     */
    @GET("/seller/goods/getQuantityWarnGoods")
    @WithHiResponse
    fun getGoodsWarningData() : Call<GoodsVO>;

    /**
     * 查询商品列表
     */
    @GET("seller/goods")
    @WithHiResponse
    fun loadGoodsList(@QueryMap map: MutableMap<String, Any>): Call<PageVO<GoodsVO>>

    /**
     * 查询预警商品列表
     */
    @GET("seller/goods/getQuantityWarnGoods")
    @WithHiResponse
    fun loadWarningGoodsList(@QueryMap map: MutableMap<String, Any>): Call<PageVO<GoodsVO>>

    /**
     * 新增商品
     */
    @POST("seller/goods")
    @WithHiResponse
    fun submitGoods(@Body goods : GoodsVOWrapper): Call<GoodsVOWrapper>

    /**
     * 编辑商品
     */
    @PUT("seller/goods/{id}")
    @WithHiResponse
    fun modifyGoods(@Path(value = "id") goodsId: String, @Body goods : GoodsVOWrapper): Call<GoodsVOWrapper>

    /**
     * 查询商品
     */
    @GET("seller/goods/{id}")
    @WithHiResponse
    fun loadGoodsById(@Path(value = "id") goodsId: String): Call<GoodsVOWrapper>

    /**
     * 商品下架
     * http://t-api.seller.fisheagle.cn:7003/seller/goods/{goods_ids}/under
     */
    @PUT("seller/goods/{goods_ids}/under")
    @WithHiResponse
    fun makeGoodsDisable(@Path(value = "goods_ids") goodsId: String): Call<Unit>

    /**
     * 商品上架
     * http://t-api.seller.fisheagle.cn:7003/seller/goods/{goods_ids}/ups
     */
    @PUT("seller/goods/{goods_ids}/ups")
    @WithHiResponse
    fun makeGoodsEnable(@Path(value = "goods_ids") goodsId: String): Call<Unit>

    /**
     * 商家单独维护库存接口
     * http://t-api.seller.fisheagle.cn:7003/seller/goods/{goods_id}/quantity
     */
    @PUT("seller/goods/{goods_id}/quantity")
    @WithHiResponse
    fun updateGoodsQuantity(@Path(value = "goods_id") goodsId: String, @Body skuList: List<QuantityRequest>): Call<GoodsVO>

    /**
     * 活动库存与价格
     */
    @POST("seller/goods/{goods_id}/quantityAndPrice")
    fun updateGoodsQuantityAndPrice(@Path(value = "goods_id") goodsId: String, @Body list :PriceAndQuantity) : Call<Unit>;
    /**
     * 批量佣金
     * http://t-api.seller.fisheagle.cn:7003/seller/distribution/batchEditGoods/{goods_id}
     */
    @FormUrlEncoded
    @PUT("seller/distribution/batchEditGoods/{goods_id}")//?grade1Rebate={grade1Rebate}&grade2Rebate={grade2Rebate}&inviterRate={inviterRate}
    @WithHiResponse
    fun batchRebate(@Path(value = "goods_id") goodsId: String, @FieldMap map: MutableMap<String, Any>) : Call<Boolean>

    /**
     * 佣金
     * http://t-api.seller.fisheagle.cn:7003/seller/distribution/goods
     */
    @FormUrlEncoded
    @PUT("seller/distribution/goods")
    @WithHiResponse
    fun rebate(@FieldMap map: MutableMap<String, Any>) : Call<Unit>

    /**
     * http://t-api.seller.fisheagle.cn:7003/seller/distribution/goods/{id}
     * 查询佣金
     */
    @GET("seller/distribution/goods/{id}")
    @WithHiResponse
    fun loadRebateById(@Path(value = "id") goodsId: String): Call<RebateResponseVo>

    /**
     * 删除商品
     */
    @DELETE("seller/goods/{goods_ids}")
    @WithHiResponse
    fun deleteGoods(@Path(value = "goods_ids") goodsId: String): Call<Unit>

    /***************************分类****************************************************/
    /**
     * 商品分类
     */
    @WithHiResponse
    @GET("seller/goods/category/{category_id}/children")
    fun loadCategory(@Path("category_id") categoryId: String): Call<List<CategoryVO>>

    /**
     * 新分类
     */
    @WithHiResponse
    @GET("seller/goods/category/app/{category_id}/children")
    fun loadUserCategory(@Path("category_id") categoryId: String?, @Query(value = "seller_id") id: String?=null): Call<List<CategoryVO>>


    /**
     * 新增商品分类
     */
    @WithHiResponse
    @POST("seller/goods/category/add")
    fun addCategory(@Body bean: CategoryVO): Call<CategoryVO>

    @WithHiResponse
    @PUT("seller/goods/category/update/{id}")
    fun updateCategory(@Path(value = "id") id: String, @QueryMap data : MutableMap<String, Any>): Call<CategoryVO>

    /**
     * 删除商品
     */
    @DELETE("seller/goods/category/{id}")
    @WithHiResponse
    fun deleteCate(@Path(value = "id") id: String): Call<Unit>

    /***************************规格****************************************************/
    /**
     * 获取规格名列表
     */
    @WithHiResponse
    @GET("seller/goods/categories/{category_id}/specs")
    fun loadSpecKey(@Path("category_id") categoryId: String): Call<List<SpecKeyVO>>

    /**
     * 添加自定义的规格名
     */
    @WithHiResponse
    @POST("seller/goods/categories/{category_id}/specs")
    fun submitSpceKey(@Path("category_id") categoryId: String, @Query("spec_name") specName: String): Call<SpecKeyVO>

    /**
     * 添加自定义的规格值
     */
    @WithHiResponse
    @POST("seller/goods/specs/{spec_id}/batch/values")
    fun submitSpceValues(@Path("spec_id") specKeyId: String, @Query("value_name") valueNames: String): Call<List<SpecValueVO>>

    /**
     * 商品分类
     */
    @WithHiResponse
    @GET("seller/goods/{goods_id}/skus")
    fun loadGoodsSKU(@Path("goods_id") goodsId: String): Call<List<GoodsSkuVOWrapper>>

    /**
     * 根据商品id 拉取与商品绑定的 skuList
     */
    @WithHiResponse
    @GET("seller/goods/app/{goods_id}/skus")
    fun loadGoodsAppSku(@Path("goods_id") goodsId: String): Call<GoodsSkuCacheVO>

    // -------------------------------- 店铺接口 -----------------------------
    /**
     * 商品分组
     */
    @WithHiResponse
    @GET("seller/shops/cats")
    fun loadLv1ShopGroup(): Call<List<ShopGroupVO>>

    /**
     * 商品分组
     */
    @WithHiResponse
    @GET("seller/shops/cats")
    fun loadLv1ShopGroup(@Query("is_top") isTop: Int): Call<List<ShopGroupVO>>

    @WithHiResponse
    @GET("seller/shops/cats/{cat_pid}/children")
    fun loadLv2ShopGroup(@Path(value = "cat_pid") groupId: String): Call<List<ShopGroupVO>>

    @WithHiResponse
    @GET("seller/shops/cats/{cat_pid}/children")
    fun load2LvShopGroup(@Path(value = "cat_pid") groupId: String, @Query("is_top") isTop: Int): Call<List<ShopGroupVO>>

    /**
     * 添加店铺分组
     */
    @WithHiResponse
    @POST("seller/shops/cats")
    fun submitShopGroup(@QueryMap map: MutableMap<String, Any>): Call<ShopGroupVO>


    @WithHiResponse
    @GET("seller/shops/cats/{id}")
    fun getShopGroup(@Path(value = "id") groupId: String): Call<ShopGroupVO>

    /**
     * 添加店铺分组
     */
    @WithHiResponse
    @PUT("seller/shops/cats/{id}")
    fun updateShopGroup(@Path(value = "id") groupId: String, @QueryMap map: MutableMap<String, Any>): Call<ShopGroupVO>

    @WithHiResponse
    @GET("seller/shops/cats/adjust_sort")
    fun sort(@Query("is_top") isTop: Int, @Query("shop_cat_id") shopCatId : String, @Query("sort") sort : Int) : Call<Unit>

    @WithHiResponse
    @DELETE("seller/shops/cats/{id}")
    fun deleteShopGroupPop(@Path("id") id: String?): Call<Unit>

    /**
     * 获取配送模板
     */
    @WithHiResponse
    @GET("seller/shops/ship-templates")
    fun loadDeliveryTempList(@Query(value = "template_type") tempType: String): Call<List<DeliveryTempVO>>

    /**
     * 绑商品
     */
    @WithHiResponse
    @POST("seller/shops/cats/bindGoods")
    fun bindGoods(@Query("goods_ids") ids: List<Int?>? ,@Query("shop_cat_id") id : String?) : Call<Unit>
    /***************************中心库****************************************************/
    /**
     * 中心库商品
     */
    @GET("seller/center/goods/")
    @WithHiResponse
    fun getCenterGoods(@QueryMap map: MutableMap<String, Any>): Call<PageVO<GoodsVO>>

    @WithHiResponse
    @POST("seller/goods/addCenterGoodsToShop")
    fun addGoodsOfCenter(@Query("center_goods_ids") id: String?): Call<Unit>

    /***************************商品信息****************************************************/
    /**
     * 商品信息查询
     */
    @WithHiResponse
    @GET("seller/goods/parameters/getByCategoryAndSeller")
    fun getInfoList(@Query(value = "categoryId") id: String): Call<List<GoodsParamVo>>

    /**
     * 商品信息添加
     */
    @WithHiResponse
    @POST("seller/goods/parameters")
    fun addInfo(@Body body: GoodsParamVo): Call<GoodsParamVo>



    /**
     * 获取商品信息列表
     */
    @WithHiResponse
    @GET("seller/goods/parameters/getByCategoryAndSeller")
    fun getInfoListOfCategory(@Query("category_ids") categoryIds: String, @Query(value = "seller_id") id: Int?=null): Call<List<GoodsParamVo>>

    /**
     * 商品信息删除
     */
    @DELETE("seller/goods/parameters/{id}")
    @WithHiResponse
    fun deleteInfo(@Path(value = "id") id: String): Call<Unit>

    /***************************规格****************************************************/
    /**
     * 规格查询
     */
    @WithHiResponse
    @GET("seller/goods/categories/{cid}/specs")
    fun getSpecList(@Path(value = "cid") id: String): Call<List<SpecKeyVO>>

    /**
     * 规格添加
     */
    @WithHiResponse
    @POST("seller/goods/categories/{c_id}/specs")
    fun addSpec(@Path(value = "c_id") cid: String, @Query("spec_name") name: String): Call<GoodsSpecVo>

    @WithHiResponse
    @DELETE("seller/goods/specs/{ids}")
    fun delSpecName(@Path(value = "ids") ids: String): Call<Unit>

    @WithHiResponse
    @DELETE("seller/goods/specsValue/{ids}")
    fun delSpecValue(@Path(value = "ids") ids: String): Call<Unit>

    /**
     * 商家自定义某分类的规格项
     */
    @WithHiResponse
    @POST("seller/goods/categories/specsAndValue")
    fun addSpecAndValue(@Body body: CateSpecAndValueVo): Call<CateSpecAndValueVo>

    @WithHiResponse
    @GET("seller/statistics/goods/goodsSales")
    fun goodsSales(@Query("start_time")startTime : Long, @Query("end_time") endTime : Long) : Call<GoodsSalesRespBean>

    @WithHiResponse
    @GET("seller/statistics/reports/sales_count")
    fun salesCount(@Query("cycle_type") type: String, @Query("start_time")startTime : Long, @Query("end_time") endTime : Long) : Call<StatsSalesVo>;
}