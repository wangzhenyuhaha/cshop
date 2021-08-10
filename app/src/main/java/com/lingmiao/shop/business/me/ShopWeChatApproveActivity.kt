package com.lingmiao.shop.business.me

import com.james.common.base.BasePreImpl
import com.james.common.base.BasePresenter
import com.james.common.base.BaseVBActivity
import com.lingmiao.shop.databinding.ActivityShopWeChatApproveBinding

class ShopWeChatApproveActivity :
    BaseVBActivity<ActivityShopWeChatApproveBinding, BasePresenter>() {


    override fun createPresenter(): BasePresenter {
        return BasePreImpl(this)
    }

    override fun getViewBinding() = ActivityShopWeChatApproveBinding.inflate(layoutInflater)


    override fun initView() {
        mToolBarDelegate?.setMidTitle("微信认证")
    }

}