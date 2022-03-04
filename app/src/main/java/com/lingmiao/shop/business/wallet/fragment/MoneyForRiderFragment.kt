package com.lingmiao.shop.business.wallet.fragment

import android.view.View
import androidx.core.content.ContextCompat
import com.blankj.utilcode.util.SPUtils
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.james.common.base.loadmore.BaseLoadMoreFragment
import com.james.common.base.loadmore.core.IPage
import com.lingmiao.shop.R
import com.lingmiao.shop.business.order.bean.OrderList
import com.lingmiao.shop.business.wallet.adapter.DepositAdapter
import com.lingmiao.shop.business.wallet.api.WalletConstants
import com.lingmiao.shop.business.wallet.bean.DepositVo
import com.lingmiao.shop.business.wallet.event.RefreshListEvent
import com.lingmiao.shop.business.wallet.presenter.RiderListPresenter
import com.lingmiao.shop.business.wallet.presenter.impl.RiderListPresenterImpl
import com.lingmiao.shop.util.stampToDate
import com.lingmiao.shop.widget.EmptyView
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode

class MoneyForRiderFragment : BaseLoadMoreFragment<DepositVo, RiderListPresenter>(),
    RiderListPresenter.View {

    private var accountId: String? = null

    companion object {
        fun newInstance(): MoneyForRiderFragment {
            return MoneyForRiderFragment()
        }
    }

    override fun createPresenter(): RiderListPresenter {
        return RiderListPresenterImpl(this)
    }

    override fun initAdapter(): RiderAdapter {
        return RiderAdapter().apply {
            emptyView = EmptyView(mContext).apply {
                setBackgroundResource(R.color.common_bg)
            }
        }
    }

    class RiderAdapter :
        BaseQuickAdapter<DepositVo, BaseViewHolder>(R.layout.wallet_adapter_rider) {

        override fun convert(helper: BaseViewHolder, item: DepositVo?) {

            helper.setText(R.id.tv_wallet_deposit_item_type, item?.tradeTypeName)
            helper.setText(R.id.tv_wallet_deposit_item_date, item?.tradeTime)
            helper.setText(R.id.tv_wallet_deposit_item_amount, String.format("%s", item?.amount))
            helper.setText(R.id.tv_wallet_deposit_sn, item?.tradeVoucherNo)
            //支付是红色的  提现是蓝的
            helper.setTextColor(
                R.id.tv_wallet_deposit_item_amount,
                if (item?.tradeTypeName == "支付") ContextCompat.getColor(
                    mContext,
                    R.color.red
                ) else
                    ContextCompat.getColor(mContext, R.color.primary)
            )
        }
    }

    override fun initOthers(rootView: View) {
        accountId = SPUtils.getInstance().getString(WalletConstants.RIDER_ACCOUNT_ID)
        //   mSmartRefreshLayout.isNestedScrollingEnabled = false
    }

    override fun executePageRequest(page: IPage) {
        mPresenter?.loadList(page, accountId!!, mAdapter.data)
    }

    override fun useEventBus(): Boolean {
        return true
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onRefreshEvent(event: RefreshListEvent) {
        if (event.isRefreshDepositList()) {
            mLoadMoreDelegate?.refresh();
        }
    }
}