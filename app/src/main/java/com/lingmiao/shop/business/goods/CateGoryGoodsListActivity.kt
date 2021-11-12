package com.lingmiao.shop.business.goods

import android.content.Context
import android.content.Intent
import android.view.View
import androidx.core.content.ContextCompat
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.james.common.base.BasePreImpl
import com.james.common.base.BasePresenter
import com.james.common.base.BaseView
import com.james.common.base.delegate.DefaultPageLoadingDelegate
import com.james.common.base.delegate.PageLoadingDelegate
import com.james.common.base.loadmore.BaseLoadMoreActivity
import com.james.common.base.loadmore.BaseLoadMoreView
import com.james.common.base.loadmore.core.IPage
import com.james.common.utils.exts.singleClick
import com.lingmiao.shop.R
import com.lingmiao.shop.business.goods.api.GoodsRepository
import com.lingmiao.shop.business.goods.api.bean.CategoryVO
import com.lingmiao.shop.business.goods.api.bean.GoodsVO
import com.lingmiao.shop.business.goods.presenter.impl.PopUserCatePreImpl
import com.lingmiao.shop.util.GlideUtils
import kotlinx.android.synthetic.main.activity_cate_gory_goods_list.*
import kotlinx.coroutines.launch


interface CateGoryGoodsPre : BasePresenter {

    fun loadListData(page: IPage, list: List<*>, searchText: String)

    fun showCategoryPop(cid: String?, target: View)

    interface StatusView : BaseView, BaseLoadMoreView<GoodsVO> {
        fun onUpdatedCategory(list: List<CategoryVO>?, name: String?)
    }
}


class CateGoryGoodsPreImpl(var context: Context, var view: CateGoryGoodsPre.StatusView) :
    BasePreImpl(view), CateGoryGoodsPre {

    private val mCatePopPreImpl: PopUserCatePreImpl by lazy { PopUserCatePreImpl(view) }

    override fun loadListData(
        page: IPage,
        list: List<*>,
        searchText: String
    ) {

        mCoroutine.launch {
            val resp = GoodsRepository.loadGoodsListOfCateId(
                page.getPageIndex(),
                null,
                GoodsVO.getEnableAuth(),
                null,
                searchText
            )
            if (resp.isSuccess) {
                val goodsList = resp.data.data
                view.onLoadMoreSuccess(goodsList, goodsList?.size ?: 0 >= 10)
            } else {
                view.onLoadMoreFailed()
            }
        }
    }

    override fun showCategoryPop(cid: String?, target: View) {
        mCatePopPreImpl.showGoodsGroupPop(context, cid) { its, name ->
            view.onUpdatedCategory(its, name)
        }
    }
}

class CateGoryGoodsAdapter :
    BaseQuickAdapter<GoodsVO, BaseViewHolder>(R.layout.goods_adapter_goods_simple) {

    override fun convert(helper: BaseViewHolder, goodsVO: GoodsVO?) {
        goodsVO?.apply {
            helper.setText(R.id.goodsNameTv, goodsName)
            helper.setText(R.id.goodsPriceTv, "售价：${price}")
            GlideUtils.setImageUrl1(helper.getView(R.id.goodsIv), thumbnail)
            helper.addOnClickListener(R.id.goodsCheckSubmit)
            helper.setGone(R.id.goodsCheckSubmit, false)
        }
    }
}

class CateGoryGoodsListActivity : BaseLoadMoreActivity<GoodsVO, CateGoryGoodsPre>(),
    CateGoryGoodsPre.StatusView {

    companion object {
        private const val KEY_CATEGORY_GOODS = "KEY_CATEGORY_GOODS"
        private const val KEY_CATEGORY_NAME = "KEY_CATEGORY_NAME"

        fun openActivity(context: Context, category: String, categoryName: String) {
            val intent = Intent(context, CateGoryGoodsListActivity::class.java)
            intent.putExtra(KEY_CATEGORY_GOODS, category)
            intent.putExtra(KEY_CATEGORY_NAME, categoryName)
            context.startActivity(intent)
        }
    }

    override fun getLayoutId() = R.layout.activity_cate_gory_goods_list

    override fun createPresenter() = CateGoryGoodsPreImpl(this, this)

    override fun useEventBus() = false

    override fun useLightMode() = false

    override fun autoRefresh() = false

    private var category: String = ""

    //分类名称
    private var categoryName: String = ""

    override fun initBundles() {
        super.initBundles()
        category = intent.getStringExtra(KEY_CATEGORY_GOODS) ?: ""
        categoryName = intent.getStringExtra(KEY_CATEGORY_NAME) ?: ""

    }

    override fun initOthers() {
        mToolBarDelegate.setMidTitle("分类商品")

        mSmartRefreshLayout.setEnableRefresh(true)
        mSmartRefreshLayout.setBackgroundColor(ContextCompat.getColor(this, R.color.color_ffffff))


    }

    override fun initView() {
        super.initView()

        menuCateL1Tv.text = categoryName
        mLoadMoreDelegate?.refresh()

        menuCateL1Tv.singleClick {
            mPresenter?.showCategoryPop("0", it)
        }
    }

    override fun onUpdatedCategory(list: List<CategoryVO>?, name: String?) {

        menuCateL1Tv.text = name





        if (list?.size ?: 0 > 0) {
            val item = list?.get(list.size - 1)
            category = item?.categoryPath ?: ""
            mLoadMoreDelegate?.refresh()
        }
    }

    override fun initAdapter(): CateGoryGoodsAdapter {
        return CateGoryGoodsAdapter().apply {
        }
    }

    override fun executePageRequest(page: IPage) {
        mPresenter.loadListData(page, mAdapter.data, category)
    }

    override fun getPageLoadingDelegate(): PageLoadingDelegate {
        if (mPageLoadingDelegate == null) {
            mPageLoadingDelegate = DefaultPageLoadingDelegate(this).apply {
                // 设置当前页面的 EmptyLayout
                setPageLoadingLayout(elEmpty)
            }
        }
        return mPageLoadingDelegate
    }


}

