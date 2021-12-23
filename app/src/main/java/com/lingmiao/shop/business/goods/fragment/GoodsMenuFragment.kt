package com.lingmiao.shop.business.goods.fragment

import android.util.Log
import android.view.View
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.james.common.base.loadmore.BaseLoadMoreFragment
import com.james.common.base.loadmore.core.IPage
import com.lingmiao.shop.R
import com.lingmiao.shop.business.goods.MenuGoodsManagerViewModel
import com.lingmiao.shop.business.goods.adapter.GoodsMenuAdapter
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
        model.item.observe(requireActivity(), Observer {
            mLoadMoreDelegate?.refresh()
        })
    }

    override fun initAdapter(): BaseQuickAdapter<GoodsVO, BaseViewHolder> {
        return GoodsMenuNewAdapter().apply {
            //menuIv
            setOnItemChildClickListener { _, view, position ->
                var item = mAdapter.getItem(position) as GoodsVO
//                if (view.id == R.id.menuIv) {
//                    mPresenter?.clickMenuView(mItem?.isTop!!, item, position, view)
//                }
            }
            setOnItemClickListener { _, _, position ->
                var item = mAdapter.getItem(position) as GoodsVO
            }
            emptyView = EmptyView(mContext).apply {
                setBackgroundResource(R.color.common_bg)
            }
        }
    }


    override fun executePageRequest(page: IPage) {
        mPresenter?.loadListData(model.item.value?.catPath, page, mAdapter.data)
    }

}