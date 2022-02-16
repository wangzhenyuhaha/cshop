package com.lingmiao.shop.business.me

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.james.common.base.BaseVBActivity
import com.lingmiao.shop.business.me.presenter.ShopInfoPresenter
import com.lingmiao.shop.business.me.presenter.impl.ShopInfoPreImpl
import com.lingmiao.shop.business.me.presenter.impl.ShopSettingPresenterImpl
import com.lingmiao.shop.databinding.ActivityOperationSettingBinding
import com.lingmiao.shop.databinding.ActivityShopInfoBinding

class ShopInfoActivity :
    BaseVBActivity<ActivityShopInfoBinding, ShopInfoPresenter>(),
    ShopInfoPresenter.View {

    override fun useLightMode() = false

    override fun createPresenter() = ShopInfoPreImpl(this, this)

    override fun getViewBinding() = ActivityShopInfoBinding.inflate(layoutInflater)

    override fun initView() {
        mToolBarDelegate?.setMidTitle("店铺信息")

    }
}