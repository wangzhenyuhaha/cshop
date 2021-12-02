package com.lingmiao.shop.business.goods

import android.content.Context
import android.content.Intent
import android.widget.TextView
import androidx.core.content.ContextCompat
import com.blankj.utilcode.util.ActivityUtils
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.james.common.base.delegate.DefaultPageLoadingDelegate
import com.james.common.base.delegate.PageLoadingDelegate
import com.james.common.base.loadmore.BaseLoadMoreActivity
import com.james.common.base.loadmore.core.IPage
import com.james.common.utils.exts.gone
import com.james.common.utils.exts.singleClick
import com.james.common.utils.exts.visiable
import com.lingmiao.shop.R
import com.lingmiao.shop.base.UserManager
import com.lingmiao.shop.business.goods.adapter.GoodsCheckedAdapter
import com.lingmiao.shop.business.goods.api.bean.CategoryVO
import com.lingmiao.shop.business.goods.api.bean.GoodsVO
import com.lingmiao.shop.business.goods.config.GoodsConfig
import com.lingmiao.shop.business.goods.event.GoodsHomeTabEvent
import com.lingmiao.shop.business.goods.event.RefreshGoodsStatusEvent
import com.lingmiao.shop.business.goods.fragment.GoodsNewFragment
import com.lingmiao.shop.business.goods.presenter.GoodsManagerPre
import com.lingmiao.shop.business.goods.presenter.impl.GoodsManagerPreImpl
import com.lingmiao.shop.util.GlideUtils
import com.lingmiao.shop.util.formatDouble
import com.lingmiao.shop.util.initAdapter
import com.lingmiao.shop.widget.EmptyView
import kotlinx.android.synthetic.main.goods_activity_goods_manager.*
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode

/**
Create Date : 2021/3/69:58 AM
Auther      : Fox
Desc        :
 **/
