package com.lingmiao.shop.business.goods.api

import com.lingmiao.shop.business.common.bean.PageVO
import com.lingmiao.shop.business.goods.api.bean.*
import com.lingmiao.shop.business.goods.api.request.QuantityRequest
import com.lingmiao.shop.business.goods.api.bean.DashboardDataVo
import com.lingmiao.shop.business.wallet.bean.PageListVo
import com.lingmiao.shop.net.Fetch
import com.james.common.netcore.networking.http.core.HiResponse
import com.james.common.netcore.networking.http.core.awaitHiResponse
import com.james.common.utils.exts.check
import com.james.common.utils.exts.isNotBlank
import retrofit2.Call
import retrofit2.http.Query

/**
 * Author : Elson
 * Date   : 2020/7/8
 * Desc   : 商品模块 Api
 */
object GoodsRepository {

    private val apiService by lazy {
        Fetch.createService(GoodsApiService::class.java)
    }

    /**
     * 商品数
     */
    suspend fun getDashboardData() : HiResponse<DashboardDataVo> {
        return apiService.getDashboardData().awaitHiResponse();
    }
    /**
     * 商品出售中(上架)，已下架
     */
    suspend fun loadMarketEnableGoodsList(pageNo: Int, marketEnable: Int): HiResponse<PageVO<GoodsVO>> {
        val map = mutableMapOf<String, Any>()
        map.put("page_no", pageNo)
        map.put("page_size", 10)
        map.put("market_enable", marketEnable)
        map.put("is_auth", GoodsVO.AUTH_STATUS_PASS)
        return apiService.loadGoodsList(map).awaitHiResponse()
    }

    /**
     * 是否审核通过的商品
     * @param isAuth：0 待审核，1 审核通过 2 未通过
     */
    suspend fun loadAuthGoodsList(pageNo: Int, isAuth: Int): HiResponse<PageVO<GoodsVO>> {
        val map = mutableMapOf<String, Any>()
        map.put("page_no", pageNo)
        map.put("page_size", 10)
        map.put("is_auth", isAuth)
        return apiService.loadGoodsList(map).awaitHiResponse()
    }

    /**
     * 新增商品
     */
    suspend fun submitGoods(goods : GoodsVOWrapper): HiResponse<GoodsVOWrapper> {
        return apiService.submitGoods(goods).awaitHiResponse()
    }

    /**
     * 编辑商品
     */
    suspend fun modifyGoods(goodsId: String, goods : GoodsVOWrapper): HiResponse<GoodsVOWrapper> {
        return apiService.modifyGoods(goodsId, goods).awaitHiResponse()
    }

    /**
     * 查询商品
     */
    suspend fun loadGoodsById(goodsId: String): HiResponse<GoodsVOWrapper> {
        return apiService.loadGoodsById(goodsId).awaitHiResponse()
    }

    /**
     * 商品下架
     */
    suspend fun makeGoodsDisable(goodsId: String): HiResponse<Unit> {
        return apiService.makeGoodsDisable(goodsId).awaitHiResponse()
    }

    /**
     * 商品上架
     */
    suspend fun makeGoodsEnable(goodsId: String): HiResponse<Unit> {
        return apiService.makeGoodsEnable(goodsId).awaitHiResponse()
    }

    /**
     * 修改商品库存
     */
    suspend fun updateGoodsQuantity(goodsId: String, skuList: List<QuantityRequest>): HiResponse<GoodsVO> {
        return apiService.updateGoodsQuantity(goodsId, skuList).awaitHiResponse()
    }

    /**
     * 批量分销佣金
     */
    suspend fun batchRebate(goodsIds : String, rebate : RebateVo) : HiResponse<Boolean> {
        var map : HashMap<String, Any> = hashMapOf();
        map.put("grade1Rebate", rebate?.grade1Rebate!!);
        map.put("grade2Rebate", rebate?.grade2Rebate!!);
        map.put("inviterRate", rebate?.inviterRate!!);
        return apiService.batchRebate(goodsIds, map).awaitHiResponse();
    }

    /**
     * 分销佣金
     */
    suspend fun rebate(goodsId : String, rebate: RebateVo) : HiResponse<Unit> {
        var map : HashMap<String, Any> = hashMapOf();
        map.put("goods_id", goodsId);
        map.put("grade1Rebate", rebate?.grade1Rebate!!);
        map.put("grade2Rebate", rebate?.grade2Rebate!!);
        map.put("inviterRate", rebate?.inviterRate!!);
        return apiService.rebate(map).awaitHiResponse();
    }

    /**
     * 查询分销佣金
     */
    suspend fun loadRebateById(goodsId: String) : HiResponse<RebateResponseVo> {
        return apiService.loadRebateById(goodsId).awaitHiResponse();
    }

    /**
     * 删除商品
     */
    suspend fun deleteGoods(goodsId: String): HiResponse<Unit> {
        return apiService.deleteGoods(goodsId).awaitHiResponse()
    }

