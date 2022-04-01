package com.lingmiao.shop.business.me.fragment

import android.os.Bundle
import android.view.View
import com.james.common.base.BaseFragment
import com.james.common.utils.exts.getViewText
import com.james.common.utils.exts.singleClick
import com.lingmiao.shop.R
import com.lingmiao.shop.business.me.presenter.DeliveryOfRiderPresenter
import com.lingmiao.shop.business.me.presenter.impl.DeliveryInOfRiderPresenterImpl
import com.lingmiao.shop.business.tools.adapter.PriceAdapter
import com.lingmiao.shop.business.tools.adapter.RangeRiderAdapter
import com.lingmiao.shop.business.tools.adapter.TimeAdapter
import com.lingmiao.shop.business.tools.bean.*
import com.lingmiao.shop.util.initAdapter
import kotlinx.android.synthetic.main.me_fragment_delivery_of_rider.*

/**
Create Date : 2021/3/53:40 PM
Auther      : Fox
Desc        :
 **/
class DeliveryOfRiderFragment : BaseFragment<DeliveryOfRiderPresenter>(),
    DeliveryOfRiderPresenter.View {

    private lateinit var mPriceAdapter: PriceAdapter
    private lateinit var mRangeAdapter: RangeRiderAdapter
    private lateinit var mTimeAdapter: TimeAdapter

    private lateinit var mTimeList: MutableList<TimeSection>
    private lateinit var mRangeList: MutableList<PeekTime>
    private lateinit var mPriceList: MutableList<DistanceSection>
    private lateinit var mTimeValueList: MutableList<TimeValue>
    private lateinit var mDayTypeList: MutableList<String>


    //qiqi
    //
    private var mItem: FreightVoItem? = null

    private var mShopItem: FreightVoItem? = null

    var mFeeSetting: FeeSettingVo = FeeSettingVo()

    var mTimeSetting: TimeSettingVo = TimeSettingVo()

    companion object {
        fun newInstance(item: FreightVoItem?): DeliveryOfRiderFragment {
            return DeliveryOfRiderFragment().apply {
                arguments = Bundle().apply {
                    putSerializable("item", item)
                }
            }
        }
    }

    override fun initBundles() {
        mItem = arguments?.getSerializable("item") as FreightVoItem?
    }

    override fun getLayoutId() = R.layout.me_fragment_delivery_of_rider

    override fun createPresenter() = DeliveryInOfRiderPresenterImpl(this)

    override fun initViewsAndData(rootView: View) {

        //无用
        initPricePart()

        //加收费用
        initRangePart()

        //按时间段的配送时效
        initTimePart()

        updateCityExpressPayTypeCheckBox()

        //配货时间
        deliveryThingEt.isEnabled = false
        //起送价
        model_km_price.isEnabled = false
        //配送费
        model_price_km.isEnabled = false
        model_price_price.isEnabled = false
        model_price_km_out.isEnabled = false
        model_price_minute_more.isEnabled = false
        //配送范围
        model_out_range_km.isEnabled = false

        //配送时效
        //按公里数
        et_model_time_km_rider.isEnabled = false
        et_model_time_minute_rider.isEnabled = false
        et_model_time_km_out_rider.isEnabled = false
        et_model_time_minute_more_rider.isEnabled = false

        cb_model_time_km_rider.isEnabled = false
        cb_model_time_section_rider.isEnabled = false

        tvShopSettingSubmit.singleClick {

            //骑手未接单转商家配送时间
            if (deliveryShiftTimeEt.getViewText().isEmpty()) {
                showToast("请填写骑手未接单转商家配送时间")
                return@singleClick
            }
            //商家配送必有
            if (mShopItem == null || mShopItem?.id == null || mShopItem?.id?.length == 0) {
                showToast("请先配置商家配送")
                return@singleClick
            }
            val setting = TimeSettingVo()

            if (shiftDeliveryCb.isChecked) {
                setting.isAllowTransTemp = 1
                setting.transTempLimitTime = deliveryShiftTimeEt.getViewText().toInt()
            } else {
                setting.isAllowTransTemp = 0
                setting.transTempLimitTime = 0
            }

            mPresenter?.updateModel(
                mShopItem?.id!!,
                setting.isAllowTransTemp!!,
                setting.transTempLimitTime!!
            )
        }

        if (mItem == null) {
            mPresenter?.getTemplate()
        } else {
            setUi()
            mPresenter?.getTemplate()
        }

        //加载商家配送模板，需要现有商家配送模板才能有骑手配送
        mPresenter?.getShopTemplate()
    }

    override fun onSetShopTemplate(item: FreightVoItem?) {
        mShopItem = item ?: FreightVoItem()
        val mTimeSetting = mPresenter?.getTimeSetting(item) ?: TimeSettingVo()
        deliveryShiftTimeEt.setText(mTimeSetting.transTempLimitTime?.toString())
        shiftDeliveryCb.isChecked = mTimeSetting.isAllowTransTemp == 1
    }

    /**
     * 同城配送的计费方式
     */
    private fun updateCityExpressPayTypeCheckBox() {
        rg_model_pay_km.setOnCheckedChangeListener { _, checkedId ->
            if (checkedId == R.id.cb_model_pay_km_section) {
                rider_rv_model_price.visibility = View.VISIBLE
                model_price_section.visibility = View.GONE
            } else if (checkedId == R.id.cb_model_pay_km_num) {
                rider_rv_model_price.visibility = View.GONE
                model_price_section.visibility = View.VISIBLE
            }
        }
        rider_rv_model_price.visibility = View.GONE
        model_price_section.visibility = View.VISIBLE
    }

    private fun initRangePart() {
        mRangeList = arrayListOf()
        val temp = PeekTime().apply {
            enable = false
        }
        mRangeList.add(temp)
        mRangeAdapter = RangeRiderAdapter().apply {
        }
        rider_rv_model_range.initAdapter(mRangeAdapter)
        mRangeAdapter.replaceData(mRangeList)
    }

    private fun initTimePart() {

        mTimeValueList = TimeValue.getTimeList()
        mDayTypeList = mutableListOf()
        mDayTypeList.add("当日")
        mDayTypeList.add("次日")

        mTimeList = arrayListOf()
        mTimeList.add(TimeSection())

        mTimeAdapter = TimeAdapter()
        rv_model_time_rider.initAdapter(mTimeAdapter)
        mTimeAdapter.replaceData(mTimeList)
    }

    private fun initPricePart() {
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

        rider_rv_model_price.initAdapter(mPriceAdapter)

        mPriceAdapter.replaceData(mPriceList)
    }

    override fun updateModelSuccess(b: Boolean) {
        showToast("提交成功")
    }

    private fun setUi() {

        // 起送价
        model_km_price.setText(String.format("%s", mItem?.baseShipPrice))
        // 配送范围
        model_out_range_km.setText(String.format("%s", mItem?.shipRange))

        mFeeSetting = mPresenter?.getFeeSetting(mItem) ?: FeeSettingVo()
        mTimeSetting = mPresenter?.getTimeSetting(mItem) ?: TimeSettingVo()

        mFeeSetting.apply {
            // 加收费用
            mRangeList = mFeeSetting.peekTimes ?: arrayListOf()
            for (i in mRangeList) {
                i.enable = false
            }
            mRangeAdapter.replaceData(mRangeList)
            //配送费
            cb_model_pay_km_num.isChecked = true
            model_price_km.setText(String.format("%s", mFeeSetting.baseDistance))
            model_price_price.setText(String.format("%s", mFeeSetting.basePrice))
            model_price_km_out.setText(String.format("%s", mFeeSetting.unitDistance))
            model_price_minute_more.setText(String.format("%s", mFeeSetting.unitPrice))
        }

        if (mItem?.is_rider_to_seller == 1) {
            shiftDeliveryCb.isChecked = true
            try {
                deliveryShiftTimeEt.setText(mItem?.rider_to_seller_time.toString())
            } catch (e: Exception) {
            }
        } else {
            shiftDeliveryCb.isChecked = false
        }
        mTimeSetting.apply {
            if (isBaseTimeType()) {
                //按公里数
                cb_model_time_km_rider.isChecked = true
                et_model_time_km_rider.setText(String.format("%s", mTimeSetting.baseDistance))
                et_model_time_minute_rider.setText(String.format("%s", mTimeSetting.baseTime))
                et_model_time_km_out_rider.setText(String.format("%s", mTimeSetting.unitDistance))
                et_model_time_minute_more_rider.setText(String.format("%s", mTimeSetting.unitTime))
                //配送时间
                deliveryThingEt.setText(String.format("%s", mTimeSetting.readyTime))
            } else {
                //按时间段
                cb_model_time_section_rider.isChecked = true
                mTimeList = mTimeSetting.timeSections ?: arrayListOf()
                mTimeAdapter.replaceData(mTimeList)
            }
        }
    }

    override fun setModel(item: FreightVoItem?) {
        mItem = item ?: FreightVoItem()
        setUi()
    }

    override fun onSetShopDeliveryStatus(size: Int, item: FreightVoItem?) {
        shiftDeliveryCb.isChecked = size > 0
        if (size <= 0) {
            showToast("请先设置商家配置送模板")
        }
        mShopItem = item ?: FreightVoItem()
        mTimeSetting = mPresenter?.getTimeSetting(item) ?: TimeSettingVo()
    }

}