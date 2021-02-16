package com.lingmiao.shop.business.tools

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.blankj.utilcode.util.LogUtils
import com.lingmiao.shop.R
import com.lingmiao.shop.base.IConstant
import com.lingmiao.shop.business.tools.adapter.AreaOfWeightAdapter
import com.lingmiao.shop.business.tools.adapter.PriceAdapter
import com.lingmiao.shop.business.tools.adapter.RangeAdapter
import com.lingmiao.shop.business.tools.adapter.TimeAdapter
import com.lingmiao.shop.business.tools.api.JsonUtil
import com.lingmiao.shop.business.tools.bean.*
import com.lingmiao.shop.business.tools.pop.DayPop
import com.lingmiao.shop.business.tools.pop.TimeListPop
import com.lingmiao.shop.business.tools.presenter.EditFreightModelPresenter
import com.lingmiao.shop.business.tools.presenter.impl.EditFreightModelPresenterImpl
import com.james.common.base.BaseActivity
import com.james.common.utils.exts.getViewText
import kotlinx.android.synthetic.main.tools_activity_freight_model_add.*
import kotlinx.android.synthetic.main.tools_adapter_time.*
import kotlinx.android.synthetic.main.tools_include_model_area.*
import kotlinx.android.synthetic.main.tools_include_model_price.*
import kotlinx.android.synthetic.main.tools_include_model_range.*
import kotlinx.android.synthetic.main.tools_include_model_time.*

class EditFreightModelActivity : BaseActivity<EditFreightModelPresenter>(), EditFreightModelPresenter.View {

    private lateinit var mAreaWeightAdapter : AreaOfWeightAdapter;
    private lateinit var mPriceAdapter : PriceAdapter;
    private lateinit var mRangeAdapter : RangeAdapter;
    private lateinit var mTimeAdapter : TimeAdapter;

    private lateinit var mTimeList : MutableList<TimeSection>;
    private lateinit var mRangeList : MutableList<PeekTime>;
    private lateinit var mPriceList : MutableList<DistanceSection>;
    private lateinit var mAreaWeightList : MutableList<Item>;
    private lateinit var mTimeValueList : MutableList<TimeValue>;
    private lateinit var mDayTypeList : MutableList<String>;

    private lateinit var id : String;
    private lateinit var mItem: FreightVoItem;

    private val TYPE_RESULT_WEIGHT = 2324;

    companion object {
        fun edit(context: Context, id : String?) {
            if (context is Activity) {
                val intent = Intent(context, EditFreightModelActivity::class.java)
                intent.putExtra(IConstant.BUNDLE_KEY_OF_ITEM_ID, id)
                context.startActivity(intent)
            }
        }
    }

    override fun initBundles() {
        id = intent?.getStringExtra(IConstant.BUNDLE_KEY_OF_ITEM_ID) ?: "";
    }

    override fun getLayoutId(): Int {
        return R.layout.tools_activity_freight_model_add;
    }

    override fun createPresenter(): EditFreightModelPresenter {
        return EditFreightModelPresenterImpl(this);
    }

    override fun useBaseLayout(): Boolean {
        return false
    }

    override fun initView() {
        mPresenter.onCreate();
        initTitle();

        updateExpressPayTypeCheckBox();
        updateCityExpressPayTypeCheckBox();
        updateTypeCheckbox();
        updateTimeCheckBox();
        cb_model_time_km.isChecked = true;
        rv_model_time.visibility = View.GONE;

        initAreaPart();
        initAreaWeightPart();
        initPricePart();
        initTimePart();
        initRangePart();

        if(isEditModel()) {
            mPresenter?.loadItem(id);
        } else {
            mItem = FreightVoItem();
        }
        mTimeValueList = TimeValue.getTimeList();
        mDayTypeList = mutableListOf();
        mDayTypeList.add("当日");
        mDayTypeList.add("次日");
    }

