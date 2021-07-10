package com.lingmiao.shop.business.main.presenter
import com.james.common.base.BasePresenter
import com.james.common.base.BaseView
import com.lingmiao.shop.business.main.bean.ApplyShopAddress
import com.lingmiao.shop.business.tools.bean.RegionVo

interface ApplyShopAddressPresenter: BasePresenter{
 
	fun requestApplyShopAddressData()

	fun showAddress(view : android.view.View, old : List<RegionVo>?);

	interface View : BaseView {
        fun onApplyShopAddressSuccess(bean: ApplyShopAddress)
        fun onApplyShopAddressError(code: Int)
        fun onSetAddress(list : List<RegionVo>)
    }
}