package com.lingmiao.shop.business.sales.fragment

import android.os.Bundle
import android.view.View
import com.blankj.utilcode.util.ActivityUtils
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.james.common.base.loadmore.BaseLoadMoreFragment
import com.james.common.base.loadmore.core.IPage
import com.james.common.utils.DialogUtils
import com.james.common.utils.exts.gone
import com.james.common.utils.exts.singleClick
import com.james.common.utils.exts.visiable
import com.lingmiao.shop.R
import com.lingmiao.shop.business.sales.UserOrderDetailActivity
import com.lingmiao.shop.business.sales.adapter.UserAdapter
import com.lingmiao.shop.business.sales.bean.UserVo
import com.lingmiao.shop.business.sales.presenter.IUserStatusListPresenter
import com.lingmiao.shop.business.sales.presenter.impl.UserListOfAllPreImpl
import com.lingmiao.shop.business.sales.presenter.impl.UserListOfNewPreImpl
import com.lingmiao.shop.util.OtherUtils
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

    override fun getLayoutId(): Int {
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
                    R.id.userPhoneTv -> {
                        OtherUtils.goToDialApp(activity, item?.mobile)
                    }
                    R.id.userOrderDetailTv -> {
                        // ????????????
                        UserOrderDetailActivity.open(requireActivity(), item, 100);
                        //ActivityUtils.startActivity(UserOrderDetailActivity::class.java)
                    }
                    R.id.userPortraitTv -> {
                        // ????????????
                        DialogUtils.showMultInputDialog(requireActivity(), "????????????", "", "?????????????????????","??????", "??????",null) {
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
                    userBottomContainer.visiable();
                    userAllCountTv.gone();
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
        // ??????
        userAllCb.setOnCheckedChangeListener { buttonView, isChecked ->
            mAdapter?.data.forEach {
                it?.isChecked = isChecked;
            }
            mAdapter?.notifyDataSetChanged();
            setCheckedCount(getCheckedCount());
        };

        // ?????????
        userBatchSendMessageTv.singleClick {
            if(getCheckedCount() > 0) {

            }
        }
        // ??????
        userBatchSendSmsTv.singleClick {
            if(getCheckedCount() > 0) {

            }
        }
        // ??????
        userBatchOptCancelTv.setOnClickListener{
            userBottomContainer.gone();
            userAllCountTv.visiable();
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
        userCountTv.text = String.format("?????????%s???", count);
    }

    override fun createPresenter(): IUserStatusListPresenter? {
        return  when (status) {
            TYPE_ALL -> UserListOfAllPreImpl(requireActivity(), this)
            TYPE_NEW -> UserListOfNewPreImpl(requireActivity(), this)
            else -> null;
        }
    }

    override fun setUserListCount(count: Int?) {
        userAllCountTv.setText(String.format("?????????%s???", count))
    }

    override fun executePageRequest(page: IPage) {
        mPresenter?.loadListData(page, mAdapter.data);
    }
}