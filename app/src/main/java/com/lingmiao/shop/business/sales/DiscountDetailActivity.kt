package com.lingmiao.shop.business.sales

import android.content.Context
import android.content.Intent
import com.james.common.base.BaseVBActivity
import com.lingmiao.shop.business.sales.presenter.DiscountDetailPre
import com.lingmiao.shop.business.sales.presenter.impl.DiscountDetailPreImpl
import com.lingmiao.shop.databinding.ActivityDiscountDetailBinding

class DiscountDetailActivity : BaseVBActivity<ActivityDiscountDetailBinding, DiscountDetailPre>(),
    DiscountDetailPre.View {

    companion object {

        //0  查看优惠券详情   1  新增优惠券
        private const val DISCOUNT_TYPE = "DISCOUNT_TYPE"

        fun openActivity(context: Context, type: Int) {
            val intent = Intent(context, DiscountDetailActivity::class.java)
            intent.putExtra(DISCOUNT_TYPE, type)
            context.startActivity(intent)
        }

    }

    override fun createPresenter() = DiscountDetailPreImpl(this, this)

    override fun getViewBinding() = ActivityDiscountDetailBinding.inflate(layoutInflater)

    //0 查看优惠券详情 1 新增优惠券
    private var type: Int = 0

    override fun initBundles() {
        type = intent.getIntExtra(DISCOUNT_TYPE, 0)

    }

    override fun initView() {

        if (type == 0) {
            mToolBarDelegate?.setMidTitle("优惠券")
        } else {
            mToolBarDelegate?.setMidTitle("新增优惠券")
        }




    }

    override fun useLightMode() = false

}