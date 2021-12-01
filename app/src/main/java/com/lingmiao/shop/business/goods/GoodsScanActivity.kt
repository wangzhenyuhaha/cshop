package com.lingmiao.shop.business.goods

import android.Manifest
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.os.Environment
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import androidx.lifecycle.MutableLiveData
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.google.gson.annotations.SerializedName
import com.google.zxing.BarcodeFormat
import com.google.zxing.ResultPoint
import com.google.zxing.client.android.BeepManager
import com.james.common.base.BasePreImpl
import com.james.common.base.BasePresenter
import com.james.common.base.BaseVBActivity
import com.james.common.base.BaseView
import com.james.common.utils.exts.doIntercept
import com.james.common.utils.exts.gone
import com.james.common.utils.exts.singleClick
import com.james.common.utils.exts.visiable
import com.james.common.utils.permission.interceptor.CameraInterceptor
import com.james.common.utils.permissionX.CheckPermission
import com.journeyapps.barcodescanner.BarcodeCallback
import com.journeyapps.barcodescanner.BarcodeResult
import com.journeyapps.barcodescanner.DecoratedBarcodeView
import com.journeyapps.barcodescanner.DefaultDecoderFactory
import com.lingmiao.shop.R
import com.lingmiao.shop.business.goods.api.GoodsRepository
import com.lingmiao.shop.databinding.ActivityGoodsScanBinding
import com.lingmiao.shop.util.GlideUtils
import com.lingmiao.shop.util.initAdapter
import kotlinx.coroutines.launch
import java.io.BufferedOutputStream
import java.io.File
import java.io.FileOutputStream
import java.util.*


