package com.lingmiao.shop.business.goods

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.MotionEvent
import android.view.View
import android.widget.CompoundButton
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.LayoutRes
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
import com.james.common.utils.exts.check
import com.james.common.utils.exts.getViewText
import com.james.common.utils.exts.isNotEmpty
import com.lingmiao.shop.R
import com.lingmiao.shop.business.goods.adapter.GoodsSearchAdapter
import com.lingmiao.shop.business.goods.adapter.GoodsStatusAdapter
import com.lingmiao.shop.business.goods.api.GoodsRepository
import com.lingmiao.shop.business.goods.api.bean.GoodsVO
import com.lingmiao.shop.business.goods.config.GoodsConfig
import com.lingmiao.shop.business.goods.event.RefreshGoodsStatusEvent
import com.lingmiao.shop.business.goods.pop.GoodsMenuPop
import com.lingmiao.shop.business.goods.pop.StatusMenuPop
import com.lingmiao.shop.business.goods.presenter.GoodsSearchPre
import com.lingmiao.shop.business.goods.presenter.impl.GoodsMenuPreImpl
import com.lingmiao.shop.business.goods.presenter.impl.GoodsSearchPreImpl
import com.lingmiao.shop.business.goods.presenter.impl.SearchStatusPreImpl
import com.lingmiao.shop.business.tools.adapter.setOnCheckedChangeListener
import com.lingmiao.shop.util.GlideUtils
import com.lingmiao.shop.util.formatDouble
import com.lingmiao.shop.widget.EmptyView
import kotlinx.android.synthetic.main.goods_activity_search.*
import kotlinx.coroutines.launch
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode

interface GoodsCenterPre : BasePresenter {

    fun loadListData(page: IPage, list: List<*>, searchText: String?, isGoodsName: Boolean)

    fun clickMenuView(item: Data?, position: Int, view: View)

    fun clickItemView(item: Data?, position: Int)

    fun clickSearchMenuView(target: View);

    interface StatusView : BaseView, BaseLoadMoreView<Data> {
        fun onGoodsEnable(goodsId: String?, position: Int)
        fun onGoodsDisable(goodsId: String?, position: Int)
        fun onGoodsQuantity(quantity: String?, position: Int)
        fun onGoodsDelete(goodsId: String?, position: Int)
        fun onShiftGoodsOwner()
        fun onShiftGoodsName()
    }
}


class GoodsCenterPreImpl(var context: Context, var view: GoodsCenterPre.StatusView) :
    BasePreImpl(view), GoodsCenterPre {

    private val menuPopPre: GoodsMenuPreImpl by lazy { GoodsMenuPreImpl(context, view) }

    private val statusPreImpl: SearchStatusPreImpl by lazy {
        SearchStatusPreImpl(context, view);
    }

    override fun loadListData(
        page: IPage,
        list: List<*>,
        searchText: String?,
        isGoodsName: Boolean
    ) {
        if (searchText.isNullOrBlank()) {
            view.showToast("请输入搜索文字")
            return
        }
        mCoroutine.launch {
            if (list.isEmpty()) {
                view.showPageLoading()
            }

            val resp = GoodsRepository.loadGoodsListByNameFromCenter(
                page.getPageIndex(),
                searchText
            )
            Log.d("WZYUSIS","aaaa")
            Log.d("WZYUSIS",resp.toString())
            if (resp.isSuccess) {
                val goodsList = resp.data.data
                Log.d("WZYUSIS",goodsList.toString())
                view.onLoadMoreSuccess(goodsList, goodsList.isNotEmpty())
            } else {
                Log.d("WZYUSIS","失败")
                view.onLoadMoreFailed()
            }
            view.hidePageLoading()
        }
    }

    override fun clickMenuView(goodsVO: Data?, position: Int, target: View) {
        if (goodsVO == null) {
            return
        }
//        menuPopPre.showMenuPop(goodsVO.getMenuType(), target) { menuType ->
//            when (menuType) {
//                GoodsMenuPop.TYPE_EDIT -> {
//                    menuPopPre.clickEditGoods(context, goodsVO)
//                }
//                GoodsMenuPop.TYPE_ENABLE -> {
//                    menuPopPre.clickEnableGoods(goodsVO.goodsId) {
//                        view.onGoodsEnable(goodsVO.goodsId, position)
//                    }
//                }
//                GoodsMenuPop.TYPE_DISABLE -> {
//                    menuPopPre.clickDisableGoods(goodsVO.goodsId) {
//                        view.onGoodsDisable(goodsVO.goodsId, position)
//                    }
//                }
//                GoodsMenuPop.TYPE_QUANTITY -> {
//                    menuPopPre.clickQuantityGoods(goodsVO.goodsId) {
//                        view.onGoodsQuantity(it, position)
//                    }
//                }
//                GoodsMenuPop.TYPE_DELETE -> {
//                    menuPopPre.clickDeleteGoods(goodsVO.goodsId) {
//                        view.onGoodsDelete(goodsVO.goodsId, position)
//                    }
//                }
//            }
//        }
    }

    override fun clickItemView(item: Data?, position: Int) {
        //  GoodsPublishNewActivity.openActivity(context, item?.goodsId)
    }

    override fun clickSearchMenuView(target: View) {
        statusPreImpl?.showMenuPop(
            StatusMenuPop.TYPE_GOODS_OWNER or StatusMenuPop.TYPE_GOODS_NAME,
            target
        ) { menuType ->
            when (menuType) {
                StatusMenuPop.TYPE_GOODS_OWNER -> {
                    view?.onShiftGoodsOwner()
                }
                StatusMenuPop.TYPE_GOODS_NAME -> {
                    view?.onShiftGoodsName()
                }
            }
        };
    }

}

