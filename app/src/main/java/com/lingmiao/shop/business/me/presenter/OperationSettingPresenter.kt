package com.lingmiao.shop.business.me.presenter

import com.james.common.base.BasePresenter
import com.james.common.base.BaseView
import com.lingmiao.shop.business.goods.api.bean.WorkTimeVo
import com.lingmiao.shop.business.main.bean.ApplyShopInfo
import com.lingmiao.shop.business.tools.bean.FreightVoItem

interface OperationSettingPresenter : BasePresenter {

    fun loadShopInfo()

    fun updateModel(id : String, isToSeller : Int, toRiderTime : Int)

    fun showWorkTimePop(target: android.view.View)

    fun setSetting(data: ApplyShopInfo)

    fun loadTemplate()

    //到店自提
    fun takeSelf(type: Int)

    interface View : BaseView {

        fun onLoadedShopInfo(bean: ApplyShopInfo)

        fun onUpdateWorkTime(workTimeVo1: WorkTimeVo?, workTimeVo2: WorkTimeVo?)

        fun onLoadedShopSetting(vo: ApplyShopInfo)

        fun onLoadedTemplate(tcItem: FreightVoItem?, qsItem: FreightVoItem?)

        fun setTakeSelfFailed()

        fun onSetSetting()
    }
}