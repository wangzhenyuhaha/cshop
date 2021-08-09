package com.lingmiao.shop.business.goods.fragment

import android.app.Activity
import android.os.Bundle
import android.view.Gravity
import android.view.View
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.TextView
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
import kotlinx.android.synthetic.main.goods_activity_spec_setting.view.*
import kotlinx.android.synthetic.main.goods_fragment_goods_list.*
import kotlinx.android.synthetic.main.goods_fragment_goods_list.navigateView
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

    private var goodsStatus: Int? = null
    private var groupPath: String? = ""
    private var catePath: String? = ""
    private var isSales: Int? = null

    override fun initBundles() {
        //获取当前加载种类
        goodsStatus = arguments?.getInt(KEY_GOODS_STATUS)
    }

    override fun getLayoutId(): Int {
        return R.layout.goods_fragment_goods_list;
    }

    override fun useEventBus(): Boolean {
        return true
    }

    override fun initOthers(rootView: View) {
        mSmartRefreshLayout.setBackgroundResource(R.color.color_ffffff)

        // 搜索
        goodsSearchLayout.singleClick {
            GoodsSearchActivity.openActivity(context!!)
        }
        //  筛选
        goodsFilterTv.singleClick {
            drawerC.openDrawer(Gravity.RIGHT)
        }

        val headview: View = navigateView.inflateHeaderView(R.layout.goods_view_menu_header)

        val rgSalesEnable : RadioGroup = headview.findViewById(R.id.rgSalesEnable);
        // 置顶
        val topMenuTv  = headview.findViewById<TextView>(R.id.topMenuTv);
        // 重置置顶
        val topGroupStatusResetTv = headview.findViewById<TextView>(R.id.topGroupStatusResetTv);
        // 常用
        val usedMenuTv = headview.findViewById<TextView>(R.id.usedMenuTv);
        // 重置常用
        val usedGroupStatusResetTv = headview.findViewById<TextView>(R.id.usedGroupStatusResetTv);

        // 置顶
        topMenuTv.singleClick {
            mPresenter?.clickGroup { group, groupName ->
                groupPath = group?.catPath;
                topMenuTv.isSelected = true;
                topMenuTv.setText(groupName);
            }
        }

        // 置顶清除
        topGroupStatusResetTv.singleClick {}

        // 常用
        usedMenuTv.singleClick {
            mPresenter?.clickCategory { cate, cateName ->
                catePath = cate?.categoryPath
                usedMenuTv.isSelected = true;
                usedMenuTv.setText(cateName);
            }
        }
        // 常用清除
        usedGroupStatusResetTv.singleClick {}
        // 商品销售
        rgSalesEnable.setOnCheckedChangeListener { group, checkedId ->
            if (checkedId == R.id.normalGoodsIv) {
                isSales = 0;
            }
            else if(checkedId == R.id.goodsDiscountIv) {
                isSales = 1;
            }
        }

        // 确定
        headview.findViewById<ITextView>(R.id.tvGoodsFilterFinish).singleClick {
            drawerC.closeDrawers();
            mLoadMoreDelegate?.refresh()
        }
        // 全部清除
        headview.findViewById<ITextView>(R.id.tvGoodsFilterReset).singleClick {
            topMenuTv.setText("点击选择置顶菜单");
            usedMenuTv.setText("点击选择常用菜单");
            rgSalesEnable.clearCheck();
            isSales = null;
            groupPath = "";
            catePath = "";
            usedMenuTv.isSelected = false;
            topMenuTv.isSelected = false;
            drawerC.closeDrawers();
            mLoadMoreDelegate?.refresh()
        }
        cb_goods_list_check_all.setOnCheckedChangeListener { buttonView, isChecked ->
            mAdapter?.data?.forEachIndexed { index, goodsVO ->
                goodsVO.isChecked = isChecked;
            }
            mAdapter?.notifyDataSetChanged();
        };
        tv_goods_off.setOnClickListener {
            mPresenter?.clickOffLine(mAdapter?.data) {
                offLineSuccess();
            }
        }
        tv_goods_on.setOnClickListener {
            mPresenter?.clickOnLine(mAdapter?.data) {
                onLineSuccess();
            }
        }
        tv_goods_rebate.setOnClickListener {
            mPresenter?.clickOnBatchRebate(mAdapter?.data) {
                onBatchRebateSuccess();
            }
        }
        tv_goods_delete.singleClick {
            mPresenter?.clickDelete(mAdapter?.data, {
                var item : GoodsVO? = null;
                val it_b: MutableIterator<GoodsVO> = mAdapter?.data?.iterator()
                while(it_b.hasNext()) {
                    item = it_b.next();
                    if(item != null && item.goodsId != null) {
                        if(it?.indexOf(item.goodsId!!) > -1) {
                            it_b.remove();
                        }
                    }
                }
                mAdapter?.notifyDataSetChanged()
            });
        }
        tv_goods_batch.singleClick {
            (mAdapter as GoodsAdapter)?.setBatchEditModel(true);
            rl_goods_option.gone();
            rl_goods_check.visiable();
        }
        tv_goods_cancel_batch.setOnClickListener{
            rl_goods_option.visiable();
            rl_goods_check.gone();
            cb_goods_list_check_all.isChecked = false;
            var list = mAdapter?.data?.filter { it?.isChecked == true };
            if(list?.size > 0) {
                list?.forEachIndexed { index, goodsVO ->
                    goodsVO.isChecked = false;
                }
            }
            (mAdapter as GoodsAdapter)?.setBatchEditModel(false);
        }

        when (goodsStatus) {
            GoodsNewFragment.GOODS_STATUS_ENABLE -> {
                tv_goods_off.visibility = View.VISIBLE;
                tv_goods_delete.visibility = View.GONE;
            }
            GoodsNewFragment.GOODS_STATUS_WAITING,
            GoodsNewFragment.GOODS_STATUS_DISABLE -> {
                tv_goods_on.visibility = View.VISIBLE;
                tv_goods_delete.visibility = View.VISIBLE;
            }
            GoodsNewFragment.GOODS_STATUS_SOLD_OUT -> {
                tv_goods_delete.visibility = View.VISIBLE;
            }
            GoodsNewFragment.GOODS_STATUE_WARNING -> {
                rl_goods_option.gone();
                rl_goods_check.gone();
                searchLayout.gone();
                filterLayout.gone();
            }
            GoodsNewFragment.GOODS_STATUS_ALL -> {
                tv_goods_batch.gone();
                rl_goods_check.gone();
            }
            else -> {
                tv_goods_delete.visibility = View.GONE;
            }
        }
    }

    override fun initAdapter(): GoodsAdapter {
        return GoodsAdapter(goodsStatus!!).apply {
            setOnItemChildClickListener { adapter, view, position ->
                var item = mAdapter.getItem(position) as GoodsVO;
                if (view.id == R.id.menuIv) {
                    mPresenter?.clickMenuView(mAdapter.getItem(position), position, view)
                } else if(view.id == R.id.goodsIv) {
                    if(item?.goodsGalleryList?.size ?:0 > 0) {
                        PhotoHelper.previewAlbum(context as Activity, 0, item?.goodsGalleryList);
                    } else {
                        PhotoHelper.previewImage(context as Activity, item?.thumbnail);
                    }
                }
            }
            setOnItemClickListener { adapter, view, position ->
                shiftChecked(position);
               // tvGoodsSelectCount.setText(String.format("已选择%s件商品", mPresenter?.getCheckedCount(mAdapter.data)))
//                mPresenter?.clickItemView(mAdapter.getItem(position), position)
            }
            onItemLongClickListener = BaseQuickAdapter.OnItemLongClickListener { adapter, view, position -> Boolean
                if(rl_goods_check.visibility != View.VISIBLE) {
                    rl_goods_check.visibility = View.VISIBLE;
                }
                setBatchEditModel(true);
                return@OnItemLongClickListener true;
            }
            emptyView = EmptyView(mContext).apply {
                setBackgroundResource(R.color.color_ffffff)
            }
        }
    }

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
        list?.forEachIndexed { index, goodsVO ->
            goodsVO?.isChecked = cb_goods_list_check_all.isChecked;
        }
    }

    override fun executePageRequest(page: IPage) {
        mPresenter?.loadListData(page, groupPath, catePath, isSales, mAdapter.data)
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
        // (mAdapter as GoodsAdapter).updateQuantity(quantity, position)
        mLoadMoreDelegate?.refresh()
    }

    override fun onGoodsDelete(goodsId: String?, position: Int) {
        mAdapter.remove(position)
        //EventBus.getDefault().post(GoodsNumberEvent(goodsStatus!!,mAdapter.data.size));
    }

    override fun onSetTotalCount(count: Int?) {
        tvGoodsCount.setText(String.format("商品共%s件", count?:0))
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
            mLoadMoreDelegate?.refresh()
        }
    }

    @Subscribe(threadMode =  ThreadMode.MAIN)
    fun onBatchSuccess(event : BatchStatusEvent) {
        if(event?.isRefresh(goodsStatus)) {
            cb_goods_list_check_all.isChecked = false;
        }
    }

}