package com.lingmiao.shop.business.order

import android.app.Activity
import android.text.TextUtils
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.blankj.utilcode.util.GsonUtils
import com.blankj.utilcode.util.TimeUtils
import com.blankj.utilcode.util.ToastUtils
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.lingmiao.shop.R
import com.lingmiao.shop.business.order.presenter.AfterSalePresenter
import com.lingmiao.shop.business.order.presenter.impl.AfterSalePresenterImpl
import com.lingmiao.shop.util.GlideUtils
import com.google.gson.reflect.TypeToken
import com.james.common.base.BaseActivity
import com.james.common.netcore.coroutine.CoroutineSupport
import com.james.common.utils.DialogUtils
import com.lingmiao.shop.business.order.bean.*
import kotlinx.android.synthetic.main.order_activity_after_sale.*
import kotlinx.android.synthetic.main.order_activity_after_sale.rvOrderDetailGoods
import org.greenrobot.eventbus.EventBus
import java.util.*
import kotlin.collections.ArrayList

/**
 *   售后处理
 */
class AfterSaleActivity : BaseActivity<AfterSalePresenter>(), AfterSalePresenter.View {


    private val mCoroutine: CoroutineSupport by lazy { CoroutineSupport() }
    private var refundId: String = ""
    private lateinit var orderId: String
    private var refundMoney: String? = "0"

    override fun getLayoutId(): Int {
        return R.layout.order_activity_after_sale
    }

    override fun initView() {
        mToolBarDelegate.setMidTitle("售后处理")
//        orderId = "20200702000007"
        orderId = intent.getStringExtra("orderId").toString()
        showPageLoading()

        mPresenter.requestAfterSaleData(orderId)

        tvAfterSaleRefuse.setOnClickListener {
            DialogUtils.showInputDialog(this, "审核拒绝", "拒绝原因:", null) {
                showDialogLoading()
                mPresenter.doAfterSaleAction(0, refundId, refundMoney, it)
            }
        }
        tvAfterSalePass.setOnClickListener {
            DialogUtils.showInputDialog(this, "审核通过", "退回金额:",  "请输入",refundMoney,"取消", "确定",null) {
                try {
                    var beforeMoney = refundMoney?.toDoubleOrNull()
                    var afterMoney = it.toDoubleOrNull()
                    if (afterMoney == null) {
                        showToast("请输入正确的金额格式")
                        return@showInputDialog
                    }
                    if (beforeMoney != null && afterMoney > beforeMoney) {
                        showToast("输入的金额不能大于总的可退款金额")
                        return@showInputDialog
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                }
                showDialogLoading()
                mPresenter.doAfterSaleAction(1, refundId, it, "")
            }
        }

        tvAfterSaleStock.setOnClickListener {
            showDialogLoading()
            mPresenter.doAfterSaleStock(refundId)
        }
    }


    override fun createPresenter(): AfterSalePresenter {
        return AfterSalePresenterImpl(this, this)
    }

    override fun onAfterSaleSuccess(bean: AfterSale) {
        hidePageLoading()

//        规则：根据订单中refund_status判断  1.APPLY-->>>审核  2.PASS --->>>入库（需要增加一个入库操作按钮）
//        订单售后处理漏了 一个退货入库操作 只有入库之后才能真正退款
        refundId = bean.refund?.sn.toString()
        tvAfterSaleOrderId.text = bean.refund?.orderSn

        tvAfterSaleApplyMoney.text = "¥" + bean.refund?.refundPrice?.toString()
        refundMoney = bean.refund?.refundPrice?.toString()
        tvAfterSaleBuyer.text = bean.refund?.memberName
        tvAfterSaleOrderApplyTime.text = TimeUtils.date2String(bean.refund?.createTime?.let {
            Date(
                it * 1000
            )
        })
        tvAfterSaleOrderApplyReason.text = bean.refund?.refundReason

        tvAfterSaleOrderRefundStatus.text = bean.refund?.refundStatusText
        tvAfterSaleOrderRefundMethod.text = bean.refund?.accountTypeText
        tvAfterSaleOrderRefundMoney.text = "¥" + bean.refund?.refundPrice.toString()
        tvAfterSaleOrderAuditRemark.text = bean.refund?.sellerRemark

        initGoodsInfo(bean.refundGoods)
        showBottomButton("PASS" == bean.refund?.refundStatus)

    }

    private fun showBottomButton(isStock: Boolean) {
        tvAfterSaleRefuse.visibility = View.GONE
        tvAfterSalePass.visibility = View.GONE
        tvAfterSaleStock.visibility = View.GONE
        if (isStock) {
            tvAfterSaleStock.visibility = View.VISIBLE
        } else {
            tvAfterSaleRefuse.visibility = View.VISIBLE
            tvAfterSalePass.visibility = View.VISIBLE
        }
    }

    override fun onAfterSaleError(code: Int) {
        hidePageLoading()
    }

    override fun onAfterSaleActionSuccess() {
        hideDialogLoading()
        ToastUtils.showShort("提交成功")
        setResult(Activity.RESULT_OK)
        EventBus.getDefault().post(OrderTabChangeEvent(2));
        finish()
    }

    override fun onAfterSaleActionError(code: Int) {
        hideDialogLoading()
    }

    override fun onAfterSaleStockSuccess() {
        hideDialogLoading()
        showToast("入库成功")
        showBottomButton(false)
    }

    override fun onAfterSaleStockError(code: Int) {
        hideDialogLoading()
    }

    private fun initGoodsInfo(refundGoods: List<RefundGood>?) {
        if (refundGoods.isNullOrEmpty()) return
        rvOrderDetailGoods.layoutManager = LinearLayoutManager(this)
        val adapter = object :
            BaseQuickAdapter<RefundGood, BaseViewHolder>(R.layout.order_adapter_refund_goods) {
            override fun convert(helper: BaseViewHolder, item: RefundGood) {
                helper.setText(R.id.tvProductName, item.goodsName)
                helper.setText(R.id.tvProductPrice, "￥" + item.price)
                helper.setText(R.id.tvProductCount, "退回" + item.returnNum + "件")
                helper.setTextColor(
                    R.id.tvProductCount,
                    ContextCompat.getColor(context, R.color.red)
                )
                val tvProductAttribute = helper.getView<TextView>(R.id.tvProductAttribute)
                val viRefundDivideLine = helper.getView<View>(R.id.viRefundDivideLine)
                if (helper.layoutPosition == refundGoods.size - 1) {
                    viRefundDivideLine.visibility = View.GONE
                } else {
                    viRefundDivideLine.visibility = View.VISIBLE
                }
                tvProductAttribute.text = ""
                var specList = ArrayList<AfterSaleSpec>()
                if (!TextUtils.isEmpty(item.specJson) && "null" != item.specJson) {
                    specList.addAll(
                        GsonUtils.fromJson<ArrayList<AfterSaleSpec>>(
                            item.specJson,
                            object : TypeToken<ArrayList<AfterSaleSpec>>() {}.type
                        )
                    )
                }
                var attributeString = ""
                specList.forEach { bean ->
                    attributeString =
                        attributeString + bean.specName + ":" + bean.specValue + ","
                }
                if (attributeString.endsWith(",")) attributeString =
                    attributeString.substring(0, attributeString.length - 1)
                tvProductAttribute.text = attributeString

                val ivProduct2 = helper.getView<ImageView>(R.id.ivProduct2)
                GlideUtils.setImageUrl(ivProduct2, item.goodsImage)
            }


        }

        rvOrderDetailGoods.adapter = adapter
        adapter.setNewData(refundGoods)
    }

}