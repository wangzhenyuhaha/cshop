package com.lingmiao.shop.business.sales.fragment

import android.os.Bundle
import android.view.View
import com.blankj.utilcode.util.ActivityUtils
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.james.common.base.loadmore.BaseLoadMoreFragment
import com.james.common.base.loadmore.core.IPage
import com.james.common.utils.DialogUtils
import com.james.common.utils.exts.singleClick
import com.lingmiao.shop.R
import com.lingmiao.shop.business.sales.UserOrderDetailActivity
import com.lingmiao.shop.business.sales.adapter.UserAdapter
import com.lingmiao.shop.business.sales.bean.UserVo
import com.lingmiao.shop.business.sales.presenter.IUserStatusListPresenter
import com.lingmiao.shop.business.sales.presenter.impl.UserListOfAllPreImpl
import com.lingmiao.shop.business.sales.presenter.impl.UserListOfNewPreImpl
import com.lingmiao.shop.widget.EmptyView
import kotlinx.android.synthetic.main.sales_fragment_user_list.*

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

    override fun getLayoutId(): Int? {
        return R.layout.sales_fragment_user_list;
    }

    override fun initAdapter(): BaseQuickAdapter<UserVo, BaseViewHolder> {
        return UserAdapter().apply {
            setOnItemClickListener { adapter, view, position ->
                var item = mAdapter.getItem(position) as UserVo;
            }
            setOnItemChildClickListener { adapter, view, position ->
                var item = mAdapter.getItem(position) as UserVo;
                when(view.id) {
                    R.id.userOrderDetailTv -> {
                        // 查看订单
                        ActivityUtils.startActivity(UserOrderDetailActivity::class.java)
                    }
                    R.id.userPortraitTv -> {
                        // 用户画像
                        DialogUtils.showMultInputDialog(activity!!, "用户画像", "", "请输入用户画像","取消", "确定",null) {
//                            tvShopManageDesc.text = it
//                            showDialogLoading()
//                            val request = ShopManageRequest()
//                            val loginInfo = UserManager.getLoginInfo()
//                            loginInfo?.let { info-> request.shopId = info.shopId }
//                            request.shopDesc = it
//                            mPresenter.updateShopManage(request)
                        }
                    }
                    R.id.userCheckCb -> {
                        item?.isChecked = !(item?.isChecked!!);
                        setCheckedCount(getCheckedCount());
                    }
                }
            }
            onItemLongClickListener = BaseQuickAdapter.OnItemLongClickListener { adapter, view, position -> Boolean
                if(userBottomContainer.visibility != View.VISIBLE) {
                    userBottomContainer.visibility = View.VISIBLE;
                }
                setBatchEditModel(true);
                return@OnItemLongClickListener true;
            }
            emptyView = EmptyView(mContext).apply {
                setBackgroundResource(R.color.color_ffffff)
            }
        }
    }

    override fun initOthers(rootView: View) {
        // 全选
        userAllCb.setOnCheckedChangeListener { buttonView, isChecked ->
            mAdapter?.data.forEach {
                it?.isChecked = isChecked;
            }
            mAdapter?.notifyDataSetChanged();
            setCheckedCount(getCheckedCount());
        };

        // 站内信
        userBatchSendMessageTv.singleClick {
            if(getCheckedCount() > 0) {

            }
        }
        // 短信
        userBatchSendSmsTv.singleClick {
            if(getCheckedCount() > 0) {

            }
        }
        // 取消
        userBatchOptCancelTv.setOnClickListener{
            userBottomContainer.visibility = View.GONE;
            userAllCb.isChecked = false;
            var list = mAdapter?.data?.filter { it?.isChecked == true };
            if(list?.size > 0) {
                list?.forEachIndexed { index, item ->
                    item.isChecked = false;
                }
            }
            (mAdapter as UserAdapter)?.setBatchEditModel(false);
        }
    }

    fun getCheckedCount() : Int {
        return mAdapter?.data?.filter { it?.isChecked == true }?.size;
    }

    fun setCheckedCount(count : Int) {
        userCountTv.text = String.format("已选择%s人", count);
    }

    override fun createPresenter(): IUserStatusListPresenter? {
        return  when (status) {
            TYPE_ALL -> UserListOfAllPreImpl(context!!, this)
            TYPE_NEW -> UserListOfNewPreImpl(context!!, this)
            else -> null;
        }
    }

    override fun executePageRequest(page: IPage) {
        mPresenter?.loadListData(page, mAdapter.data);
    }
}