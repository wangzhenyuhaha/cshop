package com.lingmiao.shop.business.order

import android.app.Activity
import com.blankj.utilcode.util.NumberUtils
import com.lingmiao.shop.R
import com.lingmiao.shop.business.order.presenter.UpdatePricePresenter
import com.lingmiao.shop.business.order.presenter.impl.UpdatePricePresenterImpl
import com.james.common.base.BaseActivity
import com.james.common.netcore.coroutine.CoroutineSupport
import kotlinx.android.synthetic.main.order_activity_update_price.*

/**
 *   修改价格
 */
class UpdatePriceActivity : BaseActivity<UpdatePricePresenter>(), UpdatePricePresenter.View {


    private val mCoroutine: CoroutineSupport by lazy { CoroutineSupport() }

    private var orderAmount: Double = 0.0
    private var shippingAmount: Double = 0.0
    private var orderId: String? = null

    override fun getLayoutId(): Int {
        return R.layout.order_activity_update_price
    }

    override fun initView() {
        mToolBarDelegate.setMidTitle("修改价格")
        orderAmount = intent.getDoubleExtra("orderAmount", 0.0)
        shippingAmount = intent.getDoubleExtra("shippingAmount", 0.0)
        orderId = intent.getStringExtra("orderId")
        tvOrderMoney.text = NumberUtils.format(orderAmount-shippingAmount,2)
        tvLogisticsMoney.text =  NumberUtils.format(shippingAmount,2)


        tvUpdatePrice.setOnClickListener { updateMoney() }

    }

    private fun updateMoney() {
        showDialogLoading()
        mPresenter.requestUpdatePriceData(
            orderId,
            etOrderMoney.text.toString(),
            etLogisticsMoney.text.toString()
        )
    }


    override fun createPresenter(): UpdatePricePresenter {
        return UpdatePricePresenterImpl(this, this)
    }

    override fun onUpdatePriceSuccess() {
        hideDialogLoading()
        if(isFinishing) return
        showToast("修改成功")
        setResult(Activity.RESULT_OK)
        finish()
    }

    override fun onUpdatePriceError(code: Int) {
        hideDialogLoading()
    }

}