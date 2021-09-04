package com.lingmiao.shop.business.main

import android.view.View
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.lingmiao.shop.business.main.bean.AddressData
import com.lingmiao.shop.business.main.bean.ApplyShopCategory
import com.lingmiao.shop.business.main.bean.ApplyShopInfo
import com.lingmiao.shop.business.main.bean.BindBankCardDTO

class ApplyShopInfoViewModel : ViewModel() {

    // 店铺是否处于开启中,用于修改进件资料
    var shopOpenOrNot: Boolean = false

    //店铺信息
    private val _applyShopInfo = MutableLiveData<ApplyShopInfo>().also {
        it.value = ApplyShopInfo()
    }

    val applyShopInfo: LiveData<ApplyShopInfo>
        get() = _applyShopInfo

    //成功加载Info
    fun onShopInfoSuccess(applyShopInfo: ApplyShopInfo) {
        _applyShopInfo.value = applyShopInfo
    }

    //初次申请店铺  true表示为第一次申请店铺
    var firstApplyShop: Boolean = true

    //页面的标题
    private val _title: MutableLiveData<String> = MutableLiveData()

    val title: LiveData<String>
        get() = _title

    //更改Title
    fun setTitle(title: String) {
        _title.value = title
    }


    //保存的对公对私账户
    //对公账户
    val companyAccount: MutableLiveData<BindBankCardDTO> =
        MutableLiveData<BindBankCardDTO>().also {
            val temp = BindBankCardDTO()
            temp.bankCardType = 1
            it.value = temp
        }

    //对私账户
    val personalAccount: MutableLiveData<BindBankCardDTO> =
        MutableLiveData<BindBankCardDTO>().also {
            val temp = BindBankCardDTO()
            temp.bankCardType = 0
            it.value = temp
        }


    //OCR识别的调用
    //调用OCR识别
    //1身份证人像面   2身份证国徽面   6银行卡      8 营业执照
    //不能给初始值
    val getOCR: MutableLiveData<Int> = MutableLiveData()


    //店铺经营类型
    private val _selectedCategoryListLiveData: MutableLiveData<List<ApplyShopCategory>> =
        MutableLiveData()

    val selectedCategoryListLiveData: LiveData<List<ApplyShopCategory>>
        get() = _selectedCategoryListLiveData

    //更改主营类目
    fun setCategory(list: List<ApplyShopCategory>) {
        _selectedCategoryListLiveData.value = list
    }


    //店铺地址
    private val _adInfo: MutableLiveData<AddressData> = MutableLiveData()

    val adInfo: LiveData<AddressData>
        get() = _adInfo

    //更改店铺地址
    fun setAddress(data: AddressData) {
        _adInfo.value = data
    }


    //绑定银行卡页面模块的可见性以及结算账户
    //对公账户
    val companyModule: MutableLiveData<Int> = MutableLiveData<Int>().also {
        it.value = View.VISIBLE
    }

    //对私账户
    val personalModule: MutableLiveData<Int> = MutableLiveData<Int>().also {
        it.value = View.GONE
    }

    //表示当前选那个账户作为结算账户
    //0  对公结算  1  对私结算
    val whichAccountToUse: MutableLiveData<Int> = MutableLiveData<Int>().also {
        it.value = 0
    }


    //根据需求获得银行卡号所代表的银行
    //获取对公账户的信息
    val ocrCBankVisibility: MutableLiveData<Int> = MutableLiveData()

    //获取对私账户的信息
    val ocrPBankVisibility: MutableLiveData<Int> = MutableLiveData()


    //更新银行卡号后更新对公UI
    val ocrCBankVisibilityU: MutableLiveData<Int> = MutableLiveData()

    //更新银行卡号后更新对私UI
    val ocrPBankVisibilityU: MutableLiveData<Int> = MutableLiveData()


    //绑定银行卡
    //0 绑定对公账户， 1，绑定对私账户  2绑定对公和对私账户
    val bindBandCard: MutableLiveData<Int> = MutableLiveData()


    //绑定银行卡结果
    // 0 绑定失败  1 绑定成功
    val bindResult: MutableLiveData<Int> = MutableLiveData()


    //申请店铺
    val go: MutableLiveData<Int> = MutableLiveData()

    //当法人和负责人发生变化时，判断是否相等
    val nameOfShopPerson: MutableLiveData<Int> = MutableLiveData<Int>().also {
        it.value = 1
    }

}