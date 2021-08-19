package com.lingmiao.shop.business.main.presenter

import com.james.common.base.BasePresenter
import com.james.common.base.BaseView
import com.lingmiao.shop.business.main.bean.*

interface ApplyShopInfoPresenter : BasePresenter {

    fun requestShopInfoData()
    fun requestApplyShopInfoData(applyShopInfo: ApplyShopInfo)

    fun searchOCR(type: Int, url: String)

    fun bindBankCard(company: BindBankCardDTO?, personal: BindBankCardDTO?)

    @Deprecated("勿用")
    fun searchBankList(memberId: Int)

    @Deprecated("勿用")
    fun searchBankCode(bankName: String): BankDetail

    interface View : BaseView {

        fun onShopInfoSuccess(applyShopInfo: ApplyShopInfo)
        fun onShopInfoError(code: Int)

        fun onApplyShopInfoSuccess()
        fun onApplyShopInfoError(code: Int)

        //更新银行卡信息
        fun onBankCard(data: BankCard)

        //更新身份证国徽面
        fun onIDCardG(data: IDCard)

        //更新身份证人像面
        fun onIDCardP(data: IDCard)

        //更新营业执照
        fun onUpdateLicense(data: License)

        //更新已有银行卡信息
        fun updateBankList(company: BindBankCardDTO?, personal: BindBankCardDTO?)

        //绑定对公账户成功
        fun companyYes()

        //绑定对公账户失败
        fun companyNO()

        //绑定对私账户成功
        fun personalYes()

        //绑定对私账户失败
        fun  personalNO()
    }
}