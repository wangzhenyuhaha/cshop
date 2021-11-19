package com.lingmiao.shop.business.order

import android.app.Activity
import android.content.Intent
import android.text.TextUtils
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import com.blankj.utilcode.util.TimeUtils
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.lingmiao.shop.R
import com.lingmiao.shop.base.IConstant
import com.lingmiao.shop.business.order.bean.OrderDetail
import com.lingmiao.shop.business.order.bean.OrderNumberEvent
import com.lingmiao.shop.business.order.bean.OrderSku
import com.lingmiao.shop.business.order.fragment.SingleOrderListFragment
import com.lingmiao.shop.business.order.presenter.OrderDetailPresenter
import com.lingmiao.shop.business.order.presenter.impl.OrderDetailPresenterImpl
import com.lingmiao.shop.util.GlideUtils
import com.lingmiao.shop.util.OtherUtils
import com.james.common.base.BaseActivity
import com.james.common.netcore.coroutine.CoroutineSupport
import com.james.common.utils.DialogUtils
import com.james.common.utils.exts.gone
import com.james.common.utils.exts.visiable
import com.luck.picture.lib.tools.DateUtils
import kotlinx.android.synthetic.main.order_activity_order_detail.*
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.greenrobot.eventbus.EventBus
import java.text.SimpleDateFormat
import java.util.*

/**
 *   订单详情
 */
