package com.lingmiao.shop.business.goods.fragment

import android.os.Bundle
import android.view.View
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.james.common.base.loadmore.BaseLoadMoreFragment
import com.james.common.base.loadmore.core.IPage
import com.james.common.utils.DialogUtils
import com.lingmiao.shop.R
import com.lingmiao.shop.business.goods.GroupManagerEditActivity
import com.lingmiao.shop.business.goods.MenuEditActivity
import com.lingmiao.shop.business.goods.adapter.UsedMenuAdapter
import com.lingmiao.shop.business.goods.api.bean.MenuVo
import com.lingmiao.shop.business.goods.api.bean.ShopGroupVO
import com.lingmiao.shop.business.goods.presenter.GroupManagerPre
import com.lingmiao.shop.business.goods.presenter.UsedMenuPre
import com.lingmiao.shop.business.goods.presenter.impl.GroupManagerPreImpl
import com.lingmiao.shop.business.goods.presenter.impl.UsedMenuPreImpl
import com.lingmiao.shop.business.me.bean.ShopManageRequest
import kotlinx.android.synthetic.main.goods_fragment_goods_used_menu.*

/**
Create Date : 2021/3/101:00 AM
Auther      : Fox
Desc        :
 **/
class UsedMenuFragment : BaseLoadMoreFragment<MenuVo, UsedMenuPre>(), UsedMenuPre.PubView {

    companion object {

        fun newInstance(): UsedMenuFragment {
            return UsedMenuFragment().apply {
//                arguments = Bundle().apply {
//                    putInt(id, goodsStatus)
//                }
            }
        }
    }

    override fun getLayoutId(): Int? {
        return R.layout.goods_fragment_goods_used_menu;
    }

    override fun createPresenter(): UsedMenuPre? {
        return UsedMenuPreImpl(this);
    }

    override fun initAdapter(): BaseQuickAdapter<MenuVo, BaseViewHolder> {
        return UsedMenuAdapter(mutableListOf()).apply {
            setOnItemClickListener { adapter, view, position ->
//                GroupManagerLv2Activity.openActivity(
//                    this@GroupManagerLv1Activity,
//                    mAdapter.getItem(position)?.shopCatId
//                )
            }
            setOnItemChildClickListener { adapter, view, position ->
                when (view.id) {
                    R.id.groupEditTv -> {
//                        GroupManagerEditActivity.openActivity(
//                            activity!!,
//                            ShopGroupVO.LEVEL_1, null, mAdapter.getItem(position)
//                        )
                    }
                    R.id.groupDeleteTv -> {
                        mPresenter?.deleteGoodsGroup(mAdapter.getItem(position), position)
                    }
                }
            }
        }
    }

    override fun initOthers(rootView: View) {
        menuAddTv.setOnClickListener {
            DialogUtils.showInputDialog(activity!!, "菜单名称", "", "请输入","取消", "保存",null) {
//                tvShopManageSlogan.text = it;

            }
        }
        menuCancelTv.setOnClickListener {
            menuBottom.visibility = View.GONE;
            menuAllCheckCb.isChecked = false;
            var list = mAdapter?.data?.filter { it?.isChecked == true };
            if(list?.size > 0) {
                list?.forEachIndexed { index, goodsVO ->
                    goodsVO.isChecked = false;
                }
            }
            (mAdapter as UsedMenuAdapter)?.setBatchEditModel(false);
        }
        menuAllCheckCb.setOnCheckedChangeListener { buttonView, isChecked ->
            mAdapter?.data?.forEachIndexed { index, goodsVO ->
                goodsVO.isChecked = isChecked;
            }
            mAdapter?.notifyDataSetChanged();
        }
        menuDeleteTv.setOnClickListener {
            mLoadMoreDelegate?.refresh()
        }

    }
    override fun onDeleteGroupSuccess(position: Int) {

    }

    override fun executePageRequest(page: IPage) {
        mPresenter?.loadLv1GoodsGroup()
    }
}