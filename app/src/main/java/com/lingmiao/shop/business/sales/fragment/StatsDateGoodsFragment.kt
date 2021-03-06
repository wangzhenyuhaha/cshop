package com.lingmiao.shop.business.sales.fragment

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import com.bigkoo.pickerview.view.TimePickerView
import com.github.aachartmodel.aainfographics.aachartcreator.*
import com.james.common.base.BaseFragment
import com.james.common.utils.exts.getViewText
import com.james.common.utils.exts.singleClick
import com.lingmiao.shop.R
import com.lingmiao.shop.business.goods.adapter.GoodsHomePageAdapter
import com.lingmiao.shop.business.sales.bean.CategorySales
import com.lingmiao.shop.business.sales.bean.GoodsSalesRespBean
import com.lingmiao.shop.business.sales.bean.SalesGoodsTop10
import com.lingmiao.shop.business.sales.presenter.IStateGoodsDataPresenter
import com.lingmiao.shop.business.sales.presenter.impl.StatsGoodsDataPreImpl
import com.lingmiao.shop.util.DATE_FORMAT
import com.lingmiao.shop.util.dateTime2Date
import com.lingmiao.shop.util.formatString
import com.lingmiao.shop.util.getDatePicker
import kotlinx.android.synthetic.main.sales_fragment_stats_goods.*
import java.util.*

/**
Create Date : 2021/3/101:21 AM
Auther      : Fox
Desc        :
 **/
class StatsDateGoodsFragment : BaseFragment<IStateGoodsDataPresenter>(), IStateGoodsDataPresenter.PubView {

    var pvCustomTime: TimePickerView? = null;
    var pvCustomTime2: TimePickerView? = null;
    var mStart : Long? = null;
    var mEnd : Long? = null;

    lateinit var endDate: Calendar;

    lateinit var selectedDate: Calendar

    private var aaChartModel = AAChartModel()

    companion object {

        fun new(): StatsDateGoodsFragment {
            return newInstance(1);
        }

        fun newInstance(status: Int): StatsDateGoodsFragment {
            return StatsDateGoodsFragment().apply {
                arguments = Bundle().apply {
//                    putInt(UserStatusFragment.KEY_TYPE, status)
                }
            }
        }
    }

    override fun getLayoutId(): Int? {
        return R.layout.sales_fragment_stats_goods;
    }

    override fun createPresenter(): IStateGoodsDataPresenter? {
        return StatsGoodsDataPreImpl(requireContext(), this);
    }

    override fun initViewsAndData(rootView: View) {
        initChartView();
//        initTabLayout();
        initDate();
    }

    private var mTabTitles = arrayOf("??????TOP10", "??????TOP10")

    fun getCheckType() : Int {
        if(btn1.isChecked) {
            return 1;
        } else if(btn2.isChecked) {
            return 2;
        } else {
            return 3;
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
            //yyyy-MM-dd HH:mm
            return "-01 23:59:59";
        } else {
            return " 23:59:59";
        }
    }

    fun getCheckTimeFormat() : String {
        if(btn1.isChecked) {
            return "yyyy";
        } else if(btn2.isChecked) {
            //yyyy-MM-dd HH:mm
            return "yyyy-MM";
        } else {
            return DATE_FORMAT;
        }
    }

