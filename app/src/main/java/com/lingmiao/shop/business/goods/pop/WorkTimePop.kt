package com.lingmiao.shop.business.goods.pop

import android.content.Context
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.lingmiao.shop.business.commonpop.adapter.DefaultItemAdapter
import com.lingmiao.shop.business.commonpop.pop.AbsDoubleItemPop
import com.lingmiao.shop.business.goods.api.bean.WorkTimeVo

/**
Create Date : 2021/3/107:25 PM
Auther      : Fox
Desc        :
 **/
class WorkTimePop(val context: Context) : AbsDoubleItemPop<WorkTimeVo>(context) {
    override fun getFirstAdapter(): BaseQuickAdapter<WorkTimeVo, BaseViewHolder> {
        return DefaultItemAdapter<WorkTimeVo>().apply {

        }
    }

    override fun getSecondAdapter(): BaseQuickAdapter<WorkTimeVo, BaseViewHolder> {
        return DefaultItemAdapter<WorkTimeVo>().apply {

        }
    }

    override fun getData2(data1: WorkTimeVo): List<WorkTimeVo> {
        return WorkTimeVo.getWorkTimeList(data1?.getIValue(), WorkTimeVo.getWorkTimeList())
    }
}