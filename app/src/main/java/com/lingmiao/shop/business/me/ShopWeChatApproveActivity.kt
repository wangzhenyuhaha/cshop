package com.lingmiao.shop.business.me

import android.view.View
import com.james.common.base.BaseVBActivity
import com.james.common.utils.DialogUtils
import com.james.common.utils.exts.gone
import com.james.common.utils.exts.visiable
import com.lingmiao.shop.base.ShopStatusConstants
import com.lingmiao.shop.base.UserManager
import com.lingmiao.shop.business.me.presenter.WeChatApprovePresenter
import com.lingmiao.shop.business.me.presenter.impl.WeChatApprovePresenterImpl
import com.lingmiao.shop.databinding.ActivityShopWeChatApproveBinding
import kotlinx.android.synthetic.main.activity_shop_we_chat_approve.*

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
                super.onBackPressed()
            }, View.OnClickListener {
                mPresenter?.approve()
                super.onBackPressed()
            });
    }

    override fun initView() {
        mToolBarDelegate?.setMidTitle("微信认证")
        if(ShopStatusConstants.isFinalOpen(UserManager.getLoginInfo()?.shopStatus)) {
            warningLayout.gone();
        } else {
            warningLayout.visiable();
        }
    }

    override fun approved() {
        super.onBackPressed();
    }

}