package com.lingmiao.shop.business.sales.fragment

import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.blankj.utilcode.util.GsonUtils
import com.github.aachartmodel.aainfographics.aachartcreator.*
import com.james.common.base.BaseFragment
import com.lingmiao.shop.R
import com.lingmiao.shop.business.sales.adapter.PayAdapter
import com.lingmiao.shop.business.sales.bean.PayItem
import com.lingmiao.shop.business.sales.presenter.IStatePayDataPresenter
import com.lingmiao.shop.business.sales.presenter.impl.StatsPayDataPreImpl
import kotlinx.android.synthetic.main.sales_fragment_stats_pay.*


/**
Create Date : 2021/3/101:21 AM
Auther      : Fox
Desc        :
 **/
class StatsDatePayFragment : BaseFragment<IStatePayDataPresenter>(), IStatePayDataPresenter.PubView {

    private var aaChartModel = AAChartModel()

    companion object {

        fun new(): StatsDatePayFragment {
            return newInstance(1);
        }

        fun newInstance(status: Int): StatsDatePayFragment {
            return StatsDatePayFragment().apply {
                arguments = Bundle().apply {
//                    putInt(UserStatusFragment.KEY_TYPE, status)
                }
            }
        }
    }

    override fun getLayoutId(): Int? {
        return R.layout.sales_fragment_stats_pay
    }

    override fun createPresenter(): IStatePayDataPresenter? {
        return StatsPayDataPreImpl(context!!, this);
    }

    override fun initViewsAndData(rootView: View) {
        initPayList();

        initChartView();
    }

    fun initPayList() {
        val list : MutableList<PayItem> = mutableListOf();

        var title = PayItem();
        title.type = PayItem.TYPE_TITLE;
        list.add(title)

        var hint = PayItem();
        hint.type = PayItem.TYPE_IN
        list.add(hint)

        var item = PayItem();
        item.type = PayItem.TYPE_IN_ITEM
        list.add(item)
        item = PayItem();
        item.type = PayItem.TYPE_IN_ITEM
        list.add(item)
        item = PayItem();
        item.type = PayItem.TYPE_IN_ITEM
        list.add(item)

        hint = PayItem();
        hint.type = PayItem.TYPE_OUT
        list.add(hint)

        item = PayItem();
        item.type = PayItem.TYPE_OUT_ITEM
        list.add(item)
        item = PayItem();
        item.type = PayItem.TYPE_OUT_ITEM
        list.add(item)

        var lvAdapter = PayAdapter(list).apply {

        };
        payListRv.apply {
            adapter = lvAdapter
            layoutManager = LinearLayoutManager(context)
        }
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
//            .title("用户来源分析")
//            .subtitle("virtual data")
            .dataLabelsEnabled(true)//是否直接显示扇形图数据
//            .yAxisTitle("℃")
            .series(arrayOf(
                AASeriesElement()
                    .name("金额")
                    .data(arrayOf(
                        arrayOf("收入",   500),
                        arrayOf("支出", 500)
                    ))))
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