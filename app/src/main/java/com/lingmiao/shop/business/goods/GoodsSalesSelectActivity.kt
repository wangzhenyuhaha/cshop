package com.lingmiao.shop.business.goods

import android.content.Context
import android.content.Intent
import android.view.Gravity
import android.view.View
import androidx.core.content.ContextCompat
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.james.common.base.delegate.DefaultPageLoadingDelegate
import com.james.common.base.delegate.PageLoadingDelegate
import com.james.common.base.loadmore.BaseLoadMoreActivity
import com.james.common.base.loadmore.core.IPage
import com.lingmiao.shop.R
import com.lingmiao.shop.business.goods.adapter.GoodsSalesAdapter
import com.lingmiao.shop.business.goods.api.bean.GoodsVO
import com.lingmiao.shop.business.goods.presenter.GoodsSalesSelectPre
import com.lingmiao.shop.business.goods.presenter.impl.GoodsSalesSelectPreImpl
import com.lingmiao.shop.widget.EmptyView
import kotlinx.android.synthetic.main.goods_activity_goods_sales_select.*

/**
Create Date : 2021/4/145:19 PM
Auther      : Fox
Desc        :
 **/
class GoodsSalesSelectActivity : BaseLoadMoreActivity<GoodsVO, GoodsSalesSelectPre>(), GoodsSalesSelectPre.StatusView {

    companion object {

        const val KEY_ID = "KEY_ID"
        const val KEY_SOURCE = "KEY_SOURCE"
        const val SOURCE_TYPE = 1;
        const val SOURCE_MENU = 2;
        const val SOURCE_SALES = 3;
        fun type(context: Context, keyId: String?) {
            val intent = Intent(context, GoodsSalesSelectActivity::class.java)
            intent.putExtra(KEY_SOURCE, SOURCE_TYPE)
            intent.putExtra(KEY_ID, keyId)
            context.startActivity(intent)
        }

        fun menu(context: Context, keyId: String?) {
            val intent = Intent(context, GoodsSalesSelectActivity::class.java)
            intent.putExtra(KEY_SOURCE, SOURCE_MENU)
            intent.putExtra(KEY_ID, keyId)
            context.startActivity(intent)
        }


        fun sales(context: Context, keyId: String?) {
            val intent = Intent(context, GoodsSalesSelectActivity::class.java)
            intent.putExtra(KEY_SOURCE, SOURCE_SALES)
            intent.putExtra(KEY_ID, keyId)
            context.startActivity(intent)
        }
    }


    private var cId: String = ""
    private var mSourceId: Int? = 0;


    override fun initBundles() {
        cId = intent.getStringExtra(KEY_ID).toString()
        mSourceId = intent.getIntExtra(KEY_SOURCE, SOURCE_TYPE);
    }

    override fun autoRefresh(): Boolean {
        return false
    }

    override fun useLightMode(): Boolean {
        return false;
    }

    override fun getLayoutId(): Int {
        return R.layout.goods_activity_goods_sales_select;
    }

    override fun createPresenter(): GoodsSalesSelectPre {
        return GoodsSalesSelectPreImpl(this, this);
    }

    override fun initOthers() {
        mToolBarDelegate.setMidTitle("选择商品")
        mToolBarDelegate.setRightTwoIcon(R.mipmap.ic_filter_white, View.OnClickListener(){
            salesDrawerLayout.openDrawer(Gravity.RIGHT)
        }, R.mipmap.ic_search, View.OnClickListener(){
            GoodsSearchActivity.openActivity(context!!)
        });
//        firstTypeTv.setOnClickListener {
//            mPresenter?.showCategoryPop(it);
//        }
        goodsSalesCb.setOnCheckedChangeListener { buttonView, isChecked ->
            mAdapter?.data.forEach {
                it?.isChecked = isChecked;
            }
            mAdapter?.notifyDataSetChanged();
            // setCheckedCount(getCheckedCount());
        }
        goodsSalesSubmit.setOnClickListener {
            finish();
//            if (getCheckedCount() > 0) {
////                EventBus.getDefault().post(TabChangeEvent(2))
//                if (mSourceId == SOURCE_TYPE) {
//                    EventBus.getDefault()
//                        .post(GoodsHomeTabEvent(GoodsNewFragment.GOODS_STATUS_WAITING))
//                    ActivityUtils.finishToActivity(GoodsListActivity::class.java, false)
//                } else {
//                    EventBus.getDefault().post(MenuEvent(1, 1));
//                    finish();
//                }
//            }
        }

        // 禁用下拉刷新
        mSmartRefreshLayout.setEnableRefresh(false)
        mSmartRefreshLayout.setEnableLoadMore(false)
        mSmartRefreshLayout.setBackgroundColor(ContextCompat.getColor(this, R.color.color_ffffff))

        mLoadMoreDelegate?.refresh()
         // showNoData()
    }

    fun getCheckedCount(): Int {
        return mAdapter?.data?.filter { it?.isChecked == true }?.size;
    }

    override fun initAdapter(): BaseQuickAdapter<GoodsVO, BaseViewHolder> {
        return GoodsSalesAdapter().apply {
            setOnItemChildClickListener { adapter, view, position ->
                val bItem = adapter.data[position] as GoodsVO;
                if (view.id == R.id.menuIv) {
                    bItem?.isChecked = !(bItem?.isChecked!!);
                }
                // setCheckedCount(getCheckedCount());
            }
            setOnItemClickListener { adapter, view, position ->
//                mPresenter?.clickItemView(mAdapter.getItem(position), position)
            }
            emptyView = EmptyView(context).apply {
                setBackgroundResource(R.color.common_bg)
            }
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

    /**
     * 执行分页请求
     */
    override fun executePageRequest(page: IPage) {
        mPresenter.loadListData(page, mAdapter.data)
    }

    override fun onLoadMoreSuccess(list: List<GoodsVO>?, hasMore: Boolean) {
        super.onLoadMoreSuccess(list, hasMore)

    }

}