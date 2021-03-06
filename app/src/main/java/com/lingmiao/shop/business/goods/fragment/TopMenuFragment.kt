package com.lingmiao.shop.business.goods.fragment

import android.app.Activity
import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.blankj.utilcode.util.LogUtils
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.chad.library.adapter.base.callback.ItemDragAndSwipeCallback
import com.chad.library.adapter.base.listener.OnItemDragListener
import com.james.common.base.loadmore.BaseLoadMoreFragment
import com.james.common.base.loadmore.core.IPage
import com.james.common.utils.DialogUtils
import com.james.common.utils.exts.gone
import com.james.common.utils.exts.singleClick
import com.james.common.utils.exts.visiable
import com.lingmiao.shop.R
import com.lingmiao.shop.business.goods.GoodsOfMenuActivity
import com.lingmiao.shop.business.goods.MenuEditActivity
import com.lingmiao.shop.business.goods.adapter.MenuAdapter
import com.lingmiao.shop.business.goods.api.bean.ShopGroupVO
import com.lingmiao.shop.business.goods.event.GroupRefreshEvent
import com.lingmiao.shop.business.goods.presenter.CateManagerPre
import com.lingmiao.shop.business.goods.presenter.impl.CateManagerPreImpl
import com.lingmiao.shop.widget.EmptyView
import kotlinx.android.synthetic.main.goods_fragment_goods_top_menu.*
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode

/**
Create Date : 2021/3/101:00 AM
Auther      : Fox
Desc        :
 **/
