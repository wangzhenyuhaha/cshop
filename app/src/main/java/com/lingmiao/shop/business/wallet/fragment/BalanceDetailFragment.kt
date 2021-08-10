package com.lingmiao.shop.business.wallet.fragment

import android.util.Log
import android.view.View
import androidx.lifecycle.lifecycleScope
import com.blankj.utilcode.util.SPUtils
import com.james.common.base.loadmore.BaseLoadMoreFragment
import com.james.common.base.loadmore.core.IPage
import com.lingmiao.shop.R
import com.lingmiao.shop.base.IConstant
import com.lingmiao.shop.business.order.OrderShowActivity
import com.lingmiao.shop.business.wallet.adapter.DepositAdapter
import com.lingmiao.shop.business.wallet.api.WalletConstants
import com.lingmiao.shop.business.wallet.api.WalletRepository
import com.lingmiao.shop.business.wallet.bean.DepositVo
import com.lingmiao.shop.business.wallet.event.RefreshListEvent
import com.lingmiao.shop.business.wallet.presenter.TradeListPresenter
import com.lingmiao.shop.business.wallet.presenter.impl.TradeListPresenterImpl
import com.lingmiao.shop.widget.EmptyView
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode

/**
 * 余额交易记录
 */
class BalanceDetailFragment : BaseLoadMoreFragment<DepositVo, TradeListPresenter>(),
    TradeListPresenter.View {

    private var accountId: String? = null

    companion object {
        fun newInstance(): BalanceDetailFragment {
            return BalanceDetailFragment()
        }
    }

    override fun createPresenter(): TradeListPresenter {
        return TradeListPresenterImpl(this)
    }

    override fun initAdapter(): DepositAdapter {
        return DepositAdapter().apply {
            emptyView = EmptyView(mContext).apply {
                setBackgroundResource(R.color.common_bg)
            }
            setOnItemClickListener { adapter, view, position ->
                val item = getItem(position)
                val name = item?.tradeTypeName
                if (name.equals("转账")) {
                    val id = item?.tradeVoucherNo
                    val newID = id?.subSequence(0, id.length - 1)

                    //暂时这样,以前的订单的ID不正确，无法使用
//                    viewLifecycleOwner.lifecycleScope.launch(Dispatchers.Main)
//                    {
//                        val temp = WalletRepository.queryOrderDetail(newID.toString())
//                        OrderShowActivity.open(mContext, temp.data)
//                    }
                }

            }
        }
    }

    override fun initOthers(rootView: View) {
        accountId = SPUtils.getInstance().getString(WalletConstants.BALANCE_ACCOUNT_ID)
    }

    override fun onLoadMoreSuccess(list: List<DepositVo>?, hasMore: Boolean) {
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