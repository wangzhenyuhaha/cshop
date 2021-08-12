package com.lingmiao.shop.business.me

import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.james.common.base.list.BaseVBListActivity
import com.james.common.base.loadmore.core.IPage
import com.lingmiao.shop.R
import com.lingmiao.shop.business.me.adapter.HelpDocAdapter
import com.lingmiao.shop.business.me.bean.HelpDocItemVo
import com.lingmiao.shop.business.me.presenter.HelpDocPresenter
import com.lingmiao.shop.business.me.presenter.impl.HelpDocPresenterImpl
import com.lingmiao.shop.databinding.MeActivityHelpDocBinding
import com.lingmiao.shop.widget.EmptyView

/**
Create Date : 2021/8/1011:22 下午
Auther      : Fox
Desc        :
 **/
class HelpDocActivity : BaseVBListActivity<HelpDocItemVo, MeActivityHelpDocBinding, HelpDocPresenter>(), HelpDocPresenter.View {

    override fun createPresenter(): HelpDocPresenter {
        return HelpDocPresenterImpl(this);
    }

    override fun getViewBinding(): MeActivityHelpDocBinding {
        return MeActivityHelpDocBinding.inflate(layoutInflater)
    }

    override fun useBaseLayout(): Boolean {
        return true;
    }

    override fun useLightMode(): Boolean {
        return false;
    }

    override fun initOthers() {
        mToolBarDelegate?.setMidTitle("帮助中心")
    }

    override fun initAdapter(): BaseQuickAdapter<HelpDocItemVo, BaseViewHolder> {
        return HelpDocAdapter().apply {
            setOnItemChildClickListener { adapter, view, position ->
            }
            setOnItemClickListener { adapter, view, position ->
            }
            emptyView = EmptyView(context!!).apply {
                setBackgroundResource(R.color.common_bg)
            }
        };
    }

    /**
     * 执行分页请求
     */
    override fun executePageRequest(page: IPage) {
        mPresenter?.queryContentList(page, "1", mAdapter.data);
    }

}