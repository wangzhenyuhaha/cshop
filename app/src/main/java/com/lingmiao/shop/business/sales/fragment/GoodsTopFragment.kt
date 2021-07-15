package com.lingmiao.shop.business.sales.fragment

import android.content.Intent
import android.os.Bundle
import android.view.View
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.james.common.base.loadmore.BaseLoadMoreFragment
import com.james.common.base.loadmore.core.IPage
import com.lingmiao.shop.R
import com.lingmiao.shop.base.IConstant
import com.lingmiao.shop.business.sales.adapter.GoodsTopAdapter
import com.lingmiao.shop.business.sales.bean.SalesGoodsTop10
import com.lingmiao.shop.business.sales.presenter.GoodsTopPresenter
import com.lingmiao.shop.business.sales.presenter.impl.GoodsTopPreImpl
import java.io.Serializable

/**
Create Date : 2021/4/71:21 PM
Auther      : Fox
Desc        :
 **/
class GoodsTopFragment : BaseLoadMoreFragment<SalesGoodsTop10, GoodsTopPresenter>(), GoodsTopPresenter.PubView {

    companion object {

        fun new(): GoodsTopFragment {
            return newInstance(1, listOf());
        }

        fun newInstance(status: Int, list: List<SalesGoodsTop10?>?): GoodsTopFragment {
            return GoodsTopFragment().apply {
                val intent = Intent();
                intent.putExtra("_list", list as Serializable);
                intent.putExtra(IConstant.BUNDLE_KEY_OF_VIEW_TYPE, status);
                arguments = intent.extras;
            }
        }
    }

    private var status: Int? = null
    var list: List<SalesGoodsTop10?>? = null;

    override fun autoRefresh(): Boolean {
        return false;
    }

    override fun initBundles() {
        status = arguments?.getInt(IConstant.BUNDLE_KEY_OF_VIEW_TYPE)
        list = arguments?.getSerializable("_list") as List<SalesGoodsTop10?>?;
    }

    override fun getLayoutId(): Int? {
        return R.layout.sales_fragment_goods_top;
    }


    override fun initAdapter(): BaseQuickAdapter<SalesGoodsTop10, BaseViewHolder> {
        return GoodsTopAdapter().apply {

        }
    }

    override fun createPresenter(): GoodsTopPresenter? {
        return GoodsTopPreImpl(context!!, this);
    }

    override fun initOthers(rootView: View) {
        mSmartRefreshLayout.setEnableRefresh(false)
        mSmartRefreshLayout.setEnableLoadMore(false)

        mAdapter.replaceData(list!!);
    }

    /**
     * 执行分页请求
     */
    override fun executePageRequest(page: IPage) {
        mPresenter?.loadListData(page, mAdapter.data);
    }
}