class GoodsScanActivity : BaseVBActivity<ActivityGoodsScanBinding, GoodsScanActivityPresenter>(),
    GoodsScanActivityPresenter.View {

    override fun createPresenter() = GoodsScanActivityPresenterImpl(this)

    override fun getViewBinding() = ActivityGoodsScanBinding.inflate(layoutInflater)

    override fun useLightMode() = false

    //是否开灯
    private var isLighted = false

    //扫码时保存的图片
    private var bitmap: Bitmap? = null

    private var torchListener = object : DecoratedBarcodeView.TorchListener {
        override fun onTorchOn() {
            mBinding.ivLight.setImageResource(R.mipmap.light_off)
        }

        override fun onTorchOff() {
            mBinding.ivLight.setImageResource(R.mipmap.light_on)
        }
    }

    private val callback: BarcodeCallback = object : BarcodeCallback {

        override fun barcodeResult(result: BarcodeResult) {
            mBinding.zxingBarcodeScanner.pauseAndWait()

            bitmap = result.bitmap

            if (result.text == null || result.text.isEmpty()) {
                mBinding.zxingBarcodeScanner.resume()
                return
            }
            beepManager?.playBeepSoundAndVibrate()
            id.value = result.text
            mPresenter?.search(result.text)
            scanType = 1
        }

        override fun possibleResultPoints(resultPoints: List<ResultPoint>) {

        }
    }

    private var beepManager: BeepManager? = null

    private var adapter: GoodsAdapter? = null

    //商品条形码
    private val id: MutableLiveData<String> = MutableLiveData<String>()

    //商品ID
    private var goodsId: String = ""

    private val viewVisibility = MutableLiveData<Int>()

    //当前查询条形码方式  1 扫码查询   2  输入查询
    private var scanType: Int = 1

    override fun initView() {

        mToolBarDelegate?.setMidTitle("扫码上架")

        //设置ZXing
        initBarCode()

        id.observe(this, {
            mBinding.inputEdt.text = it
        })

        //手动查询条形码
        mBinding.searchGoods.singleClick {
            mBinding.writeScanCode.text.toString().also {
                if (it.isNotEmpty()) {
                    id.value = it
                    mPresenter?.search(it)
                    scanType = 2
                }
            }
        }

        mBinding.goodsCheckSubmit.singleClick {
            context?.let { it1 -> GoodsPublishNewActivity.openActivity(it1, goodsId, true) }
        }

        adapter = GoodsAdapter()

        adapter?.setOnItemChildClickListener { adapter, view, position ->
            val item = adapter.getItem(position) as GoodsSkuDO
            if (view.id == R.id.goodsCheckSubmit) {
                GoodsPublishNewActivity.openActivity(this, item.goods_id.toString())
                finish()
            }
        }

        adapter?.also {
            mBinding.goodsRV.initAdapter(it)
        }

        viewVisibility.observe(this, {
            //goodsSearchLayout 条形码显示区域
            //scanView扫描区域
            //scan_goods中心库查询到商品后信息展示
            //view 填充
            //title  店铺已有的商品
            //goodsRV   店铺已有的商品列表
            when (it) {
                //查询条形码接口报错，重新开始，恢复原样
                0 -> {
                    mBinding.goodsSearchLayout.gone()
                    mBinding.scanView.visiable()
                    mBinding.scanGoods.gone()
                    mBinding.view.gone()
                    mBinding.title.gone()
                    mBinding.goodsRV.gone()
                    mBinding.zxingBarcodeScanner.resume()
                }
                //中心库未查询到商品，且店铺中也没有
                1 -> {
                    //如果是输入条形码，不需要保存图片,但是记录还是要传的
                    if (scanType == 2) {
                        //跳转到新增商品界面
                        context?.let { it1 ->
                            GoodsPublishNewActivity.newPublish(
                                it1,
                                scan = true,
                                scanCode = id.value ?: ""
                            )
                        }
                        finish()
                    } else {
                        //保存下扫码获得的图片
                        try {
                            //这是一个Fragment，请求权限保存图片在本地
                            CheckPermission.request(
                                context!!,
                                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                                Manifest.permission.READ_EXTERNAL_STORAGE
                            ) { allGranted, _ ->
                                var temp = ""
                                if (allGranted) {
                                    //目标文件
                                    var externalFileRootDir: File? = getExternalFilesDir(null)
                                    do {
                                        externalFileRootDir =
                                            Objects.requireNonNull(externalFileRootDir)?.parentFile
                                    } while (Objects.requireNonNull(externalFileRootDir)?.absolutePath?.contains(
                                            "/Android"
                                        ) == true
                                    )
                                    val saveDir: String? =
                                        Objects.requireNonNull(externalFileRootDir)?.absolutePath
                                    val savePath = saveDir + "/" + Environment.DIRECTORY_DOWNLOADS

                                    val destinationFile = File(savePath, "test_scan.png")
                                    val bos =
                                        BufferedOutputStream(FileOutputStream(destinationFile))
                                    bitmap?.compress(Bitmap.CompressFormat.PNG, 100, bos)
                                    bos.flush()
                                    bos.close()
                                    temp = savePath
                                }
                                hideDialogLoading()
                                //跳转到新增商品界面
                                context?.let { it1 ->
                                    GoodsPublishNewActivity.newPublish(
                                        it1,
                                        scan = true,
                                        scanCode = id.value ?: "",
                                        pictureAddress = "${temp}/test_scan.png"
                                    )
                                }
                                finish()
                            }

                        } catch (e: Exception) {
                            hideDialogLoading()
                            //跳转到新增商品界面
                            context?.let { it1 ->
                                GoodsPublishNewActivity.newPublish(
                                    it1,
                                    scan = true,
                                    scanCode = id.value ?: ""
                                )
                            }
                            finish()
                        }
                    }


                }
                //中心库未查询到商品，但是店铺中有
                4 -> {
                    mBinding.goodsSearchLayout.visiable()
                    mBinding.scanView.gone()
                    mBinding.scanGoods.gone()
                    mBinding.view.gone()
                    mBinding.title.visiable()
                    mBinding.goodsRV.visiable()
                }
                //中心库查询到商品，但店铺中没有
                2 -> {
                    mBinding.goodsSearchLayout.visiable()
                    mBinding.scanView.gone()
                    mBinding.scanGoods.visiable()
                    mBinding.view.visiable()
                    mBinding.title.gone()
                    mBinding.goodsRV.gone()
                }
                //中心库查询到商品，且店铺中也有
                3 -> {
                    mBinding.goodsSearchLayout.visiable()
                    mBinding.scanView.gone()
                    mBinding.scanGoods.visiable()
                    mBinding.view.gone()
                    mBinding.title.visiable()
                    mBinding.goodsRV.visiable()
                }
            }
        })

    }

    private fun initBarCode() {

        //对闪光灯的监听
        mBinding.ivLight.setOnClickListener {
            if (isLighted) {
                mBinding.zxingBarcodeScanner.setTorchOff()
            } else {
                mBinding.zxingBarcodeScanner.setTorchOn()
            }
            mBinding.ivLight.setImageResource(if (isLighted) R.mipmap.light_off else R.mipmap.light_on)
            isLighted = !isLighted
        }

        //如果没有闪关灯，则隐藏
        if (!applicationContext.packageManager.hasSystemFeature(PackageManager.FEATURE_CAMERA_FLASH)) {
            mBinding.ivLight.gone()
        }
        mBinding.zxingBarcodeScanner.setTorchListener(torchListener)

        mBinding.zxingBarcodeScanner.barcodeView.decoderFactory =
            DefaultDecoderFactory(mPresenter?.getBarcodeFormats())

        mBinding.zxingBarcodeScanner.initializeFromIntent(intent)
        mBinding.zxingBarcodeScanner.decodeContinuous(callback)

        doIntercept(CameraInterceptor(context!!), failed = {}) {
            mBinding.zxingBarcodeScanner.resume()
        }
    }

    override fun onPause() {
        super.onPause()
        mBinding.zxingBarcodeScanner.setTorchOff()
    }

    override fun onResume() {
        super.onResume()
        mBinding.zxingBarcodeScanner.resume()
    }

    override fun onScanSearchSuccess(data: ScanGoods) {
        mBinding.zxingBarcodeScanner.pauseAndWait()
        hideDialogLoading()

        if (data.centerGoodsSkuDO?.goods_name == null) {
            if (data.goodsSkuDOList?.isEmpty() == true || data.goodsSkuDOList == null) {
                //中心库未查询到商品，但店铺也没有
                viewVisibility.value = 1
            } else {
                //中心库未查询到商品，但店铺中已有
                viewVisibility.value = 4
                data.goodsSkuDOList?.let { adapter?.replaceData(it) }
                adapter?.notifyDataSetChanged()
            }
        } else {
            if (data.goodsSkuDOList?.isEmpty() == true) {
                viewVisibility.value = 2
            } else {
                viewVisibility.value = 3
            }
            GlideUtils.setImageUrl(mBinding.goodsIv, data.centerGoodsSkuDO?.thumbnail)
            mBinding.goodsNameTv.text = data.centerGoodsSkuDO?.goods_name
            mBinding.goodsPriceTv.text = data.centerGoodsSkuDO?.price.toString()
            goodsId = data.centerGoodsSkuDO?.goods_id.toString()

            if (data.goodsSkuDOList?.isEmpty() != true) {
                //中心库查询到商品，店铺中已有
                data.goodsSkuDOList?.let { adapter?.replaceData(it) }
                adapter?.notifyDataSetChanged()
            }
        }


    }

    override fun onScanSearchFailed() {
        hideDialogLoading()
        showToast("查询失败，请检查网络连接")
        viewVisibility.value = 0
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        val inflater: MenuInflater = menuInflater
        inflater.inflate(R.menu.menu_goods_scan, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        return when (item.itemId) {
            //重新扫码
            R.id.resume -> {
                viewVisibility.value = 0
                true
            }

            else -> super.onOptionsItemSelected(item)
        }
    }

}


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
        )
    }

}

