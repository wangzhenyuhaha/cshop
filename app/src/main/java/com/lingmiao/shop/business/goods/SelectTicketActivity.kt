package com.lingmiao.shop.business.goods

import android.annotation.SuppressLint
import android.app.Activity
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.james.common.base.loadmore.BaseLoadMoreActivity
import com.james.common.base.loadmore.core.IPage
import com.james.common.utils.exts.singleClick
import com.lingmiao.shop.R
import com.lingmiao.shop.business.goods.adapter.TicketSelectOneAdapter
import com.lingmiao.shop.business.goods.presenter.SelectTicketPre
import com.lingmiao.shop.business.goods.presenter.impl.SelectTicketPreImpl
import com.lingmiao.shop.business.sales.bean.ElectronicVoucher
import com.lingmiao.shop.widget.EmptyView
import kotlinx.android.synthetic.main.sales_activity_goods.*

@SuppressLint("NotifyDataSetChanged")
class SelectTicketActivity : BaseLoadMoreActivity<ElectronicVoucher, SelectTicketPre>(),
    SelectTicketPre.View {

    override fun createPresenter() = SelectTicketPreImpl(this, this)

    override fun useLightMode() = false

    //选中的电子券名称
    private var ticketID: Int? = null

    //选中的电子券ID
    private var ticketName: String? = null


    override fun initOthers() {

        mToolBarDelegate.setMidTitle("选择电子券")

        submitTv.singleClick {
            intent.putExtra("goods_id", ticketID)
            intent.putExtra("goods_name", ticketName)
            setResult(Activity.RESULT_OK, intent)
            finish()
        }
    }

    override fun getLayoutId() = R.layout.activity_select_ticket

    override fun initAdapter(): BaseQuickAdapter<ElectronicVoucher, BaseViewHolder> {
        return TicketSelectOneAdapter().apply {
            //menuIv
            setOnItemChildClickListener { adapter, view, position ->
                val item = mAdapter.getItem(position) as ElectronicVoucher
                if (view.id == R.id.menuIv) {
                    setItem(item.couponID)
                    ticketID = item.couponID
                    ticketName = item.title
                    adapter.notifyDataSetChanged()
                }
            }
            emptyView = EmptyView(context).apply {
                setBackgroundResource(R.color.common_bg)
            }
        }
    }

    override fun executePageRequest(page: IPage) {
        mPresenter.loadListData(page, mAdapter.data)
    }


}