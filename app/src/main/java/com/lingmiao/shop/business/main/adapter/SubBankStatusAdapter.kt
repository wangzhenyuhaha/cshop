package com.lingmiao.shop.business.main.adapter

import android.annotation.SuppressLint
import androidx.annotation.LayoutRes
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.lingmiao.shop.R
import com.lingmiao.shop.business.main.bean.BankDetail

open class SubBankStatusAdapter(
    @LayoutRes layout: Int = R.layout.main_adapter_apply_info
) :
    BaseQuickAdapter<BankDetail, BaseViewHolder>(layout) {


    @SuppressLint("StringFormatMatches")
    override fun convert(helper: BaseViewHolder, bank: BankDetail?) {
    }


}
