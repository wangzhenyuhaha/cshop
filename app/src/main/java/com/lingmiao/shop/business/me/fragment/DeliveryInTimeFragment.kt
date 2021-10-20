package com.lingmiao.shop.business.me.fragment

import android.os.Bundle
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
import kotlinx.android.synthetic.main.tools_adapter_time.*
import kotlinx.android.synthetic.main.tools_include_model_price.*
import kotlinx.android.synthetic.main.tools_include_model_range.*
import kotlinx.android.synthetic.main.tools_include_model_time.*

/**
Create Date : 2021/3/53:40 PM
Auther      : Fox
Desc        :
 **/
class DeliveryInTimeFragment : BaseFragment<DeliveryInTimePresenter>(),
    DeliveryInTimePresenter.View {

    private lateinit var mPriceAdapter: PriceAdapter
    private lateinit var mRangeAdapter: RangeAdapter
    private lateinit var mTimeAdapter: TimeAdapter

    //按时间段配送时效列表
    private lateinit var mTimeList: MutableList<TimeSection>

    //加收费用列表
    private lateinit var mRangeList: MutableList<PeekTime>

    private lateinit var mPriceList: MutableList<DistanceSection>

    //24小时时间列表
    private lateinit var mTimeValueList: MutableList<TimeValue>

    //按时间段配送时效当日次日
    private lateinit var mDayTypeList: MutableList<String>

    private var mItem: FreightVoItem? = null

    var mFeeSetting: FeeSettingVo = FeeSettingVo()

    //配送时效
    var mTimeSetting: TimeSettingVo = TimeSettingVo()

    companion object {
        fun newInstance(item: FreightVoItem?): DeliveryInTimeFragment {
            return DeliveryInTimeFragment().apply {
                arguments = Bundle().apply {
                    putSerializable("item", item)
                }
            }
        }
    }

    override fun initBundles() {
        mItem = arguments?.getSerializable("item") as FreightVoItem?
    }

    override fun getLayoutId() = R.layout.me_fragment_delivery_in_time

    override fun createPresenter() = DeliveryInTimePresenterImpl(this)

    override fun initViewsAndData(rootView: View) {

        //无用
        initPricePart()

        //加收费用
        initRangePart()

        //按时间段的配送时效
        initTimePart()

        //选择配送时效
        updateTimeCheckBox()

        //无用
        updateCityExpressPayTypeCheckBox()

        //保存
        tvShopSettingSubmit.singleClick {
            //不为空才起效
            mItem?.apply {

                //配货时间
                if (deliveryThingEt.getViewText().isEmpty()) {
                    showToast("请输入配货时间")
                    return@singleClick
                }
                //起送价
                if (et_model_km_price.getViewText().isEmpty()) {
                    showToast("请输入起送价")
                    return@singleClick
                }
                //配送费
                if (et_model_price_km.getViewText().isEmpty()
                    || et_model_price_price.getViewText().isEmpty()
                    || et_model_price_km_out.getViewText().isEmpty()
                    || et_model_price_minute_more.getViewText().isEmpty()
                ) {
                    showToast("请填完配送费")
                    return@singleClick
                }
                //配送范围
                if (et_model_out_range_km.getViewText().isEmpty()) {
                    showToast("请输入配送范围")
                    return@singleClick
                }
                if (cb_model_time_km.isChecked) {
                    //按公里数
                    if (et_model_time_km.getViewText().isEmpty()
                        || et_model_time_minute.getViewText().isEmpty()
                        || et_model_time_km_out.getViewText().isEmpty()
                        || et_model_time_minute_more.getViewText().isEmpty()
                    ) {
                        showToast("请填完配送时效")
                        return@singleClick
                    }
                }
                if (cb_model_time_section.isChecked) {
                    //按时间
                    if (mTimeList.size == 0) {
                        showToast("请填完配送时效")
                        return@singleClick
                    } else {
                        if (mTimeList[0].arriveTime.isNullOrEmpty() || mTimeList[0].shipTime.isNullOrEmpty()) {
                            showToast("请填完配送时效")
                            return@singleClick
                        }
                    }
                }

                name = "商家配送"
                // 基础（默认）
                templateType = FreightVoItem.TYPE_LOCAL
                type =
                    if (cb_model_pay_km_section.isChecked) FreightVoItem.TYPE_KM_SECTION else FreightVoItem.TYPE_KM_COUNT

                //起送价格
                baseShipPrice = et_model_km_price.getViewText()

                //配送范围
                shipRange = et_model_out_range_km.getViewText()

                //配送时效
                //按公里或者时间
                val setting = mTimeSetting
                if (cb_model_time_km.isChecked) {
                    // 按公里数
                    setting.timeType = TimeSettingVo.TIME_TYPE_BASE
                    setting.baseDistance = et_model_time_km.getViewText()
                    setting.baseTime = et_model_time_minute.getViewText()
                    setting.unitDistance = et_model_time_km_out.getViewText()
                    setting.unitTime = et_model_time_minute_more.getViewText()
                    // 清数据
                    setting.timeSections = null

                }
                if (cb_model_time_section.isChecked) {
                    // 按时间段
                    setting.timeType = TimeSettingVo.TIME_TYPE_SECTION
                    setting.timeSections = mTimeList
                    // 清数据
                    setting.baseDistance = null
                    setting.baseTime = null
                    setting.unitDistance = null
                    setting.unitTime = null
                }
                //配货时间
                val readyTime = deliveryThingEt.getViewText().toInt()
                setting.readyTime = readyTime
                timeSettingVo = TimeSettingReqVo(setting)
                timeSetting = JsonUtil.instance.toJson(setting)

                //配送费
                mFeeSetting.feeType = FeeSettingVo.FEE_TYPE_DISTANCE
                mFeeSetting.baseDistance = et_model_price_km.getViewText()
                mFeeSetting.basePrice = et_model_price_price.getViewText()
                mFeeSetting.unitDistance = et_model_price_km_out.getViewText()
                mFeeSetting.unitPrice = et_model_price_minute_more.getViewText()

                // 清数据
                mFeeSetting.distanceSections = null

                // 计费
                feeSetting = JsonUtil.instance.toJson(mFeeSetting)
                feeSettingVo = FeeSettingReqVo(mFeeSetting)

                // 快递 clear data
                items = null
                type = null
            }

            mPresenter?.addModel(mItem!!)
        }

        //初始化页面
        //此时未设置骑手配送
        if (mItem == null) {
            //获取本地配送模板
            mPresenter?.getTemplate(FreightVoItem.TYPE_LOCAL)
        } else {
            //设置了骑手配送
            setUi()
            //获取本地配送模板
            mPresenter?.getTemplate(FreightVoItem.TYPE_LOCAL)
        }
    }

    //无用
    private fun initPricePart() {

        //设置配送费数据（空）
        mPriceList = arrayListOf()
        mPriceList.add(DistanceSection())

        mPriceAdapter = PriceAdapter().apply {
            setOnItemChildClickListener { adapter, view, position ->
                val item = adapter.data[position] as DistanceSection
                if (view.id == R.id.tv_model_price_delete && position != 0) {
                    mPriceList.remove(item)
                    mPriceAdapter.replaceData(mPriceList)
                }
            }

            val footer = View.inflate(context, R.layout.tools_footer_model_add, null)
            footer.findViewById<View>(R.id.tv_model_add).setOnClickListener {
                mPriceList.add(DistanceSection())
                mPriceAdapter.replaceData(mPriceList)
            }
            addFooterView(footer)
        }

        rv_model_price.initAdapter(mPriceAdapter)

        mPriceAdapter.replaceData(mPriceList)
    }

    //加收费用
    private fun initRangePart() {

        //第一行加收费用
        mRangeList = arrayListOf()
        mRangeList.add(PeekTime())

        mRangeAdapter = RangeAdapter().apply {

            setOnItemChildClickListener { adapter, view, position ->
                val item = adapter.data[position] as PeekTime
                //删除
                if (view.id == R.id.tv_model_range_delete && position != 0) {
                    mRangeList.remove(item)
                    mRangeAdapter.replaceData(mRangeList)
                }
                //开始时间
                else if (view.id == R.id.et_model_range_start) {
                    val pop = TimeListPop(requireContext(), mTimeValueList)
                    pop.setOnClickListener { it ->
                        run {
                            item.peekTimeStart = it.name
                            item.startTimeCount = it.value
                            mRangeAdapter.notifyDataSetChanged()
                        }
                    }
                    pop.shiftStartTime(item.endTimeCount ?: TimeValue.getLastTimeCount())
                    pop.showPopupWindow()
                }
                //结束时间
                else if (view.id == R.id.et_model_range_end) {
                    val pop = TimeListPop(requireContext(), mTimeValueList)
                    pop.setOnClickListener { it ->
                        run {
                            item.peekTimeEnd = it.name
                            item.endTimeCount = it.value
                            mRangeAdapter.notifyDataSetChanged()
                        }
                    }
                    pop.shiftEndTime(item.startTimeCount ?: -1)
                    pop.showPopupWindow()
                }
            }

            val footer = View.inflate(context, R.layout.tools_footer_model_add, null)
            footer.findViewById<View>(R.id.tv_model_add).setOnClickListener {
                mRangeList.add(PeekTime())
                mRangeAdapter.replaceData(mRangeList)
            }
            addFooterView(footer)
        }

        rv_model_range.initAdapter(mRangeAdapter)

        mRangeAdapter.replaceData(mRangeList)
    }

    //按时间段的配送时效
    private fun initTimePart() {

        //时间列表
        mTimeValueList = TimeValue.getTimeList()
        mDayTypeList = mutableListOf()
        mDayTypeList.add("当日")
        mDayTypeList.add("次日")

        mTimeList = arrayListOf()
        mTimeList.add(TimeSection())

        mTimeAdapter = TimeAdapter().apply {

            setOnItemChildClickListener { adapter, view, position ->
                val item = adapter.data[position] as TimeSection
                if (view.id == R.id.tv_model_time_delete && position != 0) {
                    mTimeList.remove(item)
                    mTimeAdapter.replaceData(mTimeList)
                }
                //付款时间
                else if (view.id == R.id.et_model_km_start) {
                    val pop = TimeListPop(requireContext(), mTimeValueList)
                    pop.setOnClickListener { it ->
                        run {
                            item.shipTime = it.name
                            item.shipTimeCount = it.value
                            mTimeAdapter.notifyDataSetChanged()
                        }
                    }
                    pop.shiftStartTime(item.arriveTimeCount ?: TimeValue.getLastTimeCount())
                    pop.showPopupWindow()
                }
                //送达时间
                else if (view.id == R.id.et_model_km_end) {
                    val pop = TimeListPop(requireContext(), mTimeValueList)
                    pop.setOnClickListener { it ->
                        item.arriveTime = it.name
                        item.arriveTimeCount = it.value
                        run {
                            mTimeAdapter.notifyDataSetChanged()
                        }
                    }
                    pop.shiftEndTime(if (item.isToday()) item.shipTimeCount ?: -1 else -1)
                    pop.showPopupWindow()
                }
                //更换类型
                else if (view.id == R.id.tv_model_time_type) {
                    val pop = DayPop(requireContext(), mDayTypeList)
                    pop.setOnClickListener { it, position ->

                        run {
                            if (position == 0) {
                                item.shiftToday()
                                if (item.arriveTimeCount ?: 0 <= item.shipTimeCount ?: 0) {
                                    item.arriveTime = null
                                    item.arriveTimeCount = null
                                    mTimeAdapter.notifyDataSetChanged()
                                }
                            } else {
                                item.shiftTomorrow()
                            }
                            tv_model_time_type.text = it
                        }
                    }
                    pop.showPopupWindow()
                }

            }
            val footer = View.inflate(context, R.layout.tools_footer_model_add, null)
            footer.findViewById<View>(R.id.tv_model_add).setOnClickListener {
                mTimeList.add(TimeSection())
                mTimeAdapter.replaceData(mTimeList)
            }
            addFooterView(footer)
        }

        rv_model_time.initAdapter(mTimeAdapter)

        mTimeAdapter.replaceData(mTimeList)
    }

    /**
     * 配送时效
     */
    private fun updateTimeCheckBox() {
        rg_model_time.setOnCheckedChangeListener { _, checkedId ->
            if (checkedId == R.id.cb_model_time_km) {
                ll_model_km.visibility = View.VISIBLE
                rv_model_time.visibility = View.GONE

            } else if (checkedId == R.id.cb_model_time_section) {
                ll_model_km.visibility = View.GONE
                rv_model_time.visibility = View.VISIBLE
            }
        }
    }

    /**
     * 同城配送的计费方式
     */
    private fun updateCityExpressPayTypeCheckBox() {
        rg_model_pay_km.setOnCheckedChangeListener { group, checkedId ->
            if (checkedId == R.id.cb_model_pay_km_section) {
                rv_model_price.visibility = View.VISIBLE
                ll_model_price_section.visibility = View.GONE
            } else if (checkedId == R.id.cb_model_pay_km_num) {
                rv_model_price.visibility = View.GONE
                ll_model_price_section.visibility = View.VISIBLE
            }
        }
        rv_model_price.visibility = View.GONE
        ll_model_price_section.visibility = View.VISIBLE
    }

    override fun updateModelSuccess(b: Boolean) {
        showToast("提交成功")
    }

    override fun setModel(item: FreightVoItem?) {
        mItem = item ?: FreightVoItem()
        setUi()
    }

    //设置UI数据和
    private fun setUi() {
        // 起送价
        et_model_km_price.setText(String.format("%s", mItem?.baseShipPrice))
        // 配送范围
        et_model_out_range_km.setText(String.format("%s", mItem?.shipRange))

        mFeeSetting =
            if (mItem?.feeSetting?.isEmpty() == true) FeeSettingVo() else mPresenter?.getFeeSetting(
                mItem
            ) ?: FeeSettingVo()

        mTimeSetting =
            if (mItem?.timeSetting?.isEmpty() == true) TimeSettingVo() else mPresenter?.getTimeSetting(
                mItem
            ) ?: TimeSettingVo()

        mFeeSetting.apply {
            // 加收费用
            mRangeList = mFeeSetting.peekTimes ?: arrayListOf()
            mRangeAdapter.replaceData(mRangeList)

            //配送费
            cb_model_pay_km_num.isChecked = true
            et_model_price_km.setText(String.format("%s", mFeeSetting.baseDistance))
            et_model_price_price.setText(String.format("%s", mFeeSetting.basePrice))
            et_model_price_km_out.setText(String.format("%s", mFeeSetting.unitDistance))
            et_model_price_minute_more.setText(String.format("%s", mFeeSetting.unitPrice))
        }

        mTimeSetting.apply {

            if (timeType == TimeSettingVo.TIME_TYPE_BASE) {
                //按公里数
                cb_model_time_km.isChecked = true
                et_model_time_km.setText(String.format("%s", mTimeSetting.baseDistance))
                et_model_time_minute.setText(String.format("%s", mTimeSetting.baseTime))
                et_model_time_km_out.setText(String.format("%s", mTimeSetting.unitDistance))
                et_model_time_minute_more.setText(String.format("%s", mTimeSetting.unitTime))
                deliveryThingEt.setText(String.format("%s", mTimeSetting.readyTime))
            } else {
                //按时间段
                cb_model_time_section.isChecked = true
                mTimeList = mTimeSetting.timeSections ?: arrayListOf()
                mTimeAdapter.replaceData(mTimeList)
            }
        }
    }

}