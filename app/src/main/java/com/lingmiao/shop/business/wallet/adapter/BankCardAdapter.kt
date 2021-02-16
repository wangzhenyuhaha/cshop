package com.lingmiao.shop.business.wallet.adapter

import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.lingmiao.shop.R
import com.lingmiao.shop.business.wallet.bean.BankCardAccountBean

class BankCardAdapter :
    BaseQuickAdapter<BankCardAccountBean, BaseViewHolder>(R.layout.wallet_adapter_bank_card) {

    override fun convert(helper: BaseViewHolder, item: BankCardAccountBean?) {
        helper.setText(R.id.tv_bankcard_type, item?.getBankTypeName())
        helper.setText(R.id.tv_account_name, item?.openAccountName);
        helper.setText(R.id.tv_bank_name, item?.bankName);
        helper.setText(R.id.tv_bank_no, item?.cardNo);
        helper.setGone(R.id.tv_bankcard_un_bind, itemCount > 1);
        helper.addOnClickListener(R.id.tv_bankcard_un_bind);
        helper.addOnClickListener(R.id.tv_bankcard_edit);
    }
}