package com.lingmiao.shop.business.me.fragment

import android.annotation.SuppressLint
import android.graphics.Color
import android.os.Bundle
import android.text.Spannable
import android.text.SpannableStringBuilder
import android.text.style.ForegroundColorSpan
import android.view.View
import android.widget.TextView
import com.james.common.base.BaseFragment
import com.james.common.utils.DialogUtils
import com.james.common.utils.exts.getViewText
import com.james.common.utils.exts.singleClick
import com.lingmiao.shop.R
import com.lingmiao.shop.business.me.presenter.DeliveryInTimePresenter
import com.lingmiao.shop.business.me.presenter.impl.DeliveryInTimePresenterImpl
import com.lingmiao.shop.business.tools.adapter.RangeAdapter
import com.lingmiao.shop.business.tools.adapter.TimeAdapter
import com.lingmiao.shop.business.tools.api.JsonUtil
import com.lingmiao.shop.business.tools.bean.*
import com.lingmiao.shop.business.tools.pop.DayPop
import com.lingmiao.shop.business.tools.pop.TimeListPop
import com.lingmiao.shop.util.initAdapter
import kotlinx.android.synthetic.main.me_fragment_delivery_in_time.*
import kotlinx.android.synthetic.main.me_fragment_delivery_in_time.et_model_out_range_km
import kotlinx.android.synthetic.main.tools_include_model_price.*
import kotlinx.android.synthetic.main.tools_include_model_range.*
import kotlinx.android.synthetic.main.tools_include_model_time.*

/**
Create Date : 2021/3/53:40 PM
Auther      : Fox
Desc        :
 **/
