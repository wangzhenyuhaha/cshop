package com.lingmiao.shop.business.me

import com.james.common.base.BaseActivity
import com.lingmiao.shop.R
import com.lingmiao.shop.business.me.presenter.LinkInSettingPresenter
import com.lingmiao.shop.business.me.presenter.impl.LinkInSettingPresenterImpl
import kotlinx.android.synthetic.main.me_activity_link_in.*
import kotlinx.android.synthetic.main.tools_activity_logistics_tool.*

/**
Create Date : 2021/3/51:27 PM
Auther      : Fox
Desc        :
 **/
class LinkInSettingActivity : BaseActivity<LinkInSettingPresenter>(), LinkInSettingPresenter.View {

    override fun createPresenter(): LinkInSettingPresenter {
        return LinkInSettingPresenterImpl(this);
    }

    override fun initView() {
        mToolBarDelegate.setMidTitle(getString(R.string.link_in_title))
        shopOperateSubmitTv.setOnClickListener {
            //
            linkTelEt;
            linkSb;
        }

    }

    override fun getLayoutId(): Int {
        return R.layout.me_activity_link_in;
    }
}