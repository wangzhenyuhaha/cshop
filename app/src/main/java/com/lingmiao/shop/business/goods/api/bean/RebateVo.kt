package com.lingmiao.shop.business.goods.api.bean

import java.io.Serializable

class RebateVo (
    var grade1Rebate: String ?= "",
    var grade2Rebate: String ?= "",
    var inviterRate: String ?= ""
) : Serializable {

}