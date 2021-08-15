package com.lingmiao.shop.base

/**
Create Date : 2021/8/135:15 下午
Auther      : Fox
Desc        :
 **/
object ShopStatusConstants {
    /**
     * 未开店
     */
    const val UN_APPLY = "UN_APPLY";

    /**
     * 申请开店
     */
    const val APPLY = "APPLY";

    /**
     * 申请中(系统)
     */
    const val APPLYING = "APPLYING";
    /**
     * 开启中(系统)
     */
    const val OPEN = "OPEN";
    /**
     * 审核拒绝(系统)
     */
    const val REFUSED = "REFUSED";

    /**
     * 申请中(通联支付)
     */
    const val ALLINPAY_APPLYING = "ALLINPAY_APPLYING";

    /**
     * 开启中(通联支付)
     */
    const val ALLINPAY_APPROVED = "ALLINPAY_APPROVED";
    /**
     * 审核拒绝(通联支付),进见失败
     */
    const val ALLINPAY_REFUSED = "ALLINPAY_REFUSED";

    /**
     * 通联签约成功,最终的状态
     */
    const val ALLINPAY_ELECTSIGN_APPROVED = "ALLINPAY_ELECTSIGN_APPROVED";

    /**
     * 通联签约失败
     */
    const val ALLINPAY_ELECTSIGN_REFUSED = "ALLINPAY_ELECTSIGN_REFUSED";

    /**
     * 店铺关闭
     */
    const val CLOSED = "CLOSED";

    /**
     * 店铺已过期
     */
    const val OVERDUE = "OVERDUE";
}