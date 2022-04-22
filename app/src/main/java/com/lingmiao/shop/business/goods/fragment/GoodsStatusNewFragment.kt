package com.lingmiao.shop.business.goods.fragment

import android.annotation.SuppressLint
import android.app.Activity
import android.os.Bundle
import android.util.Log
import android.view.Gravity
import android.view.View
import android.widget.RadioGroup
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.core.view.GravityCompat
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import com.chad.library.adapter.base.BaseQuickAdapter
import com.james.common.base.loadmore.BaseLoadMoreFragment
import com.james.common.base.loadmore.core.IPage
import com.james.common.utils.exts.gone
import com.james.common.utils.exts.singleClick
import com.james.common.utils.exts.visiable
import com.james.common.view.ITextView
import com.lingmiao.shop.R
import com.lingmiao.shop.business.goods.GoodsSearchActivity
import com.lingmiao.shop.business.goods.adapter.GoodsAdapter
import com.lingmiao.shop.business.goods.api.bean.GoodsVO
import com.lingmiao.shop.business.goods.event.BatchStatusEvent
import com.lingmiao.shop.business.goods.event.RefreshGoodsStatusEvent
import com.lingmiao.shop.business.goods.presenter.GoodsStatusPre
import com.lingmiao.shop.business.goods.presenter.impl.*
import com.lingmiao.shop.business.photo.PhotoHelper
import com.lingmiao.shop.widget.EmptyView
import kotlinx.android.synthetic.main.fragment_shop_info.*
import kotlinx.android.synthetic.main.goods_activity_spec_setting.view.*
import kotlinx.android.synthetic.main.goods_fragment_goods_list.*
import kotlinx.coroutines.launch
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode

/**
 * Author : Elson
 * Date   : 2020/7/11
 * Desc   : 商品(出售中、待审核、已下架、库存预警)列表
 */
