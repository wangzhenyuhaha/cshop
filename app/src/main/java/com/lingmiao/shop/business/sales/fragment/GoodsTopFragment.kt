package com.lingmiao.shop.business.sales.fragment

import android.os.Bundle
import android.view.View
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.james.common.base.loadmore.BaseLoadMoreFragment
import com.james.common.base.loadmore.core.IPage
import com.lingmiao.shop.R
import com.lingmiao.shop.base.IConstant
import com.lingmiao.shop.business.sales.adapter.GoodsTopAdapter
import com.lingmiao.shop.business.sales.bean.GoodsItem
import com.lingmiao.shop.business.sales.presenter.GoodsTopPresenter
import com.lingmiao.shop.business.sales.presenter.impl.GoodsTopPreImpl

/**
Create Date : 2021/4/71:21 PM
Auther      : Fox
Desc        :
 **/
class GoodsTopFragment : BaseLoadMoreFragment<GoodsItem, GoodsTopPresenter>(), GoodsTopPresenter.PubView {

    companion object {

        fun new(): GoodsTopFragment {
            return newInstance(1);
        }

        fun newInstance(status: Int): GoodsTopFragment {
            return GoodsTopFragment().apply {
                arguments = Bundle().apply {
                    putInt(IConstant.BUNDLE_KEY_OF_VIEW_TYPE, status)
                }
            }
        }
    }

    private var status: Int? = null

    override fun initBundles() {
        status = arguments?.getInt(IConstant.BUNDLE_KEY_OF_VIEW_TYPE)
    }

    override fun getLayoutId(): Int? {
        return R.layout.sales_fragment_goods_top;
    }


    override fun initAdapter(): BaseQuickAdapter<GoodsItem, BaseViewHolder> {
        return GoodsTopAdapter().apply {

        }
    }

    override fun createPresenter(): GoodsTopPresenter? {
        return GoodsTopPreImpl(context!!, this);
    }

    override fun initOthers(rootView: View) {
        mSmartRefreshLayout.setEnableRefresh(false)
        mSmartRefreshLayout.setEnableLoadMore(false)
    }

    /**
     * 执行分页请求
     */
    override fun executePageRequest(page: IPage) {
        mPresenter?.loadListData(page, mAdapter.data);
    }
}