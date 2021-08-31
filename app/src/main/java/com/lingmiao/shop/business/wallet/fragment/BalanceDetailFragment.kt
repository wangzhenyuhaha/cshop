package com.lingmiao.shop.business.wallet.fragment

import android.view.View
import androidx.core.content.ContextCompat
import com.blankj.utilcode.util.SPUtils
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.james.common.base.loadmore.BaseLoadMoreFragment
import com.james.common.base.loadmore.core.IPage
import com.lingmiao.shop.R
import com.lingmiao.shop.base.IConstant
import com.lingmiao.shop.business.order.bean.OrderList
import com.lingmiao.shop.business.wallet.api.WalletConstants
import com.lingmiao.shop.business.wallet.event.RefreshListEvent
import com.lingmiao.shop.business.wallet.presenter.OrderListPresenter
import com.lingmiao.shop.business.wallet.presenter.TradeListPresenter
import com.lingmiao.shop.business.wallet.presenter.impl.OrderListPresenterImpl
import com.lingmiao.shop.util.stampToDate
import com.lingmiao.shop.widget.EmptyView
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode

/**
 * 余额交易记录
 */
class BalanceDetailFragment : BaseLoadMoreFragment<OrderList, OrderListPresenter>(),
    OrderListPresenter.View {

    private var accountId: String? = null

    companion object {
        fun newInstance(): BalanceDetailFragment {
            return BalanceDetailFragment()
        }
    }

    override fun createPresenter(): OrderListPresenter {
        return OrderListPresenterImpl(this)
    }

    override fun initAdapter(): OrderAdapter {
        return OrderAdapter().apply {
            emptyView = EmptyView(mContext).apply {
                setBackgroundResource(R.color.common_bg)
            }
        }
    }

    class OrderAdapter :
        BaseQuickAdapter<OrderList, BaseViewHolder>(R.layout.wallet_adapter_deposit) {

        override fun convert(helper: BaseViewHolder, item: OrderList?) {

            item?.also {
                //  1表示退款  0表示正常订单
                val temp: Int = if (it.serviceStatus == "PASS") 1 else 0
                helper.setText(R.id.tv_wallet_deposit_item_type, if (temp == 1) "订单退款" else "订单收入")

                helper.setText(R.id.tv_wallet_deposit_item_date, stampToDate(it.createTime))


                if (temp == 1) {
                    //退款
                    helper.setText(
                        R.id.tv_wallet_deposit_item_amount,
                        String.format("%s", "-${it.orderAmount}")
                    )
                    helper.setTextColor(
                        R.id.tv_wallet_deposit_item_amount,
                        ContextCompat.getColor(mContext, R.color.red)
                    )
                } else {
                    //收入
                    helper.setText(
                        R.id.tv_wallet_deposit_item_amount,
                        String.format("%s", "+${it.orderAmount}")
                    )
                    helper.setTextColor(
                        R.id.tv_wallet_deposit_item_amount,
                        ContextCompat.getColor(mContext, R.color.primary)
                    )
                }


            }
        }
    }

    override fun initOthers(rootView: View) {
        accountId = SPUtils.getInstance().getString(WalletConstants.BALANCE_ACCOUNT_ID)
    }

    override fun onLoadMoreSuccess(list: List<OrderList>?, hasMore: Boolean) {
        super.onLoadMoreSuccess(list, hasMore)
        mLoadMoreDelegate?.loadFinish(list?.size ?: 0 >= IConstant.PAGE_SIZE, !list.isNullOrEmpty())
    }

    override fun executePageRequest(page: IPage) {
        mPresenter?.loadList(page, accountId!!, mAdapter.data)
    }

    override fun useEventBus(): Boolean {
        return true
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onRefreshEvent(event: RefreshListEvent) {
        if (event.isRefreshBalanceList()) {
            mLoadMoreDelegate?.refresh()
        }
    }
}