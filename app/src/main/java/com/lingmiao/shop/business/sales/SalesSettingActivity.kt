package com.lingmiao.shop.business.sales

import com.blankj.utilcode.util.ActivityUtils
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.james.common.base.loadmore.BaseLoadMoreActivity
import com.james.common.base.loadmore.core.IPage
import com.lingmiao.shop.R
import com.lingmiao.shop.business.sales.adapter.SalesAdapter
import com.lingmiao.shop.business.sales.bean.SalesVo
import com.lingmiao.shop.business.sales.presenter.ISalesSettingPresenter
import com.lingmiao.shop.business.sales.presenter.impl.SalesSettingPreImpl

/**
Create Date : 2021/3/101:08 AM
Auther      : Fox
Desc        :
 **/
class SalesSettingActivity : BaseLoadMoreActivity<SalesVo,ISalesSettingPresenter>(), ISalesSettingPresenter.PubView {
    override fun initAdapter(): BaseQuickAdapter<SalesVo, BaseViewHolder> {
        return SalesAdapter().apply {
            setOnItemChildClickListener { adapter, view, position ->
                when(view.id) {
                    R.id.salesEditTv -> {
                        ActivityUtils.startActivity(SalesActivityEditActivity::class.java);
                    }
                }
            }
            setOnItemClickListener { adapter, view, position ->

            }
        }
    }

    override fun createPresenter(): ISalesSettingPresenter {
        return SalesSettingPreImpl(this, this);
    }

    override fun useLightMode(): Boolean {
        return false
    }

    override fun initOthers() {
        mToolBarDelegate.setMidTitle(getString(R.string.sales_setting_title))
    }

    override fun autoRefresh(): Boolean {
        return true;
    }
    /**
     * 执行分页请求
     */
    override fun executePageRequest(page: IPage) {
        mPresenter?.loadListData(page, mAdapter.data);
    }
}