package com.lingmiao.shop.business.goods

import com.james.common.base.BaseActivity
import com.lingmiao.shop.R
import com.lingmiao.shop.business.goods.adapter.GoodsPublishTypeAdapter
import com.lingmiao.shop.business.goods.api.bean.CategoryVO
import com.lingmiao.shop.business.goods.presenter.GoodsPublishTypePre
import com.lingmiao.shop.business.goods.presenter.impl.GoodsPublishTypePreImpl
import com.lingmiao.shop.business.tools.adapter.AreasAdapter
import com.lingmiao.shop.util.initAdapter
import kotlinx.android.synthetic.main.goods_activity_goods_publish_type.*

/**
Create Date : 2021/3/69:58 AM
Auther      : Fox
Desc        : 商品发布之类型选择
 **/
class GoodsPublishTypeActivity : BaseActivity<GoodsPublishTypePre>(), GoodsPublishTypePre.View {

    private var mList: MutableList<CategoryVO>? = null;
    private var mTypeAdapter: GoodsPublishTypeAdapter? = null;

    override fun createPresenter(): GoodsPublishTypePre {
        return GoodsPublishTypePreImpl(this, this);
    }

    override fun useLightMode(): Boolean {
        return false;
    }

    override fun getLayoutId(): Int {
        return R.layout.goods_activity_goods_publish_type
    }

    override fun initView() {
        mToolBarDelegate.setMidTitle(getString(R.string.goods_publish_type_title))
        initData();
        initAdapter();
        initRecycleView();
//        mPresenter?.loadList();
        addFirstLevel();
    }

    fun initAdapter() {
        mTypeAdapter = GoodsPublishTypeAdapter(mList).apply {
            setOnItemChildClickListener { adapter, view, position ->
                val viewType = adapter.getItemViewType(position);
                if (viewType == AreasAdapter.TYPE_LEVEL_0) {
//                    val bItem = adapter.data[position] as CategoryVO;
//                    if(view.id == R.id.expandIv || view.id == R.id.titleLL) {
//                        if(bItem.isExpanded) {
//                            mTypeAdapter?.collapse(position, false);
//                        } else {
//                            mTypeAdapter?.expand(position, false)
//                        }
//                    }
                    if (position == 0) {
                        GoodsManagerActivity.type(context, "0");
                    } else {
                        GoodsPublishNewActivity.newPublish(context)
                    }
                } else if (viewType == AreasAdapter.TYPE_LEVEL_1) {
                    val bItem = adapter.data[position] as CategoryVO

                    if (bItem?.parentLevel == 0) {
                        GoodsManagerActivity.type(context, "0");
                    } else {
                        GoodsPublishNewActivity.newPublish(context)
                    }
                }
            }
            setOnItemClickListener { adapter, view, position ->

            }
        }
    }

    private fun initRecycleView() {
        typeRv.initAdapter(mTypeAdapter!!)
    }

    private fun initData() {
        mList = mutableListOf();
    }

    fun addFirstLevel() {
        var item = CategoryVO();
        item.categoryId = "1";
        item.name = "商品中心库";
        item.showLevel = 0;
        mList?.add(item)

        val item2 = CategoryVO();
        item2.categoryId = "2";
        item2.name = "自有商品";
        item2.showLevel = 0;
        mList?.add(item2)

        mTypeAdapter?.notifyDataSetChanged();
    }

    override fun onListSuccess(list: List<CategoryVO>) {
        var item = CategoryVO();
        item.categoryId = "1";
        item.name = "商品中心库";
        item.showLevel = 0;

        for (it in list) {
            it.showLevel = 1;
            it.parentLevel = 0;
            item.addSubItem(it);
        }

        mList?.add(item)
        mTypeAdapter?.notifyDataSetChanged();
    }

    override fun onSelfListSuccess(list: List<CategoryVO>) {
        val item2 = CategoryVO();
        item2.categoryId = "2";
        item2.name = "自有商品";
        item2.showLevel = 0;
        for (it in list) {
            it.showLevel = 1;
            it.parentLevel = 1;
            item2.addSubItem(it);
        }
        mList?.add(item2)
        mTypeAdapter?.notifyDataSetChanged();
    }
}