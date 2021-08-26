package com.lingmiao.shop.business.goods.fragment

import android.content.Context
import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.james.common.base.loadmore.BaseLoadMoreFragment
import com.james.common.base.loadmore.core.IPage
import com.james.common.utils.exts.singleClick
import com.lingmiao.shop.MyApp
import com.lingmiao.shop.R
import com.lingmiao.shop.business.goods.adapter.GoodsMenuAdapter
import com.lingmiao.shop.business.goods.api.bean.GoodsVO
import com.lingmiao.shop.business.goods.api.bean.ShopGroupVO
import com.lingmiao.shop.business.goods.presenter.GoodsListOfMenuPre
import com.lingmiao.shop.business.goods.presenter.impl.GoodsListOfMenuPreImpl
import com.lingmiao.shop.widget.EmptyView
import kotlinx.android.synthetic.main.goods_fragment_goods_of_menu.goodsCountTv
import kotlinx.android.synthetic.main.goods_fragment_goods_of_menu.menuCateL1Tv
import kotlinx.android.synthetic.main.goods_fragment_goods_of_menu.menuCateL2Tv

/**
Desc        : 置顶菜单-商品管理-已添加列表
 **/
class GoodsListOfMenuFragment : BaseLoadMoreFragment<GoodsVO, GoodsListOfMenuPre>(),
    GoodsListOfMenuPre.View {

    private var mItem: ShopGroupVO? = null

    private var mGroups: List<ShopGroupVO>? = null

    private var catPath: String? = null

    companion object {
        const val KEY_ITEM = "KEY_ITEM"

        fun newInstance(item: ShopGroupVO): GoodsListOfMenuFragment {
            return GoodsListOfMenuFragment().apply {
                arguments = Bundle().apply {
                    putSerializable(KEY_ITEM, item)
                }
            }
        }
    }

    override fun initBundles() {
        mItem = arguments?.getSerializable(KEY_ITEM) as ShopGroupVO?
        catPath = mItem?.catPath
    }

    override fun initOthers(rootView: View) {
        menuCateL1Tv.text = mItem?.shopCatName

        // 暂时不切换
        menuCateL1Tv.singleClick {
            mPresenter?.showGroupPop(mItem?.isTop!!, catPath)
        }
        menuCateL2Tv.singleClick {

        }
    }

    override fun getLayoutId(): Int {
        return R.layout.goods_fragment_goods_of_menu
    }


    //----------------必须重写------------------------


    override fun initAdapter(): BaseQuickAdapter<GoodsVO, BaseViewHolder> {
        return GoodsMenuAdapter().apply {
            //menuIv
            setOnItemChildClickListener { _, view, position ->
                var item = mAdapter.getItem(position) as GoodsVO
                if (view.id == R.id.menuIv) {
                    mPresenter?.clickMenuView(mItem?.isTop!!, item, position, view)
                }
            }
            setOnItemClickListener { _, _, position ->
                var item = mAdapter.getItem(position) as GoodsVO
            }
            emptyView = EmptyView(mContext).apply {
                setBackgroundResource(R.color.common_bg)
            }
        }
    }

    override fun createPresenter(): GoodsListOfMenuPre {
        return GoodsListOfMenuPreImpl(mContext, this)
    }

    /**
     * 执行分页请求
     */
    override fun executePageRequest(page: IPage) {
        mPresenter?.loadListData(catPath, page, mAdapter.data)
    }

    override fun onUpdatedGoodsGroup() {
        mLoadMoreDelegate?.refresh()
    }

    override fun onUpdateGroup(groups: List<ShopGroupVO>?, groupName: String?) {
        mGroups = groups
        menuCateL1Tv.text = groupName

        if (mGroups?.size ?: 0 > 0) {
            val item = mGroups?.get(mGroups?.size!! - 1)
            catPath = item?.catPath;

            mLoadMoreDelegate?.refresh()
        }
    }

    override fun setGoodsCount(count: Int) {
        goodsCountTv.text = String.format("共%s件商品", count)
    }

}