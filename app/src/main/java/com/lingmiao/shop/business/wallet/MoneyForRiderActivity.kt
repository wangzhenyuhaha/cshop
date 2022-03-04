package com.lingmiao.shop.business.wallet

import android.content.Context
import android.content.Intent
import com.blankj.utilcode.util.FragmentUtils
import com.james.common.base.BaseVBActivity
import com.james.common.utils.DialogUtils
import com.james.common.utils.exts.singleClick
import com.lingmiao.shop.R
import com.lingmiao.shop.base.IWXConstant
import com.lingmiao.shop.business.goods.api.bean.WxPayReqVo
import com.lingmiao.shop.business.wallet.bean.AccountVo
import com.lingmiao.shop.business.wallet.fragment.MoneyForRiderFragment
import com.lingmiao.shop.business.wallet.presenter.MoneyForRiderPresenter
import com.lingmiao.shop.business.wallet.presenter.impl.MoneyForRiderPresenterImpl
import com.lingmiao.shop.databinding.ActivityMoneyForRiderBinding
import com.tencent.mm.opensdk.openapi.IWXAPI
import com.tencent.mm.opensdk.openapi.WXAPIFactory

class MoneyForRiderActivity :
    BaseVBActivity<ActivityMoneyForRiderBinding, MoneyForRiderPresenter>(),
    MoneyForRiderPresenter.View {

    //当前的骑手备付金情况
    private var account: AccountVo? = null

    var api: IWXAPI? = null

    companion object {
        fun openActivity(context: Context) {
            val intent = Intent(context, MoneyForRiderActivity::class.java)
            context.startActivity(intent)
        }
    }

    override fun createPresenter() = MoneyForRiderPresenterImpl(this, this)

    override fun getViewBinding() = ActivityMoneyForRiderBinding.inflate(layoutInflater)

    override fun useLightMode() = false

    override fun initView() {

        mToolBarDelegate?.setMidTitle("骑手备付金")

        api = WXAPIFactory.createWXAPI(this, IWXConstant.APP_ID)

        //查询骑手备付金
        mPresenter?.loadInfo()

        //充值
        mBinding.tvRecharge.singleClick {
            DialogUtils.showInputDialogNumberDecimal(
                this,
                "充值金额",
                "",
                "请输入您需要充值的金额",
                "",
                "取消",
                "充值",
                null
            ) {
                try {
                    val temp = it.toDouble()
                    mPresenter?.riderMoneyApply(account,temp)
                } catch (e: Exception) {
                    showToast("请输入数字")
                }
            }

        }

        //提现
        mBinding.tvWithdrawal.singleClick {

        }

        //加载骑手备付金交易记录
        FragmentUtils.add(
            supportFragmentManager,
            MoneyForRiderFragment.newInstance(),
            R.id.f_wallet_info
        )


    }

    override fun loadInfoSuccess(info: AccountVo?) {
        account = info
        val amount = info?.balanceAmount
        mBinding.tvWalletInfoValue.text = String.format("%s", amount ?: 0)
    }

    override fun loadInfoError(code: Int) {
        finish()
    }

    override fun onApplied(value: WxPayReqVo?) {
        if (api?.isWXAppInstalled == true) {
            val payReq = value?.getPayData()
            if (payReq != null) {
                api?.sendReq(payReq)
            }
        } else {
            showToast("请安装微信支付")
        }
    }


    override fun onResume() {
        super.onResume()
        mPresenter?.loadInfo()
    }
}