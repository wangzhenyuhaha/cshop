package com.lingmiao.shop.business.goods

import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.james.common.base.BaseActivity
import com.lingmiao.shop.R
import com.lingmiao.shop.business.goods.adapter.GoodsPublishTypeAdapter
import com.lingmiao.shop.business.goods.api.bean.GoodsPublishTypeVo
import com.lingmiao.shop.business.goods.presenter.GoodsPublishTypePre
import com.lingmiao.shop.business.goods.presenter.impl.GoodsPublishTypePreImpl
import com.lingmiao.shop.business.tools.adapter.AreasAdapter
import kotlinx.android.synthetic.main.goods_activity_goods_publish_type.*

/**
Create Date : 2021/3/69:58 AM
Auther      : Fox
Desc        : 商品发布之类型选择
 **/
class GoodsPublishTypeActivity : BaseActivity<GoodsPublishTypePre>(), GoodsPublishTypePre.View {

    private var mList : MutableList<GoodsPublishTypeVo>? =null;
    private var mTypeAdapter : GoodsPublishTypeAdapter?=null;

    override fun createPresenter(): GoodsPublishTypePre {
        return GoodsPublishTypePreImpl(this, this);
    }

    override fun useBaseLayout(): Boolean {
        return false;
    }

    override fun getLayoutId(): Int {
        return R.layout.goods_activity_goods_publish_type
    }

    override fun initView() {
        toolbarView?.apply {
            setTitleContent(getString(R.string.goods_publish_type_title))
        }
        initData();
        initAdapter();
        initRecycleView();
    }

    fun getC(pLevel : Int) : MutableList<GoodsPublishTypeVo> {
        var temp = GoodsPublishTypeVo();
        temp.id = "10";
        temp.name = "商超";
        temp.showLevel = 1;
        temp.parentLevel = pLevel;
        var tempList1 = mutableListOf<GoodsPublishTypeVo>();
        tempList1.add(temp);


        temp = GoodsPublishTypeVo();
        temp.id = "11";
        temp.name = "餐饮外卖";
        temp.showLevel = 1;
        temp.parentLevel = pLevel;
        tempList1.add(temp);

        temp = GoodsPublishTypeVo();
        temp.id = "12";
        temp.name = "水果店";
        temp.showLevel = 1;
        temp.parentLevel = pLevel;
        tempList1.add(temp);

        temp = GoodsPublishTypeVo();
        temp.id = "13";
        temp.name = "糕点面包";
        temp.showLevel = 1;
        temp.parentLevel = pLevel;
        tempList1.add(temp);
        return tempList1;
    }

    private fun initData() {
        var tempList1 = getC(0);
        var tempList2 = getC(1);

        mList  = mutableListOf();
        var item = GoodsPublishTypeVo();
        item.id = "1";
        item.name = "商品中心库";
        item.showLevel = 0;
        for(it in tempList1) {
            item.addSubItem(it)
        }
        item.children = tempList1;
        mList?.add(item)

        item = GoodsPublishTypeVo();
        item.id = "2";
        item.name = "自有商品";
        item.showLevel = 0;
        for(it in tempList2) {
            item.addSubItem(it)
        }
        item.children = tempList2;
        mList?.add(item)
    }

    fun initAdapter() {
        mTypeAdapter = GoodsPublishTypeAdapter(mList).apply {
            setOnItemChildClickListener { adapter, view, position ->
                val viewType = adapter.getItemViewType(position);
                if(viewType == AreasAdapter.TYPE_LEVEL_0) {
                    val bItem = adapter.data[position] as GoodsPublishTypeVo;
                    if(view.id == R.id.expandIv || view.id == R.id.titleLL) {
                        if(bItem.isExpanded) {
                            mTypeAdapter?.collapse(position, false);
                        } else {
                            mTypeAdapter?.expand(position, false)
                        }
                    }
                } else if(viewType == AreasAdapter.TYPE_LEVEL_1) {
                    val bItem = adapter.data[position] as GoodsPublishTypeVo

                    if(bItem?.parentLevel == 0) {
                        GoodsManagerActivity.type(context, bItem?.showLevel);
                    } else {
                        GoodsPublishNewActivity.newPublish(context, 0);
                    }
                }
            }
            setOnItemClickListener { adapter, view, position ->

            }
        }
    }

    private fun initRecycleView() {
        typeRv.apply {
            layoutManager = initLayoutManager();
            adapter = mTypeAdapter!!;
        }
    }

    private fun initLayoutManager(): RecyclerView.LayoutManager {
        return LinearLayoutManager(this)
    }
}