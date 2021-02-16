package com.lingmiao.shop.business.wallet.event

import com.lingmiao.shop.business.wallet.WalletInfoActivity

class RefreshListEvent(val type: Int = 0) {

    fun isRefreshDepositList() : Boolean{
        return type === WalletInfoActivity.VALUE_DEPOSIT;
    }

    fun isRefreshBalanceList() : Boolean {
        return type === WalletInfoActivity.VALUE_BALANCE;
    }
}