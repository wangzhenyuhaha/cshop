package com.lingmiao.shop.business.tuan.fragment

import android.os.Bundle
import com.blankj.utilcode.util.ActivityUtils
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.lingmiao.shop.R
import com.lingmiao.shop.base.IConstant
import com.lingmiao.shop.business.tuan.ActivityViewActivity
import com.lingmiao.shop.business.tuan.GoodsSubmitActivity
import com.lingmiao.shop.business.tuan.adapter.ActivityAdapter
import com.lingmiao.shop.business.tuan.bean.ActivityVo
import com.lingmiao.shop.business.tuan.presenter.ActivityListPresenter
import com.lingmiao.shop.business.tuan.presenter.impl.ActivityListPresenterImpl
import com.lingmiao.shop.widget.EmptyView
import com.james.common.base.loadmore.BaseLoadMoreFragment
import com.james.common.base.loadmore.core.IPage

class ActivityListFragment : BaseLoadMoreFragment<ActivityVo, ActivityListPresenter>(), ActivityListPresenter.View {

    companion object {
        val TYPE_MY_ACTIVITIES = 2;
        val TYPE_ALL_ACTIVITIES = 1;
        fun myActivities() : ActivityListFragment {
            return ActivityListFragment().apply {
                arguments = Bundle().apply {
                    putInt(IConstant.BUNDLE_KEY_OF_VIEW_TYPE, TYPE_MY_ACTIVITIES);
                }
            }
        }

        fun allActivities() : ActivityListFragment {
            return ActivityListFragment().apply {
                arguments = Bundle().apply {
                    putInt(IConstant.BUNDLE_KEY_OF_VIEW_TYPE, TYPE_ALL_ACTIVITIES);
                }
            }
        }
    }

    override fun initAdapter(): BaseQuickAdapter<ActivityVo, BaseViewHolder> {
        return ActivityAdapter().apply{
            setOnItemChildClickListener { adapter, view, position ->
                if(R.id.tv_activity_check == view.id) {
                    ActivityUtils.startActivity(ActivityViewActivity::class.java);
                } else if(R.id.tv_activity_status == view.id) {
                    ActivityUtils.startActivity(GoodsSubmitActivity::class.java);
                }
            }
            setOnItemClickListener { adapter, view, position ->

            }
            emptyView = EmptyView(mContext).apply {
                setBackgroundResource(R.color.common_bg)
            }
        }
    }

    override fun createPresenter(): ActivityListPresenter? {
        return ActivityListPresenterImpl(this);
    }

    override fun executePageRequest(page: IPage) {
        mPresenter?.loadList(page, mAdapter.data);
    }
}