class CenterGoodsStatusAdapter :
    BaseQuickAdapter<Data, BaseViewHolder>(R.layout.goods_adapter_goods_status) {

    override fun convert(helper: BaseViewHolder, goodsVO: Data?) {
        goodsVO?.apply {
            helper.setText(R.id.goodsNameTv, goods_name)
        }

    }


}


class GoodsSearchCenterActivity : BaseLoadMoreActivity<Data, GoodsCenterPre>(),
    GoodsCenterPre.StatusView {

    private var isGoodsName: Boolean = true

    companion object {
        fun openActivity(context: Context) {
            context.startActivity(Intent(context, GoodsSearchCenterActivity::class.java))
        }
    }

    override fun getLayoutId() = R.layout.activity_goods_search_center

    override fun createPresenter() = GoodsCenterPreImpl(this, this)

    override fun useEventBus() = true

    override fun useLightMode() = false

    override fun autoRefresh() = false

    override fun initOthers() {
        mToolBarDelegate.setMidTitle("商品搜索")

        mSmartRefreshLayout.setEnableRefresh(false)
        mSmartRefreshLayout.setBackgroundColor(ContextCompat.getColor(this, R.color.color_ffffff))

        deleteIv.setOnClickListener {
            inputEdt.setText("")
        }
        searchTv.setOnClickListener {
            mLoadMoreDelegate?.refresh()
        }
        setSearchStatus()
    }

    private fun setSearchStatus() {
        searchStatusTv.setText(if (isGoodsName) "商品名称" else "供应商")
    }

    override fun initAdapter(): CenterGoodsStatusAdapter {
        return CenterGoodsStatusAdapter().apply {
//            setOnItemChildClickListener { adapter, view, position ->
//                if (view.id == R.id.menuIv) {
//                    mPresenter?.clickMenuView(mAdapter.getItem(position), position, view)
//                }
//            }
//            setOnItemClickListener { adapter, view, position ->
//                mPresenter?.clickItemView(mAdapter.getItem(position), position)
//            }
//            emptyView = EmptyView(context).apply {
//                setBackgroundResource(R.color.common_bg)
//            }
        }
    }

    override fun executePageRequest(page: IPage) {
        mPresenter.loadListData(page, mAdapter.data, inputEdt.getViewText(), isGoodsName)
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

    override fun onLoadMoreSuccess(list: List<Data>?, hasMore: Boolean) {
        super.onLoadMoreSuccess(list, hasMore)
    }

    override fun onGoodsEnable(goodsId: String?, position: Int) {
        mAdapter.remove(position)
    }

    override fun onGoodsDisable(goodsId: String?, position: Int) {
        mAdapter.remove(position)
    }

    override fun onGoodsQuantity(quantity: String?, position: Int) {
        (mAdapter as GoodsStatusAdapter).updateQuantity(quantity, position)
    }

    override fun onGoodsDelete(goodsId: String?, position: Int) {
        mAdapter.remove(position)
    }

    override fun onShiftGoodsOwner() {
        isGoodsName = false;
        setSearchStatus()
    }

    override fun onShiftGoodsName() {
        isGoodsName = true;
        setSearchStatus()
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onRefreshEvent(event: RefreshGoodsStatusEvent) {
        mLoadMoreDelegate?.refresh()
    }

    override fun dispatchTouchEvent(ev: MotionEvent?): Boolean {
        if (ev?.action == MotionEvent.ACTION_DOWN) {
            // 当键盘未关闭时先拦截事件
            if (KeyboardUtils.isSoftInputVisible(context)) {
                KeyboardUtils.hideSoftInput(context);
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
    var price: Int = 0,
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