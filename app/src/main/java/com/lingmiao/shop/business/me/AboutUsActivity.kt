package com.lingmiao.shop.business.me

import com.james.common.base.BaseActivity
import com.james.common.base.BasePreImpl
import com.james.common.base.BasePresenter
import com.lingmiao.shop.R

/**
Create Date : 2021/5/1610:51 AM
Auther      : Fox
Desc        :
 **/
class AboutUsActivity : BaseActivity<BasePresenter>() {
    override fun createPresenter(): BasePresenter {
        return BasePreImpl(this)
    }

    override fun getLayoutId(): Int {
        return R.layout.me_activity_about_us;
    }

    override fun initView() {

    }
}