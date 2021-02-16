package com.lingmiao.shop.business.goods

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
import kotlinx.android.synthetic.main.goods_activity_search.*
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode

/**
 * Author : Elson
 * Date   : 2020/7/11
 * Desc   :
 */
class GoodsSearchActivity: BaseLoadMoreActivity<GoodsVO, GoodsSearchPre>(),
    GoodsSearchPre.StatusView {

    private var isGoodsName : Boolean = true

    companion object {
        fun openActivity(context: Context) {
            context.startActivity(Intent(context, GoodsSearchActivity::class.java))
        }
    }

    override fun useBaseLayout(): Boolean {
        return false
    }

    override fun getLayoutId(): Int {
        return R.layout.goods_activity_search
    }

    override fun createPresenter(): GoodsSearchPre {
        return GoodsSearchPreImpl(this, this)
    }

    override fun autoRefresh(): Boolean {
        return false
    }

    override fun useEventBus(): Boolean {
        return true
    }

    override fun initOthers() {
        BarUtils.setStatusBarLightMode(this, true)
        // 禁用下拉刷新
        mSmartRefreshLayout.setEnableRefresh(false)
        mSmartRefreshLayout.setBackgroundColor(ContextCompat.getColor(this, R.color.color_ffffff))
        showNoData()

        backIv.setOnClickListener {
            finish()
        }
        deleteIv.setOnClickListener {
            inputEdt.setText("")
        }
        searchTv.setOnClickListener {
            mLoadMoreDelegate?.refresh()
        }
        searchStatusTv.setOnClickListener {
            mPresenter?.clickSearchMenuView(it)
        }
        setSearchStatus()
    }

    fun setSearchStatus() {
        searchStatusTv.setText(if(isGoodsName) "商品名称" else "供应商");
    }

    override fun initAdapter(): GoodsStatusAdapter {
        return GoodsSearchAdapter().apply {
            setOnItemChildClickListener { adapter, view, position ->
                if (view.id == R.id.menuIv) {
                    mPresenter?.clickMenuView(mAdapter.getItem(position), position, view)
                }
            }
            setOnItemClickListener { adapter, view, position ->
                mPresenter?.clickItemView(mAdapter.getItem(position), position)
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

    override fun onLoadMoreSuccess(list: List<GoodsVO>?, hasMore: Boolean) {
        super.onLoadMoreSuccess(list, hasMore)
    }

    override fun onGoodsEnable(goodsId: String?, position: Int) {
        mAdapter.remove(position)
    }

    override fun onGoodsDisable(goodsId: String?, position: Int) {
        mAdapter.remove(position)
    }

    override fun onGoodsQuantity(quantity: String?, position: Int) {
        (mAdapter as GoodsStatusAdapter).updateQuantity(quantity, position)
    }

    override fun onGoodsDelete(goodsId: String?, position: Int) {
        mAdapter.remove(position)
    }

    override fun onShiftGoodsOwner() {
        isGoodsName = false;
        setSearchStatus()
    }

    override fun onShiftGoodsName() {
        isGoodsName = true;
        setSearchStatus()
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