    private fun initTitle() {
        toolbarView?.apply {
            setTitleContent(if(isEditModel()) getString(R.string.tools_logistics_model_edit) else getString(R.string.tools_logistics_model_add))
            setRightListener(
                null,
                getString(R.string.save),
                R.color.color_3870EA
            ) {

                mItem?.apply {
                    name = et_model_name.getViewText();
                    if(cb_model_type_express_city.isChecked) {
                        // 基础
                        templateType = FreightVoItem.TYPE_LOCAL;
                        type = if(cb_model_pay_km_section.isChecked) FreightVoItem.TYPE_KM_SECTION else FreightVoItem.TYPE_KM_COUNT;

                        // 同城
                        baseShipPrice = et_model_km_price.getViewText();

                        shipRange = et_model_out_range_km.getViewText();
                        if(cb_model_time_km.isChecked) {
                            var setting = mTimeSetting;
                            setting.timeType = TimeSettingVo.TIME_TYPE_BASE;
                            setting?.baseDistance = et_model_time_km.getViewText();
                            setting?.baseTime = et_model_time_minute.getViewText();
                            setting?.unitDistance = et_model_time_km_out.getViewText();
                            setting?.unitTime = et_model_time_minute_more.getViewText();
                            // 清数据
                            setting.timeSections = null;

                            // 按公里数
                            timeSettingVo = TimeSettingReqVo(setting);
                            timeSetting = JsonUtil.instance.toJson(setting);
                        }
                        if(cb_model_time_section.isChecked){
                            var setting = mTimeSetting;
                            setting.timeType = TimeSettingVo.TIME_TYPE_SECTION;
                            setting.timeSections = mTimeList;
                            // 清数据
                            setting?.baseDistance = null;
                            setting?.baseTime = null;
                            setting?.unitDistance = null;
                            setting?.unitTime = null;
                            // 按公里段
                            timeSettingVo = TimeSettingReqVo(setting);
                            timeSetting = JsonUtil.instance.toJson(setting);
                        }
                        if(cb_model_pay_km_num.isChecked) {
                            mFeeSetting.feeType = FeeSettingVo.FEE_TYPE_DISTANCE;
                            mFeeSetting?.baseDistance = et_model_price_km.getViewText();
                            mFeeSetting?.basePrice = et_model_price_price.getViewText();
                            mFeeSetting?.unitDistance = et_model_price_km_out.getViewText();
                            mFeeSetting?.unitPrice = et_model_price_minute_more.getViewText();

                            // 清数据
                            mFeeSetting.distanceSections = null;

                            // 按公里数计费
                            feeSetting = JsonUtil.instance.toJson(mFeeSetting);
                            feeSettingVo = FeeSettingReqVo(mFeeSetting);
                        }
                        if(cb_model_pay_km_section.isChecked) {
                            mFeeSetting.feeType = FeeSettingVo.FEE_TYPE_SECTION;
                            mFeeSetting.distanceSections = mPriceList;
                            // 清数据
                            mFeeSetting?.baseDistance = null;
                            mFeeSetting?.basePrice = null;
                            mFeeSetting?.unitDistance = null;
                            mFeeSetting?.unitPrice = null;

                            // 按公里段计费
                            feeSetting = JsonUtil.instance.toJson(mFeeSetting);
                            feeSettingVo = FeeSettingReqVo(mFeeSetting);
                        }

                        // 快递 clear data
                        items = null;
                        type = null;
                    } else {
                        // 基础
                        templateType = FreightVoItem.TYPE_KUAIDI;
                        type = if(cb_model_pay_count.isChecked) FreightVoItem.TYPE_COUNT else FreightVoItem.TYPE_WEIGHT;

                        // 快递
                        items = mAreaWeightList;
                        // 同城 clear data
                        baseShipPrice = null;
                        shipRange = null;
                        timeSetting = null;
                        feeSetting = null;
                        minShipPrice = null;
                    }
                }

                if(et_model_name.getViewText() == null || et_model_name.getViewText().isEmpty()) {
                    showToast("请输入模板名称");
                    return@setRightListener;
                }
                if(cb_model_type_express_city.isChecked && (et_model_km_price.getViewText() == null || et_model_km_price.getViewText().isEmpty())) {
                    showToast("请输入起送价");
                    return@setRightListener;
                }

                if(mItem?.isKuaiDiType() && mItem?.items?.size ?: 0 == 0) {
                    showToast("请添加配送区域和运费");
                    return@setRightListener;
                }

                if(mItem?.id != null && mItem?.id?.length ?:0 > 0) {
                    mPresenter.updateModel(mItem);
                } else {
                    mPresenter.addModel(mItem);
                }

            }
        }
    }

    private fun isEditModel() : Boolean {
        return id != null && id?.length > 0;
    }

