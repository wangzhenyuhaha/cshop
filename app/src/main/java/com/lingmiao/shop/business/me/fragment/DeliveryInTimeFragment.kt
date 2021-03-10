package com.lingmiao.shop.business.me.fragment

import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.james.common.base.BaseFragment
import com.lingmiao.shop.R
import com.lingmiao.shop.business.me.presenter.DeliveryInTimePresenter
import com.lingmiao.shop.business.me.presenter.impl.DeliveryInTimePresenterImpl
import com.lingmiao.shop.business.tools.adapter.PriceAdapter
import com.lingmiao.shop.business.tools.bean.DistanceSection
import kotlinx.android.synthetic.main.tools_include_model_price.*

/**
Create Date : 2021/3/53:40 PM
Auther      : Fox
Desc        :
 **/
class DeliveryInTimeFragment : BaseFragment<DeliveryInTimePresenter>(), DeliveryInTimePresenter.View {

    companion object {
        fun newInstance(): DeliveryInTimeFragment {
            return DeliveryInTimeFragment()
        }
    }

    override fun getLayoutId(): Int? {
        return R.layout.me_fragment_delivery_in_time;
    }

    override fun createPresenter(): DeliveryInTimePresenter? {
        return DeliveryInTimePresenterImpl(this);
    }

    private lateinit var mPriceList : MutableList<DistanceSection>;

    private lateinit var mPriceAdapter : PriceAdapter;

    override fun initViewsAndData(rootView: View) {
        initPricePart();
    }

    private fun initPricePart() {
        mPriceList = arrayListOf();
        mPriceList.add(DistanceSection());
        mPriceAdapter = PriceAdapter().apply {
            setOnItemChildClickListener { adapter, view, position ->
                val item = adapter.data[position] as DistanceSection;
                if (view.id == R.id.tv_model_price_delete && position != 0) {
                    mPriceList.remove(item);
                    mPriceAdapter.replaceData(mPriceList);
                }
            }

            val footer = View.inflate(context, R.layout.tools_footer_model_add, null);
            footer.findViewById<View>(R.id.tv_model_add).setOnClickListener {
                mPriceList.add(DistanceSection());
                mPriceAdapter.replaceData(mPriceList);
            }
            addFooterView(footer)
        };

        rv_model_price.apply {
            layoutManager = initLayoutManager()
            adapter = mPriceAdapter;
        }

        mPriceAdapter.replaceData(mPriceList);
    }

    private fun initLayoutManager(): RecyclerView.LayoutManager {
        return LinearLayoutManager(activity)
    }

}