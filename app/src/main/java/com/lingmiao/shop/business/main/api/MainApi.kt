package com.lingmiao.shop.business.main.api

import com.lingmiao.shop.business.main.bean.*
import com.james.common.netcore.networking.http.annotations.WithHiResponse
import com.james.common.netcore.networking.http.core.HiResponse
import com.james.common.netcore.networking.http.core.awaitHiResponse
import com.james.common.utils.exts.isNotBlank
import com.lingmiao.shop.business.common.bean.PageVO
import com.lingmiao.shop.business.goods.api.GoodsRepository
import com.lingmiao.shop.business.goods.api.bean.GoodsVO
import com.lingmiao.shop.business.goods.api.bean.WxPayReqVo
import com.lingmiao.shop.business.main.fragment.BindAccountFragment
import com.lingmiao.shop.business.me.bean.*
import com.lingmiao.shop.business.wallet.bean.BankCardVo
import com.lingmiao.shop.business.wallet.bean.DataVO
import retrofit2.Call
import retrofit2.http.*

interface MainApi {

    //APP首页数据
    @GET("seller/statistics/dashboard/appIndex")
    @WithHiResponse
    fun getMainInfo(): Call<MainInfo>

    //APP首页数据
    @GET("seller/statistics/dashboard/showIndex")
    @WithHiResponse
    fun getMainData(): Call<MainInfoVo>

    //    查询店铺站内消息列表
    @GET("seller/shops/shop-notice-logs")
    @WithHiResponse
    fun getMessageCenterList(
        @Query("page_no") pageIndex: Int,
        @Query("page_size") pageSize: Int
    ): Call<MessageCenterResponse>

    //    查询店铺状态
    @GET("seller/shops/statusInfo")
    @WithHiResponse
    fun getShopStatus(): Call<ShopStatus>

    @POST("seller/shops/editShopOpenStatus/{shop_id}")
    @WithHiResponse
    fun editShopStatus(@Path("shop_id") id: Int, @Body status: OpenShopStatusVo): Call<Unit>

    //开店 经营类目列表
    @GET("seller/goods/category/0/children")
    @WithHiResponse
    fun getApplyShopCategory(
        @Query(value = "seller_id") id: Int? = 0,
        @Query(value = "parent_id") pId: Int? = 0
    ): Call<List<ApplyShopCategory>>

    // 会员申请店铺
    @POST("seller/shops/apply")
    @WithHiResponse
    fun applyShopInfo(@Body bean: ApplyShopInfo): Call<ApplyShopInfoResponse>

    //    获取店铺
    @GET("seller/shops")
    @WithHiResponse
    fun getShop(): Call<ApplyShopInfo>

    // 查询身份
    @GET("seller/shops/identity/queryShopIdentity")
    @WithHiResponse
    fun getShopIdentity(): Call<DataVO<IdentityVo>>;

    //根据类型查询分享内容
    @GET("/seller/shops/getShareInfo")
    @WithHiResponse
    fun getShareInfo(
        @Query("channel_type") type: Int,
        @Query("id") id: String,
        @Query("share_type") share_type: Int
    ): Call<ShareVo>

    // 保障金
    @GET("seller/shops/identity/querySaleProductList")
    @WithHiResponse
    fun getShopDeposit(): Call<DataVO<IdentityVo>>;

    // vip充值列表
    @GET("seller/shops/identity/querySaleProductList")
    @WithHiResponse
    fun getVipList(): Call<DataVO<ShopProductVo>>

    // 发起支付
    @GET("seller/shops/identity/{id}")
    @WithHiResponse
    fun getPayInfo(@Path("id") id: String): Call<WxPayReqVo>;

    // 发起充值
    @POST("account/applyRecharge")
    @WithHiResponse
    fun recharge(@Body data: RechargeReqVo): Call<DataVO<WxPayReqVo>>;

    //    检测升级
    @GET("seller/app/upgrade")
    @WithHiResponse
    fun getUpgrade(
        @Query("current_version") version: String,
        @Query("current_version_code") versionCode: String
    ): Call<AccountSetting>



    //查询银行卡
    @WithHiResponse
    @POST("seller/shops/tlCnapsCode/queryBankList")
    fun searchBankCard(
        @Body body: ApplyBank
    ): Call<BankInfo>

    //查询支行
    @WithHiResponse
    @POST("seller/shops/tlCnapsCode/queryTlCnapsCodeList")
    fun searchSubBankCard(
        @Body body: ApplyBank
    ): Call<BankInfo>


    //OCR识别营业执照
    @WithHiResponse
    @POST("seller/app/ocr/ocrDiscern")
    fun readOCRLicense(
        @Body body: OCRDiscern
    ): Call<OCRInfo<License>>

    //OCR识别银行卡
    @WithHiResponse
    @POST("seller/app/ocr/ocrDiscern")
    fun readOCRBankCard(
        @Body body: OCRDiscern
    ): Call<OCRInfo<BankCard>>

    //OCR识别身份证国徽面
    @WithHiResponse
    @POST("seller/app/ocr/ocrDiscern")
    fun readOCRIDCardG(
        @Body body: OCRDiscern
    ): Call<OCRInfo<IDCard>>

    //OCR识别身份证人像面
    @WithHiResponse
    @POST("seller/app/ocr/ocrDiscern")
    fun readOCRIDCardR(
        @Body body: OCRDiscern
    ): Call<OCRInfo<IDCard>>

    //查询已绑定银行卡
    @WithHiResponse
    @POST("account/queryBankCardList")
    fun queryTestBankCard(@Body data: BindBankCardDTO): Call<Unit>

    //绑定银行卡
    @WithHiResponse
    @POST("account/bindBankCardMember")
    fun bindTestBankCard(@Body data: BindBankCardDTO): Call<Unit>


}