@SuppressLint("NotifyDataSetChanged")
class DeliveryInTimeFragment : BaseFragment<DeliveryInTimePresenter>(),
    DeliveryInTimePresenter.View {

    //加收费用Adapter
    private lateinit var mRangeAdapter: RangeAdapter

    //按时间段配送时效Adapter
    private lateinit var mTimeAdapter: TimeAdapter

    //加收费用列表
    private lateinit var mRangeList: MutableList<PeekTime>

    //按时间段配送时效列表
    private lateinit var mTimeList: MutableList<TimeSection>

    //24小时时间列表
    private lateinit var mTimeValueList: MutableList<TimeValue>

    //按时间段配送时效当日次日
    private lateinit var mDayTypeList: MutableList<String>

    //配送模板
    private var mItem: FreightVoItem? = null

    private var mFeeSetting: FeeSettingVo = FeeSettingVo()

    //配送时效
    private var mTimeSetting: TimeSettingVo = TimeSettingVo()


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

        val text1 = "按公里数（即时配送）"
        val text2 = "按时间段（定时配送）"
        val builder1 = SpannableStringBuilder(text1)
        val builder2 = SpannableStringBuilder(text2)
        val blueSpan1 = ForegroundColorSpan(Color.parseColor("#CACACA"))
        val blueSpan2 = ForegroundColorSpan(Color.parseColor("#CACACA"))
        builder1.setSpan(blueSpan1, 4, 10, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
        builder2.setSpan(blueSpan2, 4, 10, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
        cb_model_time_km.text = builder1
        cb_model_time_section.text = builder2

        //加收费用
        initRangePart()

        //按时间段的配送时效
        initTimePart()

        //选择配送时效
        updateTimeCheckBox()

        //保存
        tvShopSettingSubmit.singleClick {

            val setting = mTimeSetting
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
                if (et_model_price_km_out.getViewText() == "0") {
                    showToast("配送费中每超出公里数请勿设置为0")
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
                    if (et_model_time_km_out.getViewText() == "0") {
                        showToast("按公里数配送中每超出公里数请勿设置为0")
                        return@singleClick
                    }
                }
                if (cb_model_time_section.isChecked) {
                    //按时间
                    if (mTimeList.size == 0) {
                        showToast("请填完配送时效")
                        return@singleClick
                    } else {
                        //先检查第一个
                        if (mTimeList[0].arriveTime.isNullOrEmpty() || mTimeList[0].shipTime.isNullOrEmpty() || mTimeList[0].arriveStartTime.isNullOrEmpty()) {
                            showToast("请填完配送时效")
                            return@singleClick
                        }
                    }
                }

                //设置数据给Item
                name = "商家配送"
                // 基础（默认）
                templateType = FreightVoItem.TYPE_LOCAL
                //useless
                type =
                    if (cb_model_pay_km_section.isChecked) FreightVoItem.TYPE_KM_SECTION else FreightVoItem.TYPE_KM_COUNT
                //起送价格
                baseShipPrice = et_model_km_price.getViewText()
                //配送范围
                shipRange = et_model_out_range_km.getViewText()
                //配送时效
                //按公里或者时间

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

                // 计费
                //配送费
                mFeeSetting.feeType = FeeSettingVo.FEE_TYPE_DISTANCE
                mFeeSetting.baseDistance = et_model_price_km.getViewText()
                mFeeSetting.basePrice = et_model_price_price.getViewText()
                mFeeSetting.unitDistance = et_model_price_km_out.getViewText()
                mFeeSetting.unitPrice = et_model_price_minute_more.getViewText()

                // 清数据（无用数据）
                mFeeSetting.distanceSections = null


                // 快递 clear data
                items = null
                type = null
            }

            mItem?.apply {

                //判断加收费用和按时间段配送时效是否完整

                //配送时效
                val temp1 = mutableListOf<Int>()
                val temp2 = mutableListOf<TimeSection>()
                //加收费用
                val temp3 = mutableListOf<Int>()
                val temp4 = mutableListOf<PeekTime>()

                //配送时效
                if (cb_model_time_section.isChecked) {
                    for ((number, i) in setting.timeSections!!.withIndex()) {
                        if (!(i.arriveTime.isNullOrEmpty() || i.shipTime.isNullOrEmpty() || i.arriveStartTime.isNullOrEmpty())) {
                            //保存完整数据
                            temp1.add(number)
                        }
                    }
                }
                //加收费用
                for ((number, i) in mFeeSetting.peekTimes!!.withIndex()) {
                    if (!(i.peekTimeEnd.isNullOrEmpty() || i.peekTimePrice.isNullOrEmpty() || i.peekTimeStart.isNullOrEmpty())) {
                        temp3.add(number)
                    }
                }

                if (cb_model_time_section.isChecked) {
                    //temp1  temp3保存了完整的数据
                    //1 配送时效不完整加收费用不完整  2 配送时效不完整加收费用完整   3 配送时效完整加收费用不完整  4 配送时效完整加收费用完整
                    var type = 0
                    if (temp1.size < mTimeList.size && temp3.size < mFeeSetting.peekTimes?.size ?: 0) {
                        type = 1
                    }
                    if (temp1.size < mTimeList.size && temp3.size >= mFeeSetting.peekTimes?.size ?: 0) {
                        type = 2
                    }
                    if (temp1.size >= mTimeList.size && temp3.size < mFeeSetting.peekTimes?.size ?: 0) {
                        type = 3
                    }
                    if (temp1.size >= mTimeList.size && temp3.size >= mFeeSetting.peekTimes?.size ?: 0) {
                        type = 4
                    }
                    when (type) {
                        1 -> {
                            DialogUtils.showDialog(
                                requireActivity(),
                                "配送时效与加收费用未填写完整", "请选择填写完整或删除未完整数据",
                                "重新填写", "确定删除",
                                null
                            ) {
                                for (i in temp1) {
                                    temp2.add(mTimeList[i])
                                }
                                setting.timeSections = temp2
                                timeSettingVo = TimeSettingReqVo(setting)
                                timeSetting = JsonUtil.instance.toJson(setting)

                                for (i in temp3) {
                                    mFeeSetting.peekTimes?.get(i)?.let { it1 -> temp4.add(it1) }
                                }
                                mFeeSetting.peekTimes = temp4
                                feeSetting = JsonUtil.instance.toJson(mFeeSetting)
                                feeSettingVo = FeeSettingReqVo(mFeeSetting)
                                mPresenter?.addModel(mItem!!, true)
                            }
                        }
                        2 -> {
                            DialogUtils.showDialog(
                                requireActivity(),
                                "配送时效未填写完整", "请选择填写完整或删除未完整数据",
                                "重新填写", "确定删除",
                                null
                            ) {
                                for (i in temp1) {
                                    temp2.add(mTimeList[i])
                                }
                                setting.timeSections = temp2
                                timeSettingVo = TimeSettingReqVo(setting)
                                timeSetting = JsonUtil.instance.toJson(setting)

                                feeSetting = JsonUtil.instance.toJson(mFeeSetting)
                                feeSettingVo = FeeSettingReqVo(mFeeSetting)
                                mPresenter?.addModel(mItem!!, true)
                            }

                        }
                        3 -> {
                            DialogUtils.showDialog(
                                requireActivity(),
                                "加收费用未填写完整", "请选择填写完整或删除未完整数据",
                                "重新填写", "确定删除",
                                null
                            ) {
                                timeSettingVo = TimeSettingReqVo(setting)
                                timeSetting = JsonUtil.instance.toJson(setting)

                                for (i in temp3) {
                                    mFeeSetting.peekTimes?.get(i)?.let { it1 -> temp4.add(it1) }
                                }
                                mFeeSetting.peekTimes = temp4
                                feeSetting = JsonUtil.instance.toJson(mFeeSetting)
                                feeSettingVo = FeeSettingReqVo(mFeeSetting)
                                mPresenter?.addModel(mItem!!, true)
                            }
                        }
                        4 -> {
                            timeSettingVo = TimeSettingReqVo(setting)
                            timeSetting = JsonUtil.instance.toJson(setting)
                            feeSetting = JsonUtil.instance.toJson(mFeeSetting)
                            feeSettingVo = FeeSettingReqVo(mFeeSetting)
                            mPresenter?.addModel(mItem!!, true)
                        }
                        else -> {
                        }
                    }
                } else {
                    //按时间段配送时效无，不用管
                    timeSettingVo = TimeSettingReqVo(setting)
                    timeSetting = JsonUtil.instance.toJson(setting)

                    if (temp3.size < mFeeSetting.peekTimes?.size ?: 0) {
                        //加收费用不完整
                        DialogUtils.showDialog(
                            requireActivity(),
                            "加收费用未填写完整", "请选择填写完整或删除未完整数据",
                            "重新填写", "确定删除",
                            null
                        ) {
                            for (i in temp3) {
                                mFeeSetting.peekTimes?.get(i)?.let { it1 -> temp4.add(it1) }
                            }
                            mFeeSetting.peekTimes = temp4
                            feeSetting = JsonUtil.instance.toJson(mFeeSetting)
                            feeSettingVo = FeeSettingReqVo(mFeeSetting)
                            mPresenter?.addModel(mItem!!, true)
                        }


                    } else {
                        //加收费用完整
                        feeSetting = JsonUtil.instance.toJson(mFeeSetting)
                        feeSettingVo = FeeSettingReqVo(mFeeSetting)
                        mPresenter?.addModel(mItem!!, true)
                    }
                }
            }


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
                    pop.setOnClickListener {
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
                    pop.setOnClickListener {
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
                    pop.setOnClickListener {
                        run {
                            item.shipTime = it.name
                            item.shipTimeCount = it.value
                            mTimeAdapter.notifyDataSetChanged()
                        }
                    }
                    pop.shiftStartTime(item.arriveTimeCount ?: TimeValue.getLastTimeCount())
                    pop.showPopupWindow()
                }
                //送达时间上界
                else if (view.id == R.id.et_model_km_endo) {
                    val pop = TimeListPop(requireContext(), mTimeValueList)
                    pop.setOnClickListener {
                        item.arriveStartTime = it.name
                        item.arriveTimeCount = it.value
                        run {
                            mTimeAdapter.notifyDataSetChanged()
                        }
                    }
                    pop.shiftEndTime(if (item.isToday()) item.shipTimeCount ?: -1 else -1)
                    pop.showPopupWindow()
                }
                //送达时间下界
                else if (view.id == R.id.et_model_km_end) {
                    val pop = TimeListPop(requireContext(), mTimeValueList)
                    pop.setOnClickListener {
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
                    val pop = DayPop(requireContext(), mDayTypeList, "请选择配送日期")
                    pop.setOnClickListener { it, position1 ->

                        run {
                            if (position1 == 0) {
                                //选了当日
                                item.shiftToday()
                                if (item.arriveTimeCount ?: 0 <= item.shipTimeCount ?: 0) {
                                    item.arriveTime = null
                                    item.arriveTimeCount = null
                                    mTimeAdapter.notifyDataSetChanged()
                                }
                            } else {
                                //选择了次日
                                item.shiftTomorrow()
                            }
                            view.findViewById<TextView>(R.id.tv_model_time_type).text = it
                            /// tv_model_time_type.text = it
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


    override fun updateModelSuccess(b: Boolean, type: Boolean) {
        showToast("提交成功")
        if (type) {
            mPresenter?.getTemplate(FreightVoItem.TYPE_LOCAL)
        }
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
            } else {
                //按时间段
                cb_model_time_section.isChecked = true
                mTimeList = mTimeSetting.timeSections ?: arrayListOf()
                mTimeAdapter.replaceData(mTimeList)
            }
            //配货时间
            deliveryThingEt.setText(String.format("%s", mTimeSetting.readyTime))
        }
    }

}