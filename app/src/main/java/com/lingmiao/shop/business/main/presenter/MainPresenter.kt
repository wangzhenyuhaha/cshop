package com.lingmiao.shop.business.main.presenter

import com.james.common.base.BasePresenter
import com.james.common.base.BaseView
import com.lingmiao.shop.business.main.bean.ApplyShopInfo
import com.lingmiao.shop.business.main.bean.MainInfo
import com.lingmiao.shop.business.main.bean.MainInfoVo
import com.lingmiao.shop.business.main.bean.ShopStatus
import com.lingmiao.shop.business.me.bean.AccountSetting
import com.lingmiao.shop.business.me.bean.IdentityVo
import com.lingmiao.shop.business.me.bean.My

interface MainPresenter : BasePresenter {

    fun requestMainInfoData()
    fun requestAccountSettingData()

    fun editShopStatus(flag: Int)

    //获取当前预警商品的数量
    fun getWaringNumber()

    fun searchData()

    interface View : BaseView {
        fun onMainInfoSuccess(bean: MainInfo?)
        fun onMainDataSuccess(bean: MainInfoVo?, status: ShopStatus?)
        fun onMainInfoError(code: Int)

        fun onAccountSettingSuccess(bean: AccountSetting)
        fun onAccountSettingError(code: Int)

        fun onShopStatusEdited()

        fun applyVIP(message: String)
        fun applyVI2( my : My?, identity : IdentityVo?)

        fun onWarningNumber(data: Int)
    }
}