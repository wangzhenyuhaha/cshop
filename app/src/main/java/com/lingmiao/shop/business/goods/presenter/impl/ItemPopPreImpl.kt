package com.lingmiao.shop.business.goods.presenter.impl

import android.content.Context
import android.util.Log
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

    fun showWorkTimePop(
        context: Context,
        value: String?,
        callback: (WorkTimeVo?, WorkTimeVo?) -> Unit
    ) {
        mTwoItemPop = object : AbsDoubleItemPop<WorkTimeVo>(context) {
            override fun getFirstAdapter(): BaseQuickAdapter<WorkTimeVo, BaseViewHolder> {
                return DefaultItemAdapter<WorkTimeVo>().apply {

                }
            }

            override fun getSecondAdapter(): BaseQuickAdapter<WorkTimeVo, BaseViewHolder> {
                return DefaultItemAdapter<WorkTimeVo>().apply {

                }
            }

            override fun getData2(data1: WorkTimeVo): List<WorkTimeVo> {

                var time1: String? = data1.itemName
                val list =
                    WorkTimeVo.getWorkTimeList(data1?.getIValue(), WorkTimeVo.getWorkTimeList())

                val newList: MutableList<WorkTimeVo> = mutableListOf()


                for (it in list) {

                    var temp: String? = null

                    if (time1?.substring(0, 2)?.toInt()!! > it.itemName?.substring(0, 2)
                            ?.toInt()!!
                    ) {
                        //第二天
                        temp = "第二天${it.itemName}"
                    }
                    if (time1?.substring(0, 2)?.toInt()!! == it.itemName?.substring(0, 2)
                            ?.toInt()!!
                    ) {
                        temp = if (time1?.substring(3, 5)?.toInt()!! >= it.itemName?.substring(3, 5)
                                ?.toInt()!!
                        ) {
                            //第二天
                            "第二天${it.itemName}"
                        } else {
                            it.itemName
                        }
                    }
                    if (time1?.substring(0, 2)?.toInt()!! < it.itemName?.substring(0, 2)
                            ?.toInt()!!
                    ) {
                        temp = it.itemName
                    }

                    it.itemName = temp
                    newList?.add(it)

                }
                val long = newList.size
                val newList2 = newList.subList(0, long - 1)


                var num: Int = 0

                for (it in newList2) {
                    if (it.itemName?.startsWith("第") == true) {
                        num++
                    }
                }

                val aList = newList2.subList(0, num)
                val bList = newList2.subList(num, newList2.size)

                return bList + aList
            }
        }.apply {


            lv1Callback = {

                l1Data = it;
            }
            lv2Callback = {
                l2Data = it;
                callback.invoke(l1Data, it);
            }
        }
        mTwoItemPop?.setPopTitle("请设置营业时间");
        val list = WorkTimeVo.getWorkTimeList()
        val long = list.size
        mTwoItemPop?.setLv1Data(list.subList(0, long - 1))
        mTwoItemPop?.showPopupWindow();
    }

    override fun onDestroy() {
        mTwoItemPop?.dismiss()
        mTwoItemPop = null;
        super.onDestroy()
    }
}