class TopMenuFragment : BaseLoadMoreFragment<ShopGroupVO, CateManagerPre>(),
    CateManagerPre.GroupManagerView {

    companion object {
        fun newInstance(isTop: Int): TopMenuFragment {
            return TopMenuFragment().apply {
                arguments = Bundle().apply {
                    putInt("isTop", isTop)
                }
            }
        }
    }

    private var isTop: Int? = null

    override fun initBundles() {
        isTop = arguments?.getInt("isTop", 0)
    }

    override fun getLayoutId() = R.layout.goods_fragment_goods_top_menu

    var mSelectPosition: Int? = null

    override fun initAdapter(): BaseQuickAdapter<ShopGroupVO, BaseViewHolder> {
        val adapter = MenuAdapter().apply {
            setOnItemChildClickListener { adapter, view, position ->
                val item = adapter.getItem(position) as ShopGroupVO
                when (view.id) {
                    R.id.menuTopTv -> {
                        handleSort(position, item)
                    }
                    R.id.menuEditTv -> {
                        MenuEditActivity.openActivity(
                            requireActivity(),
                            ShopGroupVO.LEVEL_1,
                            mAdapter.getItem(position)?.shopCatPid,
                            mAdapter.getItem(position)
                        )
                    }
                    R.id.menuEditGoodsTv -> {
                        GoodsOfMenuActivity.openActivity(requireActivity(), item)
                    }
                    R.id.menuVisibleCb -> {
                        item.disable = if (item.disable == 1) 0 else 1
                        mPresenter?.updateGroup(item, position)
                    }
                    R.id.menuDeleteTv -> {
                        DialogUtils.showDialog(context as Activity,
                            "????????????", "??????????????????????????????????????????????????????",
                            "??????", "????????????",
                            null, View.OnClickListener {
                                mPresenter?.deleteGoodsGroup(mAdapter.getItem(position), position)
                            })
                    }
                }
            }
            emptyView = EmptyView(mContext).apply {
                setBackgroundResource(R.color.common_bg)
            }
        }

        var mStartPoi: Int = -1

        val listener: OnItemDragListener = object : OnItemDragListener {
            override fun onItemDragStart(viewHolder: RecyclerView.ViewHolder, pos: Int) {
                LogUtils.d(" ..start : $pos")
                mStartPoi = pos
                val holder = viewHolder as BaseViewHolder
                //                holder.setTextColor(R.id.tv, Color.WHITE);
            }

            override fun onItemDragMoving(
                source: RecyclerView.ViewHolder,
                from: Int,
                target: RecyclerView.ViewHolder,
                to: Int
            ) {
                LogUtils.d(" ..move : $from $to")
            }

            override fun onItemDragEnd(viewHolder: RecyclerView.ViewHolder, pos: Int) {
                LogUtils.d(" ..end : $pos")
                if (pos > 0) {
                    mSelectPosition = pos

                    if (mStartPoi > pos) {
                        // ??????
                        mAdapter.notifyItemRangeChanged(pos, mStartPoi - pos + 1)
                    } else {
                        // ??????
                        mAdapter.notifyItemRangeChanged(mStartPoi, pos - mStartPoi + 1)

                    }

                    val pre = adapter.data.get(pos - 1)
                    val current = adapter.data.get(pos)
                    mPresenter?.sort(isTop!!, current.shopCatId!!, pre.sort + 1)
                } else {
                    // ?????????
                    val item = adapter.data.get(pos)
                    mPresenter?.sort(isTop!!, item.shopCatId!!, 0)
                    handleSort(0, item)
                }

                //val holder = viewHolder as BaseViewHolder
            }
        }

        val mItemDragAndSwipeCallback = ItemDragAndSwipeCallback(adapter)

        val mItemTouchHelper: ItemTouchHelper? = ItemTouchHelper(mItemDragAndSwipeCallback)
        mItemTouchHelper!!.attachToRecyclerView(mLoadMoreRv)

        adapter.setOnItemDragListener(listener)
        adapter.enableDragItem(mItemTouchHelper)
        return adapter
    }

    fun handleSort(position: Int, toPosition: Int, item: ShopGroupVO, sortValue: Int) {
        mSelectPosition = position
        // ??????
        mAdapter.remove(position)
        // ????????????
        mAdapter.addData(toPosition, item)
        // ???????????????
        mAdapter.notifyItemChanged(toPosition + 1)
        // ????????????
        mLoadMoreRv.smoothScrollToPosition(toPosition)

        mPresenter?.sort(isTop!!, item.shopCatId!!, sortValue)
    }

    fun handleSort(position: Int, item: ShopGroupVO) {
        handleSort(position, 0, item, 0)
    }

    override fun initOthers(rootView: View) {
        menuAddLayout.setOnClickListener {
            MenuEditActivity.openActivity(requireActivity(), ShopGroupVO.LEVEL_1, null, null)
        }

        menuAllCheckCb.setOnCheckedChangeListener { buttonView, isChecked ->
            mAdapter?.data?.forEachIndexed { index, goodsVO ->
                goodsVO.isChecked = isChecked
            }
            mAdapter?.notifyDataSetChanged()
        }

        // ????????????
        menuCancelTv.setOnClickListener {
            (mAdapter as MenuAdapter)?.setBatchEditModel(false)
            menuCancelTv.gone()
            menuDeleteTv.gone()
            menuBottom.gone()
            addMenuLayout.visiable()
        }
        // ????????????
        menuDeleteTv.setOnClickListener {
            (mAdapter as MenuAdapter)?.setBatchEditModel(false)
            menuDeleteTv.gone()
            menuBottom.gone()
            addMenuLayout.visiable()
        }
        // ??????
        menuSortTv.singleClick {
            addMenuLayout.gone()
            menuBottom.visiable()
            menuDeleteTv.visiable()
            (mAdapter as MenuAdapter)?.setBatchEditModel(true);
        }

        mSmartRefreshLayout?.setEnableRefresh(false);
        mSmartRefreshLayout?.setEnableLoadMore(false);
    }

    fun isBatchModel(): Boolean {
        return (mAdapter as MenuAdapter)?.getBatchEdit()
    }

    fun setFinishSort() {
        (mAdapter as MenuAdapter)?.setBatchEditModel(false)
        menuDeleteTv.gone()
        menuBottom.gone()
        addMenuLayout.visiable()
    }

    override fun createPresenter(): CateManagerPre? {
        return CateManagerPreImpl(this)
    }

    override fun onDeleteGroupSuccess(position: Int) {
        if (position < mAdapter.data.size) {
            mAdapter.data.removeAt(position)
            mAdapter.notifyDataSetChanged()
        }
    }

    override fun onGroupUpdated(position: Int) {
        mAdapter?.notifyItemChanged(position)
    }

    override fun onSortSuccess() {

    }

    override fun useEventBus(): Boolean {
        return true
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun handleRefreshEvent(event: GroupRefreshEvent) {
        mLoadMoreDelegate?.refresh()
    }

    override fun onLoadMoreSuccess(list: List<ShopGroupVO>?, hasMore: Boolean) {
        super.onLoadMoreSuccess(list, hasMore)
        mLoadMoreDelegate?.loadFinish(false, !list.isNullOrEmpty())
    }

    override fun executePageRequest(page: IPage) {
        mPresenter?.loadLv1GoodsGroup(isTop!!)
    }


    override fun onResume() {
        super.onResume()
        mPresenter?.loadLv1GoodsGroup(isTop!!)
    }
}