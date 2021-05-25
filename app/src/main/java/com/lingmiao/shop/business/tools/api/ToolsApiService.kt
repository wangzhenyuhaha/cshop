package com.lingmiao.shop.business.tools.api

import com.lingmiao.shop.business.tools.bean.ExpressCompanyVo
import com.lingmiao.shop.business.tools.bean.FreightVo
import com.lingmiao.shop.business.tools.bean.FreightVoItem
import com.lingmiao.shop.business.wallet.bean.DataVO
import com.james.common.netcore.networking.http.annotations.WithHiResponse
import retrofit2.Call
import retrofit2.http.*

interface ToolsApiService {

    /**
     * 快递公司列表
     */
    @WithHiResponse
    @GET("seller/shops/logi-companies")
    fun logiCompanies() : Call<List<ExpressCompanyVo>>;

    /**
     * 关闭
     */
    @WithHiResponse
    @DELETE("seller/shops/logi-companies/{id}")
    fun closeCompany(@Path(value = "id") id: Int) : Call<Unit>;

    /**
     * 开启
     */
    @WithHiResponse
    @POST("seller/shops/logi-companies/{id}")
    fun openCompany(@Path(value = "id") id: Int) : Call<Unit>;

    /**
     * 物流模板列表
     */
    @WithHiResponse
    @GET("seller/shops/ship-templates")
    fun shipTemplates(@Query("template_type")type : String?) : Call<List<FreightVoItem>>;

    /**
     * 添加快递模板
     */
    @WithHiResponse
    @POST("seller/shops/ship-templates")
    fun addShipTemplate(@Body vo : FreightVoItem) : Call<DataVO<FreightVoItem>>;

    /**
     * 修改快递物流模板
     */
    @WithHiResponse
    @PUT("seller/shops/ship-templates/{id}")
    fun updateTemplates(@Path(value = "id") id: String, @Body vo : FreightVoItem ) : Call<FreightVoItem>;
    /**
     * 添加同城模板
     */
    @WithHiResponse
    @POST("seller/shops/ship-templates/addLocalTemp")
    fun addLocalTemp() : Call<DataVO<FreightVo>>;
    /**
     * 删除物流模板
     */
    @WithHiResponse
    @DELETE("seller/shops/ship-templates/{id}")
    fun deleteShipTemplates(@Path(value = "id") id: String) : Call<Unit>;

//    getLocalTemp
    /**
     * 修改物流模板
     */
    @WithHiResponse
    @POST("seller/shops/ship-templates/updateLocalTemp/{id}")
    fun updateShipTemplates(@Path(value = "id") id: String, @Body vo : FreightVoItem ) : Call<Boolean>;
    /**
     * 查询物流模板
     */
    @WithHiResponse
    @GET("seller/shops/ship-templates/{id}")
    fun getShipTemplates(@Path(value = "id") id: String) : Call<FreightVoItem>;

}