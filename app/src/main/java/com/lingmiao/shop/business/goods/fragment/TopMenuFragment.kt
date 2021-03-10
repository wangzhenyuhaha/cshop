package com.lingmiao.shop.business.goods.fragment

import android.view.View
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.james.common.base.loadmore.BaseLoadMoreFragment
import com.james.common.base.loadmore.core.IPage
import com.lingmiao.shop.R
import com.lingmiao.shop.business.goods.GroupManagerEditActivity
import com.lingmiao.shop.business.goods.MenuEditActivity
import com.lingmiao.shop.business.goods.adapter.GoodsStatusAdapter
import com.lingmiao.shop.business.goods.adapter.GroupManagerAdapter
import com.lingmiao.shop.business.goods.adapter.MenuAdapter
import com.lingmiao.shop.business.goods.api.bean.ShopGroupVO
import com.lingmiao.shop.business.goods.presenter.GroupManagerPre
import com.lingmiao.shop.business.goods.presenter.impl.GroupManagerPreImpl
import kotlinx.android.synthetic.main.goods_fragment_goods_top_menu.*

/**
Create Date : 2021/3/101:00 AM
Auther      : Fox
Desc        :
 **/
class TopMenuFragment : BaseLoadMoreFragment<ShopGroupVO, GroupManagerPre>(), GroupManagerPre.GroupManagerView {

    companion object {

        fun newInstance(): TopMenuFragment {
            return TopMenuFragment().apply {
//                arguments = Bundle().apply {
//                    putInt(id, goodsStatus)
//                }
            }
        }
    }

    override fun getLayoutId(): Int? {
        return R.layout.goods_fragment_goods_top_menu;
    }

    override fun initAdapter(): BaseQuickAdapter<ShopGroupVO, BaseViewHolder> {
        return MenuAdapter().apply {
            setOnItemClickListener { adapter, view, position ->
//                GroupManagerLv2Activity.openActivity(
//                    this@GroupManagerLv1Activity,
//                    mAdapter.getItem(position)?.shopCatId
//                )
            }
            setOnItemChildClickListener { adapter, view, position ->
                when (view.id) {
                    R.id.menuEditTv -> {
                        GroupManagerEditActivity.openActivity(
                            activity!!,
                            ShopGroupVO.LEVEL_1, null, mAdapter.getItem(position)
                        )
                    }
//                    R.id.groupDeleteTv -> {
//                        mPresenter?.deleteGoodsGroup(mAdapter.getItem(position), position)
//                    }
                }
            }
            onItemLongClickListener = BaseQuickAdapter.OnItemLongClickListener { adapter, view, position -> Boolean
                if(menuBottom.visibility != View.VISIBLE) {
                    menuBottom.visibility = View.VISIBLE;
                }
                setBatchEditModel(true);
                return@OnItemLongClickListener true;
            }
        }
    }

    override fun initOthers(rootView: View) {
        menuAddTv.setOnClickListener {
            MenuEditActivity.openActivity(activity!!,ShopGroupVO.LEVEL_1, null, mAdapter.getItem(0));
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
            mLoadMoreDelegate?.refresh()
        }
    }

    override fun createPresenter(): GroupManagerPre? {
        return GroupManagerPreImpl(this);
    }

    override fun onDeleteGroupSuccess(position: Int) {

    }

    override fun executePageRequest(page: IPage) {
        mPresenter?.loadLv1GoodsGroup()
    }
}