package com.lingmiao.shop.business.goods.presenter.impl

import com.lingmiao.shop.business.goods.api.GoodsRepository
import com.lingmiao.shop.business.goods.api.bean.GoodsSkuVOWrapper
import com.lingmiao.shop.business.goods.api.bean.SpecKeyVO
import com.lingmiao.shop.business.goods.api.bean.SpecValueVO
import com.lingmiao.shop.business.goods.presenter.SpecSettingPre
import com.james.common.base.BasePreImpl
import kotlinx.coroutines.launch


/**
 * Author : Elson
 * Date   : 2020/8/13
 * Desc   : 规格设置页
 */
class SpecSettingPreImpl(val view: SpecSettingPre.SpceSettingView) : BasePreImpl(view),
    SpecSettingPre {

    override fun submitSpecValue(specKeyId: String, valueNames: String) {
        mCoroutine.launch {
            view.showDialogLoading()
            val resp = GoodsRepository.submitSpceValues(specKeyId, valueNames)
            handleResponse(resp) {
                view.onAddSpecValueSuccess(specKeyId, resp.data)
            }
            view.hideDialogLoading()
        }
    }

    override fun getSpecKeyList(oldList: List<GoodsSkuVOWrapper>, specList: List<SpecKeyVO>) {
        // 1.通过笛卡尔算法生成 SpecValueList
        val specValueList = mutableListOf<List<SpecValueVO>>()
        descartes(specList, specValueList, 0, mutableListOf())

        // 有规格名，但是没有规格值时，会出现一个空集合，需要过滤掉。
        if (specValueList.size == 1 && specValueList[0].isNullOrEmpty()) {
            specValueList.clear()
        }

        // 2.生成 SkuList，并将 SpecValueList 赋值给 SkuVO。
        val skuList = mutableListOf<GoodsSkuVOWrapper>()
        specValueList.forEach{
            skuList.add(GoodsSkuVOWrapper().apply { addSpecList(it) })
        }

        // 3.将相同 SpecValue 值的数据保留 (规格值的个数不一致时，说明新增了一种规格类型，故不保留数据)
        if (oldList.isNotEmpty() && oldList.first().specList?.size == specList.first().valueList?.size) {
            skuList.forEachIndexed { index, newSku ->
                oldList.forEach { oldSku ->
                    if (oldSku.skuIds == newSku.skuIds) {
                        skuList[index] = oldSku
                    }
                }
            }
        }
        view.onLoadSkuListSuccess(skuList)
    }

    override fun loadSpecKeyList(goodsId: String?) {
        // 编辑商品的场景下，goodsId 不为空
        if (goodsId.isNullOrBlank()) {
            return
        }
        mCoroutine.launch {
            val resp = GoodsRepository.loadGoodsAppSku(goodsId)
            handleResponse(resp) {
                it.skuList?.forEach {
                    it.generateSpecNameAndId()
                }
                view.onLoadCacheSkuListSuccess(it)
            }
        }
    }

    /**
     * 笛卡尔乘积算法
     */
    private fun descartes(
        dimvalue: List<SpecKeyVO>,
        result: MutableList<List<SpecValueVO>>,
        layer: Int,
        curList: MutableList<SpecValueVO>
    ) {
        if (layer < dimvalue.size - 1) {
            if (dimvalue[layer].valueList.isNullOrEmpty()) {
                descartes(dimvalue, result, layer + 1, curList)
            } else {
                dimvalue[layer].valueList?.forEach {
                    val list: MutableList<SpecValueVO> = mutableListOf()
                    list.addAll(curList)
                    list.add(it)
                    descartes(dimvalue, result, layer + 1, list)
                }
            }
        } else if (layer == dimvalue.size - 1) {
            if (dimvalue[layer].valueList.isNullOrEmpty()) {
                result.add(curList)
            } else {
                dimvalue[layer].valueList?.forEach {
                    val list: MutableList<SpecValueVO> = mutableListOf()
                    list.addAll(curList)
                    list.add(it)
                    result.add(list)
                }
            }
        }
    }
}