package com.lingmiao.shop.business.me

import android.app.Activity
import android.content.Context
import android.content.Intent
import com.blankj.utilcode.util.ActivityUtils
import com.james.common.base.BaseActivity
import com.james.common.utils.exts.gone
import com.james.common.utils.exts.singleClick
import com.james.common.utils.exts.visiable
import com.lingmiao.shop.R
import com.lingmiao.shop.base.IWXConstant
import com.lingmiao.shop.base.UserManager
import com.lingmiao.shop.business.goods.api.bean.WxPayReqVo
import com.lingmiao.shop.business.main.MainActivity
import com.lingmiao.shop.business.main.UserServiceH5Activity
import com.lingmiao.shop.business.me.bean.IdentityVo
import com.lingmiao.shop.business.me.bean.My
import com.lingmiao.shop.business.me.bean.PersonInfoRequest
import com.lingmiao.shop.business.me.bean.VipType
import com.lingmiao.shop.business.me.event.ApplyVipEvent
import com.lingmiao.shop.business.me.event.PaySuccessEvent
import com.lingmiao.shop.business.me.presenter.ApplyVipPresenter
import com.lingmiao.shop.business.me.presenter.impl.ApplyVipPreImpl
import com.lingmiao.shop.business.wallet.bean.WalletVo
import com.lingmiao.shop.business.wallet.bean.WithdrawAccountVo
import com.lingmiao.shop.util.GlideUtils
import com.lingmiao.shop.util.formatDouble
import com.tencent.mm.opensdk.openapi.IWXAPI
import com.tencent.mm.opensdk.openapi.WXAPIFactory
import kotlinx.android.synthetic.main.me_activity_apply_vip.*
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode


/**
 *   开通会员
 */
class ApplyVipActivity : BaseActivity<ApplyVipPresenter>(), ApplyVipPresenter.View {

    var mUserInfo: My? = null

    var mIdentity: IdentityVo? = null

    var mWallet: WalletVo? = null

    var api: IWXAPI? = null

    var isPayed: Boolean = false

    //保障金状态
    var status: Int? = 1

    //原因
    var inspect_remark: String = ""

    companion object {
        fun openActivity(context: Context, my: My?, identity: IdentityVo?) {
            if (context is Activity) {
                val intent = Intent(context, ApplyVipActivity::class.java)
                intent.putExtra("clerk", my)
                intent.putExtra("identity", identity)
                context.startActivity(intent)
            }
        }
    }

    override fun initBundles() {
        mUserInfo = intent?.getParcelableExtra<My>("clerk")
        mIdentity = intent?.getSerializableExtra("identity") as? IdentityVo
    }

    override fun getLayoutId() = R.layout.me_activity_apply_vip

    override fun createPresenter() = ApplyVipPreImpl(this)

    override fun useLightMode() = false

    override fun initView() {

        api = WXAPIFactory.createWXAPI(this, IWXConstant.APP_ID)
        mToolBarDelegate?.setMidTitle("开通会员")
        mPresenter?.getVipPriceList()

        // 保障金 缴纳
        tvVip.singleClick {
            mPresenter?.depositApply(mWallet)
        }

        // 保障金 退款
        tvRefund.singleClick {
            mIdentity?.id?.let { it1 ->
                mPresenter.ensureRefund(it1)
            }
        }

        //立即开通
        tvApply.singleClick {
            val list = galleryRv.getSelectItems()
            if (list.isNotEmpty()) {
                val item = galleryRv.getCheckedItem()
                mPresenter?.apply(item.id!!)
            }
        }

        securityLayout.singleClick {
            UserServiceH5Activity.security(this, 3)
        }
        setUserInfo()

        //查询提现状态
        mPresenter.getApplyVipReason()


        if (mIdentity == null) {
            mPresenter?.getIdentity()
        } else {
            onSetVipInfo(mIdentity)
        }
    }

    private fun setUserInfo() {
        val loginInfo = UserManager.getLoginInfo()
        loginInfo?.apply {
            GlideUtils.setImageUrl(ivMyHead, shopLogo)
            tvMyShopName.text = shopName
        }
        tvMyName.text = mUserInfo?.uname
        //ivMyShopStatus.visibility = if(loginInfo?.shopStatus=="OPEN") View.VISIBLE else View.GONE
    }

    override fun onSetVipPriceList(list: List<VipType>?) {
        galleryRv.setDataList(list)
    }


