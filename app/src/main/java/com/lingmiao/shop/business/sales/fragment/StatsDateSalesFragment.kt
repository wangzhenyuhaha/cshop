package com.lingmiao.shop.business.sales.fragment

import StatsSalesVo
import android.os.Bundle
import android.view.View
import com.bigkoo.pickerview.view.TimePickerView
import com.blankj.utilcode.util.GsonUtils
import com.github.aachartmodel.aainfographics.aachartcreator.*
import com.james.common.base.BaseFragment
import com.james.common.utils.exts.getViewText
import com.james.common.utils.exts.singleClick
import com.lingmiao.shop.R
import com.lingmiao.shop.business.sales.presenter.IStateSalesDataPresenter
import com.lingmiao.shop.business.sales.presenter.impl.StatsSalesDataPreImpl
import com.lingmiao.shop.util.*
import kotlinx.android.synthetic.main.sales_fragment_stats_pay.*
import kotlinx.android.synthetic.main.sales_fragment_stats_pay.aaChartView
import kotlinx.android.synthetic.main.sales_fragment_stats_pay.dateEndTv
import kotlinx.android.synthetic.main.sales_fragment_stats_pay.dateStartTv
import kotlinx.android.synthetic.main.sales_fragment_stats_sales.*
import java.util.*

/**
Create Date : 2021/3/101:21 AM
Auther      : Fox
Desc        : 销售
 **/
class StatsDateSalesFragment : BaseFragment<IStateSalesDataPresenter>(), IStateSalesDataPresenter.PubView {

    private var aaChartModel = AAChartModel()

    var pvCustomTime: TimePickerView? = null;
    var pvCustomTime2: TimePickerView? = null;
    var mStart : Long? = null;
    var mEnd : Long? = null;

    lateinit var endDate: Calendar;
    lateinit var startDate: Calendar;

    lateinit var selectedDate: Calendar

    companion object {

        fun new(): StatsDateSalesFragment {
            return newInstance(1);
        }

        fun newInstance(status: Int): StatsDateSalesFragment {
            return StatsDateSalesFragment().apply {
                arguments = Bundle().apply {
//                    putInt(UserStatusFragment.KEY_TYPE, status)
                }
            }
        }
    }

    override fun getLayoutId(): Int? {
        return R.layout.sales_fragment_stats_sales
    }

    override fun createPresenter(): IStateSalesDataPresenter? {
        return StatsSalesDataPreImpl(context!!, this);
    }

    override fun initViewsAndData(rootView: View) {
        initChartView();
        initDate();
    }


    fun initChartView() {
        aaChartView?.setBackgroundColor(0)
        aaChartView?.background?.alpha = 0
        aaChartView?.callBack = click
        aaChartModel = configureAAChartModel()

//        val aaOptions = configureTheChartOptions();
        aaChartView?.aa_drawChartWithChartModel(aaChartModel)
//        aaChartView.aa_drawChartWithChartOptions(aaOptions)
    }


    private fun configureAAChartModel(): AAChartModel {
        val aaChartModel = AAChartModel()
            .chartType(AAChartType.Column)
//            .title("2020年")
            .subtitle("")
            .subtitleAlign(AAChartAlignType.Right)
            .markerSymbolStyle(AAChartSymbolStyleType.BorderBlank)
            .backgroundColor("#fff")
            .dataLabelsEnabled(false)
            .xAxisVisible(true)
            .yAxisTitle("订单量")
            .yAxisGridLineWidth(0f)
//            .scrollablePlotArea(
//                AAScrollablePlotArea()
//                    .minWidth(500)
//                    .scrollPositionX(1f))
            .legendEnabled(true)
            .touchEventEnabled(true)
            .categories(arrayOf( "一月", "二月", "三月", "四月", "五月", "六月",
                "七月", "八月", "九月", "十月", "十一月", "十二月"))
            .series(arrayOf(
                AASeriesElement()
                    .name("订单量")
                    .step(true)
                    .data(arrayOf(17.0, 26.9, 39.5, 144.5, 118.2, 121.5, 75.2, 86.5, 93.3, 48.3, 113.9, 29.6)),
                AASeriesElement()
                    .name("销售量")
                    .data(arrayOf(0.2, 0.8, 5.7, 11.3, 17.0, 22.0, 24.8, 24.1, 20.1, 14.1, 8.6, 2.5))
            ))

        configureColumnChartAndBarChartStyle()

        return aaChartModel
    }

