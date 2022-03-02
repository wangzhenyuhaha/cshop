package com.lingmiao.shop.business.wallet.bean

import java.io.Serializable

class WalletVo(

    /**
     * 押金
     */
    var depositAccount: AccountVo? = null,

    /**
     * 余额
     */
    var balanceAccount: AccountVo? = null,


    //骑手备付金
    var riderDepositAccountVO: AccountVo? = null
) : Serializable {

    fun getDepositAccountId(): String {
        return depositAccount?.id ?: "";
    }

    fun getBalanceAccountId(): String {
        return balanceAccount?.id ?: "";
    }

}