package com.lingmiao.shop.business.goods

import android.util.Log
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.WindowManager
import com.google.zxing.BarcodeFormat
import com.google.zxing.ResultPoint
import com.google.zxing.client.android.BeepManager
import com.james.common.base.BasePreImpl
import com.james.common.base.BasePresenter
import com.james.common.base.BaseVBActivity
import com.james.common.base.BaseView
import com.james.common.utils.exts.doIntercept
import com.james.common.utils.permission.interceptor.CameraInterceptor
import com.journeyapps.barcodescanner.BarcodeCallback
import com.journeyapps.barcodescanner.BarcodeResult
import com.journeyapps.barcodescanner.DefaultDecoderFactory
import com.lingmiao.shop.R
import com.lingmiao.shop.business.goods.api.GoodsRepository
import com.lingmiao.shop.databinding.ActivityGoodsScanBinding
import kotlinx.coroutines.launch

//https://blog.csdn.net/qq_28899635/article/details/52051617
class GoodsScanActivity : BaseVBActivity<ActivityGoodsScanBinding, GoodsScanActivityPresenter>(),
    GoodsScanActivityPresenter.View {

    override fun createPresenter() = GoodsScanActivityPresenterImpl(this)

    override fun getViewBinding() = ActivityGoodsScanBinding.inflate(layoutInflater)

    override fun useLightMode() = false

    //是否开灯
    private var isLighted = false

    private val callback: BarcodeCallback = object : BarcodeCallback {

        override fun barcodeResult(result: BarcodeResult) {
            mBinding.zxingBarcodeScanner.pauseAndWait()

            if (result.text == null || result.text.isEmpty()) {
                mBinding.zxingBarcodeScanner.resume()
                return
            }
            Log.d("WZYSDO", result.text)
            showToast(String.format("%s", result.text))
            beepManager?.playBeepSoundAndVibrate()
            mBinding.zxingBarcodeScanner.resume()

        }

        override fun possibleResultPoints(resultPoints: List<ResultPoint>) {

        }
    }

    private var beepManager: BeepManager? = null

    override fun initView() {

        mToolBarDelegate?.setMidTitle("扫码上架")

        window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)

        initBarCode()

    }

    private fun initBarCode() {

        mBinding.zxingBarcodeScanner.barcodeView.decoderFactory =
            DefaultDecoderFactory(mPresenter?.getBarcodeFormats())

        mBinding.zxingBarcodeScanner.initializeFromIntent(intent)
        mBinding.zxingBarcodeScanner.decodeContinuous(callback)

        doIntercept(CameraInterceptor(context!!), failed = {}) {
            mBinding.zxingBarcodeScanner.resume()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val inflater: MenuInflater = menuInflater
        inflater.inflate(R.menu.goods_scan_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.iv_light -> {
                if (isLighted) {
                    mBinding.zxingBarcodeScanner.setTorchOff()
                } else {
                    mBinding.zxingBarcodeScanner.setTorchOn()
                }
                isLighted = !isLighted
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onPause() {
        super.onPause()
        mBinding.zxingBarcodeScanner.setTorchOff()
    }

    override fun onScanSearchSuccess(data: ScanGoods) {
        //处理数据

    }
}


interface GoodsScanActivityPresenter : BasePresenter {

    fun getBarcodeFormats(): Collection<BarcodeFormat>

    //查询商品
    fun search(id: String)

    //添加商品


    interface View : BaseView {

       fun  onScanSearchSuccess(data:ScanGoods)
    }
}


class GoodsScanActivityPresenterImpl(val view: GoodsScanActivityPresenter.View) : BasePreImpl(view),
    GoodsScanActivityPresenter {

    override fun search(id: String) {
        mCoroutine.launch {
            view.showDialogLoading()

            val resp = GoodsRepository.searchGoodsOfCenter(id)

            handleResponse(resp) {
                view.onScanSearchSuccess(resp.data)
           }
            view.hideDialogLoading()
        }
    }

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
        )
    }

}


data class ScanGoods(var id: String)