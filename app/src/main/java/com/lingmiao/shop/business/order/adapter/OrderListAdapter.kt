package com.lingmiao.shop.business.order.adapter

import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.james.common.utils.exts.gone
import com.james.common.utils.exts.visiable
import com.lingmiao.shop.MyApp
import com.lingmiao.shop.R
import com.lingmiao.shop.base.IConstant
import com.lingmiao.shop.business.order.bean.OrderList
import com.lingmiao.shop.business.order.view.GoodsItemRvLayout
import com.lingmiao.shop.util.GlideUtils
import com.lingmiao.shop.util.OtherUtils
import com.lingmiao.shop.util.stampToDate

class OrderListAdapter :
    BaseQuickAdapter<OrderList, BaseViewHolder>(R.layout.order_adapter_order_list) {
    override fun convert(helper: BaseViewHolder, item: OrderList) {
        helper.setText(R.id.tvOrderSn, "订单编号："+item.sn)
            .setText(R.id.tvOrderStatus, item.orderStatusText)

        helper.setText(R.id.tvReplenishRemark, item.replenishRemark);
        helper.setText(R.id.tvReplenishPrice, "￥" + item.replenishPrice);
        helper.setGone(R.id.replenishLayout, item?.replenishRemark?.isNotEmpty() == true);
        helper.setText(R.id.tvOrderTime, "下单时间："+stampToDate(item.createTime))
        // 地址
        helper.setText(R.id.tvFullAddress, item.getSimpleAddress())
        // 餐费
        helper.setText(R.id.tvTableAware, item?.getTableAwareHint());
        helper.setGone(R.id.tableAwareLayout, item?.getTableAwareHint()?.isNotEmpty())
        // 打包费
        helper.setGone(R.id.packagePriceLayout, item?.packagePrice?.compareTo(0.0)?:0>0);
        helper.setGone(R.id.packagePriceLine, item?.packagePrice?.compareTo(0.0)?:0>0);
        helper.setText(R.id.tvPackagePrice, "￥" + item?.packagePrice);

        val ivProduct2 = helper.getView<ImageView>(R.id.ivProduct2)
        val ivOrderNumberCopy = helper.getView<ImageView>(R.id.ivOrderNumberCopy)
        ivOrderNumberCopy.setOnClickListener {
            OtherUtils.copyToClipData(item.sn)
        }
        val tvProductAttribute = helper.getView<TextView>(R.id.tvProductAttribute)
        val tvProductRefund = helper.getView<TextView>(R.id.tvProductRefund)

        if(item.skuList.size == 1) {
            val product = item.skuList[0]
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
                "EXPIRED" -> {
                    tvProductRefund.visibility = View.VISIBLE
                    tvProductRefund.text = "退款已失效"
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
//        GlideUtils.setCornerImageUrl(helper.getView(R.id.ivBuyerHead),item.)
            GlideUtils.setImageUrl(helper.getView(R.id.ivProduct1), product.goodsImage)
        } else if(item.skuList.size > 1) {
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
        }

        helper.getView<GoodsItemRvLayout>(R.id.goodsItemC).addItems(item.skuList);
        helper.setText(
            R.id.tvTotalMoney,
            MyApp.getInstance()
                .getString(R.string.order_money, item.skuList?.size?:0, item.orderAmount.toString())
        )


        val tvAccept = helper.getView<TextView>(R.id.tvAccept)
        val tvRefuse = helper.getView<TextView>(R.id.tvRefuse)
        val tvCancelOrder = helper.getView<TextView>(R.id.tvCancelOrder)
        val tvUpdatePrice = helper.getView<TextView>(R.id.tvUpdatePrice)
        val tvQuickPay = helper.getView<TextView>(R.id.tvQuickPay)
        val tvShipment = helper.getView<TextView>(R.id.tvShipment)
        val tvPrepare = helper.getView<TextView>(R.id.tvPrepare)
        val tvSign = helper.getView<TextView>(R.id.tvSign)
        val tvVerify = helper.getView<TextView>(R.id.tvVerify);
        val tvLookLogistics = helper.getView<TextView>(R.id.tvLookLogistics)
        val tvAfterSale = helper.getView<TextView>(R.id.tvAfterSale)
        val tvDelete = helper.getView<TextView>(R.id.tvDelete)
        val tvRefuseService = helper.getView<TextView>(R.id.tvRefuseService)
        val tvAcceptService = helper.getView<TextView>(R.id.tvAcceptService)
        val tvPhoneUser = helper.getView<TextView>(R.id.tvPhoneUser)

        tvAccept.visibility = View.GONE
        tvRefuse.visibility = View.GONE
        tvRefuseService.visibility = View.GONE
        tvAcceptService.visibility = View.GONE
        tvCancelOrder.visibility = View.GONE
        tvUpdatePrice.visibility = View.GONE
        tvQuickPay.visibility = View.GONE
        tvShipment.visibility = View.GONE
        tvPrepare.visibility = View.GONE
        tvSign.visibility = View.GONE
        tvVerify.visibility = View.GONE
        tvLookLogistics.visibility = View.GONE
        tvAfterSale.visibility = View.GONE
        tvDelete.visibility = View.GONE
        tvPhoneUser.visibility = View.GONE

        helper.addOnClickListener(R.id.tvAccept)
        helper.addOnClickListener(R.id.tvRefuse)
        helper.addOnClickListener(R.id.tvCancelOrder)
        helper.addOnClickListener(R.id.tvUpdatePrice)
        helper.addOnClickListener(R.id.tvQuickPay)
        helper.addOnClickListener(R.id.tvShipment)
        helper.addOnClickListener(R.id.tvPrepare)
        helper.addOnClickListener(R.id.tvVerify)
        helper.addOnClickListener(R.id.tvLookLogistics)
        helper.addOnClickListener(R.id.tvAfterSale)
        helper.addOnClickListener(R.id.tvDelete)
        helper.addOnClickListener(R.id.tvAcceptService)
        helper.addOnClickListener(R.id.tvRefuseService)
        helper.addOnClickListener(R.id.tvSign)
        helper.addOnClickListener(R.id.tvPhoneUser)

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
        var showBottomArea = false
        helper.setText(R.id.tvOrderSubStatus, "");
        when(item.orderStatus) {
           "PAID_OFF" -> {
               showBottomArea = true;
               // 已付款,待接单
               tvAccept.visibility = View.VISIBLE
               tvRefuse.visibility = View.VISIBLE
           }
            "ACCEPT" -> {
                showBottomArea = true;
                // 已接单,进行中,待送配
                if(item?.shippingType == IConstant.SHIP_TYPE_GLOBAL) {
                    // 骑手配送
//                    tvShipment.setText("备货完成");
//                    tvPhoneUser.visibility = View.VISIBLE
                } else {
                    tvShipment.visiable()
                }
                if(item?.isPrepare == 1) {
                    tvPrepare.gone()
                } else {
                    tvPrepare.visiable()
                }

            }
            "SHIPPED" -> {
                showBottomArea = true;
                if(item?.shippingType == IConstant.SHIP_TYPE_LOCAL) {
                    // 已发货,进行中,送配达
                    tvSign.visibility = View.VISIBLE
                } else {
                    // 骑手配送
                    tvPhoneUser.visibility = View.VISIBLE
                }
            }
            "ROG" -> {
                // 已发货,已送达
            }
            "CANCELLED" -> {
                // 已取消
                showBottomArea = false;
//                tvProductRefund.visibility = View.VISIBLE
//                tvProductRefund.text = item.cancelReason;

                helper.setText(R.id.tvOrderSubStatus, item.cancelReason)
            }
            "COMPLETE" -> {
                // 已完成
                showBottomArea = false;
            }
            "AFTER_SERVICE" -> {
                // 售后中
                helper.setText(R.id.tvOrderSubStatus, item.cancelReason)
            }
        }


        when(item.serviceStatus) {
            "NOT_APPLY" -> {
                // 未申请
            }
            "APPLY" -> {
                showBottomArea = true;

                tvAccept.gone()
                tvRefuse.gone()
                tvSign.gone()
                tvShipment.gone()

                //已申请
                tvRefuseService.visiable()
                tvAcceptService.visiable()
            }
            "PASS" -> {
                // 通过
            }
            "REFUSE" -> {
                // 未通过

            }
            "EXPIRED" -> {
                //已失效不允许申请售后
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