class GoodsAdapter :
    BaseQuickAdapter<GoodsSkuDO, BaseViewHolder>(R.layout.goods_adapter_goods_check) {

    override fun convert(helper: BaseViewHolder, item: GoodsSkuDO?) {
        item?.apply {

            //显示图片
            GlideUtils.setImageUrl1(helper.getView(R.id.goodsIv), thumbnail)


            //显示商品名
            helper.setText(R.id.goodsNameTv, goods_name)

            //useless
            helper.setText(R.id.goodsQuantityTv, "")

            //商品价格
            helper.setText(R.id.goodsPriceTv, price.toString())


            helper.setVisible(R.id.menuIv, false)
            helper.setVisible(R.id.deleteIv, false)

            helper.setVisible(R.id.goodsCheckSubmit, true)

            helper.addOnClickListener(R.id.goodsCheckSubmit)
        }
    }
}

data class ScanGoods(
    var centerGoodsSkuDO: CenterGoodsSkuDO? = null,
    var goodsSkuDOList: List<GoodsSkuDO>? = null
)

data class CenterGoodsSkuDO(
    var bar_code: String? = null,
    var category_id: Int? = null,
    var cost: Any? = null,
    var enable_quantity: Int? = null,
    var goods_id: Int? = null,
    var goods_name: String? = null,
    var hash_code: Int? = null,
    var local_template_id: Any? = null,
    var mktprice: Any? = null,
    var price: Int? = null,
    var quantity: Int? = null,
    var sku_id: Int? = null,
    var sn: String? = null,
    var template_id: Any? = null,
    var thumbnail: String? = null,
    var up_sku_id: Any? = null,
    var weight: Any? = null
)

data class GoodsSkuDO(
    var bar_code: String? = null,
    var category_id: Int? = null,
    var cost: Any? = null,
    var enable_quantity: Int? = null,
    var event_price: Double? = null,
    var event_quantity: Int? = null,
    var goods_id: Int? = null,
    var goods_name: String? = null,
    var hash_code: Int? = null,
    var local_template_id: Any? = null,
    var mktprice: Any? = null,
    var price: Double? = null,
    var quantity: Int? = null,
    var seller_id: Int? = null,
    var seller_name: String? = null,
    var sku_id: Int? = null,
    var sn: String? = null,
    var template_id: Any? = null,
    var thumbnail: String? = null,
    var up_sku_id: Any? = null,
    var weight: Any? = null
)

data class GoodsSkuBarcodeLog(
    @SerializedName("bar_code")
    var bar_code: String? = null,
    @SerializedName("img_url")
    var img_url: String? = null,
    //商品ID
    @SerializedName("goods_id")
    var goods_id: Int? = null
)
