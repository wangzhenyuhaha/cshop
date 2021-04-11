package com.lingmiao.shop.business.order.adapter

import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.lingmiao.shop.MyApp
import com.lingmiao.shop.R
import com.lingmiao.shop.business.order.bean.OrderList
import com.lingmiao.shop.business.order.view.GoodsItemRvLayout
import com.lingmiao.shop.util.GlideUtils
import com.lingmiao.shop.util.OtherUtils
import com.lingmiao.shop.util.stampToDate
import java.text.SimpleDateFormat
import java.util.*

class OrderListAdapter :
    BaseQuickAdapter<OrderList, BaseViewHolder>(R.layout.order_adapter_order_list) {
    override fun convert(helper: BaseViewHolder, item: OrderList) {
        helper.setText(R.id.tvOrderSn, "订单编号："+item.sn)
            .setText(R.id.tvOrderStatus, item.orderStatusText)

        helper.setText(R.id.tvOrderTime, "下单时间："+stampToDate(item.createTime))

        val ivProduct2 = helper.getView<ImageView>(R.id.ivProduct2)
        val ivOrderNumberCopy = helper.getView<ImageView>(R.id.ivOrderNumberCopy)
        ivOrderNumberCopy.setOnClickListener {
            OtherUtils.copyToClipData(item.sn)
        }
        val tvProductAttribute = helper.getView<TextView>(R.id.tvProductAttribute)
        val tvProductRefund = helper.getView<TextView>(R.id.tvProductRefund)
        val product = item.skuList[0]
        if (item.skuList.size > 1) {
            helper.setText(
                R.id.tvProductName,
                MyApp.getInstance().getString(R.string.order_product_count, item.totalNum)
            )
            ivProduct2.visibility = View.VISIBLE
            tvProductRefund.visibility = View.GONE
            GlideUtils.setImageUrl(ivProduct2, item.skuList[1].goodsImage)
            tvProductAttribute.text = ""
            helper.setText(R.id.tvProductPrice, "")
            helper.setText(R.id.tvProductCount, "")
        } else {
            ivProduct2.visibility = View.GONE
            when (product.serviceStatus) {
                "APPLY" -> {
                    tvProductRefund.visibility = View.VISIBLE
                    tvProductRefund.text = "退款中"
                }
                "PASS" -> {
                    tvProductRefund.visibility = View.VISIBLE
                    tvProductRefund.text = "退款通过"
                }
                "REFUSE" -> {
                    tvProductRefund.visibility = View.VISIBLE
                    tvProductRefund.text = "退款拒绝"
                }
            }
            helper.setText(R.id.tvProductName, product.name)
//            helper.setText(R.id.tvProductAttribute,product.name)
            helper.setText(R.id.tvProductPrice, "￥" + product.purchasePrice)
            helper.setText(R.id.tvProductCount, "×" + product.num)
            tvProductAttribute.text = ""
            if (product.specList != null && product.specList!!.isNotEmpty()) {
                var attributeString = ""
                product.specList!!.forEach { bean ->
                    attributeString = attributeString + bean.specName + ":" + bean.specValue + ","
                }
                attributeString = attributeString.substring(0, attributeString.length - 1)
                tvProductAttribute.text = attributeString
            }
        }

        helper.getView<GoodsItemRvLayout>(R.id.goodsItemC).addItems(item.skuList);
        helper.setText(
            R.id.tvTotalMoney,
            MyApp.getInstance()
                .getString(R.string.order_money_new, item.orderAmount.toString())
        )

//        GlideUtils.setCornerImageUrl(helper.getView(R.id.ivBuyerHead),item.)
        GlideUtils.setImageUrl(helper.getView(R.id.ivProduct1), product.goodsImage)

        val tvCancelOrder = helper.getView<TextView>(R.id.tvCancelOrder)
        val tvUpdatePrice = helper.getView<TextView>(R.id.tvUpdatePrice)
        val tvQuickPay = helper.getView<TextView>(R.id.tvQuickPay)
        val tvShipment = helper.getView<TextView>(R.id.tvShipment)
        val tvVerify = helper.getView<TextView>(R.id.tvVerify);
        val tvLookLogistics = helper.getView<TextView>(R.id.tvLookLogistics)
        val tvAfterSale = helper.getView<TextView>(R.id.tvAfterSale)
        val tvDelete = helper.getView<TextView>(R.id.tvDelete)

        tvCancelOrder.visibility = View.GONE
        tvUpdatePrice.visibility = View.GONE
        tvQuickPay.visibility = View.GONE
        tvShipment.visibility = View.GONE
        tvVerify.visibility = View.GONE
        tvLookLogistics.visibility = View.GONE
        tvAfterSale.visibility = View.GONE
        tvDelete.visibility = View.GONE

        helper.addOnClickListener(R.id.tvCancelOrder)
        helper.addOnClickListener(R.id.tvUpdatePrice)
        helper.addOnClickListener(R.id.tvQuickPay)
        helper.addOnClickListener(R.id.tvShipment)
        helper.addOnClickListener(R.id.tvVerify)
        helper.addOnClickListener(R.id.tvLookLogistics)
        helper.addOnClickListener(R.id.tvAfterSale)
        helper.addOnClickListener(R.id.tvDelete)

//        订单类型是否如下:
//        全部ALL
//        待付款WAIT_PAY
//        待发货WAIT_SHIP
//        已发货WAIT_ROG
//        售后/退款 WAIT_REFUND
//                已取消CANCELLED
//    ALL, WAIT_PAY, WAIT_SHIP, WAIT_ROG, CANCELLED, COMPLETE, WAIT_COMMENT, REFUND, WAIT_REFUN

        helper.setTextColor(
            R.id.tvOrderStatus,
            MyApp.getInstance().resources.getColor(R.color.color_3870EA)
        )
        val orderOperateAllowable = item.orderOperateAllowable
        var showBottomArea = false
        if (orderOperateAllowable != null) {
            if (orderOperateAllowable.allowAuditAfterSell) tvAfterSale.visibility = View.VISIBLE
            if (orderOperateAllowable.allowCancel || orderOperateAllowable.allowServiceCancel) tvCancelOrder.visibility = View.VISIBLE
            if (orderOperateAllowable.allowDelete) tvDelete.visibility = View.VISIBLE
            if (orderOperateAllowable.allowCheckExpress) tvLookLogistics.visibility = View.VISIBLE
            if (orderOperateAllowable.allowEditPrice) tvUpdatePrice.visibility = View.VISIBLE
            if (orderOperateAllowable.allowShip) {
                if(item?.isVirtualOrderTag()) {
                    tvVerify.visibility = View.VISIBLE
                } else {
                    tvShipment.visibility = View.VISIBLE
                }
            }
            if (orderOperateAllowable.allowAuditAfterSell||orderOperateAllowable.allowCancel||orderOperateAllowable.allowServiceCancel
                ||orderOperateAllowable.allowDelete||orderOperateAllowable.allowCheckExpress
                ||orderOperateAllowable.allowEditPrice||orderOperateAllowable.allowShip){
                showBottomArea = true
            }
        }

        val viOrderDivide = helper.getView<View>(R.id.viOrderDivide)
        val llOrderBottom = helper.getView<LinearLayout>(R.id.llOrderBottom)
        if(showBottomArea){
            llOrderBottom.visibility = View.VISIBLE
        }else{
            llOrderBottom.visibility = View.GONE
        }
        if(helper.layoutPosition==data.size-1){
            viOrderDivide.visibility = View.VISIBLE
        }else{
            viOrderDivide.visibility = View.GONE
        }
    }

}