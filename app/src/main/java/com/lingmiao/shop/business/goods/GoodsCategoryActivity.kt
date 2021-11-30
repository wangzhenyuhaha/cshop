package com.lingmiao.shop.business.goods

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.view.View
import androidx.core.content.ContextCompat
import com.amap.api.mapcore.util.it
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.james.common.base.loadmore.BaseLoadMoreActivity
import com.james.common.base.loadmore.core.IPage
import com.lingmiao.shop.R
import com.lingmiao.shop.business.goods.adapter.CategoryAdapter
import com.lingmiao.shop.business.goods.adapter.UsedMenuAdapter
import com.lingmiao.shop.business.goods.api.bean.CategoryVO
import com.lingmiao.shop.business.goods.api.bean.GoodsVO
import com.lingmiao.shop.business.goods.presenter.CategoryEditPre
import com.lingmiao.shop.business.goods.presenter.impl.CategoryEditPreImpl
import kotlinx.android.synthetic.main.goods_activity_goods_catetory.*

/**
Create Date : 2021/4/263:31 PM
Auther      : Fox
Desc        :
 **/
class GoodsCategoryActivity : BaseLoadMoreActivity<CategoryVO, CategoryEditPre>(),
    CategoryEditPre.PubView {

    private var mList: MutableList<CategoryVO>? = mutableListOf()

    //默认 0  中心库添加商品时添加分类 1
    var type: Int = 0

    companion object {

        fun openActivity(context: Context) {
            if (context is Activity) {
                val intent = Intent(context, GoodsCategoryActivity::class.java)
                intent.putExtra("type", 0)
                context.startActivity(intent)
            }
        }

        fun openActivity(context: Context, type: Int) {
            if (context is Activity) {
                val intent = Intent(context, GoodsCategoryActivity::class.java)
                intent.putExtra("type", type)
                context.startActivity(intent)
            }
        }
    }

    override fun initBundles() {
        super.initBundles()
        type = intent.getIntExtra("type", 0)
    }

    override fun useLightMode() = false

    override fun createPresenter() = CategoryEditPreImpl(this, this)

    override fun getLayoutId() = R.layout.goods_activity_goods_catetory


    override fun initAdapter(): BaseQuickAdapter<CategoryVO, BaseViewHolder> {
        return CategoryAdapter(mList).apply {
            setOnItemClickListener { adapter, _, position ->
                val bItem = adapter.data[position] as CategoryVO
            }
            setOnItemChildClickListener { adapter, view, position ->
                val item = getItem(position) as CategoryVO
                when (view.id) {
                    // 加载或者是扩张
                    R.id.cateTitleTv,
                    R.id.cateExpandIv -> {
                        if (item.isExpanded) {
                            adapter?.collapse(position, false)
                        } else {
                            adapter?.expand(position, false)
                        }
                    }
                    R.id.cateAddIv -> {
                        mPresenter?.showAddDialog(item.categoryId.toInt())
                    }
                    // 更多
                    R.id.cateMoreIv -> {
                        mPresenter?.clickMenuView(item, position, view,type)
                    }
                }
            }
            onItemLongClickListener =
                BaseQuickAdapter.OnItemLongClickListener { adapter, view, position ->
                    Boolean
                    return@OnItemLongClickListener true
                }
        }
    }

    override fun initOthers() {
        if (type == 0) {
            mToolBarDelegate.setMidTitle(getString(R.string.goods_category_manager_title))
        } else {
            mToolBarDelegate.setMidTitle("添加分类")
        }

        // 禁用上拉加载、下拉刷新
        mSmartRefreshLayout.setEnableLoadMore(false)
        mSmartRefreshLayout.setBackgroundColor(ContextCompat.getColor(this, R.color.color_ffffff))

        mAdapter.setEmptyView(R.layout.order_empty, mLoadMoreRv)

        categoryAddTv.setOnClickListener {
            mPresenter?.showAddDialog(0)
        }
        categoryCancelTv.setOnClickListener {
            categoryBottom.visibility = View.GONE
            categoryAllCheckCb.isChecked = false
            var list = mAdapter?.data?.filter { it?.isChecked == true };
            if (list?.size > 0) {
                list?.forEachIndexed { index, goodsVO ->
                    goodsVO.isChecked = false
                }
            }
            (mAdapter as UsedMenuAdapter)?.setBatchEditModel(false);
        }
        categoryAllCheckCb.setOnCheckedChangeListener { buttonView, isChecked ->
            mAdapter?.data?.forEachIndexed { index, goodsVO ->
                goodsVO.isChecked = isChecked
            }
            mAdapter?.notifyDataSetChanged()
        }
        categoryDeleteTv.setOnClickListener {
            mLoadMoreDelegate?.refresh()
        }

        mSmartRefreshLayout?.setEnableLoadMore(false)
    }

    override fun executePageRequest(page: IPage) {
        mPresenter.loadLv1GoodsGroup()
    }

    override fun onLoadedLv2(
        list1: String,
        newList: List<CategoryVO>?
    ) {
        var i = -1
        mList?.forEachIndexed { index, item ->
            if (item?.categoryId == list1) {
                // 清除
                item?.subItems?.clear()
                newList?.forEachIndexed { index, categoryVO ->
                    // 新增
                    item.addSubItem(categoryVO)
                }
                i = index
            }
        }
        if (i > -1) {
            // 刷新
            mAdapter?.collapse(i)
            mAdapter?.expand(i)
            mAdapter?.notifyDataSetChanged()
        }
    }

    override fun onDeleted(id: String) {
        var i = -1
        var __i = -1

        var item: CategoryVO? = null
        val it_b: MutableIterator<CategoryVO>? = mList?.iterator();
        while (it_b?.hasNext() == true) {
            item = it_b?.next()
            if (item?.categoryId == id) {
                it_b.remove()
            } else {
                var _i = -1
                item?.children?.forEachIndexed { index, it ->
                    if (it.categoryId == id) {
                        _i = index
                    }
                }
                if (_i > -1) {
                    item?.children?.removeAt(_i)
                    item?.subItems?.clear()
                    item?.children?.forEachIndexed { index, _it ->
                        item?.addSubItem(_it)
                    }
                }
            }
        }
        mAdapter?.notifyDataSetChanged()

    }

    override fun onAdded(pId: Int, vo: CategoryVO) {
        if (pId == 0) {
            mList?.add(vo)
            mAdapter?.notifyDataSetChanged()
        } else {
            var i = -1
            mList?.forEachIndexed { index, item ->
                if (item?.categoryId == pId.toString()) {
                    // 清除老的
                    item?.subItems?.clear()
                    // 子类新增
                    val children = item.children
                    children?.add(vo)
                    children?.forEachIndexed { index, categoryVO ->
                        categoryVO?.showLevel = 1
                        // 添加新的
                        item?.addSubItem(categoryVO)
                    }
                    i = index
                }
            }
            if (i > -1) {
                // 刷新
                mAdapter?.collapse(i)
                mAdapter?.expand(i)
                mAdapter?.notifyDataSetChanged()
            }
        }
    }

    override fun onUpdated(vo: CategoryVO) {
        if (vo.parentId == 0) {
            mList?.forEachIndexed { index, item ->
                if (item?.categoryId == vo.categoryId) {
                    item?.name = vo.name
                }
            }
        } else {
            mList?.forEachIndexed { index, item ->
                item.children?.forEachIndexed { i, it ->
                    if (it?.categoryId == vo.categoryId) {
                        it.name = vo.name
                    }
                }
            }
        }

        mAdapter?.notifyDataSetChanged()
    }

}