class GoodsStatusNewFragment : BaseLoadMoreFragment<GoodsVO, GoodsStatusPre>(),
    GoodsStatusPre.StatusView {

    companion object {
        const val KEY_GOODS_STATUS = "KEY_GOODS_STATUS"

        fun newInstance(goodsStatus: Int): GoodsStatusNewFragment {
            return GoodsStatusNewFragment().apply {
                arguments = Bundle().apply {
                    putInt(KEY_GOODS_STATUS, goodsStatus)
                }
            }
        }
    }

    //现在正在使用的Item
    private var position: Int = 0

    //是否前往目标Item
    private var toPosition: Boolean = false

    //当前加载商品的种类
    private var goodsStatus: Int? = null

    //置顶菜单
    private var groupPath: String? = ""

    //分类
    private var catePath: String? = ""

    //是否是活动商品　　１是０否
    private var isSales: Int? = null

    //排序方式 （库存，价格，时间）enable_quantity price create_time
    private var orderColumn: String? = null

    // 1 库存  2 价格  3时间
    private val selectLiveData: MutableLiveData<Int> = MutableLiveData()

    //是否倒序  1倒序，0否
    private var isDESC: Int? = 0


    override fun initBundles() {
        //获取当前加载种类
        goodsStatus = arguments?.getInt(KEY_GOODS_STATUS)
    }

    override fun getLayoutId() = R.layout.goods_fragment_goods_list


    override fun useEventBus() = true


    override fun initOthers(rootView: View) {

        mSmartRefreshLayout.setBackgroundResource(R.color.color_ffffff)

        // 搜索
        goodsSearchLayout.singleClick {
            GoodsSearchActivity.openActivity(requireContext())
        }

        //筛选
        goodsFilterTv.singleClick {
            drawerC.openDrawer(GravityCompat.END)
        }

        //抽屉布局中Layout
        val headView: View = navigateView.inflateHeaderView(R.layout.goods_view_menu_header)

        //商品信息栏
        val rgSalesEnable: RadioGroup = headView.findViewById(R.id.rgSalesEnable)

        //点击选择置顶菜单
        val topMenuTv = headView.findViewById<TextView>(R.id.topMenuTv)

        //不显示的重置
        val topGroupStatusResetTv = headView.findViewById<TextView>(R.id.topGroupStatusResetTv)

        //点击选择分类
        val usedMenuTv = headView.findViewById<TextView>(R.id.usedMenuTv)

        //又一个不显示的重置
        val usedGroupStatusResetTv = headView.findViewById<TextView>(R.id.usedGroupStatusResetTv)

        //点击选择置顶菜单
        //菜单
        topMenuTv.singleClick {
            mPresenter?.clickAllGroup { group, groupName ->
                //此时获取的时置顶菜单的相关信息
                //获取分组路径
                groupPath = group?.catPath
                topMenuTv.isSelected = true
                //获取分组名
                topMenuTv.text = groupName
            }
        }

        //不显示的重置
        topGroupStatusResetTv.singleClick {}

        //点击选择分类
        usedMenuTv.singleClick {
            mPresenter?.clickCategory { cate, cateName ->
                //
                catePath = cate?.categoryPath
                usedMenuTv.isSelected = true
                //
                usedMenuTv.text = cateName
            }
        }
        //又一个不显示的重置
        usedGroupStatusResetTv.singleClick {}


        //选择商品信息(是不是活动商品)
        rgSalesEnable.setOnCheckedChangeListener { _, checkedId ->
            if (checkedId == R.id.normalGoodsIv) {
                //不是活动商品
                isSales = 0
            } else if (checkedId == R.id.goodsDiscountIv) {
                //是活动商品
                isSales = 1
            }
        }

        //确定
        headView.findViewById<ITextView>(R.id.tvGoodsFilterFinish).singleClick {
            drawerC.closeDrawers()
            //重新调用接口
            mLoadMoreDelegate?.refresh()
        }
        // 全部清除
        headView.findViewById<ITextView>(R.id.tvGoodsFilterReset).singleClick {
            topMenuTv.text = "点击选择菜单"
            usedMenuTv.text = "点击选择分类"
            rgSalesEnable.clearCheck()
            isSales = null
            groupPath = ""
            catePath = ""
            usedMenuTv.isSelected = false
            topMenuTv.isSelected = false
            drawerC.closeDrawers()
            mLoadMoreDelegate?.refresh()
        }

        //排序
        //库存
        goodsStoreCountTv.setOnClickListener {
            selectLiveData.value = 1
        }

        //价格
        goodsPriceTv.setOnClickListener {
            selectLiveData.value = 2
        }

        //时间
        goodsTimeTv.setOnClickListener {
            selectLiveData.value = 3
        }


        //全选
        cb_goods_list_check_all.setOnCheckedChangeListener { buttonView, isChecked ->
            mAdapter.data.forEachIndexed { index, goodsVO ->
                goodsVO.isChecked = isChecked
            }
            mAdapter?.notifyDataSetChanged()
        }

        //批量下架
        tv_goods_off.setOnClickListener {
            mPresenter?.clickOffLine(mAdapter?.data) {
                offLineSuccess()
            }
        }

        //批量上架
        tv_goods_on.setOnClickListener {
            mPresenter?.clickOnLine(mAdapter?.data) {
                onLineSuccess()
            }
        }

        //设置佣金
        tv_goods_rebate.setOnClickListener {
            mPresenter?.clickOnBatchRebate(mAdapter?.data) {
                onBatchRebateSuccess()
            }
        }

        //批量删除
        tv_goods_delete.singleClick {
            mPresenter?.clickDelete(mAdapter?.data, {
                var item: GoodsVO? = null
                val it_b: MutableIterator<GoodsVO> = mAdapter?.data?.iterator()
                while (it_b.hasNext()) {
                    item = it_b.next()
                    if (item != null && item.goodsId != null) {
                        if (it?.indexOf(item.goodsId!!) > -1) {
                            it_b.remove()
                        }
                    }
                }
                mAdapter?.notifyDataSetChanged()
            })
        }

        //批量操作
        tv_goods_batch.singleClick {
            (mAdapter as GoodsAdapter)?.setBatchEditModel(true)
            rl_goods_option.gone()
            rl_goods_check.visiable()
        }

        //完成操作
        tv_goods_cancel_batch.setOnClickListener {
            rl_goods_option.visiable()
            rl_goods_check.gone()
            cb_goods_list_check_all.isChecked = false
            var list = mAdapter?.data?.filter { it?.isChecked == true }
            if (list?.size > 0) {
                list?.forEachIndexed { index, goodsVO ->
                    goodsVO.isChecked = false
                }
            }
            (mAdapter as GoodsAdapter)?.setBatchEditModel(false)
        }

        //don't know
        goodsSalesCountTv.setOnClickListener {
            // mPresenter.
        }

        //不同商品状态下控件的显示
        when (goodsStatus) {
            GoodsNewFragment.GOODS_STATUS_ENABLE -> {
                tv_goods_off.visibility = View.VISIBLE
                tv_goods_delete.visibility = View.GONE
            }
            GoodsNewFragment.GOODS_STATUS_WAITING,
            GoodsNewFragment.GOODS_STATUS_DISABLE -> {
                tv_goods_on.visibility = View.VISIBLE
                tv_goods_delete.visibility = View.VISIBLE
            }
            GoodsNewFragment.GOODS_STATUS_SOLD_OUT -> {
                tv_goods_delete.visibility = View.VISIBLE
            }
            GoodsNewFragment.GOODS_STATUE_WARNING -> {
                rl_goods_option.gone()
                rl_goods_check.gone()
                searchLayout.gone()
                filterLayout.gone()
            }
            GoodsNewFragment.GOODS_STATUS_ALL -> {
                tv_goods_batch.gone()
                rl_goods_check.gone()
            }
            else -> {
                tv_goods_delete.visibility = View.GONE
            }
        }

        selectLiveData.observe(this, Observer {
            when (it) {
                //1 库存  2 价格  3时间  enable_quantity price create_time
                1 -> {
                    goodsStoreCountTv.isSelected = goodsStoreCountTv.isSelected != true
                    goodsPriceTv.isSelected = false
                    goodsTimeTv.isSelected = false
                    goodsStoreCountTv.setTextColor(
                        ContextCompat.getColor(
                            mContext,
                            R.color.primary
                        )
                    )
                    goodsPriceTv.setTextColor(
                        ContextCompat.getColor(
                            mContext,
                            R.color.color_666666
                        )
                    )
                    goodsTimeTv.setTextColor(ContextCompat.getColor(mContext, R.color.color_666666))

                    //排序方式
                    orderColumn = "enable_quantity"
                    isDESC = if (goodsStoreCountTv.isSelected) {
                        0
                    } else {
                        1
                    }
                }
                2 -> {
                    goodsStoreCountTv.isSelected = false
                    goodsPriceTv.isSelected = goodsPriceTv.isSelected != true
                    goodsTimeTv.isSelected = false
                    goodsStoreCountTv.setTextColor(
                        ContextCompat.getColor(
                            mContext,
                            R.color.color_666666
                        )
                    )
                    goodsPriceTv.setTextColor(ContextCompat.getColor(mContext, R.color.primary))
                    goodsTimeTv.setTextColor(ContextCompat.getColor(mContext, R.color.color_666666))

                    orderColumn = "price"
                    isDESC = if (goodsPriceTv.isSelected) {
                        0
                    } else {
                        1
                    }

                }
                3 -> {
                    goodsStoreCountTv.isSelected = false
                    goodsPriceTv.isSelected = false
                    goodsTimeTv.isSelected = goodsTimeTv.isSelected != true
                    goodsStoreCountTv.setTextColor(
                        ContextCompat.getColor(
                            mContext,
                            R.color.color_666666
                        )
                    )
                    goodsPriceTv.setTextColor(
                        ContextCompat.getColor(
                            mContext,
                            R.color.color_666666
                        )
                    )
                    goodsTimeTv.setTextColor(ContextCompat.getColor(mContext, R.color.primary))

                    orderColumn = "create_time"
                    isDESC = if (goodsTimeTv.isSelected) {
                        0
                    } else {
                        1
                    }
                }
            }
            mLoadMoreDelegate?.refresh()
        })
    }

    override fun initAdapter(): GoodsAdapter {
        return GoodsAdapter(goodsStatus!!).apply {
            setOnItemChildClickListener { adapter, view, position ->
                var item = mAdapter.getItem(position) as GoodsVO
                if (view.id == R.id.menuIv) {
                    mPresenter?.clickMenuView(mAdapter.getItem(position), position, view)
                } else if (view.id == R.id.goodsIv) {
                    if (item?.goodsGalleryList?.size ?: 0 > 0) {
                        PhotoHelper.previewAlbum(context as Activity, 0, item?.goodsGalleryList)
                    } else {
                        PhotoHelper.previewImage(context as Activity, item?.thumbnail)
                    }
                }
            }
            setOnItemClickListener { adapter, view, position ->
                shiftChecked(position)
                // tvGoodsSelectCount.setText(String.format("已选择%s件商品", mPresenter?.getCheckedCount(mAdapter.data)))
//                mPresenter?.clickItemView(mAdapter.getItem(position), position)
            }
            onItemLongClickListener =
                BaseQuickAdapter.OnItemLongClickListener { adapter, view, position ->
                    Boolean
                    if (rl_goods_check.visibility != View.VISIBLE) {
                        rl_goods_check.visibility = View.VISIBLE
                    }
                    setBatchEditModel(true)
                    return@OnItemLongClickListener true
                }
            emptyView = EmptyView(mContext).apply {
                setBackgroundResource(R.color.color_ffffff)
            }
        }
    }

    //mPresenter赋值
    override fun createPresenter(): GoodsStatusPre? {
        return when (goodsStatus) {
            GoodsNewFragment.GOODS_STATUS_ALL -> GoodsAllPreImpl(mContext, this)
            GoodsNewFragment.GOODS_STATUS_ENABLE -> GoodsMarketEnablePreImpl(mContext, this)
            GoodsNewFragment.GOODS_STATUS_WAITING -> GoodsAuthWaitingPreImpl(mContext, this)
            GoodsNewFragment.GOODS_STATUS_DISABLE -> GoodsMarketDisablePreImpl(mContext, this)
            GoodsNewFragment.GOODS_STATUS_SOLD_OUT -> GoodsSellOutPreImpl(mContext, this)
            GoodsNewFragment.GOODS_STATUE_WARNING -> GoodsWarningPreImpl(mContext, this)
            else -> null
        }
    }

    override fun onLoadMoreSuccess(list: List<GoodsVO>?, hasMore: Boolean) {
        super.onLoadMoreSuccess(list, hasMore)
        list?.forEachIndexed { _, goodsVO ->
            goodsVO.isChecked = cb_goods_list_check_all.isChecked
        }
        toPosition =false
    }

    override fun setNowPosition(position: Int) {
        this.position = position
    }

    override fun executePageRequest(page: IPage) {
        if (toPosition) {
            //计算出要滚动的页数
            val temp: Int = position / 10
            mPresenter?.loadListData(
                page,
                groupPath,
                catePath,
                isSales,
                mAdapter.data,
                orderColumn,
                isDESC,
                10 * (temp + 1)
            )
        } else {
            mPresenter?.loadListData(
                page,
                groupPath,
                catePath,
                isSales,
                mAdapter.data,
                orderColumn,
                isDESC,
                null
            )
        }

    }

    override fun onGoodsEnable(goodsId: String?, position: Int) {
        mAdapter.remove(position)
        //EventBus.getDefault().post(GoodsNumberEvent(goodsStatus!!,mAdapter.data.size));
    }

    override fun onGoodsDisable(goodsId: String?, position: Int) {
        mAdapter.remove(position)
        //EventBus.getDefault().post(GoodsNumberEvent(goodsStatus!!,mAdapter.data.size));
    }

    override fun onGoodsQuantity(quantity: String?, position: Int) {
        (mAdapter as GoodsAdapter).updateQuantity(quantity, position)
    }

    override fun onGoodsDelete(goodsId: String?, position: Int) {
        mAdapter.remove(position)
        //EventBus.getDefault().post(GoodsNumberEvent(goodsStatus!!,mAdapter.data.size));
    }

    override fun onSetTotalCount(count: Int?) {
        tvGoodsCount.setText(String.format("商品共%s件", count ?: 0))
    }

    override fun onLineSuccess() {
        EventBus.getDefault().post(BatchStatusEvent(goodsStatus!!))
        EventBus.getDefault().post(RefreshGoodsStatusEvent(goodsStatus!!))
    }

    override fun offLineSuccess() {
        EventBus.getDefault().post(BatchStatusEvent(goodsStatus!!))
        EventBus.getDefault().post(RefreshGoodsStatusEvent(goodsStatus!!))
    }

    override fun onBatchRebateSuccess() {
        EventBus.getDefault().post(BatchStatusEvent(goodsStatus!!))
        EventBus.getDefault().post(RefreshGoodsStatusEvent(goodsStatus!!))
    }

    override fun onBatchDeleted(ids: String) {

    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onRefreshEvent(event: RefreshGoodsStatusEvent) {
        if (event.isRefresh(goodsStatus)) {
            if (event.type == 1) {
                //需要滚到之前的位置
                rollingToPosition()
            } else {
                mLoadMoreDelegate?.refresh()
            }

        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onBatchSuccess(event: BatchStatusEvent) {
        if (event?.isRefresh(goodsStatus)) {
            cb_goods_list_check_all.isChecked = false
        }
    }

    //该方法使列表滚到适合的位置
    private fun rollingToPosition() {
        toPosition = true
        mLoadMoreDelegate?.refresh()
    }
}