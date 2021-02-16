package com.lingmiao.shop.business.tools.fragment

import android.app.Activity
import android.os.Bundle
import android.view.View
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.lingmiao.shop.R
import com.lingmiao.shop.base.IConstant
import com.lingmiao.shop.business.tools.EditFreightModelActivity
import com.lingmiao.shop.business.tools.adapter.FreightModelAdapter
import com.lingmiao.shop.business.tools.bean.FreightVoItem
import com.lingmiao.shop.business.tools.presenter.FreightModelPresenter
import com.lingmiao.shop.business.tools.presenter.impl.FreightModelPresenterImpl
import com.lingmiao.shop.widget.EmptyView
import com.james.common.base.loadmore.BaseLoadMoreFragment
import com.james.common.base.loadmore.core.IPage
import com.james.common.utils.DialogUtils

class FreightModelFragment : BaseLoadMoreFragment<FreightVoItem, FreightModelPresenter>() , FreightModelPresenter.View{

    companion object {
        fun newInstance(): FreightModelFragment {
            return FreightModelFragment().apply {
                arguments = Bundle().apply {
                    putInt(IConstant.BUNDLE_KEY_OF_VIEW_TYPE,  1)
                }
            }
        }
    }

    override fun createPresenter(): FreightModelPresenter? {
        return FreightModelPresenterImpl(this);
    }

    override fun initAdapter(): BaseQuickAdapter<FreightVoItem, BaseViewHolder> {
        return FreightModelAdapter().apply {
            setOnItemChildClickListener { adapter, view, position ->
                val item = adapter.getItem(position) as FreightVoItem;
                if(view.id == R.id.tv_model_edit) {
                    EditFreightModelActivity.edit(context!!, item?.id);
                } else if(view.id == R.id.tv_model_delete) {
                    DialogUtils.showDialog(
                        context as Activity, "删除提示", "删除后不可恢复，确定要删除吗？",
                        "取消", "确定删除", View.OnClickListener { }, View.OnClickListener {
                            mPresenter?.onDeleteItem(item, position);
                        })
                }
            }
            setOnItemClickListener { adapter, view, position ->

            }
            emptyView = EmptyView(mContext).apply {
                setBackgroundResource(R.color.common_bg)
            }
        };
    }

    override fun onLoadMoreSuccess(list: List<FreightVoItem>?, hasMore: Boolean) {
        mAdapter?.replaceData(list ?: arrayListOf())
        mLoadMoreDelegate?.loadFinish(false, true);
    }

    override fun executePageRequest(page: IPage) {
        mPresenter?.loadList(page, mAdapter.data);
    }

    override fun onModelDeleted(position: Int) {
        mAdapter.remove(position);
    }

}