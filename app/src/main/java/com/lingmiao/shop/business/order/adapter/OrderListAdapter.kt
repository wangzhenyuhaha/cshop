package com.lingmiao.shop.business.order.adapter

import android.graphics.Color
import android.util.Log
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

        //电子券的使用情况
        //判断是否使用了电子券
        if (item.ticketList.isNullOrEmpty()) {
            helper.setGone(R.id.dainziquanUser, false)
        } else {
            helper.setGone(R.id.dainziquanUser, true)
            val temp = StringBuilder()
            item.ticketList?.let {
                for ((index, element) in it.withIndex()) {
                    if (index > 0) temp.append("，")
                    temp.append(element.title)
                }
            }
            helper.setText(R.id.dainziquanUser, "使用的电子券：${temp}")
        }
//        helper.setGone(R.id.dainziquanUser,true)
//        helper.setText(R.id.dainziquanUser,item.ticketList?.size.toString())

        //订单编号
        helper.setText(R.id.tvOrderSn, "订单编号：" + item.sn)
        //复制订单编号
        val ivOrderNumberCopy = helper.getView<ImageView>(R.id.ivOrderNumberCopy)
        ivOrderNumberCopy.setOnClickListener {
            OtherUtils.copyToClipData(item.sn)
        }
        //配送方式
        val peisongfanshi = helper.getView<TextView>(R.id.takeSelf)
        if (item.shippingType == IConstant.SHIP_TYPE_SELF) {
            peisongfanshi.text = "自提"
            peisongfanshi.setBackgroundColor(Color.parseColor("#FF8647"))
        } else {
            peisongfanshi.setBackgroundColor(Color.parseColor("#FF4747"))
            peisongfanshi.text = "配送"
        }

        //订单序号
        helper.setText(R.id.tvOrderXuHao, "# ${item.orderSequence} ")

        //下单时间
        helper.setText(R.id.tvOrderTime, "下单时间：" + stampToDate(item.createTime))

        //自提时间
        helper.setGone(R.id.zitishijian, item.pickTime != null)
        //自提时间
        helper.setText(R.id.zitishijian, "自提时间：" + stampToDate(item.pickTime))

        //订单状态
        helper.setText(R.id.tvOrderStatus, item.orderStatusText)
        //设置订单状态的颜色
        helper.setTextColor(
            R.id.tvOrderStatus,
            if (item.orderStatusText == "售后中") MyApp.getInstance().resources.getColor(R.color.color_FF0202) else MyApp.getInstance().resources.getColor(
                R.color.color_black
            )
        )

        //加购商品
        helper.setText(R.id.tvReplenishRemark, item.replenishRemark)
        //加购商品价格
        helper.setText(R.id.tvReplenishPrice, "￥" + item.replenishPrice)
        //加购项是否显示
        helper.setGone(R.id.replenishLayout, item.replenishRemark?.isNotEmpty() == true)

        //客户所需餐具
        helper.setText(R.id.tvTableAware, item.getTableAwareHint())
        //餐具是否显示
        helper.setGone(R.id.tableAwareLayout, item.getTableAwareHint().isNotEmpty())

        //打包费是否显示
        helper.setGone(R.id.packagePriceLayout, item.packagePrice?.compareTo(0.0) ?: 0 > 0)
        //打包费分割线是否显示
        helper.setGone(R.id.packagePriceLine, item.packagePrice?.compareTo(0.0) ?: 0 > 0)
        //打包费
        helper.setText(R.id.tvPackagePrice, "￥" + item.packagePrice)

        //--------------------------------------------------------------
        //商品详情
        //商品图片2
        val ivProduct2 = helper.getView<ImageView>(R.id.ivProduct2)
        //商品规格
        val tvProductAttribute = helper.getView<TextView>(R.id.tvProductAttribute)
        //商品退款状态
        val tvProductRefund = helper.getView<TextView>(R.id.tvProductRefund)
        //只有一个商品时(没用)
        if (item.skuList.size == 1) {
            //商品有一张图片
            val product = item.skuList[0]
            ivProduct2.visibility = View.GONE
            //改变商品退款状态(这东西默认不显示)
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
            //商品名
            helper.setText(R.id.tvProductName, product.name)
            //商品价格
            helper.setText(R.id.tvProductPrice, "￥" + product.purchasePrice)
            //商品数量
            helper.setText(R.id.tvProductCount, "×" + product.num)
            //清空商品规格
            tvProductAttribute.text = ""
            if (product.specList != null && product.specList!!.isNotEmpty()) {
                var attributeString = ""
                product.specList!!.forEach { bean ->
                    attributeString = attributeString + bean.specName + ":" + bean.specValue + ","
                }
                attributeString = attributeString.substring(0, attributeString.length - 1)
                //显示商品规格
                tvProductAttribute.text = attributeString
            }
            //显示商品图片
            GlideUtils.setImageUrl(helper.getView(R.id.ivProduct1), product.goodsImage)
        } else if (item.skuList.size > 1) {
            //商品名称
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
        helper.getView<GoodsItemRvLayout>(R.id.goodsItemC).addItems(item.skuList)
        //--------------------------------------------------------------

        //送货地址
        helper.setText(R.id.tvFullAddress, item.getSimpleAddress())

        //导航
        helper.addOnClickListener(R.id.tvMapNav)

        //商品数量及总付款金额
        helper.setText(
            R.id.tvTotalMoney,
            MyApp.getInstance()
                .getString(
                    R.string.order_money,
                    item.skuList.size,
                    item.orderAmount.toString()
                )
        )

        //底部按钮（Item复用，需要先按照Layout中都设为gone()）
        //取消订单
        val tvCancelOrder = helper.getView<TextView>(R.id.tvCancelOrder)
        helper.addOnClickListener(R.id.tvCancelOrder)
        tvCancelOrder.gone()
        //接单
        val tvAccept = helper.getView<TextView>(R.id.tvAccept)
        helper.addOnClickListener(R.id.tvAccept)
        tvAccept.gone()
        //拒绝接单
        val tvRefuse = helper.getView<TextView>(R.id.tvRefuse)
        helper.addOnClickListener(R.id.tvRefuse)
        tvRefuse.gone()
        //打印
        val tvPrint = helper.getView<TextView>(R.id.tvPrint)
        helper.addOnClickListener(R.id.tvPrint)
        tvPrint.gone()
        //备货完成
        val tvPrepare = helper.getView<TextView>(R.id.tvPrepare)
        helper.addOnClickListener(R.id.tvPrepare)
        tvPrepare.gone()
        //开始配送
        val tvShipment = helper.getView<TextView>(R.id.tvShipment)
        helper.addOnClickListener(R.id.tvShipment)
        tvShipment.gone()
        //联系用户
        val tvPhoneUser = helper.getView<TextView>(R.id.tvPhoneUser)
        helper.addOnClickListener(R.id.tvPhoneUser)
        tvPhoneUser.gone()
        //确认送达
        val tvSign = helper.getView<TextView>(R.id.tvSign)
        helper.addOnClickListener(R.id.tvSign)
        tvSign.gone()
        //核销
        val tvVerify = helper.getView<TextView>(R.id.tvVerify)
        helper.addOnClickListener(R.id.tvVerify)
        tvVerify.gone()
        //修改价格
        val tvUpdatePrice = helper.getView<TextView>(R.id.tvUpdatePrice)
        helper.addOnClickListener(R.id.tvUpdatePrice)
        tvUpdatePrice.gone()
        //催付
        val tvQuickPay = helper.getView<TextView>(R.id.tvQuickPay)
        helper.addOnClickListener(R.id.tvQuickPay)
        tvQuickPay.gone()
        //查看物流
        val tvLookLogistics = helper.getView<TextView>(R.id.tvLookLogistics)
        helper.addOnClickListener(R.id.tvLookLogistics)
        tvLookLogistics.gone()
        //售后处理
        val tvAfterSale = helper.getView<TextView>(R.id.tvAfterSale)
        helper.addOnClickListener(R.id.tvAfterSale)
        tvAfterSale.gone()
        //删除
        val tvDelete = helper.getView<TextView>(R.id.tvDelete)
        helper.addOnClickListener(R.id.tvDelete)
        tvDelete.gone()
        //同意退款
        val tvAcceptService = helper.getView<TextView>(R.id.tvAcceptService)
        helper.addOnClickListener(R.id.tvAcceptService)
        tvAcceptService.gone()
        //拒绝退款
        val tvRefuseService = helper.getView<TextView>(R.id.tvRefuseService)
        helper.addOnClickListener(R.id.tvRefuseService)
        tvRefuseService.gone()
        //订单核销
        val hexiaoOrder = helper.getView<TextView>(R.id.tvOrderHeXiao)
        helper.addOnClickListener(R.id.tvOrderHeXiao)
        hexiaoOrder.gone()

        var showBottomArea = false
        helper.setText(R.id.tvOrderSubStatus, "")

        //NEW("新订单"),
        //INTODB_ERROR("下单失败"),
        //CONFIRM("已确认"),
        //PAID_OFF("已付款"),
        //ACCEPT("已接单"),
        //FORMED("已经成团"),
        //SHIPPED("已发货"),
        //PART_SHIPPED("部分发货"),
        //ROG("已收货"),
        //COMPLETE("已完成"),
        //CANCELLED("已取消"),
        //AFTER_SERVICE("售后中");

        when (item.orderStatus) {
            "PAID_OFF" -> {
                showBottomArea = true
                // 已付款,待接单
                tvAccept.visiable()
                tvRefuse.visiable()
            }
            "ACCEPT" -> {
                showBottomArea = true
                // 已接单,进行中,待送配
                when (item.shippingType) {
                    IConstant.SHIP_TYPE_GLOBAL -> {
                        //骑手配送

                    }
                    IConstant.SHIP_TYPE_SELF -> {
                        //自提
                        if (item.isPrepare == 1) {
                            //备货完成
                            hexiaoOrder.visiable()
                        } else {
                            hexiaoOrder.gone()
                        }
                    }
                    IConstant.SHIP_TYPE_LOCAL -> {
                        //开始配送
                        tvShipment.visiable()
                        hexiaoOrder.gone()
                    }
                }
                //是否备货了
                if (item.isPrepare == 1) {
                    //备货完成
                    tvPrepare.gone()
                } else {
                    tvPrepare.visiable()
                }
                tvPrint.visiable()
            }
            "SHIPPED" -> {
                showBottomArea = true
                when (item.shippingType) {
                    IConstant.SHIP_TYPE_LOCAL -> {
                        tvSign.visibility = View.VISIBLE
                        hexiaoOrder.gone()
                    }
                    IConstant.SHIP_TYPE_GLOBAL -> {
                        // 骑手配送
                        tvPhoneUser.visibility = View.VISIBLE
                        hexiaoOrder.gone()
                    }
                    IConstant.SHIP_TYPE_SELF -> {
                        hexiaoOrder.visiable()
                    }
                }
            }
            "ROG" -> {
                // 已发货,已送达
            }
            "CANCELLED" -> {
                // 已取消
                showBottomArea = false
                helper.setText(R.id.tvOrderSubStatus, item.cancelReason)
            }
            "COMPLETE" -> {
                // 已完成
                showBottomArea = false
            }
            "AFTER_SERVICE" -> {
                // 售后中
                helper.setText(R.id.tvOrderSubStatus, item.cancelReason)
            }
        }

        when (item.serviceStatus) {
            "NOT_APPLY" -> {
                // 未申请
            }
            "APPLY" -> {
                showBottomArea = true

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
        if (showBottomArea) {
            llOrderBottom.visibility = View.VISIBLE
        } else {
            llOrderBottom.visibility = View.GONE
        }
        if (helper.layoutPosition == data.size - 1) {
            viOrderDivide.visibility = View.VISIBLE
        } else {
            viOrderDivide.visibility = View.GONE
        }

        //根据配送方式显示地址等信息
        //RecyclerView是Item复用的
        when (item.shippingType) {
            IConstant.SHIP_TYPE_LOCAL -> {
                //商家配送
                helper.setGone(R.id.zitidanzhuangtai, false)
                helper.setGone(R.id.tvMapNav, true)
                helper.setGone(R.id.tvFullAddressTitle, true)
                helper.setGone(R.id.tvFullAddress, true)
            }
            IConstant.SHIP_TYPE_GLOBAL -> {
                helper.setGone(R.id.zitidanzhuangtai, true)
                val temp = when (item.shipStatus) {
                    "SHOP_INVENTORY" -> {
                        "商家备货中"
                    }
                    "WAIT_RIDER_RECEIVING_ORDER" -> {
                        "待骑手接单"
                    }
                    "WAIT_RIDER_DELIVER" -> {
                        "待骑手取货"
                    }
                    "SHIP_NO" -> {
                        "未发货"
                    }
                    "SHIP_YES" -> {
                        "配送中"
                    }
                    "SHIP_ROG" -> {
                        "已送达"
                    }
                    "SHIP_CANCEL" -> {
                        "取消发货"
                    }
                    else -> {
                        ""
                    }
                }
                helper.setText(R.id.zitidanzhuangtai, "配送状态：$temp")
                helper.setGone(R.id.tvMapNav, false)
                helper.setGone(R.id.tvFullAddressTitle, true)
                helper.setGone(R.id.tvFullAddress, true)
            }
            IConstant.SHIP_TYPE_SELF -> {
                //自提
                helper.setGone(R.id.zitidanzhuangtai, false)
                helper.setGone(R.id.tvMapNav, false)
                //不显示地址
                helper.setGone(R.id.tvFullAddressTitle, false)
                helper.setGone(R.id.tvFullAddress, false)
            }
        }
        if (item.shipName?.isNotBlank() == true) {
            helper.setText(R.id.orderName, item.shipName)
        } else {
            helper.setText(R.id.orderName, item.member_nick_name)
        }

        helper.setText(R.id.orderPhotoNumber, item.shipMobile)

    }

}