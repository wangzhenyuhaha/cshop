package com.lingmiao.shop.business.common.ui

import android.content.Context
import androidx.lifecycle.lifecycleScope
import com.james.common.base.BasePreImpl
import com.james.common.base.BasePresenter
import com.james.common.base.BaseVBActivity
import com.james.common.base.BaseView
import com.james.common.utils.DialogUtils
import com.lingmiao.shop.business.order.api.OrderRepository
import com.lingmiao.shop.databinding.ActivityDialogBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class DialogActivity : BaseVBActivity<ActivityDialogBinding, DialogPre>(),
    DialogPre.View {

    //    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        setContentView(R.layout.activity_dialog)
//        window.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

    //
//    }
    override fun createPresenter() = DialogPreImpl(this, this)

    override fun getViewBinding() = ActivityDialogBinding.inflate(layoutInflater)

    override fun initView() {
        val orderSN: String? = intent.getStringExtra("ORDER_SN")

        DialogUtils.showCancelableDialog(
            false,
            this,
            "骑手未接单",
            "骑手未接订单，请选择处理方式",
            "取消订单",
            "改为商家配送",
            {
                lifecycleScope.launch(Dispatchers.Main)
                {
                    //取消订单
                    if (orderSN != null) {
                        showDialogLoading()
                        val resp = OrderRepository.apiService.orderCancel(orderSN, " ", " ", " ")
                        hideDialogLoading()
                        if (resp.isSuccessful) {
                            showToast("订单取消成功")
                        } else {
                            showToast("订单取消失败")
                        }
                        finish()
                    } else {
                        showToast("订单取消失败")
                        finish()
                    }
                }

            },
            {
                lifecycleScope.launch(Dispatchers.Main)
                {
                    //订单转商家配送
                    if (orderSN != null) {
                        showDialogLoading()
                        val resp = OrderRepository.apiService.updateShippingTypeToLocal(orderSN)
                        hideDialogLoading()
                        if (resp.isSuccessful) {
                            showToast("订单转商家配送成功")
                        } else {
                            showToast("订单转商家配送失败")
                        }
                        finish()
                    } else {
                        showToast("订单转商家配送失败")
                        finish()
                    }

                }
            })
    }
}

interface DialogPre : BasePresenter {

    interface View : BaseView {

    }
}

class DialogPreImpl(val context: Context, val view: DialogPre.View) :
    BasePreImpl(view), DialogPre {

}