package com.lingmiao.shop.business.sales

import android.annotation.SuppressLint
import android.app.Activity
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.james.common.base.loadmore.BaseLoadMoreActivity
import com.james.common.base.loadmore.core.IPage
import com.james.common.utils.exts.singleClick
import com.lingmiao.shop.R
import com.lingmiao.shop.business.goods.adapter.GoodsMenuNewAdapter
import com.lingmiao.shop.business.goods.adapter.GoodsSelectAdapter
import com.lingmiao.shop.business.goods.api.bean.CategoryVO
import com.lingmiao.shop.business.goods.api.bean.GoodsVO
import com.lingmiao.shop.business.sales.adapter.GoodsSelectOneAdapter
import com.lingmiao.shop.business.sales.presenter.SelectGoodsPresenter
import com.lingmiao.shop.business.sales.presenter.impl.SelectGoodsPreImpl
import com.lingmiao.shop.databinding.ActivitySelectGoodsBinding
import com.lingmiao.shop.widget.EmptyView
import kotlinx.android.synthetic.main.sales_activity_goods.*

@SuppressLint("NotifyDataSetChanged")
class SelectGoodsActivity : BaseLoadMoreActivity<GoodsVO, SelectGoodsPresenter>(),
    SelectGoodsPresenter.View {


    override fun createPresenter() = SelectGoodsPreImpl(this, this)

    override fun useLightMode() = false

    private var catPath: String? = null

    //选中的商品名称
    private var goodsID: String? = null

    //选中的商品ID
    private var goodsName: String? = null

    override fun initOthers() {

        mToolBarDelegate.setMidTitle(getString(R.string.sales_goods_title))

        menuCateL1Tv.singleClick {
            mPresenter?.showCategoryPop("0", it)
        }


        submitTv.singleClick {
            intent.putExtra("goods_id", goodsID)
            intent.putExtra("goods_name", goodsName)
            setResult(Activity.RESULT_OK, intent)
            finish()

        }
    }

    override fun getLayoutId() = R.layout.activity_select_goods

    override fun initAdapter(): BaseQuickAdapter<GoodsVO, BaseViewHolder> {
        return GoodsSelectOneAdapter().apply {
            //menuIv
            setOnItemChildClickListener { adapter, view, position ->
                val item = mAdapter.getItem(position) as GoodsVO;
                if (view.id == R.id.menuIv) {
                    setItem(item.goodsId)
                    goodsID = item.goodsId
                    goodsName = item.goodsName
                    adapter.notifyDataSetChanged()
                }
            }
            emptyView = EmptyView(context).apply {
                setBackgroundResource(R.color.common_bg)
            }
        }
    }

    override fun executePageRequest(page: IPage) {
        mPresenter?.loadListData(catPath, page, mAdapter.data)
    }

    override fun onUpdatedCategory(list: List<CategoryVO>?, name: String?) {
        menuCateL1Tv.text = name

        if (list?.size ?: 0 > 0) {
            val item: CategoryVO? = list?.last()
            catPath = item?.categoryPath
            mLoadMoreDelegate?.refresh()
        }

    }

}