package com.lingmiao.shop.business.goods.presenter.impl

import android.content.Context
import android.util.Log
import com.james.common.base.BasePreImpl
import com.james.common.base.BaseView
import com.lingmiao.shop.business.goods.api.GoodsRepository
import com.lingmiao.shop.business.goods.api.bean.GoodsSkuVOWrapper
import com.lingmiao.shop.business.goods.api.request.PriceAndQuantity
import com.lingmiao.shop.business.goods.api.request.QuantityPriceRequest
import com.lingmiao.shop.business.goods.pop.GoodsQuantityPricePop
import kotlinx.coroutines.launch

/**
Create Date : 2021/6/111:43 AM
Auther      : Fox
Desc        :
 **/
class QuantityPricePreImpl(var context: Context, var view: BaseView) : BasePreImpl(view) {

    //调用该方法，弹出PopWindow
    //goodsId 商品的ID callback 无用
    fun clickQuantityGoods(goodsId: String?, callback: (String) -> Unit) {
        exeGoodsSku(goodsId) {
            //这里是返回的数据  skuResp.data
            //size 表示规格数量
            //GoodsSkuVOWrapper : GoodsSkuVO()
            //返回数据

            if (it.size == 1) {
                showQuantityPricePop(goodsId!!, arrayListOf(it[0]), callback)
            } else {
                showQuantityPricePop(goodsId!!, it, callback)
            }
        }
    }

    /**
     * 修改多规格库存，活动库存
     */
    //返回的SKU信息，skuNameDesc为空，skuIds为null，isChecked为false，isEditable为true,且List<GoodsSkuVOWrapper>只有一个
    private fun showQuantityPricePop(
        goodsId: String,
        skuList: List<GoodsSkuVOWrapper>,
        callback: (String) -> Unit
    ) {
        //skuList中的数据 skuNameDesc空       skuIds null            isChecked false                  isEditable true

        //初始化PopWindow,setConfirmListener()  setSkuList()
        val multiQuantityPop = GoodsQuantityPricePop(context, "活动价格/库存")

        //设置对确定按钮的监听，post数据
        multiQuantityPop.setConfirmListener {
            if (it.isNullOrEmpty()) {
                view.showToast("请输入价格及库存数量")
                return@setConfirmListener
            }
            exeQuantityPriceRequest(goodsId, it) { skuList ->

                multiQuantityPop.dismiss()
                //useless
                callback.invoke(skuList)
            }
        }


        multiQuantityPop.apply {
            //传入数据
            setSkuList(skuList)
            showPopupWindow()
        }
    }

    /**
     * 更新活动库存
     */
    private fun exeQuantityPriceRequest(
        goodsId: String,
        skuList: List<QuantityPriceRequest>,
        callback: (String) -> Unit
    ) {

        //转换数据类
        val temp = PriceAndQuantity()
        temp.quantityList = mutableListOf()
        temp.eventQuantityList = mutableListOf()
        for (i in skuList) {
            temp.quantityList!!.add(PriceAndQuantity.QuantityPrice(i.skuId, i.price, i.quantity))
            temp.eventQuantityList!!.add(
                PriceAndQuantity.QuantityPrice(
                    i.skuId,
                    i.eventPrice,
                    i.eventQuantity
                )
            )
        }

        //更新
        mCoroutine.launch {
            val resp = GoodsRepository.updateGoodsQuantityAndPrice(goodsId, temp)
            handleResponse(resp) {
                callback.invoke(goodsId)
            }
        }
    }


    //获取指定商品对应的sku信息,包括商品的价格等信息，因为商品价格不止一种，返回List<GoodsSkuVOWrapper>
    private fun exeGoodsSku(goodsId: String?, callback: (List<GoodsSkuVOWrapper>) -> Unit) {
        if (goodsId.isNullOrBlank()) {
            return
        }
        mCoroutine.launch {
            //seller/goods/{goods_id}/skus
            //商品sku信息信息获取api
            val skuResp = GoodsRepository.loadGoodsSKU(goodsId)
            if (!skuResp.isSuccess || skuResp.data.isNullOrEmpty()) {
                handleErrorMsg(skuResp.msg)
                return@launch
            }
            callback.invoke(skuResp.data)
        }
    }


}