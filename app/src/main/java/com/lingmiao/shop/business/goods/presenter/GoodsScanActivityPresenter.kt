package com.lingmiao.shop.business.goods.presenter

import com.google.zxing.BarcodeFormat
import com.james.common.base.BasePresenter
import com.james.common.base.BaseView
import com.lingmiao.shop.business.goods.api.bean.GoodsVOWrapper
import com.lingmiao.shop.business.goods.api.bean.ScanGoods

interface GoodsScanActivityPresenter : BasePresenter {

    //添加条形码扫描记录
    fun addGoodsSkuBarCodeLog(goods_id: Int, bar_code: String, url: String)

    fun getBarcodeFormats(): Collection<BarcodeFormat>

    //查询商品
    fun search(id: String)

    fun loadGoodsInfoFromCenter(id: String)

    //设置商品分类
    fun showCategoryPop()

    //设置商品菜单
    fun showGroupPop()


    interface View : BaseView {

        fun onScanSearchSuccess(data: ScanGoods)

        fun onScanSearchFailed()

        //从中心库成功加载到商品
        fun onLoadGoodsSuccess(goodsVO: GoodsVOWrapper, center: Boolean)

        //切换分类
        fun onUpdateCategory(categoryId: String?, categoryName: String?)

        //设置菜单
        fun onUpdateGroup(groupId: String?, groupName: String?)

    }
}