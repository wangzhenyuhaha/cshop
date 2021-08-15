package com.lingmiao.shop.business.main.adapter

import android.annotation.SuppressLint
import com.chad.library.adapter.base.BaseViewHolder
import com.lingmiao.shop.R
import com.lingmiao.shop.business.main.bean.BankDetail


class SubBankSearchAdapter : SubBankStatusAdapter(R.layout.main_adapter_apply_info) {

    @SuppressLint("StringFormatMatches")
    override fun convert(helper: BaseViewHolder, bank: BankDetail?) {
        super.convert(helper, bank)
        bank?.apply {
            helper.setText(R.id.main_adapter_apply_info_textView, bafName)
        }
    }


}



class SubBankTestSearchAdapter : SubBankStatusAdapter(R.layout.main_adapter_apply_info) {

    @SuppressLint("StringFormatMatches")
    override fun convert(helper: BaseViewHolder, bank: BankDetail?) {
        super.convert(helper, bank)
        bank?.apply {
            helper.setText(R.id.main_adapter_apply_info_textView, bankName)
        }
    }



}