package com.lingmiao.shop.business.sales.fragment

import android.os.Bundle
import android.view.View
import com.blankj.utilcode.util.GsonUtils
import com.github.aachartmodel.aainfographics.aachartcreator.*
import com.james.common.base.BaseFragment
import com.lingmiao.shop.R
import com.lingmiao.shop.business.sales.bean.GoodsSalesRespBean
import com.lingmiao.shop.business.sales.presenter.IStateGoodsDataPresenter
import com.lingmiao.shop.business.sales.presenter.impl.StatsGoodsDataPreImpl
import kotlinx.android.synthetic.main.sales_fragment_stats_pay.aaChartView
import kotlinx.android.synthetic.main.sales_fragment_stats_user.*

/**
Create Date : 2021/3/101:21 AM
Auther      : Fox
Desc        :
 **/
class StatsDateUserFragment : BaseFragment<IStateGoodsDataPresenter>(), IStateGoodsDataPresenter.PubView {

    private var aaChartModel = AAChartModel()

    companion object {

        fun new(): StatsDateUserFragment {
            return newInstance(1);
        }

        fun newInstance(status: Int): StatsDateUserFragment {
            return StatsDateUserFragment().apply {
                arguments = Bundle().apply {
//                    putInt(UserStatusFragment.KEY_TYPE, status)
                }
            }
        }
    }

    override fun getLayoutId(): Int? {
        return R.layout.sales_fragment_stats_user;
    }

    override fun createPresenter(): IStateGoodsDataPresenter? {
        return StatsGoodsDataPreImpl(context!!, this);
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

        buyChart?.aa_drawChartWithChartModel(aaChartModel);
//        aaChartView.aa_drawChartWithChartOptions(aaOptions)
    }

    fun configurePieChart(): AAChartModel {
        return AAChartModel()
            .chartType(AAChartType.Pie)
            .backgroundColor("#ffffff")
//            .title("用户来源分析")
//            .subtitle("virtual data")
            .dataLabelsEnabled(true)//是否直接显示扇形图数据
//            .yAxisTitle("℃")
            .series(arrayOf(
                AASeriesElement()
                    .name("数量")
                    .data(arrayOf(
                        arrayOf("公号引流",   67),
                        arrayOf("官方注册", 999),
                        arrayOf("小程序注册", 83),
                        arrayOf("广告",     11),
                        arrayOf("其他",     30)
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

    override fun setGoodsSales(item: GoodsSalesRespBean?) {

    }
}