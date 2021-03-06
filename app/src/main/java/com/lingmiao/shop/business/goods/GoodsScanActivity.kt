package com.lingmiao.shop.business.goods

import android.Manifest
import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.os.Environment
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.lifecycleScope
import com.blankj.utilcode.util.ActivityUtils
import com.blankj.utilcode.util.KeyboardUtils
import com.google.zxing.ResultPoint
import com.google.zxing.client.android.BeepManager
import com.james.common.base.BaseVBActivity
import com.james.common.utils.DialogUtils
import com.james.common.utils.exts.*
import com.james.common.utils.permission.interceptor.CameraInterceptor
import com.james.common.utils.permissionX.CheckPermission
import com.journeyapps.barcodescanner.BarcodeCallback
import com.journeyapps.barcodescanner.BarcodeResult
import com.journeyapps.barcodescanner.DecoratedBarcodeView
import com.journeyapps.barcodescanner.DefaultDecoderFactory
import com.lingmiao.shop.R
import com.lingmiao.shop.business.goods.adapter.GoodsScanAdapter
import com.lingmiao.shop.business.goods.api.GoodsRepository
import com.lingmiao.shop.business.goods.api.bean.GoodsSkuDO
import com.lingmiao.shop.business.goods.api.bean.GoodsVOWrapper
import com.lingmiao.shop.business.goods.api.bean.ScanGoods
import com.lingmiao.shop.business.goods.event.GoodsHomeTabEvent
import com.lingmiao.shop.business.goods.event.RefreshGoodsStatusEvent
import com.lingmiao.shop.business.goods.fragment.GoodsFragment
import com.lingmiao.shop.business.goods.presenter.GoodsScanActivityPresenter
import com.lingmiao.shop.business.goods.presenter.impl.GoodsScanActivityPresenterImpl
import com.lingmiao.shop.databinding.ActivityGoodsScanBinding
import com.lingmiao.shop.util.GlideUtils
import com.lingmiao.shop.util.initAdapter
import kotlinx.android.synthetic.main.goods_include_publish_section5.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.greenrobot.eventbus.EventBus
import java.io.BufferedOutputStream
import java.io.File
import java.io.FileOutputStream
import java.util.*

