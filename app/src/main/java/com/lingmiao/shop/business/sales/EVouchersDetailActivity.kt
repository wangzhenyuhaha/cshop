package com.lingmiao.shop.business.sales

import android.content.Context
import android.content.Intent
import com.james.common.base.BaseVBActivity
import com.lingmiao.shop.business.sales.bean.Coupon
import com.lingmiao.shop.business.sales.bean.ElectronicVoucher
import com.lingmiao.shop.business.sales.presenter.EVouchersDetailPresenter
import com.lingmiao.shop.business.sales.presenter.impl.EVouchersDetailPreImpl
import com.lingmiao.shop.databinding.ActivityEvouchersDetailBinding

class EVouchersDetailActivity :
    BaseVBActivity<ActivityEvouchersDetailBinding, EVouchersDetailPresenter>(),
    EVouchersDetailPresenter.View {

    companion object {

        //0  查看电子券详情   1  新增电子券
        private const val ELECTRONIC_VOUCHERS_TYPE = "ELECTRONIC_VOUCHERS_TYPE"

        //显示的优惠券
        private const val ELECTRONIC_VOUCHERS = "ELECTRONIC_VOUCHERS"

        fun openActivity(
            context: Context,
            type: Int,
            item: ElectronicVoucher = ElectronicVoucher()
        ) {
            val intent = Intent(context, EVouchersDetailActivity::class.java)
            intent.putExtra(ELECTRONIC_VOUCHERS_TYPE, type)
            intent.putExtra(ELECTRONIC_VOUCHERS, item)
            context.startActivity(intent)
        }

    }

    override fun createPresenter() = EVouchersDetailPreImpl(this, this)

    override fun getViewBinding() = ActivityEvouchersDetailBinding.inflate(layoutInflater)

    //0 查看电子券详情 1 新增电子券
    private var type: Int = 0

    //电子券
    private var electronicVouchers: ElectronicVoucher = ElectronicVoucher()


    override fun initBundles() {
        type = intent.getIntExtra(ELECTRONIC_VOUCHERS_TYPE, 0)
        electronicVouchers = intent.getSerializableExtra(ELECTRONIC_VOUCHERS) as ElectronicVoucher
    }

    override fun initView() {

        if (type == 0) {
            mToolBarDelegate?.setMidTitle("电子券")
        } else {
            mToolBarDelegate?.setMidTitle("新增电子券")
        }

        if (type == 1) {
            addElectronicVoucher()
        } else {
            readElectronicVoucher()
        }

    }

    private fun addElectronicVoucher() {

    }

    private fun readElectronicVoucher() {

    }

    override fun useLightMode() = false


}