    private fun configureYearChartModel(): AAChartModel {
        var categoryList = arrayOf<String>();//arrayOf( "一月", "二月", "三月", "四月", "五月", "六月", "七月", "八月", "九月", "十月", "十一月", "十二月");
        var dataList : Array<Any> = arrayOf(17.0, 26.9, 39.5, 144.5, 118.2, 121.5, 75.2, 86.5, 93.3, 48.3, 113.9, 29.6);
        var salesList : Array<Any> = arrayOf(0.2, 0.8, 5.7, 11.3, 17.0, 22.0, 24.8, 24.1, 20.1, 14.1, 8.6, 2.5);


        if(btn1.isChecked) {
            val firstYear  = selectedDate.get(Calendar.YEAR);
            val secondYear  = selectedDate.get(Calendar.YEAR);
            var i = 0;
            for (index in firstYear until secondYear){
                categoryList[i++] = ""+firstYear;
            }
        } else if(btn2.isChecked) {
            categoryList = arrayOf( "一月", "二月", "三月", "四月", "五月", "六月", "七月", "八月", "九月", "十月", "十一月", "十二月");
        } else {

        }

        val aaChartModel = AAChartModel()
            .chartType(AAChartType.Column)
            .subtitle("")
            .subtitleAlign(AAChartAlignType.Right)
            .markerSymbolStyle(AAChartSymbolStyleType.BorderBlank)
            .backgroundColor("#fff")
            .dataLabelsEnabled(false)
            .xAxisVisible(true)
            .yAxisTitle("订单量")
            .yAxisGridLineWidth(0f)
//            .scrollablePlotArea(
//                AAScrollablePlotArea()
//                    .minWidth(500)
//                    .scrollPositionX(1f))
            .legendEnabled(true)
            .touchEventEnabled(true)
            .categories(categoryList)
            .series(arrayOf(
                AASeriesElement()
                    .name("订单量")
                    .step(true)
                    .data(dataList),
                AASeriesElement()
                    .name("销售量")
                    .data(salesList)
            ))

        configureColumnChartAndBarChartStyle()

        return aaChartModel
    }



    private fun configureColumnChartAndBarChartStyle() {
        aaChartModel
            .categories(arrayOf("Jan", "Feb", "Mar", "Apr", "May", "Jun", "July", "Aug", "Spe", "Oct", "Nov", "Dec"))
            .colorsTheme(arrayOf("#fe117c", "#ffc069"))
            .animationType(AAChartAnimationType.EaseInCubic)
            .animationDuration(1200)
    }

    val click = object : AAChartView.AAChartViewCallBack {

        override fun chartViewDidFinishLoad(aaChartView: AAChartView) {
            println("🔥图表加载完成回调方法 ")
        }

        override fun chartViewMoveOverEventMessage(
            aaChartView: AAChartView,
            messageModel: AAMoveOverEventMessageModel
        ) {
            println("🚀move over event message " + GsonUtils.toJson(messageModel))
        }
    };


    private var mTabTitles = arrayOf("销售TOP10", "滞销TOP10")

    fun getCheckType() : Int {
        if(btn1.isChecked) {
            return 1;
        } else if(btn2.isChecked) {
            return 2;
        } else {
            return 3;
        }
    }


    fun getCheckTypeStr() : String {
        if(btn1.isChecked) {
            return "YEAR";
        } else if(btn2.isChecked) {
            return "MONTH";
        } else {
            return "DAY";
        }
    }


