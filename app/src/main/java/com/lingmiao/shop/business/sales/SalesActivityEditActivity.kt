package com.lingmiao.shop.business.sales

import android.app.Activity
import android.content.Intent
import android.view.View
import com.bigkoo.pickerview.view.TimePickerView
import com.blankj.utilcode.util.KeyboardUtils
import com.james.common.base.BaseActivity
import com.james.common.exception.BizException
import com.james.common.utils.exts.*
import com.lingmiao.shop.R
import com.lingmiao.shop.business.goods.api.bean.GoodsVO
import com.lingmiao.shop.business.sales.adapter.ActivitySalesAdapter
import com.lingmiao.shop.business.sales.bean.SalesActivityItemVo
import com.lingmiao.shop.business.sales.bean.SalesVo
import com.lingmiao.shop.business.sales.presenter.ISalesEditPresenter
import com.lingmiao.shop.business.sales.presenter.impl.SalesActivityEditPreImpl
import com.lingmiao.shop.util.*
import kotlinx.android.synthetic.main.sales_activity_activity_edit.*
import java.util.*


/**
Create Date : 2021/3/126:05 AM
Auther      : Fox
Desc        :
 **/
class SalesActivityEditActivity : BaseActivity<ISalesEditPresenter>(), ISalesEditPresenter.PubView {

    var pvCustomTime : TimePickerView?= null;
    var pvCustomTime2 : TimePickerView? = null;

    var mItem : SalesVo? = null;
    var mViewType : Int? = 0

    private lateinit var mDiscountList : MutableList<SalesActivityItemVo>;

    private lateinit var mDiscountAdapter : ActivitySalesAdapter;

    companion object {

        const val KEY_ITEM = "KEY_ITEM"
        const val KEY_VIEW_TYPE = "KEY_VIEW_TYPE"
        const val VALUE_ALL = 1;
        const val VALUE_PART = 2;
        val REQUEST_GOODS = 299;

        fun open(context: Activity, result : Int) {
            edit(context, null, result);
        }

        fun edit(context: Activity, item: SalesVo?, result : Int) {
            val intent = Intent(context, SalesActivityEditActivity::class.java)
            intent.putExtra(KEY_ITEM, item)
            context.startActivityForResult(intent, result)
        }

        fun view(context: Activity, item: SalesVo?) {
            val intent = Intent(context, SalesActivityEditActivity::class.java)
            intent.putExtra(KEY_ITEM, item)
            intent.putExtra(KEY_VIEW_TYPE, VALUE_PART)
            context.startActivity(intent)
        }

    }


    override fun initBundles() {
        mItem = intent?.getSerializableExtra(KEY_ITEM) as SalesVo?;
        mViewType = intent?.getIntExtra(KEY_VIEW_TYPE, 0);
    }

    override fun createPresenter(): ISalesEditPresenter {
        return SalesActivityEditPreImpl(this, this);
    }

    override fun getLayoutId(): Int {
        return R.layout.sales_activity_activity_edit;
    }

    override fun useLightMode(): Boolean {
        return false;
    }

    override fun initView() {
        mToolBarDelegate.setMidTitle(getString(R.string.sales_activity_edit_title))



        initPricePart();

        initDate()

        if(mViewType == VALUE_PART) {
            submitTv.gone()
        } else {
            submitTv.visiable()
            activityGoodsPickTv.singleClick {
                SalesGoodsActivity.edit(context, mItem, REQUEST_GOODS);
            }
        }
        submitTv.singleClick {
            try {
                checkNotBlack(menuNameEdt.getViewText()) { "请输入活动名称" };

                checkBoolean(firstMenuTv.getViewText() != "请选择") { "请输入活动开始时间" };

                checkBoolean(secondMenuTv.getViewText() != "请选择") { "请输入活动开始时间" };

            } catch (e: BizException) {
                showToast(e.message.check())
                return@singleClick;
            }

            if(mDiscountAdapter.data.size == 0 || mDiscountAdapter.data[0] == null) {
                showToast("请输入满减金额")
                return@singleClick;
            }
            if(mItem?.rangeType == null) {
                showToast("请选择参与活动的商品")
                return@singleClick;
            }

            mItem?.title = menuNameEdt.getViewText()
            val s = dateTime2Date(firstMenuTv.getViewText())?.time?:0;
            val e = dateTime2Date(secondMenuTv.getViewText())?.time?:0;
            mItem?.startTime = s/1000;
            mItem?.endTime = e/1000;

            mItem?.fullType = "MONEY"
            val it = mDiscountAdapter.data.get(0);
            mItem?.fullMoney = it.peach;
            mItem?.minusValue = it.least

            if(mItem?.id?.isNotEmpty() ==true) {
                mPresenter?.update(mItem);
            } else {
                mPresenter?.submitDiscount(mItem);
            }
        }


        if(mItem != null) {
            resetUi();
        } else {
            mItem = SalesVo();
            mDiscountList.add(SalesActivityItemVo());
            mDiscountAdapter.replaceData(mDiscountList);
        }
    }

