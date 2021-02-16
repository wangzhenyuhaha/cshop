package com.lingmiao.shop.business.wallet.api

import com.lingmiao.shop.R

object WalletConstants {
    /**
     * 传递的不同类型
     */
    const val KEY_VIEW_TYPE = "KEY_VIEW_TYPE";

    /**
     * 传递的内容项
     */
    const val KEY_ITEM = "KEY_ITEM";

    /**
     * 余额账户id
     */
    const val BALANCE_ACCOUNT_ID : String = "BALANCE_ACCOUNT_ID"

    /**
     * 押金账户id
     */
    const val DEPOSIT_ACCOUNT_ID : String = "DEPOSIT_ACCOUNT_ID"

    /**
     * 个人账户
     */
    const val PUBLIC_PRIVATE = 0;
    /**
     * 企业账户
     */
    const val PUBLIC_COMPANY = 1;

    const val PUBLIC_PRIVATE_NAME = "个人账户";
    const val PUBLIC_COMPANY_NAME = "企业账户";

    const val CHECKED = R.mipmap.goods_spec_checked;
    const val UNCHECK = R.mipmap.goods_spec_uncheck;

}