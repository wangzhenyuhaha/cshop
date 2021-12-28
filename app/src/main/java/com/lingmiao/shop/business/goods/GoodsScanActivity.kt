package com.lingmiao.shop.business.goods

import android.Manifest
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.os.Environment
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.lifecycleScope
import com.blankj.utilcode.util.ActivityUtils
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


class GoodsScanActivity : BaseVBActivity<ActivityGoodsScanBinding, GoodsScanActivityPresenter>(),
    GoodsScanActivityPresenter.View {

    override fun createPresenter() = GoodsScanActivityPresenterImpl(this)

    override fun getViewBinding() = ActivityGoodsScanBinding.inflate(layoutInflater)

    override fun useLightMode() = false

    //当前分类菜单是否来自中心库（该页面中默认是来自中心库）
    private var isFromCenter = 1

    //从中心库查询到的商品
    private var goodsVO: GoodsVOWrapper = GoodsVOWrapper()

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

    private var adapter: GoodsScanAdapter? = null

    //商品条形码
    private val id: MutableLiveData<String> = MutableLiveData<String>()

    //扫码出来的中心库商品ID
    private var goodsId: String = ""

    private val viewVisibility = MutableLiveData<Int>()

    //当前查询条形码方式  1 扫码查询   2  输入查询(已经不存在了)
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

        //编辑详情
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
                    this.goodsVO.shopCatName
                )
            }
        }

        //添加新的分类，只是跳转
        cateAddIv.singleClick {
            GoodsCategoryActivity.openActivity(this, 1)
        }

        //修改商品分类
        mBinding.goodsCategoryTv.singleClick {
            mPresenter?.showCategoryPop()
        }

        //选择商品菜单
        mBinding.goodsGroupTv.singleClick {
            mPresenter?.showGroupPop()
        }

        //保存
        mBinding.saveTv.singleClick {
            clickConfirmView(0)
        }

        //保存并上架
        mBinding.confirmTv.singleClick {
            clickConfirmView(1)
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

    override fun onResume() {
        super.onResume()
        mBinding.zxingBarcodeScanner.resume()
    }

    override fun onScanSearchSuccess(data: ScanGoods) {
        mBinding.zxingBarcodeScanner.pauseAndWait()
        hideDialogLoading()

        if (data.centerGoodsSkuDO?.goodsName == null) {
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
            data.centerGoodsSkuDO?.goods_id?.let { mPresenter?.loadGoodsInfoFromCenter(it.toString()) }
            goodsId = data.centerGoodsSkuDO?.goods_id.toString()

            if (data.goodsSkuDOList?.isEmpty() != true) {
                //中心库查询到商品，店铺中已有
                data.goodsSkuDOList?.let { adapter?.replaceData(it) }
                adapter?.notifyDataSetChanged()
            }
        }


    }

    //从中心库中成功加载到商品
    override fun onLoadGoodsSuccess(goodsVO: GoodsVOWrapper, center: Boolean) {
        this.goodsVO = goodsVO

        goodsVO.apply {
            //中心库中有商品时，在UI上显示
            //商品图片
            //如果没有缩图，则用第一张主图代替
            if (thumbnail.isNotBlank()) {
                GlideUtils.setImageUrl(mBinding.goodsIv, thumbnail)
            } else {
                GlideUtils.setImageUrl(mBinding.goodsIv, goodsGalleryList?.get(0)?.original)
                thumbnail = goodsGalleryList?.get(0)?.original
            }

            //商品名
            mBinding.goodsNameTv.text = goodsName

            //商品价格
            mBinding.goodsPriceEdt.setText("$price")

            //商品库存
            mBinding.goodskucunEdt.setText("$quantity")

            //商品分类
            mBinding.goodsCategoryTv.text = categoryName

            //商品菜单
            mBinding.goodsGroupTv.text = if (shopCatName.isNotBlank()) {
                shopCatName
            } else {
                categoryName
            }
        }
    }

    //选择分类
    override fun onUpdateCategory(categoryId: String?, categoryName: String?) {
        //显示分类名字
        mBinding.goodsCategoryTv.text = categoryName
        goodsVO.apply {
            //赋值分类ID和Name
            this.categoryId = categoryId
            this.categoryName = categoryName
        }
        if(isFromCenter == 1)
        {
            goodsVO.shopCatId = null
            goodsVO.shopCatName = null
            mBinding.goodsGroupTv.text = "请选择"
            isFromCenter =0
        }
    }

    //选择菜单
    override fun onUpdateGroup(groupId: String?, groupName: String?) {
        mBinding.goodsGroupTv.text = groupName
        goodsVO.apply {
            this.shopCatId = groupId
            this.shopCatName = groupName
        }

        if(isFromCenter == 1)
        {
            goodsVO.categoryId = null
            goodsVO.categoryName = null
            mBinding.goodsCategoryTv.text = "请选择商品分类"
            isFromCenter =0
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

    private fun clickConfirmView(type: Int) {

        if (goodsVO.categoryId == null) {
            showToast("请选择商品分类")
            return
        }

        if (goodsVO.shopCatId == null) {
            showToast("请选择商品菜单")
            return
        }

        showDialogLoading()
        goodsVO.apply {
            bar_code = id.value
            price = mBinding.goodsPriceEdt.getViewText()
            quantity = mBinding.goodskucunEdt.getViewText()
            goodsId = null
        }


        //goodsVO 商品数据类  type保存或者保存并上架
        submitGoods(goodsVO, type) // 添加商品


    }

    private fun submitGoods(goodsVO: GoodsVOWrapper, type: Int) {
        lifecycleScope.launch(Dispatchers.IO) {

            val resp =
                GoodsRepository.submitGoods(goodsVO, type.toString(), isFromCenter)

            if (resp.isSuccess) {
                withContext(Dispatchers.Main)
                {
                    if (type == 0) {
                        //仅保存
                        DialogUtils.showDialog(
                            ActivityUtils.getTopActivity(),
                            "商品保存成功",
                            "是否继续扫码？",
                            "查看商品",
                            "继续",
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
                        //保存上架
                        DialogUtils.showDialog(
                            ActivityUtils.getTopActivity(),
                            "商品上架成功",
                            "是否继续扫码？",
                            "查看商品",
                            "继续",
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
                    showToast("网络错误")
                }
            }

        }
    }
}







