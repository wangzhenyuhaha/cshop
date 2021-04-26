package com.lingmiao.shop.business.goods.api

import com.lingmiao.shop.base.IConstant
import com.lingmiao.shop.business.common.bean.PageVO
import com.lingmiao.shop.business.goods.api.bean.*
import com.lingmiao.shop.business.goods.api.request.QuantityRequest
import com.lingmiao.shop.business.goods.api.bean.DashboardDataVo
import com.lingmiao.shop.business.wallet.bean.PageListVo
import com.james.common.netcore.networking.http.annotations.WithHiResponse
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
     * 查询商品列表
     */
    @GET("seller/goods")
    @WithHiResponse
    fun loadGoodsList(@QueryMap map: MutableMap<String, Any>): Call<PageVO<GoodsVO>>

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

    /**
     * 商品分类
     */
    @WithHiResponse
    @GET("seller/goods/category/{category_id}/children")
    fun loadCategory(@Path("category_id") categoryId: String): Call<List<CategoryVO>>

    /**
     * 商品分类
     */
    @WithHiResponse
    @POST("seller/goods/category/add")
    fun addCategory(@Body bean: CategoryVO): Call<CategoryVO>

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

    @WithHiResponse
    @GET("seller/shops/cats/{cat_pid}/children")
    fun loadLv2ShopGroup(@Path(value = "cat_pid") groupId: String): Call<List<ShopGroupVO>>

    /**
     * 添加店铺分组
     */
    @WithHiResponse
    @POST("seller/shops/cats")
    fun submitShopGroup(@QueryMap map: MutableMap<String, Any>): Call<ShopGroupVO>

    /**
     * 添加店铺分组
     */
    @WithHiResponse
    @PUT("seller/shops/cats/{id}")
    fun updateShopGroup(@Path(value = "id") groupId: String, @QueryMap map: MutableMap<String, Any>): Call<ShopGroupVO>

    @WithHiResponse
    @DELETE("seller/shops/cats/{id}")
    fun deleteShopGroupPop(@Path("id") shopCatId: String?): Call<Unit>

    /**
     * 获取配送模板
     */
    @WithHiResponse
    @GET("seller/shops/ship-templates")
    fun loadDeliveryTempList(@Query(value = "template_type") tempType: String): Call<List<DeliveryTempVO>>

}