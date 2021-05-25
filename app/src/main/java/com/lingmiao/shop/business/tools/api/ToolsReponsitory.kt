package com.lingmiao.shop.business.tools.api

import com.lingmiao.shop.business.tools.bean.ExpressCompanyVo
import com.lingmiao.shop.business.tools.bean.FreightVo
import com.lingmiao.shop.business.tools.bean.FreightVoItem
import com.lingmiao.shop.business.wallet.bean.DataVO
import com.lingmiao.shop.net.Fetch
import com.james.common.netcore.networking.http.core.HiResponse
import com.james.common.netcore.networking.http.core.awaitHiResponse

object ToolsRepository {

    private val apiService by lazy {
        Fetch.createService(ToolsApiService::class.java)
    }

    /**
     * 快递公司
     */
    suspend fun logiCompanies() : HiResponse<List<ExpressCompanyVo>> {
        return apiService.logiCompanies().awaitHiResponse();
    }

    suspend fun closeCompany(sellerId: Int) : HiResponse<Unit> {
        return apiService.closeCompany(sellerId).awaitHiResponse();
    }

    suspend fun openCompany(sellerId: Int) : HiResponse<Unit> {
        return apiService.openCompany(sellerId).awaitHiResponse();
    }

    /**
     * 物流模板
     */
    suspend fun shipTemplates(type : String?): HiResponse<List<FreightVoItem>> {
        return apiService.shipTemplates(type).awaitHiResponse();
    }

    /**
     * 添加快递模板
     */
    suspend fun addShipTemplate(vo : FreightVoItem) : HiResponse<DataVO<FreightVoItem>> {
        return apiService.addShipTemplate(vo).awaitHiResponse();
    }

    /**
     * 修改快递模板
     */
    suspend fun updateShipTemplates(vo : FreightVoItem ) : HiResponse<FreightVoItem> {
        return apiService.updateTemplates(vo?.id!!, vo).awaitHiResponse();
    }
    /**
     * 添加同城模板
     */
    suspend fun addLocalTemp() : HiResponse<DataVO<FreightVo>> {
        return apiService.addLocalTemp().awaitHiResponse();
    }

    /**
     * 删除物流模板
     */
    suspend fun deleteShipTemplates(id: String) : HiResponse<Unit> {
        return apiService.deleteShipTemplates(id).awaitHiResponse();
    }

    /**
     * 修改物流模板
     */
    suspend fun updateShipTemplates(id: String, vo : FreightVoItem ) : HiResponse<Boolean> {
        return apiService.updateShipTemplates(id, vo).awaitHiResponse();
    }
    /**
     * 查询物流模板
     */
    suspend fun getShipTemplates(id: String) : HiResponse<FreightVoItem> {
        return apiService.getShipTemplates(id).awaitHiResponse();
    }

}