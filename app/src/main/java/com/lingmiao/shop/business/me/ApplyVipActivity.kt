package com.lingmiao.shop.business.me

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.text.TextUtils
import com.james.common.base.BaseActivity
import com.james.common.utils.exts.gone
import com.james.common.utils.exts.singleClick
import com.james.common.utils.exts.visiable
import com.lingmiao.shop.R
import com.lingmiao.shop.base.IWXConstant
import com.lingmiao.shop.base.UserManager
import com.lingmiao.shop.business.goods.api.bean.WxPayReqVo
import com.lingmiao.shop.business.me.bean.IdentityVo
import com.lingmiao.shop.business.me.bean.My
import com.lingmiao.shop.business.me.bean.PersonInfoRequest
import com.lingmiao.shop.business.me.bean.VipType
import com.lingmiao.shop.business.me.event.PaySuccessEvent
import com.lingmiao.shop.business.me.presenter.ApplyVipPresenter
import com.lingmiao.shop.business.me.presenter.impl.ApplyVipPreImpl
import com.lingmiao.shop.business.wallet.WalletInfoActivity
import com.lingmiao.shop.business.wallet.bean.WalletVo
import com.lingmiao.shop.util.GlideUtils
import com.lingmiao.shop.util.formatDouble
import com.tencent.mm.opensdk.modelpay.PayReq
import com.tencent.mm.opensdk.openapi.IWXAPI
import com.tencent.mm.opensdk.openapi.WXAPIFactory
import kotlinx.android.synthetic.main.me_activity_apply_vip.*
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode


/**
*   开通会员
*/
class ApplyVipActivity : BaseActivity<ApplyVipPresenter>(),ApplyVipPresenter.View {

    var mUserInfo : My? = null;

    var mIdentity : IdentityVo? = null;

    var mWallet : WalletVo? = null;

    var api : IWXAPI? = null;

    var isPayed : Boolean = false;

    companion object {
        fun openActivity(context: Context, my : My?, identity : IdentityVo?) {
            if (context is Activity) {
                val intent = Intent(context, ApplyVipActivity::class.java)
                intent.putExtra("clerk", my)
                intent.putExtra("identity", identity);
                context.startActivity(intent)
            }
        }
    }

    override fun initBundles() {
        mUserInfo = intent?.getParcelableExtra<My>("clerk");
        mIdentity = intent?.getSerializableExtra("identity") as? IdentityVo;
    }

    override fun getLayoutId(): Int {
        return R.layout.me_activity_apply_vip
    }

    override fun createPresenter(): ApplyVipPresenter {
        return ApplyVipPreImpl(this)
    }

    override fun useLightMode(): Boolean {
        return false;
    }

    override fun initView() {
        api = WXAPIFactory.createWXAPI(this, IWXConstant.APP_ID);
        mToolBarDelegate?.setMidTitle("开通会员")
        mPresenter?.getVipPriceList();

        // 保障金支付
        tvVip.singleClick {
            mPresenter?.depositApply(mWallet);
        }
        // 保障金[退款]
        tvRecharge.singleClick {

        }
        // 保障金[充值]
        tvRefund.singleClick {
            WalletInfoActivity.openDepositActivity(this);
        }
        tvApply.singleClick {
            val list = galleryRv.getSelectItems();
            if(list?.size > 0) {
                val item = list.get(0);
                mPresenter?.apply(item.id!!);
            }
        }
        setUserInfo();
        mPresenter?.loadWalletData();
        if(mIdentity == null) {
            mPresenter?.getIdentity();
        } else {
            onSetVipInfo(mIdentity);
        }
    }

    fun setUserInfo() {
        val loginInfo = UserManager.getLoginInfo()
        tvMyShopName.setText(loginInfo?.shopName)
        tvMyName.setText(mUserInfo?.uname)
        if(!TextUtils.isEmpty(mUserInfo?.face)){
            GlideUtils.setImageUrl(ivMyHead,mUserInfo?.face)
        }
        //ivMyShopStatus.visibility = if(loginInfo?.shopStatus=="OPEN") View.VISIBLE else View.GONE
    }

    override fun onSetVipPriceList(list: List<VipType>?) {
        galleryRv.setDataList(list);
    }


    override fun onSetVipInfo(item : IdentityVo?) {
        dueDateTv.setText(String.format("试用时间%s到期,购买后有效期将顺延", item?.get_DueDateStr()));

        if(item?.isVip() == true) {
            dueDateTv.setText(String.format("%s%s到期,购买后有效期将顺延", "会员时间", item.get_DueDateStr()));
            tvApply.setText("续费")
            ivMyShopVipStatus.setImageResource(R.mipmap.ic_try_logo)
            ivMyShopStatus.visiable();
        } else {
            dueDateTv.setText(String.format("%s%s到期,购买后有效期将顺延", "试用时间", item?.get_DueDateStr()));
            tvApply.setText("立即开通")
            ivMyShopVipStatus.setImageResource(R.mipmap.ic_vip_period)
            ivMyShopStatus.gone();
        }
    }

    override fun onApplySuccess(item : WxPayReqVo?) {
        if(api?.isWXAppInstalled ==true) {
            val payReq = item?.getPayData();
            if(payReq != null) {
                api?.sendReq(payReq);
            }
        } else {
            showToast("请安装微信支付");
        }
    }

    override fun onDepositApplied(item: WxPayReqVo?) {
        if(api?.isWXAppInstalled ==true) {
            val payReq = item?.getPayData();
            if(payReq != null) {
                api?.sendReq(payReq);
            }
        } else {
            showToast("请安装微信支付");
        }
    }

    /**
     * 加载成功
     */
    override fun onLoadWalletDataSuccess(data: WalletVo?) {
        mWallet = data;
        tvBalance.setText(getString(R.string.wallet_value, formatDouble(data?.balanceAccount?.balanceAmount?:0.0)));
        if(data?.depositAccount?.balanceAmount?:0.0 > 0.0) {
            tvVip.gone();
            tvRefund.visiable()
            tvRecharge.visiable()
            tvDepositStatus.setText(getString(R.string.vip_deposit_applied))
        } else {
            tvVip.visiable();
            tvRefund.gone()
            tvRecharge.gone()
            tvDepositStatus.setText(getString(R.string.vip_deposit_apply))
        }
    }

    /**
     * 加载失败
     */
    override fun onLoadWalletDataError(code: Int) {

    }

    override fun useEventBus(): Boolean {
        return true;
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onPayed(event: PaySuccessEvent) {
        isPayed = true;
        mPresenter?.loadWalletData();
        mPresenter?.getIdentity();
    }

    override fun onBackPressed() {
        super.onBackPressed()
        refreshUseVipStatus();
    }

    override fun onDestroy() {
        super.onDestroy()
        refreshUseVipStatus();
    }

    fun refreshUseVipStatus() {
        if(isPayed) {
            EventBus.getDefault().post(PersonInfoRequest())
        }
    }

}