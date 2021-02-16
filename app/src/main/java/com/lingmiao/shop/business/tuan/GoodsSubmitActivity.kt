package com.lingmiao.shop.business.tuan

import androidx.core.content.ContextCompat
import com.blankj.utilcode.util.ActivityUtils
import com.blankj.utilcode.util.BarUtils
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.lingmiao.shop.R
import com.lingmiao.shop.business.goods.GroupManagerEditActivity
import com.lingmiao.shop.business.goods.api.bean.ShopGroupVO
import com.lingmiao.shop.business.tools.EditFreightModelActivity
import com.lingmiao.shop.business.tuan.adapter.GoodsAdapter
import com.lingmiao.shop.business.tuan.bean.GoodsVo
import com.lingmiao.shop.business.tuan.presenter.GoodsSubmitPresenter
import com.lingmiao.shop.business.tuan.presenter.impl.GoodsSubmitPresenterImpl
import com.james.common.base.BaseActivity
import com.james.common.base.delegate.DefaultPageLoadingDelegate
import com.james.common.base.delegate.PageLoadingDelegate
import com.james.common.base.loadmore.BaseLoadMoreActivity
import com.james.common.base.loadmore.core.IPage
import com.james.common.utils.exts.getViewText
import com.james.common.utils.exts.singleClick
import kotlinx.android.synthetic.main.goods_activity_group_manager.*
import kotlinx.android.synthetic.main.goods_activity_group_manager.toolbarView
import kotlinx.android.synthetic.main.goods_activity_search.*
import kotlinx.android.synthetic.main.tools_activity_logistics_tool.*
import kotlinx.android.synthetic.main.tuan_activity_goods_submit.*

/**
 *  提报商品
 */
class GoodsSubmitActivity : BaseLoadMoreActivity<GoodsVo, GoodsSubmitPresenter>(), GoodsSubmitPresenter.View{

    override fun getLayoutId(): Int {
        return R.layout.tuan_activity_goods_submit;
    }

    override fun useBaseLayout(): Boolean {
        return false
    }

    override fun autoRefresh(): Boolean {
        return true;
    }

    override fun createPresenter(): GoodsSubmitPresenter {
        return GoodsSubmitPresenterImpl(this);
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

    override fun initView() {
        toolbarView?.apply {
            var drawable = getDrawable(R.mipmap.goods_plus_blue)
            drawable?.setBounds(0, 0, 30, 30);

            setTitleContent(getString(R.string.tuan_title_goods_submit))
            setRightListener(drawable, getString(R.string.tuan_hint_goods_select), R.color.color_3870EA) {
                ActivityUtils.startActivity(GoodsSelectActivity::class.java)
            }
        }
        super.initView()
    }

    override fun initOthers() {
        BarUtils.setStatusBarLightMode(this, true)
        // 禁用下拉刷新
        mSmartRefreshLayout.setEnableRefresh(false)
        mSmartRefreshLayout.setEnableLoadMore(false)

        tv_goods_submit.singleClick {

        };
    }

    override fun initAdapter(): BaseQuickAdapter<GoodsVo, BaseViewHolder> {
        return GoodsAdapter().apply {
            setOnItemChildClickListener { adapter, view, position ->
                if (view.id == R.id.tv_goods_delete) {
                    mAdapter.remove(position);
                }
            }
        }
    }

    override fun executePageRequest(page: IPage) {
//        mPresenter.loadListData(page, mAdapter.data, inputEdt.getViewText())
        var goods = arrayListOf<GoodsVo>();
        goods.add(GoodsVo())
        goods.add(GoodsVo())
        onLoadMoreSuccess(goods, true);
    }

}