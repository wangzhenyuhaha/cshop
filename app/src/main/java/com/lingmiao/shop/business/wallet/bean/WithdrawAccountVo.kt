package com.lingmiao.shop.business.wallet.bean
import com.google.gson.annotations.SerializedName
import java.io.Serializable


data class WithdrawAccountVo(
    @SerializedName("alipayWithdrawAccount")
    var alipayWithdrawAccount: AlipayAccountVo? = AlipayAccountVo(),
    @SerializedName("wechatWithdrawAccount")
    var wechatWithdrawAccount: AlipayAccountVo? = AlipayAccountVo(),

    @SerializedName("bankCard")
    var bankCard: BankCardAccountBean? = BankCardAccountBean()
) : Serializable {

    fun notExistAliPayAccount() : Boolean {
        return alipayWithdrawAccount == null || alipayWithdrawAccount?.accountName?.length === 0 || alipayWithdrawAccount?.accountNo?.length === 0;
    }

    fun notExistWechatAccount() : Boolean {
        return wechatWithdrawAccount == null || wechatWithdrawAccount?.accountName?.length === 0 || wechatWithdrawAccount?.accountNo?.length === 0;
    }

    fun notExistBankAccount() : Boolean {
        return bankCard == null || bankCard?.bankName?.length === 0 || bankCard?.cardNo?.length === 0 || bankCard?.openAccountName?.length === 0;
    }

    fun getAliPayId() : String {
        return alipayWithdrawAccount?.id ?: "";
    }

    fun getBankId() : String {
        return bankCard?.id ?: "";
    }

}