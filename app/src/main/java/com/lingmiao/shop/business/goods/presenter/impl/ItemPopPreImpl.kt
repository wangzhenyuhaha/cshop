package com.lingmiao.shop.business.goods.presenter.impl

import android.content.Context
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.james.common.base.BasePreImpl
import com.james.common.base.BaseView
import com.lingmiao.shop.business.commonpop.adapter.DefaultItemAdapter
import com.lingmiao.shop.business.commonpop.pop.AbsDoubleItemPop
import com.lingmiao.shop.business.goods.api.bean.WorkTimeVo

/**
Create Date : 2021/3/74:52 PM
Auther      : Fox
Desc        :
 **/
class ItemPopPreImpl(view: BaseView) : BasePreImpl(view) {

    private var mTwoItemPop: AbsDoubleItemPop<WorkTimeVo>? = null;

    private var l1Data: WorkTimeVo? = null;

    private var l2Data: WorkTimeVo? = null;

    lateinit var mL1Adapter : DefaultItemAdapter<WorkTimeVo>;
    lateinit var mL2Adapter : DefaultItemAdapter<WorkTimeVo>;

    fun showWorkTimePop(
        context: Context,
        value: String?,
        callback: (WorkTimeVo?, WorkTimeVo?) -> Unit
    ) {
        mL1Adapter = DefaultItemAdapter<WorkTimeVo>().apply {

        };
        mL2Adapter = DefaultItemAdapter<WorkTimeVo>().apply {

        };
        mTwoItemPop = object : AbsDoubleItemPop<WorkTimeVo>(context) {
            override fun getFirstAdapter(): BaseQuickAdapter<WorkTimeVo, BaseViewHolder> {
                return mL1Adapter;
            }

            override fun getSecondAdapter(): BaseQuickAdapter<WorkTimeVo, BaseViewHolder> {
                return mL2Adapter;
            }

            override fun getData2(data1: WorkTimeVo): List<WorkTimeVo> {
                return WorkTimeVo.getWorkTimeList(data1, WorkTimeVo.getWorkTimeList())
            }
        }.apply {
            lv1Callback = {
                l1Data = it;
                mL1Adapter.setSelectedItem(it.itemValue);
            }
            lv2Callback = {
                l2Data = it;
                callback.invoke(l1Data, it);
            }
        }
        mTwoItemPop?.setPopTitle("请设置营业时间");
        mTwoItemPop?.setLv1Data(WorkTimeVo.getWorkTimeList())
        mTwoItemPop?.showPopupWindow();
    }

    override fun onDestroy() {
        mTwoItemPop?.dismiss()
        mTwoItemPop = null;
        super.onDestroy()
    }
}