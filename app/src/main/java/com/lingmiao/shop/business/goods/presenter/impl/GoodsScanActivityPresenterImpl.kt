package com.lingmiao.shop.business.goods.presenter.impl

import com.google.zxing.BarcodeFormat
import com.james.common.base.BasePreImpl
import com.lingmiao.shop.business.goods.api.GoodsRepository
import com.lingmiao.shop.business.goods.presenter.GoodsScanActivityPresenter
import kotlinx.coroutines.launch

class GoodsScanActivityPresenterImpl(val view: GoodsScanActivityPresenter.View) : BasePreImpl(view),
    GoodsScanActivityPresenter {

    override fun search(id: String) {
        mCoroutine.launch {
            view.showDialogLoading()

            val resp = GoodsRepository.searchGoodsOfCenter(id)
            handleResponse(resp) {
                view.onScanSearchSuccess(resp.data)
            }
            if (!resp.isSuccess) {
                view.onScanSearchFailed()
            }
        }
    }

    override fun addGoodsSkuBarCodeLog(goods_id: Int, bar_code: String, url: String) {
        mCoroutine.launch {

            val resp = GoodsRepository.addGoodsSkuBarCodeLog(goods_id, bar_code, url)
            handleResponse(resp) {
                //nothing to do
            }
        }
    }

    override fun getBarcodeFormats(): Collection<BarcodeFormat> {
        return listOf(
            BarcodeFormat.EAN_13,
            BarcodeFormat.EAN_8,
            BarcodeFormat.UPC_EAN_EXTENSION
        )
    }

}