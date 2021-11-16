package com.lingmiao.shop.business.goods

import android.content.Context
import android.content.Intent
import android.view.MotionEvent
import androidx.core.content.ContextCompat
import com.blankj.utilcode.util.KeyboardUtils
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
import com.james.common.utils.exts.getViewText
import com.james.common.utils.exts.isNotEmpty
import com.lingmiao.shop.R
import com.lingmiao.shop.business.goods.api.GoodsRepository
import com.lingmiao.shop.util.GlideUtils
import kotlinx.android.synthetic.main.goods_activity_search.*
import kotlinx.coroutines.launch

interface GoodsCenterPre : BasePresenter {

    fun loadListData(page: IPage, list: List<*>, searchText: String)

    interface StatusView : BaseView, BaseLoadMoreView<Data> {
    }
}


class GoodsCenterPreImpl(var context: Context, var view: GoodsCenterPre.StatusView) :
    BasePreImpl(view), GoodsCenterPre {


    override fun loadListData(
        page: IPage,
        list: List<*>,
        searchText: String
    ) {
        if (searchText.isBlank()) {
            view.showToast("请输入搜索文字")
            return
        }
        mCoroutine.launch {
            if (list.isEmpty()) {
                view.showPageLoading()
            }

            val resp =
                GoodsRepository.loadGoodsListByNameFromCenter(page.getPageIndex(), goodsName =searchText)
            if (resp.isSuccess) {
                val goodsList = resp.data.data
                view.onLoadMoreSuccess(goodsList, goodsList.isNotEmpty())
            } else {
                view.onLoadMoreFailed()
            }
            view.hidePageLoading()
        }
    }


}

class CenterGoodsStatusAdapter :
    BaseQuickAdapter<Data, BaseViewHolder>(R.layout.goods_adapter_goods_simple) {

    override fun convert(helper: BaseViewHolder, goodsVO: Data?) {
        goodsVO?.apply {
            helper.setText(R.id.goodsNameTv, goods_name)
            helper.setText(R.id.goodsPriceTv, "售价：${price}")
            GlideUtils.setImageUrl1(helper.getView(R.id.goodsIv), thumbnail)
            helper.addOnClickListener(R.id.goodsCheckSubmit)
        }
    }
}

class GoodsSearchCenterActivity : BaseLoadMoreActivity<Data, GoodsCenterPre>(),
    GoodsCenterPre.StatusView {

    companion object {
        fun openActivity(context: Context) {
            context.startActivity(Intent(context, GoodsSearchCenterActivity::class.java))
        }
    }

    override fun getLayoutId() = R.layout.activity_goods_search_center

    override fun createPresenter() = GoodsCenterPreImpl(this, this)

    override fun useEventBus() = false

    override fun useLightMode() = false

    override fun autoRefresh() = false

    override fun initOthers() {
        mToolBarDelegate.setMidTitle("商品搜索")

        mSmartRefreshLayout.setEnableRefresh(true)
        mSmartRefreshLayout.setBackgroundColor(ContextCompat.getColor(this, R.color.color_ffffff))

        deleteIv.setOnClickListener {
            inputEdt.setText("")
        }
        searchTv.setOnClickListener {
            mLoadMoreDelegate?.refresh()
        }
    }


    override fun initAdapter(): CenterGoodsStatusAdapter {
        return CenterGoodsStatusAdapter().apply {
            //  val item = getItem(position) as CategoryVO

            setOnItemChildClickListener { _, view, position ->
                if (view.id == R.id.goodsCheckSubmit) {
                    context?.let { it1 ->
                        GoodsPublishNewActivity.openActivity(
                            it1,
                            getItem(position)?.goods_id.toString(),
                            true
                        )
                    }
                }
            }

        }
    }

    override fun executePageRequest(page: IPage) {
        mPresenter.loadListData(page, mAdapter.data, inputEdt.getViewText())
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

    override fun dispatchTouchEvent(ev: MotionEvent?): Boolean {
        if (ev?.action == MotionEvent.ACTION_DOWN) {
            // 当键盘未关闭时先拦截事件
            if (KeyboardUtils.isSoftInputVisible(context)) {
                KeyboardUtils.hideSoftInput(context)
            }
        }
        return super.dispatchTouchEvent(ev)
    }

}


data class CenterGoods(
    var `data`: List<Data> = listOf(),
    var data_total: Int = 0,
    var page_no: Int = 0,
    var page_size: Int = 0
)

data class Data(
    var brand_id: Any = Any(),
    var buy_count: Int = 0,
    var category_id: Int = 0,
    var create_time: Int = 0,
    var enable_quantity: Int = 0,
    var goods_gallery_list: List<GoodsGallery> = listOf(),
    var goods_id: Int = 0,
    var goods_name: String = "",
    var goods_status_mix: Int = 0,
    var goods_status_text: String = "",
    var goods_type: String = "",
    var is_auth: Int = 0,
    var is_global: Int = 0,
    var is_self: Int = 0,
    var market_enable: Int = 0,
    var pre_sort: Int = 0,
    var price: Double? = null,
    var priority: Int = 0,
    var quantity: Int = 0,
    var sn: String = "",
    var store: Any = Any(),
    var subsidy_rate: Int = 0,
    var supplier_name: String = "",
    var thumbnail: String = "",
    var under_message: Any = Any(),
    var up_goods_id: String = ""
)

data class GoodsGallery(
    var big: String = "",
    var goods_id: Int = 0,
    var img_id: Int = 0,
    var isdefault: Int = 0,
    var original: String = "",
    var small: String = "",
    var sort: Int = 0,
    var thumbnail: String = "",
    var tiny: Any = Any()
)