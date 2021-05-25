package com.lingmiao.shop.business.goods.fragment

import android.os.Bundle
import android.view.View
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.james.common.base.loadmore.BaseLoadMoreFragment
import com.james.common.base.loadmore.core.IPage
import com.james.common.utils.DialogUtils
import com.lingmiao.shop.R
import com.lingmiao.shop.business.goods.GoodsManagerActivity
import com.lingmiao.shop.business.goods.GroupManagerEditActivity
import com.lingmiao.shop.business.goods.MenuEditActivity
import com.lingmiao.shop.business.goods.adapter.UsedMenuAdapter
import com.lingmiao.shop.business.goods.api.bean.MenuVo
import com.lingmiao.shop.business.goods.api.bean.ShopGroupVO
import com.lingmiao.shop.business.goods.event.MenuEvent
import com.lingmiao.shop.business.goods.presenter.GroupManagerPre
import com.lingmiao.shop.business.goods.presenter.UsedMenuPre
import com.lingmiao.shop.business.goods.presenter.impl.GroupManagerPreImpl
import com.lingmiao.shop.business.goods.presenter.impl.UsedMenuPreImpl
import com.lingmiao.shop.business.me.bean.ShopManageRequest
import kotlinx.android.synthetic.main.goods_fragment_goods_top_menu.*
import kotlinx.android.synthetic.main.goods_fragment_goods_used_menu.*
import kotlinx.android.synthetic.main.goods_fragment_goods_used_menu.menuAddTv
import kotlinx.android.synthetic.main.goods_fragment_goods_used_menu.menuAllCheckCb
import kotlinx.android.synthetic.main.goods_fragment_goods_used_menu.menuBottom
import kotlinx.android.synthetic.main.goods_fragment_goods_used_menu.menuCancelTv
import kotlinx.android.synthetic.main.goods_fragment_goods_used_menu.menuDeleteTv
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode

/**
Create Date : 2021/3/101:00 AM
Auther      : Fox
Desc        :
 **/
class UsedMenuFragment : BaseLoadMoreFragment<ShopGroupVO, UsedMenuPre>(), UsedMenuPre.PubView {

    private var list : MutableList<ShopGroupVO>? = mutableListOf();

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

    override fun initAdapter(): BaseQuickAdapter<ShopGroupVO, BaseViewHolder> {
        return UsedMenuAdapter(list).apply {
            setOnItemClickListener { adapter, view, position ->

//                GroupManagerLv2Activity.openActivity(
//                    this@GroupManagerLv1Activity,
//                    mAdapter.getItem(position)?.shopCatId
//                )
            }
            setOnItemChildClickListener { adapter, view, position ->
                when (view.id) {
                    R.id.menuAddGoodsIv -> {

                        GoodsManagerActivity.menu(context!!, "1");

                    }
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
            onItemLongClickListener = BaseQuickAdapter.OnItemLongClickListener { adapter, view, position -> Boolean
                if(menuBottom.visibility != View.VISIBLE) {
                    menuBottom.visibility = View.VISIBLE;
                }
                setBatchEditModel(true);
                return@OnItemLongClickListener true;
            }
        }
    }

    override fun initOthers(rootView: View) {
        menuAddTv.setOnClickListener {
            DialogUtils.showInputDialog(activity!!, "菜单名称", "", "请输入","取消", "保存",null) {
//                tvShopManageSlogan.text = it;


//                var item = ShopGroupVO();
//                item.showLevel = 0;
//                item.shopCatName = it;

                mPresenter?.addGroup(it, 0);

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

        mSmartRefreshLayout?.setEnableLoadMore(false);
    }
    override fun onDeleteGroupSuccess(position: Int) {

    }

    override fun onGroupAdded(item: ShopGroupVO) {

        list?.add(item);
        mAdapter?.replaceData(list!!);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun refreshShopStatus(event: MenuEvent) {
        var newItem = ShopGroupVO();
        newItem.shopCatName = "可乐"
        newItem.showLevel = 1;

        list?.add(newItem);
        mAdapter?.replaceData(list!!);
    }


    override fun executePageRequest(page: IPage) {
        mPresenter?.loadLv1GoodsGroup()
    }

    override fun useEventBus(): Boolean {
        return true;
    }

    override fun onLoadMoreSuccess(list: List<ShopGroupVO>?, hasMore: Boolean) {
        super.onLoadMoreSuccess(list, hasMore)
        mLoadMoreDelegate?.loadFinish(false, !list.isNullOrEmpty())
    }

}