package com.lingmiao.shop.business.main.presenter
import com.james.common.base.BasePresenter
import com.james.common.base.BaseView
import com.lingmiao.shop.business.main.bean.ApplyShopUpload

interface ApplyShopUploadPresenter: BasePresenter{
 
	fun requestApplyShopUploadData()
	
	interface View : BaseView {
        fun onApplyShopUploadSuccess(bean: ApplyShopUpload)
        fun onApplyShopUploadError(code: Int)
    }
}