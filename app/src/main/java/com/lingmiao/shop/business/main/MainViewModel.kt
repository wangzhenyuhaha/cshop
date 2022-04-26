package com.lingmiao.shop.business.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.lingmiao.shop.business.main.bean.ShopTime

class MainViewModel : ViewModel() {

    private val _timeLiveData: MutableLiveData<ShopTime> = MutableLiveData()

    val timeLiveData: LiveData<ShopTime>
        get() = _timeLiveData


    fun setTime(
        startTime: Long? = null,
        endTime: Long? = null,
        type: Int? = null
    ) {
        _timeLiveData.value = ShopTime(startTime = startTime, endTime = endTime, type)
    }
}