package com.lingmiao.shop.business.tuan

import androidx.core.content.ContextCompat
import com.blankj.utilcode.util.BarUtils
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.lingmiao.shop.R
import com.lingmiao.shop.business.goods.api.bean.GoodsVO
import com.lingmiao.shop.business.tuan.adapter.GoodsSelectAdapter
import com.lingmiao.shop.business.tuan.bean.GoodsVo
import com.lingmiao.shop.business.tuan.presenter.GoodsSelectPresenter
import com.lingmiao.shop.business.tuan.presenter.impl.GoodsSelectPresenterImpl
import com.james.common.base.BaseActivity
import com.james.common.base.delegate.DefaultPageLoadingDelegate
import com.james.common.base.delegate.PageLoadingDelegate
import com.james.common.base.loadmore.BaseLoadMoreActivity
import com.james.common.base.loadmore.core.IPage
import com.james.common.utils.exts.getViewText
import com.james.common.utils.exts.singleClick
import kotlinx.android.synthetic.main.goods_activity_search.*
import kotlinx.android.synthetic.main.tuan_activity_goods_select.*

/**
 *  选择商品
 */
class GoodsSelectActivity : BaseLoadMoreActivity<GoodsVO, GoodsSelectPresenter>(), GoodsSelectPresenter.View{

    override fun getLayoutId(): Int {
        return R.layout.tuan_activity_goods_select;
    }

    override fun useBaseLayout(): Boolean {
        return false
    }

//    override fun autoRefresh(): Boolean {
//        return false
//    }

    override fun createPresenter(): GoodsSelectPresenter {
        return GoodsSelectPresenterImpl(this);
    }

    override fun executePageRequest(page: IPage) {
        mPresenter.loadListData(page, mAdapter.data, etSearch.getViewText())
    }

    @Override
    override fun initOthers() {
        BarUtils.setStatusBarLightMode(this, true)
        // 禁用下拉刷新
        mSmartRefreshLayout.setEnableRefresh(false)
        mSmartRefreshLayout.setBackgroundColor(ContextCompat.getColor(this, R.color.color_ffffff))
        showNoData()

        ivBack.setOnClickListener {
            finish()
        }
        ivDelete.setOnClickListener {
            etSearch.setText("")
        }
        tvSearch.setOnClickListener {
            mLoadMoreDelegate?.refresh()
        }
        tv_goods_select.singleClick {
            finish();
        }
    }

    override fun initAdapter(): BaseQuickAdapter<GoodsVO, BaseViewHolder> {
        return GoodsSelectAdapter().apply {

        }
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

}