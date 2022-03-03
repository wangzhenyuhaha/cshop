package com.lingmiao.shop.business.wallet.fragment

import android.view.View
import com.blankj.utilcode.util.SPUtils
import com.james.common.base.loadmore.BaseLoadMoreFragment
import com.james.common.base.loadmore.core.IPage
import com.lingmiao.shop.R
import com.lingmiao.shop.business.wallet.adapter.DepositAdapter
import com.lingmiao.shop.business.wallet.api.WalletConstants
import com.lingmiao.shop.business.wallet.bean.DepositVo
import com.lingmiao.shop.business.wallet.event.RefreshListEvent
import com.lingmiao.shop.business.wallet.presenter.RiderListPresenter
import com.lingmiao.shop.business.wallet.presenter.impl.RiderListPresenterImpl
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

    override fun initAdapter(): DepositAdapter {
        return DepositAdapter().apply {
            emptyView = EmptyView(mContext).apply {
                setBackgroundResource(R.color.common_bg)
            }
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