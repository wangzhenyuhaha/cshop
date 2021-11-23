package com.lingmiao.shop.business.order

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import com.blankj.utilcode.util.TimeUtils
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.james.common.base.BaseActivity
import com.james.common.netcore.coroutine.CoroutineSupport
import com.lingmiao.shop.R
import com.lingmiao.shop.base.IConstant
import com.lingmiao.shop.business.order.bean.OrderDetail
import com.lingmiao.shop.business.order.bean.OrderNumberEvent
import com.lingmiao.shop.business.order.bean.OrderSku
import com.lingmiao.shop.business.order.presenter.OrderDetailPresenter
import com.lingmiao.shop.business.order.presenter.impl.OrderDetailPresenterImpl
import com.lingmiao.shop.util.GlideUtils
import kotlinx.android.synthetic.main.order_activity_order_detail.back
import kotlinx.android.synthetic.main.order_activity_order_detail.hexiaoOrder
import kotlinx.android.synthetic.main.order_activity_self_order_detail.*
import org.greenrobot.eventbus.EventBus
import java.text.SimpleDateFormat
import java.util.*


class SelfOrderDetailActivity : BaseActivity<OrderDetailPresenter>(), OrderDetailPresenter.View,
    View.OnClickListener {


    private val mCoroutine: CoroutineSupport by lazy { CoroutineSupport() }

    //订单详情
    private var order: OrderDetail? = null

    //订单SN
    private lateinit var tradeSn: String

    override fun getLayoutId() = R.layout.order_activity_self_order_detail

    override fun initView() {
        tradeSn = intent?.getStringExtra("orderId").toString()
        mToolBarDelegate.setMidTitle("订单详情")
        mPresenter.requestOrderDetailData(tradeSn)

        back.setOnClickListener(this)
        hexiaoOrder.setOnClickListener(this)
    }


    override fun createPresenter() = OrderDetailPresenterImpl(this)

    //加载数据
    override fun onOrderDetailSuccess(bean: OrderDetail) {
        hidePageLoading()
        order = bean

        if (bean.isVirtualOrderTag()) {
            mToolBarDelegate.setMidTitle("服务订单详情")
        }


        var shippingType = "快递发货"
        if (bean.shippingType == IConstant.SHIP_TYPE_LOCAL) {
            shippingType = "同城配送"
        } else if (bean.shippingType == IConstant.SHIP_TYPE_SELF) {
            shippingType = "门店自提"
        }
        pesongfanshi.text = shippingType

        //提货号码
        val photoNumber = if (bean.shipMobile.orEmpty().length > 7) bean.shipMobile?.substring(
            0,
            3
        ) + "****" + bean.shipMobile?.substring(7) else ""
        tihuohaoma.text = photoNumber

        //自提点，即店铺名称
        zitidian.text = bean.sellerName

        //取货地址
        tihuodizhi.text = bean.sellerAddress

        //订单编号
        dingdanbianhao.text = tradeSn
        dingdanzhuantai.text = bean.orderStatus

        //商品信息
        dingdanshangpin.layoutManager = LinearLayoutManager(this)
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
        dingdanshangpin.adapter = adapter
        adapter.setNewData(bean.orderSkuList)
        //商品金额
        "￥${order?.goodsPrice}".also {
            selfOrderGoodMoney.text = it
        }
        //包装费
        "￥${order?.package_price}".also {
            selfOrderBaozhuang.text = it
        }
        //运费金额
        "￥${order?.shippingPrice}".also {
            selfOrderyunfei.text = it
        }
        //订单总额
        "￥${order?.orderPrice}".also {
            selfOrderzonge.text = it
        }
        //优惠金额
        "￥${order?.discountPrice}".also {
            selfOrderyouhui.text = it
        }
        //应付总额
        "￥${order?.needPayMoney}".also {
            selfOrderyingfu.text = it
        }

        //订单类型
        var orderTypeName = "普通订单"//normal
        if ("pintuan" == order?.orderType) {
            orderTypeName = "拼团订单"
        } else if ("shetuan" == order?.orderType) {
            orderTypeName = "社区团购订单"
        }
        selfOrderType.text = orderTypeName

        //支付方式
        selfPayMethod.text = order?.paymentMethodName

        //下单时间
        selfCreateOrderTime.text = TimeUtils.date2String(order?.createTime?.let { Date(it * 1000) })

        //发货时间
        if (order?.shipTime != null) selfSendGoodsTime.text =
            TimeUtils.date2String(order?.shipTime?.let { Date(it * 1000) })

        //签收时间
        if (order?.signingTime != null) selfReceiveGoodsTime.text =
            TimeUtils.date2String(order?.signingTime?.let { Date(it * 1000) })

        //备注
        selfOrderRemark.text = order?.remark


    }


    override fun onOrderDetailError(code: Int) {
        hidePageLoading()
    }

    override fun onClick(v: View) {
        when (v.id) {
            R.id.back -> {
                finish()
            }
            R.id.hexiaoOrder -> {
                order?.sn?.let { mPresenter?.verifyOrderCodeSelf(it) }
            }
        }
    }


    override fun onDeleteOrderSuccess() {
        showToast("删除成功")
        EventBus.getDefault().post(OrderNumberEvent())
    }

    override fun verifySuccess() {
        mPresenter.requestOrderDetailData(tradeSn)
    }

    override fun verifyFailed() {
        showToast("核销失败")
    }

    fun stampToDate(s: Long): String {
        val simpleDateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
        val date = Date(s * 1000)
        return simpleDateFormat.format(date)
    }

    override fun onDestroy() {
        mCoroutine.destroy()
        super.onDestroy()
    }
}