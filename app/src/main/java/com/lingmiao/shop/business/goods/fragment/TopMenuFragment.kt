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
import com.james.common.utils.exts.singleClick
import com.lingmiao.shop.R
import com.lingmiao.shop.business.GoodsOfMenuActivity
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
class TopMenuFragment : BaseLoadMoreFragment<ShopGroupVO, CateManagerPre>(), CateManagerPre.GroupManagerView {

    companion object {
        fun newInstance(isTop : Int): TopMenuFragment {
            return TopMenuFragment().apply {
                arguments = Bundle().apply {
                    putInt("isTop", isTop)
                }
            }
        }
    }

    private var isTop: Int? = null

    override fun initBundles() {
        isTop = arguments?.getInt("isTop", 0);
    }

    override fun getLayoutId(): Int? {
        return R.layout.goods_fragment_goods_top_menu;
    }

    override fun initAdapter(): BaseQuickAdapter<ShopGroupVO, BaseViewHolder> {
        val dadapter = MenuAdapter().apply {
            setOnItemClickListener { adapter, view, position ->
//                GroupManagerLv2Activity.openActivity(
//                    this@GroupManagerLv1Activity,
//                    mAdapter.getItem(position)?.shopCatId
//                )
            }
            setOnItemChildClickListener { adapter, view, position ->
                var item = adapter.getItem(position) as ShopGroupVO;
                when (view.id) {
                    R.id.menuEditTv -> {
                        MenuEditActivity.openActivity(activity!!,ShopGroupVO.LEVEL_1, mAdapter.getItem(position)?.shopCatPid, mAdapter.getItem(position));
                    }
                    R.id.menuEditGoodsTv -> {
                        GoodsOfMenuActivity.openActivity(activity!!, item);
                        // GoodsMenuSelectActivity.menu(activity!!, item.shopCatId);
                    }
                    R.id.menuVisibleCb -> {
                        item.disable = if(item.disable == 1) 0 else 1;

                        mPresenter?.updateGroup(item, position);
                    }
                    R.id.menuDeleteTv -> {
                        DialogUtils.showDialog(context as Activity,
                            "删除提示", "删除后不可恢复，确定要删除该菜单吗？",
                            "取消", "确定删除",
                            null, View.OnClickListener {
                                mPresenter?.deleteGoodsGroup(mAdapter.getItem(position), position)
                            })

                    }
//                    R.id.groupDeleteTv -> {
//                        mPresenter?.deleteGoodsGroup(mAdapter.getItem(position), position)
//                    }
                }
            }

//            setOnItemDragListener(listener)
//            onItemLongClickListener = BaseQuickAdapter.OnItemLongClickListener { adapter, view, position -> Boolean
//                if(menuBottom.visibility != View.VISIBLE) {
//                    menuBottom.visibility = View.VISIBLE;
//                }
//                setBatchEditModel(true);
//                return@OnItemLongClickListener true;
//            }
            emptyView = EmptyView(mContext).apply {
                setBackgroundResource(R.color.common_bg)
            }
        }

        val listener: OnItemDragListener = object : OnItemDragListener {
            override fun onItemDragStart(viewHolder: RecyclerView.ViewHolder, pos: Int) {
                LogUtils.d(" ..start : $pos")
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
                val holder = viewHolder as BaseViewHolder
                //                holder.setTextColor(R.id.tv, Color.BLACK);
            }
        }


        val mItemDragAndSwipeCallback = ItemDragAndSwipeCallback(dadapter)

        val mItemTouchHelper: ItemTouchHelper? = ItemTouchHelper(mItemDragAndSwipeCallback)
        mItemTouchHelper!!.attachToRecyclerView(mLoadMoreRv)

        dadapter.setOnItemDragListener(listener);
        dadapter.enableDragItem(mItemTouchHelper);

        return dadapter;
    }

    override fun initOthers(rootView: View) {
        menuAddTv.setOnClickListener {
            MenuEditActivity.openActivity(activity!!,ShopGroupVO.LEVEL_1, null, null);
        }
        menuCancelTv.setOnClickListener {
            menuBottom.visibility = View.GONE;
            menuAllCheckCb.isChecked = false;
            var list = mAdapter?.data?.filter { it?.isChecked == true };
            if(list?.size > 0) {
                list?.forEachIndexed { index, goodsVO ->
                    goodsVO.isChecked = false;
                }
            }
            (mAdapter as MenuAdapter)?.setBatchEditModel(false);
        }
        menuAllCheckCb.setOnCheckedChangeListener { buttonView, isChecked ->
            mAdapter?.data?.forEachIndexed { index, goodsVO ->
                goodsVO.isChecked = isChecked;
            }
            mAdapter?.notifyDataSetChanged();
        }
        menuDeleteTv.setOnClickListener {
            mAdapter.data.forEachIndexed { index, shopGroupVO ->

            }
            //mLoadMoreDelegate?.refresh()
        }
        menuDeleteTv.singleClick {
//            val list = mAdapter?.data?.filter { it-> it.isChecked };
//            mPresenter?.deleteGoodsGroup()
        }

        mSmartRefreshLayout?.setEnableRefresh(false);
        mSmartRefreshLayout?.setEnableLoadMore(false);
    }


    override fun createPresenter(): CateManagerPre? {
        return CateManagerPreImpl(this);
    }

    override fun onDeleteGroupSuccess(position: Int) {
        if(position < mAdapter.data.size) {
            mAdapter.data.removeAt(position);
            mAdapter.notifyDataSetChanged();
        }
    }

    override fun onGroupUpdated(position: Int) {
        mAdapter?.notifyItemChanged(position);
    }

    override fun useEventBus(): Boolean {
        return true;
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

}