    fun getCheckStartTimeStr() : String {
        if(btn1.isChecked) {
            return "-01-01 00:00:00";
        } else if(btn2.isChecked) {
            return "-01 00:00:00";
        }  else {
            return " 00:00:00";
        }
    }

    fun getCheckEndTimeStr() : String {
        if(btn1.isChecked) {
            return "-01-01 23:59:59";
        } else if(btn2.isChecked) {
            return "-01 23:59:59";
        } else {
            return " 23:59:59";
        }
    }

    fun getCheckTimeFormat() : String {
        if(btn1.isChecked) {
            return "yyyy";
        } else if(btn2.isChecked) {
            return "yyyy-MM";
        } else {
            return DATE_FORMAT;
        }
    }


    fun initDate() {
        // 系统当前时间
        selectedDate = Calendar.getInstance()
        startDate = Calendar.getInstance()
        startDate.set(selectedDate.get(Calendar.YEAR), 1, 1)

        endDate = Calendar.getInstance()
        endDate.set(
            startDate.get(Calendar.YEAR) + 5, startDate.get(Calendar.MONTH), startDate.get(
                Calendar.DATE
            )
        )

        mStart = startDate.timeInMillis/1000;
        mEnd = endDate.timeInMillis/1000;
        mPresenter?.getSalesCount("YEAR",mStart, mEnd);

        toggleGroup.addOnButtonCheckedListener { group, checkedId, isChecked ->
            resetData(getCheckTypeStr());
        }

        dateStartTv.singleClick{


            pvCustomTime = getDatePicker(mContext, selectedDate, startDate, endDate, getCheckType(), { date, view ->
                dateStartTv.text = formatString(date, getCheckTimeFormat())

                val s = dateTime2Date(dateStartTv.getViewText()+getCheckStartTimeStr())?.time?:0;
                mStart = s/1000;

                if(mStart?:0 > 0 && mEnd?:0 > 0) {
                    mPresenter?.getSalesCount(getCheckTypeStr(), mStart, mEnd);
                }
            }, {
                pvCustomTime?.returnData()
                pvCustomTime?.dismiss()
            }, {
                pvCustomTime?.dismiss()
            });
            pvCustomTime?.show();
        }
        dateEndTv.singleClick{
            pvCustomTime2 =
                getDatePicker(mContext, selectedDate, startDate, endDate, getCheckType(), { date, view ->
                    dateEndTv.setText(formatString(date, getCheckTimeFormat()))
                    val e = dateTime2Date(dateEndTv.getViewText()+getCheckEndTimeStr())?.time?:0;
                    mEnd = e/1000;

                    if(mStart?:0 > 0 && mEnd?:0 > 0) {
                        mPresenter?.getSalesCount(getCheckTypeStr(), mStart, mEnd);
                        //mLoadMoreDelegate?.refresh()
                    }
                }, {
                    pvCustomTime2?.returnData()
                    pvCustomTime2?.dismiss()
                }, {
                    pvCustomTime2?.dismiss()
                });
            pvCustomTime2?.show();
        }

        resetData(getCheckTypeStr());
    }

    fun resetData(type : String) {
        dateStartTv.text = formatString(selectedDate.time, getCheckTimeFormat())

        val s = dateTime2Date(dateStartTv.getViewText()+getCheckStartTimeStr())?.time?:0;
        mStart = s/1000;

        if(mStart?:0 > 0 && mEnd?:0 > 0) {
            mPresenter?.getSalesCount(type, mStart, mEnd);
        }

        dateEndTv.setText(formatString(endDate.time, getCheckTimeFormat()))
        val e = dateTime2Date(dateEndTv.getViewText()+getCheckEndTimeStr())?.time?:0;
        mEnd = e/1000;

        if(mStart?:0 > 0 && mEnd?:0 > 0) {
            mPresenter?.getSalesCount(type, mStart, mEnd);
        }
    }

    override fun setSalesCount(item : StatsSalesVo?) {
        salesOrderCountTv.setText(String.format("%s", item?.orderNum?:0))
        salesAmountCountTv.setText(formatDouble(item?.orderPrice?:0.0))
    }
}