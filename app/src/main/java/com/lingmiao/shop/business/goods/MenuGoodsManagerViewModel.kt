package com.lingmiao.shop.business.goods

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.lingmiao.shop.business.goods.api.bean.ShopGroupVO

class MenuGoodsManagerViewModel : ViewModel() {


    //当前选中的一级菜单
    private val _item = MutableLiveData<ShopGroupVO>()

    val item: LiveData<ShopGroupVO>
        get() = _item

    //更改选中的Item
    fun setShopGroup(group: ShopGroupVO) {
        _item.value = group
    }

}