package com.lingmiao.shop.business.goods

import android.content.Context
import android.content.Intent
import androidx.core.content.ContextCompat
import com.lingmiao.shop.R
import com.lingmiao.shop.business.goods.adapter.GroupManagerAdapter
import com.lingmiao.shop.business.goods.api.bean.ShopGroupVO
import com.lingmiao.shop.business.goods.event.GroupRefreshEvent
import com.lingmiao.shop.business.goods.presenter.GroupManagerPre
import com.lingmiao.shop.business.goods.presenter.impl.GroupManagerPreImpl
import com.james.common.base.loadmore.BaseLoadMoreActivity
import com.james.common.base.loadmore.core.IPage
import kotlinx.android.synthetic.main.goods_activity_group_manager.*
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode

/**
 * Author : Elson
 * Date   : 2020/7/18
 * Desc   : 商品分组管理(一级)页面
 */
class GroupManagerLv1Activity : BaseLoadMoreActivity<ShopGroupVO, GroupManagerPre>(),
    GroupManagerPre.GroupManagerView {

    companion object {
        fun openActivity(context: Context) {
            context.startActivity(Intent(context, GroupManagerLv1Activity::class.java))
        }
    }

    override fun useEventBus(): Boolean {
        return true
    }

    override fun useBaseLayout(): Boolean {
        return false
    }

    override fun getLayoutId(): Int {
        return R.layout.goods_activity_group_manager
    }

    override fun createPresenter(): GroupManagerPre {
        return GroupManagerPreImpl(this)
    }

    override fun initOthers() {
        window.setBackgroundDrawable(null)
        // 禁用上拉加载、下拉刷新
        mSmartRefreshLayout.setEnableRefresh(false)
        mSmartRefreshLayout.setEnableLoadMore(false)

        toolbarView.setTitleContent("分组管理")
        toolbarView.setRightListener("添加", R.color.color_3870EA) {
            GroupManagerEditActivity.openActivity(this, ShopGroupVO.LEVEL_1, null, null)
        }
    }

    override fun initAdapter(): GroupManagerAdapter {
        return GroupManagerAdapter(R.layout.goods_adapter_group_manager).apply {
            setOnItemClickListener { adapter, view, position ->
                GroupManagerLv2Activity.openActivity(
                    this@GroupManagerLv1Activity,
                    mAdapter.getItem(position)?.shopCatId
                )
            }
            setOnItemChildClickListener { adapter, view, position ->
                when (view.id) {
                    R.id.groupEditTv -> {
                        GroupManagerEditActivity.openActivity(
                            this@GroupManagerLv1Activity,
                            ShopGroupVO.LEVEL_1, null, mAdapter.getItem(position)
                        )
                    }
                    R.id.groupDeleteTv -> {
                        mPresenter.deleteGoodsGroup(mAdapter.getItem(position), position)
                    }
                }
            }
        }
    }

    override fun onDeleteGroupSuccess(position: Int) {
        mAdapter.remove(position)
    }

    override fun executePageRequest(page: IPage) {
        mPresenter.loadLv1GoodsGroup()
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun handleRefreshEvent(event: GroupRefreshEvent) {
        if (event.isRefreshLevel1()) {
            mLoadMoreDelegate?.refresh()
        }
    }
}