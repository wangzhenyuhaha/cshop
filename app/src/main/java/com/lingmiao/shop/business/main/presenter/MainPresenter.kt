package com.lingmiao.shop.business.main.presenter
import com.james.common.base.BasePresenter
import com.james.common.base.BaseView
import com.lingmiao.shop.business.main.bean.ApplyShopInfo
import com.lingmiao.shop.business.main.bean.MainInfo
import com.lingmiao.shop.business.main.bean.MainInfoVo
import com.lingmiao.shop.business.me.bean.AccountSetting

interface MainPresenter: BasePresenter{
 
	fun requestMainInfoData()
    fun requestAccountSettingData()

    fun editShopStatus(flag: Int);
	
	interface View : BaseView {
        fun onMainInfoSuccess(bean: MainInfo?)
        fun onMainDataSuccess(bean: MainInfoVo?)
        fun onMainInfoError(code: Int)

        fun onAccountSettingSuccess(bean: AccountSetting)
        fun onAccountSettingError(code: Int)

        fun onShopStatusEdited();
    }
}