package com.lingmiao.shop.business.me.fragment

import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.james.common.base.BaseFragment
import com.james.common.utils.exts.getViewText
import com.james.common.utils.exts.singleClick
import com.lingmiao.shop.R
import com.lingmiao.shop.business.me.presenter.DeliveryOfRiderPresenter
import com.lingmiao.shop.business.me.presenter.impl.DeliveryInOfRiderPresenterImpl
import com.lingmiao.shop.business.tools.adapter.PriceAdapter
import com.lingmiao.shop.business.tools.adapter.RangeAdapter
import com.lingmiao.shop.business.tools.adapter.TimeAdapter
import com.lingmiao.shop.business.tools.bean.*
import com.lingmiao.shop.business.tools.pop.DayPop
import com.lingmiao.shop.business.tools.pop.TimeListPop
import com.lingmiao.shop.util.initAdapter
import kotlinx.android.synthetic.main.me_fragment_delivery_of_rider.*
import kotlinx.android.synthetic.main.tools_adapter_time.*
import kotlinx.android.synthetic.main.tools_include_model_price.*
import kotlinx.android.synthetic.main.tools_include_model_range.*
import kotlinx.android.synthetic.main.tools_include_model_time.*

/**
Create Date : 2021/3/53:40 PM
Auther      : Fox
Desc        :
 **/
class DeliveryOfRiderFragment : BaseFragment<DeliveryOfRiderPresenter>(), DeliveryOfRiderPresenter.View {

    private lateinit var mPriceAdapter : PriceAdapter;
    private lateinit var mRangeAdapter : RangeAdapter;
    private lateinit var mTimeAdapter : TimeAdapter;

    private lateinit var mTimeList : MutableList<TimeSection>;
    private lateinit var mRangeList : MutableList<PeekTime>;
    private lateinit var mPriceList : MutableList<DistanceSection>;
    private lateinit var mTimeValueList : MutableList<TimeValue>;
    private lateinit var mDayTypeList : MutableList<String>;

    private var mItem: FreightVoItem? = null;

    private var mShopItem: FreightVoItem? = null;

    var mFeeSetting : FeeSettingVo = FeeSettingVo();

    var mTimeSetting : TimeSettingVo = TimeSettingVo();

    companion object {
        fun newInstance(item : FreightVoItem?): DeliveryOfRiderFragment {
            return DeliveryOfRiderFragment().apply {
                arguments = Bundle().apply {
                    putSerializable("item", item);
                }
            }
        }
    }

    override fun initBundles() {
        mItem = arguments?.getSerializable("item") as FreightVoItem?;
    }


    override fun getLayoutId(): Int? {
        return R.layout.me_fragment_delivery_of_rider;
    }

    override fun createPresenter(): DeliveryOfRiderPresenter? {
        return DeliveryInOfRiderPresenterImpl(this);
    }

    override fun initViewsAndData(rootView: View) {
         initPricePart();

         initRangePart();

         initTimePart();

         updateTimeCheckBox();

         updateCityExpressPayTypeCheckBox();

        tvShopSettingSubmit.singleClick {
            if(mShopItem == null || mShopItem?.id == null || mShopItem?.id?.length == 0) {
                return@singleClick;
            }
            var setting = TimeSettingVo();
            // setting?.readyTime = deliveryThingEt.getViewText().toInt();
            if(shiftDeliveryCb.isChecked) {
                setting.isAllowTransTemp = 1;
                setting.transTempLimitTime = deliveryShiftTimeEt.getViewText().toInt();
            } else {
                setting.isAllowTransTemp = 0;
                setting.transTempLimitTime = 0
            }

            mPresenter?.updateModel(mShopItem?.id!!, setting.isAllowTransTemp!!, setting.transTempLimitTime!!);
        }

//        shiftDeliveryCb.setOnCheckedChangeListener { buttonView, isChecked ->
//            if(isChecked) {
//                mPresenter?.getShopDeliveryStatus();
//            }
//        }

        if(mItem == null) {
            mPresenter?.getTemplate();
        } else {
            setUi();
            mPresenter?.getTemplate();
        }

        mPresenter?.getShopTemplate();
    }

    override fun onSetShopTemplate(item: FreightVoItem?) {
        mShopItem = item ?: FreightVoItem();
        val mTimeSetting = mPresenter?.getTimeSetting(item) ?: TimeSettingVo();
        deliveryShiftTimeEt.setText(mTimeSetting?.transTempLimitTime?.toString())
        shiftDeliveryCb.isChecked = mTimeSetting.isAllowTransTemp == 1
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
                    val pop = TimeListPop(requireContext(), mTimeValueList)
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
                    val pop = TimeListPop(requireContext(), mTimeValueList)
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
                    val pop = TimeListPop(requireContext(), mTimeValueList)
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
                    val pop = TimeListPop(requireContext(), mTimeValueList)
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
                    val pop = DayPop(requireContext(), mDayTypeList)
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

    fun setUi() {
        // 模板名称
        //cb_model_type_express_city.isChecked = true;
        // 起送价
        et_model_km_price.setText(String.format("%s", mItem?.baseShipPrice));
        // 配送范围
        et_model_out_range_km.setText(String.format("%s", mItem?.shipRange));

        mFeeSetting = mPresenter?.getFeeSetting(mItem) ?: FeeSettingVo();
        mTimeSetting = mPresenter?.getTimeSetting(mItem) ?: TimeSettingVo();

        mFeeSetting?.apply {
            // 配送范围加收费用
            mRangeList = mFeeSetting?.peekTimes ?: arrayListOf();
            mRangeAdapter.replaceData(mRangeList);

            if(isDistanceType()) {
                // 按公里数计费
                cb_model_pay_km_num.isChecked = true;
                et_model_price_km.setText(String.format("%s", mFeeSetting?.baseDistance));
                et_model_price_price.setText(String.format("%s", mFeeSetting?.basePrice));
                et_model_price_km_out.setText(String.format("%s", mFeeSetting?.unitDistance));
                et_model_price_minute_more.setText(String.format("%s", mFeeSetting?.unitPrice));
            } else {
                // 按公里段计费
                cb_model_pay_km_section.isChecked = true;
                mPriceList = mFeeSetting?.distanceSections ?: arrayListOf();
                mPriceAdapter.replaceData(mPriceList);
            }

        }

        mTimeSetting?.apply {
            if(isBaseTimeType()) {
                cb_model_time_km.isChecked = true;
                et_model_time_km.setText(String.format("%s", mTimeSetting?.baseDistance));
                et_model_time_minute.setText(String.format("%s", mTimeSetting?.baseTime));
                et_model_time_km_out.setText(String.format("%s", mTimeSetting?.unitDistance));
                et_model_time_minute_more.setText(String.format("%s", mTimeSetting?.unitTime));
                deliveryThingEt.setText(String.format("%s", mTimeSetting?.readyTime));
            } else {
                cb_model_time_section.isChecked = true;
                mTimeList = mTimeSetting?.timeSections ?: arrayListOf();
                mTimeAdapter.replaceData(mTimeList);
            }
        }
    }
    override fun setModel(item: FreightVoItem?) {
        mItem = item ?: FreightVoItem();

        setUi();
    }

    override fun onSetShopDeliveryStatus(size : Int, item: FreightVoItem?) {
        shiftDeliveryCb.isChecked = size > 0 ;
        if(size <= 0) {
            showToast("请先设置商家配置送模板")
        }
        mShopItem = item ?: FreightVoItem();
        mTimeSetting = mPresenter?.getTimeSetting(item) ?: TimeSettingVo();
    }

}