package com.lingmiao.shop.business.order

import android.app.Activity
import android.view.View
import android.widget.ImageView
import androidx.recyclerview.widget.LinearLayoutManager
import com.blankj.utilcode.util.ToastUtils
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.lingmiao.shop.R
import com.lingmiao.shop.business.order.bean.OrderSingleReason
import com.lingmiao.shop.business.order.presenter.OrderCancelPresenter
import com.lingmiao.shop.business.order.presenter.impl.OrderCancelPresenterImpl
import com.james.common.base.BaseActivity
import com.james.common.netcore.coroutine.CoroutineSupport
import kotlinx.android.synthetic.main.order_activity_order_cancel.*

/**
 *   取消订单
 */
class OrderCancelActivity : BaseActivity<OrderCancelPresenter>(), OrderCancelPresenter.View {


    private val mCoroutine: CoroutineSupport by lazy { CoroutineSupport() }
    private var orderId: String? = null

    override fun getLayoutId(): Int {
        return R.layout.order_activity_order_cancel
    }

    override fun initView() {
        mToolBarDelegate.setMidTitle("取消订单")
        orderId = intent.getStringExtra("orderId")
        val tempList = mutableListOf<OrderSingleReason>()
        val adapter = object :
            BaseQuickAdapter<OrderSingleReason, BaseViewHolder>(R.layout.order_item_adapter_order_cancel) {
            override fun convert(helper: BaseViewHolder, item: OrderSingleReason) {
                val ivCancelStatus = helper.getView<ImageView>(R.id.ivCancelStatus)
                if(item.selected){
                    ivCancelStatus.setImageResource(R.mipmap.iv_single_select)
                }else{
                    ivCancelStatus.setImageResource(R.mipmap.iv_single_default)
                }

                helper.setText(R.id.tvCancelReason,item.reason)
                val viCancelDivide = helper.getView<View>(R.id.viCancelDivide)
                viCancelDivide.visibility = if(helper.layoutPosition==tempList.size-1) View.GONE else View.VISIBLE
            }

        }

        tempList.add(OrderSingleReason("商品库存不足",false))
        tempList.add(OrderSingleReason("店铺暂停营业",false))
        tempList.add(OrderSingleReason("联系不上买家",false))
        tempList.add(OrderSingleReason("订单信息有误",false))
        tempList.add(OrderSingleReason("其他",false))
        rvOrderCancelReason.layoutManager = LinearLayoutManager(this)
        rvOrderCancelReason.adapter = adapter
        adapter.setNewData(tempList)
        adapter.setOnItemClickListener { adapter, view, position ->
            run {

//                val orderCancelReason = adapter.data[position] as OrderCancelReason
                tempList.forEachIndexed { index, orderCancelReason ->
                    run {
                        if (index == position) {
                            orderCancelReason.selected = !orderCancelReason.selected
                        } else {
                            orderCancelReason.selected = false
                        }
                    }
                }

                adapter.notifyDataSetChanged()
            }
        }


        tvCancelOrder.setOnClickListener {
            var selectedIndex = -1
            tempList.forEachIndexed { index, orderCancelReason -> run{
                if(orderCancelReason.selected) selectedIndex = index
            } }
            if(selectedIndex<0){
                ToastUtils.showShort("请选择取消理由")
                return@setOnClickListener
            }
            if(selectedIndex==tempList.size-1&&etOrderCancelDesc.text.toString().isEmpty()){
                ToastUtils.showShort("请输入详细内容")
                return@setOnClickListener
            }
            showDialogLoading()
            mPresenter.requestOrderCancelData(orderId!!,tempList[selectedIndex].reason,etOrderCancelDesc.text.toString())
        }
    }


    override fun createPresenter(): OrderCancelPresenter {
        return OrderCancelPresenterImpl(this, this)
    }

    override fun onOrderCancelSuccess() {
        hideDialogLoading()
        ToastUtils.showShort("提交成功")
        setResult(Activity.RESULT_OK)
        finish()
    }

    override fun onOrderCancelError(code: Int) {
        hideDialogLoading()
    }

}