package com.lingmiao.shop.business.me.fragment

import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.james.common.base.BaseFragment
import com.lingmiao.shop.R
import com.lingmiao.shop.business.me.presenter.DeliveryInTimePresenter
import com.lingmiao.shop.business.me.presenter.impl.DeliveryInTimePresenterImpl
import com.lingmiao.shop.business.tools.adapter.PriceAdapter
import com.lingmiao.shop.business.tools.adapter.RangeAdapter
import com.lingmiao.shop.business.tools.adapter.TimeAdapter
import com.lingmiao.shop.business.tools.bean.DistanceSection
import com.lingmiao.shop.business.tools.bean.PeekTime
import com.lingmiao.shop.business.tools.bean.TimeSection
import com.lingmiao.shop.business.tools.bean.TimeValue
import com.lingmiao.shop.business.tools.pop.DayPop
import com.lingmiao.shop.business.tools.pop.TimeListPop
import kotlinx.android.synthetic.main.me_fragment_delivery_in_time.*
import kotlinx.android.synthetic.main.me_fragment_delivery_in_time.rg_model_pay_km
import kotlinx.android.synthetic.main.tools_activity_freight_model_add.*
import kotlinx.android.synthetic.main.tools_adapter_time.*
import kotlinx.android.synthetic.main.tools_include_model_price.*
import kotlinx.android.synthetic.main.tools_include_model_range.*
import kotlinx.android.synthetic.main.tools_include_model_time.*

/**
Create Date : 2021/3/53:40 PM
Auther      : Fox
Desc        :
 **/
class DeliveryOfRiderFragment : BaseFragment<DeliveryInTimePresenter>(), DeliveryInTimePresenter.View {

    private lateinit var mPriceAdapter : PriceAdapter;
    private lateinit var mRangeAdapter : RangeAdapter;
    private lateinit var mTimeAdapter : TimeAdapter;

    private lateinit var mTimeList : MutableList<TimeSection>;
    private lateinit var mRangeList : MutableList<PeekTime>;
    private lateinit var mPriceList : MutableList<DistanceSection>;
    private lateinit var mTimeValueList : MutableList<TimeValue>;
    private lateinit var mDayTypeList : MutableList<String>;

    companion object {
        fun newInstance(): DeliveryOfRiderFragment {
            return DeliveryOfRiderFragment()
        }
    }

    override fun getLayoutId(): Int? {
        return R.layout.me_fragment_delivery_of_rider;
    }

    override fun createPresenter(): DeliveryInTimePresenter? {
        return DeliveryInTimePresenterImpl(this);
    }


    override fun initViewsAndData(rootView: View) {
        initPricePart();

        initRangePart();

        initTimePart();

        updateTimeCheckBox();

        updateCityExpressPayTypeCheckBox();
    }

    /**
     * 配送时效
     */
    private fun updateTimeCheckBox() {

        rg_model_time.setOnCheckedChangeListener { group, checkedId ->
            if(checkedId == R.id.cb_model_time_km) {
                ll_model_km.visibility = View.VISIBLE;
                rv_model_time.visibility = View.GONE;

            } else if(checkedId == R.id.cb_model_time_section){
                ll_model_km.visibility = View.GONE;
                rv_model_time.visibility = View.VISIBLE;
            }
        }
    }

    /**
     * 同城配送的计费方式
     */
    private fun updateCityExpressPayTypeCheckBox() {
        rg_model_pay_km.setOnCheckedChangeListener { group, checkedId ->
            if(checkedId == R.id.cb_model_pay_km_section) {
                rv_model_price.visibility = View.VISIBLE;
                ll_model_price_section.visibility = View.GONE;
            } else if(checkedId == R.id.cb_model_pay_km_num) {
                rv_model_price.visibility = View.GONE;
                ll_model_price_section.visibility = View.VISIBLE;
            }
        }
        rv_model_price.visibility = View.GONE;
        ll_model_price_section.visibility = View.VISIBLE;
    }

