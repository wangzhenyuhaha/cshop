package com.lingmiao.shop.business.goods

import android.Manifest
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.Color
import android.os.Environment
import android.text.Spannable
import android.text.SpannableStringBuilder
import android.text.style.ForegroundColorSpan
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import androidx.lifecycle.MutableLiveData
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.google.zxing.BarcodeFormat
import com.google.zxing.ResultPoint
import com.google.zxing.client.android.BeepManager
import com.james.common.base.BasePreImpl
import com.james.common.base.BasePresenter
import com.james.common.base.BaseVBActivity
import com.james.common.base.BaseView
import com.james.common.utils.DialogUtils
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
import com.lingmiao.shop.business.main.pop.ApplyInfoPop
import com.lingmiao.shop.databinding.ActivityGoodsScanBinding
import com.lingmiao.shop.util.GlideUtils
import com.lingmiao.shop.util.initAdapter
import kotlinx.coroutines.launch
import java.io.BufferedInputStream
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
        }

        override fun possibleResultPoints(resultPoints: List<ResultPoint>) {

        }
    }

    private var beepManager: BeepManager? = null

    private var adapter: GoodsAdapter? = null

    //当前扫码发现的商品是否已经添加进店铺
    private var hasAdd: Boolean = false

    //商品条形码
    private val id: MutableLiveData<String> = MutableLiveData<String>()

    //商品ID
    private var goods_id: String = ""

    private val viewVisibility = MutableLiveData<Int>()

    //0 条形码   1  商品名称
    private val typeSearch = MutableLiveData<Int>().also {
        it.value = 0
    }

    //弹出的东西
    private var pop: ApplyInfoPop? = null

    //经营区域  1  2  3
    private val typeList = listOf("条形码", "商品名称")

    override fun initBundles() {
        super.initBundles()
        pop = ApplyInfoPop(this)
    }

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
                }
            }
        }


        val content1 = "1.中心库可能存在相同商品，但未绑定当前条形码，可以去搜索中心库商品"
        val builder1 = SpannableStringBuilder(content1)
        val blueSpan1 = ForegroundColorSpan(Color.parseColor("#3870EA"))
        builder1.setSpan(blueSpan1, 27, 29, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
        mBinding.noResult1.text = builder1

        val content2 = "2.未查询到该条形码所属商品，可以去手动添加商品"
        val builder2 = SpannableStringBuilder(content2)
        val blueSpan2 = ForegroundColorSpan(Color.parseColor("#3870EA"))
        builder2.setSpan(blueSpan2, 18, 22, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
        mBinding.noResult2.text = builder2


        val content3 = "3.添加其他商品，重新扫描条形码"
        val builder3 = SpannableStringBuilder(content3)
        val blueSpan3 = ForegroundColorSpan(Color.parseColor("#3870EA"))
        builder3.setSpan(blueSpan3, 9, 13, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
        mBinding.noResult3.text = builder3

        mBinding.noResult1.singleClick {
            GoodsSearchCenterActivity.openActivity(this)
        }
        mBinding.noResult2.singleClick {
            context?.let { it1 -> GoodsPublishNewActivity.newPublish(it1, 0, scan = true) }
        }

        mBinding.noResult3.singleClick {
            viewVisibility.value = 0
        }

        mBinding.goodsCheckSubmit.singleClick {
            context?.let { it1 -> GoodsPublishNewActivity.openActivity(it1, goods_id, true) }
        }

        adapter = GoodsAdapter()

        adapter?.also {
            mBinding.goodsRV.initAdapter(it)
        }

        mBinding.type.singleClick {
//            pop?.apply {
//                setList(typeList)
//                setTitle("搜索模式")
//                setType(CompanyInfoFragment.REG_MONEY)
//                showPopupWindow()
//            }
        }

        viewVisibility.observe(this, {
            //goodsSearchLayout 条形码显示区域
            //scanView扫描区域
            //noResult跳转到添加商品
            //scan_goods中心库查询到商品后信息展示
            //view 填充
            //title  店铺已有的商品
            //goodsRV   店铺已有的商品列表
            when (it) {
                //查询条形码接口报错
                0 -> {
                    mBinding.goodsSearchLayout.gone()
                    mBinding.scanView.visiable()
                    mBinding.noResult.gone()
                    mBinding.scanGoods.gone()
                    mBinding.view.gone()
                    mBinding.title.gone()
                    mBinding.goodsRV.gone()
                    mBinding.zxingBarcodeScanner.resume()
                }
                //中心库未查询到商品，且店铺中也没有
                1 -> {
                    mBinding.goodsSearchLayout.visiable()
                    mBinding.scanView.gone()
                    mBinding.noResult.visiable()
                    mBinding.scanGoods.gone()
                    mBinding.view.visiable()
                    mBinding.title.gone()
                    mBinding.goodsRV.gone()

                    //保存下扫码获得的图片
                    try {
                        context?.let { it1 ->
                            CheckPermission.request(
                                it1,
                                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                                Manifest.permission.READ_EXTERNAL_STORAGE
                            ) { allGranted, _ ->
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

                                    val bos = BufferedOutputStream(FileOutputStream(destinationFile))
                                    bitmap?.compress(Bitmap.CompressFormat.PNG,100,bos)
                                    bos.flush()
                                    bos.close()
                                }
                            }
                        }
                    } catch (e: Exception) {
                        //nothing to do
                    }
                }
                //中心库查询到商品，但店铺中没有
                2 -> {
                    mBinding.goodsSearchLayout.visiable()
                    mBinding.scanView.gone()
                    mBinding.noResult.gone()
                    mBinding.scanGoods.visiable()
                    mBinding.view.visiable()
                    mBinding.title.gone()
                    mBinding.goodsRV.gone()
                }
                //中心库查询到商品，且店铺中也有
                3 -> {
                    mBinding.goodsSearchLayout.visiable()
                    mBinding.scanView.gone()
                    mBinding.noResult.gone()
                    mBinding.scanGoods.visiable()
                    mBinding.view.gone()
                    mBinding.title.visiable()
                    mBinding.goodsRV.visiable()
                }
            }
        })

        pop?.liveData?.observe(this, {
            typeSearch.value = it
            mBinding.type.text = typeList[it]
            mBinding.inputEdt.text = if (it == 0) "搜索条形码" else "搜索商品名称"
            pop?.dismiss()
        })

        mBinding.inputEdt.setOnClickListener {
            if (typeSearch.value == 0) {
                //条形码
                DialogUtils.showInputDialog(
                    this,
                    "条形码",
                    "",
                    "请输入",
                    id.value ?: "",
                    "取消",
                    "查询",
                    null
                ) {
                    if (it.isNotEmpty()) {
                        mPresenter?.search(it)
                        id.value = it
                    }
                }

            } else {
                //商品名称
                GoodsSearchCenterActivity.openActivity(this)
            }
        }
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

    override fun onScanSearchSuccess(data: ScanGoods) {
        mBinding.zxingBarcodeScanner.pauseAndWait()
        hasAdd = data.goodsSkuDOList?.isEmpty() != true


        if (data.centerGoodsSkuDO?.goods_name == null) {
            if (data.goodsSkuDOList?.isEmpty() == true) {
                //中心库未查询到商品，但店铺也没有
                viewVisibility.value = 1

            } else {
                //中心库未查询到商品，但店铺中已有
                mBinding.noResult.gone()
                mBinding.scanGoods.gone()
                mBinding.view.gone()
                mBinding.title.visiable()
                mBinding.goodsRV.visiable()
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
            goods_id = data.centerGoodsSkuDO?.goods_id.toString()

            if (data.goodsSkuDOList?.isEmpty() != true) {
                //中心库查询到商品，店铺中已有
                data.goodsSkuDOList?.let { adapter?.replaceData(it) }
                adapter?.notifyDataSetChanged()
            }

        }


    }

    override fun onScanSearchFailed() {
        showToast("查询失败")
        viewVisibility.value = 0
    }

    override fun onAddSuccess() {
        showToast("添加成功")
        mBinding.noResult.gone()
        mBinding.scanGoods.gone()
        mBinding.view.gone()
        mBinding.title.gone()
        mBinding.goodsRV.gone()
        mBinding.zxingBarcodeScanner.resume()
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

    fun getBarcodeFormats(): Collection<BarcodeFormat>

    //查询商品
    fun search(id: String)

    //添加商品
    fun add(ids: String, categoryId: String?, shopCatId: String?, is_force: Int?)

    interface View : BaseView {

        fun onScanSearchSuccess(data: ScanGoods)

        fun onScanSearchFailed()
        fun onAddSuccess()
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
            view.hideDialogLoading()
        }
    }

    override fun add(ids: String, categoryId: String?, shopCatId: String?, is_force: Int?) {
        mCoroutine.launch {
            view.showDialogLoading()

            val resp = GoodsRepository.addGoodsOfCenter(ids, categoryId, shopCatId, is_force)

            handleResponse(resp) {
                view.onAddSuccess()
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