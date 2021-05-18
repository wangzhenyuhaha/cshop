package com.lingmiao.shop.business.goods.presenter.impl

import android.app.Activity
import android.content.Context
import android.view.View
import com.lingmiao.shop.business.goods.GoodsPublishActivity
import com.lingmiao.shop.business.goods.api.GoodsRepository
import com.lingmiao.shop.business.goods.api.bean.GoodsSkuVOWrapper
import com.lingmiao.shop.business.goods.api.bean.GoodsVO
import com.lingmiao.shop.business.goods.api.request.QuantityRequest
import com.lingmiao.shop.business.goods.pop.GoodsMenuPop
import com.lingmiao.shop.business.goods.pop.GoodsMultiQuantityPop
import com.lingmiao.shop.business.goods.pop.GoodsQuantityPop
import com.james.common.base.BasePreImpl
import com.james.common.base.BaseView
import com.james.common.utils.DialogUtils
import com.james.common.utils.exts.check
import com.lingmiao.shop.business.goods.GoodsPublishNewActivity
import kotlinx.coroutines.launch

/**
 * Author : Elson
 * Date   : 2020/7/15
 * Desc   : 商品菜单管理
 */
class GoodsMenuPreImpl(var context: Context, var view: BaseView) : BasePreImpl(view) {

    private var mGoodsMenuPop: GoodsMenuPop? = null

    fun showMenuPop(menuTypes: Int, view: View, listener: ((Int) -> Unit)?) {
        mGoodsMenuPop = GoodsMenuPop(context, menuTypes)
        mGoodsMenuPop!!.setOnClickListener(listener)
        mGoodsMenuPop?.showPopupWindow(view)
    }

    // ---------------------------- 商品编辑------------------------------

    fun clickEditGoods(context: Context, goodsVO: GoodsVO?) {
        // 进入商品编辑页面
        GoodsPublishNewActivity.openActivity(context, goodsVO?.goodsId)
    }

    // ---------------------------- 商品下架 ------------------------------

    fun clickDisableGoods(goodsId: String?, callback: () -> Unit) {
        if (context !is Activity) {
            return
        }
        mGoodsMenuPop?.dismiss()
        DialogUtils.showDialog(context as Activity,
            "下架提示", "确定要下架该商品吗？",
            "取消", "确定下架",
            null, View.OnClickListener {
                exeGoodsDisableRequest(goodsId, callback)
            })
    }

    /**
     * 商品下架
     */
    private fun exeGoodsDisableRequest(goodsId: String?, callback: () -> Unit) {
        if (goodsId.isNullOrBlank()) {
            return
        }
        mCoroutine.launch {
            val resp = GoodsRepository.makeGoodsDisable(goodsId)
            handleResponse(resp) {
                view.showToast("下架成功")
                callback.invoke()
            }
        }
    }

    // ---------------------------- 商品上架 ------------------------------

    fun clickEnableGoods(goodsId: String?, callback: () -> Unit) {
        if (context !is Activity) {
            return
        }
        mGoodsMenuPop?.dismiss()
        DialogUtils.showDialog(context as Activity,
            "上架提示", "确定要上架该商品吗？",
            "取消", "确定上架",
            null, View.OnClickListener {
                exeGoodsEnableRequest(goodsId, callback)
            })
    }


    /**
     * 商品上架
     */
    private fun exeGoodsEnableRequest(goodsId: String?, callback: () -> Unit) {
        if (goodsId.isNullOrBlank()) {
            return
        }
        mCoroutine.launch {
            val resp = GoodsRepository.makeGoodsEnable(goodsId)
            handleResponse(resp) {
                view.showToast("上架成功")
                callback.invoke()
            }
        }
    }


    // ---------------------------- 商品删除 ------------------------------

    fun clickDeleteGoods(goodsId: String?, callback: () -> Unit) {
        if (context !is Activity) {
            return
        }
        mGoodsMenuPop?.dismiss()
        DialogUtils.showDialog(context as Activity,
            "删除提示", "删除后不可恢复，确定要删除该商品吗？",
            "取消", "确定删除",
            null, View.OnClickListener {
                exeGoodsDeleteRequest(goodsId, callback)
            })
    }

    /**
     * 删除商品
     */
    private fun exeGoodsDeleteRequest(goodsId: String?, callback: () -> Unit) {
        if (goodsId.isNullOrBlank()) {
            return
        }
        mCoroutine.launch {
            val resp = GoodsRepository.deleteGoods(goodsId)
            handleResponse(resp) {
                callback.invoke()
            }
        }
    }

    // ---------------------------- 库存更新 ------------------------------

    fun clickQuantityGoods(goodsId: String?, callback: (String) -> Unit) {
        exeGoodsSku(goodsId) {
            if (it.size == 1) {
                showSingleQuantityPop(goodsId!!, it[0], callback)
            } else {
                showMultiQuantityPop(goodsId!!, it, callback)
            }
        }
    }

    private fun exeGoodsSku(goodsId: String?, callback: (List<GoodsSkuVOWrapper>) -> Unit) {
        if (goodsId.isNullOrBlank()) {
            return
        }
        mCoroutine.launch {
            val skuResp = GoodsRepository.loadGoodsSKU(goodsId)
            if (!skuResp.isSuccess || skuResp.data.isNullOrEmpty()) {
                handleErrorMsg(skuResp.msg)
                return@launch
            }
            callback.invoke(skuResp.data)
        }
    }

    /**
     * 修改单个规格的库存
     */
    private fun showSingleQuantityPop(goodsId: String, skuVO: GoodsSkuVOWrapper, callback: (String) -> Unit) {
        val singleQuantityPop = GoodsQuantityPop(context)
        singleQuantityPop.setQuantity(skuVO.quantity)
        singleQuantityPop.setConfirmListener {
            if (it.isNullOrBlank()) {
                 view.showToast("请输入库存数量")
                return@setConfirmListener
            }
            val request = QuantityRequest().apply {
                skuId = skuVO.skuId
                quantityCount = it
            }
            exeQuantityUpdateRequest(goodsId, arrayListOf(request)) {
                singleQuantityPop.dismiss()
                callback.invoke(it)
            }
        }
        singleQuantityPop.showPopupWindow()
    }

    /**
     * 修改多规格库存
     */
    private fun showMultiQuantityPop(goodsId: String, skuList: List<GoodsSkuVOWrapper>, callback: (String) -> Unit) {
        val multiQuantityPop = GoodsMultiQuantityPop(context)
        multiQuantityPop.setConfirmListener {
            if (it.isNullOrEmpty()) {
                 view.showToast("请输入库存数量")
                return@setConfirmListener
            }
            exeQuantityUpdateRequest(goodsId, it) {
                multiQuantityPop.dismiss()
                callback.invoke(it)
            }
        }
        multiQuantityPop.apply {
            setSkuList(skuList)
            showPopupWindow()
        }
    }

    /**
     * 更新库存
     */
    private fun exeQuantityUpdateRequest(goodsId: String, skuList: List<QuantityRequest>, callback: (String) -> Unit) {
        mCoroutine.launch {
            val resp = GoodsRepository.updateGoodsQuantity(goodsId, skuList)
            handleResponse(resp) {
                callback.invoke(it.quantity.check("0"))
            }
        }
    }

}