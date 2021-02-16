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
 * Desc   : 商品分组管理(二级)页面
 */
class GroupManagerLv2Activity : BaseLoadMoreActivity<ShopGroupVO, GroupManagerPre>(),
    GroupManagerPre.GroupManagerView {

    companion object {
        const val KEY_LV1_GROUP_ID = "KEY_LV1_GROUP_ID"

        fun openActivity(context: Context, lv1GroupId: String?) {
            val intent = Intent(context, GroupManagerLv2Activity::class.java)
            intent.putExtra(KEY_LV1_GROUP_ID, lv1GroupId)
            context.startActivity(intent)
        }
    }

    private var lv1GroupId: String? = null

    override fun useEventBus(): Boolean {
        return true
    }

    override fun useBaseLayout(): Boolean {
        return false
    }

    override fun getLayoutId(): Int {
        return R.layout.goods_activity_group_manager
    }

    override fun initBundles() {
        lv1GroupId = intent.getStringExtra(KEY_LV1_GROUP_ID)
    }

    override fun createPresenter(): GroupManagerPre {
        return GroupManagerPreImpl(this)
    }

    override fun initOthers() {
        window.setBackgroundDrawable(null)
        mSmartRefreshLayout.setEnableRefresh(false)
        mSmartRefreshLayout.setEnableLoadMore(false)

        toolbarView.setTitleContent("二级分组")
        toolbarView.setRightListener("添加", R.color.color_3870EA) {
            GroupManagerEditActivity.openActivity(this, ShopGroupVO.LEVEL_2, lv1GroupId, null)
        }
    }

    override fun initAdapter(): GroupManagerAdapter {
        return GroupManagerAdapter(R.layout.goods_adapter_group_manager_v2).apply {
            setOnItemChildClickListener { adapter, view, position ->
                when (view.id) {
                    R.id.groupEditTv -> {
                        GroupManagerEditActivity.openActivity(
                            this@GroupManagerLv2Activity,
                            ShopGroupVO.LEVEL_2, null, mAdapter.getItem(position)
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
        // 根据一级groupId去获取二级分组数据
        mPresenter.loadLv2GoodsGroup(lv1GroupId)
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun handleRefreshEvent(event: GroupRefreshEvent) {
        if (event.isRefreshLevel2()) {
            mLoadMoreDelegate?.refresh()
        }
    }

}