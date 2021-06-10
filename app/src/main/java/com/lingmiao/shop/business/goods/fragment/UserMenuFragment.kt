package com.lingmiao.shop.business.goods.fragment

import android.app.Activity
import android.os.Bundle
import android.view.View
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.james.common.base.loadmore.BaseLoadMoreFragment
import com.james.common.base.loadmore.core.IPage
import com.james.common.utils.DialogUtils
import com.james.common.utils.exts.singleClick
import com.lingmiao.shop.R
import com.lingmiao.shop.business.GoodsOfMenuActivity
import com.lingmiao.shop.business.goods.UserMenuEditActivity
import com.lingmiao.shop.business.goods.adapter.MenuOfUserAdapter
import com.lingmiao.shop.business.goods.api.bean.ShopGroupVO
import com.lingmiao.shop.business.goods.event.GroupRefreshEvent
import com.lingmiao.shop.business.goods.presenter.CateManagerPre
import com.lingmiao.shop.business.goods.presenter.impl.UserMenuPreImpl
import com.lingmiao.shop.widget.EmptyView
import kotlinx.android.synthetic.main.goods_fragment_goods_top_menu.*
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode

/**
Create Date : 2021/3/101:00 AM
Auther      : Fox
Desc        :
 **/
class UserMenuFragment : BaseLoadMoreFragment<ShopGroupVO, CateManagerPre>(), CateManagerPre.GroupManagerView {

    companion object {
        fun newInstance(isTop : Int): UserMenuFragment {
            return UserMenuFragment().apply {
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
        return MenuOfUserAdapter().apply {
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
                        UserMenuEditActivity.openActivity(activity!!, mAdapter.getItem(position)?.shopCatPid, mAdapter.getItem(position));
                    }
                    R.id.menuEditGoodsTv -> {
                        GoodsOfMenuActivity.openActivity(activity!!, item);
                        //GoodsMenuSelectActivity.menu(activity!!, item.shopCatId);
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
    }

    override fun initOthers(rootView: View) {
        menuAddTv.setOnClickListener {
            UserMenuEditActivity.openActivity(activity!!, "0", null);
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
            (mAdapter as MenuOfUserAdapter)?.setBatchEditModel(false);
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
    }

    override fun createPresenter(): CateManagerPre? {
        return UserMenuPreImpl(this);
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