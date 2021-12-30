package com.lingmiao.shop.business.goods

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.lingmiao.shop.business.goods.api.bean.ShopGroupVO

class MenuGoodsManagerViewModel : ViewModel() {


    //当前选中的菜单
    private val _item = MutableLiveData<ShopGroupVO>()

    //暂存的一级菜单
    private val _savedItem = MutableLiveData<ShopGroupVO>()

    val item: LiveData<ShopGroupVO>
        get() = _item

    val savedItem: LiveData<ShopGroupVO>
        get() = _savedItem

    //更改选中的Item(并包括暂存的),在点击一级菜单时使用
    fun setShopGroup(group: ShopGroupVO) {
        _item.value = group
        _savedItem.value = group
    }

    //设置当前选中的菜单
    fun setShopGroupOnlyFirst(group: ShopGroupVO) {
        _item.value = group
    }

    //将当前选中的菜单设为选中的一级菜单
    fun setFirstToSecond() {
        _item.value = _savedItem.value
    }


}