    fun resetUi() {
        menuNameEdt.setText(mItem?.title)
        firstMenuTv.setText(stampToDate(mItem?.startTime, MINUTES_TIME_FORMAT))
        secondMenuTv.setText(stampToDate(mItem?.endTime, MINUTES_TIME_FORMAT))
        mDiscountAdapter?.addData(mItem?.convertDiscountItem()!!);
        setGoodsUi();

        if(mItem?.status.equals("UNDERWAY")) {
            submitTv.gone();
        }

    }

    private fun initPricePart() {
        mDiscountList = arrayListOf();
        mDiscountAdapter = ActivitySalesAdapter().apply {
            setOnItemChildClickListener { adapter, view, position ->
                val item = adapter.data[position] as SalesActivityItemVo;
                if (view.id == R.id.discountDeleteTv && position != 0) {
                    mDiscountList.remove(item);
                    mDiscountAdapter.replaceData(mDiscountList);
                }
            }

//            val footer = View.inflate(context, R.layout.sales_footer_activity_add, null);
//            footer.findViewById<View>(R.id.activityAddTv).setOnClickListener {
//                mDiscountList.add(SalesActivityItemVo());
//                mDiscountAdapter.replaceData(mDiscountList);
//            }
//            addFooterView(footer)
        };

        activityRv.initAdapter(mDiscountAdapter);
        mDiscountAdapter.replaceData(mDiscountList);
    }

    fun initDate() {
        // 系统当前时间
        val selectedDate: Calendar = Calendar.getInstance()
        val startDate: Calendar = Calendar.getInstance()
        startDate.set(selectedDate.get(Calendar.YEAR), selectedDate.get(Calendar.MONTH), selectedDate.get(Calendar.DATE))

        val endDate: Calendar = Calendar.getInstance()
        endDate.set(selectedDate.get(Calendar.YEAR )+ 5, selectedDate.get(Calendar.MONTH), selectedDate.get(Calendar.DATE))


        firstMenuTv.singleClick {
            KeyboardUtils.hideSoftInput(it)
            //时间选择器 ，自定义布局
            pvCustomTime = getDefaultTimePicker(this, selectedDate, startDate, endDate , { date, view ->
                firstMenuTv.text = formatString(date, MINUTES_TIME_FORMAT)
                //firstMenuTv.setText(formatDateTime(date))
            }, {
                pvCustomTime?.returnData()
                pvCustomTime?.dismiss()
            }, {
                pvCustomTime?.dismiss()
            });
            pvCustomTime?.show();
        }

        secondMenuTv.singleClick {
            KeyboardUtils.hideSoftInput(it)
            //时间选择器 ，自定义布局
            pvCustomTime2 = getDefaultTimePicker(this, selectedDate, startDate, endDate , { date, view ->
                secondMenuTv.setText(formatString(date, MINUTES_TIME_FORMAT))
            }, {
                pvCustomTime2?.returnData()
                pvCustomTime2?.dismiss()
            }, {
                pvCustomTime2?.dismiss()
            });
            pvCustomTime2?.show();
        }
    }

    override fun onSubmitDiscount() {
        setResult(Activity.RESULT_OK)
        finish();
    }

    override fun onUpdate() {
        setResult(Activity.RESULT_OK)
        finish();
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
            if(requestCode == REQUEST_GOODS) {
                if(mItem == null) {
                    mItem = SalesVo();
                }

                mItem?.rangeType = data?.getIntExtra("type", VALUE_ALL);
                mItem?.goodsList = data?.getSerializableExtra("goodsList") as ArrayList<GoodsVO>?;
                setGoodsUi();
            }
        }
    }

    fun setGoodsUi() {
        var str = "请选择参与活动的商品";
        when(mItem?.rangeType) {
            VALUE_ALL -> {
                str = "全部商品参与";
            }
            VALUE_PART -> {
                str = "部分商品参与";
            }
        }
        activityGoodsPickTv.setText(str)
    }

}