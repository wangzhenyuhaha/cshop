package com.lingmiao.shop.business.goods.fragment

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.activityViewModels
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.james.common.base.loadmore.BaseLoadMoreFragment
import com.james.common.base.loadmore.core.IPage
import com.james.common.utils.exts.singleClick
import com.lingmiao.shop.R
import com.lingmiao.shop.business.goods.GoodsOfMenuActivity
import com.lingmiao.shop.business.goods.adapter.GoodsMenuAdapter
import com.lingmiao.shop.business.goods.api.bean.GoodsVO
import com.lingmiao.shop.business.goods.api.bean.ShopGroupVO
import com.lingmiao.shop.business.goods.presenter.GoodsListOfMenuPre
import com.lingmiao.shop.business.goods.presenter.impl.GoodsListOfMenuPreImpl
import com.lingmiao.shop.widget.EmptyView
import kotlinx.android.synthetic.main.goods_fragment_goods_of_menu.*

/**
Desc        : 置顶菜单-商品管理-已添加列表
 **/
class GoodsListOfMenuFragment : BaseLoadMoreFragment<GoodsVO, GoodsListOfMenuPre>(),
    GoodsListOfMenuPre.View {

    private var mItem: ShopGroupVO? = null

    private var mGroups: List<ShopGroupVO>? = null

    //第一级菜单
    private var catPath: String? = null

    //当前菜单
    private var catPathForSearch: String? = null

    //当前菜单ID(需要保存在ViewModel中)
    private var shopCatId: String? = null

    private val model by activityViewModels<GoodsOfMenuActivity.GoodsOfMenuViewModel>()

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


        catPathForSearch = mItem?.catPath
        model.catPathForSearch.value = catPathForSearch

        shopCatId = mItem?.shopCatId
        model.shopCatIdLiveData.value = shopCatId

    }

    override fun initOthers(rootView: View) {
        menuCateL1Tv.text = mItem?.shopCatName



        menuCateL1Tv.singleClick {
            mPresenter?.showGroupPop(mItem?.isTop!!, catPath)
        }

        // menuCateL1Tv.singleClick {
        //            mPresenter?.showCategoryPop("0", it)
        //        }
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
        mPresenter?.loadListData(model.catPathForSearch.value, page, mAdapter.data)
    }

    override fun onUpdatedGoodsGroup() {
        mLoadMoreDelegate?.refresh()
    }

    override fun onUpdateGroup(groups: List<ShopGroupVO>?, groupName: String?) {
        mGroups = groups
        menuCateL1Tv.text = groupName

        if (mGroups?.size ?: 0 > 1) {
            //有子菜单
            val item = mGroups?.get(1)
            catPathForSearch = item?.catPath
            model.catPathForSearch.value= catPathForSearch
            shopCatId = item?.shopCatId
            model.shopCatIdLiveData.value = shopCatId
        } else {
            //无子菜单
            val item = mGroups?.get(0)
            catPathForSearch = item?.catPath
            model.catPathForSearch.value= catPathForSearch
            shopCatId = item?.shopCatId
            model.shopCatIdLiveData.value = shopCatId

        }

        if (mGroups?.size ?: 0 > 0) {
            val item = mGroups?.get(0)
            catPath = item?.catPath
            mLoadMoreDelegate?.refresh()
        }
    }


    override fun setGoodsCount(count: Int) {
        goodsCountTv.text = String.format("共%s件商品", count)
    }


    override fun onResume() {
        super.onResume()
        mLoadMoreDelegate?.refresh()
    }
}