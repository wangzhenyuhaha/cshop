package com.lingmiao.shop.business.order.fragment

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.View
import com.blankj.utilcode.util.ActivityUtils
import com.lingmiao.shop.R
import com.lingmiao.shop.business.order.*
import com.lingmiao.shop.business.order.adapter.OrderListAdapter
import com.lingmiao.shop.business.order.bean.OrderList
import com.lingmiao.shop.business.order.bean.OrderNumberEvent
import com.lingmiao.shop.business.order.presenter.OrderListPresenter
import com.lingmiao.shop.business.order.presenter.impl.OrderListPresenterImpl
import com.james.common.base.loadmore.BaseLoadMoreFragment
import com.james.common.base.loadmore.core.IPage
import com.james.common.utils.DialogUtils
import org.greenrobot.eventbus.EventBus

private const val STATUS = "param1"

/**
订单页
 */
class SingleOrderListFragment : BaseLoadMoreFragment<OrderList, OrderListPresenter>(),
    OrderListPresenter.StatusView {
    //    ALL, WAIT_PAY, WAIT_SHIP, WAIT_ROG, CANCELLED, COMPLETE, WAIT_COMMENT, REFUND, WAIT_REFUND
    private var orderType: String? = "ALL"


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            orderType = it.getString(STATUS)
        }
    }

    override fun initOthers(rootView: View) {
        super.initOthers(rootView)
        mAdapter.setEmptyView(R.layout.order_empty,mLoadMoreRv)
    }


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

    override fun initAdapter(): OrderListAdapter {
        return OrderListAdapter().apply {
            setOnItemChildClickListener { adapter, view, position ->
                val orderBean = adapter.data[position] as OrderList
                when (view.id) {
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
                    R.id.tvQuickPay -> {//催付 备注:已取消该功能
//                        DialogUtils.showDialog(activity!!, "催付提示", "确定要催促用户付款吗？",
//                            "取消", "确定", View.OnClickListener { }, View.OnClickListener {
//                            })

                    }
                    R.id.tvShipment -> {
                        DialogUtils.showDialog(activity!!, "配送提示", "确认开始配送该订单？",
                            "取消", "确定配送", View.OnClickListener { }, View.OnClickListener {
                                showDialogLoading()
                                mPresenter?.shipOrder(orderBean.sn!!);
                            })
                          //发货
//                        val intent = Intent(activity, OrderSendGoodsActivity::class.java)
//                        intent.putExtra("orderId", orderBean.sn)
//                        intent.putExtra("shippingType", orderBean.shippingType)
//                        startActivityForResult(intent, REQUEST_CODE)
                    }
                    R.id.tvSign -> {
                        DialogUtils.showDialog(activity!!, "送达提示", "确认已经送达该订单？",
                            "取消", "确定送达", View.OnClickListener { }, View.OnClickListener {
                                showDialogLoading()
                                mPresenter?.signOrder(orderBean.sn!!);
                            })
                    }
                    R.id.tvVerify -> { // 核销
//                        orderBean.verificationCode?.apply {
//                            DialogUtils.showDialog(activity!!, "核销提示", "确定要核销该订单吗？",
//                                "取消", "确定核销", View.OnClickListener { }, View.OnClickListener {
//                                    showDialogLoading()
//                                    mPresenter?.verifyOrder(orderBean.verificationCode!!)
//                                })
//                        }
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
                        DialogUtils.showDialog(activity!!, "删除提示", "删除后不可恢复，确定要删除该订单吗？",
                            "取消", "确定删除", View.OnClickListener { }, View.OnClickListener {
                                showDialogLoading()
                                mPresenter?.deleteOrder(orderBean.sn!!)
                            })
                    }
                    R.id.tvAccept -> {
                        DialogUtils.showDialog(activity!!, "接单提示", "确认是否接单?",
                            "取消", "确定接单", View.OnClickListener { }, View.OnClickListener {
                                showDialogLoading()
                                mPresenter?.takeOrder(orderBean.sn!!)
                            })
                    }
                    R.id.tvRefuse -> {
                        DialogUtils.showDialog(activity!!, "拒绝接单提示", "确认是否拒绝接单?",
                            "取消", "确定拒绝", View.OnClickListener { }, View.OnClickListener {
                                showDialogLoading()
                                mPresenter?.refuseOrder(orderBean.sn!!)
                            })
                    }
                }

            }
            setOnItemClickListener { adapter, view, position ->
                val orderBean = adapter.data[position] as OrderList
//                val intent = Intent(activity, OrderDetailActivity::class.java)
//                intent.putExtra("orderId", orderBean.sn)
////                intent.putExtra("order",orderBean)
//                startActivity(intent)
                OrderShowActivity.open(activity!!, orderBean, 11);
            }
        }
    }

    override fun autoRefresh(): Boolean {
        if(isVisible) {
            return true;
        }
        return false;
    }

    override fun createPresenter(): OrderListPresenter {
        return OrderListPresenterImpl(
            this
        )
    }

    override fun onTakeSuccess() {
        showToast("接单成功")
        mLoadMoreDelegate?.refresh()
        EventBus.getDefault().post(OrderNumberEvent())
    }

    override fun onRefuseSuccess() {
        showToast("操作成功")
        mLoadMoreDelegate?.refresh()
        EventBus.getDefault().post(OrderNumberEvent())
    }

    override fun onDeleteOrderSuccess() {
        showToast("删除成功")
        mLoadMoreDelegate?.refresh()
        EventBus.getDefault().post(OrderNumberEvent())
    }

    override fun onShipped() {
        mLoadMoreDelegate?.refresh()
        EventBus.getDefault().post(OrderNumberEvent())
    }

    override fun onSigned() {
        mLoadMoreDelegate?.refresh()
        EventBus.getDefault().post(OrderNumberEvent())
    }

    override fun onRefusedService() {
        mLoadMoreDelegate?.refresh()
        EventBus.getDefault().post(OrderNumberEvent())
    }

    override fun onAcceptService() {
        mLoadMoreDelegate?.refresh()
        EventBus.getDefault().post(OrderNumberEvent())
    }

    override fun verifySuccess() {
        mLoadMoreDelegate?.refresh()
        EventBus.getDefault().post(OrderNumberEvent())
    }

    override fun verifyFailed() {
        showToast("核销失败");
    }

    override fun executePageRequest(page: IPage) {
        mPresenter?.loadListData(page, orderType!!, mAdapter.data)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            mLoadMoreDelegate?.refresh()
            EventBus.getDefault().post(OrderNumberEvent())
        }
    }

}