class GoodsManagerActivity : BaseLoadMoreActivity<GoodsVO, GoodsManagerPre>(),
    GoodsManagerPre.View {


    private var mCateVo: CategoryVO? = null


    //默认"0"
    private var cId: String = ""

    //默认1
    private var mSourceId: Int? = 0

    //已选中商品Adapter
    var selectedAdapter: GoodsSelectedAdapter? = null

    //选中的商品
    var list: List<GoodsVO>? = null

    //选中的分类
    var categoryId: String? = null

    //选中的菜单
    var shopCatId: String? = null

    //当前页面状态，选择还是添加,0选择 1添加
    private var type: Int = 0

    companion object {

        const val KEY_ID = "KEY_ID"
        const val KEY_SOURCE = "KEY_SOURCE"
        const val SOURCE_TYPE = 1
        private const val SOURCE_MENU = 2
        private const val SOURCE_SALES = 3

        //keyId的值为"0"
        fun type(context: Context, keyId: String?) {
            val intent = Intent(context, GoodsManagerActivity::class.java)
            //默认使用1
            intent.putExtra(KEY_SOURCE, SOURCE_TYPE)
            //默认使用"0"
            intent.putExtra(KEY_ID, keyId)
            context.startActivity(intent)
        }

        fun menu(context: Context, keyId: String?) {
            val intent = Intent(context, GoodsManagerActivity::class.java)
            intent.putExtra(KEY_SOURCE, SOURCE_MENU)
            intent.putExtra(KEY_ID, keyId)
            context.startActivity(intent)
        }

        fun sales(context: Context, keyId: String?) {
            val intent = Intent(context, GoodsManagerActivity::class.java)
            intent.putExtra(KEY_SOURCE, SOURCE_SALES)
            intent.putExtra(KEY_ID, keyId)
            context.startActivity(intent)
        }
    }

    override fun initBundles() {
        //默认"0"
        cId = intent?.getStringExtra(KEY_ID) ?: "0"
        //使用GoodsManagerActivity.type默认为1
        mSourceId = intent.getIntExtra(KEY_SOURCE, SOURCE_TYPE)
    }

    override fun autoRefresh() = false

    override fun useEventBus() = true

    override fun useLightMode() = false

    override fun getLayoutId() = R.layout.goods_activity_goods_manager

    override fun createPresenter() = GoodsManagerPreImpl(this, this)

    override fun initOthers() {
        mToolBarDelegate.setMidTitle("中心库商品管理")

        //分类选择
        firstTypeTv.setOnClickListener {
            mPresenter?.showCategoryPop(it)
        }

        //全选
        goodsManagerCb.setOnCheckedChangeListener { _, isChecked ->
            mAdapter.data.forEach {
                it?.isChecked = isChecked
            }
            mAdapter?.notifyDataSetChanged()
            setCheckedCount(getCheckedCount())
        }

        //选择商品
        goodsCheckSelect.setOnClickListener {

            if (getCheckedCount() > 0) {
                //选择了商品
                list = mAdapter.data.filter { it?.isChecked == true }

                selectedAdapter = GoodsSelectedAdapter().apply {
                    setOnItemChildClickListener { _, view, position ->
                        if (view.id == R.id.deleteIv) {
                            list?.also {
                                it[position].isChecked = false
                            }
                            list = list?.filter { it.isChecked == true }
                            list?.also { selectedAdapter?.replaceData(it) }
                        }

                    }
                    emptyView = EmptyView(context).apply {
                        setBackgroundResource(R.color.common_bg)
                    }
                }

                selectedAdapter?.also {
                    goodsManagerRV2.initAdapter(it)
                }

                selectedAdapter?.replaceData(list as List<GoodsVO>)
                selectedAdapter?.notifyDataSetChanged()

                goodsManagerRV.gone()
                goodsManagerRV2.visiable()


                goodsManagerBottomContainer.gone()
                goodsManager.visiable()

                firstTypeTv.text = "已选商品"

            } else {
                showToast("请选择商品")
            }
        }

        //返回
        goodsManagerBack.setOnClickListener {
            firstTypeTv.text = "请选择分类"


            goodsManagerRV.visiable()
            goodsManagerRV2.gone()


            goodsManagerBottomContainer.visiable()
            goodsManager.gone()

            mAdapter.data.forEach {
                it?.isChecked = false
            }
            mAdapter?.notifyDataSetChanged()
            setCheckedCount(getCheckedCount())

            list = null

            if (goodsManagerCb.isChecked) {
                goodsManagerCb.isChecked = false
            }

            goodsManagerdenlei.text = "请选择分类"
            goodsManagercaidan.text = "请选择菜单"

            categoryId = null
            shopCatId = null

        }

        //添加分类
        goodsManagerdenlei.singleClick {
            mPresenter.showCategoryPop()
        }

        //添加菜单
        goodsManagercaidan.singleClick {
            mPresenter.showGroupPop()
        }

        //使用默认菜单分类
        goodsManagermoren.setOnClickListener {
            if (goodsManagermoren.isChecked) {
                goodsManagerdenlei.text = ""
                this.categoryId = ""
                goodsManagercaidan.text = ""
                this.shopCatId = ""
            }
        }

        //确认添加商品
        goodsCheckSubmit.setOnClickListener {
            if (goodsManagermoren.isChecked) {
                //使用默认
                if (list?.size ?: 0 > 0) {
                    val ids = list?.map { it.goodsId }?.joinToString(separator = ",")
                    if (ids != null) {
                        mPresenter?.add(ids, null, null)
                    }
                }
            } else {
                if (categoryId.isNullOrEmpty()) {
                    showToast("请选择商品分类")
                    return@setOnClickListener
                }
                if (shopCatId.isNullOrEmpty()) {
                    showToast("请选择商品菜单")
                    return@setOnClickListener
                }
                if (list?.size ?: 0 > 0) {
                    val ids = list?.map { it.goodsId }?.joinToString(separator = ",")
                    if (ids != null) {
                        mPresenter?.add(ids, categoryId, shopCatId)
                    }
                }
            }
        }

        //新增分类
        cateAddIv.singleClick {
            GoodsCategoryActivity.openActivity(this, 1)
        }

        // 禁用下拉刷新
        mSmartRefreshLayout.setEnableRefresh(false)
        mSmartRefreshLayout.setEnableLoadMore(true)
        mSmartRefreshLayout.setBackgroundColor(ContextCompat.getColor(this, R.color.color_ffffff))

        //默认加载所有商品
//        if (UserManager.getLoginInfo()?.goodsCateId == null) {
//            mPresenter.loadCID()
//        } else {
//            cId = "0|${UserManager.getLoginInfo()?.goodsCateId}"
//            mLoadMoreDelegate?.refresh()
//        }

      // mLoadMoreDelegate?.refresh()

        //点击搜索
        goodsSearchLayout.singleClick {
            GoodsSearchCenterActivity.openActivity(this)
        }

    }

    //更新分类
    override fun onUpdateCategory(categoryId: String?, categoryName: String?) {
        goodsManagerdenlei.text = categoryName
        this.categoryId = categoryId
        goodsManagermoren.isChecked = false
    }

    //更新菜单
    override fun onUpdateGroup(groupId: String?, groupName: String?) {
        goodsManagercaidan.text = groupName
        this.shopCatId = groupId
        goodsManagermoren.isChecked = false
    }

    //更新页面
    override fun setCid(id: String) {
        cId = "0|${id}"
        mLoadMoreDelegate?.refresh()
    }

    override fun onAddSuccess() {
        EventBus.getDefault().post(GoodsHomeTabEvent(GoodsNewFragment.GOODS_STATUS_WAITING))
        ActivityUtils.finishToActivity(GoodsListActivity::class.java, false)
    }

    override fun onUpdateCategory(it: CategoryVO?) {
        firstTypeTv.text = it?.name
        mCateVo = it
        cId = it?.categoryPath!!
        mLoadMoreDelegate?.refresh()
    }

    override fun initAdapter(): BaseQuickAdapter<GoodsVO, BaseViewHolder> {
        return GoodsCheckedAdapter().apply {
            setOnItemChildClickListener { adapter, view, position ->
                val bItem = adapter.data[position] as GoodsVO
                if (view.id == R.id.menuIv) {
                    bItem.isChecked = !(bItem.isChecked ?: false)
                }
                setCheckedCount(getCheckedCount())
            }
            emptyView = EmptyView(context).apply {
                setBackgroundResource(R.color.common_bg)
            }
        }
    }

    private fun getCheckedCount(): Int {
        return mAdapter.data.filter { it?.isChecked == true }.size
    }

    private fun setCheckedCount(count: Int) {
        goodsCheckedCountTv.text = String.format("已选择%s件商品", count)
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

    /**
     * 执行分页请求
     */
    override fun executePageRequest(page: IPage) {
        mPresenter.loadListData(page, mAdapter.data, cId)
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onRefreshEvent(event: RefreshGoodsStatusEvent) {
        mLoadMoreDelegate?.refresh()
    }

}

class GoodsSelectedAdapter :
    BaseQuickAdapter<GoodsVO, BaseViewHolder>(R.layout.goods_adapter_goods_check) {

    override fun convert(helper: BaseViewHolder, item: GoodsVO?) {
        item?.apply {

            //显示图片
            GlideUtils.setImageUrl1(helper.getView(R.id.goodsIv), thumbnail)

            //显示是否是虚拟商品
            helper.getView<TextView>(R.id.goodsNameTv).setCompoundDrawablesWithIntrinsicBounds(
                if (item.goodsType == GoodsConfig.GOODS_TYPE_VIRTUAL) R.mipmap.ic_virtual else 0,
                0,
                0,
                0
            )
            //显示商品名
            helper.setText(R.id.goodsNameTv, goodsName)

            //useless
            helper.setText(R.id.goodsQuantityTv, "")

            //商品价格
            helper.setText(R.id.goodsPriceTv, formatDouble(price))


            helper.setVisible(R.id.menuIv, false)
            helper.setVisible(R.id.deleteIv, true)

            helper.addOnClickListener(R.id.deleteIv)
        }
    }

    fun shiftChecked(position: Int) {
        data.forEachIndexed { _, goodsVO ->
            goodsVO.isChecked = false
        }
        data[position]?.isChecked = !(data[position]?.isChecked ?: false)
        notifyItemChanged(position)
    }
}