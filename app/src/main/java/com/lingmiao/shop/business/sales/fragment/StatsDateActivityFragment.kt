package com.lingmiao.shop.business.sales.fragment

import android.os.Bundle
import android.view.View
import com.blankj.utilcode.util.GsonUtils
import com.github.aachartmodel.aainfographics.aachartcreator.*
import com.james.common.base.BaseFragment
import com.lingmiao.shop.R
import com.lingmiao.shop.business.sales.presenter.IStateActivityDataPresenter
import com.lingmiao.shop.business.sales.presenter.IStateGoodsDataPresenter
import com.lingmiao.shop.business.sales.presenter.impl.StatsActivityDataPreImpl
import kotlinx.android.synthetic.main.sales_fragment_stats_activity.*

/**
Create Date : 2021/3/101:21 AM
Auther      : Fox
Desc        :
 **/
class StatsDateActivityFragment  : BaseFragment<IStateActivityDataPresenter>(), IStateActivityDataPresenter.PubView {

    private var aaChartModel = AAChartModel()

    companion object {

        fun new(): StatsDateActivityFragment {
            return newInstance(1);
        }

        fun newInstance(status: Int): StatsDateActivityFragment {
            return StatsDateActivityFragment().apply {
                arguments = Bundle().apply {
//                    putInt(UserStatusFragment.KEY_TYPE, status)
                }
            }
        }
    }

    override fun getLayoutId(): Int? {
        return R.layout.sales_fragment_stats_activity;
    }

    override fun createPresenter(): IStateActivityDataPresenter? {
        return StatsActivityDataPreImpl(context!!, this);
    }

    override fun initViewsAndData(rootView: View) {
        initChartView();

    }


    fun initChartView() {
        aaChartView?.setBackgroundColor(0)
        aaChartView?.background?.alpha = 0
        aaChartView?.callBack = click
        aaChartModel = configurePieChart()

//        val aaOptions = configureTheChartOptions();
        aaChartView?.aa_drawChartWithChartModel(aaChartModel)
//        aaChartView.aa_drawChartWithChartOptions(aaOptions)
    }

    fun configurePieChart(): AAChartModel {
        return AAChartModel()
            .chartType(AAChartType.Pie)
            .backgroundColor("#ffffff")
//            .title("活动商品/总商品")
//            .subtitle("virtual data")
            .dataLabelsEnabled(true)//是否直接显示扇形图数据
            .yAxisTitle("℃")
            .series(arrayOf(
                AASeriesElement()
                    .name("")
                    .data(arrayOf(
                        arrayOf("活动商品",   67),
                        arrayOf("总商品", 999)
//                        ,
//                        arrayOf("Python", 83),
//                        arrayOf("OC",     11),
//                        arrayOf("Go",     30)
                    ))))
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