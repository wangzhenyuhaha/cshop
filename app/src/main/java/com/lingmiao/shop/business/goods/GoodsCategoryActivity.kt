package com.lingmiao.shop.business.goods

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.view.View
import androidx.core.content.ContextCompat
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.james.common.base.loadmore.BaseLoadMoreActivity
import com.james.common.base.loadmore.core.IPage
import com.lingmiao.shop.R
import com.lingmiao.shop.business.goods.adapter.CategoryAdapter
import com.lingmiao.shop.business.goods.adapter.UsedMenuAdapter
import com.lingmiao.shop.business.goods.api.bean.CategoryVO
import com.lingmiao.shop.business.goods.presenter.CategoryEditPre
import com.lingmiao.shop.business.goods.presenter.impl.CategoryEditPreImpl
import kotlinx.android.synthetic.main.goods_activity_goods_catetory.*

/**
Create Date : 2021/4/263:31 PM
Auther      : Fox
Desc        :
 **/
class GoodsCategoryActivity : BaseLoadMoreActivity<CategoryVO, CategoryEditPre>(), CategoryEditPre.PubView {

    private var mList : MutableList<CategoryVO>? = mutableListOf();

    companion object {

        fun openActivity(context: Context) {
            if (context is Activity) {
                val intent = Intent(context, GoodsCategoryActivity::class.java)
                context.startActivity(intent)
            }
        }
    }

    override fun useLightMode(): Boolean {
        return false;
    }

    override fun createPresenter(): CategoryEditPre {
        return CategoryEditPreImpl(this, this);
    }

    override fun getLayoutId(): Int {
        return R.layout.goods_activity_goods_catetory;
    }

    override fun initAdapter(): BaseQuickAdapter<CategoryVO, BaseViewHolder> {
        return CategoryAdapter(mList).apply {
            setOnItemClickListener { adapter, view, position ->
                val bItem = adapter.data[position] as CategoryVO;
            }
            setOnItemChildClickListener { adapter, view, position ->
                when (view.id) {
                    // 加载或者是扩张
                    R.id.cateTitleTv,
                    R.id.cateExpandIv -> {
                        val item = getItem(position) as CategoryVO;
                        if(item.isExpanded) {
                            adapter?.collapse(position, false);
                        } else {
                            adapter?.expand(position, false)
                        }
                    }
                    // 更多
                    R.id.cateMoreIv -> {
                        mPresenter?.clickMenuView(mAdapter.getItem(position), position, view)
//                        val item = getItem(position) as CategoryVO;
//                        showAddDialog(item.categoryId!!.toInt());
                    }
                }
            }
            onItemLongClickListener = BaseQuickAdapter.OnItemLongClickListener { adapter, view, position -> Boolean
//                if(menuBottom.visibility != View.VISIBLE) {
//                    menuBottom.visibility = View.VISIBLE;
//                }
//                setBatchEditModel(true);

                return@OnItemLongClickListener true;
            }
        }
    }

    override fun initOthers() {
        mToolBarDelegate.setMidTitle(getString(R.string.goods_category_manager_title))
        // 禁用上拉加载、下拉刷新
//        mSmartRefreshLayout.setEnableRefresh(false)
        mSmartRefreshLayout.setEnableLoadMore(false)
        mSmartRefreshLayout.setBackgroundColor(ContextCompat.getColor(this, R.color.color_ffffff))


        categoryAddTv.setOnClickListener {
            mPresenter?.showAddDialog(0);
        }
        categoryCancelTv.setOnClickListener {
            categoryBottom.visibility = View.GONE;
            categoryAllCheckCb.isChecked = false;
            var list = mAdapter?.data?.filter { it?.isChecked == true };
            if(list?.size > 0) {
                list?.forEachIndexed { index, goodsVO ->
                    goodsVO.isChecked = false;
                }
            }
            (mAdapter as UsedMenuAdapter)?.setBatchEditModel(false);
        }
        categoryAllCheckCb.setOnCheckedChangeListener { buttonView, isChecked ->
            mAdapter?.data?.forEachIndexed { index, goodsVO ->
                goodsVO.isChecked = isChecked;
            }
            mAdapter?.notifyDataSetChanged();
        }
        categoryDeleteTv.setOnClickListener {
            mLoadMoreDelegate?.refresh()
        }

        mSmartRefreshLayout?.setEnableLoadMore(false);
    }

    override fun executePageRequest(page: IPage) {
        mPresenter.loadLv1GoodsGroup();
    }

    override fun onLoadedLv2(
        list1: String,
        newList: List<CategoryVO>?
    ) {
        var i = -1;
        mList?.forEachIndexed { index, item ->
            if(item?.categoryId == list1) {
                // 清除
                item?.subItems?.clear();
                newList?.forEachIndexed { index, categoryVO ->
                    // 新增
                    item.addSubItem(categoryVO);
                }
                i = index;
            }
        }
        if(i > -1) {
            // 刷新
            mAdapter?.collapse(i)
            mAdapter?.expand(i);
            mAdapter?.notifyDataSetChanged();
        }
    }

    override fun onDeleted(id: String) {
        var i = -1;
        var __i = -1;
        mList?.forEachIndexed { index, item ->
            if (item?.categoryId == id) {
                i = index;
                if(i > -1) {
                    mList?.removeAt(i);
                    mAdapter?.notifyDataSetChanged();
                }
            }
            item?.children?.forEachIndexed { _i, it ->
                if(it.categoryId == id) {
                    i = index;
                    __i = _i;
                    item?.children?.removeAt(_i)
                    item?.subItems?.clear();
                    item?.children?.forEachIndexed { index, _it ->
                        item?.addSubItem(_it)
                    }
                    mAdapter?.notifyDataSetChanged();
                }
            }
        };

    }

    override fun onAdded(pId : Int, vo: CategoryVO) {
        if(pId == 0) {
            mList?.add(vo);
            mAdapter?.notifyDataSetChanged();
        } else {
            var i = -1;
            mList?.forEachIndexed { index, item ->
                if(item?.categoryId == pId.toString()) {
                    // 清除老的
                    item?.subItems?.clear();
                    // 子类新增
                    val children = item.children;
                    children?.add(vo);
                    children?.forEachIndexed { index, categoryVO ->
                        categoryVO?.showLevel = 1;
                        // 添加新的
                        item?.addSubItem(categoryVO);
                    }
                    i = index;
                }
            }
            if(i > -1) {
                // 刷新
                mAdapter?.collapse(i)
                mAdapter?.expand(i);
                mAdapter?.notifyDataSetChanged();
            }
            // val id = String.format("%s", pId);
            // mPresenter?.loadLv2GoodsGroup(id);
        }
    }

    override fun onUpdated(vo: CategoryVO) {
        if(vo.parentId == 0) {
            mList?.forEachIndexed { index, item ->
                if(item?.categoryId == vo.categoryId) {
                    item?.name = vo.name;
                }
            }
        } else {
            mList?.forEachIndexed { index, item ->
                item.children?.forEachIndexed { i, it ->
                    if(it?.categoryId == vo.categoryId) {
                        it.name = vo.name;
                    }
                }
            }
        }

        mAdapter?.notifyDataSetChanged();
//        mPresenter.loadLv1GoodsGroup();
    }

}