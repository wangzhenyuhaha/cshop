package com.lingmiao.shop.business.me

import com.james.common.base.BaseActivity
import com.lingmiao.shop.R
import com.lingmiao.shop.business.me.presenter.IShopQRCodePre
import com.lingmiao.shop.business.me.presenter.impl.ShopQRCodePreImpl
import com.lingmiao.shop.util.GlideUtils
import kotlinx.android.synthetic.main.me_activity_qr_code.*

/**
Create Date : 2021/6/43:07 PM
Auther      : Fox
Desc        :
 **/
class ShopQRCodeActivity : BaseActivity<IShopQRCodePre>(), IShopQRCodePre.View {
    override fun createPresenter(): IShopQRCodePre {
        return ShopQRCodePreImpl(this, this);
    }

    override fun getLayoutId(): Int {
        return R.layout.me_activity_qr_code;
    }

    override fun initView() {
        mToolBarDelegate.setMidTitle("我的二维码")

        mPresenter?.getQRCode();
    }

    override fun onSetQRCode(url: String) {
        GlideUtils.setImageUrl1(ivQRCode, url)
    }

    override fun onGetQRCodeFail() {

    }
}