    override fun onSetVipInfo(item: IdentityVo?) {
        dueDateTv.text = String.format("试用时间%s到期,购买后有效期将顺延", item?.get_DueDateStr())

        if (item?.isVip() == true) {
            dueDateTv.text = String.format("%s%s到期,购买后有效期将顺延", "会员时间", item.get_DueDateStr())
            tvApply.text = "续费"
            ivMyShopVipStatus.setImageResource(R.mipmap.ic_try_logo)
            ivMyShopStatus.visiable()
        } else {
            dueDateTv.text = String.format("%s%s到期,购买后有效期将顺延", "试用时间", item?.get_DueDateStr())
            tvApply.text = "立即开通"
            ivMyShopVipStatus.setImageResource(R.mipmap.ic_vip_period)
            ivMyShopStatus.gone()
        }
    }

    override fun onApplySuccess(value: WxPayReqVo?) {
        if (api?.isWXAppInstalled == true) {
            val payReq = value?.getPayData()
            if (payReq != null) {
                api?.sendReq(payReq)
            }
        } else {
            showToast("请安装微信支付")
        }
    }

    override fun onDepositApplied(value: WxPayReqVo?) {
        if (api?.isWXAppInstalled == true) {
            val payReq = value?.getPayData()
            if (payReq != null) {
                api?.sendReq(payReq)
            }
        } else {
            showToast("请安装微信支付")
        }
    }

    override fun onRefundEnsured() {
        showToast("退款中...")
        ActivityUtils.finishToActivity(MainActivity::class.java, false)
    }

    override fun onRefundEnsureFail() {

    }

    override fun onVipReason(data: ApplyVipPreImpl.ApplyVipReason) {
        status = data.status
        inspect_remark = data.inspect_remark
        mPresenter?.loadWalletData()
    }

    /**
     * 加载成功
     */
    override fun onLoadWalletDataSuccess(data: WalletVo?) {
        mWallet = data
        //已经缴纳的保障金
        tvBalance.text = getString(
            R.string.wallet_value,
            formatDouble(data?.depositAccount?.balanceAmount ?: 0.0)
        )
        if (status == null) {
            //从未进行过退款
            if (data?.depositAccount?.balanceAmount ?: 0.0 > 0.0) {
                //此时已近成功缴纳保障金
                tvVip.gone()
                tvRefund.visiable()
                tvRecharge.visiable()
                tvDepositStatus.text = getString(R.string.vip_deposit_applied)
            } else {
                //此时未缴纳保障金
                tvVip.visiable()
                tvRefund.gone()
                tvRecharge.gone()
                tvDepositStatus.text = getString(R.string.vip_deposit_apply)
            }
        } else {
            //进行过退款
            //0提现中
            when (status) {
                0 -> {
                    tvVip.gone()
                    tvRefund.gone()
                    tvRecharge.gone()
                    tvRefundStatus.visiable()
                    tvRefundStatus.text = "退款中"
                    tvDepositStatus.text = getString(R.string.vip_deposit_apply)
                }
                1 -> {
                    // 1 审核通过
                    tvVip.gone()
                    tvRefund.gone()
                    tvRecharge.gone()
                    tvRefundStatus.visiable()
                    tvRefundStatus.text = "退款通过，请等待。。"
                    tvDepositStatus.text = getString(R.string.vip_deposit_applied)
                }
                2 -> {
                    //2审核拒绝
                    tvVip.gone()
                    tvRefund.visiable()
                    tvRecharge.gone()
                    tvRefundStatus.visiable()
                    tvRefundStatus.text = "退款失败"
                    tvreason.visiable()
                    tvreason.text = inspect_remark
                    tvDepositStatus.text = getString(R.string.vip_deposit_apply)
                }
                3 -> {
                    //3提现成功
                    if (data?.depositAccount?.balanceAmount ?: 0.0 > 0.0) {
                        //此时已近成功缴纳保障金
                        tvVip.gone()
                        tvRefund.visiable()
                        tvRecharge.visiable()
                        tvDepositStatus.text = getString(R.string.vip_deposit_applied)
                    } else {
                        //此时未缴纳保障金
                        tvVip.visiable()
                        tvRefund.gone()
                        tvRecharge.gone()
                        tvDepositStatus.text = getString(R.string.vip_deposit_apply)
                    }
                }
            }
        }

        hideDialogLoading()
    }

    override fun onLoadedAccount(data: WithdrawAccountVo?) {

    }

    /**
     * 加载失败
     */
    override fun onLoadWalletDataError(code: Int) {

    }

    override fun useEventBus(): Boolean {
        return true
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onPayed(event: PaySuccessEvent) {
        isPayed = true
        mPresenter?.loadWalletData()
        mPresenter?.getIdentity()
    }

    override fun onBackPressed() {
        super.onBackPressed()
        refreshUseVipStatus()
    }

    override fun onDestroy() {
        super.onDestroy()
        refreshUseVipStatus()
    }

    fun refreshUseVipStatus() {
        if (isPayed) {
            EventBus.getDefault().post(ApplyVipEvent(from = 1))
            EventBus.getDefault().post(PersonInfoRequest())
        }
    }

}