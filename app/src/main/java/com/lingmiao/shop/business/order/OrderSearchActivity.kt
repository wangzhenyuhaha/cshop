package com.lingmiao.shop.business.order

import android.content.Context
import android.content.Intent
import android.view.MotionEvent
import androidx.core.content.ContextCompat
import com.blankj.utilcode.util.BarUtils
import com.blankj.utilcode.util.KeyboardUtils
import com.lingmiao.shop.R
import com.lingmiao.shop.business.goods.adapter.GoodsSearchAdapter
import com.lingmiao.shop.business.goods.adapter.GoodsStatusAdapter
import com.lingmiao.shop.business.goods.api.bean.GoodsVO
import com.lingmiao.shop.business.goods.event.RefreshGoodsStatusEvent
import com.lingmiao.shop.business.goods.presenter.GoodsSearchPre
import com.lingmiao.shop.business.goods.presenter.impl.GoodsSearchPreImpl
import com.lingmiao.shop.widget.EmptyView

import com.james.common.base.delegate.DefaultPageLoadingDelegate
import com.james.common.base.delegate.PageLoadingDelegate
import com.james.common.base.loadmore.BaseLoadMoreActivity
import com.james.common.base.loadmore.core.IPage
import com.james.common.utils.exts.getViewText
import com.lingmiao.shop.business.order.adapter.OrderListAdapter
import com.lingmiao.shop.business.order.bean.OrderList
import com.lingmiao.shop.business.order.presenter.OrderSearchPre
import com.lingmiao.shop.business.order.presenter.impl.OrderSearchPreImpl
import kotlinx.android.synthetic.main.order_activity_search.*
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode

/**
 * Author : Elson
 * Date   : 2020/7/11
 * Desc   :
 */
class OrderSearchActivity: BaseLoadMoreActivity<OrderList, OrderSearchPre>(),
    OrderSearchPre.StatusView {

    private var isGoodsName : Boolean = true

    companion object {
        fun openActivity(context: Context) {
            context.startActivity(Intent(context, OrderSearchActivity::class.java))
        }
    }

    override fun useLightMode(): Boolean {
        return false;
    }

    override fun getLayoutId(): Int {
        return R.layout.order_activity_search
    }

    override fun createPresenter(): OrderSearchPre {
        return OrderSearchPreImpl(this, this)
    }

    override fun autoRefresh(): Boolean {
        return false
    }

    override fun useEventBus(): Boolean {
        return true
    }

    override fun initOthers() {
        mToolBarDelegate.setMidTitle("订单搜索")

//        BarUtils.setStatusBarLightMode(this, true)
        // 禁用下拉刷新
        mSmartRefreshLayout.setEnableRefresh(false)
        mSmartRefreshLayout.setEnableLoadMore(false)
        mSmartRefreshLayout.setBackgroundColor(ContextCompat.getColor(this, R.color.color_ffffff))
//        showNoData()

        deleteIv.setOnClickListener {
            inputEdt.setText("")
        }
        searchTv.setOnClickListener {
            mLoadMoreDelegate?.refresh()
        }
    }

    override fun initAdapter(): OrderListAdapter {
        return OrderListAdapter().apply {
            setOnItemChildClickListener { adapter, view, position ->

            }
            setOnItemClickListener { adapter, view, position ->

            }
            emptyView = EmptyView(context).apply {
                setBackgroundResource(R.color.common_bg)
            }
        }
    }

    override fun executePageRequest(page: IPage) {
        mPresenter.loadListData(page, mAdapter.data, inputEdt.getViewText(), isGoodsName)
    }

    override fun getPageLoadingDelegate(): PageLoadingDelegate {
        if (mPageLoadingDelegate == null) {
            mPageLoadingDelegate = DefaultPageLoadingDelegate(this).apply {
                // 设置当前页面的 EmptyLayout
                setPageLoadingLayout(elEmpty)
            }
        }
        return mPageLoadingDelegate
    }

    override fun onLoadMoreSuccess(list: List<OrderList>?, hasMore: Boolean) {
        super.onLoadMoreSuccess(list, hasMore)
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onRefreshEvent(event: RefreshGoodsStatusEvent) {
        mLoadMoreDelegate?.refresh()
    }

    override fun dispatchTouchEvent(ev: MotionEvent?): Boolean {
        if (ev?.action == MotionEvent.ACTION_DOWN) {
            // 当键盘未关闭时先拦截事件
            if(KeyboardUtils.isSoftInputVisible(context)) {
                KeyboardUtils.hideSoftInput(context);
            }
        }
        return super.dispatchTouchEvent(ev)
    }

}