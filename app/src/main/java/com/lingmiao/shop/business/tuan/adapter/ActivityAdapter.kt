package com.lingmiao.shop.business.tuan.adapter

import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.lingmiao.shop.R
import com.lingmiao.shop.business.tuan.bean.ActivityVo

class ActivityAdapter : BaseQuickAdapter<ActivityVo, BaseViewHolder>(R.layout.tuan_adapter_activity)  {

    override fun convert(helper: BaseViewHolder, item: ActivityVo?) {
        helper.setText(R.id.tv_activity_name, "活动名称");
        helper.setText(R.id.tv_activity_status_hint, "报名中");
        helper.setText(R.id.tv_activity_start_time, "2020-09-09 00:00");
        helper.setText(R.id.tv_activity_end_time, "2020-09-09 00:00");
        helper.setText(R.id.tv_activity_register_time, "2020-09-09 00:00");

        helper.addOnClickListener(R.id.tv_activity_check);
        helper.addOnClickListener(R.id.tv_activity_status);
    }

}