    private fun updateTypeCheckbox() {
        rg_model_type.setOnCheckedChangeListener { group, checkedId ->
            if(checkedId == R.id.cb_model_type_express) {
                rg_model_pay.visibility = View.VISIBLE;
                rg_model_pay_km.visibility = View.GONE;
                updateModelType(true);
            } else if(checkedId == R.id.cb_model_type_express_city) {
                rg_model_pay.visibility = View.GONE;
                rg_model_pay_km.visibility = View.VISIBLE;
                updateModelType(false);
            }
        }
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
     * 快递发货的计费方式
     */
    private fun updateExpressPayTypeCheckBox() {
        rg_model_pay.setOnCheckedChangeListener { group, checkedId ->
            if(checkedId == R.id.cb_model_pay_count) {
                updateModelType(true);
                shiftExpressPayType(false);
            } else if(checkedId == R.id.cb_model_pay_weight) {
                updateModelType(true);
                shiftExpressPayType(true);
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
    }

    /**
     * 切换同城和快递
     */
    private fun updateModelType(isChecked : Boolean) {
        if(isChecked) {
            ll_model_area.visibility = View.VISIBLE;
            ll_model_range.visibility = View.GONE;
            ll_model_price.visibility = View.GONE;
            ll_model_time.visibility = View.GONE;
        } else {
            ll_model_area.visibility = View.GONE;
            ll_model_range.visibility = View.VISIBLE;
            ll_model_price.visibility = View.VISIBLE;
            ll_model_time.visibility = View.VISIBLE;
        }
    }

    private fun initAreaPart() {
        mAreaWeightList = arrayListOf();
        mAreaWeightAdapter = AreaOfWeightAdapter().apply {
            setOnItemChildClickListener { adapter, view, position ->
                val item = adapter.data[position] as Item;
                if (view.id == R.id.tv_model_area_weight_delete) {
                    mAreaWeightList.remove(item);
                    mAreaWeightAdapter.replaceData(mAreaWeightList);
                } else if(view.id == R.id.tv_model_area_weight_edit) {
                    AreasActivity.edit(context, position, item, mItem?.tempArea, TYPE_RESULT_WEIGHT);
                }
            }
        };

        tv_model_area_add.setOnClickListener {
            AreasActivity.add(context, mItem?.tempArea, TYPE_RESULT_WEIGHT);
        }
    }

    private fun initAreaWeightPart() {
        rv_model_area.apply {
            layoutManager = initLayoutManager()
            adapter = mAreaWeightAdapter;
        }
        mAreaWeightAdapter.replaceData(mAreaWeightList);
        shiftExpressPayType(false);
    }

    private fun shiftExpressPayType(flag : Boolean) {
        if(flag) {
            mAreaWeightAdapter.shiftWeightType();
        } else {
            mAreaWeightAdapter.shiftCountType();
        }
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
                    val pop = TimeListPop(context, mTimeValueList)
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
                    val pop = TimeListPop(context, mTimeValueList)
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

    private fun initTimePart() {
        mTimeList = arrayListOf();
        mTimeList.add(TimeSection())

        mTimeAdapter = TimeAdapter().apply {

            setOnItemChildClickListener { adapter, view, position ->
                val item = adapter.data[position] as TimeSection;
                if (view.id == R.id.tv_model_time_delete && position != 0) {
                    mTimeList.remove(item);
                    mTimeAdapter.replaceData(mTimeList);
                } else if(view.id == R.id.et_model_km_start) {
                    val pop = TimeListPop(context, mTimeValueList)
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
                    val pop = TimeListPop(context, mTimeValueList)
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
                    val pop = DayPop(context, mDayTypeList)
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

    private fun initLayoutManager(): RecyclerView.LayoutManager {
        return LinearLayoutManager(this)
    }

    override fun loadItemSuccess(item: FreightVoItem) {
        LogUtils.dTag("delete model" + item?.id);
        refreshView(item);
    }

    override fun updateModelSuccess(flag: Boolean) {
        finish();
    }

    var mFeeSetting : FeeSettingVo = FeeSettingVo();
    var mTimeSetting : TimeSettingVo = TimeSettingVo();

    private fun refreshView(item: FreightVoItem) {
        var temp = mPresenter?.getAreas(item?.items);
        item.tempArea = temp;
        mItem = item;

        et_model_name.setText(item?.name);

        if(item?.isLocalTemplateType()) {
            // 模板名称
            cb_model_type_express_city.isChecked = true;
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
                } else {
                    cb_model_time_section.isChecked = true;
                    mTimeList = mTimeSetting?.timeSections ?: arrayListOf();
                    mTimeAdapter.replaceData(mTimeList);
                }
            }
        } else {
            cb_model_type_express.isChecked = true;
            mAreaWeightList = item?.items ?: arrayListOf();
            mAreaWeightAdapter.replaceData(mAreaWeightList);
            if(item?.isWeightType()) {
                cb_model_pay_weight.isChecked = true;
                shiftExpressPayType(true)
            } else {
                cb_model_pay_count.isChecked = true;
                shiftExpressPayType(false);
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(resultCode == Activity.RESULT_OK) {
            if(requestCode == TYPE_RESULT_WEIGHT) {
                var list = data?.getSerializableExtra(IConstant.BUNDLE_KEY_OF_ITEM) as HashMap<String, Any>
                var position = data?.getIntExtra(IConstant.BUNDLE_KEY_OF_ITEM_ID, -1);
                var regions = data?.getSerializableExtra("regions") as ArrayList<Region>;
                if(position < 0) {
                    mAreaWeightList?.add(Item());
                    mAreaWeightAdapter.replaceData(mAreaWeightList);
                    position = mAreaWeightList.size - 1;
                }

                mAreaWeightList[position]?.area = JsonUtil.instance.toJson(list);
                mAreaWeightList[position]?.regions = regions;

                mItem?.tempArea = mPresenter?.getAreas(mAreaWeightList);

                mAreaWeightAdapter?.replaceData(mAreaWeightList);
            }
        }
    }
}