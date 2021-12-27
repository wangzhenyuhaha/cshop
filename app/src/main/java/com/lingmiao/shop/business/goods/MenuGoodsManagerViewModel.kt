package com.lingmiao.shop.business.goods

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.lingmiao.shop.business.goods.api.bean.ShopGroupVO

class MenuGoodsManagerViewModel : ViewModel() {


    //当前选中的一级菜单
    private val _item = MutableLiveData<ShopGroupVO>()

    //暂存的一级菜单
    private val _savedItem = MutableLiveData<ShopGroupVO>()

    val item: LiveData<ShopGroupVO>
        get() = _item

    val savedItem: LiveData<ShopGroupVO>
        get() = _savedItem

    //更改选中的Item(并包括暂存的)
    fun setShopGroup(group: ShopGroupVO) {
        _item.value = group
       // _savedItem.value = group
    }

    fun setShopGroupOnlyFirst(group: ShopGroupVO) {
        _item.value = group
    }

}