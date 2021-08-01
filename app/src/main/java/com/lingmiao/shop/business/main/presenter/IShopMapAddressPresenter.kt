package com.lingmiao.shop.business.main.presenter

import com.amap.api.maps.model.LatLng
import com.amap.api.services.core.PoiItem
import com.james.common.base.BasePresenter
import com.james.common.base.BaseView

/**
Create Date : 2021/7/2710:43 上午
Auther      : Fox
Desc        :
 **/
interface IShopMapAddressPresenter : BasePresenter {

    fun search(city : String?, key : String?, latLng: LatLng?);

    interface View : BaseView {
        fun setList(list: List<PoiItem>?);
    }

}