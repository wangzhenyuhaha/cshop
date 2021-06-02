package com.lingmiao.shop.business.goods.fragment

import android.os.Bundle
import android.view.View
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.james.common.base.loadmore.BaseLoadMoreFragment
import com.james.common.base.loadmore.core.IPage
import com.james.common.utils.exts.singleClick
import com.lingmiao.shop.R
import com.lingmiao.shop.business.goods.adapter.GoodsAdapter
import com.lingmiao.shop.business.goods.adapter.GoodsMenuAdapter
import com.lingmiao.shop.business.goods.adapter.GoodsOfMenuAdapter
import com.lingmiao.shop.business.goods.adapter.GoodsSelectAdapter
import com.lingmiao.shop.business.goods.api.bean.GoodsVO
import com.lingmiao.shop.business.goods.api.bean.ShopGroupVO
import com.lingmiao.shop.business.goods.presenter.GoodsListOfMenuPre
import com.lingmiao.shop.business.goods.presenter.impl.GoodsListOfMenuPreImpl
import com.lingmiao.shop.widget.EmptyView
import kotlinx.android.synthetic.main.goods_fragment_goods_of_menu.*

/**
Create Date : 2021/6/19:55 AM
Auther      : Fox
Desc        :
 **/
class GoodsListOfMenuFragment : BaseLoadMoreFragment<GoodsVO, GoodsListOfMenuPre>(), GoodsListOfMenuPre.View {

    var mItem : ShopGroupVO? = null;

    var mGroups: List<ShopGroupVO>? = null;

    var catPath : String ? = null;

    companion object {
        const val KEY_ITEM = "KEY_ITEM"

        fun newInstance(item : ShopGroupVO): GoodsListOfMenuFragment {
            return GoodsListOfMenuFragment().apply {
                arguments = Bundle().apply {
                    putSerializable(KEY_ITEM, item)
                }
            }
        }
    }

    override fun initBundles() {
        mItem = arguments?.getSerializable(KEY_ITEM) as ShopGroupVO?;
        catPath = mItem?.catPath;
    }

    override fun initAdapter(): BaseQuickAdapter<GoodsVO, BaseViewHolder> {
        return GoodsMenuAdapter().apply {
            //menuIv
            setOnItemChildClickListener { adapter, view, position ->
                var item = mAdapter.getItem(position) as GoodsVO;
                if(view.id == R.id.menuIv) {
                    mPresenter?.clickMenuView(item, position, view)
                }
            }
            setOnItemClickListener { adapter, view, position ->
                var item = mAdapter.getItem(position) as GoodsVO;
            }
            emptyView = EmptyView(mContext).apply {
                setBackgroundResource(R.color.common_bg)
            }
        }
    }

    override fun getLayoutId(): Int? {
        return R.layout.goods_fragment_goods_of_menu;
    }

    override fun createPresenter(): GoodsListOfMenuPre {
        return GoodsListOfMenuPreImpl(mContext, this);
    }

    override fun initOthers(rootView: View) {
        menuCateL1Tv.setText(mItem?.shopCatName);

        menuCateL1Tv.singleClick {
            mPresenter?.showGroupPop(mItem?.isTop!!);
        }
        menuCateL2Tv.singleClick {

        }
    }

    override fun onUpdateGroup(groups: List<ShopGroupVO>?, groupName: String?) {
        mGroups = groups;
        menuCateL1Tv.setText(groupName)

        if(mGroups?.size?:0 > 0) {
            val item = mGroups?.get(mGroups?.size!! - 1)
            catPath = item?.catPath;

            mLoadMoreDelegate?.refresh()
        }
    }

    /**
     * 执行分页请求
     */
    override fun executePageRequest(page: IPage) {
        mPresenter?.loadListData(catPath, page, mAdapter.data)
    }
}