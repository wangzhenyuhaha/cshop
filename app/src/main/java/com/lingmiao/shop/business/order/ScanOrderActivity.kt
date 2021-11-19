package com.lingmiao.shop.business.order

import android.content.Intent
import android.content.pm.PackageManager
import android.view.KeyEvent
import android.view.WindowManager
import com.google.zxing.ResultPoint
import com.google.zxing.client.android.BeepManager
import com.james.common.base.BaseActivity
import com.james.common.utils.exts.doIntercept
import com.james.common.utils.exts.gone
import com.james.common.utils.permission.interceptor.CameraInterceptor
import com.journeyapps.barcodescanner.BarcodeCallback
import com.journeyapps.barcodescanner.BarcodeResult
import com.journeyapps.barcodescanner.DecoratedBarcodeView
import com.journeyapps.barcodescanner.DefaultDecoderFactory
import com.lingmiao.shop.R
import com.lingmiao.shop.business.order.bean.OrderList
import com.lingmiao.shop.business.order.presenter.ScanOrderPresenter
import com.lingmiao.shop.business.order.presenter.impl.ScanOrderPreImpl
import kotlinx.android.synthetic.main.order_activity_scan.*


/**
Create Date : 2020/12/3010:10 AM
Auther      : Fox
Desc        :
 **/
class ScanOrderActivity : BaseActivity<ScanOrderPresenter>(), ScanOrderPresenter.View {

    private var beepManager: BeepManager? = null

    private var isLighted = false

    private var lastText: String? = ""

    override fun createPresenter() = ScanOrderPreImpl(this)

    override fun getLayoutId() = R.layout.order_activity_scan

    override fun useBaseLayout() = false

    override fun initView() {
        window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)

        initOtherView()

        initBarCode()
    }

    private fun initOtherView() {
        iv_back.setOnClickListener {
            finish()
        }
        iv_order_verify.setOnClickListener {
            if (et_order_name.text.toString().isNotEmpty()) {
                mPresenter?.verify(et_order_name.text.toString())
            }
        }
    }

    private fun initBarCode() {
        iv_light.setOnClickListener {
            if (isLighted) {
                zxing_barcode_scanner?.setTorchOff()
            } else {
                zxing_barcode_scanner?.setTorchOn()
            }
            iv_light.setImageResource(if (isLighted) R.mipmap.light_off else R.mipmap.light_on)
            isLighted = !isLighted
        }
        // if the device does not have flashlight in its camera,
        // then remove the switch flashlight button...
        if (!hasFlash()) {
            iv_light?.gone()
        }
        zxing_barcode_scanner.setTorchListener(torchListener)

        zxing_barcode_scanner.barcodeView.decoderFactory =
            DefaultDecoderFactory(mPresenter?.getBarcodeFormats())
        zxing_barcode_scanner.initializeFromIntent(intent)
        zxing_barcode_scanner.decodeContinuous(callback)

        doIntercept(CameraInterceptor(context), failed = {}) {
            zxing_barcode_scanner.resume()
        }
    }

    /**
     * Check if the device's camera has a Flashlight.
     * @return true if there is Flashlight, otherwise false.
     */
    private fun hasFlash() =
        applicationContext.packageManager.hasSystemFeature(PackageManager.FEATURE_CAMERA_FLASH)


    var torchListener = object : DecoratedBarcodeView.TorchListener {

        override fun onTorchOn() {
            iv_light.setImageResource(R.mipmap.light_off)
        }

        override fun onTorchOff() {

            iv_light.setImageResource(R.mipmap.light_on)
        }

    }

    private val callback: BarcodeCallback = object : BarcodeCallback {

        override fun barcodeResult(result: BarcodeResult) {
            zxing_barcode_scanner.pauseAndWait()

            if (result.text == null || result.text.isEmpty()) {
                zxing_barcode_scanner.resume()
                return
            }
            if (result.text == lastText) {
                showToast(String.format("%s已扫描", result.text))
                zxing_barcode_scanner.resume()
                return
            }
            tv_result.text = lastText
            beepManager?.playBeepSoundAndVibrate()
            mPresenter?.verify(result.text)
        }

        override fun possibleResultPoints(resultPoints: List<ResultPoint>) {

        }
    }

    override fun verifySuccess(data: OrderList?) {
        zxing_barcode_scanner?.pauseAndWait()
        data?.apply {
            val intent =
                Intent(context, OrderDetailActivity::class.java)
            intent.putExtra("orderId", data.sn)
            startActivity(intent)
        }
        finish()
    }

    override fun verifyFailed() {
        zxing_barcode_scanner?.resume()
    }

    override fun onResume() {
        super.onResume()
        zxing_barcode_scanner.resume()
    }

    override fun onPause() {
        super.onPause()
        zxing_barcode_scanner.pause()
    }

    override fun onKeyDown(keyCode: Int, event: KeyEvent?) =
        zxing_barcode_scanner!!.onKeyDown(keyCode, event) || super.onKeyDown(keyCode, event)


}