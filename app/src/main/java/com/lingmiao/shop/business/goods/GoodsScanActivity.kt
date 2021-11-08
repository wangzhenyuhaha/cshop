package com.lingmiao.shop.business.goods

import android.graphics.Color
import android.text.Spannable
import android.text.SpannableStringBuilder
import android.text.style.ForegroundColorSpan
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.WindowManager
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
import com.journeyapps.barcodescanner.BarcodeCallback
import com.journeyapps.barcodescanner.BarcodeResult
import com.journeyapps.barcodescanner.DefaultDecoderFactory
import com.lingmiao.shop.R
import com.lingmiao.shop.business.goods.api.GoodsRepository
import com.lingmiao.shop.databinding.ActivityGoodsScanBinding
import com.lingmiao.shop.util.GlideUtils
import com.lingmiao.shop.util.initAdapter
import kotlinx.coroutines.launch


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
            beepManager?.playBeepSoundAndVibrate()
            mPresenter?.search(result.text)
        }

        override fun possibleResultPoints(resultPoints: List<ResultPoint>) {

        }
    }

    private var beepManager: BeepManager? = null

    private var adapter: GoodsAdapter? = null

    //当前扫码发现的商品是否已经添加进店铺
    private var hasAdd: Boolean = false

    //通过扫码查询到的商品
    private var id: String = ""

    //1是，0否，当值为1时条形码即使存在也会复制，值为0时会判断条形码是否存在，存在的话会返回提示
    private var isForce: Int = 1

    override fun initView() {

        mToolBarDelegate?.setMidTitle("扫码上架")

        //手动查询条形码
        mBinding.searchGoods.singleClick {
            mBinding.writeScanCode.text.toString().also {
                if (it.isNotEmpty()) {
                    mPresenter?.search(it)
                    id = it
                }
            }
        }

        initBarCode()

        mBinding.noResult.singleClick {
            context?.let { it1 -> GoodsPublishNewActivity.newPublish(it1, 0, scan = true) }
        }

        mBinding.goodsCheckSubmit.singleClick {
            if (hasAdd) {
                DialogUtils.showDialog(
                    this,
                    "商品已存在",
                    "您扫描的商品已在店铺中存在，是否添加？",
                    "取消",
                    "添加",
                    {

                    },
                    {
                        mPresenter?.add(id, null, null, isForce)
                    })
            } else {
                mPresenter?.add(id, null, null, isForce)
            }

        }

        adapter = GoodsAdapter()

        adapter?.also {
            mBinding.goodsRV.initAdapter(it)
        }


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
                val content = "未搜索到商品，去手动添加"
                val builder = SpannableStringBuilder(content)
                val blueSpan = ForegroundColorSpan(Color.parseColor("#3870EA"))
                builder.setSpan(blueSpan, 8, 12, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
                mBinding.noResult.text = builder
                mBinding.noResult.visiable()
                mBinding.scanGoods.gone()
                mBinding.view.gone()
                mBinding.title.gone()
                mBinding.goodsRV.gone()
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
            mBinding.noResult.gone()
            mBinding.scanGoods.visiable()
            mBinding.view.visiable()

            mBinding.title.gone()
            mBinding.goodsRV.gone()
            GlideUtils.setImageUrl(mBinding.goodsIv, data.centerGoodsSkuDO?.thumbnail)
            mBinding.goodsNameTv.text = data.centerGoodsSkuDO?.goods_name
            mBinding.goodsPriceTv.text = data.centerGoodsSkuDO?.price.toString()
            id = data.centerGoodsSkuDO?.goods_id.toString()
            if (data.goodsSkuDOList?.isEmpty() != true) {
                //中心库查询到商品，店铺中已有
                mBinding.view.gone()
                mBinding.title.visiable()
                mBinding.goodsRV.visiable()
                data.goodsSkuDOList?.let { adapter?.replaceData(it) }
                adapter?.notifyDataSetChanged()
            }

        }


    }

    override fun onScanSearchFailed() {
        mBinding.noResult.gone()
        mBinding.scanGoods.gone()
        mBinding.view.gone()
        mBinding.title.gone()
        mBinding.goodsRV.gone()
        showToast("扫码失败")
        mBinding.zxingBarcodeScanner.resume()
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
    var event_price: Int? = null,
    var event_quantity: Int? = null,
    var goods_id: Int? = null,
    var goods_name: String? = null,
    var hash_code: Int? = null,
    var local_template_id: Any? = null,
    var mktprice: Any? = null,
    var price: Int? = null,
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