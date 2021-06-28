package com.lingmiao.shop.business.goods.presenter.impl

import android.content.Context
import com.blankj.utilcode.util.LogUtils
import com.lingmiao.shop.business.goods.api.GoodsRepository
import com.lingmiao.shop.business.goods.api.bean.GoodsSkuVOWrapper
import com.lingmiao.shop.business.goods.api.bean.SpecKeyVO
import com.lingmiao.shop.business.goods.api.bean.SpecValueVO
import com.lingmiao.shop.business.goods.presenter.SpecSettingPre
import com.james.common.base.BasePreImpl
import com.lingmiao.shop.business.tools.api.JsonUtil
import kotlinx.coroutines.launch


/**
 * Author : Elson
 * Date   : 2020/8/13
 * Desc   : 规格设置页
 */
class SpecSettingPreImpl(val context: Context, val view: SpecSettingPre.SpceSettingView) : BasePreImpl(view),
    SpecSettingPre {

    private val mItemPreImpl: ChildrenCatePopPreImpl<SpecValueVO> by lazy { ChildrenCatePopPreImpl<SpecValueVO>(view) }

    companion object {

        const val TAG ="SpecSetting";
    }

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
        specValueList.forEach{ _item ->
            skuList.add(GoodsSkuVOWrapper().apply { addSpecList(_item) })
        }

        // 3.将相同 SpecValue 值的数据保留 (规格值的个数不一致时，说明新增了一种规格类型，故不保留数据)
        if(specList.isEmpty() || specList.first() == null) {
            return;
        }
        if(oldList.isEmpty() ||oldList.first() == null) {
            view.onLoadSkuListSuccess(skuList)
            return;
        }
        var _temp : List<SpecValueVO>?;
        var _tempIt : List<GoodsSkuVOWrapper>?;
        skuList.forEachIndexed { index, newSku ->
            LogUtils.dTag(TAG, "item forEach =${JsonUtil.instance.toJson(newSku)} ")
            oldList.forEach { oldSku ->
                if (oldSku.skuIds == newSku.skuIds) {
                    skuList[index] = oldSku
                    LogUtils.dTag(TAG, "item id ${oldSku.skuIds} 已存在 value = ${JsonUtil.instance.toJson(skuList[index])}")
                } else {
                    LogUtils.dTag(TAG, "item new = ${JsonUtil.instance.toJson(newSku)}")
                    _temp = oldSku.specList?.filter { _it->newSku.skuIds?.indexOf(_it.specValueId!!)?:0>-1 };
                    if(_temp?.isNotEmpty() == true) {
                        _tempIt = oldList.filter { _oit-> _oit.skuIds?.indexOf(_temp?.get(0)?.specValueId!!)?:0>-1};
                        LogUtils.dTag(TAG, "item id ${JsonUtil.instance.toJson(_temp?.get(0)!!)}")
                        if(_tempIt?.isNotEmpty() == true && _tempIt?.get(0) != null) {
                            newSku.convert(_tempIt?.get(0)!!);
                        }
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

    override fun loadSpecListByCid(cid: String?) {
        if (cid.isNullOrBlank()) {
            return
        }
        mCoroutine.launch {
            val resp = GoodsRepository.loadSpecKey(cid)
            if (resp.isSuccess) {
                view.onLoadedSpecListByCid(resp.data);
//                val list = mutableListOf<MultiItemEntity>()
//                restoreSelectStatus(resp.data, specList)
//                list.addAll(resp.data)
//                list.add(GoodsSpecKeyAddVO())
//                view.onLoadSuccess(list)
            } else {
                //ToastUtils.showShort("网络异常，请重试")
            }
        }
    }

    fun loadSpecListByCid(cid: String?, call: (List<SpecKeyVO>?)->Unit, fail:(String?)->Unit) {
        if (cid.isNullOrBlank()) {
            return
        }
        mCoroutine.launch {
            val resp = GoodsRepository.getSpecList(cid)
            if (resp.isSuccess) {
                call?.invoke(resp.data);
            } else {
                fail?.invoke(resp.msg);
            }
        }
    }

    private var mCateKeyList : HashMap<String, List<SpecValueVO>> = hashMapOf();

    override fun showAddOldKey(cid : String?, keyId : String?, list: List<SpecValueVO>?) {
        if(mCateKeyList.containsKey(cid)) {
            show(keyId, mCateKeyList?.get(cid));
        } else {
            loadSpecListByCid(cid, { list ->
                list?.forEachIndexed { index, specKeyVO ->
                    if(specKeyVO?.specId?.isNotEmpty() == true && specKeyVO?.valueList?.isNotEmpty() == true) {
                        mCateKeyList?.put(specKeyVO.specId!!, specKeyVO.valueList!!);
                    }
                }
                val filter = list?.filter { it->it.specId == keyId };
                val value = if(filter?.size?:0 > 0) filter?.get(0) else null;
                if(value == null || value?.valueList?.isEmpty() == true) {
                    return@loadSpecListByCid;
                }
                mCateKeyList.put(keyId!!, value.valueList!!);
                show(keyId, value.valueList);
            }, { str ->

            });
        }
    }

    fun show(key : String?,_vList : List<SpecValueVO>?) {
        mItemPreImpl.showPop(context, "", _vList) { _list ->
            if(_list?.isNotEmpty() == true) {
                view.onAddSpecValueSuccess(key!!, _list);
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