@SuppressLint("NotifyDataSetChanged")
class GoodsScanActivity : BaseVBActivity<ActivityGoodsScanBinding, GoodsScanActivityPresenter>(),
    GoodsScanActivityPresenter.View {

    override fun createPresenter() = GoodsScanActivityPresenterImpl(this)

    override fun getViewBinding() = ActivityGoodsScanBinding.inflate(layoutInflater)

    override fun useLightMode() = false

    //?????????????????????????????????????????????????????????????????????????????????
    private var isFromCenter = 1

    //??????????????????????????????
    private var goodsVO: GoodsVOWrapper = GoodsVOWrapper()

    //????????????
    private var isLighted = false

    //????????????????????????
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

    private var adapter: GoodsScanAdapter? = null

    //???????????????
    private val id: MutableLiveData<String> = MutableLiveData<String>()

    //??????????????????????????????ID
    private var goodsId: String = ""

    private val viewVisibility = MutableLiveData<Int>()

    //???????????????????????????  1 ????????????   2  ????????????
    private var scanType: Int = 1

    override fun initView() {

        mToolBarDelegate?.setMidTitle("????????????")

        //??????ZXing
        initBarCode()

        id.observe(this, {
            mBinding.inputEdt.text = it
        })

        //?????????????????????
        mBinding.searchGoods.singleClick {
            mBinding.writeScanCode.text.toString().also {
                if (it.isNotEmpty()) {
                    id.value = it
                    mPresenter?.search(it)
                    scanType = 2
                }
            }
        }

        adapter = GoodsScanAdapter()

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
            //goodsSearchLayout ?????????????????????
            //scanView????????????
            //scan_goods???????????????????????????????????????
            //view ??????
            //title  ?????????????????????
            //goodsRV   ???????????????????????????
            when (it) {
                //?????????????????????????????????????????????????????????
                0 -> {
                    mBinding.goodsSearchLayout.gone()
                    mBinding.scanView.visiable()
                    mBinding.scanGoods.gone()
                    mBinding.view.gone()
                    mBinding.title.gone()
                    mBinding.goodsRV.gone()
                    mBinding.zxingBarcodeScanner.resume()
                }
                //???????????????????????????????????????????????????
                1 -> {
                    //????????????????????????????????????????????????,???????????????????????????
                    if (scanType == 2) {
                        //???????????????????????????
                        context?.let { it1 ->
                            GoodsPublishNewActivity.newPublish(
                                it1,
                                scan = true,
                                scanCode = id.value ?: ""
                            )
                        }
                        finish()
                    } else {
                        //??????????????????????????????
                        try {
                            //????????????Fragment????????????????????????????????????
                            CheckPermission.request(
                                context!!,
                                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                                Manifest.permission.READ_EXTERNAL_STORAGE
                            ) { allGranted, _ ->
                                var temp = ""
                                if (allGranted) {
                                    //????????????
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
                                //???????????????????????????
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
                            //???????????????????????????
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
                //????????????????????????????????????????????????
                4 -> {
                    mBinding.goodsSearchLayout.visiable()
                    mBinding.scanView.gone()
                    mBinding.scanGoods.gone()
                    mBinding.view.gone()
                    mBinding.title.visiable()
                    mBinding.goodsRV.visiable()
                }
                //?????????????????????????????????????????????
                2 -> {
                    mBinding.goodsSearchLayout.visiable()
                    mBinding.scanView.gone()
                    mBinding.scanGoods.visiable()
                    mBinding.view.visiable()
                    mBinding.title.gone()
                    mBinding.goodsRV.gone()
                }
                //?????????????????????????????????????????????
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

        //????????????
        mBinding.goodsCheckSubmit.singleClick {
            context?.let { it1 ->
                GoodsPublishNewActivity.openActivity(
                    it1,
                    goodsId,
                    true,
                    this.goodsVO.thumbnail,
                    mBinding.goodsPriceEdt.getViewText(),
                    mBinding.goodskucunEdt.getViewText(),
                    this.goodsVO.categoryId,
                    this.goodsVO.categoryName,
                    this.goodsVO.shopCatId,
                    this.goodsVO.shopCatName,
                    isFromCenter
                )
            }
        }

        //?????????????????????????????????
        cateAddIv.singleClick {
            GoodsCategoryActivity.openActivity(this, 1)
        }

        //??????????????????
        mBinding.goodsCategoryTv.singleClick {
            mPresenter?.showCategoryPop()
        }

        //??????????????????
        mBinding.goodsGroupTv.singleClick {
            mPresenter?.showGroupPop()
        }

        //??????
        mBinding.saveTv.singleClick {
            clickConfirmView(0)
        }

        //???????????????
        mBinding.confirmTv.singleClick {
            clickConfirmView(1)
        }

    }

    private fun initBarCode() {

        //?????????????????????
        mBinding.ivLight.setOnClickListener {
            if (isLighted) {
                mBinding.zxingBarcodeScanner.setTorchOff()
            } else {
                mBinding.zxingBarcodeScanner.setTorchOn()
            }
            mBinding.ivLight.setImageResource(if (isLighted) R.mipmap.light_off else R.mipmap.light_on)
            isLighted = !isLighted
        }

        //?????????????????????????????????
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

        if (data.centerGoodsSkuDO?.goodsName == null) {
            if (data.goodsSkuDOList?.isEmpty() == true || data.goodsSkuDOList == null) {
                //????????????????????????????????????????????????
                viewVisibility.value = 1
            } else {
                //????????????????????????????????????????????????
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
            data.centerGoodsSkuDO?.goods_id?.let { mPresenter?.loadGoodsInfoFromCenter(it.toString()) }
            goodsId = data.centerGoodsSkuDO?.goods_id.toString()

            if (data.goodsSkuDOList?.isEmpty() != true) {
                //??????????????????????????????????????????
                data.goodsSkuDOList?.let { adapter?.replaceData(it) }
                adapter?.notifyDataSetChanged()
            }
        }


    }

    //????????????????????????????????????
    override fun onLoadGoodsSuccess(goodsVO: GoodsVOWrapper, center: Boolean) {
        this.goodsVO = goodsVO

        goodsVO.apply {
            //??????????????????????????????UI?????????
            //????????????
            //????????????????????????????????????????????????
            if (thumbnail.isNotBlank()) {
                GlideUtils.setImageUrl(mBinding.goodsIv, thumbnail)
            } else {
                GlideUtils.setImageUrl(mBinding.goodsIv, goodsGalleryList?.get(0)?.original)
                thumbnail = goodsGalleryList?.get(0)?.original
            }

            //?????????
            mBinding.goodsNameTv.text = goodsName

            //????????????
            //?????????????????????????????????
            mBinding.goodsPriceEdt.setText("")
            mBinding.goodsPriceEdt.requestFocus()
            KeyboardUtils.showSoftInput()
            price = null

            //????????????
            mBinding.goodskucunEdt.setText("$quantity")

            //????????????
            mBinding.goodsCategoryTv.text = categoryName

            //????????????
            mBinding.goodsGroupTv.text = if (shopCatName.isNotBlank()) {
                shopCatName
            } else {
                categoryName
            }
        }
    }

    //????????????
    override fun onUpdateCategory(categoryId: String?, categoryName: String?) {
        //??????????????????
        mBinding.goodsCategoryTv.text = categoryName
        goodsVO.apply {
            //????????????ID???Name
            this.categoryId = categoryId
            this.categoryName = categoryName
        }
        if (isFromCenter == 1) {
            goodsVO.shopCatId = null
            goodsVO.shopCatName = null
            mBinding.goodsGroupTv.text = "?????????"
            isFromCenter = 0
        }
        if (goodsVO.shopCatId.isNullOrEmpty()) {
            //UI?????????????????????????????????????????????????????????
            onUpdateGroup(null, categoryName)
        }
    }

    //????????????
    override fun onUpdateGroup(groupId: String?, groupName: String?) {
        mBinding.goodsGroupTv.text = groupName
        if (groupId != null) {
            goodsVO.apply {
                this.shopCatId = groupId
                this.shopCatName = groupName
            }
        }
        if (isFromCenter == 1) {
            goodsVO.categoryId = null
            goodsVO.categoryName = null
            mBinding.goodsCategoryTv.text = "?????????????????????"
            isFromCenter = 0
        }

    }

    override fun onScanSearchFailed() {
        hideDialogLoading()
        showToast("????????????????????????????????????")
        viewVisibility.value = 0
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        val inflater: MenuInflater = menuInflater
        inflater.inflate(R.menu.menu_goods_scan, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        return when (item.itemId) {
            //????????????
            R.id.resume -> {
                viewVisibility.value = 0
                true
            }

            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun clickConfirmView(type: Int) {

        //???????????????
        KeyboardUtils.hideSoftInput(this)
        mBinding.writeScanCode.setText("")

        if (goodsVO.categoryId == null) {
            showToast("?????????????????????")
            return
        }
        if (mBinding.goodsPriceEdt.getViewText().isEmpty()) {
            showToast("?????????????????????")
            return
        }
        if (mBinding.goodskucunEdt.getViewText().isEmpty()) {
            showToast("?????????????????????")
            return
        }

        showDialogLoading()
        goodsVO.apply {
            bar_code = id.value
            price = mBinding.goodsPriceEdt.getViewText()
            quantity = mBinding.goodskucunEdt.getViewText()
            goodsId = null
        }


        //goodsVO ???????????????  type???????????????????????????
        submitGoods(goodsVO, type) // ????????????


    }

    private fun submitGoods(goodsVO: GoodsVOWrapper, type: Int) {
        lifecycleScope.launch(Dispatchers.IO) {

            val resp =
                GoodsRepository.submitGoods(goodsVO, type.toString(), isFromCenter)

            if (resp.isSuccess) {
                withContext(Dispatchers.Main)
                {
                    if (type == 0) {
                        //?????????
                        DialogUtils.showDialog(
                            ActivityUtils.getTopActivity(),
                            "??????????????????",
                            "?????????????????????",
                            "????????????",
                            "??????",
                            {
                                ActivityUtils.startActivity(GoodsListActivity::class.java)
                                EventBus.getDefault()
                                    .post(GoodsHomeTabEvent(GoodsFragment.GOODS_STATUS_IS_AUTH))
                                EventBus.getDefault().post(RefreshGoodsStatusEvent())
                                finish()
                            },
                            {
                                viewVisibility.value = 0
                            })
                    } else {
                        //????????????
                        DialogUtils.showDialog(
                            ActivityUtils.getTopActivity(),
                            "??????????????????",
                            "?????????????????????",
                            "????????????",
                            "??????",
                            {
                                ActivityUtils.startActivity(GoodsListActivity::class.java)
                                EventBus.getDefault()
                                    .post(GoodsHomeTabEvent(GoodsFragment.GOODS_STATUS_ENABLE))
                                EventBus.getDefault().post(RefreshGoodsStatusEvent())
                                finish()
                            },
                            {
                                viewVisibility.value = 0
                            })
                    }
                    hideDialogLoading()
                }
            } else {
                withContext(Dispatchers.Main)
                {
                    hideDialogLoading()
                    showToast("????????????")
                }
            }

        }
    }
}







