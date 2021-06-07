package com.lingmiao.shop.business.order.bean

import org.jetbrains.annotations.NotNull

/**
Create Date : 2021/6/63:08 PM
Auther      : Fox
Desc        :
 **/
class OrderServiceVo {

    /**
     * 退款单号必填
     */
    val sn: String? = null

    /**
     * 是否同意退款:同意 1，不同意 0
     */
    val agree: Int? = null

    /**
     * 退款金额
     */
    val refundPrice: Double? = null

    /**
     * 退款备注
     */
    val remark: String? = null

    /**
     * 退还积分
     */
    val refundPoint: Int? = null
}