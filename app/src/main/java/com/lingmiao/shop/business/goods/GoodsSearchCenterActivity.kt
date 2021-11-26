package com.lingmiao.shop.business.goods

import android.content.Context
import android.content.Intent
import android.view.MotionEvent
import androidx.core.content.ContextCompat
import com.blankj.utilcode.util.KeyboardUtils
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.google.gson.annotations.SerializedName
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
import com.lingmiao.shop.business.goods.api.bean.GoodsGalleryVO
import com.lingmiao.shop.business.goods.pop.GoodsMenuPop
import com.lingmiao.shop.util.GlideUtils
import kotlinx.android.synthetic.main.goods_activity_search.*
import kotlinx.coroutines.launch
import java.io.Serializable

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
            helper.setText(R.id.goodsNameTv, goodsName)
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
                            getItem(position)?.goodsId.toString(),
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
    @SerializedName("brand_id")
    var brandId: String?,
    @SerializedName("buy_count")
    var buyCount: String?,
    @SerializedName("create_time")
    var createTime: Long?,
    @SerializedName("quantity")
    var quantity: String? = null,
    @SerializedName("enable_quantity")
    var enableQuantity: Int = 0,
    @SerializedName("event_quantity")
    var eventQuantity: Int = 0,
    @SerializedName("goods_id")
    var goodsId: String?,
    @SerializedName("goods_name")
    var goodsName: String?,
    @SerializedName("goods_type")
    var goodsType: String?,
    @SerializedName("supplier_name")
    var supplierName: String?,
    /**
     * STATUS_MIX_0,STATUS_MIX_1,STATUS_MIX_2,STATUS_MIX_3
     */
    @SerializedName("goods_status_mix")
    var goodsStatusMix: Int = 0,
    /**
     * 0 未售馨
     * 1 已售馨
     */
    @SerializedName("goods_quantity_status_mix")
    var goodsQuantityStatusMix: Int = 0,
    /**
     *  3未审核通过 ；0，4审核中；1，2审核通过
     */
    @SerializedName("is_auth")
    var isAuth: Int = 0,
    @SerializedName("goods_status_text")
    var goodsStatusText: String = "",
    @SerializedName("auth_message")
    var authMessage: String = "",
    @SerializedName("market_enable")
    var marketEnable: Int = 0, //上架状态 1上架 0下架
    @SerializedName("price")
    var price: Double = 0.0,
    @SerializedName("event_price")
    var eventPrice: Double = 0.0,
    @SerializedName("priority")
    var priority: Int = 0,
    @SerializedName("seller_name")
    var sellerName: String?,
    @SerializedName("sn")
    var sn: String?,
    @SerializedName("thumbnail")
    var thumbnail: String?,
    @SerializedName("under_message")
    var underMessage: String?,
    /**
     * 轮播图
     */
    @SerializedName("goods_gallery_list")
    var goodsGalleryList: List<GoodsGalleryVO>? = listOf(),
    /**
     * 是否选中
     */
    var isChecked: Boolean? = false
): Serializable  {

    companion object {
        /**
         * 审核状态：0 待审核，1 不需要审核 2 需要审核且审核通过 3 需要审核且审核不通过 4 待编辑(中心库复制的)
         */
        const val AUTH_STATUS_WAITING = 0
        const val AUTH_STATUS_NO_CHECK = 1
        const val AUTH_STATUS_CHECK_AND_PASS = 2
        const val AUTH_STATUS_CHECK_AND_REJECT = 3
        const val AUTH_STATUS_EDITING = 4

        /**
         * 上架状态 1上架 0下架
         */
        const val MARKET_STATUS_DISABLE = 0
        const val MARKET_STATUS_ENABLE = 1

        /**
         * 0 待上架 1 已上架 2 已下架 3 待上架  4库存预警
         */
        const val STATUS_MIX_0 = 0
        const val STATUS_MIX_1 = 1
        const val STATUS_MIX_2 = 2
        const val STATUS_MIX_3 = 3


        fun getEnableAuth(): String {
            return String.format(
                "%s,%s",
                AUTH_STATUS_NO_CHECK,
                AUTH_STATUS_CHECK_AND_PASS
            );
        }

        fun getWaitAuth(): String {
            return String.format(
                "%s,%s,%s",
                AUTH_STATUS_WAITING,
                AUTH_STATUS_CHECK_AND_REJECT,
                AUTH_STATUS_EDITING
            );
        }

        fun getDisableAuth(): String {
            return String.format("%s,%s", AUTH_STATUS_NO_CHECK, AUTH_STATUS_CHECK_AND_PASS);
        }
    }

    fun isSellOut(): Boolean {
        return goodsQuantityStatusMix == 1;
    }

    fun getMenuType(): Int {
        return when (goodsStatusMix) {
            STATUS_MIX_0 -> (GoodsMenuPop.TYPE_QUANTITY)
            STATUS_MIX_1 -> (GoodsMenuPop.TYPE_EDIT or GoodsMenuPop.TYPE_DISABLE or GoodsMenuPop.TYPE_QUANTITY)
            STATUS_MIX_2 -> (GoodsMenuPop.TYPE_EDIT or GoodsMenuPop.TYPE_ENABLE or GoodsMenuPop.TYPE_DELETE)
            STATUS_MIX_3 -> (GoodsMenuPop.TYPE_ENABLE or (if (isAuth == 4) GoodsMenuPop.TYPE_EDIT else 0))
            else -> (GoodsMenuPop.TYPE_EDIT)
        }
    }
}

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