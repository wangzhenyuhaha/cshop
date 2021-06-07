package com.lingmiao.shop.business.me.fragment

import android.view.View
import com.james.common.base.BaseFragment
import com.james.common.utils.exts.getViewText
import com.james.common.utils.exts.singleClick
import com.lingmiao.shop.R
import com.lingmiao.shop.business.me.presenter.DeliveryInTimePresenter
import com.lingmiao.shop.business.me.presenter.impl.DeliveryInTimePresenterImpl
import com.lingmiao.shop.business.tools.adapter.PriceAdapter
import com.lingmiao.shop.business.tools.adapter.RangeAdapter
import com.lingmiao.shop.business.tools.adapter.TimeAdapter
import com.lingmiao.shop.business.tools.api.JsonUtil
import com.lingmiao.shop.business.tools.bean.*
import com.lingmiao.shop.business.tools.pop.DayPop
import com.lingmiao.shop.business.tools.pop.TimeListPop
import com.lingmiao.shop.util.initAdapter
import kotlinx.android.synthetic.main.me_fragment_delivery_in_time.*
import kotlinx.android.synthetic.main.me_fragment_delivery_in_time.et_model_out_range_km
import kotlinx.android.synthetic.main.me_fragment_delivery_in_time.rg_model_pay_km
import kotlinx.android.synthetic.main.tools_adapter_time.*
import kotlinx.android.synthetic.main.tools_include_model_price.*
import kotlinx.android.synthetic.main.tools_include_model_range.*
import kotlinx.android.synthetic.main.tools_include_model_time.*

/**
Create Date : 2021/3/53:40 PM
Auther      : Fox
Desc        :
 **/
class DeliveryInTimeFragment : BaseFragment<DeliveryInTimePresenter>(), DeliveryInTimePresenter.View {

    private lateinit var mPriceAdapter : PriceAdapter;
    private lateinit var mRangeAdapter : RangeAdapter;
    private lateinit var mTimeAdapter : TimeAdapter;

    private lateinit var mTimeList : MutableList<TimeSection>;
    private lateinit var mRangeList : MutableList<PeekTime>;
    private lateinit var mPriceList : MutableList<DistanceSection>;
    private lateinit var mTimeValueList : MutableList<TimeValue>;
    private lateinit var mDayTypeList : MutableList<String>;

    private lateinit var mItem: FreightVoItem;

    var mFeeSetting : FeeSettingVo = FeeSettingVo();

    var mTimeSetting : TimeSettingVo = TimeSettingVo();

    companion object {
        fun newInstance(): DeliveryInTimeFragment {
            return DeliveryInTimeFragment()
        }
    }

    override fun getLayoutId(): Int? {
        return R.layout.me_fragment_delivery_in_time;
    }

    override fun createPresenter(): DeliveryInTimePresenter? {
        return DeliveryInTimePresenterImpl(this);
    }