    private fun initRangePart() {
        mRangeList = arrayListOf();
        mRangeList.add(PeekTime())
        mRangeAdapter = RangeAdapter().apply {
            setOnItemChildClickListener { adapter, view, position ->
                val item = adapter.data[position] as PeekTime;
                if (view.id == R.id.tv_model_range_delete && position != 0) {
                    mRangeList.remove(item);
                    mRangeAdapter.replaceData(mRangeList);
                } else if(view.id == R.id.et_model_range_start) {
                    val pop = TimeListPop(context!!, mTimeValueList)
                    pop.setOnClickListener { it->
                        run {
                            item?.peekTimeStart = it?.name;
                            item?.startTimeCount = it?.value;
                            mRangeAdapter.notifyDataSetChanged();
                        }
                    }
                    pop.shiftStartTime(item?.endTimeCount ?: TimeValue.getLastTimeCount());
                    pop.showPopupWindow()
                } else if(view.id == R.id.et_model_range_end) {
                    val pop = TimeListPop(context!!, mTimeValueList)
                    pop.setOnClickListener { it->
                        run {
                            item?.peekTimeEnd = it?.name;
                            item?.endTimeCount = it?.value;
                            mRangeAdapter.notifyDataSetChanged();
                        }
                    }
                    pop.shiftEndTime(item?.startTimeCount ?: -1);
                    pop.showPopupWindow()
                }
            }

            val footer = View.inflate(context, R.layout.tools_footer_model_add, null);
            footer.findViewById<View>(R.id.tv_model_add).setOnClickListener {
                mRangeList.add(PeekTime());
                mRangeAdapter.replaceData(mRangeList);
            }
            addFooterView(footer)
        };

        rv_model_range.apply {
            layoutManager = initLayoutManager()
            adapter = mRangeAdapter;
        }

        mRangeAdapter.replaceData(mRangeList);
    }

    private fun initTimePart() {

        mTimeValueList = TimeValue.getTimeList();
        mDayTypeList = mutableListOf();
        mDayTypeList.add("当日");
        mDayTypeList.add("次日");

        mTimeList = arrayListOf();
        mTimeList.add(TimeSection())

        mTimeAdapter = TimeAdapter().apply {

            setOnItemChildClickListener { adapter, view, position ->
                val item = adapter.data[position] as TimeSection;
                if (view.id == R.id.tv_model_time_delete && position != 0) {
                    mTimeList.remove(item);
                    mTimeAdapter.replaceData(mTimeList);
                } else if(view.id == R.id.et_model_km_start) {
                    val pop = TimeListPop(context!!, mTimeValueList)
                    pop.setOnClickListener { it->
                        run {
                            item?.shipTime = it?.name;
                            item?.shipTimeCount = it?.value;
                            mTimeAdapter.notifyDataSetChanged();
                        }
                    }
                    pop.shiftStartTime(item?.arriveTimeCount ?: TimeValue.getLastTimeCount());
                    pop.showPopupWindow()
                } else if(view.id == R.id.et_model_km_end) {
                    val pop = TimeListPop(context!!, mTimeValueList)
                    pop.setOnClickListener { it->
                        item?.arriveTime = it?.name;
                        item?.arriveTimeCount = it?.value;
                        run {
                            mTimeAdapter.notifyDataSetChanged();
                        }
                    }
                    pop.shiftEndTime(if(item?.isToday()) item?.shipTimeCount ?: -1 else -1);
                    pop.showPopupWindow()
                } else if(view.id == R.id.tv_model_time_type) {
                    val pop = DayPop(context!!, mDayTypeList)
                    pop.setOnClickListener { it, position ->

                        run {
                            if(position == 0) {
                                item?.shiftToday();
                                if(item?.arriveTimeCount ?:0 <= item?.shipTimeCount?:0) {
                                    item?.arriveTime = null;
                                    item?.arriveTimeCount = null;
                                    mTimeAdapter.notifyDataSetChanged();
                                }
                            } else {
                                item?.shiftTomorrow();
                            }
                            tv_model_time_type.setText(it);
                        }
                    }
                    pop.showPopupWindow()
                }
            }
            val footer = View.inflate(context, R.layout.tools_footer_model_add, null);
            footer.findViewById<View>(R.id.tv_model_add).setOnClickListener {
                mTimeList.add(TimeSection());
                mTimeAdapter.replaceData(mTimeList);
            }
            addFooterView(footer)
        };

        rv_model_time.apply {
            layoutManager = initLayoutManager()
            adapter = mTimeAdapter;
        }

        mTimeAdapter.replaceData(mTimeList);
    }

    private fun initPricePart() {
        mPriceList = arrayListOf();
        mPriceList.add(DistanceSection());
        mPriceAdapter = PriceAdapter().apply {
            setOnItemChildClickListener { adapter, view, position ->
                val item = adapter.data[position] as DistanceSection;
                if (view.id == R.id.tv_model_price_delete && position != 0) {
                    mPriceList.remove(item);
                    mPriceAdapter.replaceData(mPriceList);
                }
            }

            val footer = View.inflate(context, R.layout.tools_footer_model_add, null);
            footer.findViewById<View>(R.id.tv_model_add).setOnClickListener {
                mPriceList.add(DistanceSection());
                mPriceAdapter.replaceData(mPriceList);
            }
            addFooterView(footer)
        };

        rv_model_price.apply {
            layoutManager = initLayoutManager()
            adapter = mPriceAdapter;
        }

        mPriceAdapter.replaceData(mPriceList);
    }

    private fun initLayoutManager(): RecyclerView.LayoutManager {
        return LinearLayoutManager(activity)
    }

}