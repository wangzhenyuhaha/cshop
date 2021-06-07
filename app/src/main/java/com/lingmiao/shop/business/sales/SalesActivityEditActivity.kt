package com.lingmiao.shop.business.sales

import android.app.Activity
import android.content.Intent
import com.bigkoo.pickerview.view.TimePickerView
import com.blankj.utilcode.util.KeyboardUtils
import com.james.common.base.BaseActivity
import com.james.common.exception.BizException
import com.james.common.utils.exts.*
import com.lingmiao.shop.R
import com.lingmiao.shop.business.goods.GoodsSalesSelectActivity
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

    companion object {

        const val KEY_ITEM = "KEY_ITEM"

        fun open(context: Activity, result : Int) {
            edit(context, null, result);
        }

        fun edit(context: Activity, item: SalesVo?, result : Int) {
            val intent = Intent(context, SalesActivityEditActivity::class.java)
            intent.putExtra(KEY_ITEM, item)
            context.startActivityForResult(intent, result)
        }

    }


    override fun initBundles() {
        mItem = intent?.getSerializableExtra(KEY_ITEM) as SalesVo?;
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

    var pvCustomTime : TimePickerView?= null;
    var pvCustomTime2 : TimePickerView? = null;

    var mItem : SalesVo? = null;

    override fun initView() {
        mToolBarDelegate.setMidTitle(getString(R.string.sales_activity_edit_title))

        activityGoodsPickTv.singleClick {
            GoodsSalesSelectActivity.sales(context, "3");
        }
        initPricePart();

        initDate()

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
            if(mItem == null) {
                mItem = SalesVo();
            }
            mItem?.title = menuNameEdt.getViewText()
            mItem?.startTime = dateTime2Date(firstMenuTv.getViewText())?.time;
            mItem?.endTime = dateTime2Date(secondMenuTv.getViewText())?.time;

            mItem?.fullType = "MONEY"
            val it = mDiscountAdapter.data.get(0);
            mItem?.fullMoney = it.peach;
            mItem?.minusValue = it.least
            mItem?.rangeType = 1;

            mPresenter?.submitDiscount(mItem);
        }


        if(mItem != null) {
            resetUi();
        } else {
            mDiscountList.add(SalesActivityItemVo());
            mDiscountAdapter.replaceData(mDiscountList);
        }
    }

    fun resetUi() {
        menuNameEdt.setText(mItem?.title)
        firstMenuTv.setText(longToDate(mItem?.startTime, DATE_TIME_FORMAT))
        secondMenuTv.setText(longToDate(mItem?.endTime, DATE_TIME_FORMAT))
        mDiscountAdapter?.addData(mItem?.convertDiscountItem()!!);
    }

    private lateinit var mDiscountList : MutableList<SalesActivityItemVo>;

    private lateinit var mDiscountAdapter : ActivitySalesAdapter;

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
                firstMenuTv.text = formatDateTime(date)
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
                secondMenuTv.setText(formatDateTime(date))
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


}