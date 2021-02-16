package com.lingmiao.shop.business.wallet.bean

import java.io.Serializable

class WalletVo(

    var depositAccount: AccountVo? = null,

    var balanceAccount: AccountVo? = null
) : Serializable {

    fun getDepositAccountId(): String {
        return depositAccount?.id ?: "";
    }

    fun getBalanceAccountId(): String {
        return balanceAccount?.id ?: "";
    }

}