package com.lingmiao.shop.business.wallet.adapter

import android.widget.TextView
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.lingmiao.shop.R
import com.lingmiao.shop.business.wallet.bean.WithdrawTypeVo
import com.lingmiao.shop.util.drawableRight

/**
Create Date : 2021/6/214:57 PM
Auther      : Fox
Desc        :
 **/
class WithdrawTypeAdapter : BaseQuickAdapter<WithdrawTypeVo, BaseViewHolder>(R.layout.wallet_adapter_withdraw_type) {

    var mCheckPosition = -1;

    /**
     * Implement this method and use the helper to adapt the view to the given item.
     *
     * @param helper A fully initialized helper.
     * @param item   The item that needs to be displayed.
     */
    override fun convert(helper: BaseViewHolder, item: WithdrawTypeVo?) {
        helper.setText(R.id.tvTypeHint, String.format("提现到%s", item?.hint));
        helper.addOnClickListener(R.id.tvTypeHint);
        helper.getView<TextView>(R.id.tvTypeHint).drawableRight(if(mCheckPosition == helper.adapterPosition) R.mipmap.goods_checked else 0);
    }

    fun shiftCheckPosition(position : Int) {
        mCheckPosition = position;
    }

}