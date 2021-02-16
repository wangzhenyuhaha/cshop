package com.lingmiao.shop.business.tools.fragment

import android.os.Bundle
import android.view.View
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.lingmiao.shop.R
import com.lingmiao.shop.base.IConstant
import com.lingmiao.shop.business.tools.adapter.ExpressCompanyAdapter
import com.lingmiao.shop.business.tools.bean.ExpressCompanyVo
import com.lingmiao.shop.business.tools.presenter.ExpressCompanyPresenter
import com.lingmiao.shop.business.tools.presenter.impl.ExpressCompanyPresenterImpl
import com.lingmiao.shop.widget.EmptyView
import com.james.common.base.loadmore.BaseLoadMoreFragment
import com.james.common.base.loadmore.core.IPage

class ExpressCompanyFragment : BaseLoadMoreFragment<ExpressCompanyVo, ExpressCompanyPresenter>(), ExpressCompanyPresenter.View {

    companion object {
        fun newInstance(): ExpressCompanyFragment {
            return ExpressCompanyFragment().apply {
                arguments = Bundle().apply {
                    putInt(IConstant.BUNDLE_KEY_OF_VIEW_TYPE, 1)
                }
            }
        }
    }

    override fun getLayoutId(): Int? {
        return R.layout.tools_fragment_express_company;
    }

    override fun initAdapter(): BaseQuickAdapter<ExpressCompanyVo, BaseViewHolder> {
        return ExpressCompanyAdapter().apply {
            setOnItemChildClickListener { adapter, view, position ->
                val item = adapter.data[position] as ExpressCompanyVo;
                if(view.id == R.id.sb_company_status_edit) {
                    mPresenter?.clickItemView(item, position);
                }
            }
            setOnItemClickListener { adapter, view, position ->

            }
            emptyView = EmptyView(mContext).apply {
                setBackgroundResource(R.color.common_bg)
            }
            addHeaderView(
                View.inflate(context, R.layout.tools_view_express_company, null)
            )
        }
    }

    override fun createPresenter(): ExpressCompanyPresenter? {
        return ExpressCompanyPresenterImpl(this);
    }

    override fun executePageRequest(page: IPage) {
        mPresenter?.loadList(page, mAdapter.data);
    }

    override fun onStatusEdit(flag: Boolean?, position: Int) {
        (mAdapter as ExpressCompanyAdapter).onStatusEdit(flag, position)
    }

    override fun onLoadMoreSuccess(list: List<ExpressCompanyVo>?, hasMore: Boolean) {
        mAdapter?.replaceData(list ?: arrayListOf())
        mLoadMoreDelegate?.loadFinish(false, true);
    }

}