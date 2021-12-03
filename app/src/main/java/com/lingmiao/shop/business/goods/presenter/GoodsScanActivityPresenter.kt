package com.lingmiao.shop.business.goods.presenter

import com.google.zxing.BarcodeFormat
import com.james.common.base.BasePresenter
import com.james.common.base.BaseView
import com.lingmiao.shop.business.goods.api.bean.ScanGoods

interface GoodsScanActivityPresenter : BasePresenter {

    //添加条形码扫描记录
    fun addGoodsSkuBarCodeLog(goods_id: Int, bar_code: String, url: String)

    fun getBarcodeFormats(): Collection<BarcodeFormat>

    //查询商品
    fun search(id: String)

    interface View : BaseView {

        fun onScanSearchSuccess(data: ScanGoods)

        fun onScanSearchFailed()
    }
}