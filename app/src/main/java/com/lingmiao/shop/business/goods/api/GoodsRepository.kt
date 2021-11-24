package com.lingmiao.shop.business.goods.api

import StatsSalesVo
import android.telecom.Call
import android.util.Log
import com.lingmiao.shop.business.common.bean.PageVO
import com.lingmiao.shop.business.goods.api.bean.*
import com.lingmiao.shop.business.goods.api.request.QuantityRequest
import com.lingmiao.shop.business.goods.api.bean.DashboardDataVo
import com.lingmiao.shop.net.Fetch
import com.james.common.netcore.networking.http.core.HiResponse
import com.james.common.netcore.networking.http.core.awaitHiResponse
import com.james.common.utils.exts.check
import com.james.common.utils.exts.isNotBlank
import com.lingmiao.shop.business.goods.CenterGoods
import com.lingmiao.shop.business.goods.Data
import com.lingmiao.shop.business.goods.GoodsSkuBarcodeLog
import com.lingmiao.shop.business.goods.ScanGoods
import com.lingmiao.shop.business.goods.api.request.PriceAndQuantity
import com.lingmiao.shop.business.goods.api.request.QuantityPriceRequest
import com.lingmiao.shop.business.goods.presenter.impl.PopCategoryPreImpl
import com.lingmiao.shop.business.main.bean.TlMccListVO
import com.lingmiao.shop.business.main.bean.TlMccListVOItem
import com.lingmiao.shop.business.sales.bean.GoodsSalesRespBean

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
    suspend fun getDashboardData(): HiResponse<DashboardDataVo> {
        return apiService.getDashboardData().awaitHiResponse();
    }

    /**
     * 库存预警
     */
    suspend fun getGoodsWarningData(): HiResponse<GoodsVO> {
        return apiService.getGoodsWarningData().awaitHiResponse();
    }

    /**
     * 所有
     */
    suspend fun loadAllGoodsList(
        pageNo: Int,
        groupPath: String?,
        catePath: String?,
        isEvent: Int?,
        order: String?,
        isDesc: Int?
    ): HiResponse<PageVO<GoodsVO>> {
        val map = mutableMapOf<String, Any>()
        map.put("page_no", pageNo)
        map.put("page_size", 10)
        groupPath?.let { it1 -> map.put("shop_cat_path", it1) }
        catePath?.let { it1 -> map.put("category_path", it1) }
        isEvent?.let { it1 -> map.put("is_event", it1) }
        order?.let { it1 -> map.put("order_column", it1) }
        isDesc?.let { it1 -> map.put("is_desc", it1) }
        return apiService.loadGoodsList(map).awaitHiResponse()
    }

    /**
     * 预警商品
     */
    suspend fun loadWarningGoodsList(pageNum: Int): HiResponse<PageVO<GoodsVO>> {
        val map = mutableMapOf<String, Any>()
        map.put("page_num", pageNum)
        map.put("page_size", 10)
        return apiService.loadWarningGoodsList(map).awaitHiResponse()
    }


    /**
     * 售馨商品列表
     */
    suspend fun loadSellOutGoodsList(
        pageNo: Int,
        groupPath: String?,
        catePath: String?,
        isEvent: Int?,
        order: String?,
        isDesc: Int?
    ): HiResponse<PageVO<GoodsVO>> {
        val map = mutableMapOf<String, Any>()
        map.put("page_no", pageNo)
        map.put("page_size", 10)
        map.put("enable_quantity", 0);
        groupPath?.let { it1 -> map.put("shop_cat_path", it1) }
        catePath?.let { it1 -> map.put("category_path", it1) }
        isEvent?.let { it1 -> map.put("is_event", it1) }
        order?.let { it1 -> map.put("order_column", it1) }
        isDesc?.let { it1 -> map.put("is_desc", it1) }
        return apiService.loadGoodsList(map).awaitHiResponse()
    }

    /**
     * 已上架、待上架、已下架
     */
    suspend fun loadGoodsList(
        pageNo: Int,
        marketEnable: String,
        isAuth: String,
        groupPath: String?, catePath: String?, isEvent: Int?, order: String?, isDesc: Int?
    ): HiResponse<PageVO<GoodsVO>> {
        val map = mutableMapOf<String, Any>()
        map.put("page_no", pageNo)
        map.put("page_size", 10)
        map.put("market_enable", marketEnable)
        map.put("is_auth_string", isAuth)
        groupPath?.let { it1 -> map.put("shop_cat_path", it1) }
        catePath?.let { it1 -> map.put("category_path", it1) }
        isEvent?.let { it1 -> map.put("is_event", it1) }
        order?.let { it1 -> map.put("order_column", it1) }
        isDesc?.let { it1 -> map.put("is_desc", it1) }
        return apiService.loadGoodsList(map).awaitHiResponse()
    }

    suspend fun loadGoodsListOfCatePath(
        pageNo: Int,
        marketEnable: String,
        isAuth: String,
        path: String?
    ): HiResponse<PageVO<GoodsVO>> {
        val map = mutableMapOf<String, Any>()
        map.put("page_no", pageNo)
        map.put("page_size", 10)
        map.put("market_enable", marketEnable)
        map.put("is_auth_string", isAuth)
        if (path != null) {
            map.put("category_path", path!!)
        }
        return apiService.loadGoodsList(map).awaitHiResponse()
    }

    suspend fun loadGoodsListOfCateId(
        pageNo: Int,
        marketEnable: String?,
        isAuth: String,
        cid: String?,
        cPath: String?
    ): HiResponse<PageVO<GoodsVO>> {
        val map = mutableMapOf<String, Any>()
        map.put("page_no", pageNo)
        map.put("page_size", 10)
        if (marketEnable != null) {
            map.put("market_enable", marketEnable)
        }
        map.put("is_auth_string", isAuth)
        if (cid != null) {
            map.put("not_in_cat_id", cid);
        }
        if (cPath != null) {
            map.put("category_path", cPath);
        }
        return apiService.loadGoodsList(map).awaitHiResponse()
    }

    suspend fun loadMenuGoodsList(
        pageNo: Int,
        marketEnable: String,
        isAuth: String,
        catPath: String?
    ): HiResponse<PageVO<GoodsVO>> {
        val map = mutableMapOf<String, Any>()
        map.put("page_no", pageNo)
        map.put("page_size", 10)
        // map.put("market_enable", marketEnable)
        // map.put("is_auth_string", isAuth)
        if (catPath != null) {
            map.put("shop_cat_path", catPath!!)
        }
        return apiService.loadGoodsList(map).awaitHiResponse()
    }


    /**
     * 新增商品
     */
    suspend fun submitGoods(goods: GoodsVOWrapper, is_up: String): HiResponse<GoodsVOWrapper> {
        return apiService.submitGoods(is_up, goods).awaitHiResponse()
    }

    /**
     * 编辑商品
     */
    suspend fun modifyGoods(
        goodsId: String,
        is_up: String,
        goods: GoodsVOWrapper
    ): HiResponse<GoodsVOWrapper> {
        return apiService.modifyGoods(goodsId, is_up, goods).awaitHiResponse()
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
    suspend fun updateGoodsQuantity(
        goodsId: String,
        skuList: List<QuantityRequest>
    ): HiResponse<GoodsVO> {
        return apiService.updateGoodsQuantity(goodsId, skuList).awaitHiResponse()
    }

    /**
     * 修改活动库存与价格
     */
    suspend fun updateGoodsQuantityAndPrice(
        goodsId: String,
        skuList: PriceAndQuantity
    ): HiResponse<Unit> {
        return apiService.updateGoodsQuantityAndPrice(goodsId, skuList).awaitHiResponse();
    }

    /**
     * 批量分销佣金
     */
    suspend fun batchRebate(goodsIds: String, rebate: RebateVo): HiResponse<Boolean> {
        var map: HashMap<String, Any> = hashMapOf();
        map.put("grade1Rebate", rebate?.grade1Rebate!!);
        map.put("grade2Rebate", rebate?.grade2Rebate!!);
        map.put("inviterRate", rebate?.inviterRate!!);
        return apiService.batchRebate(goodsIds, map).awaitHiResponse();
    }

    /**
     * 分销佣金
     */
    suspend fun rebate(goodsId: String, rebate: RebateVo): HiResponse<Unit> {
        var map: HashMap<String, Any> = hashMapOf();
        map.put("goods_id", goodsId);
        map.put("grade1Rebate", rebate?.grade1Rebate!!);
        map.put("grade2Rebate", rebate?.grade2Rebate!!);
        map.put("inviterRate", rebate?.inviterRate!!);
        return apiService.rebate(map).awaitHiResponse();
    }

    /**
     * 查询分销佣金
     */
    suspend fun loadRebateById(goodsId: String): HiResponse<RebateResponseVo> {
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
    suspend fun loadGoodsListByName(
        pageNo: Int,
        goodsName: String?,
        supplier_name: String?
    ): HiResponse<PageVO<GoodsVO>> {
        val map = mutableMapOf<String, Any>()
        map.put("page_no", pageNo)
        map.put("page_size", 20)
        if (goodsName?.isNotBlank() == true) {
            map.put("goods_name", goodsName)
        }
        if (supplier_name?.isNotBlank() == true) {
            map.put("supplier_name", supplier_name)
        }
        return apiService.loadGoodsList(map).awaitHiResponse()
    }


    /**
     * 通过商品名称从中心库进行匹配
     * @param goodsName：商品名称
     */
    suspend fun loadGoodsListByNameFromCenter(
        pageNo: Int,
        pageSize: Int = 20,
        goodsName: String
    ): HiResponse<CenterGoods> {
        return apiService.loadGoodsListFromCenter(pageNo, pageSize, goodsName).awaitHiResponse()
    }

    /**
     * 通过商品名称进行匹配
     * @param goodsName：商品名称
     */
    suspend fun loadGoodsListByCId(pageNo: Int, cId: String): HiResponse<PageVO<GoodsVO>> {
        val map = mutableMapOf<String, Any>()
        map.put("page_no", pageNo)
        map.put("page_size", 20)
        map.put("category_id", cId)
        return apiService.loadGoodsList(map).awaitHiResponse()
    }

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
    suspend fun submitSpceValues(
        specKeyId: String,
        valueNames: String
    ): HiResponse<List<SpecValueVO>> {
        return apiService.submitSpceValues(specKeyId, valueNames).awaitHiResponse()
    }

    /**
     * 删除规格名
     */
    suspend fun delSpecName(ids: String): HiResponse<Unit> {
        return apiService.delSpecName(ids).awaitHiResponse();
    }

    /**
     * 删除规格值
     */
    suspend fun delSpecValue(ids: String): HiResponse<Unit> {
        return apiService.delSpecValue(ids).awaitHiResponse();
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
     * 根据商品id 从中心库拉取与商品绑定的 skuList
     */
    suspend fun loadGoodsAppSkuFromCenter(goodsId: String): HiResponse<GoodsSkuCacheVO> {
        return apiService.loadGoodsAppSkuFromCenter(goodsId).awaitHiResponse()
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
    suspend fun loadUserCategory(
        categoryId: String?,
        sellerId: String? = null
    ): HiResponse<List<CategoryVO>> {
        return apiService.loadUserCategory(categoryId, sellerId).awaitHiResponse()
    }

    //获取商品类目
    suspend fun loadCategory(): HiResponse<List<CategoryVO>> {
        return apiService.loadUserCategory(PopCategoryPreImpl.LV1_CATEGORY_ID, "0")
            .awaitHiResponse()
    }

    //获取通联商品类目
    suspend fun loadCategory2(
        categoryId: Int?
    ): HiResponse<List<TlMccListVOItem>> {
        return apiService.loadCategoryMccList(categoryId).awaitHiResponse()
    }

    suspend fun addCategory(bean: CategoryVO): HiResponse<CategoryVO> {
        return apiService.addCategory(bean).awaitHiResponse();
    }

    /**
     * 更新店铺分组
     */
    suspend fun updateCategory(cate: CategoryVO): HiResponse<CategoryVO> {
        val map = mutableMapOf<String, Any>()
        map.put("name", cate.name)
        map.put("parent_id", cate.parentId)
        map.put("category_id", cate.categoryId)
        map.put("categoryPath", cate.categoryPath)
        map.put("image", cate.image ?: "")
        map.put("goodsCount", cate.goodsCount)
        map.put("categoryOrder", cate.categoryOrder)
        return apiService.updateCategory(cate.categoryId, map).awaitHiResponse()
    }

    /**
     * 删除
     */
    suspend fun deleteCate(id: String): HiResponse<Unit> {
        return apiService.deleteCate(id).awaitHiResponse();
    }

    /**
     * 获取店铺商品第一级分组(一级分组内包含了二级分组)
     */
    suspend fun loadLv1ShopGroup(): HiResponse<List<ShopGroupVO>> {
        return apiService.loadLv1ShopGroup().awaitHiResponse()
    }

    /**
     * 获取店铺商品第一级分组(一级分组内包含了二级分组)
     */
    //isTop=1  获取置顶菜单       isTop=0  获取常用菜单
    suspend fun loadLv1ShopGroup(isTop: Int): HiResponse<List<ShopGroupVO>> {
        return apiService.loadLv1ShopGroup(isTop).awaitHiResponse()
    }

    /**
     * 获取某分类第一级分组(一级分组内包含了二级分组)
     */
    //isTop=1  获取置顶菜单       isTop=0  获取常用菜单
    suspend fun loadLv1ShopGroup(isTop: Int, path: String?): HiResponse<List<ShopGroupVO>> {
        return apiService.loadLv1ShopGroup(isTop, path).awaitHiResponse()
    }

    /**
     * 获取店铺(第二级)分组
     */
    suspend fun loadLv2ShopGroup(lv1GroupId: String): HiResponse<List<ShopGroupVO>> {
        return apiService.loadLv2ShopGroup(lv1GroupId).awaitHiResponse()
    }

    /**
     * 获取店铺(第二级)分组
     */
    suspend fun load2LvShopGroup(lv1GroupId: String, isTop: Int): HiResponse<List<ShopGroupVO>> {
        return apiService.load2LvShopGroup(lv1GroupId, isTop).awaitHiResponse()
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
        map["is_top"] = group.isTop
        return apiService.submitShopGroup(map).awaitHiResponse()
    }

    suspend fun getShopGroup(groupId: String): HiResponse<ShopGroupVO> {
        return apiService.getShopGroup(groupId).awaitHiResponse()
    }

    /**
     * 更新店铺分组
     */
    suspend fun updateShopGroup(groupId: String, group: ShopGroupVO): HiResponse<ShopGroupVO> {
        val map = mutableMapOf<String, Any>()
        map["shop_cat_pid"] = group.shopCatPid.check()
        map["shop_cat_name"] = group.shopCatName.check()
        map["disable"] = group.disable
        map["is_event"] = group.isEvent;
        map["shop_cat_name"] = group.shopCatName.check()
        map["shop_cat_desc"] = group.shopCatDesc.check()
        map["shop_cat_pic"] = group.shopCatPic.check()
        map["sort"] = group.sort
        map["is_top"] = group.isTop
        return apiService.updateShopGroup(groupId, map).awaitHiResponse()
    }

    suspend fun sort(isTop: Int, cId: String, sort: Int): HiResponse<Unit> {
        return apiService.sort(isTop, cId, sort).awaitHiResponse();
    }

    /**
     * 删除商品分组
     */
    suspend fun deleteShopGroup(shopCatId: String?): HiResponse<Unit> {
        return apiService.deleteShopGroupPop(shopCatId).awaitHiResponse()
    }

    suspend fun bindGoods(ids: List<Int?>?, id: String?): HiResponse<Unit> {
        return apiService.bindGoods(ids, id).awaitHiResponse();
    }

    /**
     * 获取配送模板
     * template_type = TONGCHENG
     * template_type = KUAIDI
     */
    suspend fun loadDeliveryTempList(tempType: String): HiResponse<List<DeliveryTempVO>> {
        return apiService.loadDeliveryTempList(tempType).awaitHiResponse()
    }

    /***************************从中心库****************************************************/
    /**
     * 通过商品名称进行匹配
     * @param goodsName：商品名称
     */
    suspend fun getCenterGoods(pageNo: Int, cId: String): HiResponse<PageVO<GoodsVO>> {
        val map = mutableMapOf<String, Any>()
        map.put("category_path", cId)
        map.put("page_no", pageNo)
        map.put("page_size", 20)
        return apiService.getCenterGoods(map).awaitHiResponse()
    }

    /**
     * 从中心库添加商品
     */
    suspend fun addGoodsOfCenter(
        ids: String,
        categoryId: String?,
        shopCatId: String?,
        is_force: Int?
    ): HiResponse<Unit> {
        return apiService.addGoodsOfCenter(ids, categoryId, shopCatId, is_force).awaitHiResponse()
    }

    //使用条形码查询商品
    suspend fun searchGoodsOfCenter(id: String): HiResponse<ScanGoods> {
        return apiService.getCenterGoodsByScan(id).awaitHiResponse()
    }

    //添加条形码扫描记录
    suspend fun addGoodsSkuBarCodeLog(id: String, bar_code: String, url: String): HiResponse<Unit> {
        return apiService.addGoodsSkuBarCodeLog(GoodsSkuBarcodeLog(bar_code, url, id))
            .awaitHiResponse()
    }

    //从中心库查询商品
    suspend fun searchGoodsFromCenter(id: String): HiResponse<GoodsVOWrapper> {
        return apiService.getCenterGoodsFromCenter(id).awaitHiResponse()
    }


    /***************************商品信息****************************************************/
    /**
     * 获取商品信息列表
     */
    suspend fun getInfoList(id: String): HiResponse<List<GoodsParamVo>> {
        return apiService.getInfoList(id).awaitHiResponse();
    }

    /**
     * 商品信息添加
     */
    suspend fun addInfo(vo: GoodsParamVo): HiResponse<GoodsParamVo> {
        return apiService.addInfo(vo).awaitHiResponse();
    }

    /**
     * 商品信息删除
     */
    suspend fun deleteInfo(id: String): HiResponse<Unit> {
        return apiService.deleteInfo(id).awaitHiResponse();
    }

    /**
     * 跟据分类id获取商品信息列表
     */
    suspend fun getInfoListOfCategory(
        categoryIds: String,
        id: Int? = null
    ): HiResponse<List<GoodsParamVo>> {
        return apiService.getInfoListOfCategory(categoryIds, id).awaitHiResponse()
    }

    /***************************商品信息****************************************************/
    suspend fun getSpecList(cid: String): HiResponse<List<SpecKeyVO>> {
        return apiService.getSpecList(cid).awaitHiResponse();
    }

    suspend fun addSpec(cid: String, name: String): HiResponse<GoodsSpecVo> {
        return apiService.addSpec(cid, name).awaitHiResponse();
    }

    suspend fun addSpecAndValue(item: CateSpecAndValueVo): HiResponse<CateSpecAndValueVo> {
        return apiService.addSpecAndValue(item).awaitHiResponse();
    }

    suspend fun goodsSales(startTime: Long, endTime: Long): HiResponse<GoodsSalesRespBean> {
        return apiService.goodsSales(startTime, endTime).awaitHiResponse();
    }

    suspend fun salesCount(type: String, startTime: Long, endTime: Long): HiResponse<StatsSalesVo> {
        return apiService.salesCount(type, startTime, endTime).awaitHiResponse();
    }
}