    fun initDate() {
        selectedDate = Calendar.getInstance();
        // ??????????????????
        val startDate: Calendar = Calendar.getInstance()
        startDate.set(selectedDate.get(Calendar.YEAR), 1, 1)

        endDate = Calendar.getInstance()
        endDate.set(
            startDate.get(Calendar.YEAR) + 5, startDate.get(Calendar.MONTH), startDate.get(
                Calendar.DATE
            )
        )

        mStart = startDate.timeInMillis/1000;
        mEnd = endDate.timeInMillis/1000;
        mPresenter?.getGoodsSales(mStart, mEnd);

        toggleGroup.addOnButtonCheckedListener { group, checkedId, isChecked ->
            resetData();
            // R.id.btn1, R.id.btn2, R.id.btn3
        }

        dateStartTv.singleClick{
            pvCustomTime = getDatePicker(mContext, selectedDate, startDate, endDate, getCheckType(), { date, view ->
                dateStartTv.text = formatString(date, getCheckTimeFormat())

                val s = dateTime2Date(dateStartTv.getViewText()+getCheckStartTimeStr())?.time?:0;
                mStart = s/1000;

                if(mStart?:0 > 0 && mEnd?:0 > 0) {
                    mPresenter?.getGoodsSales(mStart, mEnd);
                    //mLoadMoreDelegate?.refresh()
                }
                //firstMenuTv.setText(formatDateTime(date))
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
                        mPresenter?.getGoodsSales(mStart, mEnd);
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
        resetData();
    }

    fun resetData() {
        dateStartTv.text = formatString(selectedDate.time, getCheckTimeFormat())

        val s = dateTime2Date(dateStartTv.getViewText()+getCheckStartTimeStr())?.time?:0;
        mStart = s/1000;

        if(mStart?:0 > 0 && mEnd?:0 > 0) {
            mPresenter?.getGoodsSales(mStart, mEnd);
        }

        dateEndTv.setText(formatString(endDate.time, getCheckTimeFormat()))
        val e = dateTime2Date(dateEndTv.getViewText()+getCheckEndTimeStr())?.time?:0;
        mEnd = e/1000;

        if(mStart?:0 > 0 && mEnd?:0 > 0) {
            mPresenter?.getGoodsSales(mStart, mEnd);
        }
    }

    fun initChartView() {
        aaChartView?.setBackgroundColor(0)
        aaChartView?.background?.alpha = 0
        aaChartView?.callBack = click
        orderChartView?.setBackgroundColor(0)
        orderChartView?.background?.alpha = 0
        orderChartView?.callBack = click
        //orderChartView?.aa_drawChartWithChartModel(configureAAChartModel());
    }

    private fun configureColumnChartAndBarChartStyle() {
        aaChartModel
            .categories(arrayOf("Jan", "Feb", "Mar", "Apr", "May", "Jun", "July", "Aug", "Spe", "Oct", "Nov", "Dec"))
            .colorsTheme(arrayOf("#fe117c", "#ffc069"))
            .animationType(AAChartAnimationType.EaseInCubic)
            .animationDuration(1200)
    }

    private fun configureAAChartModel(): AAChartModel {
        val aaChartModel = AAChartModel()
            .chartType(AAChartType.Column)
            .title("2020???")
            .subtitle("")
            .subtitleAlign(AAChartAlignType.Right)
            .markerSymbolStyle(AAChartSymbolStyleType.BorderBlank)
            .backgroundColor("#fff")
            .dataLabelsEnabled(false)
            .xAxisVisible(true)
            .yAxisTitle("?????????")
            .yAxisGridLineWidth(0f)
            .legendEnabled(true)
            .touchEventEnabled(true)
            .categories(arrayOf( "??????", "??????", "??????", "??????", "??????", "??????",
                "??????", "??????", "??????", "??????", "?????????", "?????????"))
            .series(arrayOf(
                AASeriesElement()
                    .name("?????????")
                    .step(true)
                    .data(arrayOf(17.0, 26.9, 39.5, 144.5, 118.2, 121.5, 75.2, 86.5, 93.3, 48.3, 113.9, 29.6)),
                AASeriesElement()
                    .name("?????????")
                    .data(arrayOf(0.2, 0.8, 5.7, 11.3, 17.0, 22.0, 24.8, 24.1, 20.1, 14.1, 8.6, 2.5))
            ))

        configureColumnChartAndBarChartStyle()

        return aaChartModel
    }

    val click = object : AAChartView.AAChartViewCallBack {

        override fun chartViewDidFinishLoad(aaChartView: AAChartView) {
            println("?????????????????????????????????? ")
        }

        override fun chartViewMoveOverEventMessage(
            aaChartView: AAChartView,
            messageModel: AAMoveOverEventMessageModel
        ) {
            //println("????move over event message " + GsonUtils.toJson(messageModel))
        }
    };

    override fun setGoodsSales(item: GoodsSalesRespBean?) {
        val goodsSales = item?.goodsSales;
        orderCountTv.setText(String.format("??????%s????????????", goodsSales?.sumOrderNum))
        goodsCountTv.setText(String.format("%s???", goodsSales?.sumGoodsNum));
        perTicketSalesTv.setText(String.format("%s???", goodsSales?.perTicketSales))

        initTabLayout(item?.salesGoodsTop10, item?.unsalesGoodsTop10);
    }

    private fun initTabLayout(sales : List<SalesGoodsTop10?>?, unSales : List<SalesGoodsTop10?>?) {

        val fragments = mutableListOf<Fragment>()
        fragments.add(GoodsTopFragment.newInstance(2, sales));
        fragments.add(GoodsTopFragment.newInstance(1, unSales))

        val fragmentAdapter = GoodsHomePageAdapter(childFragmentManager, fragments, mTabTitles)
        goodsTopVp.setAdapter(fragmentAdapter)
        goodsTopLayout.setViewPager(goodsTopVp)

        setGoodsCategory(listOf<CategorySales>());
    }

    fun setGoodsCategory(list : List<CategorySales?>?) {
        aaChartModel = configurePieChart(list)
        aaChartView?.aa_drawChartWithChartModel(aaChartModel);
    }

    fun configurePieChart(cateList : List<CategorySales?>?): AAChartModel {
        var catoryList : MutableList<Any> = mutableListOf();

        cateList?.forEachIndexed { index, categorySales ->
            catoryList.add(arrayOf(categorySales?.categoryName, categorySales?.num));
        }
        if(catoryList.isEmpty()) {
            catoryList.add( arrayOf("??????", 0))
        }

        var list : Array<AASeriesElement> = arrayOf(
            AASeriesElement()
                .name("??????")
                .data(arrayOf(catoryList)));
        return AAChartModel()
            .chartType(AAChartType.Pie)
            .backgroundColor("#ffffff")
//            .title("????????????")
//            .subtitle("virtual data")
            .dataLabelsEnabled(true)//?????????????????????????????????
            .yAxisTitle("???")
            //.series(list)
    }

}