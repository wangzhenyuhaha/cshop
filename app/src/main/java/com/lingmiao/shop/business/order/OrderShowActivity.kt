package com.lingmiao.shop.business.order

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.view.View
import com.blankj.utilcode.util.TimeUtils
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

/**
Create Date : 2021/4/119:42 AM
Auther      : Fox
Desc        :
 **/
class OrderShowActivity : BaseActivity<OrderDetailPresenter>(), OrderDetailPresenter.View {

    private var mItem : OrderList? = null

    companion object {
        fun open(
            context: Context,
            item : OrderList?,
            resultValue: Int
        ) {
            if (context is Activity) {
                val intent = Intent(context, OrderShowActivity::class.java)
                intent.putExtra("item", item)
                context.startActivityForResult(intent, resultValue)
            }
        }

    }

    override fun initBundles() {
        mItem = intent?.getSerializableExtra("item") as OrderList;
    }

    override fun createPresenter(): OrderDetailPresenter {
        return OrderDetailPresenterImpl(this);
    }

    override fun getLayoutId(): Int {
        return R.layout.order_activity_order_show;
    }

    override fun useLightMode(): Boolean {
        return false;
    }

    override fun initView() {
        mToolBarDelegate.setMidTitle("订单详情")
        refreshView();
    }

    fun refreshView() {
        goodsItemC.addItems(mItem?.skuList);

        tvOrderSn.setText(String.format("订单编号：%s", mItem?.sn));
        tvOrderStatus.setText(mItem?.orderStatusText);

        orderShipFeeTv.text = String.format("￥%s", mItem?.shippingAmount)
        orderDiscountTv.text = String.format("￥%s", mItem?.discountPrice);
        orderPayMoneyTv.text = String.format("￥%s", mItem?.payMoney);
//        orderPayTypeTv
        orderTimeTv.text = stampToDate(mItem?.createTime);

        orderRemarkTv.text = mItem?.remark;
        orderDeliveryTimeTv.text = stampToDate(mItem?.shipTime);

        val address = mItem?.shipProvince.orEmpty() + mItem?.shipCity.orEmpty() +
                mItem?.shipCounty.orEmpty() +
                mItem?.shipTown.orEmpty() + mItem?.shipAddr.orEmpty()
        orderAddressTv.text = address

        tvTableAware.text = mItem?.getTableAwareHint();
        tableAwareLayout.visibility = if(tvTableAware.text?.isNotEmpty() == true) View.VISIBLE else View.GONE;

        orderOwnerTv.text = String.format("%s %s", mItem?.shipName, mItem?.shipMobile);
        orderOwnerTv.singleClick {
            OtherUtils.goToDialApp(context!!, mItem?.shipMobile)
        }

        var shippingType = "骑手配送"
        var shippingMobile = mItem?.riderMobile;
        if (mItem?.shippingType == IConstant.SHIP_TYPE_LOCAL) {
            shippingType = "商家配送"
            shippingMobile = mItem?.sellerMobile;
        }
        //orderShipTypeTv.text = shippingType;

        orderShipHintTv.text = shippingType;
        orderShipRiderTv.text = shippingMobile;
        orderShipRiderTv.singleClick {
            OtherUtils.goToDialApp(context!!, shippingMobile)
        }

        tvReplenishRemark.text = mItem?.replenishRemark;
        tvReplenishPrice.text = "￥" + mItem?.replenishPrice
        if(mItem?.replenishRemark?.isNotEmpty() == true) {
            replenishLayout.visiable();
            replenishLine.visiable();
        } else {
            replenishLayout.gone();
            replenishLine.gone();
        }
    }

    override fun onOrderDetailSuccess(bean: OrderDetail) {
//        mItem = bean;
//        refreshView();
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