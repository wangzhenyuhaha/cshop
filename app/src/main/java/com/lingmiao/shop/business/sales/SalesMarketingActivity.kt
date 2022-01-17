package com.lingmiao.shop.business.sales

import com.blankj.utilcode.util.ActivityUtils
import com.james.common.base.BaseVBActivity
import com.james.common.utils.exts.singleClick
import com.lingmiao.shop.business.sales.presenter.SalesMarketingPre
import com.lingmiao.shop.business.sales.presenter.impl.SalesMarketingPreImpl
import com.lingmiao.shop.databinding.ActivitySalesMarketingBinding


class SalesMarketingActivity : BaseVBActivity<ActivitySalesMarketingBinding, SalesMarketingPre>(),
    SalesMarketingPre.View {

    override fun createPresenter() = SalesMarketingPreImpl(this, this)

    override fun getViewBinding() = ActivitySalesMarketingBinding.inflate(layoutInflater)

    override fun initView() {

        mToolBarDelegate?.setMidTitle("营销活动")

        //优惠券
        mBinding.container1.singleClick {
            ActivityUtils.startActivity(DiscountActivity::class.java)
        }

        //满减活动
        mBinding.container2.singleClick {
            ActivityUtils.startActivity(SalesSettingActivity::class.java)
        }

        //电子券
        mBinding.container3.singleClick {
            ActivityUtils.startActivity(ElectronicVouchersActivity::class.java)
        }


    }

    override fun useLightMode() = false

}