    /**
     * 通过商品名称进行匹配
     * @param goodsName：商品名称
     */
    suspend fun loadGoodsListByName(pageNo: Int, goodsName: String?, supplier_name : String?): HiResponse<PageVO<GoodsVO>> {
        val map = mutableMapOf<String, Any>()
        map.put("page_no", pageNo)
        map.put("page_size", 20)
        if(goodsName?.isNotBlank() == true) {
            map.put("goods_name", goodsName)
        }
        if(supplier_name?.isNotBlank() == true) {
            map.put("supplier_name", supplier_name)
        }
        return apiService.loadGoodsList(map).awaitHiResponse()
    }

    /**
     * 通过商品名称进行匹配
     * @param goodsName：商品名称
     */
    suspend fun loadGoodsListByCId(pageNo: Int, cId : String): HiResponse<PageVO<GoodsVO>> {
        val map = mutableMapOf<String, Any>()
        map.put("page_no", pageNo)
        map.put("page_size", 20)
        map.put("category_id", cId)
        return apiService.loadGoodsList(map).awaitHiResponse()
    }
    //

    /**
     * 获取规格名列表
     */
    suspend fun loadSpecKey(categoryId: String): HiResponse<List<SpecKeyVO>> {
        return apiService.loadSpecKey(categoryId).awaitHiResponse()
    }

    /**
     * 添加自定义规格名
     */
    suspend fun submitSpceKey(categoryId: String, specName: String): HiResponse<SpecKeyVO> {
        return apiService.submitSpceKey(categoryId, specName).awaitHiResponse()
    }

    /**
     * 批量添加自定义规格值
     */
    suspend fun submitSpceValues(specKeyId: String, valueNames: String): HiResponse<List<SpecValueVO>> {
        return apiService.submitSpceValues(specKeyId, valueNames).awaitHiResponse()
    }

    /**
     * 获取指定商品对应的sku信息
     */
    suspend fun loadGoodsSKU(goodsId: String): HiResponse<List<GoodsSkuVOWrapper>> {
        return apiService.loadGoodsSKU(goodsId).awaitHiResponse()
    }

    /**
     * 根据商品id 拉取与商品绑定的 skuList
     */
    suspend fun loadGoodsAppSku(goodsId: String): HiResponse<GoodsSkuCacheVO> {
        return apiService.loadGoodsAppSku(goodsId).awaitHiResponse()
    }

    /**
     * 获取商品分类(一类、二类、三类)
     */
    suspend fun loadCategory(categoryId: String): HiResponse<List<CategoryVO>> {
        return apiService.loadCategory(categoryId).awaitHiResponse()
    }


    /**
     * 获取商品分类(一类、二类、三类)
     */
    suspend fun loadUserCategory(categoryId: String, id : Int?=null): HiResponse<List<CategoryVO>> {
        return apiService.loadUserCategory(categoryId, id).awaitHiResponse()
    }

    suspend fun addCategory(bean : CategoryVO) : HiResponse<CategoryVO> {
        return apiService.addCategory(bean).awaitHiResponse();
    }

    /**
     * 获取店铺商品第一级分组(一级分组内包含了二级分组)
     */
    suspend fun loadLv1ShopGroup(): HiResponse<List<ShopGroupVO>> {
        return apiService.loadLv1ShopGroup().awaitHiResponse()
    }

    /**
     * 获取店铺(第二级)分组
     */
    suspend fun loadLv2ShopGroup(lv1GroupId: String): HiResponse<List<ShopGroupVO>> {
        return apiService.loadLv2ShopGroup(lv1GroupId).awaitHiResponse()
    }

    /**
     * 添加店铺分组
     */
    suspend fun submitShopGroup(group: ShopGroupVO): HiResponse<ShopGroupVO> {
        val map = mutableMapOf<String, Any>()
        if (group.shopCatPid.isNotBlank()) {
            // 添加二级分组需要使用该字段
            map["shop_cat_pid"] = group.shopCatPid!!
        }
        map["shop_cat_name"] = group.shopCatName.check()
        map["disable"] = group.disable
        map["shop_cat_name"] = group.shopCatName.check()
        map["shop_cat_desc"] = group.shopCatDesc.check()
        map["shop_cat_pic"] = group.shopCatPic.check()
        map["sort"] = group.sort
        return apiService.submitShopGroup(map).awaitHiResponse()
    }

    /**
     * 更新店铺分组
     */
    suspend fun updateShopGroup(groupId: String, group: ShopGroupVO): HiResponse<ShopGroupVO> {
        val map = mutableMapOf<String, Any>()
        map["shop_cat_pid"] = group.shopCatPid.check()
        map["shop_cat_name"] = group.shopCatName.check()
        map["disable"] = group.disable
        map["shop_cat_name"] = group.shopCatName.check()
        map["shop_cat_desc"] = group.shopCatDesc.check()
        map["shop_cat_pic"] = group.shopCatPic.check()
        map["sort"] = group.sort
        return apiService.updateShopGroup(groupId, map).awaitHiResponse()
    }

    /**
     * 删除商品分组
     */
    suspend fun deleteShopGroup(shopCatId: String?): HiResponse<Unit> {
        return apiService.deleteShopGroupPop(shopCatId).awaitHiResponse()
    }

    /**
     * 获取配送模板
     * template_type = TONGCHENG
     * template_type = KUAIDI
     */
    suspend fun loadDeliveryTempList(tempType: String): HiResponse<List<DeliveryTempVO>> {
        return apiService.loadDeliveryTempList(tempType).awaitHiResponse()
    }

}