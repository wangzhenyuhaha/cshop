package com.lingmiao.shop.business.sales

import android.app.Activity
import android.content.Intent
import android.view.View
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.james.common.base.loadmore.BaseLoadMoreActivity
import com.james.common.base.loadmore.core.IPage
import com.james.common.utils.exts.singleClick
import com.james.common.utils.exts.visiable
import com.lingmiao.shop.R
import com.lingmiao.shop.business.goods.adapter.GoodsSelectAdapter
import com.lingmiao.shop.business.goods.api.bean.CategoryVO
import com.lingmiao.shop.business.goods.api.bean.GoodsVO
import com.lingmiao.shop.business.sales.bean.SalesVo
import com.lingmiao.shop.business.sales.presenter.ISalesGoodPresenter
import com.lingmiao.shop.business.sales.presenter.impl.SalesGoodsPreImpl
import com.lingmiao.shop.widget.EmptyView
import kotlinx.android.synthetic.main.sales_activity_goods.*
import kotlinx.android.synthetic.main.sales_activity_goods.menuCateL1Tv

/**
Create Date : 2021/6/411:12 AM
Auther      : Fox
Desc        :
 **/
class SalesGoodsActivity : BaseLoadMoreActivity<GoodsVO, ISalesGoodPresenter>(), ISalesGoodPresenter.View {

    var catPath : String ? = null;

    var mCateList: List<CategoryVO>? = null;

    var mItem : SalesVo? = null;

    companion object {
        const val KEY_ITEM = "KEY_ITEM"

        fun open(context: Activity, result : Int) {
            edit(context, null, result);
        }

        fun edit(context: Activity, item: SalesVo?, result : Int) {
            val intent = Intent(context, SalesGoodsActivity::class.java)
            intent.putExtra(KEY_ITEM, item)
            context.startActivityForResult(intent, result)
        }

    }

    override fun initBundles() {
        mItem = intent?.getSerializableExtra(KEY_ITEM) as SalesVo?;
    }

    override fun createPresenter(): ISalesGoodPresenter {
        return SalesGoodsPreImpl(this, this);
    }

    override fun getLayoutId(): Int {
        return R.layout.sales_activity_goods;
    }

    override fun initOthers() {
        mToolBarDelegate.setMidTitle(getString(R.string.sales_goods_title))
        menuCateL1Tv.singleClick {
            mPresenter?.showCategoryPop("0",it);
        }
        goodsRg.setOnCheckedChangeListener { group, checkedId ->
            if(checkedId == R.id.allGoodsRb) {
                partGoodsLayout.visibility = View.INVISIBLE
            } else if(checkedId == R.id.partGoodsRb){
                partGoodsLayout.visiable()
            }
        }
        submitTv.singleClick {
            val list = mAdapter.data.filter { it?.isChecked == true };
            val rangeType = if(allGoodsRb.isChecked) SalesActivityEditActivity.VALUE_ALL else SalesActivityEditActivity.VALUE_PART
            if(mItem == null || mItem?.id?.isEmpty() == true) {
                val intent = Intent();
                intent.putExtra("type", rangeType);
                if(partGoodsRb.isChecked) {
                    intent.putExtra("goodsList", list as ArrayList<GoodsVO>);
                }
                setResult(Activity.RESULT_OK, intent)
                finish();
            } else {
                mItem?.goodsList = list
                mItem?.rangeType = rangeType;
                mPresenter?.updateSalesGoods(mItem!!);
            }

        }
        mItem?.apply {
            //onSetSalesGoods(mItem!!);
            if(mItem?.id?.isNotEmpty()?:false) {
                mPresenter?.getSalesGoods(mItem?.id!!);
            }
            setUi();
        }
    }

    override fun initAdapter(): BaseQuickAdapter<GoodsVO, BaseViewHolder> {
        return GoodsSelectAdapter().apply {
            //menuIv
            setOnItemChildClickListener { adapter, view, position ->
                var item = mAdapter.getItem(position) as GoodsVO;
                if (view.id == R.id.menuIv) {
                    item?.isChecked = !(item?.isChecked?:false);
                    //setCheckedCount(getCheckedCount());
                }
//                if(view.id == R.id.menuIv) {
//                    mPresenter?.clickMenuView(item, position, view)
//                }
            }
            setOnItemClickListener { adapter, view, position ->
                var item = mAdapter.getItem(position) as GoodsVO;
            }
            emptyView = EmptyView(context).apply {
                setBackgroundResource(R.color.common_bg)
            }
        }
    }

    override fun onUpdatedCategory(list: List<CategoryVO>?, name: String?) {
        mCateList = list;
        menuCateL1Tv.setText(name)

        if(mCateList?.size?:0 > 0) {
            val item = mCateList?.get(mCateList?.size!! - 1)
            catPath = item?.categoryPath;

            mLoadMoreDelegate?.refresh()
        }
    }

    override fun onSetSalesGoods(item: SalesVo) {
        mItem = item;
        setUi();
    }

    fun setUi() {
        mItem?.apply {
            if(rangeType == SalesActivityEditActivity.VALUE_ALL) {
                if(!allGoodsRb.isChecked) {
                    allGoodsRb.isChecked = true;
                }
            } else if(rangeType ==SalesActivityEditActivity.VALUE_PART){
                if(!partGoodsRb.isChecked) {
                    partGoodsRb.isChecked = true;
                }
            }
            goodsList?.forEach {
                it.isChecked = true;
            }
            mAdapter?.replaceData(goodsList?: mutableListOf());
        }
    }

    override fun onUpdatedGoods(item : SalesVo) {
        mItem = item;
        showToast("设置成功")
        finish();
    }

    override fun executePageRequest(page: IPage) {
        mPresenter?.loadListData(catPath, page, mAdapter.data)
    }

    override fun autoRefresh(): Boolean {
        return false;
    }

}