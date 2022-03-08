package com.lingmiao.shop.business.order

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.view.View
import com.james.common.base.BaseActivity
import com.james.common.utils.exts.gone
import com.james.common.utils.exts.singleClick
import com.james.common.utils.exts.visiable
import com.lingmiao.shop.R
import com.lingmiao.shop.base.IConstant
import com.lingmiao.shop.business.order.bean.OrderDetail
import com.lingmiao.shop.business.order.bean.OrderList
import com.lingmiao.shop.business.order.presenter.OrderDetailPresenter
import com.lingmiao.shop.business.order.presenter.impl.OrderDetailPresenterImpl
import com.lingmiao.shop.util.OtherUtils
import com.lingmiao.shop.util.stampToDate
import kotlinx.android.synthetic.main.order_activity_order_show.*
import kotlinx.android.synthetic.main.order_activity_order_show.couponDetail
import kotlinx.android.synthetic.main.order_activity_order_show.couponView
import kotlinx.android.synthetic.main.order_activity_order_show.ticketPrice
import kotlinx.android.synthetic.main.order_activity_self_order_detail.*

/**
Create Date : 2021/4/119:42 AM
Author      : Fox
Desc        :
 **/
class OrderShowActivity : BaseActivity<OrderDetailPresenter>(), OrderDetailPresenter.View {

    private var mItem: OrderList? = null

    companion object {
        fun open(
            context: Context,
            item: OrderList?,
            resultValue: Int
        ) {
            if (context is Activity) {
                val intent = Intent(context, OrderShowActivity::class.java)
                intent.putExtra("item", item)
                context.startActivityForResult(intent, resultValue)
            }
        }

        fun open(
            context: Context,
            item: OrderList?
        ) {
            if (context is Activity) {
                val intent = Intent(context, OrderShowActivity::class.java)
                intent.putExtra("item", item)
                context.startActivity(intent)
            }
        }

    }

    override fun initBundles() {
        mItem = intent?.getSerializableExtra("item") as OrderList
    }

    override fun createPresenter() = OrderDetailPresenterImpl(this)

    override fun getLayoutId() = R.layout.order_activity_order_show


    override fun useLightMode() = false


    override fun initView() {
        mToolBarDelegate.setMidTitle("订单详情")
        mItem?.sn?.let { mPresenter.requestOrderDetailData(it) }
        refreshView()
    }

    private fun refreshView() {
        goodsItemC.addItems(mItem?.skuList)

        //电子券
        if (mItem?.ticketList.isNullOrEmpty()) {
            ticketPrice.text = "未使用电子券"
        } else {
            val temp = StringBuilder()
            mItem?.ticketList?.let {
                for ((index, element) in it.withIndex()) {
                    if (index > 0) temp.append("，")
                    temp.append(element.title)
                    temp.append(" ${element.countNum}张")
                }
            }
            ticketPrice.text = temp
        }


        tvOrderSn.text = String.format("订单编号：%s", mItem?.sn)
        tvOrderStatus.text = mItem?.orderStatusText
        tvOrderTime.text = String.format("下单时间：%s", stampToDate(mItem?.createTime))
        tvOrderSubStatus.text = mItem?.cancelReason
        orderShipFeeTv.text = String.format("￥%s", mItem?.shippingAmount)
        orderDiscountTv.text = String.format("-￥%s", mItem?.discountPrice)
        orderPayMoneyTv.text = String.format("￥%s", mItem?.payMoney)
//        orderPayTypeTv
        orderTimeTv.text = stampToDate(mItem?.createTime)

        orderRemarkTv.text = mItem?.remark
        orderDeliveryTimeTv.text = stampToDate(mItem?.shipTime)

        orderAddressTv.text = mItem?.getFullAddress()

        tvTableAware.text = mItem?.getTableAwareHint()
        tableAwareLayout.visibility =
            if (tvTableAware.text?.isNotEmpty() == true) View.VISIBLE else View.GONE

        tvPackagePrice.text = String.format("￥%s", mItem?.packagePrice)
        packagePriceLayout.visibility =
            if (mItem?.packagePrice?.compareTo(0.0) ?: 0 > 0) View.VISIBLE else View.GONE
        packagePriceLine.visibility =
            if (mItem?.packagePrice?.compareTo(0.0) ?: 0 > 0) View.VISIBLE else View.GONE

        orderOwnerTv.text = String.format("%s %s", mItem?.shipName, mItem?.shipMobile)
        orderOwnerTv.singleClick {
            OtherUtils.goToDialApp(context!!, mItem?.shipMobile)
        }

        var shippingType = "骑手配送"
        var shippingMobile = mItem?.riderMobile
        if (mItem?.shippingType == IConstant.SHIP_TYPE_LOCAL) {
            shippingType = "商家配送"
            shippingMobile = mItem?.sellerMobile
        }
        //orderShipTypeTv.text = shippingType;

        orderShipHintTv.text = shippingType
        orderShipRiderTv.text = shippingMobile
        orderShipRiderTv.singleClick {
            OtherUtils.goToDialApp(context!!, shippingMobile)
        }

        tvReplenishRemark.text = mItem?.replenishRemark
        ("￥" + mItem?.replenishPrice).also { tvReplenishPrice.text = it }
        if (mItem?.replenishRemark?.isNotEmpty() == true) {
            replenishLayout.visiable()
            replenishLine.visiable()
        } else {
            replenishLayout.gone()
            replenishLine.gone()
        }
    }

    override fun onOrderDetailSuccess(bean: OrderDetail) {
        couponView.visiable()
        couponDetail.visiable()
        "-￥${bean.couponPrice}".also { couponUse.text = it }
    }

    override fun onOrderDetailError(code: Int) {

    }

    override fun onDeleteOrderSuccess() {

    }

    override fun verifySuccess() {

    }

    override fun verifyFailed() {

    }
}