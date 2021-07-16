package com.lingmiao.shop.business.goods.fragment

import android.os.Bundle
import android.view.View
import com.amap.api.mapcore.util.it
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.james.common.base.loadmore.BaseLoadMoreFragment
import com.james.common.base.loadmore.core.IPage
import com.james.common.utils.exts.singleClick
import com.lingmiao.shop.R
import com.lingmiao.shop.business.goods.adapter.GoodsSelectAdapter
import com.lingmiao.shop.business.goods.api.bean.CategoryVO
import com.lingmiao.shop.business.goods.api.bean.GoodsVO
import com.lingmiao.shop.business.goods.api.bean.ShopGroupVO
import com.lingmiao.shop.business.goods.presenter.GoodsListToMenuPre
import com.lingmiao.shop.business.goods.presenter.impl.GoodsListToMenuPreImpl
import com.lingmiao.shop.widget.EmptyView
import kotlinx.android.synthetic.main.goods_fragment_goods_to_menu.*
import kotlinx.android.synthetic.main.goods_fragment_goods_to_menu.menuCateL1Tv
import kotlinx.android.synthetic.main.goods_fragment_goods_to_menu.menuCateL2Tv

/**
Create Date : 2021/6/110:46 AM
Auther      : Fox
Desc        : 常用菜单-商品管理-订单列表
 **/
class GoodsListToMenuFragment : BaseLoadMoreFragment<GoodsVO, GoodsListToMenuPre>(), GoodsListToMenuPre.View {

    var mItem : ShopGroupVO? = null;

    var catPath : String ? = null;

    var catId : String ? = null;

    companion object {
        const val KEY_ITEM = "KEY_ITEM"

        fun newInstance(item : ShopGroupVO): GoodsListToMenuFragment {
            return GoodsListToMenuFragment().apply {
                arguments = Bundle().apply {
                    putSerializable(KEY_ITEM, item)
                }
            }
        }
    }

    override fun initBundles() {
        mItem = arguments?.getSerializable(KEY_ITEM) as ShopGroupVO?;
        catId = mItem?.shopCatId;
//        catPath = mItem?.catPath;
    }

    override fun initAdapter(): BaseQuickAdapter<GoodsVO, BaseViewHolder> {
        return GoodsSelectAdapter().apply {
            //menuIv
            setOnItemChildClickListener { adapter, view, position ->
                var item = mAdapter.getItem(position) as GoodsVO;
                if (view.id == R.id.menuIv) {
                    item?.isChecked = !(item?.isChecked?:false);
                    setCheckedCount(getCheckedCount());
//                    shiftChecked(position);
                }
//                if(view.id == R.id.menuIv) {
//                    mPresenter?.clickMenuView(item, position, view)
//                }
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
        return R.layout.goods_fragment_goods_to_menu;
    }

    override fun createPresenter(): GoodsListToMenuPre {
        return GoodsListToMenuPreImpl(mContext, this);
    }

    override fun initOthers(rootView: View) {
        menuCateL1Tv.singleClick {
            mPresenter?.showCategoryPop("0",it);
        }
        menuCateL2Tv.singleClick {

        }
        goodsCheckSave.singleClick {
            val list = mAdapter.data?.filter { it?.isChecked == true };
            if(list == null || list.size == 0) {
                return@singleClick;
            }
            val li = list?.map { it?.goodsId?.toInt() };

            mPresenter?.bindGoods(li!!, mItem?.shopCatId!!);
        }
    }

    var mCateList: List<CategoryVO>? = null;

    override fun onUpdatedCategory(list: List<CategoryVO>?, name : String?) {
        mCateList = list;
        menuCateL1Tv.setText(name)

        if(mCateList?.size?:0 > 0) {
            val item = mCateList?.get(mCateList?.size!! - 1)
//            catId = item?.categoryId;
            catPath = item?.categoryPath;
            mLoadMoreDelegate?.refresh()
        }
    }

    /**
     * 执行分页请求
     */
    override fun executePageRequest(page: IPage) {
        setCheckedCount(0);
        mPresenter?.loadListData(catId, catPath, page, mAdapter.data)
    }

    override fun setGoodsCount(count : Int) {
        goodsCountTv.text = String.format("共%s件商品，", count);
    }

    fun setCheckedCount(count: Int) {
        goodsCheckedCountTv.text = String.format("已选择%s件商品", count);
    }

    fun getCheckedCount(): Int {
        return mAdapter?.data?.filter { it?.isChecked == true }?.size;
    }

}