package com.lingmiao.shop.business.tuan

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.view.View
import android.widget.ImageView
import androidx.recyclerview.widget.LinearLayoutManager
import com.blankj.utilcode.util.ToastUtils
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.lingmiao.shop.R
import com.lingmiao.shop.base.IConstant
import com.lingmiao.shop.business.order.bean.OrderSingleReason
import com.lingmiao.shop.business.tuan.presenter.DeliveryGoodsPresenter
import com.lingmiao.shop.business.tuan.presenter.impl.DeliveryGoodsPresenterImpl
import com.james.common.base.BaseActivity
import kotlinx.android.synthetic.main.tuan_activity_order_delivery.*

class OrderDeliveryActivity : BaseActivity<DeliveryGoodsPresenter>(), DeliveryGoodsPresenter.View {

    private lateinit var id: String;

    companion object {
        fun open(context: Context, id: String) {
            if (context is Activity) {
                val intent = Intent(context, OrderDeliveryActivity::class.java)
                intent.putExtra(IConstant.BUNDLE_KEY_OF_ITEM_ID, id)
                context.startActivity(intent)
            }
        }
    }

    override fun initBundles() {
        id = intent?.getStringExtra(IConstant.BUNDLE_KEY_OF_ITEM_ID) ?: "";
    }

    override fun getLayoutId(): Int {
        return R.layout.tuan_activity_order_delivery;
    }

    override fun createPresenter(): DeliveryGoodsPresenter {
        return DeliveryGoodsPresenterImpl(this);
    }

    override fun initView() {
        mToolBarDelegate.setMidTitle("采购发货")

        val tempList = mutableListOf<OrderSingleReason>()
        tempList.add(OrderSingleReason("供应商承运", false))
        tempList.add(OrderSingleReason("采购方自提", false))

        val adapter = object :
            BaseQuickAdapter<OrderSingleReason, BaseViewHolder>(R.layout.order_item_adapter_order_cancel) {
            override fun convert(helper: BaseViewHolder, item: OrderSingleReason) {
                val ivCancelStatus = helper.getView<ImageView>(R.id.ivCancelStatus)
                if (item.selected) {
                    ivCancelStatus.setImageResource(R.mipmap.iv_single_select)
                } else {
                    ivCancelStatus.setImageResource(R.mipmap.iv_single_default)
                }

                helper.setText(R.id.tvCancelReason, item.reason)
                val viCancelDivide = helper.getView<View>(R.id.viCancelDivide)
                viCancelDivide.visibility =
                    if (helper.layoutPosition == tempList.size - 1) View.GONE else View.VISIBLE
            }
        }
        adapter.setOnItemClickListener { adapter, view, position ->
            run {
                tempList.forEachIndexed { index, type ->
                    run {
                        if (index == position) {
                            type.selected = !type.selected
                        } else {
                            type.selected = false
                        }
                    }
                }

                adapter.notifyDataSetChanged()
            }
        }
        adapter.setNewData(tempList)

        rvOrderDeliveryType.layoutManager = LinearLayoutManager(this)
        rvOrderDeliveryType.adapter = adapter;

        tvOrderDelivery.setOnClickListener {
            var selectedIndex = -1
            tempList.forEachIndexed { index, orderCancelReason ->
                run {
                    if (orderCancelReason.selected) selectedIndex = index
                }
            }
            if (selectedIndex < 0) {
                ToastUtils.showShort("请选择取消理由")
                return@setOnClickListener
            }
//            tvOrderDeliveryTruckName
//            tvOrderDeliveryTruckTelephone
//            tvOrderDeliveryTruckPlate

//            if (selectedIndex == tempList.size - 1 && etOrderRefuseDesc.text.toString().isEmpty()) {
//                ToastUtils.showShort("请输入详细内容")
//                return@setOnClickListener
//            }

        }

    }


}