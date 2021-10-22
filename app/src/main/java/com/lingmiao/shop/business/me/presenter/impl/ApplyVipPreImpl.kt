package com.lingmiao.shop.business.me.presenter.impl

import com.google.gson.annotations.SerializedName
import com.james.common.base.BasePreImpl
import com.james.common.netcore.networking.http.core.awaitHiResponse
import com.lingmiao.shop.business.main.api.MainRepository
import com.lingmiao.shop.business.main.bean.IdBean
import com.lingmiao.shop.business.me.bean.RechargeReqVo
import com.lingmiao.shop.business.me.presenter.ApplyVipPresenter
import com.lingmiao.shop.business.wallet.bean.WalletVo
import com.lingmiao.shop.business.wallet.presenter.MyWalletPresenter
import com.lingmiao.shop.business.wallet.presenter.impl.MyWalletPresenterImpl
import kotlinx.coroutines.launch


class ApplyVipPreImpl(private var view: ApplyVipPresenter.View) : BasePreImpl(view),
    ApplyVipPresenter {

    var mDepositMoney: Double? = 0.0

    private val mWallet: MyWalletPresenter by lazy { MyWalletPresenterImpl(view) }

    override fun getVipPriceList() {
        mCoroutine.launch {
            val resp = MainRepository.apiService.getVipList().awaitHiResponse()
            handleResponse(resp) {
                view.onSetVipPriceList(resp.data?.data?.shopProductList)
                mDepositMoney = resp.data?.data?.depositMoney ?: 0.0
            }
        }
    }


    override fun getApplyVipReason() {
        mCoroutine.launch {
            val resp = MainRepository.apiService.getPayVipReason().awaitHiResponse()
            handleResponse(resp) {
                view.onVipReason(resp.data)

            }
        }
    }

    override fun getIdentity() {
        mCoroutine.launch {
            val identity = MainRepository.apiService.getShopIdentity().awaitHiResponse()
            handleResponse(identity) {
                view.onSetVipInfo(identity.data?.data)
            }
        }
    }

    override fun apply(id: String) {
        mCoroutine.launch {
            view.showDialogLoading()
            val identity = MainRepository.apiService.getPayInfo(id).awaitHiResponse()
            handleResponse(identity) {
                view.onApplySuccess(identity.data)
            }
            view.hideDialogLoading()
        }
    }

    override fun depositApply(mWallet: WalletVo?) {
        mCoroutine.launch {
            view.showDialogLoading()
            val recharge = RechargeReqVo(
                mWallet?.depositAccount?.id,
                mDepositMoney,
                RechargeReqVo.PAY_TRADE_CHANNEL_OF_WECHAT
            )
            val resp = MainRepository.apiService.recharge(recharge).awaitHiResponse()
            handleResponse(resp) {
                view.onDepositApplied(resp.data?.data)
            }
            view.hideDialogLoading()
        }
    }

    override fun ensureRefund(id: String) {
        mCoroutine.launch {
            view.showDialogLoading()
            val item = IdBean()
            item.id = id
            val identity = MainRepository.apiService.ensureRefund(item).awaitHiResponse()
            view.showToast(identity.msg)
            if (identity.isSuccess) {
                view.onRefundEnsured()
            } else {
                view.onRefundEnsureFail()
            }
            view.hideDialogLoading()
        }
    }

    /**
     * 加载钱包数据
     */
    override fun loadWalletData() {
        view.showDialogLoading()
        mWallet.loadWalletData()
    }

    data class ApplyVipReason(
        var account_id: String = "",
        var account_type: Int = 0,
        var account_type_name: String = "",
        var actual_amount: Double = 0.0,
        var amount: Double = 0.0,
        var apply_remark: String = "",
        var apply_time: String = "",
        var bank_name: String = "",
        var card_no: Any = Any(),
        var create_time: String = "",
        var creater_id: Any = Any(),
        var id: String = "",
        var inspect_remark: String = "",
        var inspect_time: String = "",
        var is_delete: Int = 0,
        var member_id: String = "",
        var member_name: String = "",
        var member_type: Any = Any(),
        var merchant_id: Any = Any(),
        var open_account_name: Any = Any(),
        var org_id: Any = Any(),
        var other_account_name: String = "",
        var other_account_no: String = "",
        var remarks: Any = Any(),
        var service_charge: Int = 0,
        //0提现中  1审核通过    2审核拒绝  3提现成功   4 "支付中"
        var status: Int? = null,
        var sub_bank_name: Any = Any(),
        var transfer_remark: String = "",
        var transfer_time: String = "",
        var update_time: String = "",
        var updater_id: String = "",
        var withdraw_channel: Int = 0,
        var withdraw_no: String = "",
        var withdraw_type: Int = 0
    )


}