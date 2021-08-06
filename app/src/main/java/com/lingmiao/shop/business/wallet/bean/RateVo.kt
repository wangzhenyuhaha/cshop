package com.lingmiao.shop.business.wallet.bean

/**
Create Date : 2021/8/36:35 下午
Auther      : Fox
Desc        :
 **/
class RateVo {
    var rate: Double? = 0.0;

    fun getRatePercentage() : Double {
        if(rate == null) {
            return 0.0;
        } else {
            return 100 * rate!!;
        }
    }
}