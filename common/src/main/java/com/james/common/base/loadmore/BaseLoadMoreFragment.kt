package com.james.common.base.loadmore

import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.SimpleItemAnimator
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.james.common.R
import com.james.common.base.BaseFragment
import com.james.common.base.BasePresenter
import com.james.common.base.loadmore.core.*
import com.scwang.smartrefresh.layout.SmartRefreshLayout

/**
 * Author : Elson
 * Date   : 2020/7/11
 * Desc   : 分页加载
 */
abstract class BaseLoadMoreFragment<T, P : BasePresenter> :
    BaseFragment<P>(), IRequestCommand, BaseLoadMoreView<T> {

    protected lateinit var mAdapter: BaseQuickAdapter<T, BaseViewHolder>
    protected lateinit var mLoadMoreRv: RecyclerView
    protected lateinit var mSmartRefreshLayout: SmartRefreshLayout
    /**
     * 分页加载数据的辅助类
     */
    protected var mLoadMoreDelegate: IRefreshLoadMoreDelegate? = null

    override fun getLayoutId(): Int? {
        return R.layout.fragment_base_load_more
    }

    override fun initViewsAndData(rootView: View) {
        initBaseViews(rootView)
        initRecyclerView()
        initOthers(rootView)
        if (autoRefresh()) {
            mLoadMoreDelegate?.refresh()
        }
    }

    override fun onLoadMoreSuccess(list: List<T>?, hasMore: Boolean) {
        if (mLoadMoreDelegate?.getPage()?.isRefreshing() == true) {
            mAdapter.replaceData(list ?: arrayListOf())
        } else {
            mAdapter.addData(list ?: arrayListOf())
        }
        mLoadMoreDelegate?.loadFinish(!list.isNullOrEmpty(), !list.isNullOrEmpty())
    }

    override fun onLoadMoreFailed() {
        onLoadMoreFailed(true, false)
    }

    override fun onLoadMoreFailed(hasMore: Boolean, success: Boolean) {
        mLoadMoreDelegate?.loadFinish(true, false)
    }

    private fun initRecyclerView() {
        mAdapter = initAdapter()
        mLoadMoreRv.apply {
            layoutManager = initLayoutManager()
            adapter = mAdapter
        }
        disableRecyclerViewAnim()
    }


    // ------------------- 可被重写的方法 Method ---------------------

    protected open fun initOthers(rootView: View) {
        // do nothing
    }

    protected abstract fun initAdapter(): BaseQuickAdapter<T, BaseViewHolder>

    protected open fun autoRefresh(): Boolean {
        return true
    }

    protected open fun initBaseViews(rootView: View) {
        mLoadMoreRv = rootView.findViewById(R.id.loadMoreRv)
        mSmartRefreshLayout = rootView.findViewById(R.id.smartRefreshLayout)

        mLoadMoreDelegate = RefreshLoadMoreFacade().apply {
            initConfig(initLoadMoreConfig())
        }
    }

    protected open fun initLoadMoreConfig(): LoadMoreConfig {
        return LoadMoreConfig().apply {
            tag = this@BaseLoadMoreFragment::class.java.simpleName
            // 加载数据的接口
            requestCommand = this@BaseLoadMoreFragment
            layoutFactory = DefaultLayoutFactory(mSmartRefreshLayout)
        }
    }

    protected open fun initLayoutManager(): RecyclerView.LayoutManager {
        return LinearLayoutManager(mContext)
    }

    /**
     * 禁用动画
     */
    protected open fun disableRecyclerViewAnim() {
        mLoadMoreRv.itemAnimator?.apply {
            (this as? SimpleItemAnimator)?.supportsChangeAnimations = false
        }
    }

}