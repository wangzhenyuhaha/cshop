package com.lingmiao.shop.business.me

import android.view.View
import com.james.common.base.BaseVBActivity
import com.james.common.utils.DialogUtils
import com.lingmiao.shop.business.me.presenter.WeChatApprovePresenter
import com.lingmiao.shop.business.me.presenter.impl.WeChatApprovePresenterImpl
import com.lingmiao.shop.databinding.ActivityShopWeChatApproveBinding

class ShopWeChatApproveActivity :
    BaseVBActivity<ActivityShopWeChatApproveBinding, WeChatApprovePresenter>(),
    WeChatApprovePresenter.View {


    override fun createPresenter(): WeChatApprovePresenter {
        return WeChatApprovePresenterImpl(this)
    }

    override fun getViewBinding() = ActivityShopWeChatApproveBinding.inflate(layoutInflater)

    override fun useBaseLayout(): Boolean {
        return true;
    }

    override fun onBackPressed() {
        DialogUtils.showDialog(context!!, "商户认证", "认证成功后才能正常结算，确认微信商户认证成功？",
            "取消", "确认已认证", View.OnClickListener {
                super.onBackPressed();
            }, View.OnClickListener {
                mPresenter?.approve();
            });
    }

    override fun initView() {
        mToolBarDelegate?.setMidTitle("微信认证")
    }

    override fun approved() {
        super.onBackPressed();
    }

}