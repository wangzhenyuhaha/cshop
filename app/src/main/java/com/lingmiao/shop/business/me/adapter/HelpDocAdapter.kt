package com.lingmiao.shop.business.me.adapter

import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.lingmiao.shop.R
import com.lingmiao.shop.business.me.bean.HelpDocItemVo

/**
Create Date : 2021/8/1011:37 下午
Auther      : Fox
Desc        :
 **/
class HelpDocAdapter : BaseQuickAdapter<HelpDocItemVo, BaseViewHolder>(R.layout.me_adapter_help_doc) {

    /**
     * Implement this method and use the helper to adapt the view to the given item.
     *
     * @param helper A fully initialized helper.
     * @param item   The item that needs to be displayed.
     */
    override fun convert(helper: BaseViewHolder, item: HelpDocItemVo?) {
        helper.setText(R.id.tvDocTitle, item?.title);
        helper.setText(R.id.tvDocContent, item?.content);
    }
}