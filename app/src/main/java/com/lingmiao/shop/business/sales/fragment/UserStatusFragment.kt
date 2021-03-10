package com.lingmiao.shop.business.sales.fragment

import android.os.Bundle
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.james.common.base.loadmore.BaseLoadMoreFragment
import com.james.common.base.loadmore.core.IPage
import com.lingmiao.shop.business.sales.adapter.UserAdapter
import com.lingmiao.shop.business.sales.bean.UserVo
import com.lingmiao.shop.business.sales.presenter.IUserStatusListPresenter
import com.lingmiao.shop.business.sales.presenter.impl.UserStatusListPreImpl

/**
Create Date : 2021/3/101:14 AM
Auther      : Fox
Desc        :
 **/
class UserStatusFragment : BaseLoadMoreFragment<UserVo, IUserStatusListPresenter>(), IUserStatusListPresenter.PubView{

    companion object {
        const val KEY_TYPE = "KEY_TYPE"
        const val TYPE_ALL = 0
        const val TYPE_NEW = 1


        fun new(): UserStatusFragment {
            return newInstance(TYPE_NEW);
        }

        fun all(): UserStatusFragment {
            return newInstance(TYPE_ALL);
        }

        fun newInstance(status: Int): UserStatusFragment {
            return UserStatusFragment().apply {
                arguments = Bundle().apply {
                    putInt(KEY_TYPE, status)
                }
            }
        }
    }

    private var status: Int? = null

    override fun initBundles() {
        status = arguments?.getInt(KEY_TYPE)
    }

    override fun initAdapter(): BaseQuickAdapter<UserVo, BaseViewHolder> {
        return UserAdapter().apply {
            setOnItemClickListener { adapter, view, position ->
            }
            setOnItemChildClickListener { adapter, view, position ->
            }
        }
    }

    override fun createPresenter(): IUserStatusListPresenter? {
        return UserStatusListPreImpl(context!!, this);
    }

    override fun executePageRequest(page: IPage) {
        mPresenter?.loadListData(page, mAdapter.data);
    }
}