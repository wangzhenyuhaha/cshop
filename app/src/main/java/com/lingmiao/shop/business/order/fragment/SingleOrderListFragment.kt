package com.lingmiao.shop.business.order.fragment

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Gravity
import android.view.View
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.TextView
import androidx.lifecycle.lifecycleScope
import com.bigkoo.pickerview.view.TimePickerView
import com.blankj.utilcode.util.ActivityUtils
import com.blankj.utilcode.util.LogUtils
import com.james.common.base.loadmore.BaseLoadMoreFragment
import com.james.common.base.loadmore.core.IPage
import com.james.common.utils.DialogUtils
import com.james.common.utils.exts.getViewText
import com.james.common.utils.exts.gone
import com.james.common.utils.exts.singleClick
import com.james.common.utils.exts.visiable
import com.james.common.view.ITextView
import com.lingmiao.shop.R
import com.lingmiao.shop.base.UserManager
import com.lingmiao.shop.business.main.bean.TabChangeEvent
import com.lingmiao.shop.business.order.*
import com.lingmiao.shop.business.order.adapter.OrderListAdapter
import com.lingmiao.shop.business.order.bean.OrderList
import com.lingmiao.shop.business.order.bean.OrderNumberEvent
import com.lingmiao.shop.business.order.presenter.OrderListPresenter
import com.lingmiao.shop.business.order.presenter.impl.OrderListPresenterImpl
import com.lingmiao.shop.printer.PrinterModule
import com.lingmiao.shop.util.*
import com.lingmiao.shop.widget.EmptyView
import kotlinx.android.synthetic.main.order_fragment_single_order.*
import kotlinx.coroutines.Delay
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import java.util.*

private const val STATUS = "param1"

/**
订单页
 */
