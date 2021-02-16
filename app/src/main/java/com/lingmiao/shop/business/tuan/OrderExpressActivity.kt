package com.lingmiao.shop.business.tuan

import android.app.Activity
import android.content.Context
import android.content.Intent
import com.lingmiao.shop.R
import com.lingmiao.shop.base.IConstant
import com.lingmiao.shop.business.tuan.presenter.ExpressInfoPresenter
import com.lingmiao.shop.business.tuan.presenter.impl.ExpressInfoPresenterImpl
import com.james.common.base.BaseActivity

class OrderExpressActivity : BaseActivity<ExpressInfoPresenter>(), ExpressInfoPresenter.View {

    companion object {
        fun open(context: Context, id: String) {
            if (context is Activity) {
                val intent = Intent(context, OrderExpressActivity::class.java)
                intent.putExtra(IConstant.BUNDLE_KEY_OF_ITEM_ID, id)
                context.startActivity(intent)
            }
        }
    }

    override fun createPresenter(): ExpressInfoPresenter {
        return ExpressInfoPresenterImpl(this);
    }

    override fun initView() {

    }

    override fun getLayoutId(): Int {
        return R.layout.tuan_activity_order_express;
    }

}