package com.lingmiao.shop.business.goods.fragment

import android.view.View
import androidx.fragment.app.activityViewModels
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.james.common.base.loadmore.BaseLoadMoreFragment
import com.james.common.base.loadmore.core.IPage
import com.lingmiao.shop.R
import com.lingmiao.shop.business.goods.MenuGoodsManagerViewModel
import com.lingmiao.shop.business.goods.adapter.GoodsMenuNewAdapter
import com.lingmiao.shop.business.goods.api.bean.GoodsVO
import com.lingmiao.shop.business.goods.presenter.GoodsMenuInfoPre
import com.lingmiao.shop.business.goods.presenter.impl.GoodsMenuInfoPreImpl
import com.lingmiao.shop.widget.EmptyView

class GoodsMenuFragment : BaseLoadMoreFragment<GoodsVO, GoodsMenuInfoPre>(), GoodsMenuInfoPre.View {


    private val model by activityViewModels<MenuGoodsManagerViewModel>()

    override fun createPresenter() = GoodsMenuInfoPreImpl(requireContext(), this)

    override fun initViewsAndData(rootView: View) {
        super.initViewsAndData(rootView)
        model.item.observe(requireActivity(), {
            mLoadMoreDelegate?.refresh()
        })
    }

    override fun getLayoutId() = R.layout.fragment_white_load_more

    override fun initAdapter(): BaseQuickAdapter<GoodsVO, BaseViewHolder> {
        return GoodsMenuNewAdapter().apply {
            //menuIv
            setOnItemChildClickListener { _, view, position ->
                val item = mAdapter.getItem(position) as GoodsVO
                if (view.id == R.id.menuIv) {
                    mPresenter?.clickMenuView(model.item.value?.isTop ?: 0, item, position, view)
                }
            }

            emptyView = EmptyView(mContext).apply {
                setBackgroundResource(R.color.common_bg)
            }
        }
    }


    override fun executePageRequest(page: IPage) {
        showPageLoading()
        val temp = if (model.item.value?.isButton == true) {
            //选中了全部
            model.savedItem.value
        } else {
            //
            model.item.value
        }
        mPresenter?.loadListData(
            temp?.catPath,
            page,
            mAdapter.data,
            temp?.isEvent
        )
        //顺便更新一下菜单
        model.setRefreshMenu(1)
    }

    override fun onUpdatedGoodsGroup() {
        mLoadMoreDelegate?.refresh()
    }

    override fun onLoadMoreSuccess(list: List<GoodsVO>?, hasMore: Boolean) {
        hidePageLoading()
        super.onLoadMoreSuccess(list, hasMore)
        if (mAdapter.data.size == 0) {
            showReason("没有商品")
        }
    }

    override fun onResume() {
        super.onResume()
        mLoadMoreDelegate?.refresh()
    }
}