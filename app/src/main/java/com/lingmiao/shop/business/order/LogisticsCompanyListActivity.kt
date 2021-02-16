package com.lingmiao.shop.business.order

import com.lingmiao.shop.R
import com.lingmiao.shop.business.order.bean.LogisticsCompanyList
import com.lingmiao.shop.business.order.presenter.LogisticsCompanyListPresenter
import com.lingmiao.shop.business.order.presenter.impl.LogisticsCompanyListPresenterImpl
import com.james.common.base.BaseActivity
import com.james.common.netcore.coroutine.CoroutineSupport
import kotlinx.android.synthetic.main.order_activity_logistics_company_list.*

/**
*   快递公司列表
*/
class LogisticsCompanyListActivity : BaseActivity<LogisticsCompanyListPresenter>(),LogisticsCompanyListPresenter.View {


    private val mCoroutine: CoroutineSupport by lazy { CoroutineSupport() }

    override fun getLayoutId(): Int {
        return R.layout.order_activity_logistics_company_list
    }

    override fun initView() {

        showPageLoading()

        mPresenter.requestLogisticsCompanyListData()
    }



    override fun createPresenter(): LogisticsCompanyListPresenter {
        return LogisticsCompanyListPresenterImpl(this, this)
    }

    override fun onLogisticsCompanyListSuccess(bean: LogisticsCompanyList) {
        hidePageLoading()
    }

    override fun onLogisticsCompanyListError(code: Int) {
        hidePageLoading()
    }

}