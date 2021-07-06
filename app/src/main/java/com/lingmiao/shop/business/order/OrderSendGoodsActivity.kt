package com.lingmiao.shop.business.order

import android.app.Activity
import android.text.TextUtils
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import com.blankj.utilcode.util.LogUtils
import com.blankj.utilcode.util.ToastUtils
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.lingmiao.shop.R
import com.lingmiao.shop.base.IConstant
import com.lingmiao.shop.business.order.bean.LogisticsCompanyItem
import com.lingmiao.shop.business.order.bean.OrderSingleReason
import com.lingmiao.shop.business.order.bean.OrderSendGoods
import com.lingmiao.shop.business.order.pop.LogisticsCompanyListPop
import com.lingmiao.shop.business.order.presenter.OrderSendGoodsPresenter
import com.lingmiao.shop.business.order.presenter.impl.OrderSendGoodsPresenterImpl
import com.james.common.base.BaseActivity
import com.james.common.netcore.coroutine.CoroutineSupport
import com.james.common.utils.exts.gone
import com.james.common.utils.exts.visiable
import kotlinx.android.synthetic.main.order_activity_order_send_goods.*

/**
 *   订单发货
 */
class OrderSendGoodsActivity : BaseActivity<OrderSendGoodsPresenter>(),
    OrderSendGoodsPresenter.View {


    private val mCoroutine: CoroutineSupport by lazy { CoroutineSupport() }
    private var orderId: String? = null
    private lateinit var shippingType: String
    private var logisticsCompany: String? = null
    private var logisticsId: String = ""
    private var logisticsCompanyList = mutableListOf<LogisticsCompanyItem>()

    override fun getLayoutId(): Int {
        return R.layout.order_activity_order_send_goods
    }

    override fun initView() {

        mToolBarDelegate.setMidTitle("订单发货")
        orderId = intent.getStringExtra("orderId")
        val tempType = intent.getStringExtra("shippingType")
        if(TextUtils.isEmpty(tempType)){
            shippingType = IConstant.SHIP_TYPE_GLOBAL
            LogUtils.e("shippingType 为null")
        }else{
            shippingType = tempType.toString()
        }
        val tempList = initAdapter()


        mPresenter.requestLogisticsCompanyList()
        tvOrderSendGoods.setOnClickListener {
            var selectedIndex = -1
            tempList.forEachIndexed { index, bean ->
                run {
                    if (bean.selected) selectedIndex = index
                }
            }
//            if(selectedIndex<0){
//                ToastUtils.showShort("请选择取消理由")
//                return@setOnClickListener
//            }
//            if(selectedIndex == 0 &&logisticsCompanyList.size>0){
//                logisticsCompany = logisticsCompanyList[0].logisticsCompanyDo?.name
//                logisticsId = logisticsCompanyList[0].logisticsCompanyDo?.id.toString()
//            }
            if (selectedIndex <= 1 && logisticsCompany == null) {
                ToastUtils.showShort("请选择快递公司")
                return@setOnClickListener
            }
            if (selectedIndex <= 1 && etOrderLogisticsNumber.text.toString().isEmpty()) {
                ToastUtils.showShort("请输入快递单号")
                return@setOnClickListener
            }
            showDialogLoading()
            mPresenter.requestOrderSendGoodsData(
                orderId!!,
                tvOrderLogisticsCompany.text.toString(),
                etOrderLogisticsNumber.text.toString(),
                logisticsId
            )
        }

        rlOrderLogisticsCompany.setOnClickListener {
            if(logisticsCompanyList.isEmpty()){
                showToast("没有获取到快递公司信息,请稍后再试")
                return@setOnClickListener
            }
            val pop = LogisticsCompanyListPop(this,logisticsCompanyList)
            pop.setOnClickListener { item->
                run {
                    logisticsCompany = item.logisticsCompanyDo?.name
                    logisticsId = item.logisticsCompanyDo?.id.toString()
                    tvOrderLogisticsCompany.text = logisticsCompany
                }
            }
            pop.showPopupWindow()
        }

    }

    private fun initAdapter(): MutableList<OrderSingleReason> {
        val adapter = object :
            BaseQuickAdapter<OrderSingleReason, BaseViewHolder>(R.layout.order_item_adapter_order_cancel) {
            override fun convert(helper: BaseViewHolder, item: OrderSingleReason) {
                val ivCancelStatus = helper.getView<ImageView>(R.id.ivCancelStatus)
                val tvCancelReasonInfo = helper.getView<TextView>(R.id.tvCancelReasonInfo)
                val viCancelDivide = helper.getView<View>(R.id.viCancelDivide)
                if (item.selected) {
                    ivCancelStatus.setImageResource(R.mipmap.iv_single_select)
                } else {
                    ivCancelStatus.setImageResource(R.mipmap.iv_single_default)
                }

                helper.setText(R.id.tvCancelReason, item.reason)

                if (item.remark == shippingType) {
                    tvCancelReasonInfo.visibility = View.VISIBLE
                } else {
                    tvCancelReasonInfo.visibility = View.GONE
                }

               viCancelDivide.visibility = if(helper.layoutPosition==2) View.GONE else View.VISIBLE
            }

        }

        val tempList = mutableListOf<OrderSingleReason>()
        tempList.add(OrderSingleReason("快递发货", shippingType == IConstant.SHIP_TYPE_GLOBAL, "GLOBAL"))
        tempList.add(OrderSingleReason("同城配送", shippingType == IConstant.SHIP_TYPE_LOCAL, "LOCAL"))
        tempList.add(OrderSingleReason("门店自提", shippingType == IConstant.SHIP_TYPE_SELF, "SELF"))
        rvOrderSendGoods.layoutManager = LinearLayoutManager(this)
        rvOrderSendGoods.adapter = adapter
        adapter.setNewData(tempList)
        adapter.setOnItemClickListener { adapter, view, position ->
            run {

                val reason = adapter.data[position] as OrderSingleReason
                tempList.forEachIndexed { index, item ->
                    run {
                        if (index == position) {
                            item.selected = !item.selected
                        } else {
                            item.selected = false
                        }
                    }
                }

                if(position==2&&reason.selected){
                    tvOrderLogisticsCompanyKey.gone()
                    llOrderLogisticsCompany.gone()
                }else{
                    tvOrderLogisticsCompanyKey.visiable()
                    llOrderLogisticsCompany.visiable()
                }
                adapter.notifyDataSetChanged()
            }
        }
        return tempList
    }


    override fun createPresenter(): OrderSendGoodsPresenter {
        return OrderSendGoodsPresenterImpl(this, this)
    }

    override fun onOrderSendGoodsSuccess() {
        hideDialogLoading()
        setResult(Activity.RESULT_OK)
        showToast("发货成功")
        finish()
    }

    override fun onOrderSendGoodsError(code: Int) {
        hideDialogLoading()

    }

    override fun onLogisticsCompanyListSuccess(data: List<LogisticsCompanyItem>) {
        logisticsCompanyList.addAll(data)
    }

    override fun onLogisticsCompanyListError(code: Int) {
    }

}