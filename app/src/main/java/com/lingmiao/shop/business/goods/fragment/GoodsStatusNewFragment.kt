package com.lingmiao.shop.business.goods.fragment

import android.app.Activity
import android.os.Bundle
import android.view.View
import com.chad.library.adapter.base.BaseQuickAdapter
import com.lingmiao.shop.R
import com.lingmiao.shop.business.goods.adapter.GoodsStatusAdapter
import com.lingmiao.shop.business.goods.api.bean.GoodsVO
import com.lingmiao.shop.business.goods.event.BatchStatusEvent
import com.lingmiao.shop.business.goods.event.RefreshGoodsStatusEvent
import com.lingmiao.shop.business.goods.presenter.GoodsStatusPre
import com.lingmiao.shop.business.goods.presenter.impl.GoodsAuthWaitingPreImpl
import com.lingmiao.shop.business.goods.presenter.impl.GoodsMarketDisablePreImpl
import com.lingmiao.shop.business.goods.presenter.impl.GoodsMarketEnablePreImpl
import com.lingmiao.shop.business.photo.PhotoHelper
import com.lingmiao.shop.widget.EmptyView
import com.james.common.base.loadmore.BaseLoadMoreFragment
import com.james.common.base.loadmore.core.IPage
import kotlinx.android.synthetic.main.goods_fragment_goods_list.*
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode

/**
 * Author : Elson
 * Date   : 2020/7/11
 * Desc   : 商品(出售中、待审核、已下架)列表
 */
class GoodsStatusNewFragment : BaseLoadMoreFragment<GoodsVO, GoodsStatusPre>(),
    GoodsStatusPre.StatusView {

    companion object {
        const val KEY_GOODS_STATUS = "KEY_GOODS_STATUS"

        fun newInstance(goodsStatus: Int): GoodsStatusNewFragment {
            return GoodsStatusNewFragment().apply {
                arguments = Bundle().apply {
                    putInt(KEY_GOODS_STATUS, goodsStatus)
                }
            }
        }
    }

    private var goodsStatus: Int? = null

    override fun initBundles() {
        goodsStatus = arguments?.getInt(KEY_GOODS_STATUS)
    }

    override fun getLayoutId(): Int? {
        return R.layout.goods_fragment_goods_list;
    }

    override fun useEventBus(): Boolean {
        return true
    }

    override fun initOthers(rootView: View) {
        mSmartRefreshLayout.setBackgroundResource(R.color.color_ffffff)

        cb_goods_list_check_all.setOnCheckedChangeListener { buttonView, isChecked ->
            mAdapter?.data?.forEachIndexed { index, goodsVO ->
                goodsVO.isChecked = isChecked;
            }
            mAdapter?.notifyDataSetChanged();
        };
        tv_goods_off.setOnClickListener {
            mPresenter?.clickOffLine(mAdapter?.data) {
                offLineSuccess();
            }
        }
        tv_goods_on.setOnClickListener {
            mPresenter?.clickOnLine(mAdapter?.data) {
                onLineSuccess();
            }
        }
        tv_goods_rebate.setOnClickListener {
            mPresenter?.clickOnBatchRebate(mAdapter?.data) {
                onBatchRebateSuccess();
            }
        }
        tv_goods_cancel_batch.setOnClickListener{
            rl_goods_check.visibility = View.GONE;
            cb_goods_list_check_all.isChecked = false;
            var list = mAdapter?.data?.filter { it?.isChecked == true };
            if(list?.size > 0) {
                list?.forEachIndexed { index, goodsVO ->
                    goodsVO.isChecked = false;
                }
            }
            (mAdapter as GoodsStatusAdapter)?.setBatchEditModel(false);
        }

        when (goodsStatus) {
            GoodsFragment.GOODS_STATUS_ENABLE -> {
                tv_goods_off.visibility = View.VISIBLE;
            }
            GoodsFragment.GOODS_STATUS_IS_AUTH -> {
                tv_goods_off.visibility = View.VISIBLE;
            }
            GoodsFragment.GOODS_STATUS_DISABLE -> {
                tv_goods_on.visibility = View.VISIBLE;
            }
            else -> {

            }
        }
    }

    override fun initAdapter(): GoodsStatusAdapter {
        return GoodsStatusAdapter(goodsStatus!!).apply {
            setOnItemChildClickListener { adapter, view, position ->
                var item = mAdapter.getItem(position) as GoodsVO;
                if (view.id == R.id.menuIv) {
                    mPresenter?.clickMenuView(mAdapter.getItem(position), position, view)
                } else if(view.id == R.id.goodsIv) {
                    if(item?.goodsGalleryList?.size ?:0 > 0) {
                        PhotoHelper.previewAlbum(context as Activity, 0, item?.goodsGalleryList);
                    } else {
                        PhotoHelper.previewImage(context as Activity, item?.thumbnail);
                    }
                }
            }
            setOnItemClickListener { adapter, view, position ->
                shiftChecked(position);
//                mPresenter?.clickItemView(mAdapter.getItem(position), position)
            }
            onItemLongClickListener = BaseQuickAdapter.OnItemLongClickListener { adapter, view, position -> Boolean
                if(rl_goods_check.visibility != View.VISIBLE) {
                    rl_goods_check.visibility = View.VISIBLE;
                }
                setBatchEditModel(true);
                return@OnItemLongClickListener true;
            }
            emptyView = EmptyView(mContext).apply {
                setBackgroundResource(R.color.color_ffffff)
            }
        }
    }

    override fun createPresenter(): GoodsStatusPre? {
        return when (goodsStatus) {
            GoodsFragment.GOODS_STATUS_ENABLE -> GoodsMarketEnablePreImpl(mContext, this)
            GoodsFragment.GOODS_STATUS_IS_AUTH -> GoodsAuthWaitingPreImpl(mContext, this)
            GoodsFragment.GOODS_STATUS_DISABLE -> GoodsMarketDisablePreImpl(mContext, this)
            else -> null
        }
    }

    override fun onLoadMoreSuccess(list: List<GoodsVO>?, hasMore: Boolean) {
        super.onLoadMoreSuccess(list, hasMore)
        list?.forEachIndexed { index, goodsVO ->
            goodsVO?.isChecked = cb_goods_list_check_all.isChecked;
        }
    }

    override fun executePageRequest(page: IPage) {
        mPresenter?.loadListData(page, mAdapter.data)
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

    override fun onLineSuccess() {
        EventBus.getDefault().post(BatchStatusEvent(goodsStatus!!))
        EventBus.getDefault().post(RefreshGoodsStatusEvent(goodsStatus!!))
    }

    override fun offLineSuccess() {
        EventBus.getDefault().post(BatchStatusEvent(goodsStatus!!))
        EventBus.getDefault().post(RefreshGoodsStatusEvent(goodsStatus!!))
    }

    override fun onBatchRebateSuccess() {
        EventBus.getDefault().post(BatchStatusEvent(goodsStatus!!))
        EventBus.getDefault().post(RefreshGoodsStatusEvent(goodsStatus!!))
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onRefreshEvent(event: RefreshGoodsStatusEvent) {
        if (event.isRefresh(goodsStatus)) {
            mLoadMoreDelegate?.refresh()
        }
    }

    @Subscribe(threadMode =  ThreadMode.MAIN)
    fun onBatchSuccess(event : BatchStatusEvent) {
        if(event?.isRefresh(goodsStatus)) {
            cb_goods_list_check_all.isChecked = false;
        }
    }

}