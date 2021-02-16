package com.lingmiao.shop.business.tuan

import android.app.Activity
import android.content.Context
import android.content.Intent
import com.lingmiao.shop.R
import com.lingmiao.shop.base.IConstant
import com.lingmiao.shop.business.tuan.bean.OrderVo
import com.lingmiao.shop.business.tuan.presenter.OrderDetailPresenter
import com.lingmiao.shop.business.tuan.presenter.impl.OrderDetailPresenterImpl
import com.james.common.base.BaseActivity

class OrderDetailActivity : BaseActivity<OrderDetailPresenter>(), OrderDetailPresenter.View{

    private lateinit var item: OrderVo;

    companion object {
        fun open(context: Context, vo: OrderVo) {
            if (context is Activity) {
                val intent = Intent(context, OrderDetailActivity::class.java)
                intent.putExtra(IConstant.BUNDLE_KEY_OF_ITEM, vo)
                context.startActivity(intent)
            }
        }
    }

    override fun initBundles() {
        item = intent?.getSerializableExtra(IConstant.BUNDLE_KEY_OF_ITEM) as OrderVo;
    }

    override fun createPresenter(): OrderDetailPresenter {
        return OrderDetailPresenterImpl(this);
    }

    override fun getLayoutId(): Int {
        return R.layout.tuan_activity_order_detail;
    }

    override fun initView() {

    }

}