    override fun initViewsAndData(rootView: View) {
        mItem = FreightVoItem();

        initPricePart();

        initRangePart();

        initTimePart();

        updateTimeCheckBox();

        updateCityExpressPayTypeCheckBox();

        tvShopSettingSubmit.singleClick {
            mItem?.apply {
                name = "商家配送";

                // 基础
                templateType = FreightVoItem.TYPE_LOCAL;
                type = if(cb_model_pay_km_section.isChecked) FreightVoItem.TYPE_KM_SECTION else FreightVoItem.TYPE_KM_COUNT;

                // 同城
                baseShipPrice = et_model_km_price.getViewText();

                shipRange = et_model_out_range_km.getViewText();

                val readyTime = deliveryThingEt.getViewText().toInt();
                if(readyTime > 10) {
                    showToast("请输入配货时间");
                    return@singleClick;
                }
                var setting = mTimeSetting;
                if(cb_model_time_km.isChecked) {
                    // 按公里数
                    setting.timeType = TimeSettingVo.TIME_TYPE_BASE;
                    setting?.baseDistance = et_model_time_km.getViewText();
                    setting?.baseTime = et_model_time_minute.getViewText();
                    setting?.unitDistance = et_model_time_km_out.getViewText();
                    setting?.unitTime = et_model_time_minute_more.getViewText();
                    // 清数据
                    setting.timeSections = null;

                }
                if(cb_model_time_section.isChecked){
                    // 按公里段
                    setting.timeType = TimeSettingVo.TIME_TYPE_SECTION;
                    setting.timeSections = mTimeList;
                    // 清数据
                    setting?.baseDistance = null;
                    setting?.baseTime = null;
                    setting?.unitDistance = null;
                    setting?.unitTime = null;
                }
                setting?.readyTime = readyTime;
                timeSettingVo = TimeSettingReqVo(setting);
                timeSetting = JsonUtil.instance.toJson(setting);

                mFeeSetting.feeType = FeeSettingVo.FEE_TYPE_DISTANCE;
                mFeeSetting?.baseDistance = et_model_price_km.getViewText();
                mFeeSetting?.basePrice = et_model_price_price.getViewText();
                mFeeSetting?.unitDistance = et_model_price_km_out.getViewText();
                mFeeSetting?.unitPrice = et_model_price_minute_more.getViewText();

                // 清数据
                mFeeSetting.distanceSections = null;

                // 计费
                feeSetting = JsonUtil.instance.toJson(mFeeSetting);
                feeSettingVo = FeeSettingReqVo(mFeeSetting);

                // 快递 clear data
                items = null;
                type = null;
            }

            if(et_model_km_price.getViewText() == null || et_model_km_price.getViewText().isEmpty()) {
                showToast("请输入起送价");
                return@singleClick;
            }

            mPresenter?.addModel(mItem);
        }

        mPresenter?.getTemplate("TONGCHENG");
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

        rv_model_range.initAdapter(mRangeAdapter)

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

        rv_model_time.initAdapter(mTimeAdapter)

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

        rv_model_price.initAdapter(mPriceAdapter)

        mPriceAdapter.replaceData(mPriceList);
    }

    override fun updateModelSuccess(b: Boolean) {
        showToast("提交成功");
    }

    override fun setModel(item: FreightVoItem?) {
        mItem = item ?: FreightVoItem();

        // 模板名称
        //cb_model_type_express_city.isChecked = true;
        // 起送价
        et_model_km_price.setText(String.format("%s", item?.baseShipPrice));
        // 配送范围
        et_model_out_range_km.setText(String.format("%s", item?.shipRange));



        mFeeSetting = mPresenter?.getFeeSetting(item) ?: FeeSettingVo();
        mTimeSetting = mPresenter?.getTimeSetting(item) ?: TimeSettingVo();

        mFeeSetting?.apply {
            // 配送范围加收费用
            mRangeList = mFeeSetting?.peekTimes ?: arrayListOf();
            mRangeAdapter.replaceData(mRangeList);

            // 按公里数计费
            cb_model_pay_km_num.isChecked = true;
            et_model_price_km.setText(String.format("%s", mFeeSetting?.baseDistance));
            et_model_price_price.setText(String.format("%s", mFeeSetting?.basePrice));
            et_model_price_km_out.setText(String.format("%s", mFeeSetting?.unitDistance));
            et_model_price_minute_more.setText(String.format("%s", mFeeSetting?.unitPrice));
//            if(isDistanceType()) {
//
//            } else {
//                // 按公里段计费
//                cb_model_pay_km_section.isChecked = true;
//                mPriceList = mFeeSetting?.distanceSections ?: arrayListOf();
//                mPriceAdapter.replaceData(mPriceList);
//            }

        }

        mTimeSetting?.apply {
            cb_model_time_km.isChecked = true;
            et_model_time_km.setText(String.format("%s", mTimeSetting?.baseDistance));
            et_model_time_minute.setText(String.format("%s", mTimeSetting?.baseTime));
            et_model_time_km_out.setText(String.format("%s", mTimeSetting?.unitDistance));
            et_model_time_minute_more.setText(String.format("%s", mTimeSetting?.unitTime));
            deliveryThingEt.setText(String.format("%s", mTimeSetting?.readyTime));
//            if(isBaseTimeType()) {
//
//            } else {
//                cb_model_time_section.isChecked = true;
//                mTimeList = mTimeSetting?.timeSections ?: arrayListOf();
//                mTimeAdapter.replaceData(mTimeList);
//            }
        }
    }

}