class OrderDetailActivity : BaseActivity<OrderDetailPresenter>(), OrderDetailPresenter.View,
    View.OnClickListener {


    private val mCoroutine: CoroutineSupport by lazy { CoroutineSupport() }
    private var order: OrderDetail? = null
    private lateinit var tradeSn: String

    override fun getLayoutId() = R.layout.order_activity_order_detail

    override fun initView() {

        tradeSn = intent?.getStringExtra("orderId").toString()
        mToolBarDelegate.setMidTitle("订单详情")

        mPresenter.requestOrderDetailData(tradeSn)

        ivOrderNumberCopy.setOnClickListener { copyOrderNumber() }

        initClickListener()
    }

    private fun initClickListener() {
        tvCancelOrder.setOnClickListener(this)
        tvUpdatePrice.setOnClickListener(this)
        tvQuickPay.setOnClickListener(this)
        tvShipment.setOnClickListener(this)
        tvVerify.setOnClickListener(this)
        tvLookLogistics.setOnClickListener(this)
        tvAfterSale.setOnClickListener(this)
        tvDelete.setOnClickListener(this)
        back.setOnClickListener(this)
        hexiaoOrder.setOnClickListener(this)
    }

    private fun copyOrderNumber() {
        OtherUtils.copyToClipData(order?.sn)
    }

    private fun initOrderStatus() {
        //    ALL, WAIT_PAY, WAIT_SHIP, WAIT_ROG, CANCELLED, COMPLETE, WAIT_COMMENT, REFUND, WAIT_REFUN
        when (order?.orderStatusText) {
            "待付款" -> {
                ivOrderStatus.setImageResource(R.mipmap.order_wait_pay)
                var cancelTimeString = ""
                if (order?.cancelLeftTime != null && order?.cancelLeftTime!! > 0) {
                    var cancelLeftTime = order?.cancelLeftTime
                    mCoroutine.launch {
                        while (cancelLeftTime!! > 0) {
                            cancelTimeString =
                                (cancelLeftTime!! / 3600).toString() + "时" + (cancelLeftTime!! / 60 % 60).toString() + "分" + (cancelLeftTime!! % 60).toString() + "秒"
                            initOrderStatusColor("等待支付", "剩余" + cancelTimeString + "后自动取消订单", "red")
                            cancelLeftTime = cancelLeftTime!! - 1
//                            LogUtils.d("cancelLeftTime:$cancelLeftTime")
                            delay(1000)
                        }

                    }


                } else {
                    cancelTimeString = "0时0分0秒"
                    initOrderStatusColor("等待支付", "剩余" + cancelTimeString + "后自动取消订单", "red")
                }

            }
            "待发货" -> {
                ivOrderStatus.setImageResource(R.mipmap.order_wait_send_good)
                initOrderStatusColor("等待发货", "商家正在备货,请耐心等待", "red")
            }
            "已付款" -> {
                ivOrderStatus.setImageResource(R.mipmap.order_send_good)
                initOrderStatusColor("订单已支付", "待使用", "green")
            }
            "已发货" -> {
                ivOrderStatus.setImageResource(R.mipmap.order_send_good)
                var receiveTime = ""
                if (order?.rogLeftTime != null && order?.rogLeftTime!! > 0) {
                    receiveTime =
                        (order?.rogLeftTime!! / 3600 / 24).toString() + "天" + (order?.rogLeftTime!! / 3600 % 24).toString() + "时"
                } else {
                    receiveTime = "0天0小时"
                }
                initOrderStatusColor("商家已发货", "剩余" + receiveTime + "后自动确认收货", "green")
            }
            "退款/售后" -> {
                ivOrderStatus.setImageResource(R.mipmap.order_refund)
                initOrderStatusColor("售后服务中", "正在进行退换货处理", "yellow")
            }
            "已收货", "待评价" -> {
                ivOrderStatus.setImageResource(R.mipmap.order_receive_good)
                initOrderStatusColor("已确认收货", "收货商品了记得给个好评哦", "green")
            }
            "已取消" -> {
                ivOrderStatus.setImageResource(R.mipmap.order_cancel)
                initOrderStatusColor("订单取消了", "好桑心，订单被取消了", "gray")
            }
            "已完成" -> {
                ivOrderStatus.setImageResource(R.mipmap.order_complete)
                initOrderStatusColor("订单完成了", "太棒了，订单全部完成啦", "green")
            }
        }
    }

    fun stampToDate(s: Long): String {
        val simpleDateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
        val date = Date(s * 1000)
        return simpleDateFormat.format(date)
    }

    private fun initOrderStatusColor(title: String?, content: String?, type: String) {
        var textColor = resources.getColor(R.color.color_0EA60)
        if (type == "red") {
            textColor = resources.getColor(R.color.color_E83227)
        } else if (type == "yellow") {
            textColor = resources.getColor(R.color.color_F68812)
        } else if (type == "gray") {
            textColor = resources.getColor(R.color.color_C6C6C6)
        }

        tvOrderStatus.setTextColor(textColor)
        tvOrderDesc.setTextColor(textColor)
        tvOrderStatus.text = title
        tvOrderDesc.text = content
    }


    override fun createPresenter() = OrderDetailPresenterImpl(this)


    override fun onOrderDetailSuccess(bean: OrderDetail) {
        hidePageLoading()
        order = bean

        if (bean.isVirtualOrderTag()) {
            mToolBarDelegate.setMidTitle("服务订单详情")
        }

        initOrderStatus()


        var shippingType = "快递发货"
        if (bean.shippingType == IConstant.SHIP_TYPE_LOCAL) {
            shippingType = "同城配送"
        } else if (bean.shippingType == IConstant.SHIP_TYPE_SELF) {
            shippingType = "门店自提"
        }
        tvShipType.text = shippingType

        tvShipName.text = bean.shipName.orEmpty()
        if (bean.shipMobile.orEmpty().length > 7) {
            tvShipPhone.text =
                bean.shipMobile?.substring(0, 3) + "****" + bean.shipMobile?.substring(7)
        }
        val address = bean.shipProvince.orEmpty() + bean.shipCity.orEmpty() +
                bean.shipCounty.orEmpty() +
                bean.shipTown.orEmpty() + bean.shipAddr.orEmpty()
        tvShipAddress.text = address
        if (address.isEmpty()) llShipAddress.gone()
        tvShipInfoCopy.setOnClickListener {
            OtherUtils.copyToClipData(bean.shipName.orEmpty() + "，" + bean.shipMobile.orEmpty() + "，" + address)
        }
        initLeaderInfo(bean)

//        商品信息
        initGoodsInfo(bean)

        // 使用
        initUseInfo(bean);

        //其它信息
        initOtherInfo()

        //底部按钮
        initBottomButton()


        //如果时自提订单，则需要做额外设置
        if (bean.shippingType == IConstant.SHIP_TYPE_SELF) {
            orderStatusTextLayout.gone()
            tvStation.text = "取货信息"
            orderPersonName.gone()
            orderPhoto.text = "取货电话"
            tvLeaderInfoKey.gone()
            rlLeadInfoValue.gone()
            tvShipInfoCopy.text = "一键复制取货信息"

            //设置底部按钮
            llOrderBottom.visiable()
            back.visiable()
            hexiaoOrder.visiable()
            view.visiable()
        }


    }

    private fun initUseInfo(bean: OrderDetail) {
        if (order?.isVirtualOrderTag() == true) {
            tvUseInfo.visiable();
            llUseInfo.visiable();

            rlLeadInfoValue.gone();
            tvLeaderInfoKey.gone();
            tvStation.gone();
            llStation.gone();


            // 运费金额
            layoutShipMoney.gone();
            lineShipMoney.gone();
            // 订单总额
            lineOrderMoney.gone();
            layoutOrderMoney.gone();
        } else {
            tvUseInfo.gone();
            llUseInfo.gone();

            rlLeadInfoValue.visiable();
            tvLeaderInfoKey.visiable();
            tvStation.visiable();
            llStation.visiable();

            // 运费金额
            layoutShipMoney.visiable();
            lineShipMoney.visiable();
            // 订单总额
            lineOrderMoney.visiable();
            layoutOrderMoney.visiable();
        }
        tvDetailDateLimit.setText(stampToDate(order?.expiryDayTimestamp ?: 0));
        tvDetailDateTime.setText(
            String.format(
                "%s%s%s", if (bean?.availableDate?.length == 0) "" else "每",
                bean?.availableDate,
                if (bean?.availableDate?.length == 0) "" else "可用"
            )
        );
    }

    private fun initBottomButton() {
        val orderOperateAllowable = order?.orderOperateAllowableVo
        var showBottomArea = false
        if (orderOperateAllowable != null) {
            if (orderOperateAllowable.allowAuditAfterSell) tvAfterSale.visibility = View.VISIBLE
            if (orderOperateAllowable.allowCancel || orderOperateAllowable.allowServiceCancel) {
                tvCancelOrder.visibility = View.VISIBLE
            }
            if (orderOperateAllowable.allowDelete) tvDelete.visibility = View.VISIBLE
            if (orderOperateAllowable.allowCheckExpress) tvLookLogistics.visibility = View.VISIBLE
            if (orderOperateAllowable.allowEditPrice) tvUpdatePrice.visibility = View.VISIBLE
            if (orderOperateAllowable.allowShip) {
                if (order?.isVirtualOrderTag() == true) {
                    tvVerify.visibility = View.VISIBLE
                } else {
                    tvShipment.visibility = View.VISIBLE
                }
            }
            if (orderOperateAllowable.allowServiceCancel || orderOperateAllowable.allowAuditAfterSell || orderOperateAllowable.allowCancel
                || orderOperateAllowable.allowDelete || orderOperateAllowable.allowCheckExpress
                || orderOperateAllowable.allowEditPrice || orderOperateAllowable.allowShip
            ) {
                showBottomArea = true
            }
        }

        if (showBottomArea) {
            llOrderBottom.visibility = View.VISIBLE
        } else {
            llOrderBottom.visibility = View.GONE
        }

    }

    private fun initOtherInfo() {
        tvOrderNumber.text = "订单编号：" + order?.sn
        var orderTypeName = "普通订单"//normal
        if ("pintuan" == order?.orderType) {
            orderTypeName = "拼团订单"
        } else if ("shetuan" == order?.orderType) {
            orderTypeName = "社区团购订单"
        }
        tvOrderType.text = orderTypeName
        tvPayMethod.text = order?.paymentMethodName
        tvCreateOrderTime.text = TimeUtils.date2String(order?.createTime?.let { Date(it * 1000) })
        if (order?.shipTime != null) tvSendGoodsTime.text =
            TimeUtils.date2String(order?.shipTime?.let { Date(it * 1000) })
        if (order?.signingTime != null) tvReceiveGoodsTime.text =
            TimeUtils.date2String(order?.signingTime?.let { Date(it * 1000) })
        tvOrderRemark.text = order?.remark
    }

    private fun initGoodsInfo(bean: OrderDetail) {
        tvDetailGoodsMoney.text = "￥" + order?.goodsPrice
        baozhuangMoney.text = "￥" + order?.package_price
        tvDetailShipMoney.text = "￥" + order?.shippingPrice
        tvDetailOrderMoney.text = "￥" + order?.orderPrice
        tvDetailDiscountMoney.text = "￥" + order?.discountPrice
        tvDetailPayMoney.text = "￥" + order?.needPayMoney
        rvOrderDetailGoods.layoutManager = LinearLayoutManager(this)
        val adapter = object :
            BaseQuickAdapter<OrderSku, BaseViewHolder>(R.layout.order_adapter_detail_goods) {
            override fun convert(helper: BaseViewHolder, item: OrderSku) {
                helper.setText(R.id.tvProductName, item.name)
                helper.setText(R.id.tvProductPrice, "￥" + item.purchasePrice)
                helper.setText(R.id.tvProductCount, "×" + item.num)
                val tvProductAttribute = helper.getView<TextView>(R.id.tvProductAttribute)
                tvProductAttribute.text = ""
                if (item.specList != null && item.specList!!.isNotEmpty()) {
                    var attributeString = ""
                    item.specList!!.forEach { bean ->
                        attributeString =
                            attributeString + bean.specName + ":" + bean.specValue + ","
                    }
                    if (attributeString.endsWith(",")) attributeString =
                        attributeString.substring(0, attributeString.length - 1)
                    tvProductAttribute.text = attributeString
                }

                val ivProduct2 = helper.getView<ImageView>(R.id.ivProduct2)
                GlideUtils.setImageUrl(ivProduct2, item.goodsImage)
                val tvProductRefund = helper.getView<TextView>(R.id.tvProductRefund)
                tvProductRefund.visibility = View.GONE
                when (item.serviceStatus) {
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
            }

        }

        rvOrderDetailGoods.adapter = adapter
        adapter.setNewData(bean.orderSkuList)
    }

    private fun initLeaderInfo(bean: OrderDetail) {
        if (bean.orderType == "shetuan" && bean.leader != null && !TextUtils.isEmpty(bean.leader!!.leaderMobile)) {
            tvLeaderInfoKey.visibility = View.VISIBLE
            rlLeadInfoValue.visibility = View.VISIBLE
            tvLeadName.text = bean.leader?.leaderName
            GlideUtils.setImageUrl(ivLeaderHead, bean.leader?.facadePicUrl)
            tvLeaderPoint.text = bean.leader?.siteName
            tvLeaderAddress.text = bean.leader?.address
            tvLeaderPhone.text = bean.leader?.leaderMobile
            tvLeaderPhone.setOnClickListener {
                OtherUtils.goToDialApp(this@OrderDetailActivity, bean.leader?.leaderMobile)
            }
        } else {
            tvLeaderInfoKey.visibility = View.GONE
            rlLeadInfoValue.visibility = View.GONE
        }
    }

    override fun onOrderDetailError(code: Int) {
        hidePageLoading()
    }

    override fun onDeleteOrderSuccess() {
        showToast("删除成功")
        EventBus.getDefault().post(OrderNumberEvent())
    }

    override fun verifySuccess() {
        mPresenter.requestOrderDetailData(tradeSn)
    }

    override fun verifyFailed() {
        showToast("核销失败");
    }

    override fun onClick(v: View) {
        when (v.id) {
            R.id.tvCancelOrder -> {
                val intent = Intent(this, OrderCancelActivity::class.java)
                intent.putExtra("orderId", order?.sn)
                startActivityForResult(intent, SingleOrderListFragment.REQUEST_CODE)
            }
            R.id.tvUpdatePrice -> {//修改价格
                val intent = Intent(this, UpdatePriceActivity::class.java)
                intent.putExtra("orderAmount", order?.orderPrice)
                intent.putExtra("shippingAmount", order?.shippingPrice)
                intent.putExtra("orderId", order?.sn)
                startActivityForResult(intent, SingleOrderListFragment.REQUEST_CODE)
            }
            R.id.tvQuickPay -> {//催付 备注:已取消该功能
//                        DialogUtils.showDialog(activity!!, "催付提示", "确定要催促用户付款吗？",
//                            "取消", "确定", View.OnClickListener { }, View.OnClickListener {
//                            })
            }
            R.id.tvShipment -> {//发货
                val intent = Intent(this, OrderSendGoodsActivity::class.java)
                intent.putExtra("orderId", order?.sn)
                intent.putExtra("shippingType", order?.shippingType)
                startActivityForResult(intent, SingleOrderListFragment.REQUEST_CODE)
            }
            R.id.tvVerify -> {// 核销
                order?.verificationCode?.apply {
                    DialogUtils.showDialog(context!!, "核销提示", "确定要核销该订单吗？",
                        "取消", "确定核销", { }, {
                            showDialogLoading()
                            mPresenter?.verifyOrderCode(order?.verificationCode!!)
                        })
                }
            }
            R.id.tvLookLogistics -> {//查看物流
                val intent = Intent(this, LogisticsInfoActivity::class.java)
                intent.putExtra("orderId", order?.sn)
                intent.putExtra("shipNo", order?.shipNo)
                intent.putExtra("logiId", order?.logiId.toString())
                intent.putExtra("logiName", order?.logiName)
                startActivity(intent)
            }
            R.id.tvAfterSale -> {//售后/退款
                val intent = Intent(this, AfterSaleActivity::class.java)
                intent.putExtra("orderId", order?.sn)
                startActivityForResult(intent, SingleOrderListFragment.REQUEST_CODE)
            }
            R.id.tvDelete -> {
                DialogUtils.showDialog(this, "删除提示", "删除后不可恢复，确定要删除该订单吗？",
                    "取消", "确定删除", { }, {
                        showDialogLoading()
                        mPresenter?.deleteOrder(order?.sn!!)
                    })
            }
            R.id.back -> {
                finish()
            }
            R.id.hexiaoOrder -> {
                order?.sn?.let { mPresenter?.verifyOrderCodeSelf(it) }
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == SingleOrderListFragment.REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            EventBus.getDefault().post(OrderNumberEvent())
            mPresenter.requestOrderDetailData(tradeSn)
        }
    }

    override fun onDestroy() {
        mCoroutine.destroy()
        super.onDestroy()
    }
}