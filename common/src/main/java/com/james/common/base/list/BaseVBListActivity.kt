package com.james.common.base.list

import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.SimpleItemAnimator
import androidx.viewbinding.ViewBinding
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.james.common.R
import com.james.common.base.BasePresenter
import com.james.common.base.BaseVBActivity
import com.james.common.base.loadmore.BaseLoadMoreView
import com.james.common.base.loadmore.RefreshLoadMoreFacade
import com.james.common.base.loadmore.core.DefaultLayoutFactory
import com.james.common.base.loadmore.core.IRefreshLoadMoreDelegate
import com.james.common.base.loadmore.core.IRequestCommand
import com.james.common.base.loadmore.core.LoadMoreConfig
import com.james.common.databinding.ActivityBaseListBinding
import com.james.common.databinding.FragmentBaseLoadMoreBinding
import com.scwang.smartrefresh.layout.SmartRefreshLayout

/**
Create Date : 2021/8/1112:01 上午
Auther      : Fox
Desc        :
 **/
 abstract class BaseVBListActivity<T, VB : ViewBinding, P : BasePresenter> : BaseVBActivity<VB, P>(),
    IRequestCommand, BaseLoadMoreView<T> {

    protected lateinit var mAdapter: BaseQuickAdapter<T, BaseViewHolder>
    protected lateinit var mLoadMoreRv: RecyclerView
    protected lateinit var mSmartRefreshLayout: SmartRefreshLayout
    protected lateinit var mLoadMoreLayout: FragmentBaseLoadMoreBinding
    /**
     * 分页加载数据的辅助类
     */
    protected var mLoadMoreDelegate: IRefreshLoadMoreDelegate? = null

    override fun initView() {
        initBaseViews()
        initRecyclerView()
        initOthers()
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
        mLoadMoreDelegate?.loadFinish(hasMore, !list.isNullOrEmpty())
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

    protected abstract fun initAdapter(): BaseQuickAdapter<T, BaseViewHolder>

    protected open fun autoRefresh(): Boolean {
        return true
    }

    protected open fun initOthers() {
        // do nothing
    }

    protected open fun initBaseViews() {
        if(useBaseLayout()) {
            mLoadMoreLayout = FragmentBaseLoadMoreBinding.bind(mRootBinding.root);
        } else {
            mLoadMoreLayout = FragmentBaseLoadMoreBinding.bind(mBinding.root);
        }
        mLoadMoreRv = mLoadMoreLayout.loadMoreRv;
        mSmartRefreshLayout = mLoadMoreLayout.smartRefreshLayout;
        mLoadMoreDelegate = RefreshLoadMoreFacade().apply {
            initConfig(initLoadMoreConfig())
        }
    }

    protected open fun initLoadMoreConfig(): LoadMoreConfig {
        return LoadMoreConfig().apply {
            tag = this@BaseVBListActivity::class.java.simpleName
            // 加载数据的接口
            requestCommand = this@BaseVBListActivity
            layoutFactory = DefaultLayoutFactory(mSmartRefreshLayout)
        }
    }

    protected open fun initLayoutManager(): RecyclerView.LayoutManager {
        return LinearLayoutManager(this)
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