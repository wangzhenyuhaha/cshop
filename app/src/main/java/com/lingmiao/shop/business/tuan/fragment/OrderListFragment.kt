package com.lingmiao.shop.business.tuan.fragment

import android.app.Activity
import android.os.Bundle
import android.view.View
import com.blankj.utilcode.util.ActivityUtils
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.lingmiao.shop.R
import com.lingmiao.shop.base.IConstant
import com.lingmiao.shop.business.tuan.OrderDeliveryActivity
import com.lingmiao.shop.business.tuan.OrderDetailActivity
import com.lingmiao.shop.business.tuan.OrderExpressActivity
import com.lingmiao.shop.business.tuan.OrderRefuseActivity
import com.lingmiao.shop.business.tuan.adapter.OrderAdapter
import com.lingmiao.shop.business.tuan.bean.OrderVo
import com.lingmiao.shop.business.tuan.presenter.OrderListPresenter
import com.lingmiao.shop.business.tuan.presenter.impl.OrderListPresenterImpl
import com.lingmiao.shop.widget.EmptyView
import com.james.common.base.loadmore.BaseLoadMoreFragment
import com.james.common.base.loadmore.core.IPage
import com.james.common.utils.DialogUtils
import com.lingmiao.shop.business.order.OrderShowActivity

class OrderListFragment : BaseLoadMoreFragment<OrderVo, OrderListPresenter>(),
    OrderListPresenter.View {

    companion object {

        val TYPE_ALL = 0;

        val TYPE_UN_CONFIRM = TYPE_ALL + 1;

        val TYPE_UN_DELIVERY = TYPE_UN_CONFIRM + 1;

        val TYPE_DELIVERED = TYPE_UN_DELIVERY + 1;

        val TYPE_FINISH = TYPE_DELIVERED + 1;

        private fun instance(type : Int) : OrderListFragment {
            return OrderListFragment().apply {
                arguments = Bundle().apply {
                    putInt(IConstant.BUNDLE_KEY_OF_VIEW_TYPE, type);
                }
            }
        }

        fun allOrder(): OrderListFragment {
            return instance(TYPE_ALL);
        }

        fun confirm(): OrderListFragment {
            return instance(TYPE_UN_CONFIRM);
        }

        fun delivery(): OrderListFragment {
            return instance(TYPE_UN_DELIVERY);
        }

        fun sign(): OrderListFragment {
            return instance(TYPE_DELIVERED);
        }

        fun finish() : OrderListFragment {
            return instance(TYPE_FINISH);
        }
    }

    override fun initAdapter(): BaseQuickAdapter<OrderVo, BaseViewHolder> {
        return OrderAdapter().apply {
            setOnItemChildClickListener { adapter, view, position ->
                val item = adapter.data[position] as OrderVo;

                if(R.id.orderRefuseTv == view.id) {
                    // 拒绝
                    OrderRefuseActivity.open(context!!, "id");
                }else if (R.id.orderConfirmTv == view.id) {
                    // 接单
                    DialogUtils.showDialog(
                        context as Activity, "接单提示", "确定接受该订单吗？",
                        "取消", "确定接单", View.OnClickListener { }, View.OnClickListener {
//                            showDialogLoading()

                        })
                } else if (R.id.orderPrintTv == view.id) {
                    // 打印
                } else if (R.id.orderDeliveryTv == view.id) {
                    // 发货
                    OrderDeliveryActivity.open(context!!, "id");
                } else if (R.id.orderExpressTv == view.id) {
                    // 物流
                    OrderExpressActivity.open(context!!, "id");
                } else if (R.id.orderDeleteTv == view.id) {
                    // 删除
                    DialogUtils.showDialog(
                        context as Activity, "删除提示", "删除后不可恢复，确定要删除该订单吗？",
                        "取消", "确定删除", View.OnClickListener { }, View.OnClickListener {
//                            showDialogLoading()

                        })
                }
            }
            setOnItemClickListener { adapter, view, position ->
                val item = adapter.data[position] as OrderVo;
                OrderDetailActivity.open(context!!, item);
            }
            emptyView = EmptyView(mContext).apply {
                setBackgroundResource(R.color.common_bg)
            }
        }
    }

    override fun createPresenter(): OrderListPresenter? {
        return OrderListPresenterImpl(this);
    }

    override fun executePageRequest(page: IPage) {
        mPresenter?.loadList(page, mAdapter.data);
    }
}