package com.lingmiao.shop.business.order.presenter.impl

import com.lingmiao.shop.business.order.api.OrderRepository
import com.lingmiao.shop.business.order.presenter.ScanOrderPresenter
import com.google.zxing.BarcodeFormat
import com.james.common.base.BasePreImpl
import com.james.common.netcore.networking.http.core.awaitHiResponse
import kotlinx.coroutines.launch
import java.util.*

/**
Create Date : 2020/12/3010:10 AM
Auther      : Fox
Desc        :
 **/
class ScanOrderPreImpl(private val view : ScanOrderPresenter.View) : BasePreImpl(view), ScanOrderPresenter {

    override fun getBarcodeFormats(): Collection<BarcodeFormat> {
        return listOf(
            BarcodeFormat.QR_CODE,
            BarcodeFormat.CODABAR,
            BarcodeFormat.CODE_39,
            BarcodeFormat.CODE_93,
            BarcodeFormat.CODE_128,
            BarcodeFormat.DATA_MATRIX,
            BarcodeFormat.EAN_8,
            BarcodeFormat.EAN_13,
            BarcodeFormat.ITF,
            BarcodeFormat.MAXICODE,
            BarcodeFormat.PDF_417,
            BarcodeFormat.RSS_14,
            BarcodeFormat.RSS_EXPANDED,
            BarcodeFormat.UPC_A,
            BarcodeFormat.UPC_E,
            BarcodeFormat.UPC_EAN_EXTENSION,
            BarcodeFormat.AZTEC
        );
    }

    override fun verify(id: String) {
        mCoroutine.launch {
            view?.showDialogLoading();
            val resp = OrderRepository.apiService.verificationCode(id).awaitHiResponse()
            if(resp?.isSuccess) {
                view?.verifySuccess(resp?.data)
            } else {
                view?.verifyFailed()
            }
            view?.hideDialogLoading();
        }
    }

}