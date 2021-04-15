package com.lingmiao.shop.business.sales

import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.james.common.base.BaseActivity
import com.james.common.utils.exts.singleClick
import com.lingmiao.shop.R
import com.lingmiao.shop.business.goods.GoodsManagerActivity
import com.lingmiao.shop.business.goods.GoodsSalesSelectActivity
import com.lingmiao.shop.business.sales.adapter.ActivitySalesAdapter
import com.lingmiao.shop.business.sales.bean.SalesActivityItemVo
import com.lingmiao.shop.business.sales.presenter.ISalesEditPresenter
import com.lingmiao.shop.business.sales.presenter.impl.SalesActivityEditPreImpl
import kotlinx.android.synthetic.main.sales_activity_activity_edit.*

/**
Create Date : 2021/3/126:05 AM
Auther      : Fox
Desc        :
 **/
class SalesActivityEditActivity : BaseActivity<ISalesEditPresenter>(), ISalesEditPresenter.PubView {
    override fun createPresenter(): ISalesEditPresenter {
        return SalesActivityEditPreImpl(this, this);
    }

    override fun getLayoutId(): Int {
        return R.layout.sales_activity_activity_edit;
    }

    override fun useLightMode(): Boolean {
        return false;
    }

    override fun initView() {
        mToolBarDelegate.setMidTitle(getString(R.string.sales_activity_edit_title))

        activityGoodsPickTv.singleClick {
            GoodsSalesSelectActivity.sales(context, "3");
        }
        initPricePart();
    }


    private lateinit var mDiscountList : MutableList<SalesActivityItemVo>;

    private lateinit var mDiscountAdapter : ActivitySalesAdapter;

    private fun initPricePart() {
        mDiscountList = arrayListOf();
        mDiscountList.add(SalesActivityItemVo());
        mDiscountAdapter = ActivitySalesAdapter().apply {
            setOnItemChildClickListener { adapter, view, position ->
                val item = adapter.data[position] as SalesActivityItemVo;
                if (view.id == R.id.discountDeleteTv && position != 0) {
                    mDiscountList.remove(item);
                    mDiscountAdapter.replaceData(mDiscountList);
                }
            }

            val footer = View.inflate(context, R.layout.sales_footer_activity_add, null);
            footer.findViewById<View>(R.id.activityAddTv).setOnClickListener {
                mDiscountList.add(SalesActivityItemVo());
                mDiscountAdapter.replaceData(mDiscountList);
            }
            addFooterView(footer)
        };

        activityRv.apply {
            layoutManager = initLayoutManager()
            adapter = mDiscountAdapter;
        }

        mDiscountAdapter.replaceData(mDiscountList);
    }

    private fun initLayoutManager(): RecyclerView.LayoutManager {
        return LinearLayoutManager(context)
    }
}