package com.lingmiao.shop.business.sales.fragment

import android.os.Bundle
import android.view.View
import com.blankj.utilcode.util.GsonUtils
import com.github.aachartmodel.aainfographics.aachartcreator.*
import com.james.common.base.BaseFragment
import com.lingmiao.shop.R
import com.lingmiao.shop.business.sales.presenter.IStateSalesDataPresenter
import com.lingmiao.shop.business.sales.presenter.impl.StatsSalesDataPreImpl
import kotlinx.android.synthetic.main.sales_fragment_stats_pay.*

/**
Create Date : 2021/3/101:21 AM
Auther      : Fox
Desc        : 销售
 **/
class StatsDateSalesFragment : BaseFragment<IStateSalesDataPresenter>(), IStateSalesDataPresenter.PubView {

    private var aaChartModel = AAChartModel()

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
            .title("2020年")
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

//        val aaYAxisLabels = AALabels()
//            .formatter("""
//function () {
//        var yValue = this.value;
//        if (yValue >= 200) {
//            return "极佳";
//        } else if (yValue >= 150 && yValue < 200) {
//            return "非常棒";
//        } else if (yValue >= 100 && yValue < 150) {
//            return "相当棒";
//        } else if (yValue >= 50 && yValue < 100) {
//            return "还不错";
//        } else {
//            return "一般";
//        }
//    }
//                """.trimIndent()
//            )
//
//        val aaOptions = aaChartModel.aa_toAAOptions()
//        aaOptions.xAxis?.labels(aaYAxisLabels)

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
}