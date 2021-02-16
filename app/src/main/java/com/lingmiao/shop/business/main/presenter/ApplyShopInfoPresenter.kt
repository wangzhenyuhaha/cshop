package com.lingmiao.shop.business.main.presenter
import com.james.common.base.BasePresenter
import com.james.common.base.BaseView
import com.lingmiao.shop.business.main.bean.ApplyShopInfo

interface ApplyShopInfoPresenter: BasePresenter{
 
	fun requestShopInfoData()
	fun requestApplyShopInfoData(applyShopInfo: ApplyShopInfo)

	interface View : BaseView {

        fun onShopInfoSuccess(applyShopInfo: ApplyShopInfo)
        fun onShopInfoError(code: Int)

        fun onApplyShopInfoSuccess()
        fun onApplyShopInfoError(code: Int)
    }
}