class SingleOrderListFragment : BaseLoadMoreFragment<OrderList, OrderListPresenter>(),
    OrderListPresenter.StatusView {


    //当前页面类型，默认为ALL
    private var orderType: String? = "ALL"

    //备货中  配送中  已送达   进行中  已完成   已取消
    private var mCStatus: String? = null

    var pvCustomTime: TimePickerView? = null
    var pvCustomTime2: TimePickerView? = null
    var mStart: Long? = null
    var mEnd: Long? = null

    companion object {

        const val REQUEST_CODE = 64

        @JvmStatic
        fun newInstance(status: String) =
            SingleOrderListFragment().apply {
                arguments = Bundle().apply {
                    putString(STATUS, status)
                }
            }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        retainInstance = true
        super.onCreate(savedInstanceState)
    }

    override fun initBundles() {
        super.initBundles()

        //获取当前页面类型（五种）
        arguments?.let {
            orderType = it.getString(STATUS)
        }
    }

    override fun getLayoutId() = R.layout.order_fragment_single_order

    override fun useEventBus() = true

    var rbContinue: RadioButton? = null
    var rbComplete: RadioButton? = null
    var rbCancel: RadioButton? = null
    var rgAll: RadioGroup? = null

    //今日开始时间
    var startTime: Long? = null

    //今日结束时间
    var endTime: Long? = null

    override fun initOthers(rootView: View) {
        super.initOthers(rootView)

        // 设置对时间的监听
        initDate()

        when (orderType) {
            "PROCESSING" -> {
                rgEnable.visiable()
            }
            "COMPLETE" -> {
                dateLayout.visiable()
                orderResetTv.visiable()
                orderFilterTv.gone()
            }
            "ALL" -> {
                dateLayout.visiable()
                orderResetTv.visiable()
                orderFilterTv.visiable()
                orderStatusTv.visiable()
            }
            else -> {
                rgEnable.gone()
                dateLayout.gone()
                orderResetTv.gone()
                orderFilterTv.gone()
            }
        }

        //备货中等状态重置
        orderStatusResetTv.singleClick {
            rgEnable.clearCheck()
            mCStatus = null
            orderStatusTv.text = ""
            mLoadMoreDelegate?.refresh()
        }

        //选择备货中等状态
        rgEnable.setOnCheckedChangeListener { _, checkedId ->
            mCStatus = when (checkedId) {
                R.id.rbTaking -> {
                    orderStatusTv.text = "备货中"
                    "ACCEPT"
                }
                R.id.rbShipping -> {
                    orderStatusTv.text = "配送中"
                    "SHIPPED"
                }
                R.id.rbSign -> {
                    orderStatusTv.text = "已送达"
                    "ROG"
                }
                else -> {
                    null
                }
            }
            mLoadMoreDelegate?.refresh()
        }

        orderFilterTv.singleClick {
            drawerO.openDrawer(Gravity.RIGHT)
        }

        val headView: View = navigateView.inflateHeaderView(R.layout.order_view_menu_header)
        rgAll = headView.findViewById<RadioGroup>(R.id.rgAll)
        rgAll?.setOnCheckedChangeListener { _, checkedId ->
            mCStatus = when (checkedId) {
                R.id.rbContinue -> {
                    orderStatusTv.text = "进行中"
                    "PROCESSING"
                }
                R.id.rbComplete -> {
                    orderStatusTv.text = "已完成"
                    "COMPLETE"
                }
                R.id.rbCancel -> {
                    orderStatusTv.text = "已取消"
                    "CANCELLED"
                }
                else -> {
                    null
                }

            }
        }
        headView.findViewById<TextView>(R.id.tvReset).singleClick {
            rgAll?.clearCheck()
            drawerO.closeDrawers()
            mCStatus = null
            orderStatusTv.text = ""
            mLoadMoreDelegate?.refresh()
        }
        headView.findViewById<ITextView>(R.id.tvFinish).singleClick {
            drawerO.closeDrawers()
            mLoadMoreDelegate?.refresh()
        }
        rbContinue = headView.findViewById(R.id.rbContinue)
        rbComplete = headView.findViewById(R.id.rbComplete)
        rbCancel = headView.findViewById(R.id.rbCancel)

    }

    private fun initDate() {
        // 系统当前时间
        val selectedDate: Calendar = Calendar.getInstance()
        val startDate: Calendar = Calendar.getInstance()
        startDate.set(selectedDate.get(Calendar.YEAR), 1, 1)

        val endDate: Calendar = Calendar.getInstance()
        endDate.set(
            startDate.get(Calendar.YEAR) + 5, startDate.get(Calendar.MONTH), startDate.get(
                Calendar.DATE
            )
        )

        startOrderDateTv.singleClick {
            pvCustomTime = getDatePicker(mContext, selectedDate, startDate, endDate, { date, view ->
                startOrderDateTv.text = formatString(date, DATE_FORMAT)

                val s = dateTime2Date(startOrderDateTv.getViewText() + " 00:00:00")?.time ?: 0
                mStart = s / 1000

                if (mStart != null && mEnd != null) {
                    mLoadMoreDelegate?.refresh()
                }
                //firstMenuTv.setText(formatDateTime(date))
            }, {
                pvCustomTime?.returnData()
                pvCustomTime?.dismiss()
            }, {
                pvCustomTime?.dismiss()
            })
            pvCustomTime?.show()
        }
        endOrderDateTv.singleClick {
            pvCustomTime2 =
                getDatePicker(mContext, selectedDate, startDate, endDate, { date, view ->
                    endOrderDateTv.text = formatString(date, DATE_FORMAT)
                    val e = dateTime2Date(endOrderDateTv.getViewText() + " 23:59:59")?.time ?: 0
                    mEnd = e / 1000

                    if (mStart != null && mEnd != null) {
                        mLoadMoreDelegate?.refresh()
                    }
                }, {
                    pvCustomTime2?.returnData()
                    pvCustomTime2?.dismiss()
                }, {
                    pvCustomTime2?.dismiss()
                })
            pvCustomTime2?.show()
        }
        orderResetTv.singleClick {
            if (orderType == "COMPLETE" || orderType == "ALL") {
                mStart = null
                mEnd = null
                mCStatus = null
                orderStatusTv.text = ""
                startOrderDateTv.text = ""
                endOrderDateTv.text = ""
                rbContinue?.isChecked = false
                rbComplete?.isChecked = false
                rbCancel?.isChecked = false
                mLoadMoreDelegate?.refresh()
            }
        }
    }

    override fun initAdapter(): OrderListAdapter {
        return OrderListAdapter().apply {
            setOnItemChildClickListener { adapter, view, position ->
                val orderBean = adapter.data[position] as OrderList
                when (view.id) {
                    R.id.tvPhoneUser -> {
                        OtherUtils.goToDialApp(activity, orderBean?.shipMobile)
                    }
                    R.id.tvCancelOrder -> {
                        val intent = Intent(activity, OrderCancelActivity::class.java)
                        intent.putExtra("orderId", orderBean.sn)
                        startActivityForResult(intent, REQUEST_CODE)
                    }
                    R.id.tvUpdatePrice -> {//修改价格
                        val intent = Intent(activity, UpdatePriceActivity::class.java)
                        intent.putExtra("orderAmount", orderBean.orderAmount)
                        intent.putExtra("shippingAmount", orderBean.shippingAmount)
                        intent.putExtra("orderId", orderBean.sn)
                        startActivityForResult(intent, REQUEST_CODE)
                    }
                    R.id.tvShipment -> {
                        DialogUtils.showDialog(requireActivity(), "配送提示", "确认开始配送该订单？",
                            "取消", "确定配送", { }, {
                                showDialogLoading()
                                mPresenter?.shipOrder(orderBean.sn!!)
                            })
                    }
                    R.id.tvPrepare -> {
                        DialogUtils.showDialog(requireActivity(), "备货提示", "确认该订单已经备货？",
                            "取消", "备货完成", { }, {
                                showDialogLoading()
                                mPresenter?.prepareOrder(orderBean.sn!!)
                            })
                    }
                    R.id.tvSign -> {
                        DialogUtils.showDialog(requireActivity(), "送达提示", "确认已经送达该订单？",
                            "取消", "确定送达", { }, {
                                showDialogLoading()
                                mPresenter?.signOrder(orderBean.sn!!)
                            })
                    }
                    R.id.tvVerify -> { // 核销
                        val intent = Intent(activity, OrderDetailActivity::class.java)
                        intent.putExtra("orderId", orderBean.sn)
                        startActivity(intent)
                    }
                    R.id.tvLookLogistics -> {//查看物流
                        val intent = Intent(activity, LogisticsInfoActivity::class.java)
                        intent.putExtra("orderId", orderBean.sn)
                        intent.putExtra("shipNo", orderBean.shipNo)
                        intent.putExtra("logiId", orderBean.logiId.toString())
                        intent.putExtra("logiName", orderBean.logiName)
                        startActivity(intent)
                    }
                    R.id.tvRefuseService,
                    R.id.tvAcceptService,
                    R.id.tvAfterSale -> {//售后/退款
                        val intent = Intent(activity, AfterSaleActivity::class.java)
                        intent.putExtra("orderId", orderBean.sn)
                        startActivityForResult(intent, REQUEST_CODE)
                    }
                    R.id.tvDelete -> {
                        DialogUtils.showDialog(requireActivity(), "删除提示", "删除后不可恢复，确定要删除该订单吗？",
                            "取消", "确定删除", { }, {
                                showDialogLoading()
                                mPresenter?.deleteOrder(orderBean.sn!!)
                            })
                    }
                    R.id.tvAccept -> {
                        DialogUtils.showDialog(requireActivity(), "接单提示", "确认是否接单?",
                            "取消", "确定接单", { }, {
                                showDialogLoading()
                                mPresenter?.takeOrder(orderBean)
                            })
                    }
                    R.id.tvRefuse -> {
                        DialogUtils.showDialog(requireActivity(), "拒绝接单提示", "确认是否拒绝接单?",
                            "取消", "确定拒绝", { }, {
                                showDialogLoading()
                                mPresenter?.refuseOrder(orderBean.sn!!)
                            })
                    }
                    R.id.tvPrint -> {
                        printer(orderBean)
                    }
                    R.id.tvMapNav -> {
                        MapNav.chooseMapDialog(requireContext(), orderBean.getSimpleAddress())
                    }
                    R.id.tvOrderHeXiao -> {
                        ActivityUtils.startActivity(ScanOrderActivity::class.java)
                    }
                }

            }
            setOnItemClickListener { adapter, view, position ->
                val orderBean = adapter.data[position] as OrderList
                OrderShowActivity.open(requireActivity(), orderBean, 11)
            }
            emptyView = EmptyView(mContext).apply {
                setBackgroundResource(R.color.color_ffffff)
            }
        }
    }

    override fun autoRefresh(): Boolean {
        if (isVisible) {
            return true
        }
        return false
    }

    override fun createPresenter() = OrderListPresenterImpl(this)

    override fun onTakeSuccess(trade: OrderList) {
        showToast("接单成功")
        mLoadMoreDelegate?.refresh()
        if (UserManager.isAutoPrint()) {
            printer(trade)
        }
        //EventBus.getDefault().post(OrderNumberEvent())
    }

    private fun printer(trade: OrderList) {
        PrinterModule.printer(requireActivity(), trade)
    }

    override fun onRefuseSuccess() {
        showToast("操作成功")
        mLoadMoreDelegate?.refresh()
        //EventBus.getDefault().post(OrderNumberEvent())
    }

    override fun onDeleteOrderSuccess() {
        showToast("删除成功")
        mLoadMoreDelegate?.refresh()
        //EventBus.getDefault().post(OrderNumberEvent())
    }

    override fun onPreparedOrder() {
        mLoadMoreDelegate?.refresh()
    }

    override fun onPrepareOrderFail() {
        mLoadMoreDelegate?.refresh()
    }

    override fun onShipped() {
        mLoadMoreDelegate?.refresh()
        //EventBus.getDefault().post(OrderNumberEvent())
    }

    override fun onSigned() {
        mLoadMoreDelegate?.refresh()
        //EventBus.getDefault().post(OrderNumberEvent())
    }

    override fun onRefusedService() {
        mLoadMoreDelegate?.refresh()
        //EventBus.getDefault().post(OrderNumberEvent())
    }

    override fun onAcceptService() {
        mLoadMoreDelegate?.refresh()
        //EventBus.getDefault().post(OrderNumberEvent())
    }

    override fun verifySuccess() {
        mLoadMoreDelegate?.refresh()
        //EventBus.getDefault().post(OrderNumberEvent())
    }

    override fun verifyFailed() {
        showToast("核销失败")
    }

    override fun executePageRequest(page: IPage) {
        mPresenter?.loadListData(page, mCStatus ?: orderType!!, mStart, mEnd, mAdapter.data)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            mLoadMoreDelegate?.refresh()
            EventBus.getDefault().post(OrderNumberEvent())
        }
    }

    // 失效订单
    @Subscribe(threadMode = ThreadMode.MAIN)
    fun changeTabPosition(event: TabChangeEvent) {
        LogUtils.d("changeTabPosition:$event")
        if ("ALL" == orderType && event.type == 4 && "CANCELLED" == event.status) {
            mCStatus = event.status
            rbCancel?.isChecked = true
        }
        if (event.startTime != null && event.endTime != null) {

            startTime = event.startTime
            endTime = event.endTime
            if (startTime != null && endTime != null) {
                mStart = startTime
                mEnd = endTime
                lifecycleScope.launch(Dispatchers.Main) {
                    delay(500)
                    mLoadMoreDelegate?.refresh()
                }
            }
            startOrderDateTv.text = formatString(Date(event.startTime!! * 1000), DATE_FORMAT)
            endOrderDateTv.text = formatString(Date(event.endTime!! * 1000), DATE_FORMAT)
        }
        if (event.type == 1 && event.status == "ACCEPT") {
            //备货中
            mCStatus = "ACCEPT"
            rbTaking.isChecked = true
            lifecycleScope.launch(Dispatchers.Main) {
                delay(500)
                mLoadMoreDelegate?.refresh()
            }
        }
        if (event.type == 1 && event.status == "SHIPPED") {
            //配送中
            rbShipping.isChecked = true
            mCStatus = "SHIPPED"
            lifecycleScope.launch(Dispatchers.Main) {
                delay(500)
                mLoadMoreDelegate?.refresh()
            }
        }
    }

    override fun onResume() {
        super.onResume()
        mLoadMoreDelegate?.refresh()
    }

}