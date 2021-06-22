package com.lingmiao.shop.business.wallet.bean

import java.io.Serializable

/**
Create Date : 2021/6/214:58 PM
Auther      : Fox
Desc        :
 **/
class WithdrawTypeVo : Serializable {
    var type: Int =  0;
    var hint : String = "";

    companion object {

        fun getWechat() : WithdrawTypeVo{
            var item = WithdrawTypeVo();
            item.type = IPayConstants.CHANNEL_WECHAT;
            item.hint = "微信"
            return item;
        }

        fun getAli() : WithdrawTypeVo{
            var item = WithdrawTypeVo();
            item.type = IPayConstants.CHANNEL_ALI;
            item.hint = "支付宝"
            return item;
        }

        fun getCard() : WithdrawTypeVo{
            var item = WithdrawTypeVo();
            item.type = IPayConstants.CHANNEL_CARD;
            item.hint = "银行卡"
            return item;
        }

    }
}