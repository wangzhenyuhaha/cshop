package com.lingmiao.shop.business.goods

import android.content.Context
import android.content.Intent
import androidx.core.content.ContextCompat
import com.blankj.utilcode.util.ActivityUtils
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.james.common.base.BaseActivity
import com.james.common.base.delegate.DefaultPageLoadingDelegate
import com.james.common.base.delegate.PageLoadingDelegate
import com.james.common.base.loadmore.BaseLoadMoreActivity
import com.james.common.base.loadmore.core.IPage
import com.lingmiao.shop.R
import com.lingmiao.shop.business.goods.adapter.GoodsCheckedAdapter
import com.lingmiao.shop.business.goods.api.bean.GoodsVO
import com.lingmiao.shop.business.goods.event.GoodsHomeTabEvent
import com.lingmiao.shop.business.goods.event.RefreshGoodsStatusEvent
import com.lingmiao.shop.business.goods.fragment.GoodsNewFragment
import com.lingmiao.shop.business.goods.presenter.GoodsManagerPre
import com.lingmiao.shop.business.goods.presenter.impl.GoodsManagerPreImpl
import com.lingmiao.shop.widget.EmptyView
import kotlinx.android.synthetic.main.goods_activity_goods_manager.*
import kotlinx.android.synthetic.main.goods_activity_goods_manager.toolbarView
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode

/**
Create Date : 2021/3/69:58 AM
Auther      : Fox
Desc        :
 **/
class GoodsManagerActivity : BaseLoadMoreActivity<GoodsVO, GoodsManagerPre>(), GoodsManagerPre.View {

    companion object {

        const val KEY_GOODS_TYPE = "KEY_GOODS_TYPE"

        fun type(context: Context, type: Int?) {
            val intent = Intent(context, GoodsManagerActivity::class.java)
            intent.putExtra(KEY_GOODS_TYPE, type)
            context.startActivity(intent)
        }
    }


    private var cId : String =""

    override fun autoRefresh(): Boolean {
        return false
    }

    override fun useEventBus(): Boolean {
        return true
    }

    override fun useBaseLayout(): Boolean {
        return false
    }

    override fun getLayoutId(): Int {
        return R.layout.goods_activity_goods_manager;
    }

    override fun createPresenter(): GoodsManagerPre {
        return GoodsManagerPreImpl(this, this);
    }

    override fun initOthers() {
        toolbarView?.apply {
            setTitleContent(getString(R.string.goods_manager_title))
        }

        firstTypeTv.setOnClickListener {
            mPresenter?.showCategoryPop(it);
        }
        goodsManagerCb.setOnCheckedChangeListener { buttonView, isChecked ->
            mAdapter?.data.forEach {
                it?.isChecked = isChecked;
            }
            mAdapter?.notifyDataSetChanged();
            setCheckedCount(getCheckedCount());
        }
        goodsCheckSubmit.setOnClickListener {
            if(getCheckedCount() > 0) {
//                EventBus.getDefault().post(TabChangeEvent(2))
                EventBus.getDefault().post(GoodsHomeTabEvent(GoodsNewFragment.GOODS_STATUS_WAITING))
                ActivityUtils.finishToActivity(GoodsListActivity::class.java,false)
            }
        }

        // 禁用下拉刷新
        mSmartRefreshLayout.setEnableRefresh(false)
        mSmartRefreshLayout.setBackgroundColor(ContextCompat.getColor(this, R.color.color_ffffff))
        showNoData()
    }

    fun getCheckedCount() : Int {
        return mAdapter?.data?.filter { it?.isChecked == true }?.size;
    }

    fun setCheckedCount(count : Int) {
        goodsCheckedCountTv.text = String.format("已选择%s件商品", count);
    }

    override fun onUpdateCategory(categoryId: String?, categoryName: String?) {
        firstTypeTv.setText(categoryName);
        cId = categoryId!!;
        mLoadMoreDelegate?.refresh();
    }

    override fun initAdapter(): BaseQuickAdapter<GoodsVO, BaseViewHolder> {
        return GoodsCheckedAdapter().apply {
            setOnItemChildClickListener { adapter, view, position ->
                val bItem = adapter.data[position] as GoodsVO;
                if (view.id == R.id.menuIv) {
                    bItem?.isChecked = !(bItem?.isChecked!!);
                }
                setCheckedCount(getCheckedCount());
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
        mPresenter.loadListData(page, mAdapter.data, cId)
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onRefreshEvent(event: RefreshGoodsStatusEvent) {
        mLoadMoreDelegate?.refresh()
    }

}