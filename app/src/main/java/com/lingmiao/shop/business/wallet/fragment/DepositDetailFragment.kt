package com.lingmiao.shop.business.wallet.fragment

import android.view.View
import com.blankj.utilcode.util.SPUtils
import com.lingmiao.shop.R
import com.lingmiao.shop.business.wallet.adapter.DepositAdapter
import com.lingmiao.shop.business.wallet.api.WalletConstants
import com.lingmiao.shop.business.wallet.presenter.TradeListPresenter
import com.lingmiao.shop.business.wallet.bean.DepositVo
import com.lingmiao.shop.business.wallet.event.RefreshListEvent
import com.lingmiao.shop.business.wallet.presenter.impl.TradeListPresenterImpl
import com.lingmiao.shop.widget.EmptyView
import com.james.common.base.loadmore.BaseLoadMoreFragment
import com.james.common.base.loadmore.core.IPage
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode

/**
 * 押金交易记录
 */
class DepositDetailFragment : BaseLoadMoreFragment<DepositVo, TradeListPresenter>(),
    TradeListPresenter.View {

    private var accountId : String ? = null;

    companion object {
        fun newInstance(): DepositDetailFragment {
            return DepositDetailFragment()
        }
    }

    override fun createPresenter(): TradeListPresenter? {
        return TradeListPresenterImpl(this);
    }

    override fun initAdapter(): DepositAdapter {
        return DepositAdapter().apply {
            emptyView = EmptyView(mContext).apply {
                setBackgroundResource(R.color.common_bg)
            }
        }
    }

    override fun initOthers(rootView: View) {
        accountId = SPUtils.getInstance().getString(WalletConstants.DEPOSIT_ACCOUNT_ID);
        mSmartRefreshLayout.setNestedScrollingEnabled(false);
    }

    override fun executePageRequest(page: IPage) {
        mPresenter?.loadList(page, accountId!!, mAdapter.data);
    }

    override fun useEventBus(): Boolean {
        return true
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onRefreshEvent(event: RefreshListEvent) {
        if(event?.isRefreshDepositList()) {
            mLoadMoreDelegate?.refresh();
        }
    }
}