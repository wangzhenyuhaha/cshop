package com.lingmiao.shop.business.main

import android.view.MotionEvent
import android.view.View
import androidx.core.content.ContextCompat
import com.blankj.utilcode.util.KeyboardUtils
import com.blankj.utilcode.util.ToastUtils
import com.james.common.base.delegate.DefaultPageLoadingDelegate
import com.james.common.base.delegate.PageLoadingDelegate
import com.james.common.base.loadmore.BaseLoadMoreActivity
import com.james.common.base.loadmore.core.IPage
import com.james.common.utils.exts.getViewText
import com.lingmiao.shop.R
import com.lingmiao.shop.business.goods.adapter.GoodsStatusAdapter
import com.lingmiao.shop.business.goods.event.RefreshGoodsStatusEvent
import com.lingmiao.shop.business.main.adapter.SubBankSearchAdapter
import com.lingmiao.shop.business.main.adapter.SubBankStatusAdapter
import com.lingmiao.shop.business.main.adapter.SubBankTestSearchAdapter
import com.lingmiao.shop.business.main.bean.BankDetail
import com.lingmiao.shop.business.main.fragment.BindAccountFragment
import com.lingmiao.shop.business.main.presenter.SubBranchPre
import com.lingmiao.shop.business.main.presenter.impl.SubBranchPreImpl
import com.lingmiao.shop.widget.EmptyView
import kotlinx.android.synthetic.main.activity_sub_branch.*
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode

class SubBranchActivity : BaseLoadMoreActivity<BankDetail, SubBranchPre>(),
    SubBranchPre.StatusView {

    private var isGoodsName: Boolean = true

    private var type: Int? = null

    private var bank: String? = null

    override fun getLayoutId(): Int {
        return R.layout.activity_sub_branch
    }

    override fun createPresenter(): SubBranchPre {

        type = intent.getIntExtra("type", 100)
        bank = intent.getStringExtra("bank")

        return SubBranchPreImpl(this, this)
    }


    override fun initOthers() {
        when (type) {
            BindAccountFragment.COMPANY_BANK, BindAccountFragment.PERSONAL_BANK -> {
                mToolBarDelegate.setMidTitle("选择银行")
                mLoadMoreDelegate?.refresh()
                bank_container.visibility = View.GONE
            }
            else -> {
                mToolBarDelegate.setMidTitle("选择支行")
                mLoadMoreDelegate?.refresh()
            }
        }

        // 禁用下拉刷新
        mSmartRefreshLayout.setEnableRefresh(false)
        mSmartRefreshLayout.setBackgroundColor(ContextCompat.getColor(this, R.color.color_ffffff))

        bank_deleteIv.setOnClickListener {
            bank_inputEdt.setText("")
        }
        bank_searchTv.setOnClickListener {
            mLoadMoreDelegate?.refresh()
        }
    }


    override fun initAdapter(): SubBankStatusAdapter {


        when (type) {
            BindAccountFragment.COMPANY_BANK, BindAccountFragment.PERSONAL_BANK -> {
                return SubBankSearchAdapter().apply {

                    setOnItemClickListener { adapter, _, position ->
                        val tempList = adapter.data as List<BankDetail>
                        val item = tempList[position]
                        item.type = type
                        EventBus.getDefault().post(item)
                        ToastUtils.showShort("您选择了${item.bafName}")
                    }
                    emptyView = EmptyView(context).apply {
                        setBackgroundResource(R.color.common_bg)
                    }
                }

            }


            else -> {
                return SubBankTestSearchAdapter().apply {

                    setOnItemClickListener { adapter, _, position ->
                        val tempList = adapter.data as List<BankDetail>
                        val item = tempList[position]
                        item.type = type
                        EventBus.getDefault().post(item)
                        ToastUtils.showShort("您选择了${item.bankName}")
                    }

                    emptyView = EmptyView(context).apply {
                        setBackgroundResource(R.color.common_bg)
                    }
                }
            }


        }
    }


    override fun executePageRequest(page: IPage) {
        when (type) {
            BindAccountFragment.COMPANY_BANK, BindAccountFragment.PERSONAL_BANK -> {
                mPresenter.loadListData(
                    page,
                    mAdapter.data,
                    bank_inputEdt.getViewText()
                )
            }
            else -> {
                mPresenter.loadSubListData(
                    page,
                    mAdapter.data,
                    bank!!,
                    bank_inputEdt.getViewText()
                )
            }


        }

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

    override fun onLoadMoreSuccess(list: List<BankDetail>?, hasMore: Boolean) {
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
        isGoodsName = false

    }

    override fun onShiftGoodsName() {
        isGoodsName = true

    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onRefreshEvent(event: RefreshGoodsStatusEvent) {
        mLoadMoreDelegate?.refresh()
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


    override fun useEventBus(): Boolean {
        return true
    }

    override fun useLightMode(): Boolean {
        return false
    }

    override fun autoRefresh(): Boolean {
        return false
    }
}
