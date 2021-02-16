package com.lingmiao.shop.business.wallet.adapter

import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.lingmiao.shop.R
import com.lingmiao.shop.business.wallet.bean.DepositVo

class DepositAdapter :
    BaseQuickAdapter<DepositVo, BaseViewHolder>(R.layout.wallet_adapter_deposit) {
    override fun convert(helper: BaseViewHolder, item: DepositVo?) {
        helper.setText(R.id.tv_wallet_deposit_item_type, item?.tradeTypeName);
        helper.setText(R.id.tv_wallet_deposit_item_date, item?.tradeTime);
        helper.setText(R.id.tv_wallet_deposit_item_amount, String.format("%s", item?.amount));
    }
}