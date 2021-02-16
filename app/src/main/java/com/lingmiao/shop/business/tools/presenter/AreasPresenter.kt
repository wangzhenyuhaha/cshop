package com.lingmiao.shop.business.tools.presenter

import com.lingmiao.shop.business.tools.bean.Item
import com.lingmiao.shop.business.tools.bean.RegionVo
import com.lingmiao.shop.business.tools.bean.TempArea
import com.james.common.base.BasePresenter
import com.james.common.base.BaseView

interface AreasPresenter : BasePresenter {

//    fun filterData(original : MutableList<RegionVo>, mEditItem: Item?, mTempArea: TempArea?) : MutableList<RegionVo>;

    fun getJsonList(mEditItem : Item?, mTempArea: TempArea?) : MutableList<RegionVo>;

    fun getResultList(list : MutableList<RegionVo>) : MutableList<RegionVo>;

    interface View : BaseView {

    }
}