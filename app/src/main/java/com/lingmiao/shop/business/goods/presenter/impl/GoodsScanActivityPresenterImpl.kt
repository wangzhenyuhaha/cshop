package com.lingmiao.shop.business.goods.presenter.impl

import com.blankj.utilcode.util.ActivityUtils
import com.google.zxing.BarcodeFormat
import com.james.common.base.BasePreImpl
import com.lingmiao.shop.base.UserManager
import com.lingmiao.shop.business.goods.api.GoodsRepository
import com.lingmiao.shop.business.goods.presenter.GoodsScanActivityPresenter
import kotlinx.coroutines.launch

class GoodsScanActivityPresenterImpl(val view: GoodsScanActivityPresenter.View) : BasePreImpl(view),
    GoodsScanActivityPresenter {

    var shopId: Int? = null

    fun getSellerId(): Int? {
        if (shopId == null) {
            shopId = UserManager.getLoginInfo()?.shopId
        }
        return shopId
    }


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

    //根据商品名称从中心库搜索商品,从中心库拿到的菜单，分类设为null，暂时只能手动选择
    override fun loadGoodsInfoFromCenter(id: String) {
        view.showDialogLoading()
        mCoroutine.launch {
            val resp = GoodsRepository.searchGoodsFromCenter(id)
            handleResponse(resp) {
                view.onLoadGoodsSuccess(resp.data,true)
            }
            view.hideDialogLoading()
        }
    }

    //选择分类
    private val mCategoryPreImpl: PopCategoryPreImpl by lazy { PopCategoryPreImpl(view) }
    override fun showCategoryPop() {
        mCategoryPreImpl.showCategoryPop(ActivityUtils.getTopActivity(), getSellerId()!!) { cate, names ->
            view.onUpdateCategory(cate?.categoryId, names)
        }
    }

    //选择商品菜单
    private val mGroupPreImpl: PopGroupPreImpl by lazy { PopGroupPreImpl(view) }
    override fun showGroupPop() {
        mGroupPreImpl.showGoodsGroupPop(ActivityUtils.getTopActivity()) { group, names ->
            view.onUpdateGroup(group?.shopCatId, names)
        }
    }
}